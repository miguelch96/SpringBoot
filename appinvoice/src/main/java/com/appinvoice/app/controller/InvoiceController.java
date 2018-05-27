package com.appinvoice.app.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.appinvoice.app.models.entity.Customer;
import com.appinvoice.app.models.entity.Invoice;
import com.appinvoice.app.models.entity.InvoiceLine;
import com.appinvoice.app.models.entity.Product;
import com.appinvoice.app.service.ICustomerService;
import com.appinvoice.app.service.IInvoiceService;
import com.appinvoice.app.service.IProductService;

@Controller
@RequestMapping("/invoice")
@SessionAttributes("invoice")
@Secured("ROLE_ADMIN")
public class InvoiceController {
	@Autowired
	private IInvoiceService invoiceService;

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private IProductService productService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@GetMapping("/detail/{id}")
	public String detailInvoice(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Invoice invoice = invoiceService.fetchInvoiceByIdWithCustomerWhithItemInvoiceWithProduct(id);

		if (invoice == null) {
			flash.addFlashAttribute("error", "The invoice does not exist in the database!");
			return "redirect:/customer/";
		}

		model.addAttribute("invoice", invoice);
		model.addAttribute("title", "Detail Invoice");
		return "invoice/detail";
	}

	@GetMapping("/new/{customerId}")
	public String newInvoice(@PathVariable(value = "customerId") Long customerId, Model model,
			RedirectAttributes flash) {

		Customer customer = customerService.findById(customerId);

		if (customer == null) {
			flash.addFlashAttribute("error", "The client does not exist in the database");
			return "redirect:/invoice/list";
		}

		Invoice invoice = new Invoice();
		invoice.setCustomer(customer);

		model.addAttribute("invoice", invoice);
		model.addAttribute("title", "Invoice");

		return "invoice/form";
	}

	@GetMapping(value = "/loadproducts/{term}", produces = { "application/json" })
	public @ResponseBody List<Product> loadProducos(@PathVariable String term) {
		return productService.findByName(term);
	}

	@PostMapping("/save")
	public String saveInvoice(@Valid Invoice invoice, BindingResult result, Model model,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "quantity[]", required = false) Integer[] quantity, RedirectAttributes flash,
			SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("title", "New Invoice");
			return "invoice/form";
		}

		if (itemId == null || itemId.length == 0) {
			model.addAttribute("title", "New Factura");
			model.addAttribute("error", "Error: The invoice CAN NOT have lines!");
			return "invoice/form";
		}

		for (int i = 0; i < itemId.length; i++) {
			Product product = productService.findById(itemId[i]);

			InvoiceLine line = new InvoiceLine();
			line.setQuantity(quantity[i]);
			line.setProduct(product);
			invoice.addItemInvoice(line);

			log.info("ID: " + itemId[i].toString() + ", quantity: " + quantity[i].toString());
		}

		invoiceService.save(invoice);
		status.setComplete();

		flash.addFlashAttribute("success", "Invoice created successfully!");

		return "redirect:/customer/detail/" + invoice.getCustomer().getId();
	}
	
	@GetMapping("/delete/{id}")
	public String deleteInvoice(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		Invoice invoice = invoiceService.findById(id);

		if (invoice != null) {
			invoiceService.deleteById(id);
			flash.addFlashAttribute("success", "Invoice successfully eliminated!");
			return "redirect:/customer/detail/" + invoice.getCustomer().getId();
		}
		flash.addFlashAttribute("error", "The invoice does not exist in the database, it could not be deleted!");

		return "redirect:/invoice/list";
	}


}
