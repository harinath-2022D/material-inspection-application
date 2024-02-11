package com.zettamine.mi.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InspectionLot {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lot_seq")
	@SequenceGenerator(name = "lot_seq", sequenceName = "lot_sequence", initialValue = 4000, allocationSize = 1)
	private Integer lotId;
	
	@OneToMany(mappedBy="inspectionLot", cascade = CascadeType.ALL)
	private List<InspectionActuals> inspectionActuals = new ArrayList<>();
	
	@ManyToOne
	private Material material;
	
	@ManyToOne
	private Vendor vendor;
	
	@ManyToOne
	private Plant plant;
	
	
	private LocalDate creationDate;
	
	private LocalDate inspectionStartDate;
	
	private LocalDate inspectionEndDate;
	
	private String result;
	
	private String remarks;
	
	@ManyToOne
	private User user;

	public InspectionLot(Integer id) {
		this.lotId = id;
	}

	@Override
	public String toString() {
		return "InspectionLot [lotId=" + lotId + ", material=" + material + ", vendor=" + vendor + ", plant=" + plant
				+ ", creationDate=" + creationDate + "]";
	}

	public InspectionLot() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
