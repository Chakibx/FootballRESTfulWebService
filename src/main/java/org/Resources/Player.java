package org.Resources;

public class Player {
    private int rating;
    private int id;
    private String name;
    private int teamId;

    public Player(int id, String name, int teamId, int rating) {
        this.id = id;
        this.name = name;
        this.teamId = teamId;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Player{" +
                "rating=" + rating +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", teamId=" + teamId +
                '}';
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
