package com.zettamine.mi.responsedto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EditLot {
	
	private String id;
	private String result;
	private String remarks;
	private LocalDate date;
}
