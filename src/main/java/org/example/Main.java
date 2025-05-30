package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {

    try {
      ObjectMapper mapper = new ObjectMapper();

      JsonNode rootNode = mapper.readTree(new File("/Users/sathvikbk/Downloads/leads.json"));
      ArrayNode leadsArray = (ArrayNode) rootNode.get("leads");

      List<LeadDup.Lead> inputLeads = new ArrayList<>();
      for (int i = 0; i < leadsArray.size(); i++) {
        inputLeads.add(new LeadDup.Lead(leadsArray.get(i), i));
      }

      LeadDup.DeduplicationResult result = LeadDup.dupLeads(inputLeads);

      LeadDup.writeDeduplicatedResults(result, mapper);
      LeadDup.printChangeLog(result);

    } catch (IOException e) {
      System.err.println("Error processing file: " + e.getMessage());
      e.printStackTrace();
    }
  }
}