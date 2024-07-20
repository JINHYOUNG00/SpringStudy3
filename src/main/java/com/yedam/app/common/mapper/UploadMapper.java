package com.yedam.app.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yedam.app.common.service.UploadedFileVO;

public interface UploadMapper {
	void insertUploadedFile(UploadedFileVO uploadedFile);

	List<UploadedFileVO> selectFilesByDomain(@Param("domainType") String domainType, @Param("domainId") Long domainId);
}
