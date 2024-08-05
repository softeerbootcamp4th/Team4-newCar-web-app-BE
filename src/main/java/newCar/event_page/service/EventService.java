package newCar.event_page.service;

import lombok.RequiredArgsConstructor;
import newCar.event_page.dto.EventCommonDTO;
import newCar.event_page.entity.event.EventCommon;
import newCar.event_page.entity.event.EventId;
import newCar.event_page.entity.event.quiz.Quiz;
import newCar.event_page.entity.event.quiz.QuizEvent;
import newCar.event_page.repository.EventCommonRepository;
import newCar.event_page.repository.quiz.QuizEventRepository;
import newCar.event_page.repository.quiz.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventCommonRepository eventCommonRepository;
    private final QuizRepository quizRepository;
    private final QuizEventRepository quizEventRepository;

    @Transactional(readOnly = true)
    public EventCommonDTO getEventInfo() {
        EventCommon eventCommon = eventCommonRepository.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("공통 이벤트 정보가 존재하지 않습니다."));

        return EventCommonDTO.toDTO(eventCommon);
    }//이벤트의 공통 정보를 받아옵니다. 이벤트명, 담당자, 시작시간 종료시간 등

    public EventCommonDTO updateEventInfo(EventCommonDTO eventCommonDTO){
        EventCommon eventCommon = eventCommonRepository.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("공통 이벤트 정보가 존재하지 않아 수정이 불가능합니다."));

        eventCommon.update(eventCommonDTO);
        long duration = eventCommon.getDuration();
        updateQuiz(eventCommonDTO.getStartTime().toLocalDate() , duration);


        return EventCommonDTO.toDTO(eventCommon);
    }

    private void updateQuiz(LocalDate startDate, long duration) {
        List<Quiz> quizList = quizRepository.findAllByOrderByPostDateAsc();
        QuizEvent quizEvent = quizEventRepository.findById(EventId.Quiz.getValue())
                .orElseThrow(() -> new NoSuchElementException("퀴즈 이벤트가 존재하지 않습니다."));

        int quizCount = quizList.size();
        if (quizCount < duration) {
            for (int i = 0; i < duration - quizCount; i++) {
                quizList.add(Quiz.getDummy(quizEvent));
            }
        }

        for (Quiz quiz : quizList) {
            quiz.setPostDate(startDate);
            startDate = startDate.plusDays(1L);
        }

        quizRepository.saveAll(quizList);
    }
}

