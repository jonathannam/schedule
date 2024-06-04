package com.agribank.schedule.api;


import com.agribank.schedule.dto.EmailDTO;
import com.agribank.schedule.dto.ResponseDTO;
import com.agribank.schedule.dto.SearchDTO;
import com.agribank.schedule.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/email")
public class EmailAPIController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/")
	public ResponseDTO<EmailDTO> create(@RequestBody @Valid EmailDTO emailDTO) {
		emailService.create(emailDTO);
		return ResponseDTO.<EmailDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(emailDTO).build();
	}

	@PutMapping("/")
	public ResponseDTO<Void> update(@RequestBody @Valid EmailDTO emailDTO) {
		emailService.update(emailDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@GetMapping("/{id}")
	public ResponseDTO<EmailDTO> get(@PathVariable(value = "id") Integer id) {
		return ResponseDTO.<EmailDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(emailService.get(id))
				.build();
	}

	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") Integer id) {
		emailService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@DeleteMapping("/all/{ids}")
	public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Integer> ids) {
		emailService.deleteAll(ids);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PostMapping("/search")
	public ResponseDTO<List<EmailDTO>> find(@RequestBody @Valid SearchDTO searchDTO) {
		return emailService.find(searchDTO);
	}

}
