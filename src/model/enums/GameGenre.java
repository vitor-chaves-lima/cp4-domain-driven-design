package model.enums;

public enum GameGenre {
    ACTION("Ação"),
    RPG("RPG"),
    STRATEGY("Estratégia"),
    ADVENTURE("Aventura"),
    SIMULATION("Simulação"),
    SPORTS("Esportes"),
    RACING("Corrida"),
    FIGHTING("Luta"),
    PUZZLE("Quebra-cabeça"),
    PLATFORM("Plataforma"),
    SHOOTER("Tiro"),
    SURVIVAL("Sobrevivência"),
    HORROR("Terror"),
    INDIE("Indie"),
    MMO("MMO"),
    MOBA("MOBA"),
    BATTLE_ROYALE("Battle Royale"),
    CARD("Cartas"),
    BOARD("Tabuleiro"),
    MUSIC("Musical"),
    EDUCATIONAL("Educativo"),
    OTHER("Outro");

    private final String displayName;

    GameGenre(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static GameGenre fromEnumName(String enumName) {
        try {
            return GameGenre.valueOf(enumName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Gender not found: " + enumName);
        }
    }

    @Override
    public String toString() {
        return displayName;
    }
}