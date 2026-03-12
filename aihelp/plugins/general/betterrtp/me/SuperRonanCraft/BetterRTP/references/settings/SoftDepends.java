package me.SuperRonanCraft.BetterRTP.references.settings;

import java.util.logging.Level;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins.REGIONPLUGINS;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import org.bukkit.Bukkit;

public class SoftDepends {
   void load() {
      REGIONPLUGINS[] var1 = REGIONPLUGINS.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         REGIONPLUGINS plugin = var1[var3];
         this.registerPlugin(plugin);
      }

   }

   public void registerPlugin(REGIONPLUGINS pl) {
      FileOther.FILETYPE config = BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.CONFIG);
      String pre = "Settings.Respect.";
      pl.getPlugin().setRespecting(config.getBoolean(pre + pl.getSetting_name()));
      if (pl.getPlugin().isRespecting()) {
         pl.getPlugin().setEnabled(Bukkit.getPluginManager().isPluginEnabled(pl.getPluginyml_name()));
      }

      if (pl.getPlugin().isRespecting()) {
         this.debug("Respecting `" + pl.getSetting_name() + "` was " + (pl.getPlugin().enabled ? "SUCCESSFULLY" : "NOT") + " registered");
      }

   }

   private void debug(String str) {
      if (BetterRTP.getInstance().getSettings().isDebug()) {
         BetterRTP.getInstance().getLogger().log(Level.INFO, str);
      }

   }

   public static class RegionPlugin {
      private boolean respecting;
      private boolean enabled;

      public boolean isRespecting() {
         return this.respecting;
      }

      public void setRespecting(boolean respecting) {
         this.respecting = respecting;
      }

      public boolean isEnabled() {
         return this.enabled;
      }

      public void setEnabled(boolean enabled) {
         this.enabled = enabled;
      }
   }
}
