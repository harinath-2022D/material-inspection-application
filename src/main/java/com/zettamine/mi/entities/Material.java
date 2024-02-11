package com.zettamine.mi.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Material {
	
	@Id
	@Column(name = "mat_id")
	private String materialId;
	
	@Column(name = "mat_desc")
	private String materialDesc;
	
	private String type;
	
	private String status;
	
	@OneToMany(mappedBy = "material", cascade = CascadeType.ALL)
	private List<MaterialInspectionCharacteristics> materialChar = new ArrayList<>();
	
	@OneToMany(mappedBy="material", cascade = CascadeType.ALL)
	private List<InspectionLot> inspectionLot;
}
