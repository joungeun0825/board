package board.ift.auth;

import board.ift.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class AuthController {
    private final AuthService authService;

    @GetMapping
    public String loginPage() {
        return "login";
    }

    @PostMapping
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletRequest request) {
        try {
            User user =  authService.loginOrRegister(username, password);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return "redirect:/board";
        } catch (IllegalArgumentException e) {
            return "redirect:/login?error=true";  // 로그인 실패 시 /login으로 리다이렉트
        }
    }

}
