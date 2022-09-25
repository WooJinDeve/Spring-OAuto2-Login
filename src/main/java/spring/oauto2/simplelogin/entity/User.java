package spring.oauto2.simplelogin.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
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

}
