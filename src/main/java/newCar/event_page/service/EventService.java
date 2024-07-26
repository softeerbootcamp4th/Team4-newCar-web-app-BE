package newCar.event_page.service;

import jakarta.transaction.Transactional;
import newCar.event_page.dto.CommonEventDTO;
import newCar.event_page.entity.event.Event;
import newCar.event_page.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Transactional
@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository=eventRepository;
    }

    public CommonEventDTO getEventInfo() {
        Event event = eventRepository.findById(1L).orElseThrow(
                ()-> new NoSuchElementException("Event not found"));
        return CommonEventDTO.toDTO(event);
    }//이벤트의 공통 정보를 받아옵니다. 이벤트명, 담당자, 시작시간 종료시간 등

    public CommonEventDTO updateEventInfo(CommonEventDTO commonEventDTO){
        Event event = eventRepository.findById(1L).orElseThrow(
                ()->new NoSuchElementException("Event not found")
        );
        event.setEventName(commonEventDTO.getEventName());
        event.setManagerName(commonEventDTO.getEventManager());
        event.setStatus(commonEventDTO.getStatus());
        event.setStartTime(commonEventDTO.getStartTime());
        event.setEndTime(commonEventDTO.getEndTime());
        eventRepository.save(event);
        return CommonEventDTO.toDTO(event);
    }
}
