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

public class WorldCustom implements RTPWorld, RTPWorld_Defaulted {
   public World world;
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
   private RTP_SHAPE shape;

   public WorldCustom(World world) {
      FileOther.FILETYPE config = BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.CONFIG);
      List<Map<?, ?>> map = config.getMapList("CustomWorlds");
      this.world = world;
      this.setupDefaults();
      Iterator var4 = map.iterator();

      label148:
      while(var4.hasNext()) {
         Map<?, ?> m = (Map)var4.next();
         Iterator var6 = m.entrySet().iterator();

         while(true) {
            Map test;
            do {
               do {
                  do {
                     String key;
                     do {
                        if (!var6.hasNext()) {
                           continue label148;
                        }

                        Entry<?, ?> entry = (Entry)var6.next();
                        key = entry.getKey().toString();
                     } while(!key.equals(world.getName()));

                     test = (Map)m.get(key);
                  } while(test == null);

                  if (test.get("UseWorldBorder") != null && test.get("UseWorldBorder").getClass() == Boolean.class) {
                     this.useWorldborder = Boolean.parseBoolean(test.get("UseWorldBorder").toString());
                     BetterRTP.debug("- UseWorldBorder: " + this.useWorldborder);
                  }

                  if (test.get("CenterX") != null && test.get("CenterX").getClass() == Integer.class) {
                     this.centerX = Integer.parseInt(test.get("CenterX").toString());
                     BetterRTP.debug("- CenterX: " + this.centerX);
                  }

                  if (test.get("CenterZ") != null && test.get("CenterZ").getClass() == Integer.class) {
                     this.centerZ = Integer.parseInt(test.get("CenterZ").toString());
                     BetterRTP.debug("- CenterZ: " + this.centerZ);
                  }

                  if (test.get("MaxRadius") != null) {
                     if (test.get("MaxRadius").getClass() == Integer.class) {
                        this.maxRad = Integer.parseInt(test.get("MaxRadius").toString());
                        BetterRTP.debug("- MaxRadius: " + this.maxRad);
                     }

                     if (this.maxRad <= 0) {
                        Message_RTP.sms(Bukkit.getConsoleSender(), "WARNING! Custom world '" + world + "' Maximum radius of '" + this.maxRad + "' is not allowed! Set to default value!");
                        this.maxRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMaxRadius();
                     }
                  }

                  if (test.get("MinRadius") != null) {
                     if (test.get("MinRadius").getClass() == Integer.class) {
                        this.minRad = Integer.parseInt(test.get("MinRadius").toString());
                        BetterRTP.debug("- MinRadius: " + this.minRad);
                     }

                     if (this.minRad < 0 || this.minRad >= this.maxRad) {
                        Message_RTP.sms(Bukkit.getConsoleSender(), "WARNING! Custom world '" + world + "' Minimum radius of '" + this.minRad + "' is not allowed! Set to default value!");
                        this.minRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMinRadius();
                        if (this.minRad >= this.maxRad) {
                           this.maxRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMaxRadius();
                        }
                     }
                  }

                  if (test.get("Biomes") != null && test.get("Biomes").getClass() == ArrayList.class) {
                     this.biomes = new ArrayList((ArrayList)test.get("Biomes"));
                     BetterRTP.debug("- Biomes: " + this.biomes);
                  }

                  if (BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.ECO).getBoolean("Economy.Enabled") && test.get("Price") != null) {
                     if (test.get("Price").getClass() == Integer.class) {
                        this.price = Integer.parseInt(test.get("Price").toString());
                     }

                     BetterRTP.debug("- Price: " + this.price);
                  }

                  if (test.get("Shape") != null && test.get("Shape").getClass() == String.class) {
                     try {
                        this.shape = RTP_SHAPE.valueOf(test.get("Shape").toString().toUpperCase());
                        BetterRTP.debug("- Shape: " + this.shape);
                     } catch (Exception var11) {
                     }
                  }

                  if (test.get("MinY") != null && test.get("MinY").getClass() == Integer.class) {
                     this.miny = Integer.parseInt(test.get("MinY").toString());
                     BetterRTP.debug("- MinY: " + this.miny);
                  }

                  if (test.get("MaxY") != null && test.get("MaxY").getClass() == Integer.class) {
                     this.maxy = Integer.parseInt(test.get("MaxY").toString());
                     BetterRTP.debug("- MaxY: " + this.maxy);
                  }
               } while(test.get("Cooldown") == null);
            } while(test.get("Cooldown").getClass() != Integer.class && test.get("Cooldown").getClass() != Long.class);

            this.cooldown = Long.parseLong(test.get("Cooldown").toString());
            BetterRTP.debug("- Cooldown: " + this.cooldown);
         }
      }

      if (this.maxRad <= 0) {
         Message_RTP.sms(Bukkit.getConsoleSender(), "WARNING! Custom world '" + world + "' Maximum radius of '" + this.maxRad + "' is not allowed! Set to default value!");
         this.maxRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMaxRadius();
      }

      if (this.minRad < 0 || this.minRad >= this.maxRad) {
         Message_RTP.sms(Bukkit.getConsoleSender(), "WARNING! Custom world '" + world + "' Minimum radius of '" + this.minRad + "' is not allowed! Set to default value!");
         this.minRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMinRadius();
      }

   }

   public WorldCustom(World world, RTPWorld rtpWorld) {
      this.setAllFrom(rtpWorld);
      this.world = world;
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

   @NotNull
   public World getWorld() {
      return this.world;
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
