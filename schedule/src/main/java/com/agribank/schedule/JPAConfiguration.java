package com.agribank.schedule;


import com.agribank.schedule.dto.LoginUser;
import com.agribank.schedule.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JPAConfiguration {
	@Bean
	AuditorAware<User> auditorProvider() {
		return new AuditorAware<User>() {
			@Override
			public Optional<User> getCurrentAuditor() {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
					LoginUser currentUser = (LoginUser) auth.getPrincipal();

					User user = new User();
					user.setId(currentUser.getId());

					return Optional.ofNullable(user);
				}
				return Optional.ofNullable(null);

			}
		};
	}
}
