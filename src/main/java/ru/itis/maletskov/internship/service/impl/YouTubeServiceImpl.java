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
import ru.itis.maletskov.internship.util.exception.YBotException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class YouTubeServiceImpl implements YouTubeService {
    private HttpClient httpClient = HttpClientBuilder.create().build();
    @Value("${google.youtube.api.key}")
    private String API_KEY;
    @Value("${google.youtube.api.url}")
    private String YOUTUBE_API_URL;

    @Override
    public String searchVideo(String channelName, String videoName, Boolean viewCount, Boolean likeCount) throws IOException, JSONException, YBotException {
        String videoId = getVideoId(channelName, videoName);
        String videoHref = "https://www.youtube.com/watch?v=" + videoId;
        if (!viewCount && !likeCount) {
            return videoHref;
        }
        String statistics = getStatisticsFromVideo(videoId, viewCount, likeCount);
        return videoHref + statistics;
    }

    @Override
    public List<String> getFiveLastVideos(String channelName) throws IOException, JSONException, YBotException {
        channelName = channelName.replace(" ", "+");
        String channelId = getChannelId(channelName);
        StringBuilder sb = new StringBuilder(YOUTUBE_API_URL).append("/search");
        sb.append("?").append("part=").append("snippet").append("&");
        sb.append("channelId=").append(channelId).append("&");
        sb.append("key=").append(API_KEY).append("&");
        sb.append("maxResults=").append("5").append("&");
        sb.append("order=").append("date").append("&");
        sb.append("type=").append("video");
        HttpGet getVideos = new HttpGet(sb.toString());
        HttpResponse response = httpClient.execute(getVideos);
        HttpEntity entity = response.getEntity();
        JSONObject jsonObject = new JSONObject(EntityUtils.toString(entity));
        JSONArray items = jsonObject.getJSONArray("items");
        if (items.length() == 0) {
            throw new YBotException("Videos is not found");
        }
        List<String> videoReferences = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            String videoId = (String) items.getJSONObject(i).getJSONObject("id").get("videoId");
            videoReferences.add("https://www.youtube.com/watch?v=" + videoId);
        }
        return videoReferences;
    }

    @Override
    public String getRandomComment(String channelName, String videoName) throws IOException, YBotException, JSONException {
        String videoId = getVideoId(channelName, videoName);
        StringBuilder sb = new StringBuilder(YOUTUBE_API_URL).append("/commentThreads");
        sb.append("?").append("key=").append(API_KEY).append("&");
        sb.append("part=").append("snippet").append("&");
        sb.append("maxResults=").append("100").append("&");
        sb.append("videoId=").append(videoId);
        HttpGet commentGet = new HttpGet(sb.toString());
        HttpResponse response = httpClient.execute(commentGet);
        HttpEntity entity = response.getEntity();
        JSONArray commentItems = (new JSONObject(EntityUtils.toString(entity))).getJSONArray("items");
        if (commentItems.length() == 0) {
            throw new YBotException("No comments found");
        }
        int commentNumber = (new Random()).nextInt(commentItems.length());
        JSONObject commentInfo = commentItems
                .getJSONObject(commentNumber)
                .getJSONObject("snippet")
                .getJSONObject("topLevelComment")
                .getJSONObject("snippet");
        String commentatorName = (String) commentInfo.get("authorDisplayName");
        String commentaryText = (String) commentInfo.get("textOriginal");
        return commentatorName + "::" + commentaryText;
    }

    private String getChannelId(String channelName) throws IOException, JSONException {
        channelName = channelName.replace(' ', '+');
        StringBuilder sb = new StringBuilder(YOUTUBE_API_URL).append("/search");
        sb.append("?").append("key=").append(API_KEY).append("&");
        sb.append("part=").append("snippet").append("&");
        sb.append("type=").append("channel").append("&");
        sb.append("maxResults=").append("1").append("&");
        sb.append("q=").append(channelName);

        HttpGet request = new HttpGet(sb.toString());
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        JSONObject jsonObject = new JSONObject(EntityUtils.toString(entity));
        JSONArray items = jsonObject.getJSONArray("items");
        if (items.length() != 0) {
            JSONObject channel = items.getJSONObject(0).getJSONObject("id");
            return channel.get("channelId").toString();
        }
        return "";
    }

    private String getVideoId(String channelName, String videoName) throws JSONException, IOException, YBotException {
        videoName = videoName.replace(' ', '+');
        channelName = channelName.replace(' ', '+');
        StringBuilder sb = new StringBuilder(YOUTUBE_API_URL).append("/search");
        sb.append("?").append("key=").append(API_KEY).append("&");
        sb.append("q=").append(videoName).append("&");
        sb.append("part=").append("snippet").append("&");
        sb.append("maxResults=").append("1").append("&");
        sb.append("type=").append("video");
        String channelId = getChannelId(channelName);
        if (!channelId.isEmpty()) {
            sb.append("channelId=").append(channelId);
        }
        HttpGet request = new HttpGet(sb.toString());
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        JSONObject jsonObject = new JSONObject(EntityUtils.toString(entity));
        JSONArray youTubeVideos = jsonObject.getJSONArray("items");
        if (youTubeVideos.length() == 0) {
            throw new YBotException("Video not found");
        }
        return (String) youTubeVideos.getJSONObject(0).getJSONObject("id").get("videoId");
    }

    private String getStatisticsFromVideo(String videoId, Boolean viewCount, Boolean likeCount) throws JSONException, YBotException, IOException {
        StringBuilder requestBuilder = new StringBuilder(YOUTUBE_API_URL).append("/videos");
        requestBuilder.append("?").append("key=").append(API_KEY).append("&");
        requestBuilder.append("part=").append("snippet,statistics").append("&");
        requestBuilder.append("id=").append(videoId);
        HttpGet statGet = new HttpGet(requestBuilder.toString());
        HttpResponse statResponse = httpClient.execute(statGet);
        HttpEntity statEntity = statResponse.getEntity();
        JSONObject statJson = new JSONObject(EntityUtils.toString(statEntity));
        JSONArray itemArr = statJson.getJSONArray("items");
        if (itemArr.length() == 0) {
            throw new YBotException("Video not found");
        }
        String countOfViews = (String) itemArr.getJSONObject(0).getJSONObject("statistics").get("viewCount");
        String countOfLikes = (String) itemArr.getJSONObject(0).getJSONObject("statistics").get("likeCount");
        if (viewCount && likeCount) {
            return " v=" + countOfViews + " l=" + countOfLikes;
        } else if (viewCount) {
            return " v=" + countOfViews;
        } else if (likeCount) {
            return " l=" + countOfLikes;
        } else {
            return "";
        }
    }
}
