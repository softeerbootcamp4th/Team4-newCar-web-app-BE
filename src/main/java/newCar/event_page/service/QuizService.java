package newCar.event_page.service;

import lombok.RequiredArgsConstructor;
import newCar.event_page.dto.QuizDTO;
import newCar.event_page.entity.event.EventCommon;
import newCar.event_page.entity.event.quiz.Quiz;
import newCar.event_page.repository.EventCommonRepository;
import newCar.event_page.repository.EventRepository;
import newCar.event_page.repository.quiz.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final EventRepository eventRepository;


    //시작 날짜와 종료날짜에 사이에 들어오는 퀴즈들만 반환한다.
    public List<QuizDTO> getQuizList(Long eventId) {

        EventCommon eventCommon = eventRepository.findById(eventId).get().getEventCommon();
        LocalDate startTime = eventCommon.getStartTime().toLocalDate();
        LocalDate endTime = eventCommon.getEndTime().toLocalDate();
        List<Quiz> quizList = quizRepository.findAllByDuration(startTime, endTime);
        long duration = duration(startTime,endTime);
        List<QuizDTO> quizDTOList = new ArrayList<>();
        for(Quiz temp : quizList) {
            quizDTOList.add(QuizDTO.toDTO(temp));
        }
        for(int i = 0 ; i < duration ; i++){
            quizDTOList.add(new QuizDTO());
        }
        return quizDTOList;
    }

    public QuizDTO updateQuiz(QuizDTO quizDTO) {
        Quiz quiz = quizRepository.findById(quizDTO.getId()).get();
        quiz.update(quizDTO);
        quizRepository.save(quiz);
        return QuizDTO.toDTO(quiz);
    }

    private long duration(LocalDate startTime,LocalDate endTime){

    }
}
