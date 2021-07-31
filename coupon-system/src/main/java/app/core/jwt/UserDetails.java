package app.core.jwt;

import app.core.enums.ClientType;

public class UserDetails {
	public int id;
	public String name;
	public String email;
	public ClientType clientType;
	public String token;

	public UserDetails(int id, String name, String email, ClientType clientType) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.clientType = clientType;
	}

}
