package com.nisovin.shopkeepers.text;

import com.nisovin.shopkeepers.util.java.Validate;

public class InsertionText extends TextBuilder {
   private final String insertion;

   InsertionText(String insertion) {
      Validate.notEmpty(insertion, "insertion is null or empty");
      this.insertion = insertion;
   }

   public String getInsertion() {
      return this.insertion;
   }

   public boolean isPlainText() {
      return false;
   }

   public Text copy() {
      InsertionText copy = new InsertionText(this.insertion);
      copy.copy(this, true);
      return copy.build();
   }

   protected void appendToStringFeatures(StringBuilder builder) {
      builder.append(", insertion=");
      builder.append(this.getInsertion());
      super.appendToStringFeatures(builder);
   }
}
