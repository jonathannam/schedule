//package com.agribank.schedule.scheduler;
//
//
//import com.agribank.schedule.entity.ScheduleUser;
//import com.agribank.schedule.repository.ScheduleUserRepository;
//import com.agribank.schedule.utils.StatusEnum;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//@Component
//public class ScheduleUserJob {
//
//	@Autowired
//	ScheduleUserRepository scheduleUserRepository;
//
//	@Scheduled(fixedDelay = 1000 * 60 * 60)
//	public void sendAdminEmail() {
//		// chi gio hien tai - 60 phut
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.MINUTE, -60);
//		Date date = cal.getTime();
//
//		List<ScheduleUser> scheduleUsers = scheduleUserRepository.find(date, StatusEnum.HOLD);
//
//		for (ScheduleUser s : scheduleUsers) {
//			s.setStatus(StatusEnum.ACTIVE);
//			scheduleUserRepository.save(s);
//		}
//	}
//}
