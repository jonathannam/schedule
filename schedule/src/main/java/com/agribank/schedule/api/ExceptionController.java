package com.agribank.schedule.api;


import com.agribank.schedule.dto.ResponseDTO;
import com.agribank.schedule.security.TokenRefreshException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.NoResultException;
import java.util.List;

@RestControllerAdvice
public class ExceptionController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler({ NoResultException.class })
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public ResponseDTO<Void> noResult(NoResultException ex) {
		logger.info("ex: ", ex);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.NOT_FOUND.value())).message(ex.getMessage())
				.build();// view
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseDTO<Void> badInput(MethodArgumentNotValidException ex) {
		List<ObjectError> errors = ex.getBindingResult().getAllErrors();

		String msg = "";
		for (ObjectError e : errors) {
			FieldError fieldError = (FieldError) e;

			msg += fieldError.getField() + ":" + e.getDefaultMessage() + ";";
		}

		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.BAD_REQUEST.value())).message(msg).build();// view
	}

	@ExceptionHandler({ DataIntegrityViolationException.class })
	@ResponseStatus(code = HttpStatus.CONFLICT)
	public ResponseDTO<Void> conflict(Exception ex) {
		logger.info("ex: ", ex);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.CONFLICT.value()))
				.message(HttpStatus.CONFLICT.getReasonPhrase()).build();// view
	}

	@ExceptionHandler({ AccessDeniedException.class })
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public ResponseDTO<Void> forbidden(Exception ex) {
		logger.info("ex: ", ex);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.FORBIDDEN.value())).message(ex.getMessage())
				.build();// view
	}

	@ExceptionHandler(value = { TokenRefreshException.class, BadCredentialsException.class })
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseDTO<Void> handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
		logger.error("ex: ", ex);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
				.message(ex.getMessage()).build();
	}

	@ExceptionHandler({ Exception.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseDTO<Void> exception(Exception ex) {
		logger.error("ex: ", ex);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.message(ex.getMessage()).build();// view
	}

}
