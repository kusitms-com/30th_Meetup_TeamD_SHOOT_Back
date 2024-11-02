package gigedi.dev.domain.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import gigedi.dev.domain.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {}
