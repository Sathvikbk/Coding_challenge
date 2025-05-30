package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Lead {
    private String _id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String entryDate;
    private int originalIndex;

    public Lead(JsonNode node, int originalIndex) {
      this._id = node.has("id") ? node.get("id").asText() : "";
      this.email = node.has("email") ? node.get("email").asText() : "";
      this.firstName = node.has("firstName") ? node.get("firstName").asText() : "";
      this.lastName = node.has("lastName") ? node.get("lastName").asText() : "";
      this.address = node.has("address") ? node.get("address").asText() : "";
      this.entryDate = node.has("entryDate") ? node.get("entryDate").asText() : "";
      this.originalIndex = originalIndex;
    }

    public Lead(String id, String email, String firstName, String lastName, String address, String entryDate, int originalIndex) {
      this._id = id;
      this.email = email;
      this.firstName = firstName;
      this.lastName = lastName;
      this.address = address;
      this.entryDate = entryDate;
      this.originalIndex = originalIndex;
    }

    public OffsetDateTime getParsedDate() {
      return OffsetDateTime.parse(entryDate);
    }

    public boolean isNew(Lead l) {
      int dateCompare = this.entryDate.compareTo(l.entryDate);
      if (dateCompare != 0) {
        return dateCompare > 0;
      }
      return this.originalIndex > l.originalIndex;
    }

  public ObjectNode toJson(ObjectMapper mapper) {
    ObjectNode node = mapper.createObjectNode();
    node.put("_id", _id);
    node.put("email", email);
    node.put("firstName", firstName);
    node.put("lastName", lastName);
    node.put("address", address);
    node.put("entryDate", entryDate);
    return node;
    }

  public String getId() { return _id; }
  public String getEmail() { return email; }
  public String getFirstName() { return firstName; }
  public String getLastName() { return lastName; }
  public String getAddress() { return address; }
  public String getEntryDate() { return entryDate; }
}
//  public static class Changing{
//    public String dupType;
//    public Lead Record1;
//    public Lead Record2;
//    public List<FieldChange> fieldChanges;
//
//    public Changing(String dupType, Lead record1, Lead record2){
//      this.dupType = dupType;
//      this.Record1 = record1;
//      this.Record2 = record2;
//      this.fieldChanges = new ArrayList<>();
//      caculateChange();
//    }
//    private void caculateChange(){
//      changeifDifferent("firstName",Record1.firstName, Record2.firstName);
//      changeifDifferent("lastName",Record1.lastName, Record2.lastName);
//      changeifDifferent("address",Record1.address, Record2.address);
//      changeifDifferent("entryDate",Record1.entryDate, Record2.entryDate);
//    }
//
//    private void changeifDifferent(String field, String from, String to){
//      if(!Objects.equals(from, to)){
//        fieldChanges.add(new FieldChange(field, from, to));
//      }
//    }
//  }
//
//  public static class FieldChange{
//    public String fieldName;
//    public String initialValue;
//    public String toValue;
//
//    public FieldChange(String fieldName, String initialValue, String toValue){
//      this.fieldName = fieldName;
//      this.initialValue = initialValue;
//      this.toValue = toValue;
//    }
//
//    @Override
//    public String toString(){
//      return String.format("%s: '%s' -> '%s'", fieldName, initialValue, toValue);
//    }
//  }

//  public static class DeduplicationResult{
//    public List<Lead> Lead;
//    public List<Changing> Log;
//    public DeduplicationResult() {
//      this.Lead = new ArrayList<>();
//      this.Log = new ArrayList<>();
//    }
//  }

//  public static DeduplicationResult dupLeads(List<Lead> input){
//    DeduplicationResult result = new DeduplicationResult();
//
//    Map<String, Lead> recordsById = new HashMap<>();
//    Map<String, Lead> recordsByEmail = new HashMap<>();
//
//    for (Lead current : input){
//      Lead existingById = recordsById.get(current._id);
//      Lead existingByEmail = recordsByEmail.get(current.email);
//
//      Lead existing = null;
//      String dupType = null;
//
//      if(existingById != null){
//        existing = existingById;
//        dupType = "ID";
//      } else if (existingByEmail != null) {
//        existing = existingByEmail;
//        dupType = "EMAIL";
//      }
//
//      if(existing != null){
//        Lead toKeep, toDiscard;
//
//        if(current.isNew(existing)){
//          toKeep = current;
//          toDiscard = existing;
//
//          result.Lead.remove(existing);
//          recordsById.remove(existing._id);
//          recordsByEmail.remove(existing.email);
//        }
//        else{
//          toKeep = existing;
//          toDiscard = current;
//        }
//        result.Log.add(new Changing(dupType, toDiscard, toKeep));
//        if (toKeep == current) {
//          result.Lead.add(toKeep);
//          recordsById.put(toKeep._id, toKeep);
//          recordsByEmail.put(toKeep.email, toKeep);
//        }
//
//      } else {
//        // No duplicate found - add to result and tracking maps
//        result.Lead.add(current);
//        recordsById.put(current._id, current);
//        recordsByEmail.put(current.email, current);
//      }
//    }
//    return result;
//  }
//  public static void writeDeduplicatedResults(DeduplicationResult result, ObjectMapper mapper)
//          throws IOException {
//
//    ObjectNode outputRoot = mapper.createObjectNode();
//    ArrayNode outputArray = mapper.createArrayNode();
//
//    for (Lead lead : result.Lead) {
//      outputArray.add(lead.toJ(mapper));
//    }
//
//    outputRoot.set("leads", outputArray);
//
//    mapper.writerWithDefaultPrettyPrinter()
//            .writeValue(new File("leads_deduplicated.json"), outputRoot);
//
//    System.out.println("Deduplicated results written to leads_deduplicated.json");
//    System.out.println("Original count: " + (result.Lead.size() + result.Log.size()));
//    System.out.println("Final count: " + result.Lead.size());
//    System.out.println("Duplicates removed: " + result.Log.size());
//  }
//
//  public static void printChangeLog(DeduplicationResult result) {
//    System.out.println("\n=== DEDUPLICATION CHANGE LOG ===");
//
//    if (result.Log.isEmpty()) {
//      System.out.println("No duplicates found.");
//      return;
//    }
//
//    for (int i = 0; i < result.Log.size(); i++) {
//      Changing log = result.Log.get(i);
//      System.out.printf("\nDuplicate #%d (Duplicate %s):\n", i + 1, log.dupType);
//      System.out.printf("  Discarded Record: ID=%s, Email=%s, Date=%s\n",
//              log.Record1._id, log.Record1.email, log.Record1.entryDate);
//      System.out.printf("  Kept Record:      ID=%s, Email=%s, Date=%s\n",
//              log.Record2._id, log.Record2.email, log.Record2.entryDate);
//
//      if (!log.fieldChanges.isEmpty()) {
//        System.out.println("  Field Changes:");
//        for (FieldChange change : log.fieldChanges) {
//          System.out.printf("    %s\n", change);
//        }
//      } else {
//        System.out.println("  No field differences (records were identical except for duplicate key)");
//      }
//    }
//  }
//}
