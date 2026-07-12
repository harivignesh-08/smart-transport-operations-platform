package com.transitops.file_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transitops.file_service.entity.FileData;
import com.transitops.file_service.repository.FileRepository;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository repository;

    @Override
    public List<FileData> getAllFiles() {
        return repository.findAll();
    }

    @Override
    public Optional<FileData> getFileById(Long id) {
        return repository.findById(id);
    }

    @Override
    public FileData saveFile(FileData fileData) {
        return repository.save(fileData);
    }

    @Override
    public FileData updateFile(Long id, FileData fileData) {

        FileData existing = repository.findById(id).orElseThrow();

        existing.setFileName(fileData.getFileName());
        existing.setFileType(fileData.getFileType());
        existing.setFilePath(fileData.getFilePath());

        return repository.save(existing);
    }

    @Override
    public void deleteFile(Long id) {
        repository.deleteById(id);
    }
}