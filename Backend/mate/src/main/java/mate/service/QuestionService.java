package mate.service;

import lombok.RequiredArgsConstructor;
import mate.domain.question.QuestionBoard;
import mate.domain.question.QuestionBoardComment;
import mate.domain.question.QuestionBoardLike;
import mate.domain.user.User;
import mate.dto.question.QuestionBoardCommentDto;
import mate.dto.question.QuestionBoardDto;
import mate.repository.QuestionCommentRepository;
import mate.repository.QuestionLikeRepository;
import mate.repository.QuestionRepository;
import mate.repository.user.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionLikeRepository questionLikeRepository;
    private final QuestionCommentRepository questionCommentRepository;
    private final UserRepository userRepository;

    public void writeQuestion(QuestionBoardDto questionBoardDto) {
        User user = userRepository.findById(questionBoardDto.getUserIdx()).get();

        questionRepository.save(QuestionBoard.builder()
                .user(user)
                .title(questionBoardDto.getTitle())
                .content(questionBoardDto.getContent())
                        .createdAt(LocalDateTime.now())
                .build());
    }

    public QuestionBoard detailQuestion(int questionIdx) {
        Optional<QuestionBoard> question = questionRepository.findById(questionIdx);
        if (!question.isPresent()) {
            return null;
        }
        System.out.println("=====================");
        User user = question.get().getUser();
        question.map(q -> {
            QuestionBoardDto dto = new QuestionBoardDto();
            dto.setIdx(questionIdx);
            dto.setUserIdx(user.getIdx());
            dto.setTitle(q.getTitle());
            dto.setContent(q.getContent());
            dto.setCreatedAt(q.getCreatedAt());

            return dto;
        });

        return question.get();
    }

    public void modifyQuestion(QuestionBoard questionBoard) {
        questionRepository.save(questionBoard);
    }

    public void deleteQuestion(int questionIdx) {
        questionRepository.deleteById(questionIdx);
    }

    public List<QuestionBoard> getQuestionList(){
        return questionRepository.findAll(Sort.by(Sort.Direction.DESC, "idx"));
    }

    public List<QuestionBoard> getQuestionTop4(){
        return questionRepository.findTop4ByOrderByIdxDesc();
    }

    public List<QuestionBoard> findQuestionByTitle(String keyWord) {
        return questionRepository.findByTitleContainingOrderByIdxDesc(keyWord);
    }

    public List<QuestionBoard> findQuestionByContent(String keyWord) {
        return questionRepository.findByContentContainingOrderByIdxDesc(keyWord);
    }

    public List<QuestionBoard> findQuestionByName(String name) {
        return questionRepository.findByNameOrderByIdxDesc(name);
    }

    public void writeQuestionBoardLike(QuestionBoardLike like) {
        questionLikeRepository.save(like);
    }

    public void writeQuestionBoardComment(QuestionBoardCommentDto commentDto) {
        QuestionBoard qb = questionRepository.findById(commentDto.getBoardIdx()).get();
        User user = userRepository.findById(commentDto.getUserIdx()).get();
        questionCommentRepository.save(QuestionBoardComment.builder()
                        .questionBoard(qb)
                        .user(user)
                        .content(commentDto.getContent())
                        .createdAt(LocalDateTime.now())
                .build());
    }

    public void modifyQuestionBoardComment(QuestionBoardCommentDto commentDto) {
        questionCommentRepository.save(QuestionBoardComment.builder()
                        .idx(commentDto.getIdx())
                        .content(commentDto.getContent())
                .build());
    }

    public void deleteQuestionBoardComment(int commentIdx) {
        questionCommentRepository.deleteById(commentIdx);
    }

    public List<QuestionBoardComment> findCommentByBoard(Integer boardIdx) {
        return questionCommentRepository.findByQuestionBoardIdxOrderByIdxDesc(boardIdx).get();
    }
}
