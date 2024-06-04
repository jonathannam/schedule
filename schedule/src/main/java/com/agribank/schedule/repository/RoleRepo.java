package com.agribank.schedule.repository;

import com.agribank.schedule.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {
	Optional<Role> findByName(String username);

	@Query("SELECT r FROM Role r WHERE r.name LIKE :x")
	Page<Role> searchByName(@Param("x") String s, Pageable pageable);
}