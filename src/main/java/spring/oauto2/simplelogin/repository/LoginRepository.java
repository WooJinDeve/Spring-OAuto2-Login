package spring.oauto2.simplelogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.oauto2.simplelogin.entity.User;

public interface LoginRepository extends JpaRepository<User, Long> {
    public User findByEmail(String loginId);
}
