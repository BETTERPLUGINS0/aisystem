package emanondev.itemedit.compability;

import emanondev.itemedit.ItemEdit;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.Builder;
import org.bukkit.Bukkit;

public class MiniMessagePaper implements MiniMessageUtil {
   private static final LegacyComponentSerializer UNGLY_LEGACY = ((Builder)LegacyComponentSerializer.legacySection().toBuilder()).hexColors().useUnusualXRepeatedCharacterHexFormat().build();
   private static final MiniMessagePaper instance = new MiniMessagePaper();

   public String fromMiniToText(String text) {
      if (text != null && !text.isEmpty()) {
         try {
            return UNGLY_LEGACY.serialize(MiniMessage.miniMessage().deserialize(text.replace("§", "&")));
         } catch (NoSuchMethodError var3) {
            if (Bukkit.getPluginManager().isPluginEnabled("SCore")) {
               ItemEdit.get().log("SCore is disabling MiniMessage compability?");
            } else {
               var3.printStackTrace();
            }

            return text;
         } catch (Throwable var4) {
            var4.printStackTrace();
            return text;
         }
      } else {
         return text;
      }
   }

   public static MiniMessagePaper getInstance() {
      return instance;
   }
}
