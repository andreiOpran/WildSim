package com.wildsim.model.organisms.plant;

import com.wildsim.environment.Ecosystem;
import com.wildsim.environment.Position;
import com.wildsim.model.organisms.Organism;

public class Plant extends Organism {
	protected int growthRate;
	protected int energyThreshold;

	public Plant(Position position, int energy, int growthRate, int energyThreshold) {
		super(position, energy, 'P');
		this.growthRate = growthRate;
		this.energyThreshold = energyThreshold;
	}

	public int getGrowthRate() {
		return growthRate;
	}
	public void setGrowthRate(int growthRate) {
		this.growthRate = growthRate;
	}
	public int getEnergyThreshold() {
		return energyThreshold;
	}
	public void setEnergyThreshold(int energyThreshold) {
		this.energyThreshold = energyThreshold;
	}

	@Override
	public void live(Ecosystem ecosystem) {
		if (energy < energyThreshold) {
			energy += growthRate;
		}
		if (energy > energyThreshold) {
			energy = energyThreshold;
		}
	}

}

