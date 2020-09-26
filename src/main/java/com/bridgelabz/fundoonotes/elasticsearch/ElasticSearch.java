package com.bridgelabz.fundoonotes.elasticsearch;

import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service("IElasticSearch")
public class ElasticSearch implements IElasticSearch {

    private final String INDEX = "fundoo_notes";
    private final String TYPE = "note_details";

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public String createNote(NoteDetails noteDetails) {
        @SuppressWarnings("unchecked")
        Map<String, Object> documentMapper = objectMapper.convertValue(noteDetails, Map.class);

        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, String.valueOf(noteDetails.getId()))
                .source(documentMapper);
        try {
            client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "Note Creation Successfully";
    }

    @Override
    @SuppressWarnings("unchecked")
    public String updateNote(NoteDetails note) throws IOException {
        Map<String, Object> document = objectMapper.convertValue(note, Map.class);
        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, String.valueOf(note.getId())).doc(document);
        UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);
        return update.getResult().name();
    }

}
