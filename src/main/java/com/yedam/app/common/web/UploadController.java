package com.yedam.app.common.web;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UploadController {
	
//  운영서버는 리눅스인데 리눅스는 D드라이브 개념이 없기 때문에 이 경로는 고정값이 아님 
//	private String uploadPath = "D:/upload";
	
	@Value("${file.upload.path}") // 환경변수(프로세스 단위로 접근하는 외부변수)로 들어온 경로값을 읽어서 넣어줌
	private String uploadPath;
	
	@GetMapping("getPath")
	@ResponseBody
	public String getPath() {
		return uploadPath;
	}
	
	@GetMapping("formUpload")
	public void formUploadPage() {
		
	}
	// form 태그에 multiple이 있으면 반드시 MultipartFile이 배열로 선언되어야한다. 
	// 만약 여러건 처리를 하지 않는다면 배열이 아니어도 상관없다.
	// enctype="multipart/form-data" => @RequestPart, @PostMapping
	// MultipartResolver => Content-type : Multipart/form-data
	@PostMapping("uploadForm") // 중요!! 변수명(images)은 form의 name 속성을 따라가야한다. 
	public String formUploadFileString(@RequestPart MultipartFile[] images) {
		for (MultipartFile image : images) {
			log.info("imageContentType={}", image.getContentType());
			log.info("imageOriginalFilename={}", image.getOriginalFilename());
			log.info("imageSize={}", String.valueOf(image.getSize()));
			// 1) 원래 파일이름
			String fileName = image.getOriginalFilename();
			// 2) 실제 저장할 경로를 생성 : 서버의 업로드 경로 + 파일이름 합치
			String saveName = uploadPath + File.separator + fileName;
			log.info("saveName={}", saveName);
			
				// Path => 실제 pc의 경로로 변환하는 작업
			Path savePath = Paths.get(saveName);
			// 3) 파일 작성(파일 업로드) D:/upload(실습이니까 간단하게)
			try {
				// MultipartFile이 제공하는 메서드 transferTo() 
				image.transferTo(savePath);
			} catch (IOException e) {
				log.error("err={}", e.getMessage());
				e.printStackTrace();
			}
		}
		// 나중에 리다이렉트로 바꿔줘야함 PRG
		return "formUpload";
	}
	
	@GetMapping("upload")
	public void uploadPage() {}
	
	@PostMapping("/uploadsAjax")
	@ResponseBody
	public List<String> uploadFile(@RequestPart MultipartFile[] uploadFiles) {
	    
		List<String> imageList = new ArrayList<>();
		
	    for(MultipartFile uploadFile : uploadFiles){
	    	if(uploadFile.getContentType().startsWith("image") == false){
	    		System.err.println("this file is not image type");
	    		return null;
	        }
	  
	        String originalName = uploadFile.getOriginalFilename();
	        String fileName = originalName.substring(originalName.lastIndexOf("//")+1);
	        
	        System.out.println("fileName : " + fileName);
	    
	        //날짜 폴더 생성
	        String folderPath = makeFolder();
	        //UUID - Universally Unique IDentifier => 식별자 역할을 하는 고유값
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
	        // DB에 해당 경로 저장
	        // 1) 사용자가 업로드할 때 사용한 파일명
	        // 2) 실제 서버에 업로드할 때 사용한 경로
	        imageList.add(setImagePath(uploadFileName));
	     }
	    
	    return imageList;
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
	
	private String setImagePath(String uploadFileName) {
		return uploadFileName.replace(File.separator, "/");
	}
	
	
}
