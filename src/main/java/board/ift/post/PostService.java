package board.ift.post;

import board.ift.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

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

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }
}
