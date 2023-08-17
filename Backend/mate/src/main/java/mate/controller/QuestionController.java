package mate.controller;

import lombok.RequiredArgsConstructor;
import mate.domain.question.QuestionBoard;
import mate.domain.question.QuestionBoardComment;
import mate.domain.question.QuestionBoardLike;
import mate.dto.question.QuestionBoardCommentDto;
import mate.dto.question.QuestionBoardDto;
import mate.repository.QuestionRepository;
import mate.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/qna")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionRepository questionRepository;
    private final QuestionService questionService;

    @PostMapping("/write")
    public ResponseEntity<Map<String, Object>> writeNotice(@RequestBody QuestionBoardDto questionDto) {
        Map<String, Object> map = new HashMap<>();
        questionService.writeQuestion(questionDto);
        map.put("resmsg", "Q&A 글 작성 성공");

        return ResponseEntity.ok(map);
    }

    @GetMapping("/detail/{questionIdx}")
    public ResponseEntity<Map<String, Object>> detailQuestion(@PathVariable("questionIdx") Integer questionIdx) {
        Map<String, Object> map = new HashMap<>();
//        Optional<QuestionBoard> question = questionRepository.findById(questionIdx);

        QuestionBoard question = questionService.detailQuestion(questionIdx);

        map.put("resmsg", "Q&A 글 조회 성공");
        map.put("Q&A", question);

        return ResponseEntity.ok(map);
    }

    @PutMapping("/modify")
    public ResponseEntity<Map<String, Object>> modifyQuestion(@RequestBody QuestionBoard question) {
        Map<String, Object> map = new HashMap<>();

        questionService.modifyQuestion(question);
        map.put("resmsg", "Q&A 글 수정 성공");

        return ResponseEntity.ok(map);
    }

    @DeleteMapping("/delete/{questionIdx}")
    public ResponseEntity<Map<String, Object>> deleteQuestion(@PathVariable("questionIdx") int questionIdx) {
        Map<String, Object> map = new HashMap<>();

        questionService.deleteQuestion(questionIdx);
        map.put("resmsg", "Q&A 글 삭제 성공");

        return ResponseEntity.ok(map);
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getQuestionList() {
        System.out.println("questionAll");
        Map<String, Object> map = new HashMap<>();

        map.put("resmsg", "Q&A 글 리스트 조회 성공");
        map.put("Q&A", questionService.getQuestionList());
        return ResponseEntity.ok(map);
    }

//    @GetMapping("/list/{page}")
//    public ResponseEntity<Map<String, Object>> getQuestionTop(@PathVariable("page") int page) {
//        System.out.println("questionPage");
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("resmsg", "Q&A 글 리스트 조회 성공");
//        map.put("Q&A", questionService.getQuestionList().subList(10 * (page - 1), 10 * page));
//        return ResponseEntity.ok(map);
//    }

    @GetMapping("/find/title/{keyWord}")
    public ResponseEntity<Map<String, Object>> findQuestionByTitle(
            @PathVariable(value = "keyWord", required = false) String keyWord) {
        System.out.println("title = " + keyWord);
        Map<String, Object> map = new HashMap<>();

        map.put("resmsg", "Q&A 글 리스트 조회 성공");
        map.put("Q&A", questionService.findQuestionByTitle(keyWord));
        return ResponseEntity.ok(map);
    }

    @GetMapping("/find/content/{keyWord}")
    public ResponseEntity<Map<String, Object>> findQuestionByContent(
            @PathVariable(value = "keyWord", required = false) String keyWord) {
        System.out.println("content = " + keyWord);
        Map<String, Object> map = new HashMap<>();

        map.put("resmsg", "Q&A 글 리스트 조회 성공");
        map.put("Q&A", questionService.findQuestionByContent(keyWord));
        return ResponseEntity.ok(map);
    }

    @GetMapping("/find/user/{name}")
    public ResponseEntity<Map<String, Object>> findQuestionByName(
            @PathVariable(value = "name", required = false) String name) {
        System.out.println("name = " + name);
        Map<String, Object> map = new HashMap<>();

        map.put("resmsg", "Q&A 글 리스트 조회 성공");
        map.put("Q&A", questionService.findQuestionByName(name));
        return ResponseEntity.ok(map);
    }

    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> likeQuestion(@RequestBody QuestionBoardLike like) {
        questionService.writeQuestionBoardLike(like);
        Map<String, Object> map = new HashMap<>();
        map.put("resmsg", "Q&A 글 리스트 조회 성공");

        return ResponseEntity.ok(map);
    }

    @PostMapping("/comment/write")
    public ResponseEntity<Map<String, Object>> writeComment(@RequestBody QuestionBoardCommentDto comment) {
        // DTO 만들어야된다...ㅠㅠ
        questionService.writeQuestionBoardComment(comment);
        Map<String, Object> map = new HashMap<>();
        map.put("resmsg", "Q&A 댓글 작성 성공");

        return ResponseEntity.ok(map);
    }

    @PutMapping("/comment/modify")
    public ResponseEntity<Map<String, Object>> modifyComment(@RequestBody QuestionBoardCommentDto comment) {
        questionService.writeQuestionBoardComment(comment);
        Map<String, Object> map = new HashMap<>();
        map.put("resmsg", "Q&A 댓글 수정 성공");

        return ResponseEntity.ok(map);
    }

    @DeleteMapping("/comment/delete/{commentIdx}")
    public ResponseEntity<Map<String, Object>> deleteComment(int commentIdx) {
        questionService.deleteQuestionBoardComment(commentIdx);
        Map<String, Object> map = new HashMap<>();
        map.put("resmsg", "Q&A 댓글 삭제 성공");

        return ResponseEntity.ok(map);
    }

    @GetMapping("/comment/{boardIdx}")
    public ResponseEntity<Map<String, Object>> findComment(@PathVariable Integer boardIdx) {
        Map<String, Object> map = new HashMap<>();
        map.put("resmsg", "Q&A 댓글 조회 성공");
        map.put("data", questionService.findCommentByBoard(boardIdx).stream().map(comment -> {
            QuestionBoardCommentDto commentDto = new QuestionBoardCommentDto();
            commentDto.setIdx(comment.getIdx());
            commentDto.setUserIdx(comment.getUser().getIdx());
            commentDto.setUserNickname(comment.getUser().getNickname());
            commentDto.setBoardIdx(comment.getQuestionBoard().getIdx());
            commentDto.setContent(comment.getContent());
            commentDto.setCreatedAt(comment.getCreatedAt());
            System.out.println("comment = " + comment.toString());
            return commentDto;
        }));
        return ResponseEntity.ok(map);
    }
}
