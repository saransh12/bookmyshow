package com.example.bookmyshow.enums;

public enum SeatType {
    SILVER(100.0),
    GOLD(200.0),
    PLATINUM(300.0);

    private final double price;

    SeatType(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
