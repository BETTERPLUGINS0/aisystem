package com.bergerkiller.bukkit.tc.chest;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.config.BasicConfiguration;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import com.bergerkiller.bukkit.common.wrappers.ChatText;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartGroupStore;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableGroup;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableMember;
import com.bergerkiller.bukkit.tc.debug.DebugToolUtil;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.google.common.io.ByteStreams;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class TrainChestItemUtil {
   private static final String IDENTIFIER = "Traincarts.chest";
   private static final String TITLE = "Traincarts Chest";
   private static final boolean CAN_USE_NEW_BKCL_ITEM_APIS = Common.hasCapability("Common:CommonItemStack:AddGlint");
   private static final double AUTOCONNECT_EXTRA_DISTANCE = 1.0D;
   private static final double SPAWN_LOOKING_AT_REACH = 10.0D;

   public static ItemStack createItem() {
      CommonItemStack item = CommonItemStack.create(Material.ENDER_CHEST, 1).updateCustomData((tag) -> {
         tag.putValue("plugin", TrainCarts.plugin.getName());
         tag.putValue("identifier", "Traincarts.chest");
         tag.putValue("name", "");
         tag.putValue("parsed", false);
         tag.putValue("locked", false);
         tag.putValue("HideFlags", 1);
      }).hideAllAttributes();
      if (CAN_USE_NEW_BKCL_ITEM_APIS) {
         applyNewBKCLChanges(item);
      } else {
         item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
      }

      updateTitle(item);
      return item.toBukkit();
   }

   private static void applyNewBKCLChanges(CommonItemStack item) {
      item.addGlint().mimicAsType(MaterialUtil.getFirst(new String[]{"PAPER", "LEGACY_PAPER"}));
   }

   private static void updateTitle(CommonItemStack item) {
      String displayTitle = "Traincarts Chest";
      String name = getName(item);
      if (name.isEmpty() && !isEmpty(item) && (Boolean)item.getCustomData().getValue("parsed", false)) {
         name = (String)item.getCustomData().getValue("config", "");
      }

      if (!name.isEmpty()) {
         displayTitle = displayTitle + " (" + name + ")";
      }

      item.setCustomNameMessage(displayTitle);
      item.clearLores();
      if (isEmpty(item)) {
         item.addLore(ChatText.fromMessage(ChatColor.RED + "Empty"));
      } else if (isFiniteSpawns(item)) {
         item.addLore(ChatText.fromMessage(ChatColor.BLUE + "Single-use"));
      } else {
         item.addLore(ChatText.fromMessage(ChatColor.DARK_PURPLE + "Infinite uses"));
      }

      double speed = getSpeed(item);
      if (speed > 0.0D) {
         item.addLore(ChatText.fromMessage(ChatColor.YELLOW + "Speed " + DebugToolUtil.formatNumber(speed) + "b/t"));
      }

      if (isLocked(item)) {
         item.addLore(ChatText.fromMessage(ChatColor.RED + "Locked"));
      }

   }

   public static boolean isItem(ItemStack item) {
      return isItem(CommonItemStack.of(item));
   }

   public static boolean isItem(CommonItemStack item) {
      return !item.isEmpty() && item.hasCustomData() ? "Traincarts.chest".equals(item.getCustomData().getValue("identifier", "")) : false;
   }

   public static void setFiniteSpawns(ItemStack item, boolean finite) {
      setFiniteSpawns(CommonItemStack.of(item), finite);
   }

   public static void setFiniteSpawns(CommonItemStack item, boolean finite) {
      if (isItem(item)) {
         item.updateCustomData((tag) -> {
            tag.putValue("finite", finite);
         });
         updateTitle(item);
      }

   }

   public static void setLocked(ItemStack item, boolean locked) {
      setLocked(CommonItemStack.of(item), locked);
   }

   public static void setLocked(CommonItemStack item, boolean locked) {
      if (isItem(item)) {
         item.updateCustomData((tag) -> {
            tag.putValue("locked", locked);
         });
         updateTitle(item);
      }

   }

   public static void setSpeed(ItemStack item, double speed) {
      setSpeed(CommonItemStack.of(item), speed);
   }

   public static void setSpeed(CommonItemStack item, double speed) {
      if (isItem(item)) {
         item.updateCustomData((tag) -> {
            tag.putValue("speed", speed);
         });
         updateTitle(item);
      }

   }

   public static void setSpawnMessage(ItemStack item, String message) {
      setSpawnMessage(CommonItemStack.of(item), message);
   }

   public static void setSpawnMessage(CommonItemStack item, String message) {
      if (isItem(item)) {
         item.updateCustomData((tag) -> {
            tag.putValue("spawnMessage", message);
         });
      }

   }

   public static String getSpawnMessage(ItemStack item) {
      return getSpawnMessage(CommonItemStack.of(item));
   }

   public static String getSpawnMessage(CommonItemStack item) {
      return isItem(item) ? (String)item.getCustomData().getValue("spawnMessage", String.class, (Object)null) : null;
   }

   public static boolean isLocked(ItemStack item) {
      return isLocked(CommonItemStack.of(item));
   }

   public static boolean isLocked(CommonItemStack item) {
      return isItem(item) && (Boolean)item.getCustomData().getValue("locked", false);
   }

   public static boolean isFiniteSpawns(ItemStack item) {
      return isFiniteSpawns(CommonItemStack.of(item));
   }

   public static boolean isFiniteSpawns(CommonItemStack item) {
      return isItem(item) && (Boolean)item.getCustomData().getValue("finite", false);
   }

   public static double getSpeed(ItemStack item) {
      return getSpeed(CommonItemStack.of(item));
   }

   public static double getSpeed(CommonItemStack item) {
      return isItem(item) ? (Double)item.getCustomData().getValue("speed", 0.0D) : 0.0D;
   }

   public static void setName(ItemStack item, String name) {
      setName(CommonItemStack.of(item), name);
   }

   public static void setName(CommonItemStack item, String name) {
      if (isItem(item)) {
         item.updateCustomData((tag) -> {
            tag.putValue("name", name);
         });
         updateTitle(item);
      }

   }

   public static String getName(ItemStack item) {
      return getName(CommonItemStack.of(item));
   }

   public static String getName(CommonItemStack item) {
      return isItem(item) ? (String)item.getCustomData().getValue("name", "") : "";
   }

   public static void clear(ItemStack item) {
      clear(CommonItemStack.of(item));
   }

   public static void clear(CommonItemStack item) {
      if (isItem(item)) {
         item.updateCustomData((tag) -> {
            tag.remove("config");
         });
         updateTitle(item);
      }

   }

   public static boolean isEmpty(ItemStack item) {
      return isEmpty(CommonItemStack.of(item));
   }

   public static boolean isEmpty(CommonItemStack item) {
      return isItem(item) && !item.getCustomData().containsKey("config");
   }

   public static void playSoundStore(Player player) {
      PlayerUtil.playSound(player, SoundEffect.PISTON_CONTRACT, 0.4F, 1.5F);
   }

   public static void playSoundSpawn(Player player) {
      PlayerUtil.playSound(player, SoundEffect.PISTON_EXTEND, 0.4F, 1.5F);
   }

   public static void store(ItemStack item, String spawnPattern) {
      store(CommonItemStack.of(item), spawnPattern);
   }

   public static void store(CommonItemStack item, String spawnPattern) {
      if (isItem(item)) {
         item.updateCustomData((tag) -> {
            tag.putValue("config", spawnPattern);
            tag.putValue("parsed", true);
         });
         updateTitle(item);
      }

   }

   public static void store(ItemStack item, MinecartGroup group) {
      store(CommonItemStack.of(item), group);
   }

   public static void store(CommonItemStack item, MinecartGroup group) {
      if (group != null) {
         store(item, group.saveConfig());
      }

   }

   public static void store(ItemStack item, ConfigurationNode config) {
      store(CommonItemStack.of(item), config);
   }

   public static void store(CommonItemStack item, ConfigurationNode config) {
      if (isItem(item)) {
         item.updateCustomData((tag) -> {
            byte[] compressed = new byte[0];

            try {
               byte[] uncompressed = config.toString().getBytes("UTF-8");
               ByteArrayOutputStream byteStream = new ByteArrayOutputStream(uncompressed.length);

               try {
                  GZIPOutputStream zipStream = new GZIPOutputStream(byteStream);

                  try {
                     zipStream.write(uncompressed);
                  } catch (Throwable var10) {
                     try {
                        zipStream.close();
                     } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                     }

                     throw var10;
                  }

                  zipStream.close();
                  compressed = byteStream.toByteArray();
               } catch (Throwable var11) {
                  try {
                     byteStream.close();
                  } catch (Throwable var8) {
                     var11.addSuppressed(var8);
                  }

                  throw var11;
               }

               byteStream.close();
            } catch (Throwable var12) {
               TrainCarts.plugin.getLogger().log(Level.SEVERE, "Unhandled error saving item details to config", var12);
            }

            tag.putValue("config", compressed);
            tag.putValue("parsed", false);
         });
         updateTitle(item);
      }

   }

   public static SpawnableGroup getSpawnableGroup(TrainCarts plugin, ItemStack item) {
      return getSpawnableGroup(plugin, CommonItemStack.of(item));
   }

   public static SpawnableGroup getSpawnableGroup(TrainCarts plugin, CommonItemStack item) {
      if (!isItem(item)) {
         return null;
      } else if (isEmpty(item)) {
         return null;
      } else {
         SpawnableGroup group;
         if ((Boolean)item.getCustomData().getValue("parsed", false)) {
            group = SpawnableGroup.parse(plugin, (String)item.getCustomData().getValue("config", ""));
         } else {
            BasicConfiguration basicConfig = new BasicConfiguration();

            try {
               byte[] uncompressed = new byte[0];
               byte[] compressed = (byte[])item.getCustomData().getValue("config", new byte[0]);
               if (compressed != null && compressed.length > 0) {
                  ByteArrayInputStream inByteStream = new ByteArrayInputStream(compressed);

                  try {
                     GZIPInputStream zipStream = new GZIPInputStream(inByteStream);

                     try {
                        uncompressed = ByteStreams.toByteArray(zipStream);
                     } catch (Throwable var12) {
                        try {
                           zipStream.close();
                        } catch (Throwable var11) {
                           var12.addSuppressed(var11);
                        }

                        throw var12;
                     }

                     zipStream.close();
                  } catch (Throwable var13) {
                     try {
                        inByteStream.close();
                     } catch (Throwable var10) {
                        var13.addSuppressed(var10);
                     }

                     throw var13;
                  }

                  inByteStream.close();
               }

               basicConfig.loadFromStream(new ByteArrayInputStream(uncompressed));
            } catch (IOException var14) {
               plugin.getLogger().log(Level.SEVERE, "Unhandled IO error parsing train chest configuration", var14);
               return null;
            }

            group = SpawnableGroup.fromConfig(plugin, basicConfig);
         }

         return group.getMembers().isEmpty() ? null : group;
      }
   }

   public static TrainChestItemUtil.SpawnResult spawnAtBlock(SpawnableGroup group, Block clickedBlock, TrainChestItemUtil.SpawnOptions options) {
      if (group == null) {
         return TrainChestItemUtil.SpawnResult.FAIL_EMPTY;
      } else if (TCConfig.maxCartsPerTrain >= 0 && group.getMembers().size() > TCConfig.maxCartsPerTrain) {
         return TrainChestItemUtil.SpawnResult.FAIL_TOO_LONG;
      } else if (group.isExceedingSpawnLimit()) {
         return TrainChestItemUtil.SpawnResult.FAIL_LIMIT_REACHED;
      } else {
         BlockFace orientation = FaceUtil.getDirection(options.player.getEyeLocation().getDirection());
         RailType clickedRailType = RailType.getType(clickedBlock);
         if (clickedRailType == RailType.NONE) {
            return TrainChestItemUtil.SpawnResult.FAIL_NORAIL;
         } else {
            Location spawnLoc = clickedRailType.getSpawnLocation(clickedBlock, orientation);
            if (spawnLoc == null) {
               return TrainChestItemUtil.SpawnResult.FAIL_NORAIL;
            } else {
               RailState spawnStartState = new RailState();
               spawnStartState.setRailPiece(RailPiece.create(clickedRailType, clickedBlock));
               spawnStartState.setPosition(RailPath.Position.fromTo(spawnLoc, spawnLoc));
               spawnStartState.setMotionVector(spawnLoc.getDirection());
               spawnStartState.initEnterDirection();
               spawnStartState.loadRailLogic().getPath().move(spawnStartState, 0.0D);
               if (spawnStartState.position().motDot(options.player.getEyeLocation().getDirection()) < 0.0D) {
                  spawnStartState.position().invertMotion();
               }

               Vector spawnDirection = spawnStartState.motionVector();
               Optional<TrainChestItemUtil.SpawnResult> behindResult = trySpawnExtendBehind(group, spawnStartState, options);
               if (behindResult.isPresent()) {
                  return (TrainChestItemUtil.SpawnResult)behindResult.get();
               } else if (MinecartGroupStore.isPerWorldSpawnLimitReached(clickedBlock, group.getMembers().size())) {
                  return TrainChestItemUtil.SpawnResult.FAIL_MAX_PER_WORLD;
               } else {
                  SpawnableGroup.SpawnLocationList locationList = group.findSpawnLocations(spawnLoc, spawnDirection, SpawnableGroup.SpawnMode.DEFAULT);
                  return spawnAtLocations(group, locationList, options);
               }
            }
         }
      }
   }

   public static TrainChestItemUtil.SpawnResult spawnLookingAt(SpawnableGroup group, Player player, Location eyeLocation, TrainChestItemUtil.SpawnOptions options) {
      double stepSize = 0.05D;
      int steps = true;
      Vector step = eyeLocation.getDirection().multiply(0.05D);
      RailState bestState = null;
      Location pos = eyeLocation.clone();
      RailState tmp = new RailState();
      tmp.setRailPiece(RailPiece.createWorldPlaceholder(eyeLocation.getWorld()));
      double bestDistanceSq = 4.0D;

      for(int n = 0; n < 200; ++n) {
         pos.add(step);
         tmp.position().setLocation(pos);
         if (RailType.loadRailInformation(tmp)) {
            tmp.loadRailLogic().getPath().move(tmp, 0.0D);
            double dist_sq = tmp.position().distanceSquared(pos);
            if (dist_sq < bestDistanceSq) {
               bestDistanceSq = dist_sq;
               bestState = tmp.clone();
            }
         }
      }

      if (bestState == null) {
         return TrainChestItemUtil.SpawnResult.FAIL_NORAIL_LOOK;
      } else {
         if (bestState.position().motDot(step) < 0.0D) {
            bestState.position().invertMotion();
         }

         bestState.initEnterDirection();
         Optional<TrainChestItemUtil.SpawnResult> behindResult = trySpawnExtendBehind(group, bestState, options);
         if (behindResult.isPresent()) {
            return (TrainChestItemUtil.SpawnResult)behindResult.get();
         } else {
            return spawnAtState(group, bestState, options);
         }
      }
   }

   private static Optional<TrainChestItemUtil.SpawnResult> trySpawnExtendBehind(SpawnableGroup group, RailState spawnStartState, TrainChestItemUtil.SpawnOptions options) {
      if (options.tryExtendTrains) {
         SpawnableMember lastMember = (SpawnableMember)group.getMembers().get(group.getMembers().size() - 1);
         double searchDistance = 1.0D + 2.0D * lastMember.getCartCouplerLength() + 0.5D * lastMember.getLength();
         TrainChestExtendableTrain extendableTrain = TrainChestExtendableTrain.find(spawnStartState.cloneAndInvertMotion(), searchDistance, lastMember);
         if (extendableTrain != null) {
            options.tryExtendTrains = false;
            options.connectWith = extendableTrain.member;
            options.spawnMode = SpawnableGroup.SpawnMode.DEFAULT_EDGE;
            return Optional.of(spawnAtState(group, extendableTrain.startState, options));
         }
      }

      return Optional.empty();
   }

   private static TrainChestItemUtil.SpawnResult spawnAtLocations(SpawnableGroup group, SpawnableGroup.SpawnLocationList locationList, TrainChestItemUtil.SpawnOptions options) {
      if (locationList == null) {
         return TrainChestItemUtil.SpawnResult.FAIL_RAILTOOSHORT;
      } else {
         locationList.loadChunks();
         MinecartGroup spawnedGroup;
         if (options.tryExtendTrains) {
            List<SpawnableGroup.OccupiedLocation> occupiedLocations = locationList.getOccupiedLocations();
            if (!occupiedLocations.isEmpty()) {
               if (options.tryExtendTrains) {
                  TrainChestExtendableTrain extendableTrain = TrainChestExtendableTrain.findOccupied(occupiedLocations, (SpawnableMember)group.getMembers().get(0));
                  if (extendableTrain != null) {
                     options.tryExtendTrains = false;
                     options.connectWith = extendableTrain.member;
                     options.spawnMode = SpawnableGroup.SpawnMode.REVERSE_EDGE;
                     return spawnAtState(group, extendableTrain.startState.cloneAndInvertMotion(), options);
                  }
               }

               return TrainChestItemUtil.SpawnResult.FAIL_BLOCKED;
            }
         } else if (options.connectWith != null) {
            spawnedGroup = options.connectWith.getGroup();
            Iterator var9 = locationList.getOccupiedLocations().iterator();

            while(var9.hasNext()) {
               SpawnableGroup.OccupiedLocation occupied = (SpawnableGroup.OccupiedLocation)var9.next();
               if (occupied.member.getGroup() != spawnedGroup) {
                  return TrainChestItemUtil.SpawnResult.FAIL_BLOCKED;
               }
            }
         } else if (locationList.isOccupied()) {
            return TrainChestItemUtil.SpawnResult.FAIL_BLOCKED;
         }

         if (locationList.locations.size() < group.getMembers().size()) {
            return TrainChestItemUtil.SpawnResult.FAIL_RAILTOOSHORT;
         } else {
            if (options.tryExtendTrains && locationList.endState != null) {
               SpawnableMember firstMember = (SpawnableMember)group.getMembers().get(0);
               double searchDistance = 1.0D + 2.0D * firstMember.getCartCouplerLength() + 0.5D * firstMember.getLength();
               TrainChestExtendableTrain extendableTrain = TrainChestExtendableTrain.find(locationList.endState, searchDistance, firstMember);
               if (extendableTrain != null) {
                  options.tryExtendTrains = false;
                  options.connectWith = extendableTrain.member;
                  options.spawnMode = SpawnableGroup.SpawnMode.REVERSE_EDGE;
                  return spawnAtState(group, extendableTrain.startState.cloneAndInvertMotion(), options);
               }
            }

            spawnedGroup = group.spawn(locationList, options.initialSpeed);
            if (!spawnedGroup.isEmpty()) {
               spawnedGroup.getTrainCarts().getPlayer(options.player).editMember(spawnedGroup.tail());
            }

            if (options.connectWith != null) {
               MinecartMember<?> with = options.spawnMode.isReverseOrder() ? spawnedGroup.head() : spawnedGroup.tail();
               MinecartGroup.link(with, options.connectWith);
            }

            return TrainChestItemUtil.SpawnResult.SUCCESS;
         }
      }
   }

   public static TrainChestItemUtil.SpawnResult spawnAtState(SpawnableGroup group, RailState state, TrainChestItemUtil.SpawnOptions options) {
      if (group == null) {
         return TrainChestItemUtil.SpawnResult.FAIL_EMPTY;
      } else {
         int totalLength = group.getMembers().size();
         if (options.connectWith != null) {
            totalLength += options.connectWith.getGroup().size();
         }

         if (TCConfig.maxCartsPerTrain >= 0 && totalLength > TCConfig.maxCartsPerTrain) {
            return TrainChestItemUtil.SpawnResult.FAIL_TOO_LONG;
         } else if (group.isExceedingSpawnLimit()) {
            return TrainChestItemUtil.SpawnResult.FAIL_LIMIT_REACHED;
         } else if (MinecartGroupStore.isPerWorldSpawnLimitReached(state.positionLocation(), group.getMembers().size())) {
            return TrainChestItemUtil.SpawnResult.FAIL_MAX_PER_WORLD;
         } else {
            SpawnableGroup.SpawnLocationList locationList = group.findSpawnLocations(state, options.spawnMode);
            return spawnAtLocations(group, locationList, options);
         }
      }
   }

   public static enum SpawnResult {
      SUCCESS(Localization.CHEST_SPAWN_SUCCESS),
      FAIL_EMPTY(Localization.CHEST_SPAWN_EMPTY),
      FAIL_NORAIL(Localization.CHEST_SPAWN_NORAIL),
      FAIL_NORAIL_LOOK(Localization.CHEST_SPAWN_NORAIL_LOOK),
      FAIL_RAILTOOSHORT(Localization.CHEST_SPAWN_RAILTOOSHORT),
      FAIL_BLOCKED(Localization.CHEST_SPAWN_BLOCKED),
      FAIL_NO_PERM(Localization.SPAWN_FORBIDDEN_CONTENTS),
      FAIL_LIMIT_REACHED(Localization.CHEST_SPAWN_LIMIT_REACHED),
      FAIL_MAX_PER_WORLD(Localization.SPAWN_MAX_PER_WORLD),
      FAIL_TOO_LONG(Localization.SPAWN_TOO_LONG);

      private final Localization locale;

      private SpawnResult(Localization locale) {
         this.locale = locale;
      }

      public boolean hasMessage() {
         return this.locale != null;
      }

      public Localization getLocale() {
         return this.locale;
      }

      // $FF: synthetic method
      private static TrainChestItemUtil.SpawnResult[] $values() {
         return new TrainChestItemUtil.SpawnResult[]{SUCCESS, FAIL_EMPTY, FAIL_NORAIL, FAIL_NORAIL_LOOK, FAIL_RAILTOOSHORT, FAIL_BLOCKED, FAIL_NO_PERM, FAIL_LIMIT_REACHED, FAIL_MAX_PER_WORLD, FAIL_TOO_LONG};
      }
   }

   public static class SpawnOptions {
      public final Player player;
      public double initialSpeed = 0.0D;
      public boolean tryExtendTrains = false;
      public SpawnableGroup.SpawnMode spawnMode;
      public MinecartMember<?> connectWith;

      public SpawnOptions(Player player) {
         this.spawnMode = SpawnableGroup.SpawnMode.DEFAULT;
         this.connectWith = null;
         this.player = player;
      }
   }
}
