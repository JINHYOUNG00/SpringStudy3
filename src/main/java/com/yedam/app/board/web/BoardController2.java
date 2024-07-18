package com.yedam.app.board.web;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yedam.app.board.service.BoardService;
import com.yedam.app.board.service.BoardVO;

import lombok.RequiredArgsConstructor;



@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController2 {
	
	private final BoardService boardService;
	
	// 게시판 리스트
	@GetMapping
	public String boardList(Model model) {
		List<BoardVO> boardList = boardService.boardList();
		model.addAttribute("blist", boardList);
		return "/board/boardList";
	}
	
	// 단건 조회
	@GetMapping("/{boardNo}")
	public String boardInfo(@PathVariable Integer boardNo, Model model) {
		BoardVO boardVO = new BoardVO();
		boardVO.setBoardNo(boardNo);
		BoardVO board = boardService.boardInfo(boardVO);
		model.addAttribute("board", board);
		return "/board/boardInfo";
	}
	
	// 게시글 등록 (페이지)
	@GetMapping("/save")
	public String boardInsertForm() {
		return "/board/boardInsert";
	}
	
	
	// 게시글 등록
	@PostMapping("/save")
	public String boardInsert(@RequestBody BoardVO boardVO) {
		int bno = boardService.saveBoard(boardVO);
		return "redirect:board/" + bno;
	}
	
	// 게시글 수정 (페이지)
	@GetMapping("/edit/{boardNo}")
	public String boardUpdateForm(@PathVariable Integer boardNo, Model model) {
		BoardVO boardVO = new BoardVO();
		boardVO.setBoardNo(boardNo);
		BoardVO board = boardService.boardInfo(boardVO);
		model.addAttribute("board", board);
		return "/board/boardUpdate";
	}
	
	
	// 게시글 수정
	@PostMapping("/edit/{boardNo}")
	@ResponseBody
	public Map<String, Object> boardUpdate(@RequestBody BoardVO boardVO, @PathVariable Integer boardNo) {
		boardVO.setBoardNo(boardNo);
		Map<String, Object> editBoard = boardService.editBoard(boardVO);
		return editBoard;
	}
	
	// 게시글 삭제
	@GetMapping("/delete/{boardNo}")
	public String boardDelete(@PathVariable Integer boardNo) {
		boardService.removeBoard(boardNo);
		return "redirect:board";
	}
	
	
}
