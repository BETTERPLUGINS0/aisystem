package com.nisovin.shopkeepers.text;

import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.text.MessageArguments;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.bukkit.ChatColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface Text {
   Text EMPTY = of("");

   static Text of(String text) {
      return text(text).build();
   }

   static Text of(@Nullable Object object) {
      Object resolved = object;
      if (object instanceof Supplier) {
         resolved = ((Supplier)object).get();
      }

      return resolved instanceof Text ? (Text)resolved : of(String.valueOf(resolved));
   }

   static Text parse(String input) {
      return TextParser.parse(input);
   }

   static List<Text> parse(Collection<? extends String> inputs) {
      List<Text> texts = new ArrayList(inputs.size());
      Iterator var2 = inputs.iterator();

      while(var2.hasNext()) {
         String input = (String)var2.next();
         texts.add(parse(input));
      }

      return texts;
   }

   static TextBuilder text(String text) {
      return new PlainText(text);
   }

   static TextBuilder text(@Nullable Object object) {
      Object resolved = object;
      if (object instanceof Supplier) {
         resolved = ((Supplier)object).get();
      }

      Validate.isTrue(!(resolved instanceof Text), "object is already a Text");
      return text(String.valueOf(resolved));
   }

   static TextBuilder newline() {
      return text("\n");
   }

   static TextBuilder formatting(String code) {
      return new FormattingText(code);
   }

   static TextBuilder formatting(ChatColor formatting) {
      return formatting(String.valueOf(formatting.getChar()));
   }

   static TextBuilder color(ChatColor color) {
      return formatting(color);
   }

   static TextBuilder color(Color color) {
      String var10000 = String.format("%08x", color.getRGB());
      return formatting("#" + var10000.substring(2));
   }

   static TextBuilder reset() {
      return formatting(ChatColor.RESET);
   }

   static TextBuilder translatable(String translationKey) {
      return new TranslatableText(translationKey);
   }

   static TextBuilder placeholder(String placeholderKey) {
      return new PlaceholderText(placeholderKey);
   }

   static TextBuilder hoverEvent(HoverEventText.Content content) {
      return new HoverEventText(content);
   }

   static TextBuilder hoverEvent(Text hoverText) {
      return hoverEvent((HoverEventText.Content)(new HoverEventText.TextContent(hoverText)));
   }

   static TextBuilder clickEvent(ClickEventText.Action action, String value) {
      return new ClickEventText(action, value);
   }

   static TextBuilder insertion(@Nullable String insertion) {
      return (TextBuilder)(insertion != null && !insertion.isEmpty() ? new InsertionText(insertion) : text(""));
   }

   @Nullable
   <T extends Text> T getParent();

   @NonNull
   <T extends Text> T getRoot();

   @Nullable
   Text getChild();

   @Nullable
   Text getNext();

   Text setPlaceholderArguments(MessageArguments var1);

   Text setPlaceholderArguments(Map<? extends String, ?> var1);

   Text setPlaceholderArguments(Object... var1);

   Text clearPlaceholderArguments();

   String toPlainText();

   String toFormat();

   boolean isPlainText();

   boolean isPlainTextEmpty();

   String toUnformattedText();

   Text copy();

   String toString();
}
