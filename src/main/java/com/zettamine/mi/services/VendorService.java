package com.zettamine.mi.services;

import java.util.List;

import com.zettamine.mi.entities.Vendor;

public interface VendorService {
	boolean addNewVendor(Vendor vendor);
	
	List<Vendor> getAllVendor();

	Vendor getVendor(Integer id);

	boolean deleteVendor(Integer id);
}
