package newCar.event_page.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private Long id;

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
