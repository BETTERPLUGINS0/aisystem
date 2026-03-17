package advancedplugins.pm2.cv.vehicle;

import advancedplugins.pm2.cv.InfiniteVehiclesPlugin;
import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.configuration.Configuration;
import advancedplugins.pm2.cv.api.enums.EnumExitShortcut;
import advancedplugins.pm2.cv.api.enums.EnumInjectorPriority;
import advancedplugins.pm2.cv.api.event.VehicleClickedEvent;
import advancedplugins.pm2.cv.api.event.VehicleOperatorSetEvent;
import advancedplugins.pm2.cv.api.event.VehiclePassengerSetEvent;
import advancedplugins.pm2.cv.api.event.VehicleSpawnEvent;
import advancedplugins.pm2.cv.api.event.wrapper.WrapperEntityMountEvent;
import advancedplugins.pm2.cv.api.handler.VehicleHandler;
import advancedplugins.pm2.cv.api.item.ItemConfiguration;
import advancedplugins.pm2.cv.api.util.Run;
import advancedplugins.pm2.cv.api.util.inventory.ItemStackUtil;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.VehicleSeat;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleConfiguration;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerInput;
import advancedplugins.pm2.cv.api.vehicle.item.storage.VehicleItemHolder;
import advancedplugins.pm2.cv.enums.EnumPacketType;
import advancedplugins.pm2.cv.handler.PluginHandlerAdapter;
import advancedplugins.pm2.cv.handler.PluginHandlerOptions;
import advancedplugins.pm2.cv.packet.PacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.EntityActionWrapper;
import advancedplugins.pm2.cv.packet.incoming.InteractPacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.PlayerActionPacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.PlayerInputPacketWrapper;
import advancedplugins.pm2.cv.service.PacketService;
import advancedplugins.pm2.cv.util.Constants;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jeff_media.morepersistentdatatypes.DataType;
import gnu.trove.map.hash.THashMap;
import io.netty.channel.Channel;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@PluginHandlerOptions(
   apiClass = VehicleHandler.class,
   packetInjector = true,
   eventListener = true
)
public final class VehicleHandlerImpl extends PluginHandlerAdapter implements VehicleHandler, Runnable {
   final DamageSubHandler damageSubHandler = new DamageSubHandler(this);
   final Set<Vehicle> vehicles = Sets.newConcurrentHashSet();
   final Map<UUID, Set<Vehicle>> vehiclesByWorld = new THashMap();
   final Map<UUID, Vehicle> vehiclesByOperator = new THashMap();
   final Map<UUID, Vehicle> vehiclesByPassengers = new THashMap();
   private final PacketService packetService = (PacketService)InfiniteVehicles.getService(PacketService.class);
   private final PersistenceSubHandler persistenceHandler = new PersistenceSubHandler(this);
   private final InteractionSubHandler interactionHandler = new InteractionSubHandler(this);
   private final MountingSubHandler mountingSubHandler = new MountingSubHandler();
   private final GuiSubHandler guiSubHandler = new GuiSubHandler(this);
   private final Map<UUID, VehicleHandlerImpl.UnmountInput> lastUnmountInput = Maps.newConcurrentMap();

   public VehicleHandlerImpl(JavaPlugin plugin) {
      super(EnumInjectorPriority.LOWEST, EnumPacketType.INCOMING_INTERACT, EnumPacketType.INCOMING_SWING_ARM, EnumPacketType.INCOMING_PLAYER_INPUT, EnumPacketType.INCOMING_PLAYER_ACTION, EnumPacketType.INCOMING_ENTITY_ACTION, EnumPacketType.OUTGOING_UPDATE_ENTITY_POSITION);
      Run.timerAsynchronously(this, 0L, 0L);
   }

   public void onPluginDisable() {
      this.persistenceHandler.processPluginDisabled();
   }

   @NotNull
   public Set<Vehicle> getRegisteredVehicles() {
      return Collections.unmodifiableSet(this.vehicles);
   }

