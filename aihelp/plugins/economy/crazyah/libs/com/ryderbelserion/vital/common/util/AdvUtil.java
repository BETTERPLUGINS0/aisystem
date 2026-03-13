package libs.com.ryderbelserion.vital.common.util;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class AdvUtil {
   private AdvUtil() {
      throw new AssertionError();
   }

   @NotNull
   public static Component parse(@NotNull String message) {
      return (Component)(message.isEmpty() ? Component.empty() : MiniMessage.miniMessage().deserialize(message).decorationIfAbsent(TextDecoration.ITALIC, State.FALSE));
   }

   @NotNull
   public static Component toComponent(@NotNull String component) {
      return LegacyComponentSerializer.legacyAmpersand().deserialize(component.replace("§", "&"));
   }

   @NotNull
   public static List<Component> toComponent(@NotNull final List<String> lore) {
      return new ArrayList<Component>(lore.size()) {
         {
            lore.forEach((line) -> {
               this.add(AdvUtil.toComponent(line));
            });
         }
      };
   }

   @NotNull
   public static List<String> fromComponent(@NotNull List<Component> lore) {
      return fromComponent(lore, false);
   }

   @NotNull
   public static List<String> fromComponent(@NotNull final List<Component> lore, final boolean isMessage) {
      return new ArrayList<String>(lore.size()) {
         {
            lore.forEach((line) -> {
               this.add(AdvUtil.fromComponent(line, isMessage));
            });
         }
      };
   }

   @NotNull
   public static String fromComponent(@NotNull Component component) {
      return fromComponent(component, false);
   }

   @NotNull
   public static String fromComponent(@NotNull Component component, boolean isMessage) {
      String value = (String)MiniMessage.miniMessage().serialize(component);
      return isMessage ? value.replace("\\<", "<") : value;
   }

   @NotNull
   public static String convert(@NotNull String component) {
      return convert(component, false);
   }

   @NotNull
   public static List<String> convert(@NotNull List<String> components) {
      return convert(components, false);
   }

   @NotNull
   public static List<String> convert(@NotNull final List<String> components, final boolean isMessage) {
      return new ArrayList<String>(components.size()) {
         {
            components.forEach((line) -> {
               this.add(AdvUtil.convert(line, isMessage));
            });
         }
      };
   }

   @NotNull
   public static String convert(@NotNull String component, boolean isMessage) {
      return fromComponent(toComponent(component), isMessage);
   }
}
