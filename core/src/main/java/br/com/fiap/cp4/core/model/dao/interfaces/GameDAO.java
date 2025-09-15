package br.com.fiap.cp4.core.model.dao.interfaces;

import br.com.fiap.cp4.core.model.entities.Game;
import java.util.List;
import java.util.Optional;

public interface GameDAO {
    Game save(Game game);
    Optional<Game> findById(Integer id);
    List<Game> findAll();
    Game update(Game game);
    boolean deleteById(Integer id);

    List<Game> findByStatus(String status);
    List<Game> findByGenre(String genre);
    List<Game> findByPlatform(String platform);
    List<Game> findByReleaseYear(int releaseYear);
    List<Game> findByTitleContaining(String title);

    List<Game> findFavorites();
    boolean toggleFavorite(Integer gameId);

    int countByStatus(String status);
    int countTotal();
}