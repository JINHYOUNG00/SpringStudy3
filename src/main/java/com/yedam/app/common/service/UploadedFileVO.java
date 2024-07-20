package com.yedam.app.common.service;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UploadedFileVO {
	private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private LocalDateTime uploadTime;
    private String filePath;
    private String domainType;
    private Long domainId;
    private int fileOrder;
}
