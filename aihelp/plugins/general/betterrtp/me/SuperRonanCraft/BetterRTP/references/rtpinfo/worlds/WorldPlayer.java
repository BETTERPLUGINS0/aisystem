package me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds;

import java.util.ArrayList;
import java.util.List;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.commands.RTP_SETUP_TYPE;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTPSetupInformation;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_PlayerInfo;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_SHAPE;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_TYPE;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WorldPlayer implements RTPWorld, RTPWorld_Defaulted {
   private boolean useWorldborder;
   private boolean RTPOnDeath;
   private int CenterX;
   private int CenterZ;
   private int maxRad;
   private int minRad;
   private int price;
   private int min_y;
   private int max_y;
   private long cooldown;
   private List<String> Biomes;
   private final Player player;
   private final CommandSender sendi;
   private final RTP_PlayerInfo playerInfo;
   private final RTP_TYPE rtp_type;
   private final World world;
   private WORLD_TYPE world_type;
   public WorldPermissionGroup config = null;
   private RTP_SHAPE shape;
   public RTP_SETUP_TYPE setup_type;
   public String setup_name;
   private boolean setup;

   public WorldPlayer(RTPSetupInformation setup_info) {
      this.setup_type = RTP_SETUP_TYPE.DEFAULT;
      this.setup = false;
      this.sendi = setup_info.getSender();
      this.player = setup_info.getPlayer();
      this.world = setup_info.getWorld();
      this.rtp_type = setup_info.getRtp_type();
      this.playerInfo = setup_info.getPlayerInfo();
   }

   public void setup(String setup_name, RTPWorld world, List<String> biomes) {
      if (world instanceof WorldLocation) {
         this.setup_type = RTP_SETUP_TYPE.LOCATION;
      } else if (world instanceof WorldCustom) {
         this.setup_type = RTP_SETUP_TYPE.CUSTOM_WORLD;
      } else if (world instanceof WorldPermissionGroup) {
         this.setup_type = RTP_SETUP_TYPE.PERMISSIONGROUP;
      }

      this.setup_name = setup_name;
      this.setUseWorldBorder(world.getUseWorldborder());
      this.setRTPOnDeath(world.getRTPOnDeath());
      this.setCenterX(world.getCenterX());
      this.setCenterZ(world.getCenterZ());
      this.setMaxRadius(world.getMaxRadius());
      this.setMinRadius(world.getMinRadius());
      this.setShape(world.getShape());
      if (world instanceof WorldDefault) {
         this.setPrice(((WorldDefault)world).getPrice(this.getWorld().getName()));
      } else {
         this.setPrice(world.getPrice());
      }

      List<String> list = new ArrayList(world.getBiomes());
      if (biomes != null) {
         list.clear();
         list.addAll(biomes);
      }

      this.setBiomes(list);
      if (this.getUseWorldborder()) {
         WorldBorder border = this.getWorld().getWorldBorder();
         int _borderRad = (int)border.getSize() / 2;
         if (this.getMaxRadius() > _borderRad) {
            this.setMaxRadius(_borderRad);
         }

         this.setCenterX(border.getCenter().getBlockX());
         this.setCenterZ(border.getCenter().getBlockZ());
      }

      if (this.getMaxRadius() <= this.getMinRadius()) {
         this.setMinRadius(BetterRTP.getInstance().getRTP().getRTPdefaultWorld().getMinRadius());
         if (this.getMaxRadius() <= this.getMinRadius()) {
            this.setMinRadius(0);
         }
      }

      this.setMinY(world.getMinY());
      this.setMaxY(world.getMaxY());
      this.setCooldown(world.getCooldown());
      this.setup = true;
   }

   public static boolean checkIsValid(Location loc, RTPWorld rtpWorld) {
      if (loc.getWorld() != rtpWorld.getWorld()) {
         return false;
      } else {
         int _xLMax = rtpWorld.getCenterX() - rtpWorld.getMaxRadius();
         int _xLMin = rtpWorld.getCenterX() - rtpWorld.getMinRadius();
         int _xRMax = rtpWorld.getCenterX() + rtpWorld.getMaxRadius();
         int _xRMin = rtpWorld.getCenterX() + rtpWorld.getMinRadius();
         int _xLoc = loc.getBlockX();
         if (_xLoc >= _xLMax && (_xLoc <= _xLMin || _xLoc >= _xRMin) && _xLoc <= _xRMax) {
            int _zLMax = rtpWorld.getCenterZ() - rtpWorld.getMaxRadius();
            int _zLMin = rtpWorld.getCenterZ() - rtpWorld.getMinRadius();
            int _zRMax = rtpWorld.getCenterZ() + rtpWorld.getMaxRadius();
            int _zRMin = rtpWorld.getCenterZ() + rtpWorld.getMinRadius();
            int _zLoc = loc.getBlockX();
            return _zLoc >= _zLMax && (_zLoc <= _zLMin || _zLoc >= _zRMin) && _zLoc <= _zRMax;
         } else {
            return false;
         }
      }
   }

   @NotNull
   public World getWorld() {
      return this.world;
   }

   public boolean getUseWorldborder() {
      return this.useWorldborder;
   }

   public int getCenterX() {
      return this.CenterX;
   }

   public int getCenterZ() {
      return this.CenterZ;
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
      return this.Biomes;
   }

   public RTP_SHAPE getShape() {
      return this.shape;
   }

   public void setUseWorldBorder(boolean bool) {
      this.useWorldborder = bool;
   }

   public void setRTPOnDeath(boolean bool) {
      this.RTPOnDeath = bool;
   }

   public void setCenterX(int x) {
      this.CenterX = x;
   }

   public void setCenterZ(int z) {
      this.CenterZ = z;
   }

   public void setMaxRadius(int max) {
      this.maxRad = max;
   }

   public void setMinRadius(int min) {
      this.minRad = min;
   }

   public void setPrice(int price) {
      this.price = price;
   }

   public void setBiomes(List<String> biomes) {
      this.Biomes = biomes;
   }

   public void setWorld(World value) {
   }

   public void setWorldtype(WORLD_TYPE type) {
      this.world_type = type;
   }

   public void setShape(RTP_SHAPE shape) {
      this.shape = shape;
   }

   public void setMinY(int value) {
      this.min_y = value;
   }

   public void setMaxY(int value) {
      this.max_y = value;
   }

   public void setCooldown(long value) {
      this.cooldown = value;
   }

   public WorldPermissionGroup getConfig() {
      return this.config;
   }

   public WORLD_TYPE getWorldtype() {
      return this.world_type;
   }

   public int getMinY() {
      return this.min_y;
   }

   public int getMaxY() {
      return this.max_y;
   }

   public long getCooldown() {
      return this.cooldown;
   }

   public boolean getRTPOnDeath() {
      return this.RTPOnDeath;
   }

   public Player getPlayer() {
      return this.player;
   }

   public CommandSender getSendi() {
      return this.sendi;
   }

   public RTP_PlayerInfo getPlayerInfo() {
      return this.playerInfo;
   }

   public RTP_TYPE getRtp_type() {
      return this.rtp_type;
   }

   public boolean isSetup() {
      return this.setup;
   }
}
