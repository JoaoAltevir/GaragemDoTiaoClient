package service;

import network.ConnectionClient;

import javax.swing.JOptionPane;

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
        this.res = resData;
        
        int statusCode = resData.get("statusCode").getAsInt();
        
        if (statusCode != 200) {
            return resData;
        }
        
        data = resData.get("data").getAsJsonObject();
        String token = data.get("token").getAsString();

        Session.setToken(token);
        Session.setUsername(username);
        if (data.has("role") && !data.get("role").isJsonNull()) {
            Session.setRole(data.get("role").getAsString());
        }
        
        System.out.println("[CLIENTE] Login: " + resData);
        
        return resData;
        
    }

    public JsonObject register(String name, String username, String password) {
        return register(name, username, password, null, null);
    }

    public JsonObject register(String name, String username, String password, String role) {
        return register(name, username, password, role, Session.getToken());
    }

    public JsonObject register(String name, String username, String password, String role, String token) {
        JsonObject data = new JsonObject();
        data.addProperty("name", name);
        data.addProperty("username", username);
        data.addProperty("password", password);

        if (role != null && !role.isEmpty()) {
            data.addProperty("role", role);
        }
        if (token != null && !token.isEmpty()) {
            data.addProperty("token", token);
        }
        
        res = connection.sendRequest("register", data);
        return res;
    }

    public JsonObject logout() {
        JsonObject data = new JsonObject();
        data.addProperty("token", Session.getToken()); // Utiliza o token da sessão ativa
        
        res = connection.sendRequest("logout", data);
        System.out.println("[CLIENTE] Logout: " + res);

        Session.setToken(null);
        Session.setUsername(null);
        Session.setRole(null);
        
        return res;
    }

    public User getUser(String usernameTarget) {
        JsonObject data = new JsonObject();
        User user = new User();
        
        data.addProperty("token", Session.getToken());
        data.addProperty("username", usernameTarget);
        res = connection.sendRequest("getuser", data);

        if (res != null && res.has("data") && !res.get("data").isJsonNull()) {
            JsonObject resData = res.get("data").getAsJsonObject();

            if (resData.has("name") && !resData.get("name").isJsonNull()) {
                user.setName(resData.get("name").getAsString());
            }
            if (resData.has("username") && !resData.get("username").isJsonNull()) {
                user.setUsername(resData.get("username").getAsString());
            }
            if (resData.has("role") && !resData.get("role").isJsonNull()) {
                user.setRole(resData.get("role").getAsString());
                if (usernameTarget != null && usernameTarget.equals(Session.getUsername())
                        && (user.getUsername() == null || user.getUsername().equals(Session.getUsername()))) {
                    Session.setRole(user.getRole());
                }
            }
        }
        
        return user;
    }

    public JsonObject updateUserName(String newName) {
        return updateUserName(Session.getUsername(), newName);
    }

    public JsonObject updateUserName(String usernameTarget, String newName) {
        JsonObject data = new JsonObject();
        
        data.addProperty("token", Session.getToken());
        data.addProperty("username", usernameTarget);
        data.addProperty("name", newName);
        res = connection.sendRequest("updateusername", data);
        
        return res;
    }

    public JsonObject updateUserPassword(String oldPassword, String newPassword) {
        return updateUserPassword(Session.getUsername(), oldPassword, newPassword);
    }

    public JsonObject updateUserPassword(String usernameTarget, String oldPassword, String newPassword) {
        JsonObject data = new JsonObject();
        
        data.addProperty("token", Session.getToken());
        data.addProperty("username", usernameTarget);
        data.addProperty("oldPassword", oldPassword);
        data.addProperty("newPassword", newPassword);
        res = connection.sendRequest("updateuserpassword", data);
        
        return res;
    }

    public JsonObject deleteUser() {
        return deleteUser(Session.getUsername());
    }

    public JsonObject deleteUser(String usernameTarget) {
        JsonObject data = new JsonObject();
        data.addProperty("token", Session.getToken());
        data.addProperty("username", usernameTarget);
        
        res = connection.sendRequest("deleteuser", data);

        int statusCode = (res != null && res.has("statusCode") && !res.get("statusCode").isJsonNull())
                ? res.get("statusCode").getAsInt()
                : 500;

        if (statusCode == 200 && usernameTarget != null && usernameTarget.equals(Session.getUsername())) {
            Session.setToken(null);
            Session.setUsername(null);
            Session.setRole(null);
        }

        return res;
    }

    public JsonObject getRes() {
        return res;
    }
}
