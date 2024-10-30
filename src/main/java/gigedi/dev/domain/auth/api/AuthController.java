package gigedi.dev.domain.auth.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gigedi.dev.domain.auth.application.AuthService;
import gigedi.dev.domain.auth.dto.response.TokenPairResponse;
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
}
