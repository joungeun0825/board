package board.ift.post;

import board.ift.answer.Answer;
import board.ift.answer.AnswerRepository;
import board.ift.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AnswerRepository answerRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 게시글 작성
    public void savePost(String title, String content, User user) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .status(false)
                .user(user)
                .build();
        postRepository.save(post);
    }

    // 게시글 수정
    public void updatePost(Long id, String title, String content) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id"));
        post.setTitle(title);
        post.setContent(content);
        postRepository.save(post);
    }

    // 게시글 삭제
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post Id"));
        postRepository.delete(post); // 게시글 삭제
    }

    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public Page<Post> getPostsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return postRepository.findByCreatedDateBetween(startDate, endDate, pageable);
    }

    public void addAnswer(Long postId, String answerContent) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        // 새로운 답변 객체 생성
        Answer answer = new Answer();
        answer.setPost(post);
        answer.setContent(answerContent);
        answer.setCreatedDate(LocalDate.now());

        // 답변 저장
        answerRepository.save(answer);
    }

    // 게시글 상태 업데이트
    public void updatePost(Post post) {
        postRepository.save(post);
    }
}
