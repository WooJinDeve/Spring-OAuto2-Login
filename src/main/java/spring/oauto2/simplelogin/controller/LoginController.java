package spring.oauto2.simplelogin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.oauto2.simplelogin.auth.PrincipalDetails;
import spring.oauto2.simplelogin.dto.UserInfoDto;
import spring.oauto2.simplelogin.repository.LoginRepository;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginRepository loginRepository;

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        model.addAttribute("users", principalDetails.getUser());
        return "userInfo";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }
}
