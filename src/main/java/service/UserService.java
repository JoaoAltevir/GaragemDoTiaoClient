package service;

import network.ConnectionClient;
import com.google.gson.JsonObject;
import network.*;
import entities.*;

public class UserService {
    
    private ConnectionClient connection;
    private JsonObject res;

    public UserService() {
        this.connection = new ConnectionClient();
        this.res = new JsonObject();
    }


    public JsonObject login(String username, String password) {
        JsonObject data = new JsonObject();
        
        data.addProperty("username", username);
        data.addProperty("password", password);

        JsonObject resData = connection.sendRequest("login", data); 
        
        
        data = resData.get("data").getAsJsonObject();
        String token = data.get("token").getAsString();
       
        Session.setToken(token);
        Session.setUsername(username);
        
        System.out.println(resData);
        
        return resData;
        
    }

    public JsonObject register(String name, String username, String password) {
        JsonObject data = new JsonObject();
        data.addProperty("name", name);
        data.addProperty("username", username);
        data.addProperty("password", password);
        
        res = connection.sendRequest("register", data);
        System.out.println(res);
        return res;
    }

    public JsonObject logout() {
        JsonObject data = new JsonObject();
        data.addProperty("token", Session.getToken()); // Utiliza o token da sessão ativa
        
        res = connection.sendRequest("logout", data);
        System.out.println(res);
        return res;
    }

    public User getUser(String usernameTarget) {
        JsonObject data = new JsonObject();
        User user = new User();
        
        data.addProperty("token", Session.getToken());
        data.addProperty("username", usernameTarget);
        res = connection.sendRequest("getuser", data);
        System.out.println(res);
        user.setName(res.get("name").getAsString());
        user.setUsername(res.get("username").getAsString());
        
        return user;
    }

    public JsonObject updateUserName(String usernameTarget, String newName) {
        JsonObject data = new JsonObject();
        data.addProperty("token", Session.getToken());
        data.addProperty("username", usernameTarget);
        data.addProperty("name", newName);
        res = connection.sendRequest("updateusername", data);
        System.out.println(res);
        return res;
    }

    public JsonObject updateUserPassword(String usernameTarget, String oldPassword, String newPassword) {
        JsonObject data = new JsonObject();
        data.addProperty("token", Session.getToken());
        data.addProperty("username", usernameTarget);
        data.addProperty("oldPassword", oldPassword);
        data.addProperty("newPassword", newPassword);
        res = connection.sendRequest("updateuserpassword", data);
        System.out.println(res);
        return res;
    }

    public JsonObject deleteUser(String usernameTarget) {
        JsonObject data = new JsonObject();
        data.addProperty("token", Session.getToken());
        data.addProperty("username", usernameTarget);
        res = connection.sendRequest("deleteuser", data);
        System.out.println(res);
        return res;
    }
}
