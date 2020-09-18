package com.bridgelabz.fundoonotes.label.repository;

import com.bridgelabz.fundoonotes.label.model.LabelDetails;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ILabelRepository extends JpaRepository<LabelDetails,Integer> {

}
