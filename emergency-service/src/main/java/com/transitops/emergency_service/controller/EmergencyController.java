package com.transitops.emergency_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.transitops.emergency_service.entity.Emergency;
import com.transitops.emergency_service.service.EmergencyService;

@RestController
@RequestMapping("/emergencies")
public class EmergencyController {

    @Autowired
    private EmergencyService service;

    @GetMapping
    public List<Emergency> getAllEmergencies() {
        return service.getAllEmergencies();
    }

    @GetMapping("/{id}")
    public Optional<Emergency> getEmergencyById(@PathVariable Long id) {
        return service.getEmergencyById(id);
    }

    @PostMapping
    public Emergency createEmergency(@RequestBody Emergency emergency) {
        return service.saveEmergency(emergency);
    }

    @PutMapping("/{id}")
    public Emergency updateEmergency(@PathVariable Long id,
                                     @RequestBody Emergency emergency) {
        return service.updateEmergency(id, emergency);
    }

    @DeleteMapping("/{id}")
    public void deleteEmergency(@PathVariable Long id) {
        service.deleteEmergency(id);
    }
}