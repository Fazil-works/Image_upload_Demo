package com.example.Stud.TeachWork.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.Stud.TeachWork.dto.UploadedFileResponseDto;
import com.example.Stud.TeachWork.entities.UploadedFile;
import com.example.Stud.TeachWork.repo.UploadedFileRepo;
import com.example.Stud.TeachWork.serviceInterface.UploadServiceInterface;

@Service
public class UploadServiceImpl implements UploadServiceInterface {

    @Autowired
    private UploadedFileRepo uploadedFileRepo;

    @Override
    public UploadedFileResponseDto saveAttachment(MultipartFile file) throws Exception {
        // Ensure the file is not empty
        if (file.isEmpty()) {
            throw new Exception("No file selected for upload.");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Check for duplicate file name
        if (uploadedFileRepo.existsByFileName(fileName)) {
            throw new Exception("A file with this name already exists: " + fileName);
        }

        // File type validation
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.contains("jpeg") || contentType.contains("png") || contentType.contains("pdf"))) {
            throw new Exception("Invalid file type. Only JPEG, PNG, and PDF files are allowed.");
        }

        // Prepare and save file
        UploadedFile attachment = new UploadedFile();
        attachment.setFileName(fileName);
        attachment.setFileType(contentType);
        attachment.setData(file.getBytes());

        // Save and return DTO
        UploadedFile savedFile = uploadedFileRepo.save(attachment);

        String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/download/")
                .path(savedFile.getId())
                .toUriString();

        return new UploadedFileResponseDto(
                savedFile.getFileName(),
                downloadURL,
                savedFile.getFileType(),
                savedFile.getData().length
        );
    }

    @Override
    public String deleteById(String id) {
        if (uploadedFileRepo.existsById(id)) {
            uploadedFileRepo.deleteById(id);
            return "File deleted successfully.";
        } else {
            return "File not found with id: " + id;
        }
    }

    @Override
    public UploadedFile getAttachment(String fileId) throws Exception {
        return uploadedFileRepo
                .findById(fileId)
                .orElseThrow(() -> new Exception("File not found with id: " + fileId));
    }
}