   @Nullable
   public Vehicle getVehicleByOperator(@NotNull Entity operator) {
      return (Vehicle)this.vehiclesByOperator.get(var1.getUniqueId());
   }

   public void openVehicleGui(@NotNull Vehicle vehicle, @NotNull Player player) {
      this.guiSubHandler.openVehicleGui(var1, var2);
   }

   public void pickupVehicle(@NotNull Vehicle vehicle, @NotNull Player player, boolean force, boolean dropIfNecessary) {
      Location var5 = var1.getLocation();
      ItemConfiguration var6 = var1.getConfiguration().pickupItem();
      if (var6 != null || var3) {
         this.destroyVehicle(var1);
         Run.syncDelayed(() -> {
            var1.getStorage().forEach((var2x) -> {
               Arrays.stream(var2x.getHolder().getInventory().getContents()).filter(Objects::nonNull).filter((var0) -> {
                  return !var0.equals(VehicleItemHolder.BLOCKING_ITEM);
               }).forEach((var2xx) -> {
                  var2.getInventory().addItem(new ItemStack[]{var2xx}).forEach((var2x, var3) -> {
                     var2.getWorld().dropItemNaturally(var5, var3);
                  });
               });
            });
         }, 2L);
         if (var6 != null) {
            PlayerInventory var7 = var2.getInventory();
            int var8 = var7.firstEmpty();
            ItemStack var9 = var6.getItemStack();
            ItemStackUtil.setPersistentData(var9, Constants.NamespacedKeys.VEHICLE_UPGRADES_DATA, DataType.asMap(DataType.STRING, DataType.INTEGER), var1.getUpgradeTiers());
            ItemStackUtil.setPersistentData(var9, Constants.NamespacedKeys.VEHICLE_FUEL_AMOUNT, PersistentDataType.FLOAT, var1.getFuelLevel());
            if (var8 != -1) {
               Run.sync(() -> {
                  var7.setItem(var8, var9);
               });
            } else if (var4) {
               Run.sync(() -> {
                  var2.getWorld().dropItem(var5, var9);
               });
            }
         }

         World var15 = (World)Objects.requireNonNull(var5.getWorld());
         float var18;
         if (Configuration.PICKUP_PARTICLE_ENABLE.booleanValue()) {
            Particle var16 = (Particle)Configuration.PICKUP_PARTICLE_TYPE.enumValue(Particle.class);
            var18 = Configuration.PICKUP_PARTICLE_DISPERSION.floatValue();
            int var10 = Configuration.PICKUP_PARTICLE_AMOUNT.intValue();
            if (var16 != null && var10 > 0) {
               var15.spawnParticle(var16, var5, var10, (double)var18, (double)var18, (double)var18, 0.0D, (Object)null);
            }
         }

         if (Configuration.PICKUP_SOUND_ENABLE.booleanValue()) {
            String var17 = Configuration.PICKUP_SOUND_TYPE.stringValue();
            var18 = Configuration.PICKUP_SOUND_VOLUME.floatValue();
            float var19 = Configuration.PICKUP_SOUND_PITCH.floatValue();
            if (StringUtils.isNotBlank(var17) && var18 > 0.0F) {
               Sound var11;
               try {
                  var11 = (Sound)Sound.class.getField(var17.toUpperCase()).get((Object)null);
               } catch (NoSuchFieldException | IllegalAccessException var14) {
                  var11 = null;
               }

               if (var11 != null) {
                  Run.sync(() -> {
                     var15.playSound(var5, var11, var18, var19);
                  });
               } else {
                  try {
                     Run.sync(() -> {
                        var15.playSound(var5, var17.toLowerCase(), var18, var19);
                     });
                  } catch (Exception var13) {
                     InfiniteVehiclesPlugin.getInstance().getLogger().warning("Unable to play sound: " + var17 + " (" + var13.getMessage() + ")");
                  }
               }
            }
         }

      }
   }

