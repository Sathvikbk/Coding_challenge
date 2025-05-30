package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class LeadDup {
  public static DeduplicationResult dupLeads(List<Lead> input) {
    DeduplicationResult result = new DeduplicationResult();
    Map<String, Lead> byId = new HashMap<>();
    Map<String, Lead> byEmail = new HashMap<>();

    for (Lead current : input) {
      Lead existing = byId.getOrDefault(current.getId(), byEmail.get(current.getEmail()));
      String type = existing != null ? (byId.containsKey(current.getId()) ? "ID" : "EMAIL") : null;

      if (existing != null) {
        Lead keep = current.isNew(existing) ? current : existing;
        Lead discard = (keep == current) ? existing : current;

        result.getLeads().remove(discard);
        byId.remove(discard.getId());
        byEmail.remove(discard.getEmail());

        result.addLog(new ChangingPair(type, discard, keep));
        if (keep == current) {
          result.addLead(current);
          byId.put(current.getId(), current);
          byEmail.put(current.getEmail(), current);
        }
      } else {
        result.addLead(current);
        byId.put(current.getId(), current);
        byEmail.put(current.getEmail(), current);
      }
    }

    return result;
  }

  /**
   * Writes deduplicated leads to a JSON file and logs summary.
   */
  public static void writeDeduplicatedResults(DeduplicationResult result, ObjectMapper mapper) throws IOException {
    mapper.writerWithDefaultPrettyPrinter()
            .writeValue(new File("leads_deduplicated.json"), result.getLeads());
    System.out.println("Deduplicated results written to leads_deduplicated.json");
    System.out.println("Original count: " + (result.getLeads().size() + result.getLog().size()));
    System.out.println("Final count: " + result.getLeads().size());
    System.out.println("Duplicates removed: " + result.getLog().size());
  }

  /**
   * Prints a change log for deduplication.
   */
  public static void printChangeLog(DeduplicationResult result) {
    System.out.println("\n=== DEDUPLICATION CHANGE LOG ===");
    if (result.getLog().isEmpty()) {
      System.out.println("No duplicates found.");
      return;
    }
    int i = 1;
    for (ChangingPair change : result.getLog()) {
      System.out.printf("\nDuplicate #%d (Type: %s):\n", i++, change.getDupType());
      System.out.printf("  Discarded: ID=%s, Email=%s, Date=%s\n",
              change.getRecord1().getId(), change.getRecord1().getEmail(), change.getRecord1().getEntryDate());
      System.out.printf("  Kept:      ID=%s, Email=%s, Date=%s\n",
              change.getRecord2().getId(), change.getRecord2().getEmail(), change.getRecord2().getEntryDate());
      if (!change.getFieldChanges().isEmpty()) {
        System.out.println("  Field Changes:");
        change.getFieldChanges().forEach(fc -> System.out.println("    " + fc));
      } else {
        System.out.println("  No field differences (records identical except for key)");
      }
    }
  }
}
