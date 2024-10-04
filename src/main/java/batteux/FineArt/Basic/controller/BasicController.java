package batteux.FineArt.Basic.controller;

import batteux.FineArt.Basic.model.User;
import batteux.FineArt.Basic.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class BasicController {

    @Autowired
    private UserService userService;

    // 회원가입 처리
    @PostMapping("/signup")
    public void signup(@RequestBody User user, HttpServletResponse response) throws IOException {
        userService.createUser(user);
        response.sendRedirect("/index");  
    }

    // 특정 사용자 조회
    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getUserById(id);  
    }

    // 로그인 처리
    @PostMapping("/login")
    public void login(@RequestParam String username, @RequestParam String password, HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = userService.login(username, password);

        if (user != null) {
            HttpSession session = request.getSession();  
            session.setAttribute("loggedInUser", user); 
            response.sendRedirect("/index"); 
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("loginError", "Invalid username or password.");
            response.sendRedirect("/login");  
        }
    }
    
    // 로그아웃 처리
    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false); 
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("/index"); 
    }

    // 로그인 상태 확인
    @GetMapping("/status")
    public String loginStatus(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedInUser") != null) {
            return "User is logged in";
        } else {
            return "User is not logged in";
        }
    }
}

