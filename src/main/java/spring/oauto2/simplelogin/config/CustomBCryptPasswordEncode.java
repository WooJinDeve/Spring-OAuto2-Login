package spring.oauto2.simplelogin.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomBCryptPasswordEncode extends BCryptPasswordEncoder {
}
