package com.zettamine.mi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zettamine.mi.entities.Plant;
import com.zettamine.mi.entities.Vendor;
import com.zettamine.mi.repositories.PlantRepository;
import com.zettamine.mi.utils.StringUtil;

@Service
public class PlantServceImpl implements PlantService {
	private PlantRepository plantReposotory;

	public PlantServceImpl(PlantRepository plantReposotory) {
		super();
		this.plantReposotory = plantReposotory;
	}

	@Override
	public boolean addNewPlant(Plant plant) {
		Optional<Plant> optPlantId = plantReposotory.findById(plant.getPlantId());
		Optional<Plant> optPlantName = plantReposotory.findByPlantName(plant.getPlantName());

		if (optPlantId.isPresent() || optPlantName.isPresent()) {
			return false;
		}
		
		plant.setPlantId(StringUtil.removeAllSpaces(plant.getPlantId()));
		
		plant.setLocation(StringUtil.removeExtraSpaces(plant.getLocation()));

		plant.setPlantName(StringUtil.removeExtraSpaces(plant.getPlantName()));

		plantReposotory.save(plant);
		return true;
	}

	@Override
	public List<Plant> getAllPlants() {
		List<Plant> plantsList = plantReposotory.findAll();
		return plantsList;
	}

	@Override
	public Plant getPlant(String id) {
		Optional<Plant> plant = plantReposotory.findById(id);
		if (plant.isEmpty()) {
			return null;
		}
		return plant.get();
	}

	@Override
	public boolean saveEditedPlant(Plant plant) {

		plant.setPlantId(StringUtil.removeExtraSpaces(plant.getPlantId()));

		plant.setLocation(StringUtil.removeExtraSpaces(plant.getLocation()));

		plant.setPlantName(StringUtil.removeExtraSpaces(plant.getPlantName()));

		Plant savedPlant = plantReposotory.save(plant);
		if (savedPlant.getPlantId() != "") {
			return true;
		}
		return false;
	}

}
