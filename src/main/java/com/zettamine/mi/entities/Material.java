package com.zettamine.mi.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"materialId", "materialDesc"}))
public class Material {
	
	@Id
	@Column(name = "mat_id")
	private String materialId;
	
	@Column(name = "mat_desc", unique = true)
	private String materialDesc;
	
	private String type;
	
	private String status;
	
	@OneToMany(mappedBy = "material", cascade = CascadeType.ALL)
	private List<MaterialInspectionCharacteristics> materialChar = new ArrayList<>();
	
	@OneToMany(mappedBy="material", cascade = CascadeType.ALL)
	private List<InspectionLot> inspectionLot;
}
