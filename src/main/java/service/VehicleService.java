package service;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import entities.Session;
import entities.Vaga;
import entities.Vehicle;
import network.ConnectionClient;

public class VehicleService {

    private ConnectionClient connection;
    private JsonObject res;

    public VehicleService() {
        this.connection = new ConnectionClient();
        this.res = new JsonObject();
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

    public JsonObject createVehicle(String brand, String model, String type, String licensePlate) {
        JsonObject data = new JsonObject();

        data.addProperty("token", Session.getToken());
        data.addProperty("brand", brand);
        data.addProperty("model", model);
        data.addProperty("type", type);
        data.addProperty("licensePlate", licensePlate);

        res = connection.sendRequest("createvehicle", data);
        return res;
    }

    public List<Vehicle> getVehicles() {
        JsonObject data = new JsonObject();
        List<Vehicle> vehicles = new ArrayList<>();

        data.addProperty("token", Session.getToken());
        res = connection.sendRequest("getvehicles", data);

        if (res != null && res.has("data") && !res.get("data").isJsonNull()) {
            JsonArray resArray = res.get("data").getAsJsonArray();

            for (JsonElement element : resArray) {
                JsonObject item = element.getAsJsonObject();
                Vehicle vehicle = new Vehicle();

                if (item.has("brand") && !item.get("brand").isJsonNull()) {
                    vehicle.setBrand(item.get("brand").getAsString());
                }
                if (item.has("model") && !item.get("model").isJsonNull()) {
                    vehicle.setModel(item.get("model").getAsString());
                }
                if (item.has("type") && !item.get("type").isJsonNull()) {
                    vehicle.setType(item.get("type").getAsString());
                }
                if (item.has("licensePlate") && !item.get("licensePlate").isJsonNull()) {
                    vehicle.setLicensePlate(item.get("licensePlate").getAsString());
                }

                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }

    public Vehicle getVehicle(String licensePlate) {
        JsonObject data = new JsonObject();
        Vehicle vehicle = new Vehicle();

        data.addProperty("token", Session.getToken());
        data.addProperty("licensePlate", licensePlate);

        res = connection.sendRequest("getvehicle", data);

        if (res != null && res.has("data") && !res.get("data").isJsonNull()) {
            JsonObject resData = res.get("data").getAsJsonObject();

            if (resData.has("brand") && !resData.get("brand").isJsonNull()) {
                vehicle.setBrand(resData.get("brand").getAsString());
            }
            if (resData.has("model") && !resData.get("model").isJsonNull()) {
                vehicle.setModel(resData.get("model").getAsString());
            }
            if (resData.has("type") && !resData.get("type").isJsonNull()) {
                vehicle.setType(resData.get("type").getAsString());
            }
            if (resData.has("licensePlate") && !resData.get("licensePlate").isJsonNull()) {
                vehicle.setLicensePlate(resData.get("licensePlate").getAsString());
            }
        }

        return vehicle;
    }

    public JsonObject updateVehicle(String licensePlate, String brand, String model, String type) {
        JsonObject data = new JsonObject();

        data.addProperty("token", Session.getToken());
        data.addProperty("licensePlate", licensePlate);

        if (brand != null) {
            data.addProperty("brand", brand);
        }
        if (model != null) {
            data.addProperty("model", model);
        }
        if (type != null) {
            data.addProperty("type", type);
        }

        res = connection.sendRequest("updatevehicle", data);
        return res;
    }

    public JsonObject deleteVehicle(String licensePlate) {
        JsonObject data = new JsonObject();

        data.addProperty("token", Session.getToken());
        data.addProperty("licensePlate", licensePlate);

        res = connection.sendRequest("deletevehicle", data);
        return res;
    }

    public JsonObject getRes() {
        return res;
    }
}
