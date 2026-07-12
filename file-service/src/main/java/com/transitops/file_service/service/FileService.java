package com.transitops.file_service.service;

import java.util.List;
import java.util.Optional;

import com.transitops.file_service.entity.FileData;

public interface FileService {

    List<FileData> getAllFiles();

    Optional<FileData> getFileById(Long id);

    FileData saveFile(FileData fileData);

    FileData updateFile(Long id, FileData fileData);

    void deleteFile(Long id);
}