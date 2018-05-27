package com.appinvoice.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.appinvoice.app.models.entity.Customer;
import com.appinvoice.app.service.ICustomerService;
import com.appinvoice.app.util.PageRender;


@Controller
@SessionAttributes("customer")
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private ICustomerService customerService;

	@GetMapping("/")
	public String listCustomer(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 8);

		Page<Customer> customers = customerService.findAll(pageRequest);

		PageRender<Customer> pageRender = new PageRender<Customer>("/customer/", customers);

		model.addAttribute("title", "List of customers");

		model.addAttribute("customers", customers);
		model.addAttribute("page", pageRender);

		return "list";
	}

	@GetMapping(value = "/new")
	public String newCustomer(Model model) {

		Customer customer = new Customer();
		model.addAttribute("customer", customer);
		model.addAttribute("title", "New Customer");
		return "form";
	}

	@PostMapping(value = "/save")
	public String saveCustomer(@Valid Customer customer, BindingResult result, Model model, RedirectAttributes flash,
			SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("title", "Save Customer");
			return "form";
		}

		String mensajeFlash = (customer.getId() != null) ? "Successfully edited client!"
				: "Customer created successfully!";

		customerService.save(customer);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/customer/";
	}

	@GetMapping(value = "/edit/{id}")
	public String editCustomer(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Customer customer = null;

		if (id > 0) {
			customer = customerService.findById(id);
			if (customer == null) {
				flash.addFlashAttribute("error", "The customer ID does not exist in the database!");
				return "redirect:/customer/";
			}
		} else {
			flash.addFlashAttribute("error", "The customer ID can not be zero!");
			return "redirect:/customer/";
		}
		model.addAttribute("customer", customer);
		model.addAttribute("title", "Edit Customer");
		return "form";
	}

	@GetMapping(value = "/delete/{id}")
	public String deleteCustomer(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {

			customerService.deleteById(id);
			flash.addFlashAttribute("success", "Customer removed successfully!");
		}
		return "redirect:/customer/";
	}

	
	@GetMapping(value = "/detail/{id}")
	public String detailsCustomer(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Customer customer = customerService.findById(id);
		if (customer == null) {
			flash.addFlashAttribute("error", "The client does not exist in the database");
			return "redirect:/customer/";
		}

		model.addAttribute("customer", customer);
		model.addAttribute("title", "Details Customer: " + customer.getName());
		return "detail";
	}
}
