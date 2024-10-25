package gigedi.dev.domain.member.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gigedi.dev.domain.member.dao.MemberRepository;
import gigedi.dev.domain.member.domain.Member;
import gigedi.dev.domain.member.dto.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public List<MemberInfoResponse> findMemberInfo() {
        List<Member> members = memberRepository.findAll();
        return members.stream().map(MemberInfoResponse::new).collect(Collectors.toList());
    }
}
