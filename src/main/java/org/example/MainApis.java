package org.example;

import com.shaft.driver.SHAFT;
import io.restassured.http.ContentType;

public class MainApis {
    public static final int success_statusCode= 200;
    public static final int delete_statusCode = 201;
    private String authentication_serviceName = "/auth";
    public static  String baseUrl ="https://restful-booker.herokuapp.com";
    String token;
    private SHAFT.API api;

    public MainApis(SHAFT.API api) {
        this.api = api;
    }
    public void login(String username, String password){
        String tokenBody= """
                {
                    "username" : "{USERNAME}",
                    "password" : "{PASSWORD}"
                }
                """
                .replace("{USERNAME}",username)
                .replace("{PASSWORD}", password);
        api.post(authentication_serviceName).setContentType(ContentType.JSON).setRequestBody(tokenBody)
                .setTargetStatusCode(success_statusCode).perform();
        token = api.getResponseJSONValue("token");
        api.addHeader("Cookie", "token="+token);
    }
}
