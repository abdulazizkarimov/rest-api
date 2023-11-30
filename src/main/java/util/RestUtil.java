package util;

import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import model.Post;

public class RestUtil {
    private static ISettingsFile envConfigData = new JsonSettingsFile("configdata.json");

    public static HttpResponse<JsonNode> getRequest(String s) {
        HttpResponse<JsonNode> response = null;

        try {
            response = Unirest.get(envConfigData.getValue("/api").toString() + s).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static HttpResponse<JsonNode> postRequestPost(String s, Post p1) {
        HttpResponse<JsonNode> response = null;

        try {
            response = Unirest.post(envConfigData.getValue("/api").toString() + s)
                    .field("userId", p1.getUserId())
                    .field("title", p1.getTitle())
                    .field("body", p1.getBody())
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return response;
    }
}