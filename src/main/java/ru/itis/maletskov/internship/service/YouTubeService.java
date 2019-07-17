package ru.itis.maletskov.internship.service;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public interface YouTubeService {
    String searchVideo(String channel, String videoName) throws IOException, JSONException;
}
