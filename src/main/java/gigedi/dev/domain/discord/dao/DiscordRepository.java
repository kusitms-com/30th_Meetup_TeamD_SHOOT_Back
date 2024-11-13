package gigedi.dev.domain.discord.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gigedi.dev.domain.discord.domain.Discord;

@Repository
public interface DiscordRepository extends JpaRepository<Discord, Long> {}
