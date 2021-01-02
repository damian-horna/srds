# srds

# Run instructions
### Setup cluster:
```
chmod +x start.sh
./start.sh
```

# Data modelling
### Requirements:
1) Ability to book a seat for a given flight for a single person
2) Ability to book a group of seats placed together (max 6 in one row)
3) Ability to book a seat (or group of seats) and a room in a hotel . (i.e. 1 or 2 + hotel)

### Defining application queries
* Q1: Find Planes with free groups of seats (1, 2, 3, 4, 5 or 6 located next to each other)
* Q2: Book one of free seats (or group of seats)
* Q3: Find planes with groups of seats and hotel rooms with specified capacity
* Q4: Book one of free seats (or group of seats) together with hotel room