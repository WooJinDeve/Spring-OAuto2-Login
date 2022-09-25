package spring.oauto2.simplelogin.dto;

import lombok.Data;
import lombok.ToString;
import spring.oauto2.simplelogin.entity.User;

@Data
@ToString
public class UserInfoDto {

    private String email;
    private String username;
    private String role;

    public UserInfoDto(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}
