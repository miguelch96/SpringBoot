package com.appinvoice.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appinvoice.app.models.dao.InvoiceRepository;
import com.appinvoice.app.models.entity.Invoice;

@Service
public class InvoiceService implements IInvoiceService{

	@Autowired
	private InvoiceRepository invoiceRepository;
	
	
	@Override
	@Transactional
	public void save(Invoice invoice) {
		invoiceRepository.save(invoice);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		invoiceRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Invoice findById(Long id) {
		return invoiceRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly=true)
	public Invoice fetchInvoiceByIdWithCustomerWhithItemInvoiceWithProduct(Long id) {
		return invoiceRepository.fetchByIdWithCustomerWhithItemInvoiceWithProduct(id);
	}

}
