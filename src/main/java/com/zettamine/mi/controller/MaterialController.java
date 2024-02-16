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

import com.zettamine.mi.entities.InspectionActuals;
import com.zettamine.mi.entities.InspectionLot;
import com.zettamine.mi.entities.Material;
import com.zettamine.mi.entities.MaterialInspectionCharacteristics;
import com.zettamine.mi.services.MaterialService;
import com.zettamine.mi.utils.AppConstants;

@Controller
@RequestMapping("/material")
public class MaterialController {

	private MaterialService materialService;

	private Logger LOG = LoggerFactory.getLogger(MaterialController.class);

	public MaterialController(MaterialService materialService) {

		this.materialService = materialService;

	}

	@GetMapping("/add")
	public String loadAddMateriaForm(Model model) {

		model.addAttribute("material", new Material());

		LOG.info("returing new add material form");

		return AppConstants.MATERIAL_FORM;
	}

	@GetMapping("/add/char")
	public String addMaterialCharAndPopulateDropDown(Model model) {

		model.addAttribute("materialChar", new MaterialInspectionCharacteristics());

		List<Material> materials = materialService.getAllMaterials();

		LOG.info("getting all materials for adding characteristics");

		model.addAttribute("materials", materials);

		LOG.info("returing material charecteristics form to add new Characteristics");

		return AppConstants.MATERIAL_CHAR_PAGE;
	}

	@PostMapping("/save")
	public String addMaterial(Model model, Material material, RedirectAttributes redirectAttributes) {

		LOG.info("new material saving {}", material);

		boolean result = materialService.addNewMaterial(material);

		if (result) {

			LOG.info("new material succesfully saved");

			redirectAttributes.addFlashAttribute("message", material.getMaterialDesc() + " material saved");

			LOG.info("redirecting to new material adding form");

			return "redirect:/material/add";
		} else {
			LOG.info("new material adding failed due to duplicate id or description");

			redirectAttributes.addFlashAttribute("error", material.getMaterialDesc() + " saving failed due to duplicate id or description");

			LOG.info("redirecting to new material adding form");

			return "redirect:/material/add";

		}
	}

	@GetMapping("/view/all")
	public String getAllMaterials(Model model) {

		LOG.info("calling material service for all material list");

		List<Material> materialsList = materialService.getAllMaterials();

		model.addAttribute("materialsList", materialsList);

		LOG.info("returing all active material list to view");

		return AppConstants.MATERIALS_LIST_PAGE;
	}

	@GetMapping("/edit")
	public String editMaterial(@RequestParam("id") String id, Model model) {

		LOG.info("calling material service for all material list");

		Material material = materialService.getMaterial(id);

		model.addAttribute("material", material);

		LOG.info("returning material details of material id : {}", id);

		return AppConstants.MATERIAL_EDIT;
	}

	@PostMapping("/edit/save")
	public String editMaterialSave(Model model, Material material, RedirectAttributes redirectAttributes) {

		LOG.info("material updation saving {}", material);

		boolean result = materialService.saveEditMaterial(material);

		if (result) {

			LOG.info("material updation succesfully saved");

			redirectAttributes.addFlashAttribute("message", material.getMaterialDesc() + " material updated");

			LOG.info("redirecting to new material adding form");

			return "redirect:/material/add";
		}
		return AppConstants.HOME_PAGE;
	}

	@GetMapping("/view/char")
	public String viewCharacteristics(@RequestParam("id") String id, Model model) {

		LOG.info("calling material service for material characteristics if material id : {}", id);

		List<MaterialInspectionCharacteristics> list = materialService.getAllCharacteristicsOfMaterial(id);
		Material material = materialService.getMaterial(id);

		model.addAttribute("list", list);
		model.addAttribute("mat", material.getMaterialDesc());

		LOG.info("returning characteristics list and material description of material id, {}", id);

		return AppConstants.CHARACTERISTICS_LIST_PAGE;
	}

