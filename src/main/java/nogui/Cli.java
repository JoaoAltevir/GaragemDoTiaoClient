package nogui;

import service.UserService;
import entities.*;

import java.util.Scanner;

import com.google.gson.JsonObject;

public class Cli {
	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		String ip;
		int port;
		String name;
		String password;
		String un;
		
		User user = new User();
		
		int option = 10;
		
		UserService network;
		JsonObject res = new JsonObject();
		
		System.out.println("Informe o ip que deseja se conectar: ");
		ip = input.nextLine();
		System.out.println("Informe a porta: ");
		port = input.nextInt();
		
		network = new UserService(ip, port);
		LoginHandler login = new LoginHandler(network);
		
		System.out.println("Conectado!");
		
		while(option != 0) {
			
			
			System.out.println("Escolha entre as opções...\n1 - Registro\n2 - Login\n3 - Sair");
			option = input.nextInt();
			
			switch(option) {
				case 1:
					System.out.println("Informe o nome completo: ");
					input.nextLine();
					name = input.nextLine();
					user.setName(name);
					System.out.println("Informe o nome de usuário: ");
					un = input.nextLine();
					user.setUsername(un);
					System.out.println("Informe a senha: ");
					input.nextLine();
					password = input.nextLine();
					user.setPassword(password);
					res = network.register(user.getName(), user.getUsername(), user.getPassword());
					System.out.println(res);
					break;
				case 2:
					System.out.println("Informe o nome de usuário: ");
					input.nextLine();
					un = input.nextLine();
					user.setUsername(un);
					System.out.println("Informe a senha: ");
					input.nextLine();
					password = input.nextLine();
					user.setPassword(password);
					res = network.login(user.getUsername(), user.getPassword());
					System.out.println(res);
					Session.setToken(res.get("token").getAsString());
					if(Session.getToken() != null){
						login.logged();
					}
					break;
				case 3:
					option = 0;
					break;
				default: 
					System.out.println("Errooou");
					break;
			}
		}

		input.close();
	}
}
