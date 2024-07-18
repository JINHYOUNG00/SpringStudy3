package com.yedam.app.board.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yedam.app.board.mapper.BoardMapper;
import com.yedam.app.board.service.BoardService;
import com.yedam.app.board.service.BoardVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService{

	private final BoardMapper boardMapper;
	
	// 게시판 전체조회
	@Override
	public List<BoardVO> boardList() {
		return boardMapper.selectAllBoard();
	}
	
	// 단건조회
	@Override
	public BoardVO boardInfo(BoardVO boardVO) {
		return boardMapper.selectBoardInfo(boardVO);
	}
	
	// 게시글 입력 
	@Override
	public int saveBoard(BoardVO boardVO) {
		boardVO.setRegdate(new Date()); // 게시글 등록할 때 현재 시간을 넣어줌
		int result = boardMapper.insertBoardInfo(boardVO);
		log.info("saveRegdate={}", boardVO.getRegdate());
		log.info("saveBoardNo={}", boardVO.getBoardNo());
		
		return result == 1 ? boardVO.getBoardNo() : -1;
	}

	// 게시글 수정
	@Override
	public Map<String, Object> editBoard(BoardVO boardVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;
		
		int result = boardMapper.updateBaordInfo(boardVO);
		if(result == 1) {
			isSuccessed = true;
		}
		map.put("result", isSuccessed);
		map.put("target", boardVO);
		
		return map;
	}

	// 게시글 삭제
	@Override
	public int removeBoard(int boardNo) {
		return boardMapper.deleteBoardInfo(boardNo);
	}
	
	

}
