package com.appinvoice.app.service;

import com.appinvoice.app.models.entity.Invoice;

public interface IInvoiceService {

	public void save(Invoice invoice);

	public void deleteById(Long id);

	public Invoice findById(Long id);

	public Invoice fetchInvoiceByIdWithCustomerWhithItemInvoiceWithProduct(Long id);

}
