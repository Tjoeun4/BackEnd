package com.example.project.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.member.domain.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer>{

	public Optional<Users> findByEmail(String username);

	public Optional<Users> findById(Long memberId);

}
