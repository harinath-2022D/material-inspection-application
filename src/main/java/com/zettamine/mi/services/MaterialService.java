package com.zettamine.mi.services;

import java.util.List;

import com.zettamine.mi.entities.InspectionActuals;
import com.zettamine.mi.entities.InspectionLot;
import com.zettamine.mi.entities.Material;
import com.zettamine.mi.entities.MaterialInspectionCharacteristics;
import com.zettamine.mi.entities.Plant;
import com.zettamine.mi.entities.Vendor;

public interface MaterialService {

	List<Material> getAllMaterials();

	Material getMaterial(String id);

	boolean deleteMaterial(String id);

	boolean addNewMaterial(Material material);

	boolean addNewMaterialCharacteristic(MaterialInspectionCharacteristics matChar);

	List<InspectionLot> getAllInspectionLots();

	boolean createInspectionLot(InspectionLot lot);

	List<Vendor> getAllVendors();

	List<Plant> getAllPlants();

	List<MaterialInspectionCharacteristics> getMaterialCharByLotId(String id);

	InspectionLot getInspectionLot(Integer id);

	boolean saveInspActuals(InspectionActuals actuals);

}
