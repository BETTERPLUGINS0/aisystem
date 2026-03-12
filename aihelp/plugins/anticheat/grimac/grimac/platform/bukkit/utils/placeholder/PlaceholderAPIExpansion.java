package ac.grim.grimac.platform.bukkit.utils.placeholder;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderAPIExpansion extends PlaceholderExpansion {
   @NotNull
   public String getIdentifier() {
      return "grim";
   }

   @NotNull
   public String getAuthor() {
      return String.join(", ", GrimAPI.INSTANCE.getGrimPlugin().getDescription().getAuthors());
   }

   @NotNull
   public String getVersion() {
      return GrimAPI.INSTANCE.getExternalAPI().getGrimVersion();
   }

   public boolean persist() {
      return true;
   }

   @NotNull
   public List<String> getPlaceholders() {
      Set<String> staticReplacements = GrimAPI.INSTANCE.getExternalAPI().getStaticReplacements().keySet();
      Set<String> variableReplacements = GrimAPI.INSTANCE.getExternalAPI().getVariableReplacements().keySet();
      ArrayList<String> placeholders = new ArrayList(staticReplacements.size() + variableReplacements.size());
      Iterator var4 = staticReplacements.iterator();

      String s;
      while(var4.hasNext()) {
         s = (String)var4.next();
         placeholders.add(s.equals("%grim_version%") ? s : "%grim_" + s.replaceAll("%", "") + "%");
      }

      var4 = variableReplacements.iterator();

      while(var4.hasNext()) {
         s = (String)var4.next();
         placeholders.add(s.equals("%player%") ? "%grim_player%" : "%grim_player_" + s.replaceAll("%", "") + "%");
      }

      return placeholders;
   }

   public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
      Iterator var3 = GrimAPI.INSTANCE.getExternalAPI().getStaticReplacements().entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, String> entry = (Entry)var3.next();
         String key = ((String)entry.getKey()).equals("%grim_version%") ? "version" : ((String)entry.getKey()).replaceAll("%", "");
         if (params.equalsIgnoreCase(key)) {
            return (String)entry.getValue();
         }
      }

      if (offlinePlayer instanceof Player) {
         Player player = (Player)offlinePlayer;
         GrimPlayer grimPlayer = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(player.getUniqueId());
         if (grimPlayer == null) {
            return null;
         }

         Iterator var10 = GrimAPI.INSTANCE.getExternalAPI().getVariableReplacements().entrySet().iterator();

         while(var10.hasNext()) {
            Entry<String, Function<GrimUser, String>> entry = (Entry)var10.next();
            String key = ((String)entry.getKey()).equals("%player%") ? "player" : "player_" + ((String)entry.getKey()).replaceAll("%", "");
            if (params.equalsIgnoreCase(key)) {
               return (String)((Function)entry.getValue()).apply(grimPlayer);
            }
         }
      }

      return null;
   }
}
