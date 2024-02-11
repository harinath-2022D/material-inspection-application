package com.zettamine.mi.services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zettamine.mi.entities.InspectionActuals;
import com.zettamine.mi.entities.InspectionLot;
import com.zettamine.mi.entities.Material;
import com.zettamine.mi.entities.MaterialInspectionCharacteristics;
import com.zettamine.mi.entities.User;
import com.zettamine.mi.repositories.InspectionActualsRepository;
import com.zettamine.mi.repositories.InspectionLotRepository;
import com.zettamine.mi.repositories.UserRepository;
import com.zettamine.mi.requestdtos.DateRangeLotSearch;
import com.zettamine.mi.responsedto.DateRangeLotResponse;
import com.zettamine.mi.responsedto.EditLot;
import com.zettamine.mi.responsedto.LotActualsAndCharacteristics;

import jakarta.servlet.http.HttpSession;

@Service
public class InspectionServiceImpl implements InspectionService {
	private InspectionLotRepository inspectionLotRepo;
	private InspectionActualsRepository inspectionActRepo;
	private UserRepository userRepo;
	
	@Autowired
	private HttpSession session;

	public InspectionServiceImpl(InspectionLotRepository inspectionLotRepo,
			InspectionActualsRepository inspectionActRepo,
			UserRepository userRepo) {
		super();
		this.inspectionLotRepo = inspectionLotRepo;
		this.inspectionActRepo = inspectionActRepo;
		this.userRepo = userRepo;
	}

	@Override
	public InspectionLot getLotDetails(Integer id) {
		Optional<InspectionLot> optInsp = inspectionLotRepo.findById(id);

		if (optInsp.isPresent()) {

			InspectionLot lot = optInsp.get();
			Integer lotActuals = lot.getInspectionActuals().size();
			Integer lotMaterialChar = lot.getMaterial().getMaterialChar().size();

			return lot;
		}
		return null;
	}

	@Override
	public List<LotActualsAndCharacteristics> getActualAndOriginalOfLot(Integer id) {
		InspectionLot lot = getLotDetails(id);
		Material material = lot.getMaterial();
		List<MaterialInspectionCharacteristics> characteristics = material.getMaterialChar();
//		List<InspectionActuals> actuals = inspectionActRepo.
		List<InspectionActuals> actuals = lot.getInspectionActuals();

		List<LotActualsAndCharacteristics> list = new ArrayList<>();

		for (int start = 0; start < characteristics.size(); start++) {
			LotActualsAndCharacteristics lotActOrg = LotActualsAndCharacteristics.builder().sNo(start + 1)
					.characteristicId(characteristics.get(start).getCharacteristicId())
					.characteristicDesc(characteristics.get(start).getCharacteristicDescription())
					.upperToleranceLimit(characteristics.get(start).getUpperToleranceLimit())
					.lowerToleranceLimit(characteristics.get(start).getLowerToleranceLimit())
					.unitOfMeasure(characteristics.get(start).getUnitOfMeasure()).build();
			list.add(lotActOrg);
		}

		for (int start = 0; start < list.size(); start++) {
			Integer charId = list.get(start).getCharacteristicId();
			for (int act = 0; act < actuals.size(); act++) {

				if (actuals.get(act).getMaterialInspectionCharacteristics().getCharacteristicId() == charId) {
					list.get(start).setActualUtl(actuals.get(act).getMaximumMeasurement());
					list.get(start).setActualLtl(actuals.get(act).getMinimumMeasurement());
				}
			}
		}

		return list;
	}

	@Override
	public List<InspectionLot> getAllInspectionLots() {
		List<InspectionLot> lots = inspectionLotRepo.findAll();
		return lots;
	}

	public InspectionLot getInspectionLot(Integer id) {
		Optional<InspectionLot> optInspectionLot = inspectionLotRepo.findById(id);

		if (optInspectionLot.isPresent()) {
			InspectionLot insp = optInspectionLot.get();
//			System.out.println(insp.getLotId());
			return insp;
		}
		return null;
	}

