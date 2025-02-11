package board.ift.post;

import board.ift.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/post/create")
    public String createPostForm() {
        return "create-post";  // 문의글 작성 페이지로 이동
    }

    @PostMapping("/post/create")
    public String createPost(@RequestParam String title, @RequestParam String content, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        postService.savePost(title, content, user);
        return "redirect:/board";
    }

    @GetMapping("/post/{id}")
    public String getPostDetail(@PathVariable Long id, Model model) {
        // 게시글을 ID로 조회
        Post post = postService.getPostById(id);

        if (post == null) {
            // 게시글이 없으면 에러 페이지나 404 처리
            return "redirect:/board"; // 게시글 목록으로 리다이렉트
        }

        // 모델에 게시글을 추가하여 뷰에 전달
        model.addAttribute("post", post);

        // 상세보기 페이지로 이동
        return "view"; // Thymeleaf 템플릿을 렌더링
    }
}
