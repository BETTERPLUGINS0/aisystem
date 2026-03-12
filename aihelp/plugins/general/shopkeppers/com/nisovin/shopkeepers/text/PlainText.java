package com.nisovin.shopkeepers.text;

import com.nisovin.shopkeepers.util.java.Validate;

public class PlainText extends TextBuilder {
   private final String text;

   PlainText(String text) {
      Validate.notNull(text, (String)"text is null");
      this.text = text;
   }

   public String getText() {
      return this.text;
   }

   protected void appendPlainText(StringBuilder builder, boolean formatText) {
      builder.append(this.getText());
      super.appendPlainText(builder, formatText);
   }

   public boolean isPlainTextEmpty() {
      return !super.isPlainTextEmpty() ? false : this.text.isEmpty();
   }

   public Text copy() {
      PlainText copy = new PlainText(this.text);
      copy.copy(this, true);
      return copy.build();
   }

   protected void appendToStringFeatures(StringBuilder builder) {
      builder.append(", text=");
      builder.append(this.getText());
      super.appendToStringFeatures(builder);
   }
}
