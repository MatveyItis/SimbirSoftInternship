package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.maletskov.internship.service.YouTubeService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class YouTubeServiceImpl implements YouTubeService {
    private HttpClient httpClient = HttpClientBuilder.create().build();
    @Value("${google.youtube.api.key}")
    private String API_KEY;
    @Value("${google.youtube.api.url}")
    private String YOUTUBE_API_URL;

    @Override
    public String searchVideo(String channel, String videoName) throws IOException, JSONException {
        videoName = videoName.replace(' ', '+');
        StringBuilder sb = new StringBuilder(YOUTUBE_API_URL).append("/search");
        sb.append("?").append("key=").append(API_KEY).append("&");
        sb.append("q=").append(videoName).append("&");
        sb.append("part=").append("snippet").append("&");
        sb.append("maxResults=").append("4").append("&");
        sb.append("type=").append("video");
        String channelId = getChannelId(channel);
        if (!channelId.isEmpty()) {
            sb.append("channelId=").append(channelId);
        }
        HttpGet request = new HttpGet(sb.toString());

        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        JSONObject jsonObject = new JSONObject(EntityUtils.toString(entity));
        JSONArray youTubeVideos = jsonObject.getJSONArray("items");
        if (youTubeVideos.length() != 0) {
            String videoId = (String) youTubeVideos.getJSONObject(0).getJSONObject("id").get("videoId");
            return "https://www.youtube.com/watch?v=" + videoId;
        }
        return "Video is not found";
    }

    private String getChannelId(String channelName) throws IOException, JSONException {
        channelName = channelName.replace(' ', '+');
        StringBuilder sb = new StringBuilder(YOUTUBE_API_URL).append("/channels");
        sb.append("?").append("key=").append(API_KEY).append("&");
        sb.append("part=").append("snippet").append("&");
        sb.append("forUsername=").append(channelName);

        HttpGet request = new HttpGet(sb.toString());
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        JSONObject jsonObject = new JSONObject(EntityUtils.toString(entity));
        JSONArray items = jsonObject.getJSONArray("items");
        if (items.length() != 0) {
            JSONObject channel = items.getJSONObject(0);
            return channel.get("id").toString();
        }
        return "";
    }
}
