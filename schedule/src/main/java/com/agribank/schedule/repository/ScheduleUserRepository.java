package com.agribank.schedule.repository;

import com.agribank.schedule.entity.ScheduleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleUserRepository extends JpaRepository<ScheduleUser,Long> {
}
