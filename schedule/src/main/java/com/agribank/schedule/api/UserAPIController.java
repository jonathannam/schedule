package com.agribank.schedule.api;


import com.agribank.schedule.dto.LoginUser;
import com.agribank.schedule.dto.ResponseDTO;
import com.agribank.schedule.dto.SearchDTO;
import com.agribank.schedule.dto.UserDTO;
import com.agribank.schedule.service.UserService;
import com.agribank.schedule.utils.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserAPIController {

	@Autowired
	UserService userService;

	@Autowired
	FileStoreService fileStoreService;
	
	final String PREFIX_FOLDER = "avatar/";// thu muc chua logo nha mang

	@PostMapping("/")
	public ResponseDTO<UserDTO> create(@RequestBody UserDTO userDTO) {
		userService.create(userDTO);
		return ResponseDTO.<UserDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(userDTO).build();
	}

	@PutMapping("/update")
	public ResponseDTO<Void> update(@Valid @RequestBody UserDTO userDTO) {
		userService.update(userDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PutMapping("/password")
	public ResponseDTO<Void> updatePassword(@Valid @RequestBody UserDTO userDTO,
			@AuthenticationPrincipal LoginUser loginUser) {
		userDTO.setId(loginUser.getId());
		userService.updatePassword(userDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PutMapping("/avatar")
	public ResponseDTO<Void> updateAvatar(@Valid @ModelAttribute UserDTO userDTO,
			@AuthenticationPrincipal LoginUser loginUser) throws IOException {
		userDTO.setId(loginUser.getId());
		userDTO.setPhotoURL(fileStoreService.writeFile(userDTO.getFile(), PREFIX_FOLDER));

		userService.updatePhoto(userDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PutMapping("/reset/password")
	public ResponseDTO<Void> resetPassword(@Valid @RequestBody UserDTO userDTO) {
		userService.resetPassword(userDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PutMapping("/phone")
	public ResponseDTO<Void> updatePhone(@Valid @RequestBody UserDTO userDTO) {
		userService.updatePhone(userDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PutMapping("/status")
	public ResponseDTO<Void> updateStatus(@Valid @RequestBody UserDTO userDTO) {
		userService.updateStatus(userDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PutMapping("/role")
	public ResponseDTO<Void> updateRole(@Valid @RequestBody UserDTO userDTO) {
		userService.updateRole(userDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PutMapping("/email")
	public ResponseDTO<Void> updateEmail(@Valid @RequestBody UserDTO userDTO) {
		userService.updateEmail(userDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PostMapping("/search")
	public ResponseDTO<List<UserDTO>> search(@RequestBody @Valid SearchDTO searchDTO) {
		return userService.searchByTitle(searchDTO);
	}

	@GetMapping("/{id}")
	public ResponseDTO<UserDTO> get(@PathVariable(value = "id") int id) {
		return ResponseDTO.<UserDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(userService.get(id))
				.build();
	}

	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") int id) {
		userService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@DeleteMapping("/all/{ids}")
	public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Integer> ids) {
		userService.deleteAll(ids);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

}
