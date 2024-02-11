package com.zettamine.mi.services;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zettamine.mi.entities.Vendor;
import com.zettamine.mi.repositories.VendorRepository;

@Service
public class VendorServiceImpl implements VendorService {

	private VendorRepository vendorRepository;
	
	public VendorServiceImpl(VendorRepository vendorRepository) {
		this.vendorRepository = vendorRepository;
	}
	
	
	@Override
	public boolean addNewVendor(Vendor vendor) {
		if(vendor.getVendorId() == 0) {
		vendor.setStatus("Y");
		Vendor savedVendor = vendorRepository.save(vendor);
		if(savedVendor.getVendorId() > 0) {
			return true;
		}
		return false;
		}else {
			Optional<Vendor> optVendor = vendorRepository.findById(vendor.getVendorId());
			Vendor prevVendor = optVendor.get();
			prevVendor.setEmail(vendor.getEmail());
			prevVendor.setName(vendor.getName());
			vendorRepository.save(prevVendor);
			return true;
		}
		
	}


	@Override
	public List<Vendor> getAllVendor() {
		List<Vendor> vendorsList = vendorRepository.findAllActiveVendors("Y");
		return vendorsList;
	}


	@Override
	public Vendor getVendor(Integer id) {
		Optional<Vendor> vendor = vendorRepository.findById(id);
		if(vendor.isEmpty()) {
			return null;
		}
		return vendor.get();
	}


	@Override
	public boolean deleteVendor(Integer id) {
		
		Optional<Vendor> optVendor = vendorRepository.findById(id);
		if(optVendor.isEmpty()) {
			return false;
		}
		Vendor vendor = optVendor.get();
		vendor.setStatus("N");
		vendorRepository.save(vendor);
		return true;
	}

}
