package br.com.fiap.cp4.core.model.entities;

import br.com.fiap.cp4.core.model.enums.GameGenre;
import br.com.fiap.cp4.core.model.enums.GameStatus;
import br.com.fiap.cp4.core.model.enums.GamePlatform;

import java.util.Objects;

/**
 * Represents a video game entity in the system.
 * This class encapsulates all the properties and behaviors of a video game,
 * including its metadata, status, and platform information.
 * 
 * @author Game Management System
 * @version 1.0
 * @since 2024
 */
public class Game {
    /** Unique identifier for the game */
    private Integer id;
    
    /** Title of the game */
    private String title;
    
    /** Genre of the game (e.g., ACTION, RPG, STRATEGY) */
    private GameGenre genre;
    
    /** Platform the game is available on (e.g., PC, PLAYSTATION, XBOX) */
    private GamePlatform platform;
    
    /** Year the game was released */
    private int releaseYear;
    
    /** Current status of the game (e.g., NOT_STARTED, IN_PROGRESS, COMPLETED) */
    private GameStatus status;
    
    /** Binary data of the game's cover image */
    private byte[] imageData;
    
    /** Flag indicating if the game is marked as favorite */
    private boolean isFavorite;

    /**
     * Constructs a new Game with the specified details.
     *
     * @param title The title of the game
     * @param genre The genre of the game
     * @param platform The platform the game is available on
     * @param releaseYear The year the game was released
     * @param status The current status of the game
     * @param imageData Binary data of the game's cover image
     */
    public Game(String title, GameGenre genre, GamePlatform platform,
                int releaseYear, GameStatus status, byte[] imageData) {
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.releaseYear = releaseYear;
        this.status = status;
        this.imageData = imageData;
        this.isFavorite = false;
    }

    /**
     * Returns the unique identifier of the game.
     *
     * @return the game's unique identifier, or null if not yet persisted
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the game.
     * This is typically set by the persistence layer when the game is saved.
     *
     * @param id the unique identifier to set
     */
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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
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
                ", isFavorite=" + isFavorite +
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