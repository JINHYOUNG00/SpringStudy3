package com.yedam.app.board.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yedam.app.board.mapper.BoardMapper;
import com.yedam.app.board.service.BoardService;
import com.yedam.app.board.service.BoardVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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
		int result = boardMapper.insertBoardInfo(boardVO);
		
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
