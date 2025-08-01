package api.news.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private String content;
    private Date date;
}