	@Override
	public boolean saveInspActuals(InspectionActuals actuals) {
		InspectionLot lot = getInspectionLot(Integer.valueOf(actuals.getRequiredLot()));
		lot.setResult("INSP");
		actuals.setInspectionLot(lot);

		inspectionActRepo.save(actuals);
		List<MaterialInspectionCharacteristics> totalReqChar = lot.getMaterial().getMaterialChar();
		List<InspectionActuals> actualChar = lot.getInspectionActuals();

//		System.out.println(totalReqChar.size() +" total req char");
//		System.out.println(actualChar.size() +" actual char size");

		boolean result = false;

		if (totalReqChar.size() == actualChar.size()) {
//			System.out.println("inside mark for approvel proecss");
//			System.out.println();

			for (InspectionActuals actual : actualChar) {
				Double actualUpperTolerance = actual.getMaximumMeasurement();
				Double actualLowerTolerance = actual.getMinimumMeasurement();
				for (int i = 0; i < totalReqChar.size(); i++) {
					Double reqUpperTolerance = totalReqChar.get(i).getUpperToleranceLimit();
					Double reqLowerTolerance = totalReqChar.get(i).getLowerToleranceLimit();

//					System.out.println(actualUpperTolerance+ ","+ actualLowerTolerance + " actual value | " + reqUpperTolerance + ", " + reqLowerTolerance);
//					System.out.println(actual.getMaterialInspectionCharacteristics().getCharacteristicId() + " == " + totalReqChar.get(i).getCharacteristicId());
//					System.out.println();

					if (actual.getMaterialInspectionCharacteristics().getCharacteristicId() == totalReqChar.get(i)
							.getCharacteristicId()) {
//						System.out.println("inside comparing");
						if (actualUpperTolerance > reqUpperTolerance || actualUpperTolerance < reqLowerTolerance) {
//							System.out.println(actualUpperTolerance + " actual value abc | " + reqUpperTolerance + ", " + reqLowerTolerance);
							result = true;
						}
						if (actualLowerTolerance < reqLowerTolerance || actualLowerTolerance > reqUpperTolerance) {
//							System.out.println(actualLowerTolerance + " actual value abc | " + reqUpperTolerance + ", " + reqLowerTolerance);
							result = true;
						}
					}
				}
			}
			if (result == false) {
				lot.setResult("MARKED FOR APPROVEL");
//				System.out.println("MARKED FOR APPROVEL");
				inspectionLotRepo.save(lot);
			}
		}

		return true;
	}



	@Override
	public List<DateRangeLotResponse> getAllLotsDetailsBetweenDateRange(DateRangeLotSearch obj) {
		List<InspectionLot> inspList = inspectionLotRepo.findAllBycreationDateBetween(obj.getFromDate(),
				obj.getToDate());
//		List<InspectionLot> responseList = inspList.stream()
//				                                   .filter(lot -> (lot.getMaterial().getMaterialId().equals(obj.getMaterialId()))
//					                            	|| (lot.getPlant().getPlantId().equals(obj.getPlantId()))
//					                               	|| (lot.getResult().equals(obj.getStatus())))
//				                                   .collect(Collectors.toList());
		
		List<Predicate<InspectionLot>> searchCriteriaList = new LinkedList<>();
		
		if (obj.getMaterialId().length() != 0) {
			
			searchCriteriaList.add(lot -> (lot.getMaterial().getMaterialId().equals(obj.getMaterialId())));
		}
		
		if(obj.getPlantId().length() != 0) {
			searchCriteriaList.add(lot -> (lot.getPlant().getPlantId().equals(obj.getPlantId())));
		}
		
		if(obj.getStatus().length() != 0 ) {
			searchCriteriaList.add(lot -> (lot.getResult().equals(obj.getStatus())));
		}
		
		
		Predicate<InspectionLot> searchCriteria =  lot -> true;
		
		for(Predicate predicate : searchCriteriaList) {
			searchCriteria  = searchCriteria.and(predicate);
		}
		
		List<InspectionLot> requiredList = inspList.stream().filter(searchCriteria).collect(Collectors.toList());
		
		List<DateRangeLotResponse> responseList = new LinkedList<>();

		for (InspectionLot lot : requiredList) {
			
			System.out.println(lot.getLotId());
			
			boolean actualsStatus = lot.getMaterial().getMaterialChar().size() == lot.getInspectionActuals().size() ? true : false;
			
			
			
			DateRangeLotResponse respLot = DateRangeLotResponse.builder()
				                                               .createdOn(lot.getCreationDate())
					                                           .endOn(lot.getInspectionEndDate())
					                                           .startOn(lot.getInspectionStartDate())
					                                           .result(lot.getResult())
					                                           .inspectedBy(lot.getUser().getUserId())
					                                           .lotId(lot.getLotId())
					                                           .actualsStatus(actualsStatus)
					                                           .build();
			
			responseList.add(respLot);
		}

		return responseList;
	}

	@Override
	public boolean updateInspectionLot(EditLot lot) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateInspectionLot(EditLot lot, HttpSession session) {
		Integer id = Integer.valueOf(lot.getId());

		Optional<InspectionLot> optInsp = inspectionLotRepo.findById(id);

		if (optInsp.isEmpty()) {
			return false;
		}

		InspectionLot originalLot = optInsp.get();
		
		Integer userid = (Integer) session.getAttribute("userId");
		
		Optional<User> user = userRepo.findById(userid);

		originalLot.setInspectionEndDate(lot.getDate());
		originalLot.setResult(lot.getResult());
		originalLot.setRemarks(lot.getRemarks());
		originalLot.setUser(user.get());

		inspectionLotRepo.save(originalLot);

		return true;
	}

	

	

	

}
