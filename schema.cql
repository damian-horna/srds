CREATE KEYSPACE srds WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 3};

CREATE TABLE srds.flights (
    id text PRIMARY KEY,
    plane_id text,
    start_city text,
    target_city text
) WITH comment = 'Q: Find information about flights';

CREATE TABLE srds.hotels (
    id text PRIMARY KEY,
    city text
) WITH comment = 'Q: Find information about hotels';

CREATE TABLE srds.available_hotel_rooms_by_city_and_capacity (
    room_id text,
    city text,
    capacity int,
    hotel_id text,
    available boolean,
    PRIMARY KEY ((city, capacity), available, hotel_id)
) WITH comment = 'Q: Find hotel rooms in a given city with specified capacity';

CREATE TABLE srds.available_plane_seats_by_flight (
    plane_id text,
    flight_id text,
    seat_id text,
    available boolean,
    PRIMARY KEY ((flight_id), available, seat_id)
) WITH comment = 'Q: find available seats in a given flight/plane';

CREATE TABLE srds.seat_reservations_by_customer_id (
    seat_id text,
    plane_id text,
    flight_id text,
    customer_id text ,
    PRIMARY KEY ((customer_id), seat_id)
) WITH comment = 'Q: find/store seat reservations';