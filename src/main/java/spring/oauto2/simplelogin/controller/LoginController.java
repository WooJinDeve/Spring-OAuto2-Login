package spring.oauto2.simplelogin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.oauto2.simplelogin.dto.UserInfoDto;
import spring.oauto2.simplelogin.repository.LoginRepository;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginRepository loginRepository;

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public String info(Model model){
        List<UserInfoDto> users = loginRepository.findAll().stream().map(UserInfoDto::new)
                .collect(Collectors.toList());
        model.addAttribute("users", users);
        return "userInfo";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    @ResponseBody
    public String data(Model model){
        return "userData";
    }
}
