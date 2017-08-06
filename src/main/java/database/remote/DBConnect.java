package database.remote;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import cryptography.MyEncryptor;
import database.local.PokedexConnect;
import model.entities.PokemonWithLife;
import model.entities.Trainer;
import model.entities.TrainerImpl;
import scala.Int;
import scala.Tuple2;
import scala.Tuple3;
import scala.Tuple4;
import scala.collection.Iterator;
import scala.collection.JavaConverters;
import scala.collection.immutable.List;
import scala.collection.immutable.HashMap;
import utilities.Settings;
import view.AccountData;

import javax.swing.*;

/**
 * DBConnect is a class composed by static methods to manage the connection with the remote database. This class
 * contains methods to save new data in the database but also to get info about users, their respective favourite or captured Pokemons, info about Pokemons stored
 * in the database (life, level, experience, attack...). The remote database is composed by four tables that contains:
 *  1) User data
 *  2) Trainer data (more related to the game like his six favourite Pokemons)
 *  3) Pokemon captured by all the trainers with their data (attacks, life...)
 *  4) Pokemon ids met by every trainer
 */
public final class DBConnect {
	private static Connection con;
	private static Statement st;
	private static ResultSet rs;
	private static Trainer trainer;

	private DBConnect() {}

	/**
	 * Initializes the connection to the database the first time and return the instance of the connection.
	 * If the connection is already open, return only the instance.
	 */
	private static void initConnection() {
		try {
			if(con == null || con.isClosed()) {
                try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    con = DriverManager.getConnection(Settings.Constants$.MODULE$.REMOTE_DB_URL(),
							Settings.Constants$.MODULE$.REMOTE_DB_USER(), Settings.Constants$.MODULE$.REMOTE_DB_PASSWORD());
                    st = con.createStatement();
                    MyEncryptor.init();
                } catch (Exception ex) {
                    System.out.println(ex.toString());

                }
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close the connection to the database
	 */
	public static void closeConnection(){
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    /**
     * Insert a new user into the database (with name, surname, email, username, password and avatar id)
     * and create his respective trainer (experience points, favourite pokemons).
     * @param accountData a Map with all the user data (Name, Surname, Email, Username, Password)
     * @param id_image avatar id chosen by the user
     * @return true if the username chose is available and the sign up went well, false if the username chosen is
     *         already used
     */
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
		} catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * When an account is create, create a new trainer in the database
	 * @param username username
	 */
	private static void createTrainer(String username) {
		try {
			String query = "select id from users where username = '"+username+"'";
			rs = st.executeQuery(query);
			if(rs.next()) {
				int id = rs.getInt("id");
				String q = "Insert into trainers (id,exp_points,id_pokemon1,id_pokemon2,id_pokemon3,id_pokemon4,id_pokemon5,id_pokemon6) " +
						"value ("+id+",0,0,0,0,0,0,0)";
				st.executeUpdate(q);
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * Check if the username and password used during sign in are correct and, if so, return true; return false if
     * the username doesn't exist or if the password is not correct.
     * @param username username used by the user
     * @param password password used by the user
     * @return true if username and password are correct, false in the opposite case
     */
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
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Check if username is already present in the database
	 * @param username username
	 * @return true if the username is usable
	 */
	private static boolean checkUsername(String username) {
		try {
			String query = "select username from users";
			rs = st.executeQuery(query);
			while (rs.next()) {
				String user = rs.getString("username");
				if(user.equals(username)) {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

    /**
     * Get the next id that will be used by the database in the table passed as an argument.
     * @param tableName name of the table you want to get autoincrement from
     * @return next id of the table passed as an argument (autoincrement of that table)
     */
	public static int getAutoIncrement(String tableName){
		try {
			String query = "SELECT `AUTO_INCREMENT`\n" +
					"FROM  INFORMATION_SCHEMA.TABLES\n" +
					"WHERE TABLE_SCHEMA = 'poke_mp'\n" +
					"AND   TABLE_NAME   = '"+tableName+"';";
			rs = st.executeQuery(query);
			if(rs.next()){
				return rs.getInt("AUTO_INCREMENT");
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return 0;
	}

    /**
     * Delete all the user data related to the username passed as argument
     * @param username username of the user you want to delete
     */
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
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

    /**
     * Check if the Pokemon with the id passed as argument has life or not.
     * @param pokemonId the id of the Pokemon
     * @return true if the Pokemon has life bigger than 0, false in the opposite case
     */
	public static Boolean pokemonHasLife(int pokemonId){
		initConnection();
		String query = "select life from pokemon where id_db =  '" + pokemonId + "'";
		try {
			rs = st.executeQuery(query);
			if(rs.next()){
				if(rs.getInt("life")>0){
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

    /**
     * Get all the Pokemon infos stored in the database related to the Pokemon id passed as argument.
     * @param databaseId Pokemon id of the the database
     * @return an Optional of a Map with all the data of the requested Pokemon
     */
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
				pokemon.put("levelExperience",rs.getString("level_exp"));
				pokemon.put("idImage", rs.getString("id_pokemon")+".png");   //DATABASE LOCALE
				pokemon.put("lifePoints", rs.getString("life"));
				return Optional.of(pokemon);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

    /**
     * Update the Pokemon with the id passed as argument in the database with new level, experience points,
     * level experience, life.
     * @param map An HashMap with Pokemon data
     * @param id_db the id of the Pokemon you want to update
     */
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
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

    /**
     * Insert a new Pokemon in the db when a trainer captures it.
     * @param pokemon the PokemonWithlife instance of the captured Pokemon
     * @param trainerId id mof the trainer that captured the Pokemon
     */
	public static void insertWildPokemonIntoDB(PokemonWithLife pokemon, int trainerId) {
		initConnection();
		try {
			Tuple4 moves = pokemon.pokemon().attacks();
			String query =
					"Insert into pokemon (id_db,id_pokemon,id_trainer,level,points,level_exp,life,moves1,moves2,moves3,moves4)" +
							"value (NULL,'"+pokemon.pokemon().id()+"','"+trainerId+"','"+pokemon.pokemon().level()+"','"+
							pokemon.pokemon().experiencePoints()+"','"+pokemon.pokemon().levelExperience()+"','"+
							pokemon.pokemonLife()+"','"+moves._1()+"','"+moves._2()+"','"+
							moves._3()+"','"+moves._4()+"')";
			st.executeUpdate(query);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

    /**
     * Return the id associated to a username if it exists
     * @param username username of the user you need the database id
     * @return an Optional of the id or an Optional of empty if username doesn't exist
     */
	public static Optional<Integer> getTrainerIdFromUsername(String username){
		initConnection();
		try {
			String query = "select id from users where username =  '" + username + "'";
			rs = st.executeQuery(query);
			if (rs.next()) {
				return Optional.of(rs.getInt("id"));
			} else throw new UsernameNotFoundException();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

    /**
     * Return a new Trainer with the data related with the username passed as argument
     * @param username username of the user related to the trainer
     * @return an Optional of Trainer
     */
	public static Optional<Trainer> getTrainerFromDB(String username) {
		initConnection();
		int id = getTrainerIdFromUsername(username).get();
		try {
			int id_image = 0;
			String query = "select id_image from users where id = '"+id+"'";
			ResultSet rs2 = st.executeQuery(query);
			if(rs2.next()){
				id_image = rs2.getInt("id_image");
			}
			String query2 = "select exp_points from trainers where id = '"+id+"'";
			rs = st.executeQuery(query2);
			if (rs.next()) {
				Trainer trainer = new TrainerImpl(username, id_image, rs.getInt("exp_points"));
				return Optional.of(trainer);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

    /**
     * Update a trainer in the database with new experience points and a new list of favourite Pokemons
     * @param id id of the trainer you want to update
     * @param experiencePoints updated experience points of the trainer
     * @param pokemon List of the the trainer's favourite Pokemons
     */
	public static void updateTrainer(int id, int experiencePoints, List<Int> pokemon){
		initConnection();
		try {
			StringBuilder pokemonQuery = new StringBuilder();
			int n = 0;
			scala.collection.Iterator it = pokemon.iterator();
			while(it.hasNext()){
				pokemonQuery.append(", id_pokemon").append(++n).append(" = '").append(it.next().toString()).append("'");
			}
			String query = "update trainers set exp_points = '"+experiencePoints+"'"+pokemonQuery+" where id = '"+id+"'";
			st.executeUpdate(query);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

    /**
     * Return the ranking position of the trainer id passed as argument
     * @param id id of the trainer
     * @return trainer's ranking position if the id is found, -1 in the opposite case
     */
	public static int getTrainerRank(int id){
		try {
			String query = "SELECT u.name ,u.id ,t.exp_points FROM trainers t, users u WHERE t.id = u.id ORDER BY t.exp_points DESC";
			rs = st.executeQuery(query);
			int rank = 0;
			while (rs.next()) {
				rank++;
				int userId = rs.getInt("id");
				if(userId == id) {
					return rank;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

    /**
     * Return a complete ranking with every trainer in the database and their infos (username, experience points and
     * avatar id.
     * @return An Optional of List of Tuple3 with username, experience points and avatar id
     */
	public static Optional<java.util.List<Tuple3<String, Integer, String>>> getRanking(){
		try {
			String query = "SELECT u.username ,u.id ,t.exp_points, u.id_image FROM trainers t, users u WHERE t.id = u.id ORDER BY t.exp_points DESC";
			rs = st.executeQuery(query);
			java.util.List<Tuple3<String, Integer, String>> list = new ArrayList<>();
			while (rs.next()) {
				Tuple3<String,Integer, String> tuple = new Tuple3<>(rs.getString("username"), rs.getInt("exp_points"), rs.getString("id_image"));
				list.add(tuple);
			}
			return Optional.of(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

    /**
	 * Add a new met Pokemon for the trainer id passed as argument.
	 * @param trainerId id of the trainer that met the Pokemon
	 * @param pokemonId id of the Pokemon met by the trainer
	 */
	public static void addMetPokemon(int trainerId, int pokemonId){
		initConnection();
		String q = "Insert into pokemon_met (id,id_trainer,id_pokemon,captured) " +
				"value (NULL,'"+trainerId+"','"+pokemonId+"',0)";
		try {
			st.executeUpdate(q);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a captured Pokemon.
	 * @param trainerId id of the trainer that met the Pokemon
	 * @param pokemonId id of the Pokemon met by the trainer
	 */
	public static void addCapturedPokemon(int trainerId, int pokemonId){
		initConnection();
		String query = "update pokemon_met SET captured = 1 where id_trainer="+
				trainerId+" and id_pokemon="+pokemonId+"";
		try {
			st.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    /**
     * Update all the favourite Pokemons of a specific trainer.
     * @param trainerId id of the trainer to update
     * @param list new list of the favourite Pokemons
     */
	public static void setAllFavouritePokemon(int trainerId, java.util.List list){
		initConnection();
		String q = "update trainers set id_pokemon1='"+list.get(0)+"',id_pokemon2='"+list.get(1)+"'," +
				"id_pokemon3='"+list.get(2)+"', id_pokemon4='"+list.get(3)+"',id_pokemon5='"+list.get(4)+"'," +
				"id_pokemon6='"+list.get(5)+"' where id = '"+trainerId+"'";
		try {
			st.executeUpdate(q);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    /**
     * Add one favourite pokemon in a specific position of the list
     * @param trainerId id of the trainer
     * @param pokemonId id of the new favourite pokemon
     * @param pos position (from 1 to 6) where the new Pokemon id must be added
     */
	public static void addFavouritePokemon(int trainerId, int pokemonId, int pos){
		initConnection();
		String q = "update trainers set id_pokemon"+pos+" = '"+pokemonId+"' where id = '"+trainerId+"'";
		try {
			st.executeUpdate(q);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    /**
     * Return the list of the favoutite Pokemons of a specific trainer
     * @param trainerId id of the trainer
     * @return Optional of a list with the six favourite Pokemons of the trainer id passed as argument
     */
	public static Optional<List<Int>> getFavouritePokemonList(int trainerId){
		initConnection();
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

    /**
     * Return the list of the captured Pokemons (containing the unique id on the database and the local id of the
     * pokemon) related to a specific trainer
     * @param trainerId id of the trainer
     * @return Optional of a list with all the captured Pokemons of the trainer id passed as argument
     */
	public static Optional<List<scala.Tuple2<Int,Int>>> getCapturedPokemonList(int trainerId){
		initConnection();
		String query = "select id_db, id_pokemon from pokemon where id_trainer =  '" + trainerId + "'";
		java.util.List<scala.Tuple2<Integer,Integer>> pokemonList = new ArrayList<>();
		try {
			rs = st.executeQuery(query);
			while(rs.next()){
				scala.Tuple2<Integer,Integer> tupla = new Tuple2<Integer,Integer>(rs.getInt("id_db"),rs.getInt("id_pokemon"));
				pokemonList.add(tupla);
			}
			List list = JavaConverters.asScalaBuffer(pokemonList).toList();
			return Optional.of(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

    /**
     * Return the list of the captured Pokemons (containing only the local id of the Pokemon) related to a
     * specific trainer
     * @param trainerId id of the trainer
     * @return Optional of a list with the local ids of the Pokemons captured by the trainer id passed as argument
     */
	public static Optional<List<Int>> getCapturedPokemonIdList(int trainerId){
		initConnection();
		String query = "select id_pokemon from pokemon_met where id_trainer =  '" + trainerId + "' and captured = '1'";
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

    /**
     * Return a list with all the local ids of the pokemons met by a specific trainer
     * @param trainerId id of the trainer
     * @return list with all the local ids of the pokemons met by a specific trainer
     */
	public static Optional<List<Int>> getMetPokemonList(int trainerId){
		initConnection();
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

	private static Optional<String> getPokemonMaxLife(int databaseId) {
		initConnection();
		try {
			String query = "select points from pokemon where id_db = '"+databaseId+"'";
			rs = st.executeQuery(query);
			if (rs.next()) {
				return Optional.of(rs.getString("points"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

    /**
     * Set the maximum life for the six favourite Pokemons of a specific trainer
     * @param trainerId id of the trainer
     */
	public static void rechangeAllTrainerPokemon(int trainerId) {
		initConnection();
		List trainerPokemon = getFavouritePokemonList(trainerId).get();
		Iterator iterator = trainerPokemon.iterator();
		while(iterator.hasNext()) {
			int id = (int)iterator.next();
			if(id != 0) {
				try {
					String query = "update pokemon set life = "+getPokemonMaxLife(id).get()+" where id_db = '"+id+"'";
					st.executeUpdate(query);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * Set the trainer
	 * @param t ,trainer
	 */
	public static void setMyTrainer(Trainer t) {
		trainer = t;
	}

	/**
	 * @return trainer
	 */
	public static Trainer getMyTrainer() {
		return trainer;
	}

}