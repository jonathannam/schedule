package com.agribank.schedule;


import com.agribank.schedule.entity.Privilege;
import com.agribank.schedule.entity.Role;
import com.agribank.schedule.entity.User;
import com.agribank.schedule.repository.CategoryRepository;
import com.agribank.schedule.repository.PrivilegeRepository;
import com.agribank.schedule.repository.RoleRepo;
import com.agribank.schedule.repository.UserRepo;
import com.agribank.schedule.utils.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class DatabaseInitConfiguration implements ApplicationRunner {

	@Autowired
	private CategoryRepository categoryDao;


	@Autowired
	private RoleRepo roleDao;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	public void run(ApplicationArguments args) throws Exception {
		new Thread(() -> {
			ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
			taskScheduler.initialize();
			CountDownLatch latch = new CountDownLatch(1);
			taskScheduler.destroy();

			RoleEnum[] roleEnums = RoleEnum.class.getEnumConstants();
			for (int i = 0; i < roleEnums.length; i++) {
				Role role = roleDao.findById(roleEnums[i].getRoleId()).orElse(null);
				if (role == null) {
					role = new Role();
					role.setId(roleEnums[i].getRoleId());
					role.setName(roleEnums[i].getRoleName());
					roleDao.save(role);
				} else if (!role.getName().equals(roleEnums[i].getRoleName())) {
					role.setName(roleEnums[i].getRoleName());
					roleDao.save(role);
				}
			}

			// admin
			User user = userRepo.findByUsername("admin").orElse(null);
			if (user == null) {
				user = new User();
				user.setId(1);
				user.setName("ADMIN");
				user.setUsername("admin");
				user.setPassword(new BCryptPasswordEncoder().encode("123456"));
				user.setEnabled(true);

				Role role = new Role();
				role.setId(RoleEnum.ADMIN.getRoleId());
				user.setRole(role);

				userRepo.save(user);
			}

			Privilege privilege = privilegeRepository.findByAuthority("SIGNIN").orElse(null);

			if (privilege == null) {
				privilege = new Privilege();
				privilege.setApi("^/signin$");
				privilege.setAuthenticated(false);
				privilege.setAuthority("SIGNIN");
				privilege.setMethod(HttpMethod.POST);
				privilege.setSecured(false);
				
				privilegeRepository.save(privilege);
			}
			
			privilege = privilegeRepository.findByAuthority("MEDIA_DOWNLOAD").orElse(null);

			if (privilege == null) {
				privilege = new Privilege();
				privilege.setApi("^/media/download/.+$");
				privilege.setAuthenticated(false);
				privilege.setAuthority("MEDIA_DOWNLOAD");
				privilege.setMethod(HttpMethod.GET);
				privilege.setSecured(false);
				
				privilegeRepository.save(privilege);
			}
			
			privilege = privilegeRepository.findByAuthority("REFRESH_TOKEN").orElse(null);

			if (privilege == null) {
				privilege = new Privilege();
				privilege.setApi("^/refresh-token$");
				privilege.setAuthenticated(false);
				privilege.setAuthority("REFRESH_TOKEN");
				privilege.setMethod(HttpMethod.POST);
				privilege.setSecured(false);
				
				privilegeRepository.save(privilege);
			}

		}).start();
	}

}
