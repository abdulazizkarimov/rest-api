import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import model.Post;
import model.User;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.MapperUtil;
import util.RestUtil;
import java.util.List;

public class MyTest {
    private static final int STATUS_OK = 200;
    private static final int STATUS_CREATED = 201;
    private static final int STATUS_NOT_FOUND = 404;
    private static final String APPLICATION_JSON = "application/json";

    @Test
    public void myTestCase() {
        ISettingsFile envConfigData = new JsonSettingsFile("configdata.json");
        ISettingsFile envTestData = new JsonSettingsFile("testdata.json");

        String route_posts = envConfigData.getValue("/route_posts").toString();
        String route_users = envConfigData.getValue("/route_users").toString();
        String user_json = envTestData.getValue("/user_json").toString();
        String empty_body = envTestData.getValue("/empty_body").toString();
        int id_5 = Integer.valueOf(envTestData.getValue("/id_5").toString());
        int id_10 = Integer.valueOf(envTestData.getValue("/id_10").toString());
        int id_99 = Integer.valueOf(envTestData.getValue("/id_99").toString());
        int id_150 = Integer.valueOf(envTestData.getValue("/id_150").toString());

        //-----------------------------------------------

        HttpResponse<JsonNode> response = RestUtil.getRequest(route_posts);
        Assert.assertEquals(response.getStatus(), STATUS_OK, "Status is not equal to 200");

        String contentType = response.getHeaders().get("Content-Type").toString();
        Assert.assertTrue(contentType.contains(APPLICATION_JSON), "Content type is not json");

        List<Post> postList = MapperUtil.mapToPostList(response.getBody().getArray().toString());
        Assert.assertTrue(Post.isSorted(postList), "Posts are not sorted my id");

        //-----------------------------------------------

        response = RestUtil.getRequest(route_posts + '/' + id_99);
        Assert.assertEquals(response.getStatus(), STATUS_OK, "Status is not equal to 200");

        Post post = MapperUtil.mapToPost(response.getBody().toString());
        Assert.assertEquals(post.getUserId(), id_10, "userId is not equal to 10");
        Assert.assertEquals(post.getId(), id_99, "id is not equal to 99");
        Assert.assertNotNull(post.getTitle(), "Title is empty");
        Assert.assertNotNull(post.getBody(), "Body is empty");

        //-----------------------------------------------

        response = RestUtil.getRequest(route_posts + '/' + id_150);
        Assert.assertEquals(response.getStatus(), STATUS_NOT_FOUND, "Status is not equal to 404");
        Assert.assertEquals(response.getBody().toString(), empty_body, "Body is not empty");

        //-----------------------------------------------

        Post p1 = new Post(1, "AlexExample", "example_body");
        response = RestUtil.postRequestPost(route_posts, p1);
        Assert.assertEquals(response.getStatus(), STATUS_CREATED, "Status is not equal to 201");

        Post p2 = MapperUtil.mapToPost(response.getBody().toString());
        Assert.assertEquals(p1.getUserId(), p2.getUserId(), "Incorrect userId");
        Assert.assertEquals(p1.getTitle(), p2.getTitle(), "Incorrect title");
        Assert.assertEquals(p1.getBody(), p2.getBody(), "Incorrect body");
        Assert.assertTrue(p2.getId() > 0, "id is not present");

        //-----------------------------------------------

        response = RestUtil.getRequest(route_users);

        contentType = response.getHeaders().get("Content-Type").toString();
        Assert.assertTrue(contentType.contains(APPLICATION_JSON), "Content type is not json");

        List<User> userList = MapperUtil.mapToUserList(response.getBody().getArray().toString());

        User u1 = MapperUtil.mapToUser(user_json);
        User u2 = User.getUserById(userList, id_5);

        Assert.assertTrue(u1.equals(u2), "Data is incorrect");

        //-----------------------------------------------

        response = RestUtil.getRequest(route_users + '/' + id_5);

        u1 = MapperUtil.mapToUser(user_json);
        u2 = MapperUtil.mapToUser(response.getBody().toString());

        Assert.assertTrue(u1.equals(u2), "Data is incorrect");
    }
}