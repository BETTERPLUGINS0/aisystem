package com.volmit.iris.util.nbt.tag;

public class StringTag extends Tag<String> implements Comparable<StringTag> {
   public static final byte ID = 8;
   public static final String ZERO_VALUE = "";

   public StringTag() {
      super("");
   }

   public StringTag(String value) {
      super(var1);
   }

   public byte getID() {
      return 8;
   }

   public String getValue() {
      return (String)super.getValue();
   }

   public void setValue(String value) {
      super.setValue(var1);
   }

   public String valueToString(int maxDepth) {
      return escapeString(this.getValue(), false);
   }

   public boolean equals(Object other) {
      return super.equals(var1) && this.getValue().equals(((StringTag)var1).getValue());
   }

   public int compareTo(StringTag o) {
      return this.getValue().compareTo(var1.getValue());
   }

   public StringTag clone() {
      return new StringTag(this.getValue());
   }
}
