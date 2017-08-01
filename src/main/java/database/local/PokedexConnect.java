package database.local;

import scala.Int;
import scala.Tuple2;
import scala.collection.IndexedSeq;
import scala.collection.mutable.Seq;

import java.sql.*;
import java.util.*;


public final class PokedexConnect {
    private static final int LAST_POKEMON_ID = 151;
    private static Connection con;

    private PokedexConnect() {}
    private enum Type {
        String,Tuple2,Integer;
    }

    public static void init() {
        if(con == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                String url = "jdbc:sqlite:src/main/resources/database/pokedex.db";
                con = DriverManager.getConnection(url);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    private static Optional<?> executeTheQuery(String sql, Type t,List<String> columns) {
        init();
        try (PreparedStatement pstmt  = con.prepareStatement(sql)){
            ResultSet rs  = pstmt.executeQuery();
            if (rs.next()) {
                switch (t) {
                    case String:
                        return Optional.of(rs.getString(columns.get(0)));
                    case Integer:
                        return Optional.of(rs.getInt(columns.get(0)));
                    case Tuple2:
                        return Optional.of(new Tuple2<>(rs.getString(columns.get(0)),rs.getInt(columns.get(1))));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<String> getPokemonName(int id){
        String sql = "SELECT identifier FROM pokemon WHERE id = "+id;
        Optional<?> result = executeTheQuery(sql,Type.String, Collections.singletonList("identifier"));
        return result.isPresent() ? (Optional<String>)result : Optional.empty();
    }

    public static Optional<Tuple2<String,Integer>> getPokemonAttack(int id){
        String sql = "SELECT identifier, power FROM moves WHERE id = "+id;
        Optional<?> result = executeTheQuery(sql,Type.Tuple2, Arrays.asList("identifier","power"));
        return result.isPresent() ? (Optional<Tuple2<String,Integer>>)result : Optional.empty();
    }

    public static Tuple2<Boolean,Optional<Integer>> pokemonHasToEvolve(int id, int actualLevel) {
        String sql = "SELECT id FROM pokemon_species WHERE evolves_from_species_id = "+id;
        Optional<?> result = executeTheQuery(sql,Type.Integer, Collections.singletonList("id"));
        if(result.isPresent()){
            int evolvedId = ((Optional<Integer>)result).get();
            sql = "SELECT minimum_level FROM pokemon_evolution WHERE evolved_species_id = "+evolvedId;
            result = executeTheQuery(sql,Type.Integer, Collections.singletonList("minimum_level"));
            if (((Optional<Integer>)result).get() == actualLevel) {
                return new Tuple2<Boolean,Optional<Integer>>(true,Optional.of(evolvedId));
            }
        }
        return new Tuple2<>(false,Optional.empty());
    }

    public static Optional<List<Integer>> getPossibleWildPokemon(int rarity) {
        init();
        String sql = "SELECT id "+
                     "FROM pokemon "+
                     "WHERE pokemon_rarity = "+ rarity +
                     " AND id <= "+ LAST_POKEMON_ID;
        try (PreparedStatement pstmt  = con.prepareStatement(sql)){
            ResultSet rs  = pstmt.executeQuery();
            List<Integer> pokemonList = new ArrayList<>();
            while (rs.next()) {
                pokemonList.add(rs.getInt("id"));
            }
            return Optional.of(pokemonList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static int getMinLevelWildPokemon(int id) {
        String sql = "SELECT minimum_level FROM pokemon_evolution WHERE evolved_species_id = "+id;
        Optional<?> result = executeTheQuery(sql,Type.Integer, Collections.singletonList("minimum_level"));
        return result.isPresent() ?  (((Optional<Integer>)result).get()) : 1;
    }

    public static int getMaxLevelWildPokemon(int id) {
        String sql = "SELECT id FROM pokemon_species WHERE evolves_from_species_id = "+id;
        Optional<?> res = executeTheQuery(sql,Type.Integer, Collections.singletonList("id"));
        return res.isPresent() ? getMinLevelWildPokemon(((Optional<Integer>)res).get()) : 70;
    }

    public static int getFirstStagePokemon(int id) {
        String sql = "SELECT first_stage FROM pokemon_species WHERE id = "+ id;
        Optional<?> result = executeTheQuery(sql,Type.Integer, Collections.singletonList("first_stage"));
        return ((Optional<Integer>)result).get();
    }

    public static Optional<Integer> getBaseExperiencePokemon(int id) {
        String sql = "SELECT base_experience FROM pokemon WHERE id = "+ getFirstStagePokemon(id);
        Optional<?> result = executeTheQuery(sql,Type.Integer, Collections.singletonList("base_experience"));
        return result.isPresent() ? ((Optional<Integer>)result) : Optional.empty();
    }

    public static int getRarity(int id) {
        String sql = "SELECT pokemon_rarity FROM pokemon WHERE id = "+ id;
        Optional<?> result = executeTheQuery(sql,Type.Integer, Collections.singletonList("pokemon_rarity"));
        return ((Optional<Integer>)result).get();
    }

    public static List<Integer> getAttacksList(int id) {
        int rarity = getRarity(id);
        int min = 0, max = 250;
        if( rarity == 1 || rarity == 5 || rarity == 10) {
            min = 1;
            max = 49;
        } else if ( rarity == 0 || rarity == 20 || rarity == 30) {
            min = 50;
            max = 119;
        } else if ( rarity == 50 || rarity == 100) {
            min = 20;
            max = 250;
        }
        init();
        List<Integer> attacksList = new ArrayList<>();
        String sql = "SELECT id FROM moves "+
                "WHERE generation_id = 1"+
                " AND power >= "+ min +
                " AND power <= "+ max;
        try (PreparedStatement pstmt  = con.prepareStatement(sql)){
            ResultSet rs  = pstmt.executeQuery();
            while (rs.next()) {
                attacksList.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attacksList;
    }


}
