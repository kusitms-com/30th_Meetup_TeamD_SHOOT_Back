package gigedi.dev.domain.auth.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gigedi.dev.domain.auth.application.AuthService;
import gigedi.dev.domain.auth.dto.request.TokenRefreshRequest;
import gigedi.dev.domain.auth.dto.response.TokenPairResponse;
import gigedi.dev.domain.auth.dto.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth", description = "Auth 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "구글 소셜 로그인", description = "회원가입 및 로그인을 진행하는 API")
    @GetMapping("/code/google")
    public TokenPairResponse googleSocialLogin(@RequestParam String code) {
        return authService.googleSocialLogin(code);
    }

    @Operation(summary = "피그마 소셜 로그인", description = "회원가입 및 로그인을 진행하는 API")
    @GetMapping("/code/figma")
    public UserInfoResponse figmaSocialLogin(@RequestParam String code) {
        return authService.figmaSocialLogin(code);
    }

    @Operation(summary = "토큰 재발급", description = "엑세스 토큰 및 리프테시 토큰을 모두 재발급합니다.")
    @PostMapping("/refresh")
    public TokenPairResponse refreshToken(@RequestBody TokenRefreshRequest request) {
        return authService.refreshToken(request);
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 진행하는 API")
    @PostMapping("/logout")
    public ResponseEntity<Void> memberLogout() {
        authService.memberLogout();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원탈퇴", description = "회원탈퇴를 진행하는 API")
    @GetMapping("/withdrawal")
    public ResponseEntity<Void> memberWithdrawal() {
        authService.memberWithdrawal();
        return ResponseEntity.ok().build();
    }
}
