package com.appinvoice.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.appinvoice.app.models.entity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	//TODO
	@Query("select p from Product p where p.name like %?1%")
	public List<Product> findByName(String term);
	
	public List<Product> findByNameLikeIgnoreCase(String term);
}
