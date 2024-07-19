package com.yedam.app.board.service;

import java.util.List;
import java.util.Map;

public interface BoardService {
	
	// 게시판 전체 조회
	public List<BoardVO> boardList();
	// 게시판 상세 조회
	public BoardVO boardInfo(BoardVO boardVO);
	// 게시판 등록
	public int saveBoard(BoardVO boardVO);

	// 게시판 수정
	public Map<String, Object> editBoard(BoardVO boardVO);
	// 게시판 삭제
	public int removeBoard(int boardNo);
	

}
