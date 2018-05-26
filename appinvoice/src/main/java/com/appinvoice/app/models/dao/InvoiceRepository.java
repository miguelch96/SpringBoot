package com.appinvoice.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.appinvoice.app.models.entity.Invoice;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	
	@Query("select i from Invoice i join fetch i.customer c join fetch i.items l join fetch l.product where i.id=?1")
	public Invoice fetchByIdWithCustomerWhithItemInvoiceWithProduct(Long id);
}
