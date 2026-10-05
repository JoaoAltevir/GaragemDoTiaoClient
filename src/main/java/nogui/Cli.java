package nogui;

import service.UserService;
import entities.*;

import java.util.Scanner;

public class Cli {
	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		String ip;
		int port;
		
		User user = new User();
		
		int option = 10;
		
		UserService network;
		
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
					user.setName(input.nextLine());
					System.out.println("Informe o nome de usuário: ");
					user.setUsername(input.nextLine());
					System.out.println("Informe a senha: ");
					user.setPassword(input.nextLine());
					network.register(user.getName(), user.getUsername(), user.getPassword());
					break;
				case 2:
					System.out.println("Informe o nome de usuário: ");
					user.setUsername(input.nextLine());
					System.out.println("Informe a senha: ");
					user.setPassword(input.nextLine());
					String res = network.login(user.getUsername(), user.getPassword());
					Session.setToken(res);
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
