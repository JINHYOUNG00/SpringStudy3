package com.yedam.app.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yedam.app.common.service.UploadedFileVO;

public interface UploadMapper {
	// 파일 업로드
	void insertUploadedFile(UploadedFileVO uploadedFile);
	// 파일 조회
	List<UploadedFileVO> selectFilesByDomain(@Param("domainType") String domainType, @Param("domainId") Long domainId);
	// 파일 Path 조회
	List<String> selectFilePathsByBoard(@Param("domainType") String domainType, @Param("domainId") Long domainId);
	// 파일 삭제
	void deleteFilesByDomain(@Param("domainType") String domainType, @Param("domainId") Long domainId);
	
}
