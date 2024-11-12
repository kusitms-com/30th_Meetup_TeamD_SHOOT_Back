package gigedi.dev.global.error.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SAMPLE_ERROR(HttpStatus.BAD_REQUEST, "Sample Error Message"),

    // Common
    METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "요청 한 값의 Type이 일치하지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP method 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류, 관리자에게 문의하세요"),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    MEMBER_INVALID_NORMAL(HttpStatus.FORBIDDEN, "일반 회원이 아닙니다."),
    MEMBER_SOCIAL_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "소셜 정보를 찾을 수 없습니다."),
    OAUTH_PROVIDER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "지원하지 않는 Oauth Provider입니다."),

    // Security
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "시큐리티 인증 정보를 찾을 수 없습니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    MISSING_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 정보가 존재하지 않습니다."),
    MEMBER_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 가입된 회원입니다."),
    MEMBER_ALREADY_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    MEMBER_INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "올바르지 않는 닉네임입니다."),
    MEMBER_ALREADY_DELETED(HttpStatus.NOT_FOUND, "이미 탈퇴한 회원입니다."),
    PASSWORD_NOT_MATCHES(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    ID_TOKEN_VERIFICATION_FAILED(HttpStatus.UNAUTHORIZED, "ID 토큰 검증에 실패했습니다."),

    // Archive
    ARCHIVE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 아카이브를 찾을 수 없습니다."),
    ARCHIVE_ALREADY_DELETED(HttpStatus.NOT_FOUND, "이미 삭제된 아카이브입니다."),
    ARCHIVE_INVALID_MEMBER(HttpStatus.FORBIDDEN, "해당 아카이브에 접근할 수 없는 회원입니다."),
    ARCHIVE_EXCEED_LIMIT(HttpStatus.BAD_REQUEST, "파일 내 아카이브 개수는 최대 20개입니다."),
    ARCHIVE_TITLE_EXCEED_LIMIT(HttpStatus.BAD_REQUEST, "아카이브의 제목 최대 길이는 30입니다."),
    ARCHIVE_NOT_EXIST_IN_FILE(HttpStatus.BAD_REQUEST, "파일에 해당 아카이브가 존재하지 않습니다."),

    // Block
    BLOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 블록을 찾을 수 없습니다."),
    BLOCK_EXCEED_LIMIT(HttpStatus.BAD_REQUEST, "아카이브 내 블록 개수는 최대 20개입니다."),

    // Figma
    FIGMA_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 Figma 정보를 찾을 수 없습니다."),

    // 추가
    GOOGLE_LOGIN_FAILED(HttpStatus.BAD_REQUEST, "구글 로그인 과정에서 오류가 발생했습니다."),
    GOOGLE_AUTH_NOT_FOUND(HttpStatus.BAD_REQUEST, "구글 리프레시 토큰이 존재하지 않습니다."),
    GOOGLE_TOKEN_REISSUE_FAILED(HttpStatus.BAD_REQUEST, "구글 토큰 재발급에 실패했습니다."),
    GOOGLE_WITHDRAWAL_FAILED(HttpStatus.BAD_REQUEST, "구글 회원탈퇴에 실패했습니다."),

    FIGMA_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "피그마 정보가 포함되지 않았습니다."),
    FIGMA_NOT_CONNECTED(HttpStatus.BAD_REQUEST, "피그마 계정이 연결되지 않았습니다."),
    UNAUTHORIZED_FIGMA_ACCESS(HttpStatus.BAD_REQUEST, "해당 사용자와 피그마 계정이 연결되지 않았습니다.");

    private final HttpStatus status;
    private final String message;
}
