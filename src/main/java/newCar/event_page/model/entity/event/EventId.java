package newCar.event_page.model.entity.event;

public enum EventId {
    Racing(1),
    Quiz(2);

    private final long value;

    EventId(long value){
        this.value=value;
    }

    public long getValue(){
        return value;
    }
}
