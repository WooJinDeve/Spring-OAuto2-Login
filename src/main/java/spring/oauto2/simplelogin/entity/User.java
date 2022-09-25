package spring.oauto2.simplelogin.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@ToString
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String email;
    private String username;
    private String password;
    private String role; // ROLE_USER, ROLE_ADMIN

    private String provider;
    private String providerId;

    //회원 가입 사용
    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
        setRole("ROLE_USER");
    }

    public User(String email, String username, String password, String provider, String providerId) {
        this(email, password, username);
        this.provider = provider;
        this.providerId = providerId;
    }

}
