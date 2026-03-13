package com.nisovin.shopkeepers.text;

import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.text.MessageArguments;
import org.bukkit.ChatColor;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class TextBuilder extends AbstractText {
   private boolean built = false;

   protected TextBuilder() {
   }

   public boolean isBuilt() {
      return this.built;
   }

   protected void validateModification() {
      Validate.State.isTrue(!this.built, "This Text has already been built!");
   }

   public Text build() {
      if (this.isBuilt()) {
         return this;
      } else {
         this.built = true;
         buildIfRequired(this.getChild());
         buildIfRequired(this.getNext());
         return this;
      }
   }

   public Text buildRoot() {
      Text root = this.getRoot();
      buildIfRequired(root);
      return root;
   }

   protected static void buildIfRequired(@Nullable Object text) {
      if (text instanceof TextBuilder) {
         ((TextBuilder)text).build();
      }

   }

   protected static boolean isUnbuiltText(@Nullable Object text) {
      return text instanceof TextBuilder && !((TextBuilder)text).isBuilt();
   }

   public Text setPlaceholderArguments(MessageArguments arguments) {
      Validate.State.isTrue(this.isBuilt(), "Cannot set placeholder arguments of unbuilt Text!");
      return super.setPlaceholderArguments(arguments);
   }

   public <T extends Text> T child(T child) {
      this.validateModification();
      this.setChild(child);
      return child;
   }

   public <T extends Text> T next(T next) {
      this.validateModification();
      this.setNext(next);
      return next;
   }

   public TextBuilder childAndNext(@Nullable Text child, @Nullable Text next) {
      this.validateModification();
      this.setChild(child);
      this.setNext(next);
      return this;
   }

   public TextBuilder childText(String text) {
      return (TextBuilder)this.child(Text.text(text));
   }

   public TextBuilder childText(@Nullable Object object) {
      return (TextBuilder)this.child(Text.text(object));
   }

   public TextBuilder childNewline() {
      return (TextBuilder)this.child(Text.newline());
   }

   public TextBuilder childFormatting(ChatColor formatting) {
      return (TextBuilder)this.child(Text.formatting(formatting));
   }

   public TextBuilder childColor(ChatColor color) {
      return (TextBuilder)this.child(Text.color(color));
   }

   public TextBuilder childReset() {
      return (TextBuilder)this.child(Text.reset());
   }

   public TextBuilder childTranslatable(String translationKey) {
      return (TextBuilder)this.child(Text.translatable(translationKey));
   }

   public TextBuilder childPlaceholder(String placeholderKey) {
      return (TextBuilder)this.child(Text.placeholder(placeholderKey));
   }

   public TextBuilder childHoverEvent(HoverEventText.Content content) {
      return (TextBuilder)this.child(Text.hoverEvent(content));
   }

   public TextBuilder childHoverEvent(Text hoverText) {
      return (TextBuilder)this.child(Text.hoverEvent(hoverText));
   }

   public TextBuilder childClickEvent(ClickEventText.Action action, String value) {
      return (TextBuilder)this.child(Text.clickEvent(action, value));
   }

   public TextBuilder childInsertion(@Nullable String insertion) {
      return (TextBuilder)this.child(Text.insertion(insertion));
   }

   public TextBuilder text(String text) {
      return (TextBuilder)this.next(Text.text(text));
   }

   public TextBuilder text(@Nullable Object object) {
      return (TextBuilder)this.next(Text.text(object));
   }

   public TextBuilder newline() {
      return (TextBuilder)this.next(Text.newline());
   }

   public TextBuilder formatting(ChatColor formatting) {
      return (TextBuilder)this.next(Text.formatting(formatting));
   }

   public TextBuilder color(ChatColor color) {
      return (TextBuilder)this.next(Text.color(color));
   }

   public TextBuilder reset() {
      return (TextBuilder)this.next(Text.reset());
   }

   public TextBuilder translatable(String translationKey) {
      return (TextBuilder)this.next(Text.translatable(translationKey));
   }

   public TextBuilder placeholder(String placeholderKey) {
      return (TextBuilder)this.next(Text.placeholder(placeholderKey));
   }

   public TextBuilder hoverEvent(HoverEventText.Content content) {
      return (TextBuilder)this.next(Text.hoverEvent(content));
   }

   public TextBuilder hoverEvent(Text hoverText) {
      return (TextBuilder)this.next(Text.hoverEvent(hoverText));
   }

   public TextBuilder clickEvent(ClickEventText.Action action, String value) {
      return (TextBuilder)this.next(Text.clickEvent(action, value));
   }

   public TextBuilder insertion(@Nullable String insertion) {
      return (TextBuilder)this.next(Text.insertion(insertion));
   }

   public abstract Text copy();

   public TextBuilder copy(Text sourceText, boolean copyChilds) {
      Validate.notNull(sourceText, (String)"sourceText is null");
      if (copyChilds) {
         this.copyChild(sourceText);
         this.copyNext(sourceText);
      }

      return this;
   }

   protected void copyChild(Text sourceText) {
      Text sourceChild = sourceText.getChild();
      if (sourceChild != null) {
         this.child(sourceChild.copy());
      }

   }

   protected void copyNext(Text sourceText) {
      Text sourceNext = sourceText.getNext();
      if (sourceNext != null) {
         this.next(sourceNext.copy());
      }

   }

   protected void appendToStringFeatures(StringBuilder builder) {
      builder.append(", child=");
      builder.append(this.getChild());
      builder.append(", next=");
      builder.append(this.getNext());
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getClass().getSimpleName());
      builder.append(" [");
      builder.append("built=");
      builder.append(this.isBuilt());
      this.appendToStringFeatures(builder);
      builder.append("]");
      return builder.toString();
   }
}
