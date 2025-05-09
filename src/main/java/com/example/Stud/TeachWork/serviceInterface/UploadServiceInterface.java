package com.example.Stud.TeachWork.serviceInterface;

import org.springframework.web.multipart.MultipartFile;

import com.example.Stud.TeachWork.dto.UploadedFileResponseDto;  // Updated import
import com.example.Stud.TeachWork.entities.UploadedFile;

public interface UploadServiceInterface {
    // Return type ko UploadedFileResponseDto update karo
    UploadedFileResponseDto saveAttachment(MultipartFile file) throws Exception;

    UploadedFile getAttachment(String fileId) throws Exception;

    String deleteById(String id);
}
