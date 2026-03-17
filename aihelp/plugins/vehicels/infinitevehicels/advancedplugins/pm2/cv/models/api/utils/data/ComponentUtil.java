package advancedplugins.pm2.cv.models.api.utils.data;

import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.Style.Builder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

public class ComponentUtil {
   public static Style reset() {
      return ((Builder)Style.style().decorations(Set.of(TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.OBFUSCATED, TextDecoration.STRIKETHROUGH, TextDecoration.UNDERLINED), false)).color(NamedTextColor.WHITE).build();
   }

   public static Style color(int var0) {
      return color(TextColor.color(var0));
   }

   public static Style color(TextColor var0) {
      return ((Builder)Style.style().decorations(Set.of(TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.OBFUSCATED, TextDecoration.STRIKETHROUGH, TextDecoration.UNDERLINED), false)).color(var0).build();
   }

   public static String string(Component var0) {
      return LegacyComponentSerializer.legacySection().serialize(var0);
   }

   public static BaseComponent[] base(Component var0) {
      return new BaseComponent[0];
   }

   public static void sendMessage(Player var0, Component var1) {
      var0.sendMessage(base(var1));
   }
}
