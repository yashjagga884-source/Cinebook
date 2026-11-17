package model;

import java.util.List;
import java.util.ArrayList;

public class Screen {
    private String screenId;
    private int totalSeats;
    private String seatLayout;

    public Screen() {
    }

    public Screen(String screenId, int totalSeats, String seatLayout) {
        this.screenId = screenId;
        this.totalSeats = totalSeats;
        this.seatLayout = seatLayout;
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public String getSeatLayout() {
        return seatLayout;
    }

    public void setSeatLayout(String seatLayout) {
        this.seatLayout = seatLayout;
    }

    @Override
    public String toString() {
        return "Screen{" +
                "screenId='" + screenId + '\'' +
                ", totalSeats=" + totalSeats +
                ", seatLayout='" + seatLayout + '\'' +
                '}';
    }
}
