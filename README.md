# srds

# Run instructions
### Setup cluster:
```
chmod +x start.sh
./start.sh
```

### Create a schema:
```
ccm node1 cqlsh -f schema.cql
```

### Run the app:
```
gradle run
```

# Data modelling
### Requirements:
1) Ability to book a seat for a given flight for a single person
2) Ability to book a group of seats placed together (max 6 in one row)
3) Ability to book a seat (or group of seats) and a room in a hotel . (i.e. 1 or 2 + hotel)

### Defining application queries
* Q1: Find free groups of seats (1, 2, 3, 4, 5 or 6 located next to each other) in a given flight/plane
* Q2: Book one of free seats (or group of seats)
* Q3: Find free groups of seats together with a hotel room in a given flight/plane and destination city
* Q4: Book one of free seats (or group of seats) together with hotel room

### Simplifying assumptions:
All flights happen in the same day, no need to worry about dates too much

### Sample use case #1
1. Thomas wants to book a group of 2 adjacent seats in a randomly selected flight
2. First look up free groups of seets
3. Let Thomas choose on of the groups
4. Book them
5. At the end: Check if assigned to Thomas

### Sample use case #2
1. Thomas wants to book a single seat in a randomly selected flight
2. Fetch free sets
3. Thomas chooses one of them
4. Thomas books it
5. At the end: Check if place is assigned to Thomas

### Sample use case #3
1. Thomas wants to book a group of 4 seats for him and his family together with a hotel room for 4 people in a selected city
2. Randomly pick up a flight and look up free groups of 4 adjacent seats only if hotel room is available as well
3. Book both the group of seats and hotel room
4. At the end: Check if booked successfully for statistics

### Sample use case #4
1. Thomas is already in the city and is looking for a room
2. Randomly select a hotel and book a room if available
3. At the end: Check if booked successfully for statistics

### Tables:
available_plane_seats_by_flight -> to book a group of seats if possible

available_hotel_rooms_by_city_and_capacity -> to book one of the rooms with specified capacity in a given city 
flights -> we need them because we'll choose one of them randomly 

seat_reservations_by_customer_id -> for statistics checks at the end
room_reservations_by_customer_id -> for statistics checks at the end