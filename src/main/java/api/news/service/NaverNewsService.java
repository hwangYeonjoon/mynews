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
            int display = 100;
            int maxResults = 1000;
            int totalCollected = 0;
            StringBuilder sb = new StringBuilder();

            // 날짜 파싱용 포맷
            var formatter = new java.text.SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", java.util.Locale.ENGLISH);
            var userFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd");

            java.util.Date start = startDate != null ? userFormatter.parse(startDate) : null;
            java.util.Date end = endDate != null ? userFormatter.parse(endDate) : null;

            for (int startIndex = 1; startIndex <= maxResults; startIndex += display) {
                String url = "https://openapi.naver.com/v1/search/news.json?query=" + encodedKeyword
                        + "&display=" + display
                        + "&start=" + startIndex
                        + "&sort=date";

                HttpHeaders headers = new HttpHeaders();
                headers.set("X-Naver-Client-Id", clientId);
                headers.set("X-Naver-Client-Secret", clientSecret);

                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

                JSONObject resultJson = new JSONObject(response.getBody());

                if (startIndex == 1) {
                    int total = resultJson.getInt("total");
                    maxResults = Math.min(maxResults, total);
                    if (total == 0) return "⚠️ 검색 결과가 없습니다.";
                }

                JSONArray items = resultJson.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    String title = item.getString("title").replaceAll("<.*?>", "");
                    String description = item.getString("description").replaceAll("<.*?>", "");
                    String pubDateRaw = item.getString("pubDate");
                    String link = item.getString("link");

                    java.util.Date pubDate = formatter.parse(pubDateRaw);

                    // ✅ 날짜 필터링
                    if ((start != null && pubDate.before(start)) || (end != null && pubDate.after(end))) {
                        continue;
                    }

                    // ✅ 키워드 포함 필터링
                    if (!(title.contains(keyword) || description.contains(keyword))) {
                        continue;
                    }

                    sb.append("🗓 ").append(pubDateRaw).append("\n");
                    sb.append("📌 제목: ").append(title).append("\n");
                    sb.append("🔍 내용: ").append(description).append("\n");
                    sb.append("🔗 링크: ").append(link).append("\n");
                    sb.append("--------------------------------------------------\n");

                    totalCollected++;
                    if (totalCollected >= maxResults) break;
                }

                if (totalCollected >= maxResults) break;
            }

            return totalCollected == 0 ? "⚠️ 검색 조건에 맞는 결과가 없습니다." : sb.toString();

        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
}