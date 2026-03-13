package com.nisovin.shopkeepers.text;

import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;

public class FormattingText extends TextBuilder {
   private final String formattingCode;

   FormattingText(String formattingCode) {
      Validate.notEmpty(formattingCode, "formatting code is null or empty");
      Validate.isTrue(formattingCode.length() == 1 || TextUtils.isHexCode(formattingCode), () -> {
         return "Invalid formatting code: " + formattingCode;
      });
      this.formattingCode = formattingCode;
   }

   public String getFormattingCode() {
      return this.formattingCode;
   }

   private boolean isHexColor() {
      return this.formattingCode.length() > 1;
   }

   protected void appendPlainText(StringBuilder builder, boolean formatText) {
      if (formatText) {
         builder.append('&');
         builder.append(this.getFormattingCode());
      } else {
         builder.append('§');
         if (this.isHexColor()) {
            builder.append(TextUtils.toBukkitHexCode(this.getFormattingCode(), '§'));
         } else {
            builder.append(this.getFormattingCode());
         }
      }

      super.appendPlainText(builder, formatText);
   }

   public boolean isPlainTextEmpty() {
      return false;
   }

   public Text copy() {
      FormattingText copy = new FormattingText(this.formattingCode);
      copy.copy(this, true);
      return copy.build();
   }

   protected void appendToStringFeatures(StringBuilder builder) {
      builder.append(", formattingCode=");
      builder.append(this.getFormattingCode());
      super.appendToStringFeatures(builder);
   }
}
