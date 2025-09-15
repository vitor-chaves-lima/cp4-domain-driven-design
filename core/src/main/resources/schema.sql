    CREATE TABLE games
    (
        id           SERIAL PRIMARY KEY,
        title        VARCHAR(255) NOT NULL,
        genre        VARCHAR(50)  NOT NULL,
        platform     VARCHAR(50)  NOT NULL,
        release_year INT          NOT NULL,
        status       VARCHAR(50)  NOT NULL,
        image_data   BYTEA,
        is_favorite  BOOLEAN DEFAULT FALSE,
        created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

    CREATE INDEX idx_games_status ON games(status);
    CREATE INDEX idx_games_genre ON games(genre);
    CREATE INDEX idx_games_platform ON games(platform);
    CREATE INDEX idx_games_release_year ON games(release_year);
    CREATE INDEX idx_games_title ON games(title);
    CREATE INDEX idx_games_is_favorite ON games(is_favorite);