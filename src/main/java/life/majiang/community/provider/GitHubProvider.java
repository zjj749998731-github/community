package life.majiang.community.provider;

import com.alibaba.fastjson.JSON;
import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;


@Component
public class GitHubProvider {

    //使用okhttp发送POST请求
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try{
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            //从响应体access_token=0a710f6eafc53798634a1ac7ced2c06e0b0dde6f&scope=user&token_type=bearer解析出0a710f6eafc53798634a1ac7ced2c06e0b0dde6f
            String accessToken = string.split("&")[0].split("=")[1];
            System.out.println(accessToken);
            return accessToken;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //使用okhttp发送GET请求
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
