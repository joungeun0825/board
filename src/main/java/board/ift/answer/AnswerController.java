package board.ift.answer;

import board.ift.post.Post;
import board.ift.post.PostService;
import board.ift.user.Role;
import board.ift.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AnswerController {
    private final PostService postService;

    @GetMapping("/post/{id}/answer")
    public String answerForm(@PathVariable Long id, Model model, HttpSession session) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return "redirect:/board";
        }

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null || sessionUser.getRole() != Role.ADMIN) {
            return "redirect:/board";  // 관리자가 아니면 목록으로 리다이렉트
        }

        model.addAttribute("post", post);
        return "answerForm";  // 답변 페이지로 이동
    }

    @PostMapping("/post/{id}/answer")
    public String answerPost(@PathVariable Long id, @RequestParam String answer, HttpSession session) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return "redirect:/board";
        }

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null || sessionUser.getRole() != Role.ADMIN) {
            return "redirect:/board";  // 관리자가 아니면 목록으로 리다이렉트
        }

        // 답변 처리
        postService.addAnswer(id, answer);
        post.setStatus(true);  // 답변 완료 상태로 설정
        postService.updatePost(post);

        return "redirect:/post/{id}";  // 수정된 게시글 상세보기 페이지로 리다이렉트
    }
}
