package com.nisovin.shopkeepers.spigot.text;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.debug.Debug;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.spigot.SpigotFeatures;
import com.nisovin.shopkeepers.text.ClickEventText;
import com.nisovin.shopkeepers.text.FormattingText;
import com.nisovin.shopkeepers.text.HoverEventText;
import com.nisovin.shopkeepers.text.InsertionText;
import com.nisovin.shopkeepers.text.PlaceholderText;
import com.nisovin.shopkeepers.text.PlainText;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.text.TranslatableText;
import com.nisovin.shopkeepers.util.bukkit.RegistryUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Entity;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SpigotText {
   public static void sendMessage(CommandSender recipient, Text message) {
      Validate.notNull(recipient, (String)"recipient is null");
      Validate.notNull(message, (String)"message is null");
      if (!message.isPlainTextEmpty()) {
         if (SpigotFeatures.isSpigotAvailable()) {
            SpigotText.Internal.sendMessage(recipient, message);
         } else {
            String plainMessage = message.toPlainText();
            TextUtils.sendMessage(recipient, plainMessage);
         }

      }
   }

   private SpigotText() {
   }

   private static final class Internal {
      public static void sendMessage(CommandSender recipient, Text message) {
         assert recipient != null && message != null;

         BaseComponent component = toSpigot(message);
         if (Debug.isDebugging(DebugOptions.textComponents)) {
            Log.info("Text: " + String.valueOf(message));
            Log.info("Plain text: " + message.toPlainText());
            Log.info("Format: " + message.toFormat());
            Log.info("Component: " + String.valueOf(component));
            Bukkit.getConsoleSender().spigot().sendMessage(component);
         }

         recipient.spigot().sendMessage(component);
      }

      private static BaseComponent toSpigot(Text text) {
         assert text != null;

         BaseComponent root = new TextComponent();
         toSpigot(text, (TextComponent)null, root, new SpigotText.Internal.TextStyle());
         return root;
      }

      private static BaseComponent toSpigot(Text text, TextComponent previous, BaseComponent parent, SpigotText.Internal.TextStyle textStyle) {
         assert text != null && parent != null && textStyle != null;

         TextComponent current = previous;
         boolean ignoreChild = false;
         Object component;
         if (text instanceof FormattingText) {
            String formattingCode = ((FormattingText)text).getFormattingCode();
            ChatColor chatColor = toSpigotChatColor(formattingCode);
            if (chatColor == null) {
               if (previous == null || hasText(previous) || hasExtra(previous)) {
                  current = newTextComponent(parent, textStyle);
               }

               current.setText(((FormattingText)text).toPlainText());
               component = current;
            } else {
               textStyle.setFormatting(chatColor);
               if (previous != null && !hasText(previous) && !hasExtra(previous) && chatColor != ChatColor.RESET) {
                  textStyle.applyTo(previous);
               } else {
                  current = newTextComponent(parent, textStyle);
               }

               component = current;
            }
         } else if (text instanceof PlainText) {
            if (previous == null || hasText(previous) || hasExtra(previous)) {
               current = newTextComponent(parent, textStyle);
            }

            current.setText(((PlainText)text).getText());
            component = current;
         } else if (text instanceof PlaceholderText) {
            PlaceholderText placeholderText = (PlaceholderText)text;
            if (placeholderText.hasPlaceholderArgument()) {
               if (previous == null) {
                  current = newTextComponent(parent, textStyle);
               }
            } else {
               if (previous == null || hasText(previous) || hasExtra(previous)) {
                  current = newTextComponent(parent, textStyle);
               }

               current.setText(placeholderText.getFormattedPlaceholderKey());
            }

            component = current;
         } else if (text instanceof HoverEventText) {
            if (previous == null || hasText(previous) || hasExtra(previous)) {
               current = newTextComponent(parent, textStyle);
            }

            current.setHoverEvent(toSpigot((HoverEventText)text));
            component = current;
         } else if (text instanceof ClickEventText) {
            if (previous == null || hasText(previous) || hasExtra(previous)) {
               current = newTextComponent(parent, textStyle);
            }

            current.setClickEvent(toSpigot((ClickEventText)text));
            component = current;
         } else if (text instanceof InsertionText) {
            if (previous == null || hasText(previous) || hasExtra(previous)) {
               current = newTextComponent(parent, textStyle);
            }

            current.setInsertion(((InsertionText)text).getInsertion());
            component = current;
         } else {
            if (!(text instanceof TranslatableText)) {
               throw new IllegalArgumentException("Unknown type of Text: " + text.getClass().getName());
            }

            TranslatableText translatableText = (TranslatableText)text;
            String translationKey = translatableText.getTranslationKey();

            assert translationKey != null;

            List<? extends Text> translationArgs = translatableText.getTranslationArguments();

            assert translationArgs != null;

            Object[] spigotTranslationArgs = new Object[translationArgs.size()];

            for(int i = 0; i < translationArgs.size(); ++i) {
               spigotTranslationArgs[i] = toSpigot((Text)translationArgs.get(i));
            }

            component = new TranslatableComponent(translationKey, spigotTranslationArgs);
            parent.addExtra((BaseComponent)component);
            textStyle.applyTo((BaseComponent)component);
            current = null;
            ignoreChild = true;
         }

         assert component != null;

         Text child = text.getChild();
         if (!ignoreChild && child != null) {
            toSpigot(child, current, (BaseComponent)component, textStyle);
         }

         Text next = text.getNext();
         if (next != null) {
            toSpigot(next, current, parent, textStyle);
         }

         return (BaseComponent)component;
      }

      private static TextComponent newTextComponent(BaseComponent parent, SpigotText.Internal.TextStyle textStyle) {
         assert parent != null && textStyle != null;

         TextComponent component = new TextComponent();
         parent.addExtra(component);
         textStyle.applyTo(component);
         return component;
      }

      private static boolean hasText(TextComponent component) {
         assert component != null;

         return !StringUtils.isEmpty(component.getText());
      }

      private static boolean hasExtra(BaseComponent component) {
         assert component != null;

         List<BaseComponent> extra = (List)Unsafe.cast(component.getExtra());
         return extra != null && !extra.isEmpty();
      }

      @Nullable
      private static ChatColor toSpigotChatColor(String formattingCode) {
         assert formattingCode != null;

         if (formattingCode.length() == 1) {
            char formattingChar = Character.toLowerCase(formattingCode.charAt(0));
            return ChatColor.getByChar(formattingChar);
         } else {
            try {
               return ChatColor.of(formattingCode);
            } catch (IllegalArgumentException var2) {
               return null;
            }
         }
      }

      private static HoverEvent toSpigot(HoverEventText hoverEvent) {
         assert hoverEvent != null;

         HoverEventText.Content hoverEventContent = hoverEvent.getContent();
         Action action;
         if (hoverEventContent instanceof HoverEventText.TextContent) {
            HoverEventText.TextContent textContent = (HoverEventText.TextContent)hoverEventContent;
            action = Action.SHOW_TEXT;
            BaseComponent[] value = new BaseComponent[]{toSpigot(textContent.getText())};
            net.md_5.bungee.api.chat.hover.content.Text content = new net.md_5.bungee.api.chat.hover.content.Text(value);
            return new HoverEvent(action, new Content[]{content});
         } else if (hoverEventContent instanceof HoverEventText.ItemContent) {
            HoverEventText.ItemContent itemContent = (HoverEventText.ItemContent)hoverEventContent;
            ItemStack item = ItemUtils.asItemStack(itemContent.getItem());
            Action action = Action.SHOW_ITEM;
            Item content = new Item(RegistryUtils.getKeyOrThrow(item.getType()).toString(), item.getAmount(), (ItemTag)null);
            return new HoverEvent(action, new Content[]{content});
         } else if (hoverEventContent instanceof HoverEventText.EntityContent) {
            HoverEventText.EntityContent entityContent = (HoverEventText.EntityContent)hoverEventContent;
            action = Action.SHOW_ENTITY;
            Text nameText = entityContent.getName();
            Entity content = new Entity(entityContent.getType().toString(), entityContent.getUuid().toString(), nameText == null ? null : toSpigot(nameText));
            return new HoverEvent(action, new Content[]{content});
         } else {
            throw new IllegalStateException("Unexpected hover event content: " + String.valueOf(hoverEventContent));
         }
      }

      private static ClickEvent toSpigot(ClickEventText clickEvent) {
         assert clickEvent != null;

         net.md_5.bungee.api.chat.ClickEvent.Action action = toSpigot(clickEvent.getAction());
         return new ClickEvent(action, clickEvent.getValue());
      }

      private static net.md_5.bungee.api.chat.ClickEvent.Action toSpigot(ClickEventText.Action clickEventAction) {
         assert clickEventAction != null;

         switch(clickEventAction) {
         case OPEN_URL:
            return net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL;
         case OPEN_FILE:
            return net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_FILE;
         case RUN_COMMAND:
            return net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND;
         case SUGGEST_COMMAND:
            return net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND;
         case CHANGE_PAGE:
            return net.md_5.bungee.api.chat.ClickEvent.Action.CHANGE_PAGE;
         default:
            throw new IllegalStateException("Unexpected click event action: " + String.valueOf(clickEventAction));
         }
      }

      private static final class TextStyle {
         @Nullable
         private ChatColor color = null;
         @Nullable
         private Boolean bold = null;
         @Nullable
         private Boolean italic = null;
         @Nullable
         private Boolean underlined = null;
         @Nullable
         private Boolean strikethrough = null;
         @Nullable
         private Boolean obfuscated = null;

         public void setFormatting(ChatColor formatting) {
            assert formatting != null;

            if (formatting.getColor() != null) {
               this.reset();
               this.color = formatting;
            } else if (formatting == ChatColor.BOLD) {
               this.bold = true;
            } else if (formatting == ChatColor.ITALIC) {
               this.italic = true;
            } else if (formatting == ChatColor.UNDERLINE) {
               this.underlined = true;
            } else if (formatting == ChatColor.STRIKETHROUGH) {
               this.strikethrough = true;
            } else if (formatting == ChatColor.MAGIC) {
               this.obfuscated = true;
            } else if (formatting == ChatColor.RESET) {
               this.reset();
            } else {
               Log.warning("Unexpected Text formatting: " + String.valueOf(formatting));
            }

         }

         private void reset() {
            this.color = null;
            this.bold = null;
            this.italic = null;
            this.underlined = null;
            this.strikethrough = null;
            this.obfuscated = null;
         }

         public void applyTo(BaseComponent component) {
            assert component != null;

            component.setColor((ChatColor)Unsafe.nullableAsNonNull(this.color));
            component.setBold((Boolean)Unsafe.nullableAsNonNull(this.bold));
            component.setItalic((Boolean)Unsafe.nullableAsNonNull(this.italic));
            component.setUnderlined((Boolean)Unsafe.nullableAsNonNull(this.underlined));
            component.setStrikethrough((Boolean)Unsafe.nullableAsNonNull(this.strikethrough));
            component.setObfuscated((Boolean)Unsafe.nullableAsNonNull(this.obfuscated));
         }
      }
   }
}
