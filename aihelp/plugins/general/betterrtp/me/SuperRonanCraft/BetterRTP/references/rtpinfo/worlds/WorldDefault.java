package me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_SHAPE;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class WorldDefault implements RTPWorld {
   private boolean useWorldborder;
   private boolean RTPOnDeath;
   private int centerX;
   private int centerZ;
   private int maxRad;
   private int minRad;
   private int price;
   private int miny;
   private int maxy;
   private List<String> Biomes;
   private final HashMap<String, Integer> prices = new HashMap();
   private RTP_SHAPE shape;

   public void load() {
      BetterRTP.debug("Loading Defaults...");
      String pre = "Default";
      FileOther.FILETYPE config = BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.CONFIG);
      this.useWorldborder = config.getBoolean(pre + ".UseWorldBorder");
      this.RTPOnDeath = config.getBoolean(pre + ".RTPOnDeath");
      this.centerX = config.getInt(pre + ".CenterX");
      this.centerZ = config.getInt(pre + ".CenterZ");
      this.maxRad = config.getInt(pre + ".MaxRadius");

      try {
         this.shape = RTP_SHAPE.valueOf(config.getString(pre + ".Shape").toUpperCase());
      } catch (Exception var9) {
         this.shape = RTP_SHAPE.SQUARE;
      }

      if (this.maxRad <= 0) {
         BetterRTP.getInstance().getLogger().warning("WARNING! Default Maximum radius of '" + this.maxRad + "' is not allowed! Value set to '1000'");
         this.maxRad = 1000;
      }

      this.minRad = config.getInt(pre + ".MinRadius");
      if (this.minRad < 0 || this.minRad >= this.maxRad) {
         BetterRTP.getInstance().getLogger().warning("The Default MinRadius of '" + this.minRad + "' is not allowed! Value set to '0'");
         this.minRad = 0;
      }

      this.prices.clear();
      if (BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.ECO).getBoolean("Economy.Enabled")) {
         this.price = BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.ECO).getInt("Economy.Price");
         if (BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.ECO).getBoolean("CustomWorlds.Enabled")) {
            List<Map<?, ?>> world_map = BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.ECO).getMapList("CustomWorlds.Prices");
            Iterator var4 = world_map.iterator();

            while(var4.hasNext()) {
               Map<?, ?> m = (Map)var4.next();
               Iterator var6 = m.entrySet().iterator();

               while(var6.hasNext()) {
                  Entry<?, ?> entry = (Entry)var6.next();
                  String _world = entry.getKey().toString();
                  if (entry.getValue().getClass() == Integer.class) {
                     this.prices.put(_world, Integer.parseInt(entry.getValue().toString()));
                  }
               }
            }
         }
      } else {
         this.price = 0;
      }

      this.Biomes = config.getStringList(pre + ".Biomes");
      this.miny = config.getInt(pre + ".MinY");
      if (this.miny > 0) {
         this.miny = 0;
         BetterRTP.getInstance().getLogger().warning("Warning! Default MinY value is solely for 1.17+ support, and can only be negative!");
      }

      this.maxy = config.getInt(pre + ".MaxY");
      if (this.maxy < 64) {
         this.maxy = 320;
         BetterRTP.getInstance().getLogger().warning("Warning! Default MaxY value is below water level (64)! Reset to default 320!");
      }

      if (BetterRTP.getInstance().getSettings().isDebug()) {
         Logger log = BetterRTP.getInstance().getLogger();
         log.info("- UseWorldBorder: " + this.useWorldborder);
         log.info("- CenterX: " + this.centerX);
         log.info("- CenterZ: " + this.centerZ);
         log.info("- MaxRadius: " + this.maxRad);
         log.info("- MinRadius: " + this.minRad);
         log.info("- Price: " + this.price);
         log.info("- MinY: " + this.miny);
         log.info("- MaxY: " + this.maxy);
         log.info("- Cooldown (default): " + this.getCooldown());
      }

   }

   public boolean getUseWorldborder() {
      return this.useWorldborder;
   }

   public int getCenterX() {
      return this.centerX;
   }

   public int getCenterZ() {
      return this.centerZ;
   }

   public int getMaxRadius() {
      return this.maxRad;
   }

   public int getMinRadius() {
      return this.minRad;
   }

   public int getPrice(String world) {
      return (Integer)this.prices.getOrDefault(world, this.getPrice());
   }

   public int getPrice() {
      return this.price;
   }

   public List<String> getBiomes() {
      return this.Biomes;
   }

   @NotNull
   public World getWorld() {
      return null;
   }

   public RTP_SHAPE getShape() {
      return this.shape;
   }

   public int getMinY() {
      return this.miny;
   }

   public int getMaxY() {
      return this.maxy;
   }

   public long getCooldown() {
      return (long)BetterRTP.getInstance().getCooldowns().getDefaultCooldownTime();
   }

   public boolean getRTPOnDeath() {
      return this.RTPOnDeath;
   }
}
