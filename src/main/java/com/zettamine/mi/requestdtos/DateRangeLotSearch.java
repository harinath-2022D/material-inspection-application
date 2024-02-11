package com.zettamine.mi.requestdtos;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DateRangeLotSearch {
	
	private LocalDate fromDate;
	
	private LocalDate toDate;
	
	private String materialId;
	
	private String vendorId;
	
	private String plantId;
	
	private String status;
}