   @NotNull
   public Vehicle spawnVehicle(@NotNull VehicleConfiguration configuration, @NotNull World world, double x, double y, double z, @Nullable UUID uniqueId, @Nullable UUID ownerUniqueId) {
      VehicleImpl var11 = new VehicleImpl(this, var1, var2, var3, var5, var7, var9, var10);
      if (!(new VehicleSpawnEvent(var11)).callEvent()) {
         return var11;
      } else {
         var11.spawn();
         this.register(var11);
         return var11;
      }
   }

   public void removeVehicle(@NotNull Vehicle vehicle) {
      if (var1 instanceof VehicleImpl) {
         var1.remove();
      }

      this.unregister(var1);
   }

   public void destroyVehicle(@NotNull Vehicle vehicle) {
      if (var1 instanceof VehicleImpl) {
         ((VehicleImpl)var1).destroy(false);
      }

      this.unregister(var1);
   }

   @Nullable
   public Vehicle getVehicleByPassenger(@NotNull Entity passenger) {
      return (Vehicle)this.vehiclesByPassengers.get(var1.getUniqueId());
   }

   public void register(@NotNull Vehicle vehicle) {
      if (this.vehicles.add(var1)) {
         ((Set)this.vehiclesByWorld.computeIfAbsent(var1.getWorld().getUID(), (var0) -> {
            return Sets.newConcurrentHashSet();
         })).add(var1);
      }

   }

   public void unregister(@NotNull Vehicle vehicle, boolean keepPersistence) {
      if (var2) {
         if (var1.isPersistent()) {
            this.persistenceHandler.processSave(var1.getWorld());
         }
      } else {
         this.persistenceHandler.processUnregisteredVehicle(var1);
      }

      this.vehicles.remove(var1);
      Set var3 = (Set)this.vehiclesByWorld.get(var1.getWorld().getUID());
      if (var3 != null) {
         var3.remove(var1);
      }

      this.vehiclesByOperator.entrySet().removeIf((var1x) -> {
         return Objects.equals(var1x.getValue(), var1);
      });
      this.vehiclesByPassengers.entrySet().removeIf((var1x) -> {
         return Objects.equals(var1x.getValue(), var1);
      });
   }

   void processVehicleWorldChanged(@NotNull VehicleImpl vehicle, @NotNull World from, @NotNull World to) {
      Set var4 = (Set)this.vehiclesByWorld.get(var2.getUID());
      if (var4 != null) {
         var4.remove(var1);
      }

      var4 = (Set)this.vehiclesByWorld.computeIfAbsent(var3.getUID(), (var0) -> {
         return Sets.newConcurrentHashSet();
      });
      var4.add(var1);
      this.persistenceHandler.processVehicleWorldChanged(var1, var2, var3);
   }

   void processVehiclePersistenceChanged(@NotNull VehicleImpl vehicle, boolean persistent) {
      this.persistenceHandler.processVehiclePersistenceChanged(var1, var2);
   }

   void processWorldUnloaded(@NotNull World world) {
      this.persistenceHandler.processUnloadedWorld(var1);
      Set var2 = (Set)this.vehiclesByWorld.remove(var1.getUID());
      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Vehicle var4 = (Vehicle)var3.next();
            if (var4 instanceof VehicleImpl) {
               var4.despawn();
            }
         }

