package com.bridgelabz.fundoonotes.user.repository;


import com.bridgelabz.fundoonotes.user.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserDetails, Integer> {
    Optional<UserDetails> findByEmailID(String emilID);
}