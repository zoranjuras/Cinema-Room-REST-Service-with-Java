package cinema.dto;

import java.util.*;

public class ProjectionRoom {
    final int rows;
    final int columns;
    ArrayList<Seat> seats = new ArrayList<>();

    public ProjectionRoom(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        // Initialization of available seats
        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= columns; col++) {
                seats.add(new Seat(row, col));
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }
}
