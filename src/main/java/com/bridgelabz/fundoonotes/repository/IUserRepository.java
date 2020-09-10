package com.bridgelabz.fundoonotes.repository;


import com.bridgelabz.fundoonotes.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserDetails, Integer> {
    Optional<UserDetails> findByEmailID(String emilID);
}
