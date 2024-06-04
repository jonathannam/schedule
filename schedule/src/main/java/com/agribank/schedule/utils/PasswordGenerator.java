package com.agribank.schedule.utils;

import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;

public class PasswordGenerator {
	private static final int strength = 12;

	// BCryptPasswordEncoder method
	public static String encode(String input) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(strength);
		String hashedPassword = passwordEncoder.encode(input);

		return hashedPassword;
	}

	// BCryptPasswordEncoder method
	public static boolean checkPassword(String rawPassword, String encodedPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(strength);
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	// generate 6 characters 0-9 and a-zA-Z for password
	public static String generateRandomPassword() {
		final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdeghijklmnopqrstuvwxyz";
		final int N = alphabet.length();

		Random r = new Random();
		StringBuffer output = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			output.append(alphabet.charAt(r.nextInt(N)));
		}

		return output.toString();
	}

	public static void main(String[] args) throws JsonProcessingException {
		System.out.println(encode("123456"));
	}
}
