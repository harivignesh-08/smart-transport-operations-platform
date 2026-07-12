package com.transitops.file_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.transitops.file_service.entity.FileData;
import com.transitops.file_service.service.FileService;

@RestController
@RequestMapping("/files")
@CrossOrigin("*")
public class FileController {

    @Autowired
    private FileService service;

    @GetMapping
    public List<FileData> getAllFiles() {
        return service.getAllFiles();
    }

    @GetMapping("/{id}")
    public Optional<FileData> getFileById(@PathVariable Long id) {
        return service.getFileById(id);
    }

    @PostMapping
    public FileData createFile(@RequestBody FileData fileData) {
        return service.saveFile(fileData);
    }

    @PutMapping("/{id}")
    public FileData updateFile(@PathVariable Long id,
                               @RequestBody FileData fileData) {
        return service.updateFile(id, fileData);
    }

    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable Long id) {
        service.deleteFile(id);
    }
}