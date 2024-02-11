package com.zettamine.mi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zettamine.mi.entities.InspectionActuals;
import com.zettamine.mi.entities.InspectionLot;
import com.zettamine.mi.entities.Material;
import com.zettamine.mi.entities.MaterialInspectionCharacteristics;
import com.zettamine.mi.entities.Plant;
import com.zettamine.mi.entities.Vendor;
import com.zettamine.mi.services.MaterialService;

@Controller
@RequestMapping("/material")
public class MaterialController {

	private MaterialService materialService;

	public MaterialController(MaterialService materialService) {
		this.materialService = materialService;
	}

	@GetMapping("/addMaterial")
	public String loadAddVendorForm(Model model) {
		model.addAttribute("material", new Material());
		return "material";
	}

	@GetMapping("/add/char")
	public String addMaterialCharAndPopulateDropDown(Model model) {
		model.addAttribute("materialChar", new MaterialInspectionCharacteristics());
		List<Material> materials = materialService.getAllMaterials();
		model.addAttribute("materials", materials);
		return "material-char";
	}

	@GetMapping("/add/actu")
	public String addMaterialActuals( Model model) {
		
			model.addAttribute("actuals", new InspectionActuals());
//		List<MaterialInspectionCharacteristics> characteristics = materialService.getAllMaterials();
			List<InspectionLot> lots = materialService.getAllInspectionLots();
//		model.addAttribute("characteristics", characteristics);
			model.addAttribute("lots", lots);
			model.addAttribute("selectedLotId", "select lot");
			return "material-actu";
		
	}

	@GetMapping("/create/lot")
	public String createInpesctinLot(Model model) {
		model.addAttribute("inspectionLot", new InspectionLot());
		List<Vendor> vendors = materialService.getAllVendors();
		List<Material> materials = materialService.getAllMaterials();
		List<Plant> plants = materialService.getAllPlants();

		model.addAttribute("materials", materials);
		model.addAttribute("vendors", vendors);
		model.addAttribute("plants", plants);
		return "insp-lot";
	}

	@PostMapping("/save")
	public String addVendor(Model model, Material material) {
		System.out.println(material);
		boolean result = materialService.addNewMaterial(material);
		if (result) {
			model.addAttribute("message", material.getMaterialDesc() + " material saved");
			return "redirect:/material/addMaterial";
		}
		return "home";
	}

	@GetMapping("/viewMaterials")
	public String getAllVendors(Model model) {
		List<Material> materialsList = materialService.getAllMaterials();
		model.addAttribute("materialsList", materialsList);
		return "material-list";
	}

	@GetMapping("/edit")
	public String editVendor(@RequestParam("id") String id, Model model) {
		System.out.println("edit vendor id : " + id);
		Material material = materialService.getMaterial(id);
		model.addAttribute("material", material);
		return "material";
	}

	@GetMapping("/delete")
	public String deleteVendor(@RequestParam("id") String id, Model model) {
		System.out.println("delete material id : " + id);
		boolean result = materialService.deleteMaterial(id);
		if (result) {
			return "redirect:/material/viewMaterials";
		}
		return "home";
	}

	@PostMapping("/sumbit/mat/char")
	public String addMaterialCharacteristics(Model model, MaterialInspectionCharacteristics matChar) {
		System.out.println(matChar);
		boolean result = materialService.addNewMaterialCharacteristic(matChar);
		if (result) {
			model.addAttribute("message", matChar.getCharacteristicId() + " material characteristic saved");
			return "redirect:/material/add/char";
		}
		return "home";
	}

	@PostMapping("/create/insp/lot")
	public String addInspectionLot(Model model, InspectionLot lot) {
//		System.out.println(lot);
		boolean result = materialService.createInspectionLot(lot);
		if (result) {
			model.addAttribute("message", lot.getLotId() + " inspection lot created ");
			return "redirect:/material/create/lot";
		}
		return "home";
	}
	
	
	
	@PostMapping("/save/insp/actu")
	public String addInspectionActuals(Model model, InspectionActuals actuals) {
		System.out.println(actuals);
		Integer lotId = Integer.valueOf(actuals.getRequiredLot());
//		InspectionLot lotid = materialService.getInspectionLot(lotId);
//		System.out.println(lotid.getLotId());
		System.out.println("inspection lot id :" + lotId);
		boolean response = materialService.saveInspActuals(actuals);
		if(response) {
			return "redirect:/material/add/actu";
		}
		return "home";
	}

	@GetMapping("/lot/char")
	public String getLotCharacteristicsOfAssociatedMaterial(@RequestParam("id") String id, Model model) {
		System.out.println("lot id : " + id);
		
		List<MaterialInspectionCharacteristics> characteristicsList = materialService.getMaterialCharByLotId(id);

//		model.addAttribute("characteristics", characteristicsList);
	
			model.addAttribute("actuals", new InspectionActuals());
			List<InspectionLot> lots = materialService.getAllInspectionLots();
			model.addAttribute("lots", lots);
			model.addAttribute("selectedLotId", id);
//			model.addAttribute("inspectionLot", lotid);
			model.addAttribute("characteristics", characteristicsList);
			return "material-actu";
		
//		return "redirect:/material/add/actu/";
	}
}
