package service;

import network.ConnectionClient;
import com.google.gson.JsonObject;
import network.*;
import entities.*;

public class UserService {
    
    private ConnectionClient connection;

    public UserService(String host, int port) {
        this.connection = new ConnectionClient(host, port);
    }


    public String login(String username, String password) {
        JsonObject data = new JsonObject();
        data.addProperty("username", username);
        data.addProperty("password", password);

        JsonObject resData = connection.sendRequest("login", data); 
        
        return resData.get("token").getAsString();
        
    }

    public JsonObject register(String name, String username, String password) {
        JsonObject data = new JsonObject();
        data.addProperty("name", name);
        data.addProperty("username", username);
        data.addProperty("password", password);
        return connection.sendRequest("register", data);
    }

    public JsonObject logout() {
        JsonObject data = new JsonObject();
        data.addProperty("token", Session.getToken()); // Utiliza o token da sessão ativa
        return connection.sendRequest("logout", data);
    }

    public JsonObject getUser(String usernameTarget) {
        JsonObject data = new JsonObject();
        data.addProperty("token", Session.getToken());
        data.addProperty("username", usernameTarget);
        return connection.sendRequest("getuser", data);
    }

    public JsonObject updateUserName(String usernameTarget, String newName) {
        JsonObject data = new JsonObject();
        data.addProperty("token", Session.getToken());
        data.addProperty("username", usernameTarget);
        data.addProperty("name", newName);
        return connection.sendRequest("updateusername", data);
    }

    public JsonObject updateUserPassword(String usernameTarget, String oldPassword, String newPassword) {
        JsonObject data = new JsonObject();
        data.addProperty("token", Session.getToken());
        data.addProperty("username", usernameTarget);
        data.addProperty("oldPassword", oldPassword);
        data.addProperty("newPassword", newPassword);
        return connection.sendRequest("updateuserpassword", data);
    }

    public JsonObject deleteUser(String usernameTarget) {
        JsonObject data = new JsonObject();
        data.addProperty("token", Session.getToken());
        data.addProperty("username", usernameTarget);
        return connection.sendRequest("deleteuser", data);
    }
}
