package gigedi.dev.infra.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import gigedi.dev.infra.config.jwt.JwtProperties;
import gigedi.dev.infra.config.oauth.DiscordProperties;
import gigedi.dev.infra.config.oauth.GoogleProperties;
import gigedi.dev.infra.config.oauth.FigmaProperties;

@Configuration
@EnableConfigurationProperties({
    GoogleProperties.class,
    JwtProperties.class,
    DiscordProperties.class,
    FigmaProperties.class
})
