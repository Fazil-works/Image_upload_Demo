package com.example.Stud.TeachWork.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Stud.TeachWork.ResponseData.ResponseData;
import com.example.Stud.TeachWork.dto.UploadedFileResponseDto;  
import com.example.Stud.TeachWork.entities.UploadedFile;
import com.example.Stud.TeachWork.serviceInterface.UploadServiceInterface;

@RestController
@RequestMapping("/api")
public class UploadController {

    @Autowired
    private UploadServiceInterface uploadServiceInterface;

    // Updated uploadFile method to return ResponseData with consistent message
    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            UploadedFileResponseDto responseDto = uploadServiceInterface.saveAttachment(file);

            // Return success response with file data
            return new ResponseData(
                "File uploaded successfully.",
                responseDto
            );
        } catch (Exception e) {
            return new ResponseData(
                "File upload failed: " + e.getMessage(),
                "FILE_UPLOAD_ERROR"
            );
        }
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        UploadedFile attachment = uploadServiceInterface.getAttachment(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable String id) {
        String result = uploadServiceInterface.deleteById(id);
        if (result.contains("Successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }
}
