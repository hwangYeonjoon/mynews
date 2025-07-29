package api.news.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class NaverNewsService {

    private final String clientId = "YBdVp_ZcCRGnQFEFn5H2";
    private final String clientSecret = "a_ceBG7h7R";

    private final RestTemplate restTemplate = new RestTemplate();

    public String searchNews(String keyword, String startDate, String endDate) {
        try {
            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            String url = "https://openapi.naver.com/v1/search/news.json?query=" + encodedKeyword
                    + "&display=1000&sort=date"; // 필요시 display 수 조절

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JSONObject resultJson = new JSONObject(response.getBody());
            JSONArray items = resultJson.getJSONArray("items");

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String title = item.getString("title").replaceAll("<.*?>", "");
                String description = item.getString("description").replaceAll("<.*?>", "");
                String pubDate = item.getString("pubDate");
                String link = item.getString("link");

                sb.append("🗓 ").append(pubDate).append("\n");
                sb.append("📌 제목: ").append(title).append("\n");
                sb.append("🔍 내용: ").append(description).append("\n");
                sb.append("🔗 링크: ").append(link).append("\n");
                sb.append("--------------------------------------------------\n");
            }

            return sb.toString();

        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
}