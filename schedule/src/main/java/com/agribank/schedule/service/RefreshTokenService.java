package com.agribank.schedule.service;


import com.agribank.schedule.dto.LoginUser;
import com.agribank.schedule.entity.RefreshToken;
import com.agribank.schedule.repository.RefreshTokenRepo;
import com.agribank.schedule.repository.UserRepo;
import com.agribank.schedule.security.TokenRefreshException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {
	@Value("${jwt.refresh.validity:60}")
	private Long validity;

	@Autowired
	private RefreshTokenRepo refreshTokenRepository;

	@Autowired
	private UserRepo userRepository;

	@Autowired
	private LoginService loginService;

	@Transactional
	public String createRefreshToken(Integer userId) {
		RefreshToken refreshToken = new RefreshToken();

		refreshToken.setUser(userRepository.findById(userId).orElseThrow(NoResultException::new));

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 60 * 24 * 30);// 30 ngay
		refreshToken.setExpiryDate(cal.getTime());

		refreshToken.setToken(UUID.randomUUID().toString());

		refreshTokenRepository.deleteByUserId(userId);
		refreshTokenRepository.save(refreshToken);
		return refreshToken.getToken();
	}

	@Transactional
	public LoginUser getLoginUser(String token) {
		RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(NoResultException::new);

		if (refreshToken.getExpiryDate().compareTo(new Date()) < 0) {
			refreshTokenRepository.delete(refreshToken);
			throw new TokenRefreshException(token, "Refresh token was expired. Please make a new signin request");
		}

		return (LoginUser) loginService.loadUserByUsername(refreshToken.getUser().getUsername());
	}

}
