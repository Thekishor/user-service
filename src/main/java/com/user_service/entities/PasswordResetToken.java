package com.user_service.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken {

    private static final int EXPIRATION_TIME = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expiredTime;

    @OneToOne
    @JoinColumn(name = "user_id",
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_TOKEN"))
    private User user;

    public PasswordResetToken(User user, String token){
        super();
        this.user = user;
        this.token = token;
        this.expiredTime = CalculateExpiredTime(EXPIRATION_TIME);
    }

    public PasswordResetToken(String token){
        super();
        this.token = token;
        this.expiredTime = CalculateExpiredTime(EXPIRATION_TIME);
    }

    private Date CalculateExpiredTime(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}
