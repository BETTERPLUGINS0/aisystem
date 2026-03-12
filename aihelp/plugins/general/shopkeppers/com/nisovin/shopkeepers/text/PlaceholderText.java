package com.nisovin.shopkeepers.text;

import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.text.MessageArguments;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PlaceholderText extends TextBuilder {
   public static final char PLACEHOLDER_PREFIX_CHAR = '{';
   public static final char PLACEHOLDER_SUFFIX_CHAR = '}';
   private final String placeholderKey;
   private final String formattedPlaceholderKey;
   @Nullable
   private Text placeholderArgument = null;

   PlaceholderText(String placeholderKey) {
      Validate.notEmpty(placeholderKey, "placeholderKey is null or empty");
      this.placeholderKey = placeholderKey;
      this.formattedPlaceholderKey = "{" + placeholderKey + "}";
   }

   private UnsupportedOperationException unsupportedPlaceholderOperation() {
      return new UnsupportedOperationException("This operation is not supported for placeholder Texts!");
   }

   public String getPlaceholderKey() {
      return this.placeholderKey;
   }

   public String getFormattedPlaceholderKey() {
      return this.formattedPlaceholderKey;
   }

   @Nullable
   public Text getPlaceholderArgument() {
      return this.placeholderArgument;
   }

   public boolean hasPlaceholderArgument() {
      return this.placeholderArgument != null;
   }

   public void setPlaceholderArgument(@Nullable Object placeholderArgument) {
      Text placeholderArgumentText = null;
      if (placeholderArgument != null) {
         Validate.isTrue(placeholderArgument != this, "placeholderArgument cannot be this Text itself");
         placeholderArgumentText = Text.of(placeholderArgument);
         Validate.isTrue(placeholderArgumentText.getParent() == null, "placeholderArgument is a non-root Text");
         buildIfRequired(placeholderArgumentText);
      }

      this.placeholderArgument = placeholderArgumentText;
   }

   public Text setPlaceholderArguments(MessageArguments arguments) {
      Text prevArgument = this.getPlaceholderArgument();
      this.setPlaceholderArgument((Object)null);

      try {
         super.setPlaceholderArguments(arguments);
         Object argument = arguments.get(this.placeholderKey);
         if (argument != null) {
            this.setPlaceholderArgument(argument);
            prevArgument = null;
         }
      } finally {
         if (prevArgument != null) {
            this.setPlaceholderArgument(prevArgument);
         }

      }

      return this;
   }

   public Text clearPlaceholderArguments() {
      this.setPlaceholderArgument((Object)null);
      super.clearPlaceholderArguments();
      return this;
   }

   @Nullable
   public Text getChild() {
      return this.placeholderArgument;
   }

   public <T extends Text> T child(T child) {
      throw this.unsupportedPlaceholderOperation();
   }

   protected void appendPlainText(StringBuilder builder, boolean formatText) {
      if (!formatText && this.hasPlaceholderArgument()) {
         super.appendPlainText(builder, formatText);
      } else {
         Text prevArgument = this.getPlaceholderArgument();
         this.setPlaceholderArgument((Object)null);
         builder.append(this.getFormattedPlaceholderKey());

         try {
            super.appendPlainText(builder, formatText);
         } finally {
            if (prevArgument != null) {
               this.setPlaceholderArgument(prevArgument);
            }

         }
      }

   }

   public Text copy() {
      PlaceholderText copy = new PlaceholderText(this.placeholderKey);
      copy.copy(this, true);
      return copy.build();
   }

   public PlaceholderText copy(Text sourceText, boolean copyChilds) {
      super.copy(sourceText, copyChilds);
      this.copyPlaceholderArgument(sourceText);
      return this;
   }

   protected void copyChild(Text sourceText) {
   }

   protected void copyPlaceholderArgument(Text sourceText) {
      if (sourceText instanceof PlaceholderText) {
         PlaceholderText placeholderSourceText = (PlaceholderText)sourceText;
         Text placeholderArgument = placeholderSourceText.getPlaceholderArgument();
         this.setPlaceholderArgument(placeholderArgument != null ? placeholderArgument.copy() : null);
      }

   }

   protected void appendToStringFeatures(StringBuilder builder) {
      builder.append(", placeholderKey=");
      builder.append(this.placeholderKey);
      super.appendToStringFeatures(builder);
   }
}
