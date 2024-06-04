package com.agribank.schedule.service;

import com.agribank.schedule.dto.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtTokenService {
	@Value("${jwt.secret:123456}")
	private String secretKey;

	@Value("${jwt.validity:60}") // 60 put
	private long validity;

	public String createToken(LoginUser loginUser) {
		List<String> roles = loginUser.getAuthorities().stream().map(a -> a.getAuthority())
				.collect(Collectors.toList());

		Claims claims = Jwts.claims().setSubject(loginUser.getUsername());
		claims.put("roles", roles);
		claims.put("uid", loginUser.getId());

		Date now = new Date();
		Date expiredTime = new Date(now.getTime() + validity * 60 * 1000);

		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(expiredTime)
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			log.info("err", e);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public LoginUser getLoginUser(String token) {
		try {
			String username = getClaims(token).getSubject();
			Integer uid = getClaims(token).get("uid", Integer.class);
			List<String> roles = getClaims(token).get("roles", List.class);

			List<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role))
					.collect(Collectors.toList());

			LoginUser loginUser = new LoginUser(username, "", authorities);
			loginUser.setId(uid);

			return loginUser;
		} catch (Exception e) {
			log.info("err", e);
			return null;
		}
	}

	public Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

}
