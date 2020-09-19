package com.bridgelabz.fundoonotes.label.repository;

import com.bridgelabz.fundoonotes.label.model.LabelDetails;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


public interface ILabelRepository extends JpaRepository<LabelDetails,Integer> {
    List<LabelDetails> findAllByUser(UserDetails userDetails);

    Optional<LabelDetails> findByLabelName(String labelName);
}
