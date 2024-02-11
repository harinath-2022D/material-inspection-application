package com.zettamine.mi.services;

import java.util.List;

import com.zettamine.mi.entities.InspectionActuals;
import com.zettamine.mi.entities.InspectionLot;
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
	boolean updateInspectionLot(EditLot lot);
	List<DateRangeLotResponse> getAllLotsDetailsBetweenDateRange(DateRangeLotSearch obj);
	boolean updateInspectionLot(EditLot lot, HttpSession session);
	
}
