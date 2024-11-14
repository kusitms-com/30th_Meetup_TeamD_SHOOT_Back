package gigedi.dev.domain.discord.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.discord.dao.DiscordRepository;
import gigedi.dev.domain.discord.domain.Discord;
import gigedi.dev.domain.discord.dto.response.*;
import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.global.error.exception.CustomException;
import gigedi.dev.global.error.exception.ErrorCode;
import gigedi.dev.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DiscordAuthService {
    private final MemberUtil memberUtil;
    private final ImageConverter converter;
    private final DiscordAuthApiService discordAuthApiService;
    private final DiscordGuildApiService discordGuildApiService;
    private final DiscordDmApiService discordDmApiService;
    private final DiscordRepository discordRepository;

    public DiscordInfoResponse discordConnect(String code) {
        DiscordLoginResponse loginResponse = discordAuthApiService.discordLogin(code);
        DiscordUserResponse userInfo =
                discordAuthApiService.getDiscordUserInfo(loginResponse.accessToken());

        discordGuildApiService.updateGuildInfo(
                loginResponse.getGuildId(), converter.getShootImageBase64Encoded());
        CreateDMChannelResponse dmChannel = discordDmApiService.createDMChannel(userInfo.id());

        return saveDiscord(loginResponse, userInfo, dmChannel);
    }

    private DiscordInfoResponse saveDiscord(
            DiscordLoginResponse loginResponse,
            DiscordUserResponse userInfo,
            CreateDMChannelResponse dmChannel) {
        Member currentMember = memberUtil.getCurrentMember();
        Discord discord =
                Discord.createDiscord(
                        currentMember,
                        userInfo.email(),
                        loginResponse.refreshToken(),
                        userInfo.id(),
                        dmChannel.id(),
                        loginResponse.getGuildId());
        return DiscordInfoResponse.from(discordRepository.save(discord));
    }

    public void discordDisconnect(Long discordId) {
        Discord discordById = findDiscordById(discordId);
        ReissueDiscordTokenResponse tokenResponse =
                discordAuthApiService.reissueDiscordToken(discordById.getRefreshToken());
        discordById.updateRefreshToken(tokenResponse.refreshToken());

        discordAuthApiService.disconnectDiscordAccount(tokenResponse.accessToken());
        discordById.disconnectDiscordAccount();
    }

    private Discord findDiscordById(Long discordId) {
        Member currentMember = memberUtil.getCurrentMember();
        Discord discord =
                discordRepository
                        .findById(discordId)
                        .orElseThrow(
                                () -> new CustomException(ErrorCode.DISCORD_ACCOUNT_NOT_FOUND));
        if (!discord.getMember().equals(currentMember)) {
            throw new CustomException(ErrorCode.DISCORD_ACCOUNT_NOT_OWNER);
        }
        return discord;
    }
}
