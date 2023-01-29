DROP TABLE IF EXISTS users, locations, categories, compilations, events, participation_requests, compilations_events,
    comments;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255) UNIQUE                     NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat FLOAT                                   NOT NULL,
    lon FLOAT                                   NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) UNIQUE                     NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title  VARCHAR(512)                            NOT NULL,
    pinned BOOLEAN,
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    category_id        BIGINT                                  NOT NULL,
    created_on         TIMESTAMP,
    description        VARCHAR(7000)                           NOT NULL,
    event_date         TIMESTAMP                               NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    location_id        BIGINT,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  INTEGER,
    published_on       TIMESTAMP,
    request_moderation BOOLEAN,
    state              VARCHAR(255),
    title              VARCHAR(120)                            NOT NULL,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE,
    CONSTRAINT fk_initiator FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES locations (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS participation_requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP                               NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    status       VARCHAR(255),
    CONSTRAINT pk_participation PRIMARY KEY (id),
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_requester FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,
    CONSTRAINT pk_compilation_event PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT fk_compilation_event FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE,
    CONSTRAINT fk_event_compilation FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text       VARCHAR(512)                            NOT NULL,
    event_id   BIGINT                                  NOT NULL,
    author_id  BIGINT                                  NOT NULL,
    created_on TIMESTAMP,
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT fk_event_com FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE
);


