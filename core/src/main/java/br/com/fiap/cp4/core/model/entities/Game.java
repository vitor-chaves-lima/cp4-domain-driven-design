package br.com.fiap.cp4.core.model.entities;

import br.com.fiap.cp4.core.model.enums.GameGenre;
import br.com.fiap.cp4.core.model.enums.GameStatus;
import br.com.fiap.cp4.core.model.enums.GamePlatform;

import java.util.Objects;

/**
 * Entity that represents a game in the system
 */
public class Game {
    private Integer id;
    private String title;
    private GameGenre genre;
    private GamePlatform platform;
    private int releaseYear;
    private GameStatus status;
    private byte[] imageData;

    public Game(String title, GameGenre genre, GamePlatform platform,
                int releaseYear, GameStatus status, byte[] imageData) {
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.releaseYear = releaseYear;
        this.status = status;
        this.imageData = imageData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GameGenre getGenre() {
        return genre;
    }

    public void setGenre(GameGenre genre) {
        this.genre = genre;
    }

    public GamePlatform getPlatform() {
        return platform;
    }

    public void setPlatform(GamePlatform platform) {
        this.platform = platform;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + (genre != null ? genre.getDisplayName() : "null") + '\'' +
                ", platform='" + (platform != null ? platform.getDisplayName() : "null") + '\'' +
                ", releaseYear=" + releaseYear +
                ", status='" + (status != null ? status.getDisplayName() : "null") + '\'' +
                ", imageData=" + (imageData != null ? imageData.length + " bytes" : "null") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}