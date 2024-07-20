package com.yedam.app.common.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.common.mapper.UploadMapper;
import com.yedam.app.common.service.UploadService;
import com.yedam.app.common.service.UploadedFileVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {
	@Value("${file.upload.path}")
	private String uploadPath;
	
	private final UploadMapper uploadedFileMapper;
	
	@Override
	public String imageUpload(MultipartFile uploadFile, String domainType, Long domainId, int fileOrder) {
		if(!uploadFile.getContentType().startsWith("image")){
    		System.err.println("this file is not image type");
    		return null;
        }
  
        String originalName = uploadFile.getOriginalFilename();
        String fileName = originalName.substring(originalName.lastIndexOf("//")+1);
        
        //날짜 폴더 생성
        String folderPath = makeFolder();
        
        //UUID
        String uuid = UUID.randomUUID().toString();
        
        //저장할 파일 이름 중간에 "_"를 이용하여 구분
        String uploadFileName = folderPath +File.separator + uuid + "_" + fileName;
        
        String saveName = uploadPath + File.separator + uploadFileName;
        
        Path savePath = Paths.get(saveName);
        //Paths.get() 메서드는 특정 경로의 파일 정보를 가져옵니다.(경로 정의하기)
        System.out.println("path : " + saveName);
        try{
        	uploadFile.transferTo(savePath);
            //uploadFile에 파일을 업로드 하는 메서드 transferTo(file)
        } catch (IOException e) {
             e.printStackTrace();	             
        }
        
        // 파일 정보를 데이터베이스에 저장
        UploadedFileVO uploadedFile = new UploadedFileVO();
        uploadedFile.setFileName(fileName);
        uploadedFile.setFileType(uploadFile.getContentType());
        uploadedFile.setFileSize(uploadFile.getSize());
        uploadedFile.setUploadTime(LocalDateTime.now());
        uploadedFile.setFilePath(uploadFileName);
        uploadedFile.setDomainType(domainType);
        uploadedFile.setDomainId(domainId);
        uploadedFile.setFileOrder(fileOrder);

        uploadedFileMapper.insertUploadedFile(uploadedFile);
        
        return saveName;
	}
	
	private String makeFolder() {
		String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		// LocalDate를 문자열로 포멧
		String folderPath = str.replace("/", File.separator);
		File uploadPathFoler = new File(uploadPath, folderPath);
		// File newFile= new File(dir,"파일명");
		if (uploadPathFoler.exists() == false) {
			uploadPathFoler.mkdirs();
			// 만약 uploadPathFolder가 존재하지않는다면 makeDirectory하라는 의미입니다.
			// mkdir(): 디렉토리에 상위 디렉토리가 존재하지 않을경우에는 생성이 불가능한 함수
			// mkdirs(): 디렉토리의 상위 디렉토리가 존재하지 않을 경우에는 상위 디렉토리까지 모두 생성하는 함수
		}
		return folderPath;
	}
}