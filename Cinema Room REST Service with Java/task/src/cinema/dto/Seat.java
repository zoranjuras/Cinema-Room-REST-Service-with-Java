package cinema.dto;

import com.fasterxml.jackson.annotation.*;
import java.util.*;

public class Seat {
    private final int row;
    private final int column;
    private final int price;
    private boolean purchased;
    private UUID uuid;

    public Seat(int row, int column) {

        this.row = row;
        this.column = column;
        purchased = false;
        price = row <= 4? 10 : 8;
        this.uuid = null;
    }

    public boolean attemptPurchase() {
        if (!this.purchased) {
            this.purchased = true;
            return true;
        }
        return false;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getPrice() {
        return price;
    }
    @JsonIgnore
    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    @JsonIgnore
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
