package com.wildsim.model.environment;

import com.wildsim.environment.Position;

public class WaterSource extends Environment {
    private Position position;
    private double waterLevel;

    public WaterSource(Position position, double waterLevel) {
        super(position);
        this.position = position;
        this.waterLevel = waterLevel;
        this.symbol = 'W';
    }

    public Position getPosition() { return position; }
    public double getWaterLevel() { return waterLevel; }
    public void setWaterLevel(double waterLevel) { this.waterLevel = waterLevel; }
}