package com.zettamine.mi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zettamine.mi.entities.Plant;
import com.zettamine.mi.services.PlantService;
import com.zettamine.mi.utils.AppConstants;

@Controller
@RequestMapping("/plant")
public class PlantController {
	private PlantService plantService;
	
	private Logger LOG = LoggerFactory.getLogger(PlantController.class);
	
	public PlantController(PlantService plantService) {
		super();
		this.plantService = plantService;
	}

	@GetMapping("/add-plant")
	public String loadAddVendorForm(Model model) {
		model.addAttribute("plant", new Plant());
		
		return AppConstants.PLANT_PAGE;
	}
	
	@PostMapping("/sumbit-plant")
	public String addVendor(Model model, Plant plant, RedirectAttributes redirectAttributes) {
		
		boolean result = plantService.addNewPlant(plant);
		if (result) {
			
			LOG.info("new plant added successfully | {}", plant);
			
			redirectAttributes.addFlashAttribute("message", plant.getPlantName() + " plant saved");
			
			return "redirect:/plant/add-plant";
		}else {
			redirectAttributes.addFlashAttribute("error", plant.getPlantName() + " plant is already added");
			return "redirect:/plant/add-plant";
		}
		
	}
	
	@GetMapping("/view/plants")
	public String getAllVendors(Model model) {
		
		List<Plant> plantsList = plantService.getAllPlants();
		model.addAttribute("plantsList", plantsList);
		
		return AppConstants.PLANTLIST_PAGE;
	}
	
	@GetMapping("/edit")
	public String editVendor(@RequestParam("id") String id, Model model) {
		System.out.println("edit plant id : " + id);
		Plant plant = plantService.getPlant(id);
		model.addAttribute("plant", plant);
		return AppConstants.PLANT_EDIT_PAGE;
	}
	
	@PostMapping("/edit/save")
	public String saveEditPlant(Model model, Plant plant, RedirectAttributes redirectAttributes) {
		boolean result = plantService.saveEditedPlant(plant);
		if (result) {
			
			LOG.info("plant updation successfull | {}", plant);
			
			redirectAttributes.addFlashAttribute("message", plant.getPlantName() + " plant updated");
			
			return "redirect:/plant/add-plant";
		}else {
			redirectAttributes.addFlashAttribute("message", plant.getPlantName() + " plant is already added");
			return "redirect:/plant/add-plant";
		}
	}
}
