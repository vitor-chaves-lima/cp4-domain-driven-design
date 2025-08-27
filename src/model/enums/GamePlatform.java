package model.enums;

public enum GamePlatform {
    PC("PC"),
    XBOX_SERIES("Xbox Series X/S"),
    XBOX_ONE("Xbox One"),
    PLAYSTATION_5("PlayStation 5"),
    PLAYSTATION_4("PlayStation 4"),
    NINTENDO_SWITCH("Nintendo Switch"),
    STEAM_DECK("Steam Deck"),
    MOBILE_ANDROID("Android"),
    MOBILE_IOS("iPhone/iPad"),
    WEB("Navegador Web"),
    VR("Realidade Virtual"),
    OTHER("Outro");

    private final String displayName;

    GamePlatform(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static GamePlatform fromEnumName(String enumName) {
        try {
            return GamePlatform.valueOf(enumName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Platform not found: " + enumName);
        }
    }

    @Override
    public String toString() {
        return displayName;
    }
}