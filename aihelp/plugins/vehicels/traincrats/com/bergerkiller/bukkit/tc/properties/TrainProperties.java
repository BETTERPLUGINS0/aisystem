package com.bergerkiller.bukkit.tc.properties;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.common.chunk.ChunkFutureProvider;
import com.bergerkiller.bukkit.common.chunk.ForcedChunk;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.tc.CollisionMode;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.offline.train.OfflineGroup;
import com.bergerkiller.bukkit.tc.properties.api.IProperty;
import com.bergerkiller.bukkit.tc.properties.api.IPropertyRegistry;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParseResult;
import com.bergerkiller.bukkit.tc.properties.defaults.DefaultProperties;
import com.bergerkiller.bukkit.tc.properties.standard.StandardProperties;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.type.BankingOptions;
import com.bergerkiller.bukkit.tc.properties.standard.type.ChunkLoadOptions;
import com.bergerkiller.bukkit.tc.properties.standard.type.CollisionMobCategory;
import com.bergerkiller.bukkit.tc.properties.standard.type.CollisionOptions;
import com.bergerkiller.bukkit.tc.properties.standard.type.SignSkipOptions;
import com.bergerkiller.bukkit.tc.properties.standard.type.SlowdownMode;
import com.bergerkiller.bukkit.tc.properties.standard.type.TrainNameFormat;
import com.bergerkiller.bukkit.tc.properties.standard.type.WaitOptions;
import com.bergerkiller.bukkit.tc.utils.SoftReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TrainProperties extends TrainPropertiesStore implements IProperties {
   private static final long serialVersionUID = 1L;
   private final TrainCarts traincarts;
   private final SoftReference<MinecartGroup> group = new SoftReference();
   private final FieldBackedProperty.TrainInternalDataHolder standardProperties = new FieldBackedProperty.TrainInternalDataHolder();
   private final ConfigurationNode config;
   protected String trainname;
   protected boolean removed;

   protected TrainProperties(TrainCarts traincarts, String trainname, ConfigurationNode config) {
      this.traincarts = traincarts;
      this.trainname = trainname;
      this.config = config;
      this.removed = true;
      if (config.isNode("carts")) {
         Iterator var4 = config.getNode("carts").getNodes().iterator();

         while(var4.hasNext()) {
            ConfigurationNode cartConfig = (ConfigurationNode)var4.next();

            UUID uuid;
            try {
               uuid = UUID.fromString(cartConfig.getName());
            } catch (IllegalArgumentException var8) {
               traincarts.getLogger().log(Level.WARNING, "Invalid UUID for cart: " + cartConfig.getName());
               continue;
            }

            CartProperties cProp = CartPropertiesStore.createNew(this, cartConfig, uuid);
            cProp.group = this;
            super.add(CartPropertiesStore.createNew(this, cartConfig, uuid));
         }
      }

   }

   public TrainCarts getTrainCarts() {
      return this.traincarts;
   }

   public String getTypeName() {
      return "train";
   }

   public final ConfigurationNode getConfig() {
      return this.config;
   }

   public boolean isRemoved() {
      return this.removed;
   }

   public final <T> T get(IProperty<T> property) {
      return property.get(this);
   }

   public final <T> void set(IProperty<T> property, T value) {
      property.set(this, value);
   }

   public FieldBackedProperty.TrainInternalDataHolder getStandardPropertiesHolder() {
      return this.standardProperties;
   }

   public MinecartGroup getHolder() {
      MinecartGroup group = (MinecartGroup)this.group.get();
      return group != null && !group.isRemoved() ? group : null;
   }

   protected void updateHolder(MinecartGroup holder, boolean set) {
      if (set) {
         if (this.group.get() != holder) {
            this.group.set(holder);
            this.onConfigurationChanged(true);
         }
      } else if (this.group.get() == holder) {
         this.group.set((Object)null);
      }

   }

   public boolean hasHolder() {
      return this.getHolder() != null;
   }

   public CompletableFuture<Boolean> restore() {
      if (this.isLoaded()) {
         return CompletableFuture.completedFuture(true);
      } else {
         OfflineGroup group = this.getTrainCarts().getOfflineGroups().findGroup(this.trainname);
         if (group == null) {
            return CompletableFuture.completedFuture(false);
         } else {
            List<ForcedChunk> chunksOfTrain = new ArrayList();
            World world = group.world.getLoadedWorld();
            if (world != null) {
               group.forAllChunks((cx, cz) -> {
                  chunksOfTrain.add(WorldUtil.forceChunkLoaded(world, cx, cz));
               });
            }

            CompletableFuture<Void> whenAllChunkEntitiesLoaded = loadChunkFutureWithFutureProvider(this.traincarts, chunksOfTrain);
            CompletableFuture<Boolean> result = new CompletableFuture();
            whenAllChunkEntitiesLoaded.thenAccept((unused) -> {
               result.complete(this.hasHolder());
               chunksOfTrain.forEach(ForcedChunk::close);
            }).exceptionally((err) -> {
               this.traincarts.getLogger().log(Level.SEVERE, "Failed to load chunks of train", err);
               result.complete(false);
               chunksOfTrain.forEach(ForcedChunk::close);
               return null;
            });
            return result;
         }
      }
   }

   private static CompletableFuture<Void> loadChunkFutureWithFutureProvider(TrainCarts traincarts, List<ForcedChunk> chunks) {
      ChunkFutureProvider provider = ChunkFutureProvider.of(traincarts);
      return CompletableFuture.allOf((CompletableFuture[])chunks.stream().map((c) -> {
         return provider.whenEntitiesLoaded(c.getWorld(), c.getX(), c.getZ());
      }).toArray((x$0) -> {
         return new CompletableFuture[x$0];
      }));
   }

   public double getWaitDistance() {
      return ((WaitOptions)this.get(StandardProperties.WAIT)).distance();
   }

   public void setWaitDistance(double waitDistance) {
      this.update(StandardProperties.WAIT, (opt) -> {
         return WaitOptions.create(waitDistance, opt.delay(), opt.acceleration(), opt.deceleration(), opt.predict());
      });
   }

   public double getWaitDelay() {
      return ((WaitOptions)this.get(StandardProperties.WAIT)).delay();
   }

   public void setWaitDelay(double delay) {
      this.update(StandardProperties.WAIT, (opt) -> {
         return WaitOptions.create(opt.distance(), delay, opt.acceleration(), opt.deceleration(), opt.predict());
      });
   }

   public double getWaitAcceleration() {
      return ((WaitOptions)this.get(StandardProperties.WAIT)).acceleration();
   }

   public double getWaitDeceleration() {
      return ((WaitOptions)this.get(StandardProperties.WAIT)).deceleration();
   }

   public void setWaitAcceleration(double acceleration) {
      this.setWaitAcceleration(acceleration, acceleration);
   }

   public void setWaitAcceleration(double acceleration, double deceleration) {
      this.update(StandardProperties.WAIT, (opt) -> {
         return WaitOptions.create(opt.distance(), opt.delay(), acceleration, deceleration, opt.predict());
      });
   }

   public boolean isWaitPredicted() {
      return ((WaitOptions)this.get(StandardProperties.WAIT)).predict();
   }

   public void setWaitPredicted(boolean use) {
      this.update(StandardProperties.WAIT, (opt) -> {
         return WaitOptions.create(opt.distance(), opt.delay(), opt.acceleration(), opt.deceleration(), use);
      });
   }

   public double getSpeedLimit() {
      return StandardProperties.SPEEDLIMIT.getDouble(this);
   }

   public void setSpeedLimit(double limit) {
      this.set(StandardProperties.SPEEDLIMIT, limit);
   }

   public double getGravity() {
      return StandardProperties.GRAVITY.getDouble(this);
   }

   public void setGravity(double gravity) {
      this.set(StandardProperties.GRAVITY, gravity);
   }

   public double getFriction() {
      return StandardProperties.FRICTION.getDouble(this);
   }

   public void setFriction(double friction) {
      this.set(StandardProperties.FRICTION, friction);
   }

   /** @deprecated */
   @Deprecated
   public boolean isSlowingDown() {
      return !((Set)this.get(StandardProperties.SLOWDOWN)).isEmpty();
   }

   public boolean isSlowingDownAll() {
      return ((Set)this.get(StandardProperties.SLOWDOWN)).equals(EnumSet.allOf(SlowdownMode.class));
   }

   public boolean isSlowingDownNone() {
      return ((Set)this.get(StandardProperties.SLOWDOWN)).isEmpty();
   }

   public void setSlowingDown(boolean slowingDown) {
      if (slowingDown) {
         this.set(StandardProperties.SLOWDOWN, EnumSet.allOf(SlowdownMode.class));
      } else {
         this.set(StandardProperties.SLOWDOWN, Collections.emptySet());
      }

   }

   public boolean isSlowingDown(SlowdownMode mode) {
      return ((Set)this.get(StandardProperties.SLOWDOWN)).contains(mode);
   }

   public void setSlowingDown(SlowdownMode mode, boolean slowingDown) {
      this.update(StandardProperties.SLOWDOWN, (curr_modes) -> {
         if (slowingDown == curr_modes.contains(mode)) {
            return curr_modes;
         } else {
            EnumSet<SlowdownMode> new_modes = EnumSet.noneOf(SlowdownMode.class);
            new_modes.addAll(curr_modes);
            LogicUtil.addOrRemove(new_modes, mode, slowingDown);
            return new_modes;
         }
      });
   }

   public String getDisplayName() {
      String name = (String)this.get(StandardProperties.DISPLAY_NAME);
      return name.isEmpty() ? this.getTrainName() : name;
   }

   public String getDisplayNameOrEmpty() {
      return (String)this.get(StandardProperties.DISPLAY_NAME);
   }

   public void setDisplayName(String displayName) {
      this.set(StandardProperties.DISPLAY_NAME, displayName);
   }

   public boolean isKeepingChunksLoaded() {
      return ((ChunkLoadOptions)this.get(StandardProperties.CHUNK_LOAD_OPTIONS)).keepLoaded();
   }

   public void setKeepChunksLoaded(boolean state) {
      this.setChunkLoadOptions(this.getChunkLoadOptions().withMode(state ? ChunkLoadOptions.Mode.FULL : ChunkLoadOptions.Mode.DISABLED));
   }

   public ChunkLoadOptions getChunkLoadOptions() {
      return (ChunkLoadOptions)this.get(StandardProperties.CHUNK_LOAD_OPTIONS);
   }

   public void setChunkLoadOptions(ChunkLoadOptions options) {
      this.set(StandardProperties.CHUNK_LOAD_OPTIONS, options);
   }

   public boolean isSoundEnabled() {
      return (Boolean)this.get(StandardProperties.SOUND_ENABLED);
   }

   public void setSoundEnabled(boolean enabled) {
      this.set(StandardProperties.SOUND_ENABLED, enabled);
   }

   public boolean add(CartProperties properties) {
      if (properties.group != null && properties.group != this) {
         properties.group.remove(properties);
      }

      properties.group = this;
      if (!super.add(properties)) {
         return false;
      } else {
         this.config.getNode("carts").set(properties.getUUID().toString(), properties.getConfig());
         return true;
      }
   }

   public boolean remove(Object o) {
      if (o instanceof MinecartMember) {
         o = ((MinecartMember)o).getProperties();
      }

      if (!super.remove(o)) {
         return false;
      } else {
         if (o instanceof CartProperties && this.config.isNode("carts")) {
            this.config.getNode("carts").remove(((CartProperties)o).getUUID().toString());
         }

         return true;
      }
   }

   public CartProperties get(int index) {
      int i = 0;
      Iterator var3 = this.iterator();

      CartProperties prop;
      do {
         if (!var3.hasNext()) {
            throw new IndexOutOfBoundsException("No cart properties found at index " + index);
         }

         prop = (CartProperties)var3.next();
      } while(i++ != index);

      return prop;
   }

   public void setPickup(boolean pickup) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties prop = (CartProperties)var2.next();
         prop.setPickup(pickup);
      }

   }

   public boolean isOwnedByEveryone() {
      return !this.hasOwners() && !this.hasOwnerPermissions();
   }

   public boolean hasOwners() {
      Iterator var1 = this.iterator();

      CartProperties prop;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         prop = (CartProperties)var1.next();
      } while(!prop.hasOwners());

      return true;
   }

   public boolean hasOwnership(Player player) {
      return CartProperties.hasGlobalOwnership(player) || this.isOwnedByEveryone() || this.isOwner(player);
   }

   public boolean isOwner(Player player) {
      Iterator var2 = this.iterator();

      CartProperties prop;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         prop = (CartProperties)var2.next();
      } while(!prop.isOwner(player));

      return true;
   }

   public boolean hasOwnerPermissions() {
      Iterator var1 = this.iterator();

      CartProperties prop;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         prop = (CartProperties)var1.next();
      } while(!prop.hasOwnerPermissions());

      return true;
   }

   public Set<String> getOwnerPermissions() {
      Set<String> rval = new HashSet();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties cprop = (CartProperties)var2.next();
         rval.addAll(cprop.getOwnerPermissions());
      }

      return rval;
   }

   public void setOwnerPermissions(Set<String> newOwnerPermissions) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties cprop = (CartProperties)var2.next();
         cprop.setOwnerPermissions(newOwnerPermissions);
      }

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
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties cprop = (CartProperties)var2.next();
         cprop.addOwners(ownersToAdd);
      }

   }

   public void removeOwners(Collection<String> ownersToRemove) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties cprop = (CartProperties)var2.next();
         cprop.removeOwners(ownersToRemove);
      }

   }

   public void clearOwnerPermissions() {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         CartProperties prop = (CartProperties)var1.next();
         prop.clearOwnerPermissions();
      }

   }

   public void addOwnerPermission(String permission) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties prop = (CartProperties)var2.next();
         prop.addOwnerPermission(permission);
      }

   }

   public void removeOwnerPermission(String permission) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties prop = (CartProperties)var2.next();
         prop.removeOwnerPermission(permission);
      }

   }

   public void setOwner(String player, boolean owner) {
      Iterator var3 = this.iterator();

      while(var3.hasNext()) {
         CartProperties cProp = (CartProperties)var3.next();
         cProp.setOwner(player, owner);
      }

   }

   public boolean isPlayerTakeable() {
      return (Boolean)this.get(StandardProperties.ALLOW_PLAYER_TAKE);
   }

   public void setPlayerTakeable(boolean takeable) {
      this.set(StandardProperties.ALLOW_PLAYER_TAKE, takeable);
   }

   public double getBankingStrength() {
      return ((BankingOptions)this.get(StandardProperties.BANKING)).strength();
   }

   public double getBankingSmoothness() {
      return ((BankingOptions)this.get(StandardProperties.BANKING)).smoothness();
   }

   public void setBanking(double strength, double smoothness) {
      this.set(StandardProperties.BANKING, BankingOptions.create(strength, smoothness));
   }

   public void setBankingStrength(double strength) {
      this.update(StandardProperties.BANKING, (opt) -> {
         return BankingOptions.create(strength, opt.smoothness());
      });
   }

   public void setBankingSmoothness(double smoothness) {
      this.update(StandardProperties.BANKING, (opt) -> {
         return BankingOptions.create(opt.strength(), smoothness);
      });
   }

   public boolean getCanOnlyOwnersEnter() {
      return (Boolean)this.get(StandardProperties.ONLY_OWNERS_CAN_ENTER);
   }

   public void setCanOnlyOwnersEnter(boolean state) {
      this.set(StandardProperties.ONLY_OWNERS_CAN_ENTER, state);
   }

   public void setEnterMessage(String message) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties prop = (CartProperties)var2.next();
         prop.setEnterMessage(message);
      }

   }

   public boolean matchTag(String tag) {
      Iterator var2 = this.iterator();

      CartProperties prop;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         prop = (CartProperties)var2.next();
      } while(!prop.matchTag(tag));

      return true;
   }

   public boolean hasTags() {
      Iterator var1 = this.iterator();

      CartProperties prop;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         prop = (CartProperties)var1.next();
      } while(!prop.hasTags());

      return true;
   }

   public Collection<String> getTags() {
      return (Collection)this.get(StandardProperties.TAGS);
   }

   public void setTags(String... tags) {
      this.set(StandardProperties.TAGS, new HashSet(Arrays.asList(tags)));
   }

   public void clearTags() {
      this.set(StandardProperties.TAGS, Collections.emptySet());
   }

   public void addTags(String... tags) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties prop = (CartProperties)var2.next();
         prop.addTags(tags);
      }

   }

   public void removeTags(String... tags) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties prop = (CartProperties)var2.next();
         prop.removeTags(tags);
      }

   }

   public boolean getPlayersEnter() {
      Iterator var1 = this.iterator();

      CartProperties prop;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         prop = (CartProperties)var1.next();
      } while(!prop.getPlayersEnter());

      return true;
   }

   public void setPlayersEnter(boolean state) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties prop = (CartProperties)var2.next();
         prop.setPlayersEnter(state);
      }

   }

   public boolean getPlayersExit() {
      Iterator var1 = this.iterator();

      CartProperties prop;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         prop = (CartProperties)var1.next();
      } while(!prop.getPlayersExit());

      return true;
   }

   public void setPlayersExit(boolean state) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties prop = (CartProperties)var2.next();
         prop.setPlayersExit(state);
      }

   }

   public boolean isInvincible() {
      Iterator var1 = this.iterator();

      CartProperties prop;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         prop = (CartProperties)var1.next();
      } while(!prop.isInvincible());

      return true;
   }

   public void setInvincible(boolean enabled) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties prop = (CartProperties)var2.next();
         prop.setInvincible(enabled);
      }

   }

   public boolean getSpawnItemDrops() {
      Iterator var1 = this.iterator();

      CartProperties prop;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         prop = (CartProperties)var1.next();
      } while(!prop.getSpawnItemDrops());

      return true;
   }

   public void setSpawnItemDrops(boolean spawnDrops) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties prop = (CartProperties)var2.next();
         prop.setSpawnItemDrops(spawnDrops);
      }

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
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties prop = (CartProperties)var2.next();
         prop.addDestinationToRoute(destination);
      }

   }

   public void removeDestinationFromRoute(String destination) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties prop = (CartProperties)var2.next();
         prop.removeDestinationFromRoute(destination);
      }

   }

   public int getCurrentRouteDestinationIndex() {
      Iterator var1 = this.iterator();

      CartProperties prop;
      do {
         if (!var1.hasNext()) {
            return -1;
         }

         prop = (CartProperties)var1.next();
      } while(prop.getDestinationRoute().isEmpty());

      return prop.getCurrentRouteDestinationIndex();
   }

   public String getNextDestinationOnRoute() {
      Iterator var1 = this.iterator();

      CartProperties prop;
      do {
         if (!var1.hasNext()) {
            return "";
         }

         prop = (CartProperties)var1.next();
      } while(prop.getDestinationRoute().isEmpty());

      return prop.getNextDestinationOnRoute();
   }

   public String getNextDestinationOnRoute(String currentDestination) {
      Iterator var2 = this.iterator();

      CartProperties prop;
      do {
         if (!var2.hasNext()) {
            return "";
         }

         prop = (CartProperties)var2.next();
      } while(prop.getDestinationRoute().isEmpty());

      return prop.getNextDestinationOnRoute(currentDestination);
   }

   public void clearDestination() {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         CartProperties prop = (CartProperties)var1.next();
         prop.clearDestination();
      }

   }

   public String getLastPathNode() {
      return this.isEmpty() ? "" : this.get(0).getLastPathNode();
   }

   public void setLastPathNode(String nodeName) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         CartProperties cprop = (CartProperties)var2.next();
         cprop.setLastPathNode(nodeName);
      }

   }

   public boolean isPoweredMinecartRequired() {
      return (Boolean)this.get(StandardProperties.REQUIRE_POWERED_MINECART);
   }

   public void setPoweredMinecartRequired(boolean required) {
      this.set(StandardProperties.REQUIRE_POWERED_MINECART, required);
   }

   public double getCollisionDamage() {
      return (Double)this.get(StandardProperties.COLLISION_DAMAGE);
   }

   public void setCollisionDamage(double collisionDamage) {
      this.set(StandardProperties.COLLISION_DAMAGE, collisionDamage);
   }

   public CollisionMode getCollisionMode(Entity entity) {
      if (entity.isDead()) {
         return CollisionMode.CANCEL;
      } else {
         MinecartMember<?> member = MinecartMemberStore.getFromEntity(entity);
         CollisionOptions collision = this.getCollision();
         if (member != null) {
            if (collision.trainMode() == CollisionMode.LINK) {
               return member.getGroup().getProperties().getCollision().trainMode() == CollisionMode.LINK ? CollisionMode.LINK : CollisionMode.CANCEL;
            } else {
               return collision.trainMode();
            }
         } else if (!(entity instanceof Player)) {
            return collision.forEntity(entity);
         } else {
            GameMode playerGameMode = ((Player)entity).getGameMode();
            if (playerGameMode == GameMode.SPECTATOR) {
               return CollisionMode.CANCEL;
            } else {
               if (TCConfig.collisionIgnoreOwners && collision.playerMode() != CollisionMode.DEFAULT) {
                  if (TCConfig.collisionIgnoreGlobalOwners && CartProperties.hasGlobalOwnership((Player)entity)) {
                     return CollisionMode.DEFAULT;
                  }

                  if (this.hasOwnership((Player)entity)) {
                     return CollisionMode.DEFAULT;
                  }
               }

               return playerGameMode != GameMode.CREATIVE || collision.playerMode() != CollisionMode.KILL && collision.playerMode() != CollisionMode.KILLNODROPS && collision.playerMode() != CollisionMode.DAMAGE && collision.playerMode() != CollisionMode.DAMAGENODROPS ? collision.playerMode() : CollisionMode.PUSH;
            }
         }
      }
   }

   public String getTrainName() {
      return this.trainname;
   }

   public void setTrainName(String newTrainName) {
      rename(this, newTrainName);
   }

   /** @deprecated */
   @Deprecated
   public TrainProperties setName(String newtrainname) {
      this.setTrainName(newtrainname);
      return this;
   }

   public boolean hasSuffocation() {
      return (Boolean)this.get(StandardProperties.SUFFOCATION);
   }

   public void setSuffocation(boolean suffocation) {
      this.set(StandardProperties.SUFFOCATION, suffocation);
   }

   public boolean isManualMovementAllowed() {
      return (Boolean)this.get(StandardProperties.ALLOW_PLAYER_MANUAL_MOVEMENT);
   }

   public void setManualMovementAllowed(boolean allow) {
      this.set(StandardProperties.ALLOW_PLAYER_MANUAL_MOVEMENT, allow);
   }

   public boolean isMobManualMovementAllowed() {
      return (Boolean)this.get(StandardProperties.ALLOW_MOB_MANUAL_MOVEMENT);
   }

   public void setMobManualMovementAllowed(boolean allow) {
      this.set(StandardProperties.ALLOW_MOB_MANUAL_MOVEMENT, allow);
   }

   public boolean hasRealtimePhysics() {
      return (Boolean)this.get(StandardProperties.REALTIME_PHYSICS);
   }

   public void setRealtimePhysics(boolean realtime) {
      this.set(StandardProperties.REALTIME_PHYSICS, realtime);
   }

   public Set<String> getTickets() {
      return (Set)this.get(StandardProperties.TICKETS);
   }

   public void addTicket(String ticketName) {
      this.update(StandardProperties.TICKETS, (tickets) -> {
         if (tickets.contains(ticketName)) {
            return tickets;
         } else {
            HashSet<String> new_tickets = new HashSet(tickets);
            new_tickets.add(ticketName);
            return new_tickets;
         }
      });
   }

   public void removeTicket(String ticketName) {
      this.update(StandardProperties.TICKETS, (tickets) -> {
         if (!tickets.contains(ticketName)) {
            return tickets;
         } else {
            HashSet<String> new_tickets = new HashSet(tickets);
            new_tickets.remove(ticketName);
            return new_tickets;
         }
      });
   }

   public void clearTickets() {
      this.set(StandardProperties.TICKETS, Collections.emptySet());
   }

   public SignSkipOptions getSkipOptions() {
      return (SignSkipOptions)this.get(StandardProperties.SIGN_SKIP);
   }

   public void setSkipOptions(SignSkipOptions options) {
      this.set(StandardProperties.SIGN_SKIP, options);
   }

   public String getKillMessage() {
      return (String)this.get(StandardProperties.KILL_MESSAGE);
   }

   public void setKillMessage(String killMessage) {
      this.set(StandardProperties.KILL_MESSAGE, killMessage);
   }

   public boolean isTrainRenamed() {
      return !TrainNameFormat.DEFAULT.matches(this.getTrainName());
   }

   public boolean isLoaded() {
      return this.hasHolder();
   }

   public boolean matchName(String expression) {
      return Util.matchText(this.getTrainName(), expression);
   }

   public boolean matchName(String[] expressionElements, boolean firstAny, boolean lastAny) {
      return Util.matchText(this.getTrainName(), expressionElements, firstAny, lastAny);
   }

   public BlockLocation getLocation() {
      Iterator var1 = this.iterator();
      if (var1.hasNext()) {
         CartProperties prop = (CartProperties)var1.next();
         return prop.getLocation();
      } else {
         return null;
      }
   }

   public void setDefault() {
      this.setDefault("default");
   }

   public void setDefault(String key) {
      DefaultProperties defaults = getDefaultsByName(key);
      if (defaults != null) {
         this.apply(defaults);
      }

   }

   public void setDefault(Player player) {
      if (player == null) {
         this.setDefault();
      } else {
         this.apply(getDefaultsByPlayer(player));
      }

   }

   /** @deprecated */
   @Deprecated
   public void setDefault(ConfigurationNode node) {
      this.apply(node);
   }

   public void tryUpdate() {
      MinecartGroup g = this.getHolder();
      if (g != null) {
         g.onPropertiesChanged();
      }

   }

   public boolean parseSet(String key, String arg) {
      return this.parseAndSet(key, arg).getReason() != PropertyParseResult.Reason.PROPERTY_NOT_FOUND;
   }

   public CollisionOptions getCollision() {
      return (CollisionOptions)this.get(StandardProperties.COLLISION);
   }

   public void setCollision(CollisionOptions collisionConfig) {
      this.set(StandardProperties.COLLISION, collisionConfig);
   }

   public void setCollisionMode(CollisionMobCategory mobCategory, CollisionMode mode) {
      this.update(StandardProperties.COLLISION, (opt) -> {
         return opt.cloneAndSetMobMode(mobCategory, mode);
      });
   }

   public boolean setCollisionMode(String key, String value) {
      key = key.toLowerCase(Locale.ENGLISH);
      value = value.toLowerCase(Locale.ENGLISH);
      String mobType;
      CollisionMode mode;
      if (key.startsWith("push") && key.length() > 4) {
         mobType = key.substring(4);
         if (ParseUtil.isBool(value)) {
            mode = CollisionMode.fromPushing(ParseUtil.parseBool(value));
         } else {
            mode = CollisionMode.parse(value);
         }

         return this.updateCollisionProperties(mobType, mode);
      } else if (key.endsWith("collision") && key.length() > 9) {
         mobType = key.substring(0, key.length() - 9);
         mode = CollisionMode.parse(value);
         return this.updateCollisionProperties(mobType, mode);
      } else {
         return false;
      }
   }

   public boolean updateCollisionProperties(String mobType, CollisionMode mode) {
      if (mode == null) {
         return false;
      } else if (!mobType.equals("mob") && !mobType.equals("mobs")) {
         CollisionMobCategory[] var3 = CollisionMobCategory.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            CollisionMobCategory mobCategory = var3[var5];
            if (mobType.equals(mobCategory.getMobType()) || mobType.equals(mobCategory.getPluralMobType())) {
               this.setCollisionMode(mobCategory, mode);
               return true;
            }
         }

         return false;
      } else {
         this.setCollisionModeForMobs(mode);
         return true;
      }
   }

   public void setCollisionModeForMobs(CollisionMode mode) {
      this.setCollision(this.getCollision().cloneAndSetForAllMobs(mode));
   }

   public void setCollisionModeIfModeForMobs(CollisionMode expected, CollisionMode mode) {
      this.setCollision(this.getCollision().cloneCompareAndSetForAllMobs(expected, mode));
   }

   public void setLinking(boolean linking) {
      this.update(StandardProperties.COLLISION, (opt) -> {
         if (linking) {
            return opt.cloneAndSetTrainMode(CollisionMode.LINK);
         } else {
            return opt.trainMode() == CollisionMode.LINK ? opt.cloneAndSetTrainMode(CollisionMode.DEFAULT) : opt;
         }
      });
   }

   public void load(TrainProperties source) {
      this.load(source.getConfig());
   }

   public void load(ConfigurationNode node) {
      Iterator var2 = (new ArrayList(this.config.getKeys())).iterator();

      while(var2.hasNext()) {
         String key = (String)var2.next();
         if (!"carts".equals(key)) {
            this.config.remove(key);
         }
      }

      node.cloneIntoExcept(this.config, Collections.singleton("carts"));
      this.onConfigurationChanged(false);
   }

   public void save(ConfigurationNode node) {
      this.getConfig().cloneInto(node);
   }

   protected void onConfigurationChanged(boolean cartsChanged) {
      Iterator var2 = IPropertyRegistry.instance().all().iterator();

      while(var2.hasNext()) {
         IProperty<?> property = (IProperty)var2.next();
         property.onConfigurationChanged(this);
      }

      if (cartsChanged) {
         var2 = this.iterator();

         while(var2.hasNext()) {
            CartProperties cart = (CartProperties)var2.next();
            cart.onConfigurationChanged();
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public ConfigurationNode saveToConfig() {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         CartProperties cProp = (CartProperties)var1.next();
         cProp.saveToConfig();
      }

      return this.config;
   }

   public void apply(ConfigurationNode node) {
      if (node != null) {
         DefaultProperties.of(node).applyTo(this);
      }

   }

   public void apply(DefaultProperties defaultProperties) {
      defaultProperties.applyTo(this);
   }
}
