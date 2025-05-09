	package com.example.Stud.TeachWork.repo;
	
	
	import org.springframework.data.jpa.repository.JpaRepository;
	
	import com.example.Stud.TeachWork.entities.UploadedFile;
	
	public interface UploadedFileRepo extends JpaRepository<UploadedFile, String>{
	
		boolean existsByFileName(String fileName);
	
		
	
	}
