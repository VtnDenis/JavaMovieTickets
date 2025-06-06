package model;

public class Discount {
    private String code;
    private String description;
    private double percentage;
    private boolean active;

    public Discount(String code, String description, double percentage, boolean active) {
        this.code = code;
        this.description = description;
        this.percentage = percentage;
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getPercentage() {
        return percentage;
    }

    public boolean isActive() {
        return active;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}