	@GetMapping("/delete")
	public String deleteVendor(@RequestParam("id") String id, Model model) {

		System.out.println("delete material id : " + id);

		boolean result = materialService.deleteMaterial(id);

		if (result) {

			LOG.info("redirecting to  all material view form");

			return "redirect:/material/viewMaterials";
		}
		return AppConstants.HOME_PAGE;
	}

	@PostMapping("/sumbit/mat/char")
	public String addMaterialCharacteristics(Model model, MaterialInspectionCharacteristics matChar, RedirectAttributes redirectAttribute) {


		LOG.info("new material characteristics adding for material id : {}", matChar.getMaterial().getMaterialId());

		boolean result = materialService.addNewMaterialCharacteristic(matChar);

		if (result) {
			redirectAttribute.addFlashAttribute("message", matChar.getCharacteristicDescription() + " material characteristic saved");

			LOG.info("redirecting to material characteristics form");

			return "redirect:/material/add/char";
		}
		else {
			redirectAttribute.addFlashAttribute("error", matChar.getCharacteristicDescription() + " material characteristic is already availble or invalid upper limit or lower limit");

			LOG.info("saving failed | redirecting to material characteristics form");

			return "redirect:/material/add/char";
		}
		
	}

	@GetMapping("/lot/char")
	public String getLotCharacteristicsOfAssociatedMaterial(@RequestParam("id") String id, Model model) {

		List<MaterialInspectionCharacteristics> characteristicsList = materialService.getMaterialCharByLotId(id);

		model.addAttribute("actuals", new InspectionActuals());

		List<InspectionLot> lots = materialService.getAllInspectionLots();
		model.addAttribute("lots", lots);

		model.addAttribute("selectedLotId", id);

		model.addAttribute("characteristics", characteristicsList);

		LOG.info("returing lot inspection characteristics");

		return AppConstants.INSP_ACTU_PAGE;

	}

//	@PostMapping("/create/insp/lot")
//	public String addInspectionLot(Model model, InspectionLot lot) {
//
//		boolean result = materialService.createInspectionLot(lot);
//		
//		if (result) {
//			
//			LOG.info("redirecting to lot creation form");
//			
//			model.addAttribute("message", lot.getLotId() + " inspection lot created ");
//			return "redirect:/material/create/lot";
//		}
//		return "home";
//	}

//	@PostMapping("/save/insp/actu")
//	public String addInspectionActuals(Model model, InspectionActuals actuals) {
//		
//		LOG.info("new actuals adding for lot {}", actuals.getInspectionLot().getLotId());
//		
//
//		
//		boolean response = materialService.saveInspActuals(actuals);
//		if(response) {
//			
//			LOG.info("redirecting to material characteristics form");
//			
//			return "redirect:/material/add/actu";
//		}
//		return "home";
//	}

//	@GetMapping("/add/actu")
//	public String addMaterialActuals( Model model) {
//		
//			model.addAttribute("actuals", new InspectionActuals());
//			
////		List<MaterialInspectionCharacteristics> characteristics = materialService.getAllMaterials();
//			
//			List<InspectionLot> lots = materialService.getAllInspectionLots();
//			
////		model.addAttribute("characteristics", characteristics);
//			
//			model.addAttribute("lots", lots);
//			
//			model.addAttribute("selectedLotId", "select lot");
//			
//			LOG.info("returning actuals form with lots");
//			
//			return "material-actu";
//		
//	}

//	@GetMapping("/create/lot")
//	public String createInpesctinLot(Model model) {
//		
//		model.addAttribute("inspectionLot", new InspectionLot());
//		
//		List<Vendor> vendors = materialService.getAllVendors();
//		
//		List<Material> materials = materialService.getAllMaterials();
//		
//		List<Plant> plants = materialService.getAllPlants();
//
//		model.addAttribute("materials", materials);
//		model.addAttribute("vendors", vendors);
//		model.addAttribute("plants", plants);
//		
//		LOG.info("returing lot creation form along with materials, vendors, plants");
//		
//		return "insp-lot";
//	}
}
