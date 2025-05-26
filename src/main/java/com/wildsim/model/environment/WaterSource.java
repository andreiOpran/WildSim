package com.wildsim.model.environment;

public class WaterSource {
    private int x;
    private int y;
    private double waterLevel;

    public WaterSource(int x, int y, double waterLevel) {
        this.x = x;
        this.y = y;
        this.waterLevel = waterLevel;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public double getWaterLevel() { return waterLevel; }
    public void setWaterLevel(double waterLevel) { this.waterLevel = waterLevel; }
}