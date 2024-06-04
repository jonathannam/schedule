package com.agribank.schedule.repository;

import com.agribank.schedule.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

	@Query("SELECT u FROM User u WHERE u.username LIKE :name OR u.phone LIKE :name OR u.name LIKE :name")
	Page<User> find(@Param("name") String value, Pageable pageable);

	Optional<User> findByUsername(String username);

}
