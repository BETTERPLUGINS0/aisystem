package com.nisovin.shopkeepers.text;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.text.MessageArguments;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractText implements Text {
   private static final Map<String, Object> TEMP_ARGUMENTS_MAP = new HashMap();
   private static final MessageArguments TEMP_ARGUMENTS;
   @Nullable
   private Text parent = null;
   @Nullable
   private Text child = null;
   @Nullable
   private Text next = null;

   protected AbstractText() {
   }

   @Nullable
   public <T extends Text> T getParent() {
      return (Text)Unsafe.cast(this.parent);
   }

   private void setParent(@Nullable Text parent) {
      this.parent = parent;
   }

   @NonNull
   public <T extends Text> T getRoot() {
      Object text;
      Text parent;
      for(text = this; (parent = ((Text)text).getParent()) != null; text = parent) {
      }

      return (Text)text;
   }

   @Nullable
   public Text getChild() {
      return this.child;
   }

   protected void setChild(@Nullable Text child) {
      if (child != null) {
         Validate.isTrue(child != this, "child cannot be this Text itself");
         Validate.isTrue(child.getParent() == null, "child already has a parent");
         ((AbstractText)child).setParent(this);
      }

      if (this.child != null) {
         ((AbstractText)this.child).setParent((Text)null);
      }

      this.child = child;
   }

   @Nullable
   public Text getNext() {
      return this.next;
   }

   protected void setNext(@Nullable Text next) {
      if (next != null) {
         Validate.isTrue(next != this, "next cannot be this Text itself");
         Validate.isTrue(next.getParent() == null, "next already has a parent");
         ((AbstractText)next).setParent(this);
      }

      if (this.next != null) {
         ((AbstractText)this.next).setParent((Text)null);
      }

      this.next = next;
   }

   public Text setPlaceholderArguments(MessageArguments arguments) {
      Validate.notNull(arguments, (String)"arguments is null");
      Text child = this.getChild();
      if (child != null) {
         child.setPlaceholderArguments(arguments);
      }

      Text next = this.getNext();
      if (next != null) {
         next.setPlaceholderArguments(arguments);
      }

      return this;
   }

   public final Text setPlaceholderArguments(Map<? extends String, ?> arguments) {
      return this.setPlaceholderArguments(MessageArguments.ofMap(arguments));
   }

   public final Text setPlaceholderArguments(Object... argumentPairs) {
      assert TEMP_ARGUMENTS_MAP.isEmpty();

      Text var2;
      try {
         StringUtils.addArgumentsToMap(TEMP_ARGUMENTS_MAP, argumentPairs);
         var2 = this.setPlaceholderArguments(TEMP_ARGUMENTS);
      } finally {
         TEMP_ARGUMENTS_MAP.clear();
      }

      return var2;
   }

   public Text clearPlaceholderArguments() {
      Text child = this.getChild();
      if (child != null) {
         child.clearPlaceholderArguments();
      }

      Text next = this.getNext();
      if (next != null) {
         next.clearPlaceholderArguments();
      }

      return this;
   }

   public String toPlainText() {
      StringBuilder builder = new StringBuilder();
      this.appendPlainText(builder, false);
      return builder.toString();
   }

   public String toFormat() {
      StringBuilder builder = new StringBuilder();
      this.appendPlainText(builder, true);
      return builder.toString();
   }

   protected void appendPlainText(StringBuilder builder, boolean formatText) {
      Text child = this.getChild();
      if (child != null) {
         ((AbstractText)child).appendPlainText(builder, formatText);
      }

      Text next = this.getNext();
      if (next != null) {
         ((AbstractText)next).appendPlainText(builder, formatText);
      }

   }

   public boolean isPlainText() {
      Text child = this.getChild();
      if (child != null && !child.isPlainText()) {
         return false;
      } else {
         Text next = this.getNext();
         return next == null || next.isPlainText();
      }
   }

   public boolean isPlainTextEmpty() {
      Text child = this.getChild();
      if (child != null && !child.isPlainTextEmpty()) {
         return false;
      } else {
         Text next = this.getNext();
         return next == null || next.isPlainTextEmpty();
      }
   }

   public String toUnformattedText() {
      String unformatted = ChatColor.stripColor(this.toPlainText());
      return (String)Unsafe.assertNonNull(unformatted);
   }

   static {
      TEMP_ARGUMENTS = MessageArguments.ofMap(TEMP_ARGUMENTS_MAP);
   }
}
