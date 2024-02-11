package com.zettamine.mi.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zettamine.mi.entities.InspectionActuals;
import com.zettamine.mi.entities.InspectionLot;
import com.zettamine.mi.entities.Material;
import com.zettamine.mi.entities.MaterialInspectionCharacteristics;
import com.zettamine.mi.entities.Plant;
import com.zettamine.mi.entities.Vendor;
import com.zettamine.mi.repositories.InspectionActualsRepository;
import com.zettamine.mi.repositories.InspectionLotRepository;
import com.zettamine.mi.repositories.MaterialCharRepository;
import com.zettamine.mi.repositories.MaterialRepository;
import com.zettamine.mi.repositories.PlantRepository;
import com.zettamine.mi.repositories.VendorRepository;

@Service
public class MaterialServiceIImpl implements MaterialService {

	private MaterialRepository materialRepository;

	private MaterialCharRepository materialCharReposotory;

	private InspectionLotRepository inspectionLotRepo;

	private VendorRepository vendorRepository;

	private PlantRepository plantRepository;

	private InspectionActualsRepository inspActRepo;

	public MaterialServiceIImpl(MaterialRepository materialRepository, MaterialCharRepository materialCharReposotory,
			InspectionLotRepository inspectionLotRepo, VendorRepository vendorRepository,
			PlantRepository plantRepository, InspectionActualsRepository inspActRepo) {
		super();
		this.materialRepository = materialRepository;

		this.materialCharReposotory = materialCharReposotory;

		this.inspectionLotRepo = inspectionLotRepo;

		this.vendorRepository = vendorRepository;

		this.plantRepository = plantRepository;

		this.inspActRepo = inspActRepo;
	}

	@Override
	public List<Material> getAllMaterials() {

		List<Material> materialList = materialRepository.findAll();

		return materialList;
	}

	@Override
	public Material getMaterial(String id) {

		Optional<Material> optMaterial = materialRepository.findById(id);

		if (optMaterial.isEmpty()) {
			return null;
		}

		return optMaterial.get();
	}

	@Override
	public boolean deleteMaterial(String id) {

		Optional<Material> optMaterial = materialRepository.findById(id);

		if (optMaterial.isEmpty()) {
			return false;
		}

		Material material = optMaterial.get();

		material.setStatus("N");

		materialRepository.save(material);

		return true;
	}

	@Override
	public boolean addNewMaterial(Material material) {

		material.setStatus("Y");

		Material savedMaterial = materialRepository.save(material);

		if (savedMaterial.getMaterialId() == null) {
			return false;
		}

		return true;
	}

	@Override
	public boolean addNewMaterialCharacteristic(MaterialInspectionCharacteristics matChar) {
		System.out.println(matChar);

		MaterialInspectionCharacteristics savedCharacteristic = materialCharReposotory.save(matChar);

		Optional<Material> material = materialRepository.findById(matChar.getMaterial());

		if (material.isPresent()) {

			Material mat = material.get();

			for (int i = 0; i < mat.getMaterialChar().size(); i++) {
				System.out.println(mat.getMaterialChar().get(i));
			}

		} else {
			System.out.println("material is null ------------------------->");
		}

		if (savedCharacteristic.getCharacteristicId() > 0) {
			return true;
		}

		return false;
	}

	@Override
	public List<InspectionLot> getAllInspectionLots() {

		List<InspectionLot> lots = inspectionLotRepo.findAll();

		List<InspectionLot> responseList = new LinkedList<>();

		for (InspectionLot lot : lots) {

			if (lot.getMaterial().getMaterialChar().size() != lot.getInspectionActuals().size()) {
				responseList.add(lot);
			}
		}
		return responseList;
//		return lots;
	}

	@Override
	public boolean createInspectionLot(InspectionLot lot) {
		
		lot.setResult("INSP");
		InspectionLot savedLot = inspectionLotRepo.save(lot);

		if (savedLot.getLotId() > 0) {
			return true;
		}

		return false;
	}

	@Override
	public List<Vendor> getAllVendors() {

		List<Vendor> vendorList = vendorRepository.findAll();

		return vendorList;
	}

	@Override
	public List<Plant> getAllPlants() {

		List<Plant> plantList = plantRepository.findAll();

		return plantList;
	}

	@Override
	public List<MaterialInspectionCharacteristics> getMaterialCharByLotId(String id) {

		Optional<InspectionLot> optLot = inspectionLotRepo.findById(id);

		if (optLot.isEmpty()) {
			return null;

		} else {

			Material material = optLot.get().getMaterial();

			List<Integer> inspActualsList = optLot.get().getInspectionActuals().stream()
					.map(inspAct -> inspAct.getMaterialInspectionCharacteristics().getCharacteristicId())
					.collect(Collectors.toList());

			List<MaterialInspectionCharacteristics> charList = material.getMaterialChar();

			List<MaterialInspectionCharacteristics> repsonseList = new LinkedList<>();

			for (MaterialInspectionCharacteristics item : charList) {
				if (inspActualsList.contains(item.getCharacteristicId())) {

				} else {
					repsonseList.add(item);
				}
			}
			return repsonseList;
		}
//		Optional<Material> optMaterial = materialRepository.findById(optLot.get().getMaterial().getMaterialId());
//		if(optMaterial.isEmpty()) {
//			return null;
//		}
//		List<MaterialInspectionCharacteristics> charList = optMaterial.get().getMaterialChar();
//		
////		List<Integer> charIds = charList.stream().map(charid -> charid.getCharacteristicId()).collect(Collectors.toList());
//		return charList;
	}

	@Override
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

		actuals.setInspectionLot(lot);

		inspActRepo.save(actuals);

		return true;
	}

}
