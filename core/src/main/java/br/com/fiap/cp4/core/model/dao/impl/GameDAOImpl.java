package br.com.fiap.cp4.core.model.dao.impl;

import br.com.fiap.cp4.core.database.ConnectionFactory;
import br.com.fiap.cp4.core.model.dao.interfaces.GameDAO;
import br.com.fiap.cp4.core.model.entities.Game;
import br.com.fiap.cp4.core.model.enums.GameGenre;
import br.com.fiap.cp4.core.model.enums.GamePlatform;
import br.com.fiap.cp4.core.model.enums.GameStatus;
import br.com.fiap.cp4.core.model.exceptions.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameDAOImpl implements GameDAO {

    private static final String INSERT_SQL =
            "INSERT INTO games (title, genre, platform, release_year, status, image_data, is_favorite) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM games WHERE id = ?";

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM games ORDER BY title";

    private static final String UPDATE_SQL =
            "UPDATE games SET title = ?, genre = ?, platform = ?, release_year = ?, status = ?, image_data = ?, is_favorite = ? WHERE id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM games WHERE id = ?";

    private static final String SELECT_BY_STATUS_SQL =
            "SELECT * FROM games WHERE status = ? ORDER BY title";

    private static final String SELECT_BY_GENRE_SQL =
            "SELECT * FROM games WHERE genre = ? ORDER BY title";

    private static final String SELECT_BY_PLATFORM_SQL =
            "SELECT * FROM games WHERE platform = ? ORDER BY title";

    private static final String SELECT_BY_RELEASE_YEAR_SQL =
            "SELECT * FROM games WHERE release_year = ? ORDER BY title";

    private static final String SELECT_BY_TITLE_CONTAINING_SQL =
            "SELECT * FROM games WHERE title ILIKE ? ORDER BY title";

    private static final String SELECT_FAVORITES_SQL =
            "SELECT * FROM games WHERE is_favorite = true ORDER BY title";

    private static final String TOGGLE_FAVORITE_SQL =
            "UPDATE games SET is_favorite = NOT is_favorite WHERE id = ?";

    private static final String COUNT_BY_STATUS_SQL =
            "SELECT COUNT(*) FROM games WHERE status = ?";

    private static final String COUNT_ALL_SQL =
            "SELECT COUNT(*) FROM games";

    @Override
    public Game save(Game game) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setGameParameters(ps, game);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Failed to save game, no rows affected");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    game.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Failed to save game, no ID obtained");
                }
            }

            return game;

        } catch (SQLException e) {
            throw new DAOException("Error saving game: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Game> findById(Integer id) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToGame(rs));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DAOException("Error finding game by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Game> findAll() {
        List<Game> games = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                games.add(mapResultSetToGame(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Error finding all games: " + e.getMessage(), e);
        }

        return games;
    }

    @Override
    public Game update(Game game) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            setGameParameters(ps, game);
            ps.setInt(8, game.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Failed to update game, no rows affected");
            }

            return game;

        } catch (SQLException e) {
            throw new DAOException("Error updating game: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DAOException("Error deleting game: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Game> findByStatus(String status) {
        return findByStringField(SELECT_BY_STATUS_SQL, status);
    }

    @Override
    public List<Game> findByGenre(String genre) {
        return findByStringField(SELECT_BY_GENRE_SQL, genre);
    }

    @Override
    public List<Game> findByPlatform(String platform) {
        return findByStringField(SELECT_BY_PLATFORM_SQL, platform);
    }

    @Override
    public List<Game> findByReleaseYear(int releaseYear) {
        List<Game> games = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_RELEASE_YEAR_SQL)) {

            ps.setInt(1, releaseYear);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    games.add(mapResultSetToGame(rs));
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Error finding games by release year: " + e.getMessage(), e);
        }

        return games;
    }

    @Override
    public List<Game> findByTitleContaining(String title) {
        List<Game> games = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_TITLE_CONTAINING_SQL)) {

            ps.setString(1, "%" + title + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    games.add(mapResultSetToGame(rs));
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Error finding games by title: " + e.getMessage(), e);
        }

        return games;
    }

    @Override
    public List<Game> findFavorites() {
        List<Game> games = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_FAVORITES_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                games.add(mapResultSetToGame(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Error finding favorite games: " + e.getMessage(), e);
        }

        return games;
    }

    @Override
    public boolean toggleFavorite(Integer gameId) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(TOGGLE_FAVORITE_SQL)) {

            ps.setInt(1, gameId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new DAOException("Error toggling favorite status: " + e.getMessage(), e);
        }
    }

    @Override
    public int countByStatus(String status) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(COUNT_BY_STATUS_SQL)) {

            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Error counting games by status: " + e.getMessage(), e);
        }

        return 0;
    }

    @Override
    public int countTotal() {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(COUNT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new DAOException("Error counting total games: " + e.getMessage(), e);
        }

        return 0;
    }

    private List<Game> findByStringField(String sql, String value) {
        List<Game> games = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, value);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    games.add(mapResultSetToGame(rs));
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Error finding games: " + e.getMessage(), e);
        }

        return games;
    }

    private void setGameParameters(PreparedStatement ps, Game game) throws SQLException {
        ps.setString(1, game.getTitle());
        ps.setString(2, game.getGenre().name());
        ps.setString(3, game.getPlatform().name());
        ps.setInt(4, game.getReleaseYear());
        ps.setString(5, game.getStatus().name());

        if (game.getImageData() != null) {
            ps.setBytes(6, game.getImageData());
        } else {
            ps.setNull(6, Types.BINARY);
        }

        ps.setBoolean(7, game.isFavorite());
    }

    private Game mapResultSetToGame(ResultSet rs) throws SQLException {
        String title = rs.getString("title");
        GameGenre genre = GameGenre.fromEnumName(rs.getString("genre"));
        GamePlatform platform = GamePlatform.fromEnumName(rs.getString("platform"));
        int releaseYear = rs.getInt("release_year");
        GameStatus status = GameStatus.fromEnumName(rs.getString("status"));

        byte[] imageData = rs.getBytes("image_data");
        if (rs.wasNull()) {
            imageData = null;
        }

        boolean isFavorite = rs.getBoolean("is_favorite");

        Game game = new Game(title, genre, platform, releaseYear, status, imageData);
        game.setId(rs.getInt("id"));
        game.setFavorite(isFavorite);

        return game;
    }
}