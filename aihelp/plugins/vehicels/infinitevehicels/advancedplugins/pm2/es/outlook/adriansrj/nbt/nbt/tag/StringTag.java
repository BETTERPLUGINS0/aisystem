package es.outlook.adriansrj.nbt.nbt.tag;

public class StringTag extends Tag<String> implements Comparable<StringTag> {
   public static final byte ID = 8;
   public static final String ZERO_VALUE = "";

   public StringTag() {
      super("");
   }

   public StringTag(String var1) {
      super(var1);
   }

   public byte getID() {
      return 8;
   }

   public String getValue() {
      return (String)super.getValue();
   }

   public void setValue(String var1) {
      super.setValue(var1);
   }

   public String valueToString(int var1) {
      return escapeString(this.getValue(), false);
   }

   public boolean equals(Object var1) {
      return super.equals(var1) && this.getValue().equals(((StringTag)var1).getValue());
   }

   public int compareTo(StringTag var1) {
      return this.getValue().compareTo(var1.getValue());
   }

   public StringTag clone() {
      return new StringTag(this.getValue());
   }
}
