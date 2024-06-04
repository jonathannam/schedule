package com.agribank.schedule.service;


import com.agribank.schedule.dto.ResponseDTO;
import com.agribank.schedule.dto.SearchDTO;
import com.agribank.schedule.dto.UserDTO;
import com.agribank.schedule.entity.Role;
import com.agribank.schedule.entity.User;
import com.agribank.schedule.repository.RoleRepo;
import com.agribank.schedule.repository.UserRepo;
import com.agribank.schedule.utils.CacheNames;
import com.agribank.schedule.utils.PasswordGenerator;
import com.agribank.schedule.utils.RoleEnum;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface UserService {
	void create(UserDTO userDTO);

	void resetPassword(UserDTO userDTO);

	void updateRole(UserDTO userDTO);

	void updateEmail(UserDTO userDTO);

	void updatePhone(UserDTO userDTO);

	void updateStatus(UserDTO userDTO);

	void updatePassword(UserDTO userDTO);

	void updatePhoto(UserDTO userDTO);

	void signup(UserDTO userDTO);

	void update(UserDTO userDTO);

	void delete(Integer id);

	void deleteAll(List<Integer> ids);

	UserDTO get(Integer id);

	ResponseDTO<List<UserDTO>> searchByTitle(SearchDTO searchDTO);

	UserDTO getUserByUsername(String username);
}

@Service
class UserServiceImpl implements UserService {
	@Autowired
	UserRepo userRepo;

	@Autowired
	RoleRepo roleRepo;

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void create(UserDTO userDTO) {
		User user = new ModelMapper().map(userDTO, User.class);
		user.setPassword(PasswordGenerator.encode(userDTO.getPassword()));
		userRepo.save(user);
		userDTO.setId(user.getId());
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void signup(UserDTO userDTO) {
		User user = new ModelMapper().map(userDTO, User.class);
		user.setPassword(PasswordGenerator.encode(userDTO.getPassword()));
		user.setRole(roleRepo.findByName(RoleEnum.MEMBER.getRoleName()).orElse(null));
		user.setEnabled(true);

		userRepo.save(user);
		userDTO.setId(user.getId());
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void update(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);

		if (userDTO.getPhotoURL() != null) {
			user.setPhotoURL(userDTO.getPhotoURL());
		}
		user.setName(userDTO.getName());
		user.setAddress(userDTO.getAddress());
		user.setNote(userDTO.getNote());
		user.setBirthdate(userDTO.getBirthdate());
		user.setWebsite(userDTO.getWebsite());

		userRepo.save(user);
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void updatePhoto(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);

		if (userDTO.getPhotoURL() != null) {
			user.setPhotoURL(userDTO.getPhotoURL());
		}

		userRepo.save(user);
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void updatePhone(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);

		if (userDTO.getPhone() != null) {
			user.setPhone(userDTO.getPhone());
			userRepo.save(user);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void updateEmail(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);

		if (userDTO.getEmail() != null) {
			user.setEmail(userDTO.getEmail());
			userRepo.save(user);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void updateRole(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);
		Role role = roleRepo.findById(userDTO.getRole().getId()).orElseThrow(NoResultException::new);
		if (userDTO.getRole() != null) {
			user.setRole(role);
			userRepo.save(user);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void updateStatus(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);
		user.setEnabled(userDTO.getEnabled());
		userRepo.save(user);
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void updatePassword(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);
		if (new BCryptPasswordEncoder().matches(userDTO.getOldPassword(), user.getPassword())) {
			user.setPassword(PasswordGenerator.encode(userDTO.getPassword()));
		} else throw new NoResultException("Sai mat khau hien tai");
		userRepo.save(user);
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void resetPassword(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);
		user.setPassword(PasswordGenerator.encode(userDTO.getPassword()));
		userRepo.save(user);
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void delete(Integer id) {
		userRepo.deleteById(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void deleteAll(List<Integer> ids) {
		userRepo.deleteAllById(ids);
	}

	@Override
	public UserDTO getUserByUsername(String username) {
		return userRepo.findByUsername(username).map(user -> convert(user)).orElseThrow(NoResultException::new);
	}

	@Override
	public UserDTO get(Integer id) {
		return userRepo.findById(id).map(user -> convert(user)).orElseThrow(NoResultException::new);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Cacheable(cacheNames = CacheNames.CACHE_USER_FIND, unless = "#result.totalElements == 0", key = "#searchDTO.toString()")
	public ResponseDTO<List<UserDTO>> searchByTitle(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
				.map(order -> {
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		Page<User> page = userRepo.find(searchDTO.getValue(), pageable);

		ResponseDTO<List<UserDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().map(user -> convert(user)).collect(Collectors.toList()));
		return responseDTO;
	}

	private UserDTO convert(User user) {
		return new ModelMapper().map(user, UserDTO.class);
	}

}