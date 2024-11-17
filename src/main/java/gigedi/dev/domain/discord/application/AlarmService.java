package gigedi.dev.domain.discord.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gigedi.dev.domain.discord.domain.Discord;
import gigedi.dev.domain.discord.dto.response.AlarmFileResponse;
import gigedi.dev.domain.discord.dto.response.GetAlarmFileListResponse;
import gigedi.dev.domain.file.application.AuthorityService;
import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final DiscordService discordService;
    private final AuthorityService authorityService;
    private final MemberUtil memberUtil;

    public GetAlarmFileListResponse getAlarmFileList() {
        Member currentMember = memberUtil.getCurrentMember();
        Discord connectedDiscord = discordService.findConnectedDiscord();
        List<AlarmFileResponse> alarmList =
                authorityService.getRelatedAuthorityList(currentMember.getId()).stream()
                        .map(AlarmFileResponse::from)
                        .collect(Collectors.toList());
        return new GetAlarmFileListResponse(connectedDiscord.getEmail(), alarmList);
    }
}
