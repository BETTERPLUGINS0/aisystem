package me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import lombok.NonNull;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_SHAPE;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import me.SuperRonanCraft.BetterRTP.references.messages.Message_RTP;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class WorldPermissionGroup implements RTPWorld, RTPWorld_Defaulted {
   private boolean useWorldborder;
   private boolean RTPOnDeath;
   private int centerX;
   private int centerZ;
   private int maxRad;
   private int minRad;
   private int price;
   private int miny;
   private int maxy;
   private List<String> biomes;
   public World world;
   private RTP_SHAPE shape;
   private int priority;
   private final String groupName;
   private long cooldown;

   public WorldPermissionGroup(String group, World world, Entry fields) {
      this.groupName = group;
      this.world = world;
      this.setupDefaults();
      this.priority = 0;
      Iterator var4 = ((HashMap)fields.getValue()).entrySet().iterator();

      while(var4.hasNext()) {
         Object hash2 = var4.next();
         Entry hash3 = (Entry)hash2;
         String field = hash3.getKey().toString();
         if (field.equalsIgnoreCase("Priority") && hash3.getValue().getClass() == Integer.class) {
            this.priority = Integer.parseInt(hash3.getValue().toString());
            BetterRTP.debug("- - Priority: " + this.priority);
         }

         if (field.equalsIgnoreCase("UseWorldBorder") && hash3.getValue().getClass() == Boolean.class) {
            this.useWorldborder = Boolean.parseBoolean(hash3.getValue().toString());
            BetterRTP.debug("- - UseWorldBorder: " + this.useWorldborder);
         }

         if (field.equalsIgnoreCase("CenterX") && hash3.getValue().getClass() == Integer.class) {
            this.centerX = Integer.parseInt(hash3.getValue().toString());
            BetterRTP.debug("- - CenterX: " + this.centerX);
         }

         if (field.equalsIgnoreCase("CenterZ") && hash3.getValue().getClass() == Integer.class) {
            this.centerZ = Integer.parseInt(hash3.getValue().toString());
            BetterRTP.debug("- - CenterZ: " + this.centerZ);
         }

         if (field.equalsIgnoreCase("MaxRadius")) {
            if (hash3.getValue().getClass() == Integer.class) {
               this.maxRad = Integer.parseInt(hash3.getValue().toString());
               BetterRTP.debug("- - MaxRadius: " + this.maxRad);
            }

            if (this.maxRad <= 0) {
               Message_RTP.sms(Bukkit.getConsoleSender(), "WARNING! Group '" + group + "' Maximum radius of '" + this.maxRad + "' is not allowed! Set to default value!");
               this.maxRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMaxRadius();
            }
         }

         if (field.equalsIgnoreCase("MinRadius")) {
            if (hash3.getValue().getClass() == Integer.class) {
               this.minRad = Integer.parseInt(hash3.getValue().toString());
               BetterRTP.debug("- - MinRadius: " + this.minRad);
            }

            if (this.minRad < 0 || this.minRad >= this.maxRad) {
               Message_RTP.sms(Bukkit.getConsoleSender(), "WARNING! Group '" + group + "' Minimum radius of '" + this.minRad + "' is not allowed! Set to default value!");
               this.minRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMinRadius();
               if (this.minRad >= this.maxRad) {
                  this.maxRad = BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMaxRadius();
               }
            }
         }

         if (field.equalsIgnoreCase("Biomes") && hash3.getValue().getClass() == ArrayList.class) {
            this.biomes = new ArrayList((ArrayList)hash3.getValue());
            BetterRTP.debug("- - Biomes: " + this.biomes);
         }

         if (FileOther.FILETYPE.ECO.getBoolean("Economy.Enabled") && field.equalsIgnoreCase("Price") && hash3.getValue().getClass() == Integer.class) {
            this.price = Integer.parseInt(hash3.getValue().toString());
            BetterRTP.debug("- - Price: " + this.price);
         }

         if (field.equalsIgnoreCase("Shape") && hash3.getValue().getClass() == String.class) {
            try {
               this.shape = RTP_SHAPE.valueOf(hash3.getValue().toString().toUpperCase());
               BetterRTP.debug("- - Shape: " + this.shape.name());
            } catch (Exception var9) {
               BetterRTP.debug("- - Shape: (INVALID) " + hash3.getValue().toString());
            }
         }

         if (field.equalsIgnoreCase("MinY") && hash3.getValue().getClass() == Integer.class) {
            this.miny = Integer.parseInt(hash3.getValue().toString());
            BetterRTP.debug("- - MinY: " + this.miny);
         }

         if (field.equalsIgnoreCase("MaxY") && hash3.getValue().getClass() == Integer.class) {
            this.maxy = Integer.parseInt(hash3.getValue().toString());
            BetterRTP.debug("- - MaxY: " + this.maxy);
         }

         if (field.equalsIgnoreCase("Cooldown") && (hash3.getValue().getClass() == Integer.class || hash3.getValue().getClass() == Long.class)) {
            this.cooldown = Long.parseLong(hash3.getValue().toString());
            BetterRTP.debug("- - Cooldown: " + this.cooldown);
         }

         if (field.equalsIgnoreCase("RTPOnDeath") && hash3.getValue().getClass() == Boolean.class) {
            this.RTPOnDeath = Boolean.parseBoolean(hash3.getValue().toString());
            BetterRTP.debug("- - RTPOnDeath: " + this.RTPOnDeath);
         }
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

   public int getPrice() {
      return this.price;
   }

   public List<String> getBiomes() {
      return this.biomes;
   }

   @NotNull
   @NonNull
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

   public int getPriority() {
      return this.priority;
   }

   public String getGroupName() {
      return this.groupName;
   }
}
