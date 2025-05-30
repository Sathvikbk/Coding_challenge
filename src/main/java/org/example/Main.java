package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    String inputPath = "/Users/sathvikbk/Downloads/leads.json";

    try {
      ObjectMapper mapper = new ObjectMapper();
      // ← add these two lines:
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

      JsonNode rootNode = mapper.readTree(new File(inputPath));
      ArrayNode leadsArray = (ArrayNode) rootNode.get("leads");

      List<Lead> inputLeads = new ArrayList<>();
      for (int i = 0; i < leadsArray.size(); i++) {
        inputLeads.add(new Lead(leadsArray.get(i), i));
      }

      DeduplicationResult result = LeadDup.dupLeads(inputLeads);

      LeadDup.writeDeduplicatedResults(result, mapper);
      LeadDup.printChangeLog(result);

    } catch (IOException e) {
      System.err.println("Error processing file: " + e.getMessage());
      e.printStackTrace();
    }
  }
}