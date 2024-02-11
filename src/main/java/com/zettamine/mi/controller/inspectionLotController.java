package com.zettamine.mi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zettamine.mi.entities.InspectionActuals;
import com.zettamine.mi.entities.InspectionLot;
import com.zettamine.mi.requestdtos.DateRangeLotSearch;
import com.zettamine.mi.requestdtos.Search;
import com.zettamine.mi.responsedto.DateRangeLotResponse;
import com.zettamine.mi.responsedto.EditLot;
import com.zettamine.mi.responsedto.LotActualsAndCharacteristics;
import com.zettamine.mi.services.InspectionServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/insp")
public class inspectionLotController {
	private InspectionServiceImpl inspectionlotService;
	
	public inspectionLotController(InspectionServiceImpl inspectionlotService) {
		super();
		this.inspectionlotService = inspectionlotService;
	}

	@GetMapping("/lot")
	public String loadInspectionLotDetails(Model model) {
		model.addAttribute("search", new Search());
		return "insp-lot-details";
	}
	
	@PostMapping("/lot/search")
	public String getInspectinLotDetails(Model model, Search search) {
		System.out.println("inspection lot post method");
		System.out.println(search);
		return "redirect:/insp/lot/data?id="+search.getLot();
	}
	
	@GetMapping("/lot/data")
	public String fetchInspectionLotDetails(@RequestParam Integer id, Model model) {
		System.out.println("lot ID ------>" + id);
		model.addAttribute("search", new Search(id));
		InspectionLot inspLot = inspectionlotService.getLotDetails(id);
//		System.out.println(inspLot.getLotId()+"------------------>");
		model.addAttribute("lot", inspLot);
		return "insp-lot-details";
	}
	
	@GetMapping("/lot/search/info")
	public String getActualAndOriginalOfLot(@RequestParam Integer id, Model model) {
		System.out.println(id);
		List<LotActualsAndCharacteristics> list = inspectionlotService.getActualAndOriginalOfLot(id);
		model.addAttribute("list", list);
		return "insp-actu-char";
	}
	
	@GetMapping("/add/actu")
	public String addMaterialActuals( Model model) {
		
			model.addAttribute("actuals", new InspectionActuals());
//		List<MaterialInspectionCharacteristics> characteristics = materialService.getAllMaterials();
			List<InspectionLot> lots = inspectionlotService.getAllInspectionLots();
//		model.addAttribute("characteristics", characteristics);
			model.addAttribute("lots", lots);
			model.addAttribute("selectedLotId", "select lot");
			return "material-actu";
		
	}
	
	@PostMapping("/save/insp/actu")
	public String addInspectionActuals(Model model, InspectionActuals actuals) {
//		System.out.println(actuals);
		Integer lotId = Integer.valueOf(actuals.getRequiredLot());
//		InspectionLot lotid = materialService.getInspectionLot(lotId);
//		System.out.println(lotid.getLotId());
		System.out.println("inspection lot id :" + lotId);
		boolean response = inspectionlotService.saveInspActuals(actuals);
		if(response) {
			return "redirect:/material/add/actu";
		}
		return "home";
	}
	
	@GetMapping("/lot/edit/info")
	public String editInspectionLotDetails(@RequestParam Integer id, Model model) {
		InspectionLot inspLot = inspectionlotService.getLotDetails(id);
		model.addAttribute("lot", inspLot);
		model.addAttribute("editLot", new EditLot());
		return "insp-edit";
	}
	
	@PostMapping("/save/edit/lot")
	public String saveEditedLot(Model model,  EditLot lot, HttpSession session) {
		
		System.out.println(lot.getRemarks());
		System.out.println(lot.getResult());
		System.out.println(lot.getDate());
		System.out.println(lot.getId());
		
		boolean result = inspectionlotService.updateInspectionLot(lot, session);
		Integer id = Integer.valueOf(lot.getId());
		
		return "redirect:/insp/lot/data?id="+id;
	}
	
	@GetMapping("/lot/date")
	public String getDateSearchForm(Model  model) {
		model.addAttribute("searchObj",new DateRangeLotSearch());
		return "date-range-search";
	}
	
	@GetMapping("/lot/date/seatch")
	public String DateRangeLotSearch(Model model, DateRangeLotSearch obj) {
		System.out.println(obj.getMaterialId());
		System.out.println(obj.getFromDate());
		System.out.println(obj.getToDate());
		System.out.println(obj.getPlantId());
		System.out.println(obj.getVendorId());
		System.out.println(obj.getStatus());
		
		List<DateRangeLotResponse> list = inspectionlotService.getAllLotsDetailsBetweenDateRange(obj);
		model.addAttribute("list", list);
		return "insp-lot-search";
	}
}
