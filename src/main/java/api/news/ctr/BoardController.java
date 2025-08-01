package api.news.ctr;

import api.news.DTO.Comment;
import api.news.DTO.Post;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final List<Post> posts = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong();

    // 📝 글 작성
    @PostMapping
    public Post writePost(@RequestBody Post post) {
        post.setId(idGenerator.incrementAndGet());
        post.setDate(new Date());
        post.setComments(new ArrayList<>());
        posts.add(0, post); // 최신 글이 위로
        return post;
    }

    // 📜 글 목록 조회
    @GetMapping
    public List<Post> getPosts() {
        return posts;
    }

    // 💬 댓글 작성
    @PostMapping("/{id}/comments")
    public Comment addComment(@PathVariable Long id, @RequestBody Comment comment) {
        Post target = posts.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
        if (target == null) return null;

        comment.setDate(new Date());
        target.getComments().add(comment);
        return comment;
    }

    // 🧪 단건 조회도 원한다면 (선택)
    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return posts.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }
}