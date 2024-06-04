package com.agribank.schedule.service;


import com.agribank.schedule.dto.ResponseDTO;
import com.agribank.schedule.dto.RoleDTO;
import com.agribank.schedule.dto.SearchDTO;
import com.agribank.schedule.entity.Role;
import com.agribank.schedule.repository.RoleRepo;
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

public interface RoleService {
	void create(RoleDTO roleDTO);

	void update(RoleDTO roleDTO);

	void delete(Integer id);

	void deleteAll(List<Integer> ids);

	RoleDTO get(Integer id);

	ResponseDTO<List<RoleDTO>> searchByTitle(SearchDTO searchDTO);
}

@Service
class RoleServiceImpl implements RoleService {
	@Autowired
	RoleRepo roleRepo;

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_ROLE_FIND, allEntries = true)
	public void create(RoleDTO roleDTO) {
		Role role = new Role();
		role.setName(roleDTO.getName());
		roleRepo.save(role);
		roleDTO.setId(role.getId());
	}

	@Override
	@Transactional
	public void update(RoleDTO roleDTO) {
		ModelMapper mapper = new ModelMapper();
		mapper.createTypeMap(RoleDTO.class, Role.class)
				.setProvider(p -> roleRepo.findById(roleDTO.getId()).orElseThrow(NoResultException::new));

		Role role = mapper.map(roleDTO, Role.class);
		roleRepo.save(role);
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_ROLE_FIND, allEntries = true)
	public void delete(Integer id) {
		roleRepo.deleteById(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_ROLE_FIND, allEntries = true)
	public void deleteAll(List<Integer> ids) {
		roleRepo.deleteAllById(ids);
	}

	@Override
	public RoleDTO get(Integer id) {
		RoleDTO roleDTO = roleRepo.findById(id).map(role -> convert(role)).orElseThrow(NoResultException::new);
		return roleDTO;
	}

	@Cacheable(cacheNames = CacheNames.CACHE_ROLE_FIND, unless = "#result.totalElements == 0", key = "#searchDTO.toString()")
	@Override
	public ResponseDTO<List<RoleDTO>> searchByTitle(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
				.map(order -> { // Tạo 1 cái searchDTO dùng chung cho tất ca
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		Page<Role> page = roleRepo.searchByName(searchDTO.getValue(), pageable);
		@SuppressWarnings("unchecked")
		ResponseDTO<List<RoleDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().map(role -> convert(role)).collect(Collectors.toList()));
		return responseDTO;
	}

	private RoleDTO convert(Role role) {
		return new ModelMapper().map(role, RoleDTO.class);
	}

}