package com.appinvoice.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appinvoice.app.models.entity.Users;


@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
	public Users findByUsername(String username);
}
