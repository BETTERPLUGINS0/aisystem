package com.bergerkiller.bukkit.tc.properties;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentModel;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.controller.global.TrainCartsPlayer;
import com.bergerkiller.bukkit.tc.offline.train.OfflineMember;
import com.bergerkiller.bukkit.tc.properties.api.IProperty;
import com.bergerkiller.bukkit.tc.properties.api.IPropertyRegistry;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParseResult;
import com.bergerkiller.bukkit.tc.properties.standard.StandardProperties;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.type.ExitOffset;
import com.bergerkiller.bukkit.tc.properties.standard.type.SignSkipOptions;
import com.bergerkiller.bukkit.tc.utils.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class CartProperties extends CartPropertiesStore implements IProperties {
   private final TrainCarts traincarts;
   private SoftReference<MinecartMember<?>> member = new SoftReference();
   protected TrainProperties group = null;
   private final FieldBackedProperty.CartInternalDataHolder standardProperties = new FieldBackedProperty.CartInternalDataHolder();
   private ConfigurationNode config;
   private final UUID uuid;
   protected boolean removed;

   protected CartProperties(TrainCarts traincarts, TrainProperties group, ConfigurationNode config, UUID uuid) {
      this.traincarts = traincarts;
      this.uuid = uuid;
      this.group = group;
      this.config = config;
      this.removed = false;
   }

   public TrainCarts getTrainCarts() {
      return this.traincarts;
   }

   public boolean isRemoved() {
      return this.removed;
   }

   protected void reassign(TrainProperties group, ConfigurationNode config) {
      if (this.group != null && this.group != group) {
         this.group.remove(this);
      }

      this.group = group;
      this.config = config;
   }

   public static boolean hasGlobalOwnership(Player player) {
      return Permission.COMMAND_GLOBALPROPERTIES.has(player);
   }

   public TrainProperties getTrainProperties() {
      return this.group;
   }

   public String getTypeName() {
      return "cart";
   }

   public final ConfigurationNode getConfig() {
      return this.config;
   }

   public final <T> T get(IProperty<T> property) {
      return property.get(this);
   }

   public final <T> void set(IProperty<T> property, T value) {
      property.set(this, value);
   }

   public FieldBackedProperty.CartInternalDataHolder getStandardPropertiesHolder() {
      return this.standardProperties;
   }

   protected void setHolder(MinecartMember<?> holder) {
      this.member.set(holder);
   }

   public MinecartMember<?> getHolder() {
      MinecartMember<?> member = (MinecartMember)this.member.get();
      return member != null && member.getEntity() != null && ((CommonMinecart)member.getEntity()).getUniqueId().equals(this.uuid) ? member : (MinecartMember)this.member.set(MinecartMemberStore.getFromUID(this.uuid));
   }

   public boolean hasHolder() {
      return this.getHolder() != null;
   }

   public CompletableFuture<Boolean> restore() {
      return this.getTrainProperties().restore();
   }

   public MinecartGroup getGroup() {
      MinecartMember<?> member = this.getHolder();
      if (member == null) {
         return this.group == null ? null : this.group.getHolder();
      } else {
         return member.getGroup();
      }
   }

   public UUID getUUID() {
      return this.uuid;
   }

   public void tryUpdate() {
      MinecartMember<?> m = this.getHolder();
      if (m != null) {
         m.onPropertiesChanged();
      }

   }

   public Collection<UUID> getEditing() {
      List<TrainCartsPlayer> players = this.traincarts.getPlayerStore().find((p) -> {
         return p.getEditedCart() == this;
      });
      return (Collection)players.stream().map(TrainCartsPlayer::getUniqueId).collect(Collectors.toList());
   }

   public Collection<Player> getEditingPlayers() {
      Collection<UUID> uuids = this.getEditing();
      ArrayList<Player> players = new ArrayList(uuids.size());
      Iterator var3 = uuids.iterator();

      while(var3.hasNext()) {
         UUID uuid = (UUID)var3.next();
         Player p = Bukkit.getServer().getPlayer(uuid);
         if (p != null) {
            players.add(p);
         }
      }

      return players;
   }

   public boolean canBreak(Block block) {
      Set<Material> types = (Set)this.get(StandardProperties.BLOCK_BREAK_TYPES);
      return !types.isEmpty() && types.contains(block.getType());
   }

   public boolean hasOwnership(Player player) {
      if (!hasGlobalOwnership(player) && !this.isOwnedByEveryone() && !this.isOwner(player)) {
         Iterator var2 = this.getOwnerPermissions().iterator();

         String ownerPermission;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            ownerPermission = (String)var2.next();
         } while(!CommonUtil.hasPermission(player, ownerPermission));

         return true;
      } else {
         return true;
      }
   }

   public boolean isOwner(Player player) {
      return this.isOwner(player.getName());
   }

   public boolean isOwner(String player) {
      return ((Set)this.get(StandardProperties.OWNERS)).contains(player.toLowerCase());
   }

   public void setOwner(String player) {
      this.setOwner(player, true);
   }

   public void setOwner(String player, boolean owner) {
      this.update(StandardProperties.OWNERS, (curr_owners) -> {
         String player_lc = player.toLowerCase();
         if (curr_owners.contains(player_lc) == owner) {
            return curr_owners;
         } else {
            HashSet<String> new_owners = new HashSet(curr_owners);
            LogicUtil.addOrRemove(new_owners, player_lc, owner);
            return new_owners;
         }
      });
   }

   public void setOwner(Player player) {
      this.setOwner(player, true);
   }

   public void setOwner(Player player, boolean owner) {
      if (player != null) {
         this.setOwner(player.getName(), owner);
      }
   }

   public boolean isOwnedByEveryone() {
      return !this.hasOwners() && !this.hasOwnerPermissions();
   }

   public Set<String> getOwnerPermissions() {
      return (Set)this.get(StandardProperties.OWNER_PERMISSIONS);
   }

   public void setOwnerPermissions(Set<String> newOwnerPermissions) {
      this.set(StandardProperties.OWNER_PERMISSIONS, newOwnerPermissions);
   }

   public void addOwnerPermission(String permission) {
      this.update(StandardProperties.OWNER_PERMISSIONS, (curr_perms) -> {
         if (curr_perms.contains(permission)) {
            return curr_perms;
         } else {
            HashSet<String> new_perms = new HashSet(curr_perms);
            new_perms.add(permission);
            return new_perms;
         }
      });
   }

   public void removeOwnerPermission(String permission) {
      this.update(StandardProperties.OWNER_PERMISSIONS, (curr_perms) -> {
         if (!curr_perms.contains(permission)) {
            return curr_perms;
         } else {
            HashSet<String> new_perms = new HashSet(curr_perms);
            new_perms.remove(permission);
            return new_perms;
         }
      });
   }

   public void clearOwnerPermissions() {
      this.set(StandardProperties.OWNER_PERMISSIONS, Collections.emptySet());
   }

   public boolean hasOwnerPermissions() {
      return !((Set)this.get(StandardProperties.OWNER_PERMISSIONS)).isEmpty();
   }

   public Set<String> getOwners() {
      return (Set)this.get(StandardProperties.OWNERS);
   }

   public void setOwners(Set<String> newOwners) {
      this.set(StandardProperties.OWNERS, newOwners);
   }

   public void clearOwners() {
      this.set(StandardProperties.OWNERS, Collections.emptySet());
   }

   public void addOwners(Collection<String> ownersToAdd) {
      this.update(StandardProperties.OWNERS, (curr_owners) -> {
         Set<String> newOwners = new HashSet(curr_owners);
         newOwners.addAll(curr_owners);
         return newOwners;
      });
   }

   public void removeOwners(Collection<String> ownersToRemove) {
      this.update(StandardProperties.OWNERS, (curr_owners) -> {
         Set<String> newOwners = new HashSet(curr_owners);
         newOwners.removeAll(curr_owners);
         return newOwners;
      });
   }

   public boolean hasOwners() {
      return !((Set)this.get(StandardProperties.OWNERS)).isEmpty();
   }

   public ExitOffset getExitOffset() {
      return (ExitOffset)this.get(StandardProperties.EXIT_OFFSET);
   }

   public void setExitOffset(ExitOffset new_offset) {
      this.set(StandardProperties.EXIT_OFFSET, new_offset);
   }

   public boolean canPickup() {
      return (Boolean)this.get(StandardProperties.PICK_UP_ITEMS);
   }

   public void setPickup(boolean pickup) {
      this.set(StandardProperties.PICK_UP_ITEMS, pickup);
   }

   public boolean getCanOnlyOwnersEnter() {
      return (Boolean)this.get(StandardProperties.ONLY_OWNERS_CAN_ENTER);
   }

   public void setCanOnlyOwnersEnter(boolean state) {
      this.set(StandardProperties.ONLY_OWNERS_CAN_ENTER, state);
   }

   public boolean matchTag(String tag) {
      return Util.matchText((Collection)this.getTags(), tag);
   }

   public boolean hasTags() {
      return !this.getTags().isEmpty();
   }

   public void clearTags() {
      this.set(StandardProperties.TAGS, Collections.emptySet());
   }

   public void addTags(String... tags) {
      this.update(StandardProperties.TAGS, (curr_tags) -> {
         HashSet<String> new_tags = new HashSet(curr_tags);
         new_tags.addAll(Arrays.asList(tags));
         return new_tags;
      });
   }

   public void removeTags(String... tags) {
      this.update(StandardProperties.TAGS, (curr_tags) -> {
         HashSet<String> new_tags = new HashSet(curr_tags);
         new_tags.removeAll(Arrays.asList(tags));
         return new_tags;
      });
   }

   public Set<String> getTags() {
      return (Set)this.get(StandardProperties.TAGS);
   }

   public void setTags(String... tags) {
      this.set(StandardProperties.TAGS, new HashSet(Arrays.asList(tags)));
   }

   public boolean getSpawnItemDrops() {
      return (Boolean)this.get(StandardProperties.SPAWN_ITEM_DROPS);
   }

   public void setSpawnItemDrops(boolean spawnDrops) {
      this.set(StandardProperties.SPAWN_ITEM_DROPS, spawnDrops);
   }

   public BlockLocation getLocation() {
      MinecartMember<?> member = this.getHolder();
      if (member != null) {
         return new BlockLocation(((CommonMinecart)member.getEntity()).getLocation().getBlock());
      } else {
         OfflineMember omember = this.getTrainCarts().getOfflineGroups().findMember(this.getTrainProperties().getTrainName(), this.getUUID());
         if (omember == null) {
            return null;
         } else {
            World world = omember.group.world.getLoadedWorld();
            return world == null ? new BlockLocation("Unknown", omember.cx << 4, 0, omember.cz << 4) : new BlockLocation(world, omember.cx << 4, 0, omember.cz << 4);
         }
      }
   }

   public boolean hasBlockBreakTypes() {
      return !((Set)this.get(StandardProperties.BLOCK_BREAK_TYPES)).isEmpty();
   }

   public void clearBlockBreakTypes() {
      this.set(StandardProperties.BLOCK_BREAK_TYPES, Collections.emptySet());
   }

   public Collection<Material> getBlockBreakTypes() {
      return (Collection)this.get(StandardProperties.BLOCK_BREAK_TYPES);
   }

   public String getEnterMessage() {
      return (String)this.get(StandardProperties.ENTER_MESSAGE);
   }

   public void setEnterMessage(String message) {
      this.set(StandardProperties.ENTER_MESSAGE, message);
   }

   public boolean hasEnterMessage() {
      return !((String)this.get(StandardProperties.ENTER_MESSAGE)).isEmpty();
   }

   public void showEnterMessage(Player player) {
      String message = this.getEnterMessage();
      if (!message.isEmpty()) {
         TrainCarts.sendMessage(player, ChatColor.YELLOW + TrainCarts.getMessage(message));
      }

   }

   public void clearDestination() {
      this.set(StandardProperties.DESTINATION, "");
   }

   public boolean hasDestination() {
      return !((String)this.get(StandardProperties.DESTINATION)).isEmpty();
   }

   public String getDestination() {
      return (String)this.get(StandardProperties.DESTINATION);
   }

   public void setDestination(String destination) {
      this.set(StandardProperties.DESTINATION, destination);
   }

   public List<String> getDestinationRoute() {
      return (List)this.get(StandardProperties.DESTINATION_ROUTE);
   }

   public void setDestinationRoute(List<String> route) {
      this.set(StandardProperties.DESTINATION_ROUTE, route);
   }

   public void clearDestinationRoute() {
      this.set(StandardProperties.DESTINATION_ROUTE, Collections.emptyList());
   }

   public void addDestinationToRoute(String destination) {
      if (destination != null && !destination.isEmpty()) {
         this.update(StandardProperties.DESTINATION_ROUTE, (curr_route) -> {
            ArrayList<String> new_route = new ArrayList(curr_route);
            new_route.add(destination);
            return new_route;
         });
      }

   }

   public void removeDestinationFromRoute(String destination) {
      if (destination != null && !destination.isEmpty()) {
         this.update(StandardProperties.DESTINATION_ROUTE, (curr_route) -> {
            ArrayList new_route = new ArrayList(curr_route);

            while(new_route.remove(destination)) {
            }

            return new_route;
         });
      }

   }

   public int getCurrentRouteDestinationIndex() {
      List<String> destinationRoute = this.getDestinationRoute();
      String destination = this.getDestination();
      if (!destinationRoute.isEmpty() && !destination.isEmpty()) {
         int destinationRouteIndex = (Integer)this.get(StandardProperties.DESTINATION_ROUTE_INDEX);
         if (destinationRouteIndex >= 0 && destinationRouteIndex < destinationRoute.size()) {
            return destination.equals(destinationRoute.get(destinationRouteIndex)) ? destinationRouteIndex : destinationRoute.indexOf(destination);
         } else {
            return destinationRoute.indexOf(destination);
         }
      } else {
         return -1;
      }
   }

   public String getNextDestinationOnRoute(String currentDestination) {
      List<String> destinationRoute = this.getDestinationRoute();
      if (destinationRoute.isEmpty()) {
         return "";
      } else {
         int destinationRouteIndex = (Integer)this.get(StandardProperties.DESTINATION_ROUTE_INDEX);
         if (destinationRouteIndex < 0 || destinationRouteIndex >= destinationRoute.size()) {
            this.set(StandardProperties.DESTINATION_ROUTE_INDEX, 0);
            destinationRouteIndex = 0;
         }

         if (currentDestination != null && !currentDestination.isEmpty()) {
            int index;
            if (currentDestination.equals(destinationRoute.get(destinationRouteIndex))) {
               index = destinationRouteIndex;
            } else {
               index = destinationRoute.indexOf(currentDestination);
               if (index == -1) {
                  return "";
               }
            }

            return (String)destinationRoute.get((index + 1) % destinationRoute.size());
         } else {
            return (String)destinationRoute.get(destinationRouteIndex);
         }
      }
   }

   public String getLastPathNode() {
      return (String)this.get(StandardProperties.DESTINATION_LAST_PATH_NODE);
   }

   public void setLastPathNode(String nodeName) {
      this.set(StandardProperties.DESTINATION_LAST_PATH_NODE, nodeName);
   }

   public AttachmentModel getModel() {
      return (AttachmentModel)this.get(StandardProperties.MODEL);
   }

   public void resetModel() {
      this.set(StandardProperties.MODEL, (Object)null);
   }

   public boolean parseSet(String key, String arg) {
      return this.parseAndSet(key, arg).getReason() != PropertyParseResult.Reason.PROPERTY_NOT_FOUND;
   }

   public void load(CartProperties source) {
      this.load(source.getConfig());
   }

   public void load(ConfigurationNode node) {
      this.config.clear();
      node.cloneInto(this.config);
      this.onConfigurationChanged();
   }

   public void save(ConfigurationNode node) {
      this.getConfig().cloneInto(node);
   }

   protected void onConfigurationChanged() {
      Iterator var1 = IPropertyRegistry.instance().all().iterator();

      while(var1.hasNext()) {
         IProperty<?> property = (IProperty)var1.next();
         property.onConfigurationChanged(this);
      }

   }

   /** @deprecated */
   @Deprecated
   public ConfigurationNode saveToConfig() {
      return this.config;
   }

   public boolean isInvincible() {
      return (Boolean)this.get(StandardProperties.INVINCIBLE);
   }

   public void setInvincible(boolean invincible) {
      this.set(StandardProperties.INVINCIBLE, invincible);
   }

   public boolean getPlayersEnter() {
      return (Boolean)this.get(StandardProperties.ALLOW_PLAYER_ENTER);
   }

   public void setPlayersEnter(boolean state) {
      this.set(StandardProperties.ALLOW_PLAYER_ENTER, state);
   }

   public boolean getPlayersExit() {
      return (Boolean)this.get(StandardProperties.ALLOW_PLAYER_EXIT);
   }

   public void setPlayersExit(boolean state) {
      this.set(StandardProperties.ALLOW_PLAYER_EXIT, state);
   }

   public SignSkipOptions getSkipOptions() {
      return (SignSkipOptions)this.get(StandardProperties.SIGN_SKIP);
   }

   public void setSkipOptions(SignSkipOptions options) {
      Set<BlockLocation> old_skipped_signs = ((SignSkipOptions)this.get(StandardProperties.SIGN_SKIP)).skippedSigns();
      if (old_skipped_signs.equals(options.skippedSigns())) {
         this.set(StandardProperties.SIGN_SKIP, options);
      } else {
         this.set(StandardProperties.SIGN_SKIP, SignSkipOptions.create(options.ignoreCounter(), options.skipCounter(), options.filter(), old_skipped_signs));
      }

   }

   public String getDriveSound() {
      return (String)this.get(StandardProperties.DRIVE_SOUND);
   }

   public void setDriveSound(String driveSound) {
      this.set(StandardProperties.DRIVE_SOUND, driveSound);
   }
}
