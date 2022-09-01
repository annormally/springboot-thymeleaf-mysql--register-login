package springboot.thymeleaf.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springboot.thymeleaf.entity.AuthoritiesEntity;
import springboot.thymeleaf.entity.UserEntity;
import springboot.thymeleaf.model.UserModel;
import springboot.thymeleaf.repository.UserAuthoritiesRepository;
import springboot.thymeleaf.repository.UserEntityRepository;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserEntityRepository userEntityRepository;
    private final UserAuthoritiesRepository userAuthoritiesRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserEntityRepository userEntityRepository, UserAuthoritiesRepository userAuthoritiesRepository, PasswordEncoder passwordEncoder) {
        logger.info(getClass() + " working ...");
        this.userEntityRepository = userEntityRepository;
        this.userAuthoritiesRepository = userAuthoritiesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public ModelAndView getRegisterOnDash() {
        return new ModelAndView("redirect:/register");
    }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("user", new UserModel());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView setUserFields(@ModelAttribute("user") UserModel userModel) {
        ModelAndView modelAndView = new ModelAndView("redirect:/login");

        UserEntity userEntity;
        if (userModel.getId() != null) {
            userEntity = userEntityRepository.findById(userModel.getId()).get();
        } else {
            userEntity = new UserEntity();
        }

        userEntity.setLocked(true);
        userEntity.setUsername(userModel.getUsername().trim());
        userEntity.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userEntity.setEmail(userModel.getEmail());

        AuthoritiesEntity authoritiesEntity;
        if (userModel.getId() != null) {
            authoritiesEntity = userAuthoritiesRepository.findById(userModel.getId()).get();
        } else {
            authoritiesEntity = new AuthoritiesEntity();
        }

        authoritiesEntity.setUsername(userModel.getUsername());
        authoritiesEntity.setAuthority("USER");
        authoritiesEntity.setUser(userEntity);

        userEntityRepository.save(userEntity);

        userAuthoritiesRepository.save(authoritiesEntity);

        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView getLogin() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    public String currentUsername(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return principal.getName();
    }

    @GetMapping("/successLogin")
    public ModelAndView getSuccessLogin() {
        return new ModelAndView("successLogin");
    }
}
