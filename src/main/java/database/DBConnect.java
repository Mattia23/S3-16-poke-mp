package database;
import java.sql.*;
import java.util.Map;

import cryptography.MyEncryptor;
import view.AccountData;

import javax.swing.*;

public class DBConnect {
    private Connection con;
    private Statement st;
    private ResultSet rs;
    
    public DBConnect() {
		 try {
			 Class.forName("com.mysql.jdbc.Driver").newInstance();
		     con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pokemon_mp", "root", "");
		     st = con.createStatement();
		     MyEncryptor.init();
		 } catch (Exception ex) {
		     System.out.println("Error: " + ex);
		     
		 }
    }    
    
    public Statement getDatabaseInstance() {
    	return st;
    }
    
    public boolean insertCredentials(Map<String,JTextField> accountData, int id_image) {
    	try {
    		if(!checkUsername(accountData.get(AccountData.Username.toString()).getText())){
    			return false;
    		}
    		String name = accountData.get(AccountData.Name.toString()).getText();
    		String surname = accountData.get(AccountData.Surname.toString()).getText();
    		String email = accountData.get(AccountData.Email.toString()).getText();
    		String username = accountData.get(AccountData.Username.toString()).getText();
    		String password = MyEncryptor.encrypt(accountData.get(AccountData.Password.toString()).getText());
    		String query = "Insert into users (id,name,surname,email,username,password,id_image) " +
					"value (NULL,'"+name+"','"+surname+"','"+email+"','"+username+"','"+password+"','"+id_image+"')";
    		st.executeUpdate(query);
    		createTrainer(username);
    	} catch(Exception ex) {
   	     	System.out.println(ex);
   	 	}
    	return true;
    }

    private void createTrainer(String username) {
		try {
			String query = "select id from users where username = '"+username+"'";
			rs = st.executeQuery(query);
			if(rs.next()) {
				int id = rs.getInt("id");
				String q = "Insert into trainers (id,exp_points,id_pokemon1,id_pokemon2,id_pokemon3,id_pokemon4,id_pokemon5,id_pokemon6) " +
						"value ("+id+",0,0,0,0,0,0,0)";
				try {
					st.executeUpdate(q);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} catch(Exception ex) {
			System.out.println(ex);
		}


	}
    
    public boolean checkCredentials(String username, String password) {
    	try {
    		 String query = "select * from users";
	   	     rs = st.executeQuery(query);
	   	     while (rs.next()) {
	   			  String user = rs.getString("username");
	   			  String pass = rs.getString("password");
	   			  pass = MyEncryptor.decrypt(pass);
	   			  if(user.equals(username) && pass.equals(password)) {
	   				  return true;
	   			  }
	   	     }
    	} catch(Exception ex) {
    		System.out.println(ex);
    	}
    	
    	return false;
    }
    
    private boolean checkUsername(String username) {
    	try {
    		String query = "select * from users";
	   	     rs = st.executeQuery(query);
	   	     while (rs.next()) {
	   			  String user = rs.getString("username");
	   			  if(user.equals(username)) {
	   				  return false;
	   			  }
	   	     }
    	} catch (Exception ex) {
    		System.out.println(ex);
    	}
    	
    	return true;
    }
    
    
}