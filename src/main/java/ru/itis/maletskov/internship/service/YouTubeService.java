package ru.itis.maletskov.internship.service;

import org.json.JSONException;

import java.io.IOException;

public interface YouTubeService {
    String searchVideo(String channel, String videoName, Boolean viewCount, Boolean likeCount) throws IOException, JSONException;
}
