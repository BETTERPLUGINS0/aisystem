package me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_SHAPE;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import me.SuperRonanCraft.BetterRTP.references.messages.Message_RTP;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldLocation implements RTPWorld, RTPWorld_Defaulted {
   private boolean useWorldborder;
   private boolean RTPOnDeath;
   private int centerX;
   private int centerZ;
   private int maxRad;
   private int minRad;
   private int price;
   private int miny;
   private int maxy;
   private long cooldown;
   private List<String> biomes;
   private World world;
   private RTP_SHAPE shape;
   private final String name;

   public WorldLocation(String location_name) {
      FileOther.FILETYPE config = BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.LOCATIONS);
      List<Map<?, ?>> map = config.getMapList("Locations");
      this.setupDefaults();
      this.name = location_name;
      BetterRTP.debug("- Loading Location " + location_name + ":");
      Iterator var4 = map.iterator();

      label165:
      while(var4.hasNext()) {
         Map<?, ?> m = (Map)var4.next();
         Iterator var6 = m.entrySet().iterator();

         while(true) {
            Map section;
            do {
               String key;
               do {
                  if (!var6.hasNext()) {
                     continue label165;
                  }

                  Entry<?, ?> entry = (Entry)var6.next();
                  key = entry.getKey().toString();
               } while(!key.equals(location_name));

               section = (Map)m.get(key);
            } while(section == null);

            if (section.get("World") != null && section.get("World").getClass() == String.class) {
               this.world = Bukkit.getWorld(section.get("World").toString());
               BetterRTP.debug("- - World: " + this.world.getName());
            }

            if (this.world == null) {
               BetterRTP.getInstance().getLogger().warning("Location `" + location_name + "` does NOT have a `World` or world doesnt exist!");
               return;
            }

            if (section.get("UseWorldBorder") != null && section.get("UseWorldBorder").getClass() == Boolean.class) {
               this.useWorldborder = Boolean.parseBoolean(section.get("UseWorldBorder").toString());
               BetterRTP.debug("- - UseWorldBorder: " + this.useWorldborder);
            }

            if (section.get("CenterX") != null && section.get("CenterX").getClass() == Integer.class) {
               this.centerX = Integer.parseInt(section.get("CenterX").toString());
               BetterRTP.debug("- - World: " + this.centerX);
            }

            if (section.get("CenterZ") != null && section.get("CenterZ").getClass() == Integer.class) {
               this.centerZ = Integer.parseInt(section.get("CenterZ").toString());
               BetterRTP.debug("- - CenterZ: " + this.centerZ);
            }

            if (section.get("MaxRadius") != null) {
               if (section.get("MaxRadius").getClass() == Integer.class) {
                  this.maxRad = Integer.parseInt(section.get("MaxRadius").toString());
               }

               if (this.maxRad <= 0) {
                  Message_RTP.sms(Bukkit.getConsoleSender(), "WARNING! Location '" + location_name + "' Maximum radius of '" + this.maxRad + "' is not allowed! Set to default value!");
                  this.maxRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMaxRadius();
               }

               BetterRTP.debug("- - MaxRadius: " + this.maxRad);
            }

            if (section.get("MinRadius") != null) {
               if (section.get("MinRadius").getClass() == Integer.class) {
                  this.minRad = Integer.parseInt(section.get("MinRadius").toString());
               }

               if (this.minRad < 0 || this.minRad >= this.maxRad) {
                  Message_RTP.sms(Bukkit.getConsoleSender(), "WARNING! Location '" + location_name + "' Minimum radius of '" + this.minRad + "' is not allowed! Set to default value!");
                  this.minRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMinRadius();
                  if (this.minRad >= this.maxRad) {
                     this.maxRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMaxRadius();
                     BetterRTP.debug("- ! MaxRadius: " + this.maxRad);
                  }
               }

               BetterRTP.debug("- - MinRad: " + this.minRad);
            }

            if (section.get("Biomes") != null && section.get("Biomes").getClass() == ArrayList.class) {
               this.biomes = new ArrayList((ArrayList)section.get("Biomes"));
               BetterRTP.debug("- - Biomes: " + this.biomes);
            }

            if (BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.ECO).getBoolean("Economy.Enabled") && section.get("Price") != null && section.get("Price").getClass() == Integer.class) {
               this.price = Integer.parseInt(section.get("Price").toString());
               BetterRTP.debug("- - Price: " + this.price);
            }

            if (section.get("Shape") != null && section.get("Shape").getClass() == String.class) {
               try {
                  this.shape = RTP_SHAPE.valueOf(section.get("Shape").toString().toUpperCase());
                  BetterRTP.debug("- - Shape: " + this.shape.name());
               } catch (Exception var12) {
               }
            }

            if (section.get("UseWorldBorder") != null && section.get("UseWorldBorder").getClass() == Boolean.class) {
               try {
                  this.useWorldborder = Boolean.parseBoolean(section.get("UseWorldBorder").toString());
                  BetterRTP.debug("- - UseWorldBorder: " + this.useWorldborder);
               } catch (Exception var11) {
               }
            }

            if (section.get("MinY") != null && section.get("MinY").getClass() == Integer.class) {
               this.miny = Integer.parseInt(section.get("MinY").toString());
               BetterRTP.debug("- - MinY: " + this.miny);
            }

            if (section.get("MaxY") != null && section.get("MaxY").getClass() == Integer.class) {
               this.maxy = Integer.parseInt(section.get("MaxY").toString());
               BetterRTP.debug("- - MaxY: " + this.maxy);
            }

            if (section.get("Cooldown") != null && (section.get("Cooldown").getClass() == Integer.class || section.get("Cooldown").getClass() == Long.class)) {
               this.cooldown = Long.parseLong(section.get("Cooldown").toString());
               BetterRTP.debug("- - Cooldown: " + this.cooldown);
            }

            if (section.get("RTPOnDeath") != null && section.get("RTPOnDeath").getClass() == Boolean.class) {
               this.RTPOnDeath = Boolean.parseBoolean(section.get("RTPOnDeath").toString());
               BetterRTP.debug("- - RTPOnDeath: " + this.RTPOnDeath);
            }
         }
      }

   }

   public boolean isValid() {
      return this.world != null;
   }

   @NotNull
   public World getWorld() {
      return this.world;
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

   public int getPrice() {
      return this.price;
   }

   public List<String> getBiomes() {
      return this.biomes;
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

   @Nullable
   public String getID() {
      return this.name;
   }

   public long getCooldown() {
      return this.cooldown;
   }

   public boolean getRTPOnDeath() {
      return this.RTPOnDeath;
   }

   public void setUseWorldBorder(boolean value) {
      this.useWorldborder = value;
   }

   public void setCenterX(int value) {
      this.centerX = value;
   }

   public void setCenterZ(int value) {
      this.centerZ = value;
   }

   public void setMaxRadius(int value) {
      this.maxRad = value;
   }

   public void setMinRadius(int value) {
      this.minRad = value;
   }

   public void setPrice(int value) {
      this.price = value;
   }

   public void setBiomes(List<String> value) {
      this.biomes = value;
   }

   public void setWorld(World value) {
      this.world = value;
   }

   public void setShape(RTP_SHAPE value) {
      this.shape = value;
   }

   public void setMinY(int value) {
      this.miny = value;
   }

   public void setMaxY(int value) {
      this.maxy = value;
   }

   public void setCooldown(long value) {
      this.cooldown = value;
   }

   public void setRTPOnDeath(boolean bool) {
      this.RTPOnDeath = bool;
   }
}
