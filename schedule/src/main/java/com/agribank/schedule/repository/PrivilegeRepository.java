package com.agribank.schedule.repository;

import com.agribank.schedule.entity.Privilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
	@Query("SELECT p FROM Privilege p WHERE p.api LIKE :value OR p.authority LIKE :value")
	Page<Privilege> find(@Param("value") String value, Pageable pageable);
	
	Optional<Privilege> findByAuthority(String authority);

}
