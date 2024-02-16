package com.zettamine.mi.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mat_isp_ch")
@Getter
@Setter
public class MaterialInspectionCharacteristics {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chare_seq")
	@SequenceGenerator(name = "chare_seq", sequenceName = "chare_sequence", initialValue = 101, allocationSize = 1)
	@Column(name = "ch_id")
	private Integer characteristicId;
	
	@OneToMany(mappedBy="materialInspectionCharacteristics", cascade = CascadeType.ALL)
	private List<InspectionActuals> inspectionActuals = new ArrayList<>();
	
	@Column(name = "ch_desc")
	private String characteristicDescription;
	
	@Column(name = "tol_ul")
	private Double upperToleranceLimit;
	
	@Column(name = "tol_ll")
	private Double lowerToleranceLimit;
	
	@Column(name = "uom")
	private String unitOfMeasure;
	
//	@Column(name = "mat_id")
//	private String material;
	
	@ManyToOne
	private Material material;

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		MaterialInspectionCharacteristics other = (MaterialInspectionCharacteristics) obj;
//		return Objects.equals(characteristicDescription, other.characteristicDescription)
//				&& Objects.equals(characteristicId, other.characteristicId)
//				&& Objects.equals(inspectionActuals, other.inspectionActuals)
//				&& Objects.equals(lowerToleranceLimit, other.lowerToleranceLimit)
//				&& Objects.equals(material, other.material) && Objects.equals(unitOfMeasure, other.unitOfMeasure)
//				&& Objects.equals(upperToleranceLimit, other.upperToleranceLimit);
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(characteristicDescription, characteristicId, inspectionActuals, lowerToleranceLimit,
//				material, unitOfMeasure, upperToleranceLimit);
//	}
}
