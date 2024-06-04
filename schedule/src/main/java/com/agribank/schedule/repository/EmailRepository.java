package com.agribank.schedule.repository;

import com.agribank.schedule.entity.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email,Integer>{
	@Query("SELECT e FROM Email e WHERE e.email LIKE :email ")
    Page<Email> find(@Param("email") String value, Pageable pageable);
}
