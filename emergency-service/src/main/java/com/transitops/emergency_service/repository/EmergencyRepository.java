package com.transitops.emergency_service.repository;

import com.transitops.emergency_service.entity.Emergency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyRepository extends JpaRepository<Emergency, Long> {

}