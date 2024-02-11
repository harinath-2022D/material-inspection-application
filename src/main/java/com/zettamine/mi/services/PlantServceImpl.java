package com.zettamine.mi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zettamine.mi.entities.Plant;
import com.zettamine.mi.entities.Vendor;
import com.zettamine.mi.repositories.PlantRepository;

@Service
public class PlantServceImpl implements PlantService {
	private PlantRepository plantReposotory;

	public PlantServceImpl(PlantRepository plantReposotory) {
		super();
		this.plantReposotory = plantReposotory;
	}

	@Override
	public boolean addNewPlant(Plant plant) {
//		plant.setStatus("Y");
//		Plant savedPlant = plantReposotory.save(plant);
//		if(savedPlant.getVendorId() > 0) {
//			return true;
//		}
//		return true;
		
		if(plant.getPlantId() != "") {
//			plant.setStatus("Y");
			Plant savedPlant = plantReposotory.save(plant);
			if(savedPlant.getPlantId() != "") {
				return true;
			}
			return false;
			}else {
				Optional<Plant> optVendor = plantReposotory.findById(plant.getPlantId());
				Plant prevPlant = optVendor.get();
				prevPlant.setLocation(plant.getLocation());
				prevPlant.setPlantName(plant.getPlantName());
				plantReposotory.save(prevPlant);
				return true;
			}
	}

	@Override
	public List<Plant> getAllPlants() {
		List<Plant> plantsList = plantReposotory.findAll();
		return plantsList;
	}

	@Override
	public Plant getPlant(String id) {
		Optional<Plant> plant = plantReposotory.findById(id);
		if(plant.isEmpty()) {
			return null;
		}
		return plant.get();
	}
	
	
}
