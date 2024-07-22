package com.yedam.app.board.web;

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
import com.yedam.app.common.service.UploadedFileVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
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
		List<UploadedFileVO> images = uploadService.selectFilesByDomain("BOARD", (long) boardNo);
//		log.info("imageName={}", images.get(0).getFileName()); 파일 없을땐 500에러
		model.addAttribute("flist", images);
		return "/board/boardInfo";
	}

	// 게시글 등록 (페이지)
	@GetMapping("/save")
	public String boardInsertForm() {
		return "/board/boardInsert";
	}

	// 게시글 등록
	@PostMapping("/save")
	public String boardInsert(BoardVO boardVO, @RequestPart MultipartFile[] uploadFiles) {
		int bno = boardService.saveBoard(boardVO);
		// 파일 업로드 처리 // 파일 배열과, 게시판 유형, 글번호를 넣어줘야함.
		uploadService.imageUpload(uploadFiles, "BOARD", (long) bno);

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
	public Map<String, Object> boardUpdate(BoardVO boardVO,
			@RequestPart(value = "uploadFiles") MultipartFile[] uploadFiles,
			@PathVariable Integer boardNo) {
		boardVO.setBoardNo(boardNo);
		// 게시글 수정
		Map<String, Object> editBoard = boardService.editBoard(boardVO);
		// 파일 수정 처리
//		uploadService.imageUpdate(uploadFiles, "BOARD", (long)boardNo);
		 // 파일 수정 처리
	    if (uploadFiles != null && uploadFiles.length > 0) {
	        log.info("업로드 파일 수: {}", uploadFiles.length);
	        for (MultipartFile file : uploadFiles) {
	            log.info("파일 이름: {}, 파일 크기: {}", file.getOriginalFilename(), file.getSize());
	        }
	        uploadService.imageUpdate(uploadFiles, "BOARD", (long)boardNo);
	    } else {
	        log.info("업로드된 파일이 없습니다.");
	    }
		

		return editBoard;
	}

	// 게시글 삭제
	@GetMapping("/{boardNo}/delete")
	public String boardDelete(@PathVariable Integer boardNo) {
		boardService.removeBoard(boardNo);
		return "redirect:/board";
	}

}
