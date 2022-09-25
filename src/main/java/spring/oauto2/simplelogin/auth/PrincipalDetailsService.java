package spring.oauto2.simplelogin.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.oauto2.simplelogin.entity.User;
import spring.oauto2.simplelogin.repository.LoginRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService  implements UserDetailsService {

    private final LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("email = {}", username);
        User findUser = loginRepository.findByEmail(username);
        if (findUser != null){
            return new PrincipalDetails(findUser);
        }
        return null;
    }
}