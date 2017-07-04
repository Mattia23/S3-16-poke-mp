package database.remote;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import cryptography.MyEncryptor;
import database.local.PokedexConnect;
import model.entities.Pokemon;
import model.entities.PokemonWithLife;
import model.entities.Trainer;
import model.entities.TrainerImpl;
import scala.Int;
import scala.Tuple2;
import scala.Tuple4;
import scala.collection.JavaConverters;
import scala.collection.immutable.List;
import scala.collection.immutable.HashMap;
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

	public static int getAutoIncrement(String tableName){
		try {
			String query = "SELECT `AUTO_INCREMENT`\n" +
					"FROM  INFORMATION_SCHEMA.TABLES\n" +
					"WHERE TABLE_SCHEMA = 'pokemon_mp'\n" +
					"AND   TABLE_NAME   = '"+tableName+"';";
			rs = st.executeQuery(query);
			if(rs.next()){
				return rs.getInt("AUTO_INCREMENT");
			}
		} catch(Exception ex){
			System.out.println(ex);
		}
		return 0;
	}

	public static void deleteUserAndRelatedData(String username){
		initConnection();
		int id = getTrainerIdFromUsername(username).get();
		try{
			String query = "delete from pokemon_met where id_trainer = '"+id+"'";
			st.executeUpdate(query);
			query = "delete from pokemon where id_trainer = '"+id+"'";
			st.executeUpdate(query);
			query = "delete from trainers where id = '"+id+"';";
			st.executeUpdate(query);
			query = "delete from users where id = '"+id+"';";
			st.executeUpdate(query);
		} catch(SQLException ex){
			System.out.println(ex);
		}
	}

	public static Optional<Map> getPokemonFromDB(int databaseId) {
		initConnection();
		try {
			String query = "select * from pokemon where id_db = '"+databaseId+"'";
			rs = st.executeQuery(query);
			if (rs.next()) {
				Tuple4<Integer,Integer,Integer,Integer> attacks =
						new Tuple4<>(Integer.parseInt(rs.getString("moves1")),
								Integer.parseInt(rs.getString("moves2")),
								Integer.parseInt(rs.getString("moves3")),
								Integer.parseInt(rs.getString("moves4")));
				Map<String,Object> pokemon = new java.util.HashMap<>();
				pokemon.put("databaseId",databaseId) ;
				pokemon.put("id", rs.getString("id_pokemon"));
				pokemon.put("name", PokedexConnect.getPokemonName(rs.getInt("id_pokemon")).get());
				pokemon.put("attacks", attacks);
				pokemon.put("level", rs.getString("level"));
				pokemon.put("experiencePoints",rs.getString("points"));
				pokemon.put("idImage", rs.getString("id_pokemon")+".png");   //DATABASE LOCALE
				pokemon.put("lifePoints", rs.getString("life"));
				return Optional.of(pokemon);
			}
		} catch(Exception ex) {
			System.out.println(ex);
		}
		return Optional.empty();
	}

	public static void updatePokemon(HashMap<String, String> map, int id_db) {
		initConnection();
		try {
			String id = map.get("id").get();
			String level = map.get("level").get();
			String points = map.get("points").get();
			String level_exp = map.get("level_exp").get();
			String life = map.get("life").get();

			String query = "UPDATE `pokemon` SET `id_pokemon`="+id+",`level`="+level+",`points`="+points+",`level_exp`="+
					level_exp+",`life`="+life+" WHERE `id_db`="+id_db;
			st.executeUpdate(query);
		} catch(Exception ex) {
			System.out.println(ex);
		}
	}

	public static void insertWildPokemonIntoDB(PokemonWithLife pokemon, int trainerId) {
		initConnection();
		try {
			Tuple4 moves = pokemon.pokemon().attacks();
			String query =
					"Insert into pokemon (id_db,id_pokemon,id_trainer,level,points,level_exp,life,moves1,moves2,moves3,moves4)" +
							"value (NULL,'"+pokemon.pokemon().id()+"','"+trainerId+"','"+pokemon.pokemon().level()+"','"+
							pokemon.pokemon().experiencePoints()+"','"+pokemon.pokemonLife()+"','"+
							pokemon.pokemon().levelExperience()+"','"+moves._1()+"','"+moves._2()+"','"+
							moves._3()+"','"+moves._4()+"')";
			st.executeUpdate(query);
		} catch(Exception ex) {
			System.out.println(ex);
		}
	}

	public static Optional<Integer> getTrainerIdFromUsername(String username){
		initConnection();
		try {
			String query = "select id from users where username =  '" + username + "'";
			rs = st.executeQuery(query);
			if (rs.next()) {
				return Optional.of(rs.getInt("id"));
			} else throw new UsernameNotFoundException();
		}
		catch(Exception ex) {
			System.out.println(ex);
		}
		return Optional.empty();
	}

	public static Optional<Trainer> getTrainerFromDB(String username) {
		initConnection();
		int id = getTrainerIdFromUsername(username).get();
		try {
			int id_image = 0;
			String query = "select id_image from users where id = '"+id+"'";
			ResultSet rs2 = st.executeQuery(query);
			if(rs2.next()) id_image = rs2.getInt("id_image");
			String query2 = "select * from trainers where id = '"+id+"'";
			rs = st.executeQuery(query2);
			if (rs.next()) {
				Trainer trainer = new TrainerImpl(username, id_image, rs.getInt("exp_points"));
				return Optional.of(trainer);
			}
		} catch(Exception ex) {
			System.out.println(ex);
		}
		return Optional.empty();
	}

	public static void updateTrainer(String username, int experiencePoints, List<Int> pokemon){
		initConnection();
		int id = getTrainerIdFromUsername(username).get();
		try {
			String pokemonQuery = "";
			int n = 0;
			scala.collection.Iterator it = pokemon.iterator();
			while(it.hasNext()){
				pokemonQuery += ", id_pokemon"+ ++n +" = '"+it.next().toString()+"'";
			}
			String query = "update trainers set exp_points = '"+experiencePoints+"'"+pokemonQuery+" where id = '"+id+"'";
			st.executeUpdate(query);
		} catch(Exception ex) {
			System.out.println(ex);
		}
	}

	public static void addCapturedPokemon(String username, Pokemon pokemon){
		initConnection();
		int trainerId = getTrainerIdFromUsername(username).get();
		String q = "Insert into pokemon (id_db,id_pokemon,id_trainer,level,points,life,moves1,moves2,moves3,moves4) " +
				"value (NULL,'"+pokemon.id()+"','"+trainerId+"','"+pokemon.level()+"','"+pokemon.experiencePoints()+"'," +
				"100,'"+pokemon.attacks()._1().toString()+"','"+pokemon.attacks()._2().toString()+"','"+pokemon.attacks()._3().toString()+"'," +
				"'"+pokemon.attacks()._4().toString()+"')";
		try {
			st.executeUpdate(q);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addMetPokemon(String username, int pokemon){
		initConnection();
		int trainerId = getTrainerIdFromUsername(username).get();
		String q = "Insert into pokemon_met (id,id_trainer,id_pokemon) " +
				"value (NULL,'"+trainerId+"','"+pokemon+"')";
		try {
			st.executeUpdate(q);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addFavouritePokemon(String username, int pokemonId, int pos){
		initConnection();
		int trainerId = getTrainerIdFromUsername(username).get();
		String q = "update trainers set id_pokemon"+pos+" = '"+pokemonId+"' where id = '"+trainerId+"'";
		try {
			st.executeUpdate(q);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Optional<List<Int>> getFavouritePokemonList(String name){
		initConnection();
		int trainerId = getTrainerIdFromUsername(name).get();

		String query = "select * from trainers where id =  '" + trainerId + "'";
		try {
			rs = st.executeQuery(query);
			if(rs.next()){
				java.util.List<Integer> pokemonList = new ArrayList<>();
				for(int i=1;i<7;i++){
					pokemonList.add(rs.getInt("id_pokemon"+i));
				}
				List list = JavaConverters.asScalaBuffer(pokemonList).toList();
				return Optional.of(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public static Optional<List<scala.Tuple2<Int,Int>>> getCapturedPokemonList(String name){
		initConnection();
		int trainerId = getTrainerIdFromUsername(name).get();
		String query = "select * from pokemon where id_trainer =  '" + trainerId + "'";
		java.util.List<scala.Tuple2<Integer,Integer>> pokemonList = new ArrayList<>();
		try {
			rs = st.executeQuery(query);
			while(rs.next()){
				scala.Tuple2<Integer,Integer> tupla = new Tuple2<>(rs.getInt("id_db"),rs.getInt("id_pokemon"));
				pokemonList.add(tupla);
			}
			List list = JavaConverters.asScalaBuffer(pokemonList).toList();
			return Optional.of(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public static Optional<List<Int>> getMetPokemonList(String name){
		initConnection();
		int trainerId = getTrainerIdFromUsername(name).get();
		String query = "select id_pokemon from pokemon_met where id_trainer =  '" + trainerId + "'";
		java.util.List<Integer> pokemonList = new ArrayList<>();
		try {
			rs = st.executeQuery(query);
			while(rs.next()){
				pokemonList.add(rs.getInt("id_pokemon"));
			}
			List list = JavaConverters.asScalaBuffer(pokemonList).toList();
			return Optional.of(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public static Boolean pokemonIsLive(Int pokemonId){
		initConnection();
		String query = "select life from pokemon where id_db =  '" + pokemonId + "'";
		try {
			rs = st.executeQuery(query);
			if(rs.next()){
				if(rs.getInt("life")>0) return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}