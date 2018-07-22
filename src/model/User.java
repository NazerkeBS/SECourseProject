package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String code;

	private byte[] password;
	private byte[] salt;

	private String name;
	private String email;

	public User() {

	}

	public User(String code, byte[] password, byte[] salt, String name, String email) {
		this.code = code;
		this.password = password;
		this.salt = salt;
		this.name = name;
		this.email = email;

	}

	public String getCode() {
		return code;
	}

	public byte[] getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public int getId() {
		return this.id;
	}
	public byte[] getSalt() {
		return this.salt;
	}
}
