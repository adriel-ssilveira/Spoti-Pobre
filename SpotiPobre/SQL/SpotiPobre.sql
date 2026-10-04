
-- SPOTIPOBRE




-- 1. TABELA USUARIO


CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);



-- 2. TABELA ALBUM


CREATE TABLE album (
    id_album SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    artista VARCHAR(100) NOT NULL
);



-- 3. TABELA MUSICA


CREATE TABLE musica (
    id_musica SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);



-- 4. TABELA ALBUM_MUSICA


CREATE TABLE album_musica (
    id_album INTEGER NOT NULL,
    id_musica INTEGER NOT NULL,

    PRIMARY KEY (id_album, id_musica),

    FOREIGN KEY (id_album)
        REFERENCES album(id_album),

    FOREIGN KEY (id_musica)
        REFERENCES musica(id_musica)
);



-- 5. TABELA PLAYLIST


CREATE TABLE playlist (
    id_playlist SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    publica BOOLEAN NOT NULL
);



-- 6. TABELA USUARIO_PLAYLIST


CREATE TABLE usuario_playlist (
    id_usuario INTEGER NOT NULL,
    id_playlist INTEGER NOT NULL,
    dono BOOLEAN NOT NULL,

    PRIMARY KEY (id_usuario, id_playlist),

    FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario),

    FOREIGN KEY (id_playlist)
        REFERENCES playlist(id_playlist)
);


-- 7. TABELA REPRODUCAO


CREATE TABLE reproducao (
    id_reproducao SERIAL PRIMARY KEY,
    id_musica INTEGER NOT NULL,
    id_usuario INTEGER NOT NULL,
    data_hora TIMESTAMP NOT NULL,

    FOREIGN KEY (id_musica)
        REFERENCES musica(id_musica),

    FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
);



-- DADOS INICIAIS




-- USUARIOS


INSERT INTO usuario (nome)
VALUES
    ('Ronaldo'),
    ('João'),
    ('Maria');



-- ALBUNS


INSERT INTO album (nome, artista)
VALUES
    ('Appetite for Destruction', 'Guns N Roses'),
    ('Graduation', 'Kanye West'),
    ('Thriller', 'Michael Jackson');



-- MUSICAS


INSERT INTO musica (nome)
VALUES
    -- Thriller
    ('Wanna Be Startin Somethin'),
    ('Baby Be Mine'),
    ('The Girl Is Mine'),
    ('Thriller'),
    ('Beat It'),
    ('Billie Jean'),
    ('Human Nature'),
    ('P.Y.T. (Pretty Young Thing)'),
    ('The Lady in My Life'),

    -- Appetite for Destruction
    ('Welcome to the Jungle'),
    ('Its So Easy'),
    ('Nightrain'),
    ('Out Ta Get Me'),
    ('Mr. Brownstone'),
    ('Paradise City'),
    ('My Michelle'),
    ('Think About You'),
    ('Sweet Child o Mine'),
    ('Youre Crazy'),
    ('Anything Goes'),
    ('Rocket Queen'),

    -- Graduation
    ('Good Morning'),
    ('Champion'),
    ('Stronger'),
    ('I Wonder'),
    ('Good Life'),
    ('Cant Tell Me Nothing'),
    ('Barry Bonds'),
    ('Drunk and Hot Girls'),
    ('Flashing Lights'),
    ('Everything I Am'),
    ('The Glory'),
    ('Homecoming'),
    ('Big Brother'),
    ('Good Night');



-- RELACIONAMENTO ALBUM_MUSICA


INSERT INTO album_musica (id_album, id_musica)
VALUES

    -- Appetite for Destruction
    (1, 10),
    (1, 11),
    (1, 12),
    (1, 13),
    (1, 14),
    (1, 15),
    (1, 16),
    (1, 17),
    (1, 18),
    (1, 19),
    (1, 20),
    (1, 21),

    -- Graduation
    (2, 22),
    (2, 23),
    (2, 24),
    (2, 25),
    (2, 26),
    (2, 27),
    (2, 28),
    (2, 29),
    (2, 30),
    (2, 31),
    (2, 32),
    (2, 33),
    (2, 34),
    (2, 35),

    -- Thriller
    (3, 1),
    (3, 2),
    (3, 3),
    (3, 4),
    (3, 5),
    (3, 6),
    (3, 7),
    (3, 8),
    (3, 9);


-- FIM DO SCRIPT
