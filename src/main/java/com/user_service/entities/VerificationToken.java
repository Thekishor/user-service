package com.user_service.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "verificationToken")
@Getter
@Setter
@NoArgsConstructor
public class VerificationToken {

    private static final int EXPIRATION_TIME = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expirationToken;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private User user;

    public VerificationToken(User user, String token){
        super();
        this.user = user;
        this.token = token;
        this.expirationToken = calculateExpirationTime(EXPIRATION_TIME);
    }

    public VerificationToken(String token){
        super();
        this.token = token;
        this.expirationToken = calculateExpirationTime(EXPIRATION_TIME);
    }

    private Date calculateExpirationTime(int expiredTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expiredTime);
        return new Date(calendar.getTime().getTime());
    }
}
