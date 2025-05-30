package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChangingPair {
  public String dupType;
  public Lead record1;
  public Lead record2;
  public List<FieldChange> fieldChanges;

  public ChangingPair(String dupType, Lead record1, Lead record2) {
    this.dupType = dupType;
    this.record1 = record1;
    this.record2 = record2;
    this.fieldChanges = new ArrayList<>();
    caculateChange();
  }
  private void caculateChange(){
    changeifDifferent("firstName",record1.getFirstName(), record2.getFirstName());
    changeifDifferent("lastName",record1.getLastName(), record2.getLastName());
    changeifDifferent("address",record1.getAddress(), record2.getAddress());
    changeifDifferent("entryDate",record1.getEntryDate(), record2.getEntryDate());
  }
  private void changeifDifferent(String field, String from, String to) {
    if (!Objects.equals(from, to)) {
      fieldChanges.add(new FieldChange(field, from, to));
    }
  }
  public String getDupType() {
    return dupType;
  }

  public Lead getRecord1() {
    return record1;
  }

  public Lead getRecord2() {
    return record2;
  }

  public List<FieldChange> getFieldChanges() {
    return fieldChanges;
  }

}
