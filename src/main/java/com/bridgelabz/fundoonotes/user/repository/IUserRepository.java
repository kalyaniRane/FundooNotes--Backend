package com.bridgelabz.fundoonotes.user.repository;


import com.bridgelabz.fundoonotes.user.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserDetails, Integer> {
    Optional<UserDetails> findByEmailID(String emilID);

    @Query(value = "select * from user_details where is_verified= :verify",nativeQuery = true)
    List<UserDetails> findAllByVerified(@Param("verify") boolean verify);
}
