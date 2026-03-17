package com.bergerkiller.bukkit.tc.attachments;

import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.wrappers.ChatText;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.generated.com.mojang.authlib.GameProfileHandle;
import com.bergerkiller.generated.com.mojang.authlib.properties.PropertyHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawnHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeamHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacketHandle.EnumPlayerInfoActionHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacketHandle.PlayerInfoDataHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public enum FakePlayerSpawner {
   NORMAL((String)null, (String)null, false),
   NO_NAMETAG("DinnerBone", "BoredTCRiders", true),
   NO_NAMETAG_RANDOM("DinnerBone", "BoredTCRandoms", true),
   NO_NAMETAG_SECONDARY("DinnarBone", "BoredTCRiders2", true),
   NO_NAMETAG_TERTIARY("DinnarBone", "BoredTCRiders3", true),
   UPSIDEDOWN("Dinnerbone", "DizzyTCRiders", true);

   private static final Map<UUID, FakePlayerSpawner.ProfileState> _dummyProfileStates = new HashMap();
   private static final Map<UUID, Map<UUID, FakePlayerSpawner.ProfileState>> _profileStates = new HashMap();
   private static final int TAB_LIST_CLEANUP_DELAY = 5;
   private final String _playerName;
   private final ChatText _teamName;
   private final boolean _hideNametag;
   private final Set<UUID> _teamSentPlayers = new HashSet();

   private FakePlayerSpawner(String playerName, String teamName, boolean hideNametag) {
      this._playerName = playerName;
      this._teamName = ChatText.fromMessage(teamName);
      this._hideNametag = hideNametag;
   }

   public ChatText getPlayerName() {
      return ChatText.fromMessage(this._playerName);
   }

   public void spawnPlayer(Player viewer, Player player, int entityId, FakePlayerSpawner.FakePlayerPosition position, Consumer<DataWatcher> metaFunction) {
      this.spawnPlayer(AttachmentViewer.fallback(viewer), player, entityId, position, metaFunction);
   }

   public void spawnPlayer(AttachmentViewer viewer, Player player, int entityId, FakePlayerSpawner.FakePlayerPosition position, Consumer<DataWatcher> metaFunction) {
      this.spawnPlayerSimple(viewer, player, entityId, (fakePlayerSpawnPacket) -> {
         fakePlayerSpawnPacket.setPosX(position.getX());
         fakePlayerSpawnPacket.setPosY(position.getY());
         fakePlayerSpawnPacket.setPosZ(position.getZ());
         fakePlayerSpawnPacket.setYaw(position.getYaw());
         fakePlayerSpawnPacket.setPitch(position.getPitch());
      }, metaFunction);
      CommonPacket headPacket = PacketType.OUT_ENTITY_HEAD_ROTATION.newInstance();
      headPacket.write(PacketType.OUT_ENTITY_HEAD_ROTATION.entityId, entityId);
      headPacket.write(PacketType.OUT_ENTITY_HEAD_ROTATION.headYaw, position.getHeadYaw());
      viewer.send(headPacket);
   }

   public void spawnPlayerSimple(Player viewer, Player player, int entityId, Consumer<PacketPlayOutNamedEntitySpawnHandle> applier, Consumer<DataWatcher> metaFunction) {
      this.spawnPlayerSimple(AttachmentViewer.fallback(viewer), player, entityId, applier, metaFunction);
   }

   public void spawnPlayerSimple(AttachmentViewer viewer, Player player, int entityId, Consumer<PacketPlayOutNamedEntitySpawnHandle> applier, Consumer<DataWatcher> metaFunction) {
      FakePlayerSpawner.ProfileState state = this.getProfileState(player, viewer.getPlayer());
      UUID uuidOfFakePlayer = this.sendPlayerProfileInfo(viewer, player, state);
      PacketPlayOutNamedEntitySpawnHandle fakePlayerSpawnPacket = PacketPlayOutNamedEntitySpawnHandle.createNew();
      fakePlayerSpawnPacket.setEntityId(entityId);
      fakePlayerSpawnPacket.setEntityUUID(uuidOfFakePlayer);
      applier.accept(fakePlayerSpawnPacket);
      DataWatcher metaData = player == null ? new DataWatcher() : EntityUtil.getDataWatcher(player).clone();
      metaFunction.accept(metaData);
      viewer.sendNamedEntitySpawnPacket(fakePlayerSpawnPacket, metaData);
   }

   private UUID sendPlayerProfileInfo(AttachmentViewer viewer, Player player, FakePlayerSpawner.ProfileState state) {
      if (this == NORMAL && player != null) {
         return player.getUniqueId();
      } else {
         UUID uuid = state.getUUID(this);
         state.runAndClearCleanupTasksFor(viewer, uuid);
         GameProfileHandle newFakeGameProfile;
         ChatText playerListName;
         if (player == null) {
            newFakeGameProfile = createDummyPlayerProfile(uuid, this._playerName);
            playerListName = ChatText.fromMessage("Dummy");
         } else {
            newFakeGameProfile = GameProfileHandle.createNew(uuid, this._playerName).withPropertiesOf(GameProfileHandle.getForPlayer(player));
            playerListName = ChatText.fromMessage(player.getPlayerListName());
         }

         ClientboundPlayerInfoUpdatePacketHandle newInfoPacket = ClientboundPlayerInfoUpdatePacketHandle.createNew();
         newInfoPacket.setAction(EnumPlayerInfoActionHandle.ADD_PLAYER);
         PlayerInfoDataHandle playerInfo = PlayerInfoDataHandle.createNew(newInfoPacket, newFakeGameProfile, 50, GameMode.CREATIVE, playerListName, false);
         newInfoPacket.getPlayers().add(playerInfo);
         viewer.send((PacketHandle)newInfoPacket);
         if (this._hideNametag && this._teamName != null && this._teamSentPlayers.add(viewer.getPlayer().getUniqueId())) {
            PacketPlayOutScoreboardTeamHandle teamPacket = PacketPlayOutScoreboardTeamHandle.createNew();
            teamPacket.setMethod(0);
            teamPacket.setName(this._teamName.getMessage());
            teamPacket.setDisplayName(this._teamName);
            teamPacket.setPrefix(ChatText.fromMessage(""));
            teamPacket.setSuffix(ChatText.fromMessage(""));
            teamPacket.setVisibility("never");
            teamPacket.setCollisionRule("never");
            teamPacket.setTeamOptionFlags(3);
            teamPacket.setPlayers(new ArrayList(Collections.singleton(this._playerName)));
            teamPacket.setColor(ChatColor.RESET);
            viewer.send((PacketHandle)teamPacket);
         }

         state.scheduleCleanupTask(viewer, uuid);
         return uuid;
      }
   }

   private final FakePlayerSpawner.ProfileState getProfileState(Player player, Player viewer) {
      return player == null ? (FakePlayerSpawner.ProfileState)_dummyProfileStates.computeIfAbsent(viewer.getUniqueId(), (uuid) -> {
         return new FakePlayerSpawner.ProfileState(true);
      }) : (FakePlayerSpawner.ProfileState)((Map)_profileStates.computeIfAbsent(player.getUniqueId(), (uuid) -> {
         return new HashMap(1);
      })).computeIfAbsent(viewer.getUniqueId(), (uuid) -> {
         return new FakePlayerSpawner.ProfileState(false);
      });
   }

   public static void onViewerQuit(Player viewer) {
      _profileStates.remove(viewer.getUniqueId());
      FakePlayerSpawner[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         FakePlayerSpawner modifier = var1[var3];
         modifier._teamSentPlayers.remove(viewer.getUniqueId());
      }

   }

   public static void runAndClearCleanupTasks() {
      _profileStates.values().stream().flatMap((e) -> {
         return e.values().stream();
      }).forEach(FakePlayerSpawner.ProfileState::runAndClearCleanupTasks);
      _dummyProfileStates.values().forEach(FakePlayerSpawner.ProfileState::runAndClearCleanupTasks);
   }

   private static UUID generateNPCUUID() {
      UUID uuid = UUID.randomUUID();
      return new UUID(uuid.getMostSignificantBits() & -61441L | 8192L, uuid.getLeastSignificantBits());
   }

   public static GameProfileHandle createDummyPlayerProfile() {
      return createDummyPlayerProfile(generateNPCUUID(), "Dummy");
   }

   public static GameProfileHandle createDummyPlayerProfile(UUID uuid, String playerName) {
      return GameProfileHandle.createNew(uuid, playerName).withPropertyPut("textures", PropertyHandle.createNew("textures", "ewogICJ0aW1lc3RhbXAiIDogMTY0Mjc4NTAwMzQ3NywKICAicHJvZmlsZUlkIiA6ICIwNjNhMTc2Y2RkMTU0ODRiYjU1MjRhNjQyMGM1YjdhNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJkYXZpcGF0dXJ5IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2YzMzBlNjk1OTc4ZTgyZDE1M2IxZmRhMWM1NjE2OTA3NGUyNzZlNTMzODY2ZGE3OWFkZDQzZDIwMTczNzUxYWUiCiAgICB9CiAgfQp9", "s1s8RzOgcymEF56ow13Vvw0UfvhJG1PY7Qh5A5kpBi5uscbwaI/ib3QfK5wll4Gge06JreHAGbHIiTx1jAX17ciJQHhWvSF3/VnnnZaLEyXo3xWaOwFEIedGgUeqv9RigMJbJKvLqA0hQ1ezhwTGylQCLhz5Pxrsqtj+x6sozqRmL6YvLm+xTwAH2r5bj5luRrakgRYpG5kOh2ykYGwL4PEgU1yaZB7pcpnRfwOX2a/qm2e0l9RGDAW1X36fJ9w/kUzPVZSD9yXMu4XX6NVXn1fmhFeezfqVEtbQTozCVoEbLh3828rY+P7U5b8GfdHWM9hs5Ukc7dcLfzcwPU2bRTfvT0t95BdKI5P9bDlchqBGQFNQ49ii9dwZ4+JxLBTWQT/7/X5XsfpNKl96GVnPfVZ49hczx6O923XdD3j7MknDC11ZA8KGo03nNmz2cPqLKUfhyqmSCvQA70A9DBKP4Ys35I3HkiS3Qxrd6bdNtrixys7oJmGA8MUf1tyDW2w9tq3S5+nHLUnMahhqSzToznIt3cu+OVEbjVbaM9LGj5VK3H7M3brkb1C4jRRYL3Pia3cck9BtLEvx42gjCfA2rqXR6YxOMcq3GuunwtC1oRfrocRzW73qg0gCDVHCAdcfazmKWwCh3h6dvxOy7GaXKQaNKKdF1rxfsvYT+8zLwOQ="));
   }

   // $FF: synthetic method
   private static FakePlayerSpawner[] $values() {
      return new FakePlayerSpawner[]{NORMAL, NO_NAMETAG, NO_NAMETAG_RANDOM, NO_NAMETAG_SECONDARY, NO_NAMETAG_TERTIARY, UPSIDEDOWN};
   }

   public static class FakePlayerPosition {
      private final double x;
      private final double y;
      private final double z;
      private final float yaw;
      private final float pitch;
      private final float headyaw;

      private FakePlayerPosition(double x, double y, double z, float yaw, float pitch, float headyaw) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.yaw = yaw;
         this.pitch = pitch;
         this.headyaw = headyaw;
      }

      public double getX() {
         return this.x;
      }

      public double getY() {
         return this.y;
      }

      public double getZ() {
         return this.z;
      }

      public float getYaw() {
         return this.yaw;
      }

      public float getPitch() {
         return this.pitch;
      }

      public float getHeadYaw() {
         return this.headyaw;
      }

      public FakePlayerSpawner.FakePlayerPosition atOppositePitchBoundary() {
         return create(this.x, this.y, this.z, this.yaw, Util.atOppositeRotationGlitchBoundary(this.pitch), this.headyaw);
      }

      public static FakePlayerSpawner.FakePlayerPosition ofPlayer(Player player) {
         EntityHandle playerHandle = EntityHandle.fromBukkit(player);
         return new FakePlayerSpawner.FakePlayerPosition(playerHandle.getLocX(), playerHandle.getLocY(), playerHandle.getLocZ(), playerHandle.getYaw(), playerHandle.getPitch(), playerHandle.getHeadRotation());
      }

      public static FakePlayerSpawner.FakePlayerPosition ofPlayer(double x, double y, double z, Player player) {
         EntityHandle playerHandle = EntityHandle.fromBukkit(player);
         return new FakePlayerSpawner.FakePlayerPosition(x, y, z, playerHandle.getYaw(), playerHandle.getPitch(), playerHandle.getHeadRotation());
      }

      public static FakePlayerSpawner.FakePlayerPosition ofPlayerUpsideDown(double x, double y, double z, Player player) {
         EntityHandle playerHandle = EntityHandle.fromBukkit(player);
         float yaw = playerHandle.getYaw();
         return new FakePlayerSpawner.FakePlayerPosition(x, y, z, yaw, -playerHandle.getPitch(), -playerHandle.getHeadRotation() + 2.0F * yaw);
      }

      public static FakePlayerSpawner.FakePlayerPosition create(double x, double y, double z, float yaw, float pitch, float headyaw) {
         return new FakePlayerSpawner.FakePlayerPosition(x, y, z, yaw, pitch, headyaw);
      }
   }

   private static class ProfileState {
      private final UUID npcUUID;
      private final UUID npcUUID2;
      private final UUID npcUUID3;
      public final List<FakePlayerSpawner.CleanupPlayerListEntryTask> pendingCleanup;

      public ProfileState(boolean dummy) {
         this.npcUUID = dummy ? null : FakePlayerSpawner.generateNPCUUID();
         this.npcUUID2 = dummy ? null : FakePlayerSpawner.generateNPCUUID();
         this.npcUUID3 = dummy ? null : FakePlayerSpawner.generateNPCUUID();
         this.pendingCleanup = new ArrayList();
      }

      public UUID getUUID(FakePlayerSpawner type) {
         return type != FakePlayerSpawner.NO_NAMETAG_RANDOM && this.npcUUID != null ? (type == FakePlayerSpawner.NO_NAMETAG_TERTIARY ? this.npcUUID3 : (type == FakePlayerSpawner.NO_NAMETAG_SECONDARY ? this.npcUUID2 : this.npcUUID)) : FakePlayerSpawner.generateNPCUUID();
      }

      public void scheduleCleanupTask(AttachmentViewer viewer, UUID playerUUID) {
         Iterator iter = this.pendingCleanup.iterator();

         FakePlayerSpawner.CleanupPlayerListEntryTask task;
         while(iter.hasNext()) {
            task = (FakePlayerSpawner.CleanupPlayerListEntryTask)iter.next();
            if (task.playerUUID.equals(playerUUID)) {
               task.stop();
               iter.remove();
            }
         }

         task = new FakePlayerSpawner.CleanupPlayerListEntryTask(TrainCarts.plugin, this, viewer, playerUUID);
         this.pendingCleanup.add(task);
         task.start(5L, 1L);
      }

      public void runAndClearCleanupTasksFor(AttachmentViewer viewer, UUID uuid) {
         Iterator iter = this.pendingCleanup.iterator();

         while(iter.hasNext()) {
            FakePlayerSpawner.CleanupPlayerListEntryTask task = (FakePlayerSpawner.CleanupPlayerListEntryTask)iter.next();
            if (task.viewer.equals(viewer) && uuid.equals(task.playerUUID)) {
               iter.remove();
               task.finish();
            }
         }

      }

      public void runAndClearCleanupTasks() {
         if (!this.pendingCleanup.isEmpty()) {
            List<FakePlayerSpawner.CleanupPlayerListEntryTask> all = new ArrayList(this.pendingCleanup);
            this.pendingCleanup.clear();
            Iterator var2 = all.iterator();

            while(var2.hasNext()) {
               FakePlayerSpawner.CleanupPlayerListEntryTask task = (FakePlayerSpawner.CleanupPlayerListEntryTask)var2.next();
               task.finish();
            }
         }

      }
   }

   private static class CleanupPlayerListEntryTask extends Task {
      private final FakePlayerSpawner.ProfileState state;
      private final AttachmentViewer viewer;
      private final long runWhen;
      public final UUID playerUUID;

      public CleanupPlayerListEntryTask(JavaPlugin plugin, FakePlayerSpawner.ProfileState state, AttachmentViewer viewer, UUID playerUUID) {
         super(plugin);
         this.state = state;
         this.viewer = viewer;
         this.playerUUID = playerUUID;
         this.runWhen = System.currentTimeMillis() + 250L;
      }

      public void run() {
         if (System.currentTimeMillis() >= this.runWhen) {
            this.finish();
         }

      }

      public void finish() {
         try {
            if (this.viewer.isConnected()) {
               this.viewer.send((PacketHandle)ClientboundPlayerInfoRemovePacketHandle.createNew(Collections.singletonList(this.playerUUID)));
            }
         } finally {
            this.stop();
            this.state.pendingCleanup.remove(this);
         }

      }
   }
}
