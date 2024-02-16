package com.zettamine.mi.responsedto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DateRangeLotResponse {
	
	private Integer lotId;
	
	private LocalDate createdOn;
	
	private LocalDate startOn;
	
	private LocalDate endOn;
	
	private String result;
	
	private Integer inspectedBy;
	
	private Boolean actualsStatus;
	
	private String material;
}
