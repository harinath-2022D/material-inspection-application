package com.zettamine.mi.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@Data
public class Vendor {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ven_seq")
	@SequenceGenerator(name = "ven_seq", sequenceName = "ven_sequence", initialValue = 5001, allocationSize = 1)
	private int vendorId;
	
	private String name;
	
	private String status;
	
	private String email;
	
	@OneToMany(mappedBy="vendor", cascade = CascadeType.ALL)
	private List<InspectionLot> inspectionLot;
}
