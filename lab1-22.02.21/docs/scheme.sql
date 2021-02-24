CREATE TABLE "user"
(
    user_id     SERIAL PRIMARY KEY,
    email       TEXT NOT NULL,
    password    TEXT NOT NULL,
    name        TEXT NOT NULL
);

CREATE TYPE TYPE_OF_ADVERT AS ENUM('SALE', 'RENT');

CREATE TYPE TYPE_OF_ESTATE AS ENUM('FLAT', 'NEWFLAT', 'ROOM', 'HOUSE', 'COTTAGE', 'DACHA', 'TOWNHOUSE', 'LAND');

CREATE TABLE advert
(
    advert_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES "user" ON DELETE CASCADE,
    type_of_advert TYPE_OF_ADVERT,
    type_of_estate TYPE_OF_ESTATE,
    cost INT NOT NULL,
    location TEXT NOT NULL,
    quantity_of_rooms INT NOT NULL,
    area INT,
    floor INT NOT NULL,
    description TEXT,
    name TEXT,
    mobile_number TEXT,
    is_realtor BOOL,
    image TEXT
);