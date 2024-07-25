package newCar.event_page.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import newCar.event_page.entity.event.LoginType;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String userName;

    private String nickName;

    @NotNull
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @NotNull
    private LoginType loginType;

    @NotNull
    private Integer clickNumber = 0;

    @Enumerated(EnumType.STRING)
    private Team team;

    @NotNull
    private Boolean isMarketingAgree;

}
