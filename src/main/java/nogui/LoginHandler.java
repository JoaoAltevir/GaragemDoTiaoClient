package nogui;

import java.util.Scanner;

import com.google.gson.JsonObject;

import entities.*;

import entities.Session;
import service.UserService;

public class LoginHandler {

    private Scanner input = new Scanner(System.in);
    private User user = new User();
    private UserService network;

    public LoginHandler(UserService service){
        this.network = service;
    }
    public void logged(){

        int op = 9;

        while(op != 0){
            
            System.out.println("Escolha entre as opções...\n1 - Buscar informações\n2 - Atualizar Nome\n3 - Atualizar senha\n4 - Logout");
			op = input.nextInt();

            switch(op) {
                case 1:
                    // Buscar informações
                    
                    System.out.println("--- Informações do Usuário ---");
                    System.out.println("Nome: " + user.getName());
                    System.out.println("Usuário: " + user.getUsername());
                    break;

                case 2:
                    // Atualizar Nome
                    System.out.println("Informe o novo nome: ");
                    input.nextLine(); // Limpa o buffer do teclado após o nextInt()
                    user.setName(input.nextLine());
                    System.out.println("Nome atualizado com sucesso!");
                    break;

                case 3:
                    // Atualizar senha
                    System.out.println("Informe a nova senha: ");
                    input.nextLine(); // Limpa o buffer do teclado após o nextInt()
                    user.setPassword(input.nextLine());
                    System.out.println("Senha atualizada com sucesso!");
                    break;

                case 4:
                    // Logout
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
