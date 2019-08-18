package ru.itis.maletskov.internship.service;

import org.json.JSONException;
import ru.itis.maletskov.internship.util.exception.YBotException;

import java.io.IOException;
import java.util.List;

public interface YouTubeService {
    String searchVideo(String channel, String videoName, Boolean viewCount, Boolean likeCount) throws IOException, JSONException, YBotException;

    List<String> getFiveLastVideos(String channelName) throws IOException, JSONException, YBotException;

    String getRandomComment(String channelName, String videoName) throws IOException, YBotException, JSONException;
}
