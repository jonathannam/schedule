package com.agribank.schedule.scheduler;

import jmaster.io.simservice.repository.RefreshTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class TokenJob {

	@Autowired
	RefreshTokenRepo refreshTokenRepo;

	@Scheduled(cron = "0 0 0 * * *")
	public void deleteExpireTokens() {
		// chi gio hien tai - 60 phut
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -30);
		Date date = cal.getTime();

		refreshTokenRepo.deleteExpiredToken(date);
	}
}
