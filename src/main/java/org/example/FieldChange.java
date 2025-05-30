package org.example;

public class FieldChange {
  public final String fieldName;
  public final String initialValue;
  public final String Record2;

  public FieldChange(String fieldName, String record1, String record2) {
    this.fieldName = fieldName;
    this.initialValue = record1;
    this.Record2 = record2;
  }

  @Override
  public String toString() {
    return String.format("%s: '%s' → '%s'", fieldName, initialValue, Record2);
  }
  public String getFieldName() { return fieldName; }
  public String getFromValue()  { return initialValue;}
  public String getToValue()    { return Record2; }
}
