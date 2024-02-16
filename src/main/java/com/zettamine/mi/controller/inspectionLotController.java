package com.zettamine.mi.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zettamine.mi.entities.InspectionActuals;
import com.zettamine.mi.entities.InspectionLot;
import com.zettamine.mi.entities.Material;
import com.zettamine.mi.entities.Plant;
import com.zettamine.mi.entities.Vendor;
import com.zettamine.mi.requestdtos.DateRangeLotSearch;
import com.zettamine.mi.requestdtos.Search;
import com.zettamine.mi.responsedto.DateRangeLotResponse;
import com.zettamine.mi.responsedto.EditLot;
import com.zettamine.mi.responsedto.LotActualsAndCharacteristics;
import com.zettamine.mi.services.InspectionServiceImpl;
import com.zettamine.mi.utils.AppConstants;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/insp")
public class InspectionLotController {
	private InspectionServiceImpl inspectionlotService;

	private Logger LOG = LoggerFactory.getLogger(InspectionLotController.class);

	public InspectionLotController(InspectionServiceImpl inspectionlotService) {
		super();
		this.inspectionlotService = inspectionlotService;
	}

	@GetMapping("/lot")
	public String loadInspectionLotDetails(Model model) {

		model.addAttribute("search", new Search());

		LOG.info("returing lot search form");

		return AppConstants.INSP_LOT_DETAILS;
	}

	@PostMapping("/lot/search")
	public String getInspectinLotDetails(Model model, Search search) {

		LOG.info("Searching lot having id : {}", search.getLot());

		return "redirect:/insp/lot/data?id=" + search.getLot();
	}

	@GetMapping("/lot/data")
	public String fetchInspectionLotDetails(@RequestParam Integer id, Model model) {

		model.addAttribute("search", new Search(id));

		LOG.info("Searching lot having id : {}", id);

		InspectionLot inspLot = inspectionlotService.getLotDetails(id);

		model.addAttribute("lot", inspLot);

		LOG.info("Retunring lot details of id : {}", id);

		return AppConstants.INSP_LOT_DETAILS;
	}

	@GetMapping("/lot/search/info")
	public String getActualAndOriginalOfLot(@RequestParam Integer id, Model model) {

		LOG.info("Finding lot actuals and characteristics of lot id : {}", id);

		List<LotActualsAndCharacteristics> list = inspectionlotService.getActualAndOriginalOfLot(id);

		model.addAttribute("list", list);

		LOG.info("Returning list of lot actual and characteristics of lot id : {}", id);

		return AppConstants.INSP_ACTU_CHAR_PAGE;
	}

	@GetMapping("/add/actu")
	public String addMaterialActuals(Model model) {

		model.addAttribute("actuals", new InspectionActuals());

		List<InspectionLot> lots = inspectionlotService.getAllInspectionLots();

		model.addAttribute("lots", lots);

		model.addAttribute("selectedLotId", "select lot");

		LOG.info("Retunring new Inspection Actual obj with inspection lots to add inspection actuals");

		return AppConstants.INSP_ACTU_PAGE;

	}

	@PostMapping("/save/insp/actu")
	public String addInspectionActuals(Model model, InspectionActuals actuals) {

		Integer lotId = Integer.valueOf(actuals.getRequiredLot());

		boolean response = inspectionlotService.saveInspActuals(actuals);

		if (response) {

			LOG.info("new Inspection actual saved for lot id : {}", lotId);

			return "redirect:/insp/add/actu";
		} else {
			
			LOG.info("Failed saving new inspection actual of lot id : {}", lotId);
			
			return AppConstants.HOME_PAGE;
		}
	}

	@GetMapping("/lot/edit/info")
	public String editInspectionLotDetails(@RequestParam Integer id, Model model) {
		
		LOG.info("Findind inspection lot for editing details id: {}", id);
		
		InspectionLot inspLot = inspectionlotService.getLotDetails(id);
		
		model.addAttribute("lot", inspLot);
		
		model.addAttribute("editLot", new EditLot());
		
		LOG.info("Returing inspectionlot along with edit form of lot id : {}", id);
		
		return AppConstants.INSP_EDIT_PAGE;
	}

	@PostMapping("/save/edit/lot")
	public String saveEditedLot(Model model, EditLot lot, HttpSession session) {

		boolean result = inspectionlotService.updateInspectionLot(lot, session);
		
		if(result) {
			
		Integer id = Integer.valueOf(lot.getId());
		
		LOG.info("Lot details are updated successfully lot id : {}", id);

		return "redirect:/insp/lot/data?id=" + id;
		
		}else {
			
			return AppConstants.HOME_PAGE;
		}
	}

	@GetMapping("/lot/date")
	public String getDateSearchForm(Model model) {
		
		model.addAttribute("searchObj", new DateRangeLotSearch());
		
		LOG.info("Returning new DateRangeSearch form");
		
		return AppConstants.DATE_RANGE_SEARCH_PAGE;
	}

	@GetMapping("/lot/date/search")
	public String DateRangeLotSearch(Model model, DateRangeLotSearch obj) {
//		System.out.println(obj.getMaterialId());
//		System.out.println(obj.getFromDate());
//		System.out.println(obj.getToDate());
//		System.out.println(obj.getPlantId());
//		System.out.println(obj.getVendorId());
//		System.out.println(obj.getStatus());
		
		LOG.info("Searching lots ");

		List<DateRangeLotResponse> list = inspectionlotService.getAllLotsDetailsBetweenDateRange(obj);
		
		model.addAttribute("list", list);
		
		LOG.info("Returning lots having search criteria , size : {}", list.size());
		
		return AppConstants.INSP_LOT_SEARCH;
	}

	@GetMapping("/create/lot")
	public String createInpesctinLot(Model model) {

		model.addAttribute("inspectionLot", new InspectionLot());

		List<Vendor> vendors = inspectionlotService.getAllVendors();

		List<Material> materials = inspectionlotService.getAllMaterials();

		List<Plant> plants = inspectionlotService.getAllPlants();

		model.addAttribute("materials", materials);
		model.addAttribute("vendors", vendors);
		model.addAttribute("plants", plants);

		LOG.info("returing lot creation form along with materials, vendors, plants");

		return AppConstants.INSP_LOT_PAGE;
	}

	@PostMapping("/create/insp/lot")
	public String addInspectionLot(Model model, InspectionLot lot) {

		boolean result = inspectionlotService.createInspectionLot(lot);

		if (result) {

			LOG.info("redirecting to lot creation form");

			model.addAttribute("message", lot.getLotId() + " inspection lot created ");
			return "redirect:/insp/create/lot";
		}
		return AppConstants.HOME_PAGE;
	}
}
