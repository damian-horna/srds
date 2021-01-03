CREATE KEYSPACE srds WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 3};

CREATE TABLE srds.flights
(
    id          int PRIMARY KEY,
    plane_id    int,
    start_city  text,
    target_city text
) WITH comment = 'Q: Find information about flights';

CREATE TABLE srds.hotels
(
    id   int PRIMARY KEY,
    city text
) WITH comment = 'Q: Find information about hotels';

CREATE TABLE srds.available_plane_seats_by_flight
(
    plane_id  int,
    flight_id int,
    seat_id   int,
    available boolean,
    PRIMARY KEY ((flight_id), seat_id)
) WITH comment = 'Q: find available seats in a given flight/plane';

CREATE TABLE srds.available_hotel_rooms_by_capacity
(
    room_id   int,
    city      text,
    capacity  int,
    hotel_id  int,
    available boolean,
    PRIMARY KEY ((hotel_id), capacity, room_id)
) WITH comment = 'Q: Find hotel rooms in a given city with specified capacity';

CREATE TABLE srds.seat_reservations_by_customer_id
(
    seat_id     int,
    plane_id    int,
    flight_id   int,
    customer_id int,
    PRIMARY KEY ((seat_id), customer_id)
) WITH comment = 'Q: find/store seat reservations';

CREATE TABLE srds.room_reservations_by_customer_id
(
    hotel_id    int,
    room_id     int,
    customer_id int,
    PRIMARY KEY ((hotel_id), room_id, customer_id)
) WITH comment = 'Q: find/store room reservations';