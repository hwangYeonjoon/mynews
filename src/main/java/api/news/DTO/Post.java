package api.news.DTO;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Post {
    private Long id;
    private String content;
    private Date date;
    private List<Comment> comments;
}