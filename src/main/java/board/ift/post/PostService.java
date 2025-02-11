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

    public void savePost(String title, String content, User user) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .status(false)
                .user(user)
                .build();
        postRepository.save(post);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }
}
