package com.agribank.schedule.service;


import com.agribank.schedule.dto.LoginUser;
import com.agribank.schedule.entity.User;
import com.agribank.schedule.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoginService implements UserDetailsService {
	@Autowired
	UserRepo userRepo;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username).orElse(null);
		// nguồn xác thực đọc từ Database
		if (user == null) {
			throw new UsernameNotFoundException("User Not Found");
		}

		Set<String> privileges = user.getRole().getPrivileges().stream().map(p -> p.getAuthority())
				.collect(Collectors.toSet());

		privileges.add(user.getRole().getName());

		List<SimpleGrantedAuthority> list = privileges.stream().map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		// tạo user của Security
		LoginUser currentUser = new LoginUser(username, user.getPassword(), user.getEnabled(), true, true, true, list);
		currentUser.setId(user.getId());
		currentUser.setName(user.getName());

		return currentUser;
	}
}
