package database.local;

import scala.Tuple2;

import java.io.File;
import java.sql.*;
import java.util.Optional;


public final class PokedexConnect {

    private static Connection con;

    private PokedexConnect() {}

    public static void init() {
        if(con == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                String url = "jdbc:sqlite:src/main/resources/database/pokedex.db";
                con = DriverManager.getConnection(url);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (ClassNotFoundException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static Optional<String> getPokemonName(int id){
        init();
        String sql = "SELECT identifier FROM pokemon WHERE id = ?";
        try (PreparedStatement pstmt  = con.prepareStatement(sql)){
            pstmt.setInt(1,id);
            ResultSet rs  = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(rs.getString("identifier"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public static Optional<Tuple2<String,Integer>> getPokemonAttack(int id){
        init();
        String sql = "SELECT identifier, power FROM moves WHERE id = ?";
        try (PreparedStatement pstmt  = con.prepareStatement(sql)){
            pstmt.setInt(1,id);
            ResultSet rs  = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Tuple2<>(rs.getString("identifier"),rs.getInt("power")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }
}
