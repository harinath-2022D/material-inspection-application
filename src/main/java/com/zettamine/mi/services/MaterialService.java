package com.zettamine.mi.services;

import java.util.List;

import com.zettamine.mi.entities.InspectionActuals;
import com.zettamine.mi.entities.InspectionLot;
import com.zettamine.mi.entities.Material;
import com.zettamine.mi.entities.MaterialInspectionCharacteristics;
import com.zettamine.mi.entities.Plant;
import com.zettamine.mi.entities.Vendor;
import com.zettamine.mi.responsedto.LotActualsAndCharacteristics;

public interface MaterialService {

	List<Material> getAllMaterials();

	Material getMaterial(String id);

	boolean deleteMaterial(String id);

	boolean addNewMaterial(Material material);

	boolean addNewMaterialCharacteristic(MaterialInspectionCharacteristics matChar);

	List<InspectionLot> getAllInspectionLots();
	
	List<MaterialInspectionCharacteristics> getMaterialCharByLotId(String id);
//
//	boolean createInspectionLot(InspectionLot lot);
//
//	List<Vendor> getAllVendors();
//
//	List<Plant> getAllPlants();
//
//	InspectionLot getInspectionLot(Integer id);
//
//	boolean saveInspActuals(InspectionActuals actuals);

	List<MaterialInspectionCharacteristics> getAllCharacteristicsOfMaterial(String id);

	boolean saveEditMaterial(Material material);

}
