package entities;

public class Session {
	
	private static String token;

	public static String getToken() {
		return token;
	}

	public static void setToken(String token) {
		Session.token = token;
	}
	
}
