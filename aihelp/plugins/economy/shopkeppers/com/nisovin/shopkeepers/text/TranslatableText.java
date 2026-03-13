package com.nisovin.shopkeepers.text;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TranslatableText extends TextBuilder {
   private final String translationKey;
   private List<? extends Text> translationArguments = Collections.emptyList();

   TranslatableText(String translationKey) {
      Validate.notEmpty(translationKey, "translationKey is null or empty");
      this.translationKey = translationKey;
   }

   public String getTranslationKey() {
      return this.translationKey;
   }

   public List<? extends Text> getTranslationArguments() {
      return this.translationArguments;
   }

   public TranslatableText setTranslationArguments(List<?> translationArguments) {
      Validate.notNull(translationArguments, (String)"translationArguments is null");
      if (translationArguments.isEmpty()) {
         this.translationArguments = Collections.emptyList();
      } else {
         List<Text> translationTextArguments = new ArrayList(translationArguments.size());
         translationArguments.forEach((argument) -> {
            Validate.notNull(argument, "translationArguments contains null");
            Validate.isTrue(argument != this, "translationArguments contains this Text itself");
            Text argumentText = Text.of(argument);
            Validate.isTrue(argumentText.getParent() == null, "translationArguments contains a non-root Text");
            translationTextArguments.add(argumentText);
         });
         translationTextArguments.forEach(TextBuilder::buildIfRequired);
         this.translationArguments = Collections.unmodifiableList(translationTextArguments);
      }

      return this;
   }

   public boolean isPlainText() {
      return false;
   }

   public Text copy() {
      TranslatableText copy = new TranslatableText(this.translationKey);
      copy.copy(this, true);
      return copy.build();
   }

   public TranslatableText copy(Text sourceText, boolean copyChilds) {
      super.copy(sourceText, copyChilds);
      this.copyTranslationArguments(sourceText);
      return this;
   }

   protected void copyTranslationArguments(Text sourceText) {
      if (sourceText instanceof TranslatableText) {
         TranslatableText translatableSourceText = (TranslatableText)sourceText;
         this.setTranslationArguments(copyAll(translatableSourceText.getTranslationArguments()));
      }

   }

   private static List<? extends Text> copyAll(Collection<? extends Text> toCopy) {
      assert toCopy != null;

      if (toCopy.isEmpty()) {
         return Collections.emptyList();
      } else {
         List<Text> copies = new ArrayList(toCopy.size());
         toCopy.forEach((text) -> {
            copies.add(text.copy());
         });
         return copies;
      }
   }

   protected void appendToStringFeatures(StringBuilder builder) {
      builder.append(", translationKey=");
      builder.append(this.translationKey);
      builder.append(", translationArguments=");
      builder.append(this.translationArguments);
      super.appendToStringFeatures(builder);
   }
}
