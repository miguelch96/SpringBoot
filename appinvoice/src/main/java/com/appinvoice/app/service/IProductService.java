package com.appinvoice.app.service;

import java.util.List;

import com.appinvoice.app.models.entity.Product;

public interface IProductService {
	
	public List<Product> findByName(String term);

	public Product findById(Long id);

}
