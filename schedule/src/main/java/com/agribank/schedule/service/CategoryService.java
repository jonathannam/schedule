package com.agribank.schedule.service;


import com.agribank.schedule.dto.CategoryDTO;
import com.agribank.schedule.dto.ResponseDTO;
import com.agribank.schedule.dto.SearchDTO;
import com.agribank.schedule.entity.Category;
import com.agribank.schedule.repository.CategoryRepository;
import com.agribank.schedule.utils.CacheNames;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface CategoryService {
	void create(CategoryDTO categoryDTO);

	void update(CategoryDTO categoryDTO);

	void delete(Integer id);

	void deleteAll(List<Integer> ids);

	CategoryDTO get(Integer id);

	ResponseDTO<List<CategoryDTO>> find(SearchDTO searchDTO);
}

@Service
class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepo;

	@Transactional
	@Override
	@CacheEvict(value = CacheNames.CACHE_CATEGORY_FIND, allEntries = true)
	public void create(CategoryDTO categoryDTO) {
		ModelMapper mapper = new ModelMapper();
		Category category = mapper.map(categoryDTO, Category.class);
		categoryRepo.save(category);
		categoryDTO.setId(category.getId());
	}

	@Transactional
	@Override
	@CacheEvict(value = CacheNames.CACHE_CATEGORY_FIND, allEntries = true)
	public void update(CategoryDTO categoryDTO) {
		ModelMapper mapper = new ModelMapper();
		mapper.createTypeMap(CategoryDTO.class, Category.class)
				.setProvider(p -> categoryRepo.findById(categoryDTO.getId()).orElseThrow(NoResultException::new));

		Category category = mapper.map(categoryDTO, Category.class);
		categoryRepo.save(category);
	}

	@Transactional
	@Override
	@CacheEvict(value = CacheNames.CACHE_CATEGORY_FIND, allEntries = true)
	public void delete(Integer id) {
		categoryRepo.deleteById(id);
	}

	@Transactional
	@Override
	@CacheEvict(value = CacheNames.CACHE_CATEGORY_FIND, allEntries = true)
	public void deleteAll(List<Integer> ids) {
		categoryRepo.deleteAllByIdInBatch(ids);
	}

	@Override
	public CategoryDTO get(Integer id) {
		return categoryRepo.findById(id).map(category -> convert(category)).orElseThrow(NoResultException::new);
	}

	@Cacheable(cacheNames = CacheNames.CACHE_CATEGORY_FIND, unless = "#result.totalElements == 0", key = "#searchDTO.toString()")
	@Override
	public ResponseDTO<List<CategoryDTO>> find(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
				.map(order -> {
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		Page<Category> page = categoryRepo.find(searchDTO.getValue(), pageable);

		@SuppressWarnings("unchecked")
		ResponseDTO<List<CategoryDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().map(category -> convert(category)).collect(Collectors.toList()));
		return responseDTO;
	}

	private CategoryDTO convert(Category category) {
		return new ModelMapper().map(category, CategoryDTO.class);
	}
}
