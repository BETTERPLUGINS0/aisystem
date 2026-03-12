package me.SuperRonanCraft.BetterRTP.player.rtp.effects;

import java.util.HashMap;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import me.SuperRonanCraft.BetterRTP.references.messages.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RTPEffect_Titles {
   boolean enabled = false;
   private final HashMap<RTPEffect_Titles.RTP_TITLE_TYPE, RTPEffect_Titles.RTP_TITLE> titles = new HashMap();

   void load() {
      this.titles.clear();
      FileOther.FILETYPE config = FileOther.FILETYPE.EFFECTS;
      this.enabled = config.getBoolean("Titles.Enabled");
      if (this.enabled) {
         RTPEffect_Titles.RTP_TITLE_TYPE[] var2 = RTPEffect_Titles.RTP_TITLE_TYPE.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            RTPEffect_Titles.RTP_TITLE_TYPE type = var2[var4];
            this.titles.put(type, new RTPEffect_Titles.RTP_TITLE(type.path));
         }
      }

   }

   public void showTitle(RTPEffect_Titles.RTP_TITLE_TYPE type, Player p, Location loc, int attempts, int delay) {
      if (this.titles.containsKey(type)) {
         String title = this.getPlaceholders(((RTPEffect_Titles.RTP_TITLE)this.titles.get(type)).title, p, loc, attempts, delay);
         String sub = this.getPlaceholders(((RTPEffect_Titles.RTP_TITLE)this.titles.get(type)).subTitle, p, loc, attempts, delay);
         this.show(p, title, sub);
      }

   }

   public boolean sendMsg(RTPEffect_Titles.RTP_TITLE_TYPE type) {
      return this.titles.containsKey(type) && ((RTPEffect_Titles.RTP_TITLE)this.titles.get(type)).send_message || !this.enabled;
   }

   private String getPlaceholders(String str, Player p, Location loc, int attempts, int delay) {
      return str.replace("%player%", p.getName()).replace("%x%", String.valueOf(loc.getBlockX())).replace("%y%", String.valueOf(loc.getBlockY())).replace("%z%", String.valueOf(loc.getBlockZ())).replace("%attempts%", String.valueOf(attempts)).replace("%time%", String.valueOf(delay));
   }

   private void show(Player p, String title, String sub) {
      title = Message.color(title);
      sub = Message.color(sub);
      p.sendTitle(title, sub);
   }

   public static enum RTP_TITLE_TYPE {
      NODELAY("NoDelay"),
      TELEPORT("Teleport"),
      DELAY("Delay"),
      CANCEL("Cancelled"),
      LOADING("Loading"),
      FAILED("Failed");

      final String path;

      private RTP_TITLE_TYPE(String path) {
         this.path = path;
      }

      // $FF: synthetic method
      private static RTPEffect_Titles.RTP_TITLE_TYPE[] $values() {
         return new RTPEffect_Titles.RTP_TITLE_TYPE[]{NODELAY, TELEPORT, DELAY, CANCEL, LOADING, FAILED};
      }
   }

   private static class RTP_TITLE {
      String title;
      String subTitle;
      boolean send_message;

      RTP_TITLE(String path) {
         FileOther.FILETYPE config = FileOther.FILETYPE.EFFECTS;
         this.title = config.getString("Titles." + path + ".Title");
         this.subTitle = config.getString("Titles." + path + ".Subtitle");
         this.send_message = config.getBoolean("Titles." + path + ".SendMessage");
      }
   }
}
