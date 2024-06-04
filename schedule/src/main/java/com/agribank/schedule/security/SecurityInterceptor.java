package com.agribank.schedule.security;


import com.agribank.schedule.dto.PrivilegeDTO;
import com.agribank.schedule.service.PrivilegeService;
import com.agribank.schedule.utils.RoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SecurityInterceptor implements HandlerInterceptor {

	@Autowired
	PrivilegeService privilegeService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("!!!!!!!!!!!!");
		log.debug(request.getServletPath());
		log.debug(request.getMethod());

		// if admin
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			List<String> authorities = authentication.getAuthorities().stream().map(g -> g.getAuthority())
					.collect(Collectors.toList());

			if (authorities.contains(RoleEnum.ADMIN.getRoleName())) {
				return true;
			}
		}

		List<PrivilegeDTO> privileges = privilegeService.findAll().stream().filter(p -> {
			Pattern pattern = Pattern.compile(p.getApi());
			Matcher matcher = pattern.matcher(request.getServletPath());
			if (matcher.matches() && p.getMethod().equals(request.getMethod())) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());

		for (PrivilegeDTO privilege : privileges) {
			if (!privilege.isSecured())
				return true;

			if (authentication instanceof UsernamePasswordAuthenticationToken) {
				if (privilege.isAuthenticated()) {
					return true;
				}

				List<String> authorities = authentication.getAuthorities().stream().map(g -> g.getAuthority())
						.collect(Collectors.toList());

				if (authorities.contains(privilege.getAuthority())) {
					return true;
				}
			}
		}

		throw new AccessDeniedException("Deny");
	}

}
