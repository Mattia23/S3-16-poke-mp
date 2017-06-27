package database;
import java.sql.*;
import java.util.Map;
import java.util.Optional;

import cryptography.MyEncryptor;
import org.json.simple.JSONObject;
import scala.Tuple4;
import view.AccountData;

import javax.swing.*;

public final class DBConnect {
    private static Connection con;
    private static Statement st;
    private static ResultSet rs;
    
    private DBConnect() {}

    private static void initConnection() {
    	if(con == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pokemon_mp", "root", "");
				st = con.createStatement();
				MyEncryptor.init();
			} catch (Exception ex) {
				System.out.println("Error: " + ex);

			}
		}
	}

    public static boolean insertCredentials(Map<String,JTextField> accountData, int id_image) {
    	initConnection();
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

    private static void createTrainer(String username) {
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
    
    public static boolean checkCredentials(String username, String password) {
		initConnection();
    	try {
    		 String query = "select password from users where username = '"+username+"'";
	   	     rs = st.executeQuery(query);
	   	     if (rs.next()) {
	   			  String pass = rs.getString("password");
	   			  pass = MyEncryptor.decrypt(pass);
	   			  if(pass.equals(password)) {
	   				  return true;
	   			  }
	   	     }
    	} catch(Exception ex) {
    		System.out.println(ex);
    	}
    	return false;
    }
    
    private static boolean checkUsername(String username) {
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

    public static Optional<JSONObject> getPokemonFromDB(int databaseId) {
		initConnection();
		try {
			String query = "select * from pokemon where id_db = '"+databaseId+"'";
			rs = st.executeQuery(query);
			if (rs.next()) {
				JSONObject pokemon = new JSONObject();
				Tuple4<Integer,Integer,Integer,Integer> attacks = 
						new Tuple4<>(Integer.parseInt(rs.getString("moves1")),
								Integer.parseInt(rs.getString("moves2")),
								Integer.parseInt(rs.getString("moves3")),
								Integer.parseInt(rs.getString("moves4")));
				pokemon.put("databaseId",databaseId);
				pokemon.put("id", rs.getString("id_pokemon"));
				pokemon.put("name", "name");  //DATABASE LOCALE
				pokemon.put("pokemonType", "tipo");   //DATABASE LOCALE
				pokemon.put("attacks", attacks);
				pokemon.put("level", rs.getString("level"));
				pokemon.put("experiencePoints",rs.getString("points"));
				pokemon.put("idImage", 1);   //DATABASE LOCALE
				pokemon.put("lifePoints", rs.getString("life"));
				return Optional.of(pokemon);
			}
		} catch(Exception ex) {
			System.out.println(ex);
		}
		return Optional.empty();
	}

}