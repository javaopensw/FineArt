package batteux.FineArt.Basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import batteux.FineArt.Basic.model.User; // User 클래스 경로에 따라 변경 가능
import batteux.FineArt.Basic.service.UserService;
@Controller
public class PageController {

    @Autowired
    private UserService userService;
	
    // 회원가입 페이지로 이동 (HTML)
    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";  // signup.html을 렌더링
    }
   
    // 로그인 페이지로 이동 (HTML)
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; 
    }
    
    // 계정찾기
    @GetMapping("/findacc")
    public String showFindAccountPage() {
        return "findacc";  
    }

    @PostMapping("/findacc")
    public String verifyAccount(@RequestParam("emailOrLoginName") String emailOrLoginName, HttpSession session, Model model) {
        User user = userService.findUserByEmailOrLoginName(emailOrLoginName);
        if (user != null) {
            System.out.println("사용자 발견: " + user.getUsername()); 
            session.setAttribute("verifiedUser", user);  
            return "redirect:/resetpass";  
        } else {
            model.addAttribute("error", "계정을 찾을 수 없습니다.");
            System.out.println("사용자 발견 실패: " + emailOrLoginName);
            return "findacc";  
        }
    }
   
    @GetMapping("/resetpass")
    public String showResetPasswordPage(HttpSession session) {
        User user = (User) session.getAttribute("verifiedUser");  
        if (user != null) {
            return "resetpass"; 
        } else {
            return "redirect:/findacc";  
        }
    }

    @PostMapping("/resetpass")
    public String resetPassword(@RequestParam("newPassword") String newPassword, HttpSession session) {
        User user = (User) session.getAttribute("verifiedUser"); 
        if (user != null) {
            userService.updatePassword(user.getUserId(), newPassword);
            session.invalidate(); 
            return "redirect:/login";  
        } else {
            return "redirect:/findacc";  
        }
    }
    
    @GetMapping("/myacc")
    public String showMyAccountPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedInUser") != null) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            model.addAttribute("user", loggedInUser); 
            return "myacc";  
        } else {
            return "redirect:/login"; 
        }
    }
    
    @PostMapping("/myaccch")
    public String updateMyAccount(@ModelAttribute User user, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedInUser") != null) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            user.setUserId(loggedInUser.getUserId()); 
            if (user.getPassword().isEmpty()) {
                user.setPassword(loggedInUser.getPassword());  
            }
            userService.updateUser(user);  
            session.setAttribute("loggedInUser", user);  
            return "redirect:/myacc"; 
        } else {
            return "redirect:/login";  
        }
    }

    @GetMapping("/myaccch")
    public String showMyAccountEditPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedInUser") != null) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            model.addAttribute("user", loggedInUser); 
            return "myaccch";  
        } else {
            return "redirect:/login"; 
        }
    }

    // index 페이지로 이동
    @GetMapping("/index")
    public String showIndexPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedInUser") != null) {
            User loggedInUser = (User) session.getAttribute("loggedInUser"); 
            request.setAttribute("username", loggedInUser.getUsername()); 
            return "L_index";  
        } else {
            return "G_index";  
        }
    }

    // 유저 조회 페이지로 이동 (HTML)
    @GetMapping("/findUser")
    public String showFindUserPage() {
        return "user";  
    }
     
    @GetMapping("/pong")
    public String showPongPage() {
        return "pong";  // signup.html을 렌더링
    }
    
}
