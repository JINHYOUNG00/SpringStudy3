package com.yedam.app.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

	public String imageUpload(MultipartFile uploadFile, String domainType, Long domainId, int fileOrder);
}
