package entities;

public class Session {
	
	private static String ip;
	private static int port;
	
	private static String token;

	public static String getToken() {
		return token;
	}

	public static void setToken(String token) {
		Session.token = token;
	}

	public static String getIp() {
		return ip;
	}

	public static void setIp(String ip) {
		Session.ip = ip;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		Session.port = port;
	}
	
	
	
}
