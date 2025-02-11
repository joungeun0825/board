package board.ift.auth;

import board.ift.user.Role;
import board.ift.user.User;
import board.ift.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User loginOrRegister(String username, String password) {

        username = username.trim();

        Optional<User> userOptional = userRepository.findByUsername(username);

        // 존재하면 로그인
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (password.equals(user.getPassword())) {
                return user;
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        }

        Role role = (username.equals("admin")) ? Role.ADMIN : Role.USER;

        // 존재하지 않으면 회원가입 후 로그인
        User newUser = User.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();

        return userRepository.save(newUser);
    }
}
