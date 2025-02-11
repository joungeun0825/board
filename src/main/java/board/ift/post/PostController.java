package board.ift.post;

import board.ift.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public String boardPage(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Pageable pageable = PageRequest.of(page, 3);
        Page<Post> postPage = postService.getPosts(pageable);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("currentPage", page);
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

    // 게시글 수정 페이지
    @GetMapping("/post/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model, HttpSession session) {
        Post post = postService.getPostById(id);

        if (post == null) {
            return "redirect:/board";
        }

        // 현재 로그인한 사용자가 작성자인지 확인
        User sessionUser = (User) session.getAttribute("user");
        if (!post.getUser().getId().equals(sessionUser.getId())) {
            return "redirect:/board"; // 작성자가 아니면 목록으로 리다이렉트
        }

        model.addAttribute("post", post);
        return "edit"; // 수정 페이지로 이동
    }

    // 게시글 수정 처리
    @PostMapping("/post/{id}/edit")
    public String editPost(@PathVariable Long id, @RequestParam String title,
                           @RequestParam String content, HttpSession session) {
        Post post = postService.getPostById(id);

        if (post == null) {
            return "redirect:/board";
        }

        // 현재 로그인한 사용자가 작성자인지 확인
        User sessionUser = (User) session.getAttribute("user");
        if (!post.getUser().getId().equals(sessionUser.getId())) {
            return "redirect:/board"; // 작성자가 아니면 목록으로 리다이렉트
        }

        // 게시글 수정
        postService.updatePost(id, title, content);
        return "redirect:/post/" + id; // 수정된 게시글 상세보기 페이지로 리다이렉트
    }

    // 게시글 삭제
    @PostMapping("/post/{id}/delete")
    public String deletePost(@PathVariable Long id, HttpSession session) {
        Post post = postService.getPostById(id);

        if (post == null) {
            return "redirect:/board";
        }

        // 현재 로그인한 사용자가 작성자인지 확인
        User sessionUser = (User) session.getAttribute("user");
        if (!post.getUser().getId().equals(sessionUser.getId())) {
            return "redirect:/board"; // 작성자가 아니면 목록으로 리다이렉트
        }

        // 게시글 삭제
        postService.deletePost(id);
        return "redirect:/board"; // 게시글 목록으로 리다이렉트
    }
}
