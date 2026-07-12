package com.transitops.digital_twin_service.service;

import java.util.List;

import com.transitops.digital_twin_service.entity.DigitalTwin;

public interface DigitalTwinService {

    List<DigitalTwin> getAllDigitalTwins();

    DigitalTwin getDigitalTwinById(Long id);

    DigitalTwin saveDigitalTwin(DigitalTwin digitalTwin);

    DigitalTwin updateDigitalTwin(Long id, DigitalTwin digitalTwin);

    void deleteDigitalTwin(Long id);
}