package com.bridgelabz.fundoonotes.enums;

import com.bridgelabz.fundoonotes.note.model.NoteDetails;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum SortedNotesEnum {

    NAME{
        @Override
        public List<NoteDetails> sortedNotes(List<NoteDetails> allByUser) {
            NoteDetails[] noteDetails = allByUser.toArray(new NoteDetails[allByUser.size()]);

            Arrays.parallelSort(noteDetails, Comparator.comparing(noteDetails1 -> noteDetails1.getTitle()));
            List<NoteDetails> collect = Arrays.stream(noteDetails).collect(Collectors.toList());
            return collect;
        }
    },
    DATE{
        @Override
        public List<NoteDetails> sortedNotes(List<NoteDetails> allByUser) {
            NoteDetails[] noteDetails = allByUser.toArray(new NoteDetails[allByUser.size()]);

            Arrays.parallelSort(noteDetails,
                    Comparator.comparing(noteDetails1 -> noteDetails1.getCreated()));

            List<NoteDetails> collect = Arrays.stream(noteDetails).collect(Collectors.toList());

            return collect;
        }
    },
    DESCRIPTION{
        @Override
        public List<NoteDetails> sortedNotes(List<NoteDetails> allByUser) {
            NoteDetails[] noteDetails = allByUser.toArray(new NoteDetails[allByUser.size()]);

            Arrays.parallelSort(noteDetails,
                    Comparator.comparing(noteDetails1 -> noteDetails1.getDescription()));

            List<NoteDetails> collect = Arrays.stream(noteDetails).collect(Collectors.toList());

            return collect;
        }
    };

    public abstract List<NoteDetails> sortedNotes(List<NoteDetails> allByUser);


}
