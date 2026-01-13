package com.example.project.security.token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TokenRepository extends JpaRepository<Token, Integer> {

	  @Query("""
	      select t from Token t 
	      where t.user.id = :id and t.expired = false and t.revoked = false
	      """)
	  List<Token> findAllValidTokenByUser(@Param("id") Integer id);

	  Optional<Token> findByToken(String token);
	}
