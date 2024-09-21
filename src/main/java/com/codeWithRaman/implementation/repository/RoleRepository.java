package com.codeWithRaman.implementation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codeWithRaman.implementation.model.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

	Role findByName(String roleName);

}
