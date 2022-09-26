## 🍃 스프링 부트 OAuth2 로그인

### 기본 설정
- **스프링 시작**
    - [https://start.spring.io/](https://start.spring.io/)


- **Spring Package Setting**
    - `Java Version` : 11
    - `Project` : `Gradle Project`
    - `Packaging`
        - `Spring Boot` : `Jar`
    - `Dependencies`
        - `Spring Web`, `Thymeleaf`, `Lombok`, `Validation`, `Spring DATA JPA`, `MySQL Driver`, `Spring Security`, `OAuth-Client`


- **Login Key**
  - `Google Login Key` : [https://console.cloud.google.com/](https://console.cloud.google.com/)
  - `Facebook Login Key` : [https://developers.facebook.com/?/](https://developers.facebook.com/)
  - `Naver Login Key` : [https://developers.naver.com/main/](https://developers.naver.com/main/)
  
### 기존의 문제점

- 현제에는 많은 사이트가 존재하고 우리 정보는 사이트의 개수만큼이나 많이 존재하게 된다.
- 이러한 상황에서의 문제는 개인정보에 대한 문제를 초래한다.
    - 한곳에서 정보를 관리한다면 최소한으로 문제를 줄일 수 있게 된다.
    - 애플리케이션 관리자는 인증처리에 대한 부분도 안해도 된다.

### OAuth ( Open Auth )

- `OAuth 서버`가 인증 처리를 대신해준다.
    - 로그인, 로그아웃, 회원가입 등..
- `OAuth 서버`에게 해당 `리소스서버`에 있는 정보에 접근할 수 있는 권한을 얻는다.
- **로그인 동작 방식**
    - **기존** : `클라이언트 → 서버`
    - **OAuth**
        - `클라이언트 → 서버 → OAuth 서버(카카오, 네이버 등) → 클라이언트(인증 요청) → 인증 완료(서버에게 Code Value 콜백)`
        - `OAuth 서버`에게 받은 `Code Value`로 인증 요청시 `Access Token` 을 발급 받음.
        - `Access Token` 발급시 `리소스서버`에 접근 할 수 있는 권한을 부여 받음.

### OAuth Client

- 스프링은 `OAuth`를 편리하게 구현하기 위해 `OAuth-Client`라는 라이브러리를 제공한다.
    - `OAuth 서버`에 요청시 받는 토큰 `URL`은 `/login/oauth2/code/google`
    - `OAuth` 인증을 위한 `LoginForm URL`은 `/auto2/authorization/google`
- `OAuth Client`는 사용자 로그인 완료시 `OAuth 서버`에 **엑세스 토큰 과 사용자 프로필을 미리 받게 된다.**

```java
http.authorizeRequests()
                 // Oauth2 로그인
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(null);
```

- `DefaultOAuth2UserService`를 통해 `Auth 서버`에 받은 데이터를 후처리 할 수 있다.
    - `org.springframework.security.oauth2.client`
    - `userRequest.getAccessToken().getTokenValue();` : 엑세스 토큰
    - `uper.loadUser(userRequest).getAttributes();` : 사용자 프로필

```java
@Slf4j
@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    // Auth 서버로 부터 받은 UserRequest 데이터에 대한 후처리 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest : {}",userRequest);
        return super.loadUser(userRequest);
    }
}
```

- **OAuto-Client 동작원리**
    - 클라이언트 로그인 요청 → `OAuth 서버` 로그인 창 → 로그인 성공 → `code` 콜백 → `Access Token` 요청


### OAuth2 GetMapping

- `OAuth2` 에 대한 `GetMapping` 정보는 `Authentication` 객체에 전달 받을 수 있으며 이를 다운 캐스팅을 통해 정보를 획득한다
- `@AuthenticationPrincipal` 를 통해 다운캐스팅된 객체 또한 전달 받을 수 있다.

```java
@GetMapping("/oauth/login")
@ResponseBody
public String testOAuth(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth){
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    log.info("oAuth2User.getAttributes(): {}", oAuth2User.getAttributes());
    log.info("oauth.getAttributes(): {}", oauth.getAttributes());
    return "testOAuth";
}
```
### OAuth2 로그인 설정

- 스프링 시큐리티가 관리하는 세션에는 `Authentication` 객체만 존재할 수 있다.
    - `Authentication` 객체가 담을 수 있는 필드가 2가지가 존재한다.
        - `UserDatiles`, `OAuth2User`이를 통해 객체간 `DI`가능해진다.
    - `UserDetaile`와 `OAuth2User` 객체를 통합하여 저장하면 일반 로그인과 OAuth 로그인을 구분하지않고 관리할 수 있다.

```java
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    ...
}

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final LoginRepository loginRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getClientId(); // Social Id;
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String username = oAuth2User.getAttribute("name");
        String password = bCryptPasswordEncoder.encode("password");

        User userEntity = loginRepository.findByEmail(email);
        if (userEntity == null){
            userEntity = User.builder()
                    .email(email)
                    .username(username)
                    .password(password)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            loginRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
```

## OAuth-Client 추가 소셜

- 구글, 페이스북, 트위터 이외의 `OAuth` 로그인은 `application.yml`에 `provider`를 등록해야한다.

```java
naver:
  client-id: gFFZUZs1Sg1nEy0ydXIF
  client-secret: jB4haLnYKl
  scope:
  - name
  - email
  client-name: Naver
  authorization-grant-type: authorization_code
  redirect-url: http://localhost:8080/login/oauth2/code/naver

provider:
  naver:
    authorization-uri: https://nid.naver.com/oauth2.0/authorize # 네이버 로그인찬
    token-uri: https://nid.naver.com/auth2.0/token              # 토큰 Uri
    user-info-uri: http://openapi.naver.com/v1/nid/me           # 프로필 주소 Uri
    user-name-attribute: response                               # 응답 Value Key
```
