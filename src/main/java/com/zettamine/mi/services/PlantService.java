package com.zettamine.mi.services;

import java.util.List;

import com.zettamine.mi.entities.Plant;

public interface PlantService {

	boolean addNewPlant(Plant plant);

	List<Plant> getAllPlants();

	Plant getPlant(String id);

}
