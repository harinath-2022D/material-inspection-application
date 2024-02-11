package com.zettamine.mi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zettamine.mi.entities.Vendor;
import com.zettamine.mi.services.VendorService;

@Controller
@RequestMapping("/vendor")
public class VendorController {

	private VendorService vendorService;

	public VendorController(VendorService vendorService) {
		this.vendorService = vendorService;
	}

	@GetMapping("/addvendor")
	public String loadAddVendorForm(Model model) {
		model.addAttribute("vendor", new Vendor());
		return "vendor";
	}

	@PostMapping("/sumbitVendor")
	public String addVendor(Model model, Vendor vendor) {
		System.out.println(vendor);
		boolean result = vendorService.addNewVendor(vendor);
		if (result) {
			model.addAttribute("message", vendor.getName() + " vendor saved");
			return "redirect:/vendor/addvendor";
		}
		return "home";
	}

	@GetMapping("/viewVendors")
	public String getAllVendors(Model model) {
		List<Vendor> vendorsList = vendorService.getAllVendor();
		model.addAttribute("vendorsList", vendorsList);
		return "vendor-list";
	}

	@GetMapping("/editVendor")
	public String editVendor(@RequestParam("id") Integer id, Model model) {
		System.out.println("edit vendor id : " + id);
		Vendor vendor = vendorService.getVendor(id);
		model.addAttribute("vendor", vendor);
		return "vendor";
	}

	@GetMapping("/deleteVendor")
	public String deleteVendor(@RequestParam("id") Integer id, Model model) {
		System.out.println("delete vendor id : " + id);
		boolean result = vendorService.deleteVendor(id);
		if (result) {
			return "redirect:/vendor/viewVendors";
		}
		return "home";
	}

}
