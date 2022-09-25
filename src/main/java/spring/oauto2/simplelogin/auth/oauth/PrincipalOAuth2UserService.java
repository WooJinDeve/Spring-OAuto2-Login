package spring.oauto2.simplelogin.auth.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import spring.oauto2.simplelogin.auth.PrincipalDetails;
import spring.oauto2.simplelogin.auth.oauth.provider.FacebookUserInfo;
import spring.oauto2.simplelogin.auth.oauth.provider.GoogleUserInfo;
import spring.oauto2.simplelogin.auth.oauth.provider.NaverUserInfo;
import spring.oauto2.simplelogin.auth.oauth.provider.OAuth2UserInfo;
import spring.oauto2.simplelogin.entity.User;
import spring.oauto2.simplelogin.repository.LoginRepository;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final LoginRepository loginRepository;

    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 생성
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("getAttributes : {}", oAuth2User.getAttributes());
        log.info("userRequest.getClientRegistration().getClientId() : {}", userRequest.getClientRegistration());

        OAuth2UserInfo oAuth2UserInfo = null;

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")){
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        }

        String provider = oAuth2UserInfo.getProvider(); // Social Name;
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String username = oAuth2UserInfo.getName();
        String password = bCryptPasswordEncoder.encode("password");

        User userEntity = loginRepository.findByEmail(email);
        if (userEntity == null){
            userEntity = new User(email,username,password,provider,providerId);
            loginRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
