package com.zettamine.mi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zettamine.mi.entities.Vendor;
import com.zettamine.mi.services.VendorService;
import com.zettamine.mi.utils.AppConstants;

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
		return AppConstants.VENDOR_PAGE;
	}

	@PostMapping("/sumbitVendor")
	public String addVendor(Model model, Vendor vendor, RedirectAttributes redirectAttributes) {
		System.out.println(vendor);
		boolean result = vendorService.addNewVendor(vendor);
		if (result) {
			redirectAttributes.addFlashAttribute("message", vendor.getName() + " vendor saved");
			return "redirect:/vendor/addvendor";
		}else {
			redirectAttributes.addFlashAttribute("error", vendor.getName() + " vendor is already available");
			return "redirect:/vendor/addvendor";
		}
		
	}

	@GetMapping("/viewVendors")
	public String getAllVendors(Model model) {
		List<Vendor> vendorsList = vendorService.getAllActiveVendor();
		model.addAttribute("vendorsList", vendorsList);
		return AppConstants.VENDORLIST_PAGE;
	}

	@GetMapping("/editVendor")
	public String editVendor(@RequestParam("id") Integer id, Model model) {
		System.out.println("edit vendor id : " + id);
		Vendor vendor = vendorService.getVendor(id);
		model.addAttribute("vendor", vendor);
		return AppConstants.VENDOR_PAGE;
	}

	@GetMapping("/deleteVendor")
	public String deleteVendor(@RequestParam("id") Integer id, Model model) {
		System.out.println("delete vendor id : " + id);
		boolean result = vendorService.deleteVendor(id);
		if (result) {
			return "redirect:/vendor/viewVendors";
		}
		return AppConstants.HOME_PAGE;
	}

}
