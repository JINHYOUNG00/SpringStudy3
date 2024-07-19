package com.yedam.app.board.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class BoardVO {
	private Integer boardNo; // 번호
	private String boardTitle; // 제목
	private String boardContent; // 내용
	private String boardWriter; // 작성자
	// java.util.Date : yyyy/MM/dd
	// <input type="date"> => @DateTimeFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd") // 파라미터 자동변환할때 사용
	private Date regdate; // 작성일
	@DateTimeFormat(pattern = "yyyy-MM-dd") // 파라미터 자동변환할때 사용
	private Date updatedate; // 수정일
	private String image; // 첨부이미지
}
