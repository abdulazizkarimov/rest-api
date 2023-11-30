package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Post;
import model.User;
import java.util.List;

public class MapperUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    public static List<Post> mapToPostList(String s) {
        List<Post> postList = null;

        try {
            postList = mapper.readValue(s, new TypeReference<List<Post>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return postList;
    }

    public static Post mapToPost(String s) {
        Post post = null;

        try {
            post = mapper.readValue(s, Post.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return post;
    }

    public static List<User> mapToUserList(String s) {
        List<User> userList = null;

        try {
            userList = mapper.readValue(s, new TypeReference<List<User>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return userList;
    }

    public static User mapToUser(String s) {
        User user = null;

        try {
            user = mapper.readValue(s, User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return user;
    }
}