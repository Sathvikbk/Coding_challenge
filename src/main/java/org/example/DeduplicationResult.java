package org.example;

import java.util.ArrayList;
import java.util.List;

public class DeduplicationResult {
  private List<Lead> leads;
  private List<ChangingPair> log;

  public DeduplicationResult() {
    this.leads = new ArrayList<>();
    this.log = new ArrayList<>();
  }

  public List<Lead> getLeads() {
    return leads;
  }

  public List<ChangingPair> getLog() {
    return log;
  }

  void addLead(Lead lead) {
    leads.add(lead);
  }

  void addLog(ChangingPair change) {
    log.add(change);
  }
}
