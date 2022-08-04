package com.api.authorization.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.authorization.entity.Users;

@Repository
public interface UsersDAO extends JpaRepository<Users, String> {
	//@Query(value = "select u from Users u where u.createdByName like %:userName% ")
	@Query(value = "select * from users where LOWER(first_name || last_name)  like %:userName%",nativeQuery = true)
	List<Users> findByUserNameRegex(String userName);

}
