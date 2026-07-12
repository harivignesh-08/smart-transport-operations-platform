package com.transitops.emergency_service.service;

import java.util.List;
import java.util.Optional;

import com.transitops.emergency_service.entity.Emergency;

public interface EmergencyService {

    List<Emergency> getAllEmergencies();

    Optional<Emergency> getEmergencyById(Long id);

    Emergency saveEmergency(Emergency emergency);

    Emergency updateEmergency(Long id, Emergency emergency);

    void deleteEmergency(Long id);
}