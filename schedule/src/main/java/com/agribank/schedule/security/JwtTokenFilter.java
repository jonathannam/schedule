package com.agribank.schedule.security;


import com.agribank.schedule.dto.LoginUser;
import com.agribank.schedule.service.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtTokenService jwtTokenService;

	// chỉ nên check các path cần security ( cần login )
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {
		// Đọc token từ header
		String token = resolveToken(httpServletRequest);

		// verify token
		if (token != null) {
			LoginUser loginUser = jwtTokenService.getLoginUser(token);
			if (loginUser != null) {
				Authentication auth = new UsernamePasswordAuthenticationToken(loginUser, "",
						loginUser.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);
			} else {
				SecurityContextHolder.clearContext();
			}
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	// Lấy token từ request gửi lên
	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
		log.info("bearer Token: " + bearerToken);

		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return null;
	}
}