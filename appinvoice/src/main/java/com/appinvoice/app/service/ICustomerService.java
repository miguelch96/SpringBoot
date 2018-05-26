package com.appinvoice.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.appinvoice.app.models.entity.Customer;

public interface ICustomerService {

	public List<Customer> findAll();

	public void save(Customer customer);

	public Customer findById(Long id);

	public void deleteById(Long id);

	public Customer fetchByIdWithInvoices(Long id);

	public Page<Customer> findAll(Pageable pageable);

}
