package com.zettamine.mi.services;

import java.util.List;

import com.zettamine.mi.entities.InspectionActuals;
import com.zettamine.mi.entities.InspectionLot;
import com.zettamine.mi.entities.Material;
import com.zettamine.mi.entities.Plant;
import com.zettamine.mi.entities.Vendor;
import com.zettamine.mi.requestdtos.DateRangeLotSearch;
import com.zettamine.mi.responsedto.DateRangeLotResponse;
import com.zettamine.mi.responsedto.EditLot;
import com.zettamine.mi.responsedto.LotActualsAndCharacteristics;

import jakarta.servlet.http.HttpSession;

public interface InspectionService {
	InspectionLot getLotDetails(Integer lot);
	
	List<LotActualsAndCharacteristics> getActualAndOriginalOfLot(Integer id);
	
	List<InspectionLot> getAllInspectionLots();
	
	boolean saveInspActuals(InspectionActuals actuals);
	
	
	
	List<DateRangeLotResponse> getAllLotsDetailsBetweenDateRange(DateRangeLotSearch obj);
	
	boolean updateInspectionLot(EditLot lot, HttpSession session);
	
	boolean createInspectionLot(InspectionLot lot);

	List<Vendor> getAllVendors();

	List<Plant> getAllPlants();
	
	List<Material> getAllMaterials();
	
	
	
}
