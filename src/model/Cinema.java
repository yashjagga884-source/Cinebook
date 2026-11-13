package model;

public class Cinema {
    private String cinemaId;
    private String name;
    private String location;

    public Cinema() {
    }

    public Cinema(String cinemaId, String name, String location) {
        this.cinemaId = cinemaId;
        this.name = name;
        this.location = location;
    }

    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Cinema{" +
                "cinemaId='" + cinemaId + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
