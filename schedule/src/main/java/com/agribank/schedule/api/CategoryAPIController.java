package com.agribank.schedule.api;


import com.agribank.schedule.dto.CategoryDTO;
import com.agribank.schedule.dto.ResponseDTO;
import com.agribank.schedule.dto.SearchDTO;
import com.agribank.schedule.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryAPIController {
	@Autowired
	private CategoryService categoryService;

	@PostMapping("/")
	public ResponseDTO<CategoryDTO> create(@RequestBody @Valid CategoryDTO categoryDTO) throws IOException {
		categoryService.create(categoryDTO);
		return ResponseDTO.<CategoryDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(categoryDTO).build();
	}

	@PutMapping("/")
	public ResponseDTO<Void> update(@RequestBody @Valid CategoryDTO categoryDTO) throws IOException {
		categoryService.update(categoryDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@GetMapping("/{id}")
	public ResponseDTO<CategoryDTO> get(@PathVariable(value = "id") int id) {
		return ResponseDTO.<CategoryDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(categoryService.get(id)).build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") int id) {
		categoryService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@DeleteMapping("/all/{ids}")
	public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Integer> ids) {
		categoryService.deleteAll(ids);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PostMapping("/search")
	public ResponseDTO<List<CategoryDTO>> search(@RequestBody @Valid SearchDTO searchDTO) {
		return categoryService.find(searchDTO);
	}

}
