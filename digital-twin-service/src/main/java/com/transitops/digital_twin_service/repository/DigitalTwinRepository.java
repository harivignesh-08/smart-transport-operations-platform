package com.transitops.digital_twin_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transitops.digital_twin_service.entity.DigitalTwin;

@Repository
public interface DigitalTwinRepository extends JpaRepository<DigitalTwin, Long> {

}