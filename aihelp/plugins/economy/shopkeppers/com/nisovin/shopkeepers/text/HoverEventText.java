package com.nisovin.shopkeepers.text;

import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.text.MessageArguments;
import java.util.UUID;
import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.Nullable;

public class HoverEventText extends TextBuilder {
   private final HoverEventText.Content content;

   HoverEventText(HoverEventText.Content content) {
      Validate.notNull(content, (String)"content is null");
      this.content = content;
   }

   public Text build() {
      super.build();
      return this;
   }

   public HoverEventText.Content getContent() {
      return this.content;
   }

   public Text setPlaceholderArguments(MessageArguments arguments) {
      super.setPlaceholderArguments(arguments);
      HoverEventText.Content var3 = this.content;
      if (var3 instanceof HoverEventText.TextContent) {
         HoverEventText.TextContent textContent = (HoverEventText.TextContent)var3;
         textContent.getText().setPlaceholderArguments(arguments);
      }

      return this;
   }

   public Text clearPlaceholderArguments() {
      super.clearPlaceholderArguments();
      HoverEventText.Content var2 = this.content;
      if (var2 instanceof HoverEventText.TextContent) {
         HoverEventText.TextContent textContent = (HoverEventText.TextContent)var2;
         textContent.getText().clearPlaceholderArguments();
      }

      return this;
   }

   public boolean isPlainText() {
      return false;
   }

   public Text copy() {
      HoverEventText copy = new HoverEventText(this.content.copy());
      copy.copy(this, true);
      return copy.build();
   }

   protected void appendToStringFeatures(StringBuilder builder) {
      builder.append(", content=");
      builder.append(this.getContent());
      super.appendToStringFeatures(builder);
   }

   public interface Content {
      HoverEventText.Content copy();
   }

   public static class TextContent implements HoverEventText.Content {
      private final Text text;

      public TextContent(Text text) {
         Validate.notNull(text, (String)"text is null");
         TextBuilder.buildIfRequired(text);
         this.text = text;
      }

      public Text getText() {
         return this.text;
      }

      public HoverEventText.Content copy() {
         return new HoverEventText.TextContent(this.text.copy());
      }

      public String toString() {
         StringBuilder builder = new StringBuilder();
         builder.append("TextContent [text=");
         builder.append(this.text);
         builder.append("]");
         return builder.toString();
      }
   }

   public static class EntityContent implements HoverEventText.Content {
      private final NamespacedKey type;
      private final UUID uuid;
      @Nullable
      private final Text name;

      public EntityContent(NamespacedKey type, UUID uuid, @Nullable Text name) {
         Validate.notNull(type, (String)"type is null");
         Validate.notNull(uuid, (String)"uuid is null");
         TextBuilder.buildIfRequired(name);
         this.type = type;
         this.uuid = uuid;
         this.name = name;
      }

      public NamespacedKey getType() {
         return this.type;
      }

      public UUID getUuid() {
         return this.uuid;
      }

      @Nullable
      public Text getName() {
         return this.name;
      }

      public HoverEventText.Content copy() {
         return this;
      }

      public String toString() {
         StringBuilder builder = new StringBuilder();
         builder.append("EntityContent [type=");
         builder.append(this.type);
         builder.append(", uuid=");
         builder.append(this.uuid);
         builder.append(", name=");
         builder.append(this.name);
         builder.append("]");
         return builder.toString();
      }
   }

   public static class ItemContent implements HoverEventText.Content {
      private final UnmodifiableItemStack item;

      public ItemContent(UnmodifiableItemStack item) {
         Validate.notNull(item, (String)"item is null");
         this.item = item;
      }

      public UnmodifiableItemStack getItem() {
         return this.item;
      }

      public HoverEventText.Content copy() {
         return this;
      }

      public String toString() {
         StringBuilder builder = new StringBuilder();
         builder.append("ItemContent [item=");
         builder.append(this.item.getType());
         builder.append("]");
         return builder.toString();
      }
   }
}
