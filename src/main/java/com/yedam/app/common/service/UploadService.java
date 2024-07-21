package com.yedam.app.common.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

	public String imageUpload(MultipartFile[] uploadFiles, String domainType, Long domainId);
	
	public String imageUpdate(MultipartFile[] uploadFiles, String domainType, Long domainId);

	List<UploadedFileVO> selectFilesByDomain(String domainType, Long domainId);
}
