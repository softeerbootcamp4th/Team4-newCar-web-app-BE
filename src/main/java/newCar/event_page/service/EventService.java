package newCar.event_page.service;

import lombok.RequiredArgsConstructor;
import newCar.event_page.dto.EventCommonDTO;
import newCar.event_page.entity.event.EventCommon;
import newCar.event_page.entity.event.quiz.Quiz;
import newCar.event_page.repository.EventCommonRepository;
import newCar.event_page.repository.quiz.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class EventService {

    private final EventCommonRepository eventCommonRepository;
    private final QuizRepository quizRepository;

    @Transactional(readOnly = true)
    public EventCommonDTO getEventInfo() {
        EventCommon eventCommon = eventCommonRepository.findById(1L).get();
        return EventCommonDTO.toDTO(eventCommon);
    }//이벤트의 공통 정보를 받아옵니다. 이벤트명, 담당자, 시작시간 종료시간 등

    public EventCommonDTO updateEventInfo(EventCommonDTO eventCommonDTO){
        EventCommon eventCommon = eventCommonRepository.findById(1L).get();
        eventCommon.update(eventCommonDTO);
        List<Quiz> quizList = quizRepository.findAll();
        LocalDate startDate = eventCommonDTO.getStartTime().toLocalDate();
        for (Quiz quiz : quizList) {
            quiz.setPostDate(startDate);
            startDate = startDate.plusDays(1L);
        }
        quizRepository.saveAll(quizList);
        return EventCommonDTO.toDTO(eventCommon);
    }
}
