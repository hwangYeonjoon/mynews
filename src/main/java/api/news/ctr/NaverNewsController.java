package api.news.ctr;

import api.news.service.NaverNewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
public class NaverNewsController {

    private final NaverNewsService newsService;

    public NaverNewsController(NaverNewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<String> getNews(
            @RequestParam String keyword,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        String result = newsService.searchNews(keyword, startDate, endDate);
        return ResponseEntity.ok(result);
    }
}
