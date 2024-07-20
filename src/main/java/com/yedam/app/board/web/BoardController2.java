package com.yedam.app.board.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yedam.app.board.service.BoardService;
import com.yedam.app.board.service.BoardVO;
import com.yedam.app.common.service.UploadService;

import lombok.RequiredArgsConstructor;



@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController2 {
	
	private final BoardService boardService;
	private final UploadService uploadService;
	
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
	
	
	private String setImagePath(String uploadFileName) {
		return uploadFileName.replace(File.separator, "/");
	}
	
	// 게시글 등록
	@PostMapping("/save")
	public String boardInsert(BoardVO boardVO, @RequestPart MultipartFile[] uploadFiles) {
		
		List<String> imageList = new ArrayList<>();
		
		int bno = boardService.saveBoard(boardVO);
		int i = 0;
		for (MultipartFile uploadFile : uploadFiles) {
			String savePath = uploadService.imageUpload(uploadFile, "BOARD", (long)bno, i);
			boardVO.setImage(savePath);
			imageList.add(setImagePath(savePath));
			i++;
		}
		
		
		return "redirect:" + bno;
	}
	
	// 게시글 수정 (페이지)
	@GetMapping("/{boardNo}/edit")
	public String boardUpdateForm(@PathVariable Integer boardNo, Model model) {
		BoardVO boardVO = new BoardVO();
		boardVO.setBoardNo(boardNo);
		BoardVO board = boardService.boardInfo(boardVO);
		model.addAttribute("board", board);
		return "/board/boardUpdate";
	}
	
	
	// 게시글 수정
	@PostMapping("/{boardNo}/edit")
	@ResponseBody
	public Map<String, Object> boardUpdate(@RequestBody BoardVO boardVO, @PathVariable Integer boardNo) {
		boardVO.setBoardNo(boardNo);
		Map<String, Object> editBoard = boardService.editBoard(boardVO);
		return editBoard;
	}
	
	// 게시글 삭제
	@GetMapping("/{boardNo}/delete")
	public String boardDelete(@PathVariable Integer boardNo) {
		boardService.removeBoard(boardNo);
		return "redirect:/board";
	}
	
	
}
