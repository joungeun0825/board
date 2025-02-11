package board.ift.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.createdDate BETWEEN :startDate AND :endDate")
    Page<Post> findByCreatedDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
}