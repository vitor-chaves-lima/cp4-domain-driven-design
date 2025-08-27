package model.enums;

public enum GameStatus {
    PLAYING("Jogando", "#4CAF50"),
    COMPLETED("Concluído", "#2196F3"),
    WISHLIST("Lista de Desejos", "#FF9800"),
    DROPPED("Abandonado", "#F44336"),
    ON_HOLD("Pausado", "#9C27B0");

    private final String displayName;
    private final String color;

    GameStatus(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColor() {
        return color;
    }

    public static GameStatus fromEnumName(String enumName) {
        try {
            return GameStatus.valueOf(enumName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status not found: " + enumName);
        }
    }

    @Override
    public String toString() {
        return displayName;
    }
}