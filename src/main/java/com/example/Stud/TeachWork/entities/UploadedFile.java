package com.example.Stud.TeachWork.entities;




import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UploadedFile {

	@SuppressWarnings("deprecation")
	@Id
	@GeneratedValue(generator = "uuid")		//UUID = Universally Unique Identifier
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;	// We use String for id because UUID is string-based and it gives us a globally unique identifier for every file.

	
	private String fileName;
	private String fileType;
	
	@Lob
	private byte[] data;

}