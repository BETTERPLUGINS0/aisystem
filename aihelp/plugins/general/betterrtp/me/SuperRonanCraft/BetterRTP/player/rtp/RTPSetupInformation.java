package me.SuperRonanCraft.BetterRTP.player.rtp;

import java.util.List;
import lombok.NonNull;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldLocation;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class RTPSetupInformation {
   private World world;
   @NonNull
   private final CommandSender sender;
   @Nullable
   private final Player player;
   private final boolean personalized;
   @Nullable
   private List<String> biomes;
   @Nullable
   private WorldLocation location;
   @Nullable
   private final RTP_TYPE rtp_type;
   private final RTP_PlayerInfo playerInfo;

   public RTPSetupInformation(@Nullable World world, @NonNull CommandSender sender, @Nullable Player player, boolean personalized) {
      this(world, sender, player, personalized, (List)null, false, (RTP_TYPE)null, (WorldLocation)null);
      if (sender == null) {
         throw new NullPointerException("sender is marked non-null but is null");
      }
   }

   public RTPSetupInformation(@Nullable World world, @NonNull CommandSender sender, @Nullable Player player, boolean personalized, @Nullable List<String> biomes, boolean delay, @Nullable RTP_TYPE rtp_type, @Nullable WorldLocation location) {
      this(world, sender, player, personalized, biomes, delay, rtp_type, location, true);
      if (sender == null) {
         throw new NullPointerException("sender is marked non-null but is null");
      }
   }

   public RTPSetupInformation(@Nullable World world, @NonNull CommandSender sender, @Nullable Player player, boolean personalized, @Nullable List<String> biomes, boolean delay, @Nullable RTP_TYPE rtp_type, @Nullable WorldLocation location, boolean cooldown) {
      this(world, sender, player, personalized, biomes, rtp_type, location, new RTP_PlayerInfo(delay, cooldown));
      if (sender == null) {
         throw new NullPointerException("sender is marked non-null but is null");
      }
   }

   public RTPSetupInformation(@Nullable World world, @NonNull CommandSender sender, @Nullable Player player, boolean personalized, @Nullable List<String> biomes, @Nullable RTP_TYPE rtp_type, @Nullable WorldLocation location, RTP_PlayerInfo playerInfo) {
      if (sender == null) {
         throw new NullPointerException("sender is marked non-null but is null");
      } else {
         this.world = world;
         this.sender = sender;
         this.player = player;
         this.personalized = personalized;
         this.biomes = biomes;
         this.rtp_type = rtp_type;
         this.location = location;
         if (this.world == null && player != null) {
            this.world = player.getWorld();
         }

         this.playerInfo = playerInfo;
      }
   }

   public World getWorld() {
      return this.world;
   }

   public void setWorld(World world) {
      this.world = world;
   }

   @NonNull
   public CommandSender getSender() {
      return this.sender;
   }

   @Nullable
   public Player getPlayer() {
      return this.player;
   }

   public boolean isPersonalized() {
      return this.personalized;
   }

   @Nullable
   public List<String> getBiomes() {
      return this.biomes;
   }

   public void setBiomes(@Nullable List<String> biomes) {
      this.biomes = biomes;
   }

   @Nullable
   public WorldLocation getLocation() {
      return this.location;
   }

   public void setLocation(@Nullable WorldLocation location) {
      this.location = location;
   }

   @Nullable
   public RTP_TYPE getRtp_type() {
      return this.rtp_type;
   }

   public RTP_PlayerInfo getPlayerInfo() {
      return this.playerInfo;
   }
}
