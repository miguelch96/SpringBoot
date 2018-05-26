package com.appinvoice.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.appinvoice.app.models.entity.Customer;


@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

	@Query("select c from Customer c left join fetch c.invoices i where c.id=?1")
	public Customer fetchByIdWithInvoices(Long id);
}
