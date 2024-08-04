package newCar.event_page.service;

import lombok.RequiredArgsConstructor;
import newCar.event_page.dto.QuizDTO;
import newCar.event_page.entity.event.Event;
import newCar.event_page.entity.event.quiz.Quiz;
import newCar.event_page.exception.UnmodifiableFieldException;
import newCar.event_page.repository.EventRepository;
import newCar.event_page.repository.quiz.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizService {

    private final QuizRepository quizRepository;
    private final EventRepository eventRepository;

    //event기간에 맞게 선착순 퀴즈 리스트를 보내준다
    @Transactional(readOnly = true)
    public List<QuizDTO> getQuizList(Long quizEventId) {
        Event quizEvent = eventRepository.findById(quizEventId)
                .orElseThrow(() -> new NoSuchElementException("퀴즈 이벤트가 존재하지 않습니다."));

        long eventDuration = quizEvent.getDuration();

        return quizRepository.findAllByOrderByPostDateAsc()
                .stream()
                .map(QuizDTO::toDTO)
                .limit(eventDuration)
                .toList();
    }

    public QuizDTO updateQuiz(QuizDTO quizDTO) {
        Long id = quizDTO.getId();
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + "- 존재하지 않는 퀴즈 ID입니다."));

        if(quizDTO.getPostDate() != null){
            throw new UnmodifiableFieldException("게시 날짜는 변경할 수 없습니다.");
        }

        quiz.update(quizDTO);
        quizRepository.save(quiz);
        return QuizDTO.toDTO(quiz);
    }

}
