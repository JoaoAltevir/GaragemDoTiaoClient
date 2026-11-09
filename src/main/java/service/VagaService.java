package service;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import entities.Session;
import entities.Vaga;
import network.ConnectionClient;

public class VagaService {

    private ConnectionClient connection;
    private JsonObject res;

    public VagaService() {
        this.connection = new ConnectionClient();
        this.res = new JsonObject();
    }

    public JsonObject createVaga(String spotName, int floor, String type) {
        JsonObject data = new JsonObject();

        data.addProperty("token", Session.getToken());
        data.addProperty("floor", floor);
        data.addProperty("spotName", spotName);
        data.addProperty("type", type);

        res = connection.sendRequest("createvaga", data);
        return res;
    }

    public List<Vaga> getVagas() {
        JsonObject data = new JsonObject();
        List<Vaga> vagas = new ArrayList<>();

        data.addProperty("token", Session.getToken());
        res = connection.sendRequest("getvagas", data);

        if (res != null && res.has("data") && !res.get("data").isJsonNull()) {
            JsonArray resArray = res.get("data").getAsJsonArray();

            for (JsonElement element : resArray) {
                JsonObject item = element.getAsJsonObject();
                Vaga vaga = new Vaga();

                if (item.has("floor") && !item.get("floor").isJsonNull()) {
                    vaga.setFloor(item.get("floor").getAsInt());
                }
                if (item.has("spotName") && !item.get("spotName").isJsonNull()) {
                    vaga.setSpotName(item.get("spotName").getAsString());
                }
                if (item.has("status") && !item.get("status").isJsonNull()) {
                    vaga.setStatus(item.get("status").getAsString());
                }
                if (item.has("licensePlate") && !item.get("licensePlate").isJsonNull()) {
                    vaga.setLicensePlate(item.get("licensePlate").getAsString());
                }
                if (item.has("type") && !item.get("type").isJsonNull()) {
                    vaga.setType(item.get("type").getAsString());
                }

                vagas.add(vaga);
            }
        }

        return vagas;
    }

    public JsonObject updateVaga(String spotName, String newSpotName, Integer floor, String status, String licensePlate) {
        JsonObject data = new JsonObject();

        data.addProperty("token", Session.getToken());
        data.addProperty("spotName", spotName);

        if (newSpotName != null) {
            data.addProperty("newSpotName", newSpotName);
        }
        if (floor != null) {
            data.addProperty("floor", floor);
        }
        if (status != null) {
            data.addProperty("status", status);
        }
        if (licensePlate != null) {
            data.addProperty("licensePlate", licensePlate);
        }

        res = connection.sendRequest("updatevaga", data);
        return res;
    }

    public JsonObject deleteVaga(String spotName) {
        JsonObject data = new JsonObject();

        data.addProperty("token", Session.getToken());
        data.addProperty("spotName", spotName);

        res = connection.sendRequest("deletevaga", data);
        return res;
    }

    public Vaga findVehicle(String licensePlate) {
        JsonObject data = new JsonObject();

        data.addProperty("token", Session.getToken());
        data.addProperty("licensePlate", licensePlate);

        res = connection.sendRequest("findvehicle", data);

        if (res != null && res.has("data") && !res.get("data").isJsonNull()) {
            JsonObject resData = res.get("data").getAsJsonObject();
            Vaga vaga = new Vaga();

            if (resData.has("floor") && !resData.get("floor").isJsonNull()) {
                vaga.setFloor(resData.get("floor").getAsInt());
            }
            if (resData.has("spotName") && !resData.get("spotName").isJsonNull()) {
                vaga.setSpotName(resData.get("spotName").getAsString());
            }
            if (resData.has("status") && !resData.get("status").isJsonNull()) {
                vaga.setStatus(resData.get("status").getAsString());
            }
            if (resData.has("type") && !resData.get("type").isJsonNull()) {
                vaga.setType(resData.get("type").getAsString());
            }
            if (resData.has("licensePlate") && !resData.get("licensePlate").isJsonNull()) {
                vaga.setLicensePlate(resData.get("licensePlate").getAsString());
            }

            return vaga;
        }

        return null;
    }

    public JsonObject getRes() {
        return res;
    }
}
