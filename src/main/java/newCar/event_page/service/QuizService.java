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


    //event기간에 맞게 선착순 퀴즈 리스트를 보내준다
    @Transactional(readOnly = true)
    public List<QuizDTO> getQuizList(Long eventId) {

        EventCommon eventCommon = eventRepository.findById(eventId).get().getEventCommon();
        LocalDate startTime = eventCommon.getStartTime().toLocalDate(); //이벤트 시작 날짜
        LocalDate endTime = eventCommon.getEndTime().toLocalDate();//이벤트 종료 날짜
        List<Quiz> quizList = quizRepository.findAll();
        List<QuizDTO> quizDTOList = new ArrayList<>();
        for(Quiz temp : quizList) {
            quizDTOList.add(QuizDTO.toDTO(temp));
        }
        return quizDTOList.stream().limit(getDuration(startTime,endTime)).toList();
    }



    public QuizDTO updateQuiz(QuizDTO quizDTO) {
        Quiz quiz = quizRepository.findById(quizDTO.getId()).get();
        quiz.update(quizDTO);
        quizRepository.save(quiz);
        return QuizDTO.toDTO(quiz);
    }


    private long getDuration(LocalDate startDate, LocalDate endDate){
        return ChronoUnit.DAYS.between(startDate,endDate)+1;
    }
}
