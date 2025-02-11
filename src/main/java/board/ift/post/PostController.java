package board.ift.post;

import board.ift.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/board")
    public String boardPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        model.addAttribute("username", user.getUsername());
        return "board";
    }

    @PostMapping("/board")
    public String createPost(@RequestParam String title, @RequestParam String content, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        postService.savePost(title, content, user);
        return "redirect:/board";
    }
}
