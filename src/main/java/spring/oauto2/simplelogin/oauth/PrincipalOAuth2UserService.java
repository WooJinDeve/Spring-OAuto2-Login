package spring.oauto2.simplelogin.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    // Auth 서버로 부터 받은 UserRequest 데이터에 대한 후처리 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest.getClientRegistration : {}",userRequest.getClientRegistration());
        log.info("userRequest.getClientRegistration.getClientId() : {}",userRequest.getClientRegistration().getClientId());
        log.info("userRequest.getClientRegistration.getClientSecret() : {}",userRequest.getClientRegistration().getClientSecret());
        log.info("userRequest.getAccessToken : {}",userRequest.getAccessToken().getTokenValue());
        log.info("userRequest.getAdditionalParameters : {}",userRequest.getAdditionalParameters());
        log.info("super.loadUser(userRequest).getAttributes() : {}", super.loadUser(userRequest).getAttributes());
        return super.loadUser(userRequest);
    }
}
