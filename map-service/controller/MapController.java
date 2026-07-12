package com.transitops.map_service.controller;

import com.transitops.map_service.entity.VehicleLocation;
import com.transitops.map_service.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @PostMapping("/location")
    public VehicleLocation saveLocation(@RequestBody VehicleLocation location) {
        return mapService.saveLocation(location);
    }

    @GetMapping("/locations")
    public List<VehicleLocation> getAllLocations() {
        return mapService.getAllLocations();
    }
}