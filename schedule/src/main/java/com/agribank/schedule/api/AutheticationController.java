package com.agribank.schedule.api;


import com.agribank.schedule.dto.*;
import com.agribank.schedule.service.JwtTokenService;
import com.agribank.schedule.service.LoginService;
import com.agribank.schedule.service.RefreshTokenService;
import com.agribank.schedule.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class AutheticationController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenService jwtTokenService;

	@Autowired
	UserService userService;

	@Autowired
	RefreshTokenService refreshTokenService;

	@Autowired
	LoginService loginService;

	@PostMapping("/signin")
	public ResponseDTO<TokenDTO> login(@RequestBody @Valid LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		LoginUser loginUser = (LoginUser) authentication.getPrincipal();

		return ResponseDTO.<TokenDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(TokenDTO.builder().accessToken(jwtTokenService.createToken(loginUser))
						.refreshToken(refreshTokenService.createRefreshToken(loginUser.getId()))
						.user(userService.get(loginUser.getId())).build())
				.build();
	}

	@PostMapping("/signup")
	public ResponseDTO<TokenDTO> signup(@RequestBody @Valid UserDTO userDTO) {
		userService.signup(userDTO);

		LoginUser loginUser = (LoginUser) loginService.loadUserByUsername(userDTO.getUsername());

		return ResponseDTO.<TokenDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(TokenDTO.builder().accessToken(jwtTokenService.createToken(loginUser))
						.refreshToken(refreshTokenService.createRefreshToken(loginUser.getId()))
						.user(userService.get(loginUser.getId())).build())
				.build();
	}

	@PostMapping("/refresh-token")
	public ResponseDTO<TokenDTO> login(@RequestParam("token") String refreshToken) {
		LoginUser loginUser = refreshTokenService.getLoginUser(refreshToken);

		return ResponseDTO
				.<TokenDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(TokenDTO.builder()
						.accessToken(jwtTokenService.createToken(loginUser)).refreshToken(refreshToken).build())
				.build();
	}

	@GetMapping("/me")
	public ResponseDTO<UserDTO> me(@AuthenticationPrincipal LoginUser loginUser) {
		return ResponseDTO.<UserDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(userService.get(loginUser.getId())).build();
	}

}
