package com.zettamine.mi.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zettamine.mi.entities.InspectionLot;
import com.zettamine.mi.entities.Material;
import com.zettamine.mi.entities.MaterialInspectionCharacteristics;
import com.zettamine.mi.repositories.InspectionLotRepository;
import com.zettamine.mi.repositories.MaterialCharRepository;
import com.zettamine.mi.repositories.MaterialRepository;
import com.zettamine.mi.responsedto.LotActualsAndCharacteristics;
import com.zettamine.mi.utils.StringUtil;

@Service
public class MaterialServiceIImpl implements MaterialService {

	private MaterialRepository materialRepository;

	private MaterialCharRepository materialCharReposotory;

	private InspectionLotRepository inspectionLotRepo;

	private Logger LOG = LoggerFactory.getLogger(MaterialServiceIImpl.class);

	public MaterialServiceIImpl(MaterialRepository materialRepository, MaterialCharRepository materialCharReposotory,
			InspectionLotRepository inspectionLotRepo) {
		super();
		this.materialRepository = materialRepository;

		this.materialCharReposotory = materialCharReposotory;

		this.inspectionLotRepo = inspectionLotRepo;

	}

	@Override
	public List<Material> getAllMaterials() {

		LOG.info("finding all materials");

		List<Material> materialList = materialRepository.findAll();

		LOG.info("returing all materials list");

		return materialList;
	}

	@Override
	public Material getMaterial(String id) {

		LOG.info("finding material with id : {}", id);

		Optional<Material> optMaterial = materialRepository.findById(id);

		if (optMaterial.isEmpty()) {

			LOG.info("no material associated with id : {}", id);

			return null;
		}

		LOG.info("returing material with id : {}", id);

		return optMaterial.get();
	}

	@Override
	public boolean deleteMaterial(String id) {

		LOG.info("finding material with id : {}", id);

		Optional<Material> optMaterial = materialRepository.findById(id);

		if (optMaterial.isEmpty()) {

			LOG.info("no material associated with id : {}", id);

			return false;
		}

		Material material = optMaterial.get();

		LOG.info("setting material status to INACTIVE");

		material.setStatus("N");

		LOG.info("saving material of id : {}", id);

		materialRepository.save(material);

		LOG.info("returning true");

		return true;
	}

	@Override
	public boolean addNewMaterial(Material material) {

		Optional<Material> optMaterial = materialRepository.findById(material.getMaterialId());
		Optional<Material> optMaterialDesc = materialRepository.findByMaterialDesc(material.getMaterialDesc());

		if (optMaterial.isPresent() || optMaterialDesc.isPresent()) {
			return false;
		} else {

			material.setStatus("Y");

			material.setMaterialId(StringUtil.removeAllSpaces(material.getMaterialId()));

			material.setMaterialDesc(StringUtil.removeExtraSpaces(material.getMaterialDesc()).toUpperCase());

			material.setType(StringUtil.removeExtraSpaces(material.getType().toUpperCase()));

			Material savedMaterial = materialRepository.save(material);

			if (savedMaterial.getMaterialId() == null) {
				return false;
			}

			LOG.info("new material saved with id : {}", savedMaterial.getMaterialId());

			return true;
		}
	}

	@Override
	public boolean addNewMaterialCharacteristic(MaterialInspectionCharacteristics matChar) {

		if (Double.valueOf(matChar.getLowerToleranceLimit()) > Double.valueOf(matChar.getUpperToleranceLimit())) {
			return false;
		}

		matChar.setCharacteristicDescription(
				StringUtil.removeExtraSpaces(matChar.getCharacteristicDescription()).toUpperCase());

		Material material = matChar.getMaterial();

		for (MaterialInspectionCharacteristics matCharItem : material.getMaterialChar()) {

			if (matCharItem.getCharacteristicDescription().equals(matChar.getCharacteristicDescription())) {
				return false;
			}

		}

		LOG.info("new Material characteristic adding {}", matChar);

		MaterialInspectionCharacteristics savedCharacteristic = materialCharReposotory.save(matChar);

		if (savedCharacteristic.getCharacteristicId() > 0) {

			return true;
		}

		return false;
	}

	@Override
	public List<InspectionLot> getAllInspectionLots() {

		LOG.info("getting all lots");

		List<InspectionLot> lots = inspectionLotRepo.findAll();

		List<InspectionLot> responseList = new LinkedList<>();

		for (InspectionLot lot : lots) {

			if (lot.getMaterial().getMaterialChar().size() != lot.getInspectionActuals().size()) {

				LOG.info("adding lots those have not done all inspection actuals");

				responseList.add(lot);
			}
		}

		LOG.info("returing response list");

		return responseList;
//		return lots;
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

					LOG.info("getting all material characteristics of lot {}", id);

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
	public List<MaterialInspectionCharacteristics> getAllCharacteristicsOfMaterial(String id) {
		Material material = getMaterial(id);

		List<MaterialInspectionCharacteristics> list = new LinkedList<>();

		if (material != null) {

			list = material.getMaterialChar();
		}
		return list;
	}

	@Override
	public boolean saveEditMaterial(Material material) {
		material.setStatus("Y");

		material.setMaterialId(StringUtil.removeAllSpaces(material.getMaterialId()));

		material.setMaterialDesc(StringUtil.removeExtraSpaces(material.getMaterialDesc()).toUpperCase());

		material.setType(StringUtil.removeExtraSpaces(material.getType().toUpperCase()));

		Material savedMaterial = materialRepository.save(material);

		if (savedMaterial.getMaterialId() == null) {
			return false;
		}

		LOG.info("material updation saved with id : {}", savedMaterial.getMaterialId());

		return true;
	}

//	@Override
//	public InspectionLot getInspectionLot(Integer id) {
//
//		Optional<InspectionLot> optInspectionLot = inspectionLotRepo.findById(id);
//
//		if (optInspectionLot.isPresent()) {
//			InspectionLot insp = optInspectionLot.get();
////			System.out.println(insp.getLotId());
//			
//			LOG.info("finding inspection lot with id {}", id);
//			
//			return insp;
//		}
//		
//		LOG.info("no lot associated with id {}", id);
//		
//		return null;
//	}

//	@Override
//	public boolean saveInspActuals(InspectionActuals actuals) {
//
//		InspectionLot lot = getInspectionLot(Integer.valueOf(actuals.getRequiredLot()));
//
//		actuals.setInspectionLot(lot);
//
//		inspActRepo.save(actuals);
//
//		return true;
//	}

//	@Override
//	public boolean createInspectionLot(InspectionLot lot) {
//		
//		lot.setResult("INSP");
//		InspectionLot savedLot = inspectionLotRepo.save(lot);
//
//		if (savedLot.getLotId() > 0) {
//			
//			LOG.info("new lot Created with id : {}" , savedLot.getLotId());
//			
//			return true;
//		}
//		
//		LOG.info("lot creation failed");
//		
//		return false;
//	}

//	@Override
//	public List<Vendor> getAllVendors() {
//		
//		LOG.info("getting all vendors");
//		
//		List<Vendor> vendorList = vendorRepository.findAll();
//
//		return vendorList;
//	}

//	@Override
//	public List<Plant> getAllPlants() {
//		
//		LOG.info("getting all plants");
//		
//		List<Plant> plantList = plantRepository.findAll();
//
//		return plantList;
//	}

}
