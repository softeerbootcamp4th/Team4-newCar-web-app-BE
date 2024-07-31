package newCar.event_page.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import newCar.event_page.dto.EventCommonDTO;
import newCar.event_page.entity.event.EventCommon;
import newCar.event_page.repository.EventCommonRepository;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Transactional
@Service
public class EventService {

    private final EventCommonRepository eventCommonRepository;


    public EventCommonDTO getEventInfo() {
        EventCommon eventCommon = eventCommonRepository.findById(1L).get();
        return EventCommonDTO.toDTO(eventCommon);
    }//이벤트의 공통 정보를 받아옵니다. 이벤트명, 담당자, 시작시간 종료시간 등

    public EventCommonDTO updateEventInfo(EventCommonDTO eventCommonDTO){
        EventCommon eventCommon = eventCommonRepository.findById(1L).get();
        eventCommon.update(eventCommonDTO);
        return EventCommonDTO.toDTO(eventCommon);
    }
}
