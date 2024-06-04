package com.agribank.schedule.api;


import com.agribank.schedule.dto.ResponseDTO;
import com.agribank.schedule.dto.RoleDTO;
import com.agribank.schedule.dto.SearchDTO;
import com.agribank.schedule.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/role")
@RestController
public class RoleAPIController {
	@Autowired
	private RoleService roleService;

	@PostMapping("/")
	public ResponseDTO<RoleDTO> create(@RequestBody @Valid RoleDTO roleDTO) {
		return ResponseDTO.<RoleDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(roleDTO).build();
	}

	@PutMapping("/")
	public ResponseDTO<Void> update(@RequestBody @Valid RoleDTO roleDTO) {
		roleService.update(roleDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@GetMapping("/{id}")
	public ResponseDTO<RoleDTO> get(@PathVariable(value = "id") int id) {
		return ResponseDTO.<RoleDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(roleService.get(id))
				.build();
	}

	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") int id) {
		roleService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@DeleteMapping("/all/{ids}")
	public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Integer> ids) {
		roleService.deleteAll(ids);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PostMapping("/search")
	public ResponseDTO<List<RoleDTO>> search(@RequestBody @Valid SearchDTO searchDTO) {
		return roleService.searchByTitle(searchDTO);
	}
}
