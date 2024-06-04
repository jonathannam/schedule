package com.agribank.schedule.service;


import com.agribank.schedule.dto.EmailDTO;
import com.agribank.schedule.dto.ResponseDTO;
import com.agribank.schedule.dto.SearchDTO;
import com.agribank.schedule.entity.Email;
import com.agribank.schedule.repository.EmailRepository;
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

public interface EmailService {

	void create(EmailDTO emailDTO);

	void deleteAll(List<Integer> ids);

	ResponseDTO<List<EmailDTO>> find(SearchDTO searchDTO);

	EmailDTO get(Integer id);

	void delete(Integer id);

	void update(EmailDTO emailDTO);

}

@Service
class EmailServiceImpl implements EmailService {

	@Autowired
	EmailRepository emailRepository;

	@Transactional
	@Override
	@CacheEvict(value = CacheNames.CACHE_EMAIL_FIND, allEntries = true)
	public void create(EmailDTO emailDTO) {
		ModelMapper mapper = new ModelMapper();
		Email email = mapper.map(emailDTO, Email.class);
		emailRepository.save(email);
		emailDTO.setId(email.getId());
	}

	@Transactional
	@Override
	@CacheEvict(value = CacheNames.CACHE_EMAIL_FIND, allEntries = true)
	public void update(EmailDTO emailDTO) {
		ModelMapper mapper = new ModelMapper();
		mapper.createTypeMap(EmailDTO.class, Email.class)
				.setProvider(p -> emailRepository.findById(emailDTO.getId()).orElseThrow(NoResultException::new));

		Email email = mapper.map(emailDTO, Email.class);
		emailRepository.save(email);
	}

	@Transactional
	@Override
	@CacheEvict(value = CacheNames.CACHE_EMAIL_FIND, allEntries = true)
	public void delete(Integer id) {
		emailRepository.deleteById(id);
	}

	@Transactional
	@Override
	@CacheEvict(value = CacheNames.CACHE_EMAIL_FIND, allEntries = true)
	public void deleteAll(List<Integer> ids) {
		emailRepository.deleteAllByIdInBatch(ids);
	}

	@Override
	public EmailDTO get(Integer id) {
		return emailRepository.findById(id).map(email -> convert(email)).orElseThrow(NoResultException::new);
	}

	@Cacheable(cacheNames = CacheNames.CACHE_EMAIL_FIND, unless = "#result.totalElements == 0", key = "#searchDTO.toString()")
	@Override
	@SuppressWarnings("unchecked")
	public ResponseDTO<List<EmailDTO>> find(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
				.map(order -> {
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		Page<Email> page = emailRepository.find(searchDTO.getValue(), pageable);

		ResponseDTO<List<EmailDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().map(email -> convert(email)).collect(Collectors.toList()));
		return responseDTO;
	}

	private EmailDTO convert(Email email) {
		return new ModelMapper().map(email, EmailDTO.class);
	}
}
