package com.agribank.schedule.repository;

import com.agribank.schedule.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {
	Optional<RefreshToken> findByToken(String token);

	@Modifying
	@Query("delete from RefreshToken rt where rt.user.id = :uid")
	int deleteByUserId(@Param("uid") int id);

	@Query("select rt from RefreshToken rt where rt.user.id = :uid")
	RefreshToken getRefreshTokenByUid(@Param("uid") int id);
	
	@Modifying
	@Query("delete from RefreshToken rt where rt.expiryDate <= :date")
	int deleteExpiredToken(@Param("date") Date date);
}
