package bookStore;

public class UserEnity {
	private String id;
	private String password;
	private String email;
	private String phone;
	
	public UserEnity() {}
	
	public String getId() {  
        return id;  
    }
	
	public void setId(String id) {  
        this.id = id;  
    }
	
	public String getPassword() {  
        return password;  
    }
	
	public void setPassword(String password) {  
        this.password = password;  
    }
	
	public String getEmail() {  
        return this.email;  
    }
	
	public void setEmail(String Email) {  
        this.email = Email;  
    }
	
	public String getPhone() {  
        return this.phone;  
    }
	
	public void setPhone(String phone) {  
        this.phone = phone;  
    }
}
