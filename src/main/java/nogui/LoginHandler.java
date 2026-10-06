package nogui;

import java.util.Scanner;

import com.google.gson.JsonObject;

import entities.*;
import service.UserService;

public class LoginHandler {

    private Scanner input = new Scanner(System.in);
    private User user = new User();
    private UserService network;
    private JsonObject res = new JsonObject();

    public LoginHandler(UserService service){
        this.network = service;
    }
    public void logged(){

        String un;
        String name;

        int op = 9;

        while(op != 0){
            
            System.out.println("Escolha entre as opções...\n1 - Buscar informações\n2 - Atualizar Nome\n3 - Atualizar senha\n4 - Logout");
			op = input.nextInt();

            switch(op) {
                case 1:

                    System.out.println("Informe o nome de usuário: ");
                    un = input.nextLine();
                    JsonObject res = network.getUser(un);
                    System.out.println(res);
                    JsonObject resData = res.get("data").getAsJsonObject();
                    user.setName(resData.get("name").getAsString());
                    user.setUsername(resData.get("username").getAsString());
                    // Buscar informações
                    System.out.println("--- Informações do Usuário ---");
                    System.out.println("Nome: " + user.getName());
                    System.out.println("Usuário: " + user.getUsername());
                    break;

                case 2:
                    // Atualizar Nome
                    System.out.println("Informe o nome de usuário: ");
                    un = input.nextLine();
                    System.out.println("Informe o novo nome: ");
                    name = input.nextLine();
                    res = network.updateUserName(un, name);
                    System.out.println(res);
                    System.out.println("Nome atualizado com sucesso!");
                    
                    break;

                case 3:
                    // Atualizar senha  
                    System.out.println("Informe o nome de usuário: ");
                    un = input.nextLine();
                    System.out.println("Informe a antiga senha: ");
                    String oldPass =input.nextLine();
                    System.out.println("Informe a nova senha: ");
                    String pass = input.nextLine(); // Limpa o buffer do teclado após o nextInt()
                    
                    res = network.updateUserPassword(un, oldPass, pass);
                    System.out.println(res);

                    System.out.println("Senha atualizada com sucesso!");

                    break;

                case 4:
                    //deletuser

                    System.out.println("Informe o nome de usuário: ");
                    un = input.nextLine();

                    res = network.deleteUser(un);
                    System.out.println(res);

                    System.out.println("Deletado com sucesso!");

                    break;
                case 5:
                    // Logout

                    System.out.println("Informe o nome de usuário: ");
                    un = input.nextLine();
                    
                    res = network.logout();
                    System.out.println(res);

                    Session.setToken(null);

                    System.out.println("Saindo...");
                    op = 0; // Define op como 0 para quebrar o laço while(op != 0)
                    break;
                
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }
}
