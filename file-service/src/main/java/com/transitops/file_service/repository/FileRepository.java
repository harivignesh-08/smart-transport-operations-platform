package com.transitops.file_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transitops.file_service.entity.FileData;

@Repository
public interface FileRepository extends JpaRepository<FileData, Long> {

}
