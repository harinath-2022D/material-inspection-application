package com.zettamine.mi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zettamine.mi.entities.Plant;
import com.zettamine.mi.entities.Vendor;
import com.zettamine.mi.services.PlantService;

@Controller
@RequestMapping("/plant")
public class PlantController {
	private PlantService plantService;
	
	public PlantController(PlantService plantService) {
		super();
		this.plantService = plantService;
	}

	@GetMapping("/add-plant")
	public String loadAddVendorForm(Model model) {
		model.addAttribute("plant", new Plant());
		return "plant";
	}
	
	@PostMapping("/sumbit-plant")
	public String addVendor(Model model, Plant plant) {
		System.out.println(plant);
		boolean result = plantService.addNewPlant(plant);
		if (result) {
			model.addAttribute("message", plant.getPlantName() + " plant saved");
			return "redirect:/plant/add-plant";
		}
		return "home";
	}
	
	@GetMapping("/view/plants")
	public String getAllVendors(Model model) {
		List<Plant> plantsList = plantService.getAllPlants();
		model.addAttribute("plantsList", plantsList);
		return "plant-list";
	}
	
	@GetMapping("/edit")
	public String editVendor(@RequestParam("id") String id, Model model) {
		System.out.println("edit plant id : " + id);
		Plant plant = plantService.getPlant(id);
		model.addAttribute("plant", plant);
		return "plant";
	}
}
