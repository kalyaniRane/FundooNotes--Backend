package com.bridgelabz.fundoonotes.label.repository;

import com.bridgelabz.fundoonotes.label.model.LabelDetails;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ILabelRepository extends JpaRepository<LabelDetails,Integer> {
    List<LabelDetails> findAllByUser(UserDetails userDetails);
}
