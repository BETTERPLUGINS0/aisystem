package com.bergerkiller.bukkit.tc.controller.spawnable;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartGroupStore;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.properties.SavedTrainProperties;
import com.bergerkiller.bukkit.tc.properties.TrainPropertiesStore;
import com.bergerkiller.bukkit.tc.properties.defaults.DefaultProperties;
import com.bergerkiller.bukkit.tc.properties.standard.StandardProperties;
import com.bergerkiller.bukkit.tc.properties.standard.type.TrainNameFormat;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SpawnableGroup implements TrainCarts.Provider {
   private final TrainCarts plugin;
   private final List<SpawnableMember> members;
   private final ConfigurationNode config;
   private SpawnableGroup.CenterMode centerMode;
   private static final double CAN_MOVE_DISTANCE = 2.0D;

   /** @deprecated */
   @Deprecated
   public SpawnableGroup() {
      this(TrainCarts.plugin);
   }

   public SpawnableGroup(TrainCarts plugin) {
      this.members = new ArrayList();
      this.centerMode = SpawnableGroup.CenterMode.NONE;
      this.plugin = plugin;
      this.config = new ConfigurationNode();
   }

   public TrainCarts getTrainCarts() {
      return this.plugin;
   }

   /** @deprecated */
   @Deprecated
   public TrainCarts getPlugin() {
      return this.plugin;
   }

   public ConfigurationNode getConfig() {
      return this.config;
   }

   public TrainNameFormat getNameFormat() {
      return (TrainNameFormat)StandardProperties.TRAIN_NAME_FORMAT.readFromConfig(this.config).orElse(StandardProperties.TRAIN_NAME_FORMAT.getDefault());
   }

   public String getSavedName() {
      return this.config.contains("savedName") ? (String)this.config.get("savedName", "dummyname") : this.getNameFormat().generate(1);
   }

   public SpawnableGroup.CenterMode getCenterMode() {
      return this.centerMode;
   }

   public void setCenterMode(SpawnableGroup.CenterMode mode) {
      this.centerMode = mode;
   }

   public List<SpawnableMember> getMembers() {
      return this.members;
   }

   public SpawnableMember addMember(ConfigurationNode config) {
      SpawnableMember newMember = new SpawnableMember(this, config.clone());
      this.members.add(newMember);
      return newMember;
   }

   public SpawnableMember addMember(SpawnableMember member) {
      SpawnableMember newMember = member.cloneWithGroup(this);
      this.members.add(newMember);
      return newMember;
   }

   public ConfigurationNode getFullConfig() {
      ConfigurationNode fullConfig = this.config.clone();
      List<ConfigurationNode> cartConfigList = fullConfig.getNodeList("carts");

      for(int i = this.members.size() - 1; i >= 0; --i) {
         cartConfigList.add(((SpawnableMember)this.members.get(i)).getConfig().clone());
      }

      return fullConfig;
   }

   public List<SavedTrainProperties> getActiveSavedTrainSpawnLimits() {
      Optional<List<String>> names = StandardProperties.ACTIVE_SAVED_TRAIN_SPAWN_LIMITS.readFromConfig(this.config);
      if (names.isPresent()) {
         List<SavedTrainProperties> propsList = new ArrayList(((List)names.get()).size());
         Iterator var3 = ((List)names.get()).iterator();

         while(var3.hasNext()) {
            String name = (String)var3.next();
            SavedTrainProperties props = this.getTrainCarts().getSavedTrains().getProperties(name);
            if (props != null && props.getSpawnLimit() >= 0) {
               propsList.add(props);
            }
         }

         return Collections.unmodifiableList(propsList);
      } else {
         return Collections.emptyList();
      }
   }

   public boolean isExceedingSpawnLimit() {
      Optional<List<String>> names = StandardProperties.ACTIVE_SAVED_TRAIN_SPAWN_LIMITS.readFromConfig(this.config);
      if (names.isPresent()) {
         Iterator var2 = ((List)names.get()).iterator();

         while(var2.hasNext()) {
            String name = (String)var2.next();
            SavedTrainProperties props = this.getTrainCarts().getSavedTrains().getProperties(name);
            if (props != null) {
               int limit = props.getSpawnLimit();
               if (limit >= 0 && props.getSpawnLimitCurrentCount() >= limit) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public List<SpawnableMember> addTrainWithConfig(SavedTrainProperties savedTrainProperties) {
      if (savedTrainProperties != null && !savedTrainProperties.isEmpty()) {
         List<SpawnableMember> addedMembers = this.addTrainWithConfig(savedTrainProperties.getConfig());
         if (!addedMembers.isEmpty() && savedTrainProperties.getSpawnLimit() >= 0) {
            StandardProperties.ACTIVE_SAVED_TRAIN_SPAWN_LIMITS.addSavedTrainToConfig(this.config, savedTrainProperties.getName());
         }

         return addedMembers;
      } else {
         return Collections.emptyList();
      }
   }

   public List<SpawnableMember> addTrainWithConfig(ConfigurationNode savedConfig) {
      Iterator var2 = savedConfig.getKeys().iterator();

      while(var2.hasNext()) {
         String key = (String)var2.next();
         if (!key.equals("carts")) {
            this.config.set(key, savedConfig.get(key));
         }
      }

      List<ConfigurationNode> cartConfigList = savedConfig.getNodeList("carts");
      List<SpawnableMember> newMembers = new ArrayList(cartConfigList.size());

      for(int i = cartConfigList.size() - 1; i >= 0; --i) {
         newMembers.add(this.addMember((ConfigurationNode)cartConfigList.get(i)));
      }

      return newMembers;
   }

   public double getTotalLength() {
      if (this.members.isEmpty()) {
         return 0.0D;
      } else {
         boolean first = true;
         double totalLength = 0.0D;
         double previousCartCouplerLength = 0.0D;

         SpawnableMember member;
         for(Iterator var6 = this.members.iterator(); var6.hasNext(); totalLength += member.getLength()) {
            member = (SpawnableMember)var6.next();
            if (first) {
               first = false;
            } else {
               totalLength += previousCartCouplerLength + member.getCartCouplerLength();
            }

            previousCartCouplerLength = member.getCartCouplerLength();
         }

         return totalLength;
      }
   }

   public String toString() {
      StringBuilder str = new StringBuilder();
      str.append("{").append("center=").append(this.centerMode);
      str.append(", types=[");
      boolean first = true;

      SpawnableMember member;
      for(Iterator var3 = this.members.iterator(); var3.hasNext(); str.append(member.toString())) {
         member = (SpawnableMember)var3.next();
         if (first) {
            first = false;
         } else {
            str.append(", ");
         }
      }

      str.append("]}");
      return str.toString();
   }

   public SpawnableGroup.SpawnLocationList findSpawnLocations(Block startRailBlock, Vector forwardDirection, SpawnableGroup.SpawnMode mode) {
      return this.findSpawnLocations(RailPiece.create(startRailBlock), forwardDirection, mode);
   }

   public SpawnableGroup.SpawnLocationList findSpawnLocations(RailPiece startRails, Vector forwardDirection, SpawnableGroup.SpawnMode mode) {
      if (startRails != null && !startRails.isNone()) {
         RailState state = RailState.getSpawnState(startRails);
         if (state.motionVector().dot(forwardDirection) < 0.0D) {
            state.position().invertMotion();
         }

         return this.findSpawnLocations(state, mode);
      } else {
         return null;
      }
   }

   public SpawnableGroup.SpawnLocationList findSpawnLocations(Location startLocation, Vector forwardDirection, SpawnableGroup.SpawnMode mode) {
      RailPiece piece = RailType.findRailPiece(startLocation);
      if (piece != null && !piece.isNone()) {
         RailState state = new RailState();
         state.setRailPiece(piece);
         state.position().setLocation(startLocation);
         RailType.loadRailInformation(state);
         state.setMotionVector(forwardDirection);
         state.loadRailLogic().getPath().snap(state.position(), state.railBlock());
         return this.findSpawnLocations(state, mode);
      } else {
         return null;
      }
   }

   public SpawnableGroup.SpawnLocationList findSpawnLocations(RailState startState, SpawnableGroup.SpawnMode mode) {
      if (this.members.isEmpty()) {
         return null;
      } else if (startState.railType() == RailType.NONE) {
         return null;
      } else {
         boolean edgeAtStart = mode == SpawnableGroup.SpawnMode.DEFAULT_EDGE || mode == SpawnableGroup.SpawnMode.REVERSE_EDGE;
         SpawnableGroup.SpawnLocationList result;
         TrackWalkingPoint walker;
         if (!edgeAtStart && this.members.size() == 1) {
            result = new SpawnableGroup.SpawnLocationList();
            result.addMember((SpawnableMember)this.members.get(0), startState.motionVector(), startState.positionLocation());
            result.endState = startState.clone();
            walker = new TrackWalkingPoint(startState);
            walker.skipFirst();
            result.can_move = walker.move(0.5D * ((SpawnableMember)this.members.get(0)).getLength() + 2.0D);
            return result;
         } else {
            double gap;
            if (mode == SpawnableGroup.SpawnMode.CENTER) {
               double halfLength = 0.5D * this.getTotalLength();
               if (!(halfLength < 1.0E-10D)) {
                  List<SpawnableMember> backward = new ArrayList(this.members.size());
                  List<SpawnableMember> forward = new ArrayList(this.members.size());
                  gap = 0.0D;
                  double forwardOffset = 0.0D;
                  double accumLength = 0.0D;
                  double prevMemberLengthWithCoupler = Double.NaN;
                  boolean isForwardPortion = false;
                  Iterator var17 = this.members.iterator();

                  while(var17.hasNext()) {
                     SpawnableMember member = (SpawnableMember)var17.next();
                     if (isForwardPortion) {
                        forward.add(member);
                     } else {
                        double memberStartLength = 0.5D * member.getLength();
                        if (!Double.isNaN(prevMemberLengthWithCoupler)) {
                           memberStartLength += member.getCartCouplerLength() + prevMemberLengthWithCoupler;
                        }

                        double distanceBeyondHalf = accumLength + memberStartLength - halfLength;
                        if (distanceBeyondHalf >= 0.0D) {
                           gap = halfLength - accumLength;
                           forwardOffset = distanceBeyondHalf;
                           isForwardPortion = true;
                           forward.add(member);
                        } else {
                           accumLength += memberStartLength;
                           prevMemberLengthWithCoupler = 0.5D * member.getLength() + member.getCartCouplerLength();
                           backward.add(member);
                        }
                     }
                  }

                  if (!isForwardPortion) {
                     gap = halfLength - accumLength;
                     forwardOffset = halfLength;
                  }

                  Collections.reverse(backward);
                  SpawnableGroup.SpawnLocationList result = new SpawnableGroup.SpawnLocationList();
                  TrackWalkingPoint walker = new TrackWalkingPoint(startState.cloneAndInvertMotion());
                  walker.skipFirst();
                  if (!walker.move(gap)) {
                     return null;
                  } else {
                     SpawnableMember member;
                     int i;
                     double gap;
                     for(i = 0; i < backward.size(); ++i) {
                        member = (SpawnableMember)backward.get(i);
                        if (i > 0 && !walker.move(0.5D * member.getLength())) {
                           return null;
                        }

                        result.addMember(member, walker.state.motionVector().multiply(-1.0D), Util.invertRotation(walker.state.positionLocation()));
                        gap = 0.0D;
                        if (i < backward.size() - 1) {
                           gap = member.getCartCouplerLength() + ((SpawnableMember)backward.get(i + 1)).getCartCouplerLength();
                        }

                        if (!walker.move(0.5D * member.getLength() + gap)) {
                           return null;
                        }
                     }

                     Collections.reverse(result.locations);
                     walker = new TrackWalkingPoint(startState);
                     walker.skipFirst();
                     if (!walker.move(forwardOffset)) {
                        return null;
                     } else {
                        for(i = 0; i < forward.size(); ++i) {
                           member = (SpawnableMember)forward.get(i);
                           if (i > 0 && !walker.move(0.5D * member.getLength())) {
                              return null;
                           }

                           result.addMember(member, walker.state.motionVector(), walker.state.positionLocation());
                           gap = 0.0D;
                           if (i < forward.size() - 1) {
                              gap = member.getCartCouplerLength() + ((SpawnableMember)forward.get(i + 1)).getCartCouplerLength();
                           }

                           if (!walker.move(0.5D * member.getLength() + gap)) {
                              return null;
                           }
                        }

                        result.endState = walker.state.clone();
                        result.can_move = walker.move(2.0D);
                        return result;
                     }
                  }
               } else {
                  SpawnableGroup.SpawnLocationList result = new SpawnableGroup.SpawnLocationList();
                  Vector forward = startState.motionVector();
                  Location location = startState.positionLocation();
                  Iterator var9 = this.members.iterator();

                  while(var9.hasNext()) {
                     SpawnableMember member = (SpawnableMember)var9.next();
                     result.addMember(member, forward, location);
                  }

                  result.endState = startState.clone();
                  TrackWalkingPoint walker = new TrackWalkingPoint(startState);
                  walker.skipFirst();
                  result.can_move = walker.move(2.0D);
                  return result;
               }
            } else {
               int i;
               SpawnableMember member;
               if (mode != SpawnableGroup.SpawnMode.DEFAULT && mode != SpawnableGroup.SpawnMode.DEFAULT_EDGE) {
                  if (mode != SpawnableGroup.SpawnMode.REVERSE && mode != SpawnableGroup.SpawnMode.REVERSE_EDGE) {
                     return null;
                  } else {
                     result = new SpawnableGroup.SpawnLocationList();
                     walker = new TrackWalkingPoint(startState.cloneAndInvertMotion());
                     walker.skipFirst();

                     for(i = this.members.size() - 1; i >= 0; --i) {
                        member = (SpawnableMember)this.members.get(i);
                        if (!edgeAtStart && i == this.members.size() - 1) {
                           if (!walker.move(0.0D)) {
                              return null;
                           }
                        } else if (!walker.move(0.5D * member.getLength())) {
                           return null;
                        }

                        result.addMember(member, walker.state.motionVector().multiply(-1.0D), Util.invertRotation(walker.state.positionLocation()));
                        gap = 0.0D;
                        if (i > 0) {
                           gap = member.getCartCouplerLength() + ((SpawnableMember)this.members.get(i - 1)).getCartCouplerLength();
                        }

                        if (!walker.move(0.5D * member.getLength() + gap)) {
                           return null;
                        }
                     }

                     Collections.reverse(result.locations);
                     result.endState = walker.state.clone();
                     result.can_move = walker.move(2.0D);
                     return result;
                  }
               } else {
                  result = new SpawnableGroup.SpawnLocationList();
                  walker = new TrackWalkingPoint(startState);
                  walker.skipFirst();

                  for(i = 0; i < this.members.size(); ++i) {
                     member = (SpawnableMember)this.members.get(i);
                     if (!edgeAtStart && i == 0) {
                        if (!walker.move(0.0D)) {
                           return null;
                        }
                     } else if (!walker.move(0.5D * member.getLength())) {
                        return null;
                     }

                     result.addMember(member, walker.state.motionVector(), walker.state.positionLocation());
                     gap = 0.0D;
                     if (i < this.members.size() - 1) {
                        gap = member.getCartCouplerLength() + ((SpawnableMember)this.members.get(i + 1)).getCartCouplerLength();
                     }

                     if (!walker.move(0.5D * member.getLength() + gap)) {
                        return null;
                     }
                  }

                  result.endState = walker.state.clone();
                  result.can_move = walker.move(2.0D);
                  return result;
               }
            }
         }
      }
   }

   public MinecartGroup spawn(SpawnableGroup.SpawnLocationList spawnLocations) {
      return MinecartGroupStore.spawn(this, spawnLocations);
   }

   public MinecartGroup spawn(SpawnableGroup.SpawnLocationList spawnLocations, double initialSpeed) {
      return MinecartGroupStore.spawn(this, spawnLocations, initialSpeed);
   }

   /** @deprecated */
   @Deprecated
   public static SpawnableGroup fromConfig(ConfigurationNode savedConfig) {
      return fromConfig(TrainCarts.plugin, savedConfig);
   }

   public static SpawnableGroup fromConfig(SavedTrainProperties savedTrainProperties) {
      SpawnableGroup result = new SpawnableGroup(savedTrainProperties.getTrainCarts());
      result.addTrainWithConfig(savedTrainProperties);
      return result;
   }

   public static SpawnableGroup fromConfig(TrainCarts plugin, ConfigurationNode savedConfig) {
      SpawnableGroup result = new SpawnableGroup(plugin);
      result.addTrainWithConfig(savedConfig);
      return result;
   }

   public boolean checkSpawnPermissions(CommandSender sender) {
      boolean canHaveItems = false;
      Iterator var3 = this.getMembers().iterator();

      while(var3.hasNext()) {
         SpawnableMember member = (SpawnableMember)var3.next();
         if (!member.getPermission().handleMsg(sender, Localization.SPAWN_DISALLOWED_TYPE.get(member.toString()))) {
            return false;
         }

         if (!canHaveItems && member.hasInventoryItems()) {
            canHaveItems = Permission.SPAWNER_INVENTORY_ITEMS.has(sender);
            if (!canHaveItems) {
               Localization.SPAWN_DISALLOWED_INVENTORY.message(sender, new String[0]);
               return false;
            }
         }
      }

      DefaultProperties defaults;
      if (sender instanceof Player) {
         defaults = TrainPropertiesStore.getDefaultsByPlayer((Player)sender);
      } else {
         defaults = TrainPropertiesStore.getDefaultsByName("default");
      }

      if (!defaults.checkSavedTrainPermissions(sender, this)) {
         return false;
      } else {
         return true;
      }
   }

   /** @deprecated */
   @Deprecated
   public static SpawnableGroup parse(String typesText) {
      return parse(TrainCarts.plugin, typesText);
   }

   public static SpawnableGroup parse(TrainCarts plugin, String typesText) {
      Function<String, String> savedTrainMatcher = (name) -> {
         return plugin.getSavedTrains().findName(name);
      };
      TrainSpawnPattern.ParsedSpawnPattern pattern = TrainSpawnPattern.parse(typesText, savedTrainMatcher);
      SpawnableGroup result = new SpawnableGroup(plugin);
      result.setCenterMode(pattern.centerMode());

      try {
         pattern.newGroupApplier().apply(result, new Random(), savedTrainMatcher);
      } catch (TrainSpawnPattern.TrainTooLongException var6) {
      }

      return result;
   }

   /** @deprecated */
   @Deprecated
   public static SpawnableGroup ofMembers(Iterable<SpawnableMember> members) {
      return ofMembers(TrainCarts.plugin, members);
   }

   public static SpawnableGroup ofMembers(TrainCarts plugin, Iterable<SpawnableMember> members) {
      SpawnableGroup group = new SpawnableGroup(plugin);
      Iterator var3 = members.iterator();

      while(var3.hasNext()) {
         SpawnableMember member = (SpawnableMember)var3.next();
         group.addMember(member);
      }

      return group;
   }

   public static enum CenterMode {
      NONE,
      MIDDLE,
      LEFT,
      RIGHT;

      public SpawnableGroup.CenterMode next(SpawnableGroup.CenterMode adjusted) {
         return this != NONE && this != adjusted ? MIDDLE : adjusted;
      }

      // $FF: synthetic method
      private static SpawnableGroup.CenterMode[] $values() {
         return new SpawnableGroup.CenterMode[]{NONE, MIDDLE, LEFT, RIGHT};
      }
   }

   public static enum SpawnMode {
      DEFAULT,
      REVERSE,
      DEFAULT_EDGE,
      REVERSE_EDGE,
      CENTER;

      public boolean isReverseOrder() {
         switch(this) {
         case REVERSE:
         case REVERSE_EDGE:
            return true;
         default:
            return false;
         }
      }

      // $FF: synthetic method
      private static SpawnableGroup.SpawnMode[] $values() {
         return new SpawnableGroup.SpawnMode[]{DEFAULT, REVERSE, DEFAULT_EDGE, REVERSE_EDGE, CENTER};
      }
   }

   public static final class SpawnLocationList {
      public final List<SpawnableMember.SpawnLocation> locations = new ArrayList();
      public RailState endState;
      public boolean can_move = true;

      public void addMember(SpawnableMember member, Vector forward, Location location) {
         this.locations.add(new SpawnableMember.SpawnLocation(member, forward, location));
      }

      public void loadChunks() {
         Iterator var1 = this.locations.iterator();

         while(var1.hasNext()) {
            SpawnableMember.SpawnLocation loc = (SpawnableMember.SpawnLocation)var1.next();
            WorldUtil.loadChunks(loc.location, 2);
         }

      }

      public List<SpawnableGroup.OccupiedLocation> getOccupiedLocations() {
         List<SpawnableGroup.OccupiedLocation> occupying = Collections.emptyList();
         Iterator var2 = this.locations.iterator();

         while(var2.hasNext()) {
            SpawnableMember.SpawnLocation loc = (SpawnableMember.SpawnLocation)var2.next();
            MinecartMember<?> member = MinecartMemberStore.getAt(loc.location);
            if (member != null && !member.isUnloaded() && !((CommonMinecart)member.getEntity()).isRemoved()) {
               if (((List)occupying).isEmpty()) {
                  occupying = new ArrayList();
               }

               ((List)occupying).add(new SpawnableGroup.OccupiedLocation(loc, member));
            }
         }

         return (List)occupying;
      }

      public boolean isOccupied() {
         return !this.getOccupiedLocations().isEmpty();
      }
   }

   public static class OccupiedLocation {
      public final SpawnableMember.SpawnLocation spawnLocation;
      public final MinecartMember<?> member;

      public OccupiedLocation(SpawnableMember.SpawnLocation spawnLocation, MinecartMember<?> member) {
         this.spawnLocation = spawnLocation;
         this.member = member;
      }
   }

   public static enum VanillaCartType {
      RIDEABLE('m', EntityType.MINECART),
      STORAGE('s', EntityType.MINECART_CHEST),
      POWERED('p', EntityType.MINECART_FURNACE),
      HOPPER('h', EntityType.MINECART_HOPPER),
      TNT('t', EntityType.MINECART_TNT);

      private final char code;
      private final EntityType type;

      private VanillaCartType(char code, EntityType type) {
         this.code = code;
         this.type = type;
      }

      public char getCode() {
         return this.code;
      }

      public EntityType getType() {
         return this.type;
      }

      public String toString() {
         return Character.toString(this.code);
      }

      public static Optional<SpawnableGroup.VanillaCartType> parse(char c) {
         c = Character.toLowerCase(c);
         SpawnableGroup.VanillaCartType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            SpawnableGroup.VanillaCartType type = var1[var3];
            if (type.getCode() == c) {
               return Optional.of(type);
            }
         }

         return Optional.empty();
      }

      // $FF: synthetic method
      private static SpawnableGroup.VanillaCartType[] $values() {
         return new SpawnableGroup.VanillaCartType[]{RIDEABLE, STORAGE, POWERED, HOPPER, TNT};
      }
   }
}
