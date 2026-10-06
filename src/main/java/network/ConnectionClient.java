package network;

import java.io.*;
import java.net.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ConnectionClient {
    private String host;
    private int port;
    private Gson gson;

    public ConnectionClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.gson = new Gson();
    }

    public JsonObject sendRequest(String method, JsonObject data) {
        JsonObject request = new JsonObject();
        request.addProperty("method", method);
        request.add("data", data);

        // O try-with-resources garante que o socket e os fluxos sejam fechados no cliente
        try (Socket socket = new Socket(host, port);
             PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Envia a requisição no formato esperado pelo servidor
            saida.println(request.toString());

            // Lê a resposta do servidor
            String responseJson = entrada.readLine();
            
            if (responseJson != null && !responseJson.isEmpty()) {
                return gson.fromJson(responseJson, JsonObject.class);
            }

        } catch (IOException e) {
            System.err.println("Erro na comunicação com o servidor: " + e.getMessage());
            JsonObject error = new JsonObject();
            error.addProperty("statusCode", 500);
            error.addProperty("message", "Falha ao conectar: " + e.getMessage());
            return error;
        }
        
        return null;
    }
}