         this.vehicles.removeAll(var2);
         this.vehiclesByOperator.entrySet().removeIf((var1x) -> {
            return var2.contains(var1x.getValue());
         });
         this.vehiclesByPassengers.entrySet().removeIf((var1x) -> {
            return var2.contains(var1x.getValue());
         });
      }

   }

   public void run() {
      this.vehicles.forEach((var0) -> {
         if (!(var0 instanceof VehicleImpl) || ((VehicleImpl)var0).isSpawned()) {
            if (!var0.isRemoved()) {
               var0.tick();
            }

         }
      });
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onSave(WorldSaveEvent event) {
      this.persistenceHandler.processSave(var1.getWorld());
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onLoad(ChunkLoadEvent event) {
      Chunk var2 = var1.getChunk();
      File var3 = var1.getWorld().getWorldFolder();
      this.persistenceHandler.processLoadedChunk(var1.getWorld(), var3, var2.getX(), var2.getZ());
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onConnect(PlayerJoinEvent event) {
      Player var2 = var1.getPlayer();
      this.persistenceHandler.processPlayerConnecting(var2);
      this.interactionHandler.processPlayerConnecting(var2);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onMove(PlayerMoveEvent event) {
      Player var2 = var1.getPlayer();
      this.persistenceHandler.processPlayerMoving(var1);
      this.interactionHandler.processPlayerChangingLocation(var2);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onChangeWorld(PlayerChangedWorldEvent event) {
      Player var2 = var1.getPlayer();
      this.interactionHandler.processPlayerChangingWorld(var2, var2.getWorld());
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onWorldUnload(WorldUnloadEvent event) {
      this.processWorldUnloaded(var1.getWorld());
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDisconnect(PlayerQuitEvent event) {
      Player var2 = var1.getPlayer();
      this.interactionHandler.processPlayerDisconnecting(var2);
      Vehicle var3 = this.getVehicleByOperator(var2);
      if (var3 != null) {
         var3.setOperator((Entity)null);
      } else if ((var3 = this.getVehicleByPassenger(var2)) != null) {
         Iterator var4 = var3.getSeats().iterator();

         while(var4.hasNext()) {
            VehicleSeat var5 = (VehicleSeat)var4.next();
            Entity var6 = var5.getPassenger();
            if (var6 != null && Objects.equals(var6.getUniqueId(), var2.getUniqueId())) {
               var5.setPassenger((Entity)null);
               break;
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onPlayerDeath(PlayerDeathEvent event) {
      Player var2 = var1.getEntity();
      Vehicle var3 = this.getVehicleByOperator(var2);
      if (var3 != null) {
         var3.setOperator((Entity)null);
      } else if ((var3 = this.getVehicleByPassenger(var2)) != null) {
         Iterator var4 = var3.getSeats().iterator();

         while(var4.hasNext()) {
            VehicleSeat var5 = (VehicleSeat)var4.next();
            Entity var6 = var5.getPassenger();
            if (var6 != null && Objects.equals(var6.getUniqueId(), var2.getUniqueId())) {
               var5.setPassenger((Entity)null);
               break;
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onPlayerSneak(PlayerToggleSneakEvent event) {
      Player var2 = var1.getPlayer();
      Vehicle var3 = this.getVehicleByPassenger(var2);
      if (var3 != null) {
         var1.setCancelled(true);
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onExit(VehicleExitEvent event) {
      LivingEntity var3 = var1.getExited();
      if (var3 instanceof Player) {
         Player var2 = (Player)var3;
         Vehicle var7 = this.getVehicleByPassenger(var2);
         if (var7 != null) {
            Iterator var4 = var7.getSeats().iterator();

            while(var4.hasNext()) {
               VehicleSeat var5 = (VehicleSeat)var4.next();
               Entity var6 = var5.getPassenger();
               if (var6 != null && Objects.equals(var6.getUniqueId(), var2.getUniqueId())) {
                  var5.setPassenger((Entity)null);
                  break;
               }
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onPassengerSet(VehiclePassengerSetEvent event) {
      Entity var2 = var1.getPreviousOperator();
      Entity var3 = var1.getNewOperator();
      if (var2 != null) {
         this.vehiclesByPassengers.remove(var2.getUniqueId());
      }

      if (var3 != null) {
         this.vehiclesByPassengers.put(var3.getUniqueId(), var1.getVehicle());
      }

   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onEntityMount(WrapperEntityMountEvent event) {
      var1.getEntity().setMetadata("NPC", new FixedMetadataValue(InfiniteVehiclesPlugin.getInstance(), true));
      Run.syncDelayed(() -> {
         var1.getEntity().removeMetadata("NPC", InfiniteVehiclesPlugin.getInstance());
      }, 1L);
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onOperatorSet(VehicleOperatorSetEvent event) {
      Entity var2 = var1.getPreviousOperator();
      Entity var3 = var1.getNewOperator();
      if (var2 != null) {
         this.vehiclesByOperator.remove(var2.getUniqueId());
      }

      if (var3 != null) {
         this.vehiclesByOperator.put(var3.getUniqueId(), var1.getVehicle());
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onClick(VehicleClickedEvent event) {
      this.mountingSubHandler.processClickedVehicle(var1);
      this.guiSubHandler.processClickedVehicle(var1);
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onHit(ProjectileHitEvent event) {
      Projectile var2 = var1.getEntity();
      ProjectileSource var3 = var2.getShooter();
      Entity var4 = var1.getHitEntity();
      if (var4 != null && var3 instanceof Entity) {
         List var5 = var4.getMetadata("crafty-vehicles-damage-hitbox-handle");
         MetadataValue var6 = var5.size() > 0 ? (MetadataValue)var5.get(0) : null;
         Object var7 = var6 != null ? var6.value() : null;
         if (var6 != null && var7 instanceof VehicleImpl) {
            VehicleImpl var8 = (VehicleImpl)var7;
            Iterator var9 = var8.seats.iterator();

            while(var9.hasNext()) {
               VehicleSeatImpl var10 = (VehicleSeatImpl)var9.next();
               if (Objects.equals(var10.getPassenger(), var3)) {
                  var1.setCancelled(true);
                  break;
               }
            }
         }

      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onSplit(SlimeSplitEvent event) {
      this.damageSubHandler.processSlimeSplit(var1);
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onDying(EntityDeathEvent event) {
      this.damageSubHandler.processDeath(var1);
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onProjectile(PlayerInteractEvent event) {
      Player var2 = var1.getPlayer();
      Vehicle var3 = this.getVehicleByOperator(var2);
      if (var3 != null) {
         Action var4 = var1.getAction();
         PlayerInput.InputType var5 = var4.name().contains("RIGHT") ? PlayerInput.InputType.RIGHT_CLICK : (var4.name().contains("LEFT") ? PlayerInput.InputType.LEFT_CLICK : null);
         if (var5 != null) {
            ((VehicleImpl)var3).vehicleInteraction.addSecondaryBinding(var2, var5);
            ((VehicleImpl)var3).vehicleInteraction.addPrimaryBinding(var2, var5);
         }
      }
   }

   @Nullable
   public Object onPacketReceive(@Nullable Player sender, @NotNull Channel channel, @NotNull Object packet) {
      PacketWrapper var4 = PacketWrapper.of(var3);
      if (var4 instanceof PlayerInputPacketWrapper && var1 != null) {
         return this.processSteerInput(var1, var3, (PlayerInputPacketWrapper)var4);
      } else if (var4 instanceof EntityActionWrapper && var1 != null) {
         return this.processAction(var1, var3, (EntityActionWrapper)var4);
      } else if (var4 instanceof InteractPacketWrapper && var1 != null) {
         return this.interactionHandler.processInteractionPacket(var1, var3, (InteractPacketWrapper)var4);
      } else {
         if (var4 instanceof PlayerActionPacketWrapper && var1 != null) {
            this.processInput(var1, (PlayerActionPacketWrapper)var4);
         }

         return var3;
      }
   }

   @Nullable
   private Object processAction(@NotNull Player player, @NotNull Object packet, EntityActionWrapper wrapper) {
      boolean var4 = false;
      boolean var5 = false;
      if (this.getVehicleByOperator(var1) != null) {
         var4 = true;
      } else if (this.getVehicleByPassenger(var1) == null) {
         return var2;
      }

      return var3.getAction() != EntityActionWrapper.Action.START_SNEAKING ? var2 : null;
   }

   @NotNull
   private Object processSteerInput(@NotNull Player player, @NotNull Object packet, @NotNull PlayerInputPacketWrapper wrapper) {
      boolean var5 = false;
      boolean var6 = false;
      Vehicle var4;
      if ((var4 = this.getVehicleByOperator(var1)) != null) {
         var5 = true;
      } else if ((var4 = this.getVehicleByPassenger(var1)) == null) {
         return var2;
      }

      VehicleConfiguration var7 = var4.getConfiguration();
      EnumExitShortcut var8 = var5 ? var7.getOperatorExitShortcut() : var7.getPassengerExitShortcut();
      switch(var8) {
      case CROUCH:
         var6 = var3.unmount;
         break;
      case CROUCH_TWICE:
         VehicleHandlerImpl.UnmountInput var9 = (VehicleHandlerImpl.UnmountInput)this.lastUnmountInput.get(var1.getUniqueId());
         if (var3.unmount && var9 != null && !var9.value && System.currentTimeMillis() - var9.timestamp < 500L) {
            var6 = true;
         }

         if (var9 == null || var9.value != var3.unmount) {
            this.lastUnmountInput.put(var1.getUniqueId(), new VehicleHandlerImpl.UnmountInput(System.currentTimeMillis(), var3.unmount));
         }
         break;
      case JUMP:
         var6 = var3.jump;
         break;
      case CROUCH_JUMP:
         var6 = var3.unmount && var3.jump;
      }

      PlayerInput.InputType var14 = var3.jump && !var3.unmount ? PlayerInput.InputType.JUMP : (var3.unmount && !var3.jump ? PlayerInput.InputType.CROUCH : null);
      if (var14 != null) {
         ((VehicleImpl)var4).vehicleInteraction.addPrimaryBinding(var1, var14);
         ((VehicleImpl)var4).vehicleInteraction.addSecondaryBinding(var1, var14);
      }

      Optional var10 = Optional.empty();
      Iterator var11 = var4.getSeats().iterator();

      while(var11.hasNext()) {
         VehicleSeat var12 = (VehicleSeat)var11.next();
         Entity var13 = var12.getPassenger();
         if (var13 != null && Objects.equals(var13.getUniqueId(), var1.getUniqueId())) {
            var10 = Optional.of(var12);
            break;
         }
      }

      if (var6) {
         Run.sync(() -> {
            var10.ifPresent((var0) -> {
               var0.setPassenger((Entity)null);
            });
         });
      }

      if (var5 && !var6) {
         var4.input(var3.toSteerInput());
      }

      return var3.unmount ? this.packetService.createInstance(new PlayerInputPacketWrapper(var3.sideways, var3.forward, var3.jump, false)) : var2;
   }

   @Nullable
   public Object onPacketSend(@Nullable Player receiver, @NotNull Channel channel, @NotNull Object packet) {
      return super.onPacketSend(var1, var2, var3);
   }

   private void processInput(@NotNull Player player, @NotNull PlayerActionPacketWrapper wrapper) {
      Vehicle var3 = this.getVehicleByOperator(var1);
      if (var3 != null && var2.action == PlayerActionPacketWrapper.Action.SWAP_ITEM_WITH_OFFHAND) {
         var3.input(new PlayerInput(false, false, true, false, false));
         var3.getProjectileShooters().forEach((var2x) -> {
            ((VehicleImpl)var3).vehicleInteraction.addPrimaryBinding(var1, PlayerInput.InputType.SWAP_OFFHAND);
            ((VehicleImpl)var3).vehicleInteraction.addSecondaryBinding(var1, PlayerInput.InputType.SWAP_OFFHAND);
         });
      }

   }

   private static class UnmountInput {
      private final long timestamp;
      private final boolean value;

      public UnmountInput(long timestamp, boolean value) {
         this.timestamp = var1;
         this.value = var3;
      }

      public String toString() {
         return "VehicleHandlerImpl.UnmountInput(timestamp=" + this.timestamp + ", value=" + this.value + ")";
      }
   }
}
