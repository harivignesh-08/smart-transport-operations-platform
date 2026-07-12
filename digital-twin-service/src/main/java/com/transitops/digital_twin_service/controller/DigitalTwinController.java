package com.transitops.digital_twin_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.transitops.digital_twin_service.entity.DigitalTwin;
import com.transitops.digital_twin_service.service.DigitalTwinService;

@RestController
@RequestMapping("/digitaltwins")
@CrossOrigin("*")
public class DigitalTwinController {

    @Autowired
    private DigitalTwinService service;

    @GetMapping
    public List<DigitalTwin> getAllDigitalTwins() {
        return service.getAllDigitalTwins();
    }

    @GetMapping("/{id}")
    public DigitalTwin getDigitalTwinById(@PathVariable Long id) {
        return service.getDigitalTwinById(id);
    }

    @PostMapping
    public DigitalTwin createDigitalTwin(@RequestBody DigitalTwin digitalTwin) {
        return service.saveDigitalTwin(digitalTwin);
    }

    @PutMapping("/{id}")
    public DigitalTwin updateDigitalTwin(@PathVariable Long id,
                                         @RequestBody DigitalTwin digitalTwin) {
        return service.updateDigitalTwin(id, digitalTwin);
    }

    @DeleteMapping("/{id}")
    public String deleteDigitalTwin(@PathVariable Long id) {
        service.deleteDigitalTwin(id);
        return "Digital Twin deleted successfully.";
    }
}