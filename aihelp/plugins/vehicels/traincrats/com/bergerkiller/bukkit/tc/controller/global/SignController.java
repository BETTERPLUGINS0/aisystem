package com.bergerkiller.bukkit.tc.controller.global;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.bases.IntVector2;
import com.bergerkiller.bukkit.common.block.SignChangeTracker;
import com.bergerkiller.bukkit.common.collections.FastTrackedUpdateSet;
import com.bergerkiller.bukkit.common.collections.FastTrackedUpdateSet.Tracker;
import com.bergerkiller.bukkit.common.component.LibraryComponent;
import com.bergerkiller.bukkit.common.events.MultiBlockChangeEvent;
import com.bergerkiller.bukkit.common.events.SignEditTextEvent;
import com.bergerkiller.bukkit.common.events.SignEditTextEvent.EditReason;
import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.common.wrappers.HumanHand;
import com.bergerkiller.bukkit.tc.PowerState;
import com.bergerkiller.bukkit.tc.SignActionHeader;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignBuildEvent;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.signactions.util.SignActionLookupMap;
import com.bergerkiller.bukkit.tc.utils.RecursionGuard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class SignController implements LibraryComponent, Listener {
   private static final int MAX_REDSTONE_UPDATES_PER_TICK = 100000;
   private static final boolean CAN_DEBUG_DISPLAY_SIGNS = Common.hasCapability("Common:BlockData:GetInteractableBox");
   private final TrainCarts plugin;
   private final SignControllerWorld NONE = new SignControllerWorld(this);
   private final IdentityHashMap<World, SignControllerWorld> byWorld = new IdentityHashMap();
   private int pendingRedstoneUpdatesThisTick = 0;
   private final FastTrackedUpdateSet<SignController.Entry> pendingRedstoneUpdates = new FastTrackedUpdateSet();
   private final FastTrackedUpdateSet<SignController.Entry> ignoreRedstoneUpdates = new FastTrackedUpdateSet();
   private final boolean blockPhysicsFireForSigns;
   private boolean enabled = true;
   private SignControllerWorld byWorldLastGet;
   private final SignController.RedstoneUpdateTask updateTask;
   private boolean redstonePhysicsSuppressed;
   private final RecursionGuard<ChunkLoadEvent> loadChunkRecursionGuard;

   public SignController(TrainCarts plugin) {
      this.byWorldLastGet = this.NONE;
      this.redstonePhysicsSuppressed = false;
      this.plugin = plugin;
      this.updateTask = new SignController.RedstoneUpdateTask(plugin);
      this.blockPhysicsFireForSigns = doesBlockPhysicsFireForSigns();
      this.loadChunkRecursionGuard = RecursionGuard.handleOnce((event) -> {
         if (TCConfig.logSyncChunkLoads) {
            plugin.getLogger().log(Level.WARNING, "Sync chunk load detected loading signs in chunk " + event.getWorld().getName() + " [" + event.getChunk().getX() + ", " + event.getChunk().getZ() + "]", new RuntimeException("Stack"));
         }
      });
   }

   public TrainCarts getPlugin() {
      return this.plugin;
   }

   public void updateEnabled() {
      if (TCConfig.enableVanillaActionSigns) {
         this.plugin.register(this);
         this.updateTask.start(1L, 1L);
         if (!this.enabled) {
            this.enabled = true;
            Iterator var1 = Bukkit.getWorlds().iterator();

            while(var1.hasNext()) {
               World world = (World)var1.next();
               this.forWorld(world);
            }
         }
      } else {
         this.disable();
      }

   }

   public void enable() {
      this.updateEnabled();
   }

   public void disable() {
      if (this.enabled) {
         CommonUtil.unregisterListener(this);
         this.byWorld.values().forEach(SignControllerWorld::clear);
         this.byWorld.clear();
         this.pendingRedstoneUpdates.clear();
         this.byWorldLastGet = this.NONE;
         this.updateTask.stop();
         this.enabled = false;
      }

   }

   public void suppressRedstonePhysicsDuring(Runnable runnable) {
      if (this.redstonePhysicsSuppressed) {
         runnable.run();
      } else {
         try {
            this.redstonePhysicsSuppressed = true;
            runnable.run();
         } finally {
            this.redstonePhysicsSuppressed = false;
         }
      }

   }

   public SignControllerWorld forWorld(World world) {
      SignControllerWorld c = this.byWorldLastGet;
      if (c.getWorld() == world) {
         return c;
      } else if ((c = (SignControllerWorld)this.byWorld.get(world)) != null) {
         return this.byWorldLastGet = c;
      } else if (!this.enabled) {
         return new SignControllerWorld.SignControllerWorldDisabled(this, world);
      } else {
         Object c;
         if (TrainCarts.isWorldDisabled(world)) {
            c = new SignControllerWorld.SignControllerWorldDisabled(this, world);
         } else {
            c = new SignControllerWorld(this, world);
         }

         this.byWorld.put(world, c);
         this.byWorldLastGet = (SignControllerWorld)c;
         ((SignControllerWorld)c).initialize();
         return (SignControllerWorld)c;
      }
   }

   public SignControllerWorld forWorldSkipInitialization(World world) {
      SignControllerWorld c = this.byWorldLastGet;
      if (c.getWorld() == world) {
         return c;
      } else if ((c = (SignControllerWorld)this.byWorld.get(world)) != null) {
         return this.byWorldLastGet = c;
      } else if (!this.enabled) {
         return new SignControllerWorld.SignControllerWorldDisabled(this, world);
      } else {
         Object c;
         if (TrainCarts.isWorldDisabled(world)) {
            c = new SignControllerWorld.SignControllerWorldDisabled(this, world);
         } else {
            c = new SignControllerWorld(this, world);
         }

         this.byWorld.put(world, c);
         this.byWorldLastGet = (SignControllerWorld)c;
         return (SignControllerWorld)c;
      }
   }

   private SignControllerWorld tryGetForWorld(World world) {
      SignControllerWorld c = this.byWorldLastGet;
      if (c.getWorld() != world) {
         c = (SignControllerWorld)this.byWorld.get(world);
         if (c != null) {
            this.byWorldLastGet = c;
         }
      }

      return c;
   }

   public void forEachNearbyVerify(Block block, boolean mustHaveSignActions, Consumer<SignController.Entry> handler) {
      this.forWorld(block.getWorld()).forEachNearbyVerify(block, mustHaveSignActions, handler);
   }

   public void ignoreOutputLever(Block lever) {
      Block att = BlockUtil.getAttachedBlock(lever);
      this.forEachNearbyVerify(att, true, (entry) -> {
         if (entry.sign.isAttachedTo(att)) {
            entry.ignoreRedstone();
         }

      });
   }

   public SignControllerWorld.RefreshResult refreshInChunk(Chunk chunk) {
      return this.forWorld(chunk.getWorld()).refreshInChunk(chunk);
   }

   public void notifySignChanged(SignChangeTracker tracker) {
      SignControllerWorld worldController = this.forWorld(tracker.getWorld());
      SignController.Entry entry = worldController.findForSign(tracker.getBlock(), false);
      if (entry != null) {
         if (entry.sign != tracker) {
            entry.sign.update();
         }

         if (!entry.verifyAfterUpdate(true, true)) {
            entry.removeInvalidEntry();
         }
      }

   }

   private void cleanupUnloaded() {
      Iterator iter = this.byWorld.values().iterator();

      while(iter.hasNext()) {
         SignControllerWorld controller = (SignControllerWorld)iter.next();
         if (!controller.isValid()) {
            iter.remove();
            this.byWorldLastGet = this.NONE;
            controller.clear();
         }
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   private void onWorldInit(WorldInitEvent event) {
      this.forWorld(event.getWorld());
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   private void onWorldUnload(WorldUnloadEvent event) {
      World world = event.getWorld();
      CommonUtil.nextTick(() -> {
         SignControllerWorld controller = (SignControllerWorld)this.byWorld.remove(world);
         if (controller != null) {
            controller.clear();
            this.byWorldLastGet = this.NONE;
         }

      });
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   private void onChunkLoad(ChunkLoadEvent event) {
      RecursionGuard.Token t = this.loadChunkRecursionGuard.open(event);

      try {
         this.forWorld(event.getWorld()).loadChunk(event.getChunk());
      } catch (Throwable var6) {
         if (t != null) {
            try {
               t.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (t != null) {
         t.close();
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   private void onChunkUnload(ChunkUnloadEvent event) {
      SignControllerWorld controller = this.tryGetForWorld(event.getWorld());
      if (controller != null) {
         controller.unloadChunk(event.getChunk());
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   private void onSignEditText(SignEditTextEvent event) {
      if (!TrainCarts.isWorldDisabled((BlockEvent)event)) {
         this.handleSignChange(SignBuildEvent.BKCLSignEditBuildEvent.create(event, true), event.getBlock(), event.getSide(), event.getEditReason() != EditReason.CTRL_PICK_PLACE);
      }
   }

   protected void handleSignChange(SignBuildEvent event, Block signBlock, com.bergerkiller.bukkit.common.block.SignSide signSide, boolean isSignEdit) {
      SignControllerWorld controller = this.forWorld(event.getBlock().getWorld());
      SignController.Entry newSignEntry = controller.addSign(event.getBlock(), true, signSide.isFront());
      SignAction.handleBuild(event);
      if (newSignEntry != null && !event.isCancelled()) {
         newSignEntry.updateRedstoneLater();
      }

      if (event.isCancelled() && !CommonCapabilities.HAS_SIGN_BACK_TEXT) {
         Material signBlockType = signBlock.getType();
         if (!Util.canInstantlyBuild(event.getPlayer()) && MaterialUtil.ISSIGN.get(signBlockType)) {
            Material signItemType;
            if (signBlockType != MaterialUtil.getMaterial("LEGACY_SIGN_POST") && signBlockType != MaterialUtil.getMaterial("LEGACY_WALL_SIGN")) {
               if (signBlockType.name().contains("_WALL_")) {
                  signItemType = MaterialUtil.getMaterial(signBlockType.name().replace("_WALL_", "_"));
                  if (signItemType == null) {
                     signItemType = MaterialUtil.getFirst(new String[]{"OAK_SIGN", "LEGACY_SIGN"});
                  }
               } else {
                  signItemType = signBlockType;
               }
            } else {
               signItemType = MaterialUtil.getFirst(new String[]{"OAK_SIGN", "LEGACY_SIGN"});
            }

            ItemStack item = HumanHand.getItemInMainHand(event.getPlayer());
            if (LogicUtil.nullOrEmpty(item)) {
               HumanHand.setItemInMainHand(event.getPlayer(), new ItemStack(signItemType, 1));
            } else if (MaterialUtil.isType(item, new Material[]{signItemType}) && item.getAmount() < ItemUtil.getMaxSize(item)) {
               ItemUtil.addAmount(item, 1);
               HumanHand.setItemInMainHand(event.getPlayer(), item);
            } else {
               Location loc = signBlock.getLocation().add(0.5D, 0.5D, 0.5D);
               loc.getWorld().dropItemNaturally(loc, new ItemStack(signItemType, 1));
            }
         }

         signBlock.setType(Material.AIR);
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onBlockBreak(BlockBreakEvent event) {
      Block block = event.getBlock();
      SignControllerWorld controller = this.forWorld(block.getWorld());
      SignController.Entry e = controller.findForSign(block, false);
      if (e != null) {
         if (!e.verify()) {
            return;
         }

         e.updateRedstoneLater();
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   private void onBlockPhysics(BlockPhysicsEvent event) {
      if (!this.redstonePhysicsSuppressed) {
         Block block = event.getBlock();
         SignControllerWorld controller = this.forWorld(block.getWorld());
         if (MaterialUtil.ISSIGN.get(event.getChangedType())) {
            controller.detectNewSigns(block);
         }

         if (this.blockPhysicsFireForSigns) {
            SignController.Entry e = controller.findForSign(block, true);
            if (e != null) {
               e.updateRedstoneLater();
            }
         } else {
            SignController.Entry[] var8 = controller.findNearby(block, true);
            int var5 = var8.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               SignController.Entry e = var8[var6];
               e.updateRedstoneLater();
            }
         }

      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   private void onBlockRedstoneChange(BlockRedstoneEvent event) {
      if (!this.redstonePhysicsSuppressed && !TrainCarts.isWorldDisabled((BlockEvent)event)) {
         Block block = event.getBlock();
         SignController.Entry[] var3 = this.forWorld(block.getWorld()).findNearby(block, true);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            SignController.Entry e = var3[var5];
            e.updateRedstoneLater();
         }

         BlockData event_block_data = WorldUtil.getBlockData(event.getBlock());
         if (event_block_data.isType(Material.LEVER)) {
            Block leverBlock = event.getBlock();
            boolean isPowered = event.getNewCurrent() > 0;
            this.forEachNearbyVerify(leverBlock, true, (entry) -> {
               Block signBlock = entry.getBlock();
               if (leverBlock.getX() == signBlock.getX() && leverBlock.getZ() == signBlock.getZ() && Math.abs(leverBlock.getY() - signBlock.getY()) == 1) {
                  entry.updateRedstonePowerVerify(isPowered);
               }

            });
            this.ignoreOutputLever(event.getBlock());
         }

      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   private void onMultiBlockChange(MultiBlockChangeEvent event) {
      SignControllerWorld worldController = this.forWorld(event.getWorld());
      Iterator var3 = event.getChunkCoordinates().iterator();

      while(var3.hasNext()) {
         IntVector2 chunkCoord = (IntVector2)var3.next();
         Chunk chunk = WorldUtil.getChunk(event.getWorld(), chunkCoord.x, chunkCoord.z);
         if (chunk != null) {
            worldController.refreshInChunk(chunk);
         }
      }

   }

   SignController.Entry createEntry(Sign sign, SignControllerWorld world, SignControllerChunk chunk, long blockKey) {
      return new SignController.Entry(sign, world, chunk, blockKey, this);
   }

   void activateEntry(SignController.Entry entry) {
      this.activateEntry(entry, false, true);
   }

   void activateEntry(SignController.Entry entry, boolean refreshRailSigns, boolean handleLoadChange) {
      RailLookup.TrackedSign frontTrackedSign = null;
      RailLookup.TrackedSign backTrackedSign = null;
      if (refreshRailSigns) {
         if (entry.front.hasLoadedChangeHandler()) {
            frontTrackedSign = RailLookup.TrackedSign.forRealSign((Sign)entry.sign.getSign(), true, (RailPiece)null);
         }

         if (entry.back.hasLoadedChangeHandler()) {
            backTrackedSign = RailLookup.TrackedSign.forRealSign((Sign)entry.sign.getSign(), false, (RailPiece)null);
         }

         if (frontTrackedSign != null) {
            frontTrackedSign.getRail().forceCacheVerification();
         } else if (backTrackedSign != null) {
            backTrackedSign.getRail().forceCacheVerification();
         }
      }

      boolean wasFrontActivated = entry.front.activated;
      boolean wasBackActivated = entry.back.activated;
      if (!wasFrontActivated || !wasBackActivated) {
         Block b = entry.sign.getBlock();

         try {
            entry.activate();
            if (handleLoadChange) {
               if (refreshRailSigns) {
                  if (frontTrackedSign != null && !wasFrontActivated) {
                     SignAction.handleLoadChange(frontTrackedSign, true);
                  }

                  if (backTrackedSign != null && !wasBackActivated) {
                     SignAction.handleLoadChange(backTrackedSign, true);
                  }
               } else {
                  if (!wasFrontActivated) {
                     entry.front.handleLoadChange(true);
                  }

                  if (!wasBackActivated) {
                     entry.back.handleLoadChange(true);
                  }
               }
            }
         } catch (Throwable var10) {
            this.plugin.getLogger().log(Level.SEVERE, "Error while initializing sign in world " + b.getWorld().getName() + " at " + b.getX() + " / " + b.getY() + " / " + b.getZ(), var10);
         }

      }
   }

   private void updateRedstoneNow(SignController.Entry entry) {
      ++this.pendingRedstoneUpdatesThisTick;
      if (this.pendingRedstoneUpdatesThisTick >= 100000) {
         Block b = entry.sign.getBlock();
         this.plugin.getLogger().warning("Too many Redstone updates! Skipped sign at world=" + b.getWorld().getName() + " x=" + b.getX() + " y=" + b.getY() + " z=" + b.getZ());
      } else if (!entry.ignoreRedstoneUpdateTracker.isSet()) {
         if (entry.verify()) {
            entry.updateRedstonePower();
         }
      }
   }

   public void redetectSignActions() {
      Iterator var1 = (new ArrayList(this.byWorld.values())).iterator();

      while(var1.hasNext()) {
         SignControllerWorld world = (SignControllerWorld)var1.next();
         world.redetectSignActions();
      }

   }

   private static boolean doesBlockPhysicsFireForSigns() {
      if (Common.evaluateMCVersion("<=", "1.18.2")) {
         return true;
      } else {
         if (Common.IS_PAPERSPIGOT_SERVER) {
            if (Common.evaluateMCVersion(">=", "1.19.3")) {
               return true;
            }

            if (Common.evaluateMCVersion("==", "1.19.2")) {
               if (checkBuildNumberLessThan("Paper", 165)) {
                  return false;
               }

               if (checkBuildNumberLessThan("Purpur", 1788)) {
                  return false;
               }

               return true;
            }
         }

         return false;
      }
   }

   private static boolean checkBuildNumberLessThan(String serverName, int buildNumberThreshold) {
      String version = Bukkit.getVersion();
      Matcher m;
      if (version != null && (m = Pattern.compile("^git-" + serverName + "-(\\d+)\\s.*$").matcher(version)).matches()) {
         try {
            int build = Integer.parseInt(m.group(1));
            if (build < buildNumberThreshold) {
               return true;
            }
         } catch (NumberFormatException var5) {
         }
      }

      return false;
   }

   public static Runnable spawnDebugHighlight(AttachmentViewer viewer, SignChangeTracker sign, RailLookup.TrackedSign.DebugDisplayOptions options) {
      if (CAN_DEBUG_DISPLAY_SIGNS && viewer.supportsDisplayEntities()) {
         SignDebugHighlight highlight = new SignDebugHighlight(viewer);
         highlight.spawn(sign, options);
         return highlight;
      } else {
         return () -> {
         };
      }
   }

   public static final class Entry {
      public final SignChangeTracker sign;
      private SignChangeTracker signLastState;
      public final SignControllerWorld world;
      public final SignControllerChunk chunk;
      public final SignController.Entry.SignSide front;
      public final SignController.Entry.SignSide back;
      private final Tracker<SignController.Entry> redstoneUpdateTracker;
      private final Tracker<SignController.Entry> ignoreRedstoneUpdateTracker;
      final long blockKey;
      SignBlocksAround blocks;
      private boolean registeredInNeighbouringBlocks;
      final SignController.EntryList singletonList;

      private Entry(Sign sign, SignControllerWorld world, SignControllerChunk chunk, long blockKey, SignController controller) {
         this.sign = SignChangeTracker.track(sign);
         this.world = world;
         this.chunk = chunk;
         this.front = new SignController.Entry.SignSide(true, SignChangeTracker::getFrontLine);
         this.back = new SignController.Entry.SignSide(false, SignChangeTracker::getBackLine);
         this.redstoneUpdateTracker = controller.pendingRedstoneUpdates.track(this);
         this.ignoreRedstoneUpdateTracker = controller.ignoreRedstoneUpdates.track(this);
         this.blockKey = blockKey;
         this.blocks = SignBlocksAround.of(this.sign.getAttachedFace());
         this.registeredInNeighbouringBlocks = false;
         this.singletonList = SignController.EntryList.createSingleton(this);
         this.updateLastSignState();
      }

      void updateLastSignState() {
         this.signLastState = this.sign.clone();
      }

      void updateSignFacing() {
         if (this.sign.getAttachedFace() != this.blocks.getAttachedFace()) {
            if (this.registeredInNeighbouringBlocks) {
               SignBlocksAround var10000 = this.blocks;
               SignControllerWorld var10002 = this.world;
               Objects.requireNonNull(var10002);
               var10000.forAllBlocks(this, var10002::removeChunkByBlockEntry);
               this.blocks = SignBlocksAround.of(this.sign.getAttachedFace());
               var10000 = this.blocks;
               var10002 = this.world;
               Objects.requireNonNull(var10002);
               var10000.forAllBlocks(this, var10002::addChunkByBlockEntry);
            } else {
               this.blocks = SignBlocksAround.of(this.sign.getAttachedFace());
            }
         }

      }

      public Block getBlock() {
         return this.sign.getBlock();
      }

      public SignActionHeader getFrontHeader() {
         return this.front.getHeader();
      }

      public SignActionHeader getBackHeader() {
         return this.back.getHeader();
      }

      public RailLookup.TrackedSign createFrontTrackedSign(RailPiece rail) {
         return this.front.createTrackedSign(rail);
      }

      public RailLookup.TrackedSign createBackTrackedSign(RailPiece rail) {
         return this.back.createTrackedSign(rail);
      }

      void removeInvalidEntry() {
         this.chunk.removeEntry(this);
         this.unregisterInNeighbouringBlocks();
         this.onRemoved();
      }

      void onRemoved() {
         this.redstoneUpdateTracker.untrack();
         this.ignoreRedstoneUpdateTracker.untrack();
      }

      void deactivate() {
         try {
            this.front.deactivate();
            this.back.deactivate();
         } catch (Throwable var3) {
            Block b = this.sign.getBlock();
            this.world.getPlugin().getLogger().log(Level.SEVERE, "Error while unloading sign in world " + b.getWorld().getName() + " at " + b.getX() + " / " + b.getY() + " / " + b.getZ(), var3);
         }

      }

      boolean verify() {
         boolean changed = this.sign.update();
         return this.verifyAfterUpdate(changed, changed);
      }

      boolean verifyAfterUpdate(boolean frontChanged, boolean backChanged) {
         if (this.sign.isRemoved() && this.signLastState != null && !this.signLastState.isRemoved() && WorldUtil.isLoaded(this.sign.getBlock())) {
            this.handleDestroy(frontChanged, backChanged);
            return false;
         } else {
            if (frontChanged || backChanged || this.signLastState == null && !this.sign.isRemoved()) {
               this.updateLastSignState();
            }

            if (this.sign.isRemoved()) {
               return false;
            } else {
               if (frontChanged && backChanged) {
                  this.world.getPlugin().getOfflineSigns().verifySign(this.sign.getSign());
               } else if (frontChanged) {
                  this.world.getPlugin().getOfflineSigns().verifySign(this.sign.getSign(), true, (Class)null);
               } else if (backChanged) {
                  this.world.getPlugin().getOfflineSigns().verifySign(this.sign.getSign(), false, (Class)null);
               }

               boolean hadSignActions = this.hasSignActionEvents();
               if (frontChanged) {
                  this.front.updateSignAction();
               }

               if (backChanged) {
                  this.back.updateSignAction();
               }

               boolean nowHasSignActions = this.hasSignActionEvents();
               if (hadSignActions != nowHasSignActions) {
                  this.chunk.updateEntryHasSignActions(this, nowHasSignActions);
               }

               this.updateSignFacing();
               return true;
            }
         }
      }

      void redetectSignActions() {
         if (!this.sign.isRemoved()) {
            boolean hadSignActions = this.hasSignActionEvents();
            this.front.detectSignAction();
            this.back.detectSignAction();
            boolean nowHasSignActions = this.hasSignActionEvents();
            if (hadSignActions != nowHasSignActions) {
               this.chunk.updateEntryHasSignActions(this, nowHasSignActions);
            }

         }
      }

      boolean verifyBeforeSignChange(boolean frontText) {
         this.sign.update();
         if (this.sign.isRemoved()) {
            this.verifyAfterUpdate(frontText, !frontText);
            return false;
         } else {
            this.handleDestroy(frontText, !frontText);
            this.updateSignFacing();
            this.updateLastSignState();
            return true;
         }
      }

      private void handleDestroy(boolean destroyFront, boolean destroyBack) {
         if (this.signLastState != null && !this.signLastState.isRemoved()) {
            RailLookup.TrackedSign sign;
            if (destroyFront && !this.front.cachedHeader.isEmpty()) {
               sign = RailLookup.TrackedSign.forRealSign(this.signLastState, true, RailPiece.NONE);
               SignAction.handleDestroy(new SignActionEvent(sign));
            }

            if (destroyBack && !this.back.cachedHeader.isEmpty()) {
               sign = RailLookup.TrackedSign.forRealSign(this.signLastState, false, RailPiece.NONE);
               SignAction.handleDestroy(new SignActionEvent(sign));
            }

            if (destroyFront && destroyBack) {
               this.world.getPlugin().getOfflineSigns().removeAll(this.signLastState.getBlock());
               this.signLastState = null;
            } else if (destroyFront) {
               this.world.getPlugin().getOfflineSigns().removeAll(this.signLastState.getBlock(), true);
            } else if (destroyBack) {
               this.world.getPlugin().getOfflineSigns().removeAll(this.signLastState.getBlock(), false);
            }

         }
      }

      public boolean hasSignActionEvents() {
         return !TCConfig.onlyRegisteredSignsHandleRedstone || this.front.hasSignAction() || this.back.hasSignAction();
      }

      void registerInNeighbouringBlocks() {
         if (!this.registeredInNeighbouringBlocks) {
            this.registeredInNeighbouringBlocks = true;
            SignBlocksAround var10000 = this.blocks;
            SignControllerWorld var10002 = this.world;
            Objects.requireNonNull(var10002);
            var10000.forAllBlocks(this, var10002::addChunkByBlockEntry);
         }

      }

      void unregisterInNeighbouringBlocks() {
         this.unregisterInNeighbouringBlocks(false);
      }

      void unregisterInNeighbouringBlocks(boolean purgeAllInSameChunk) {
         if (this.registeredInNeighbouringBlocks) {
            this.registeredInNeighbouringBlocks = false;
            if (purgeAllInSameChunk) {
               this.blocks.forAllBlocks(this, (e, key) -> {
                  this.world.removeChunkByBlockEntry(e, key, true);
               });
            } else {
               SignBlocksAround var10000 = this.blocks;
               SignControllerWorld var10002 = this.world;
               Objects.requireNonNull(var10002);
               var10000.forAllBlocks(this, var10002::removeChunkByBlockEntry);
            }
         }

      }

      public void ignoreRedstone() {
         this.ignoreRedstoneUpdateTracker.set(true);
      }

      public void updateRedstoneLater() {
         this.redstoneUpdateTracker.set(true);
      }

      private static boolean skipReadingPower(SignActionHeader header) {
         return header.isEmpty() || header.isAlwaysOn() || header.isAlwaysOff();
      }

      private boolean checkIsSignPowered() {
         PowerState.Options opt = !this.front.hasSignAction() && !this.back.hasSignAction() ? PowerState.Options.SIGN : PowerState.Options.SIGN_CONNECT_WIRE;
         return PowerState.isSignPowered(this.sign.getBlock(), opt);
      }

      void activate() {
         boolean powered;
         if (skipReadingPower(this.front.getHeader()) && skipReadingPower(this.back.getHeader())) {
            powered = false;
         } else {
            powered = this.checkIsSignPowered();
         }

         this.front.activated = true;
         this.front.setInitialPower(powered);
         this.back.activated = true;
         this.back.setInitialPower(powered);
      }

      public void updateRedstonePower() {
         SignActionHeader frontHeader = this.getFrontHeader();
         SignActionHeader backHeader = this.getBackHeader();
         boolean powered = (!skipReadingPower(frontHeader) || !skipReadingPower(backHeader)) && this.checkIsSignPowered();
         if (!frontHeader.isEmpty()) {
            if (!frontHeader.isAlwaysOn() && !frontHeader.isAlwaysOff()) {
               this.front.setRedstonePower(frontHeader, powered);
            } else {
               this.front.setRedstonePowerChanged(frontHeader);
            }
         }

         if (!backHeader.isEmpty()) {
            if (!backHeader.isAlwaysOn() && !backHeader.isAlwaysOff()) {
               this.back.setRedstonePower(backHeader, powered);
            } else {
               this.back.setRedstonePowerChanged(backHeader);
            }
         }

      }

      public void updateRedstonePowerVerify(boolean isPowered) {
         SignActionHeader frontHeader = this.getFrontHeader();
         SignActionHeader backHeader = this.getBackHeader();
         boolean powerStateCorrect = (!skipReadingPower(frontHeader) || !skipReadingPower(backHeader)) && isPowered == this.checkIsSignPowered();
         if (!frontHeader.isEmpty()) {
            if (!frontHeader.isAlwaysOn() && !frontHeader.isAlwaysOff()) {
               if (powerStateCorrect) {
                  this.front.setRedstonePower(frontHeader, isPowered);
               }
            } else {
               this.front.setRedstonePowerChanged(frontHeader);
            }
         }

         if (!backHeader.isEmpty()) {
            if (!backHeader.isAlwaysOn() && !backHeader.isAlwaysOff()) {
               if (powerStateCorrect) {
                  this.back.setRedstonePower(backHeader, isPowered);
               }
            } else {
               this.back.setRedstonePowerChanged(backHeader);
            }
         }

      }

      // $FF: synthetic method
      Entry(Sign x0, SignControllerWorld x1, SignControllerChunk x2, long x3, SignController x4, Object x5) {
         this(x0, x1, x2, x3, x4);
      }

      public class SignSide {
         private final boolean front;
         private final SignController.Entry.GetLineFunction lineFunc;
         public String headerLine;
         private SignActionHeader cachedHeader;
         private boolean hasSignAction;
         private boolean hasLoadedChangeHandler;
         public boolean powered;
         public boolean activated;

         public SignSide(boolean front, SignController.Entry.GetLineFunction lineFunc) {
            this.front = front;
            this.lineFunc = lineFunc;
            this.headerLine = lineFunc.getLine(Entry.this.sign, 0);
            this.cachedHeader = SignActionHeader.parse(Util.cleanSignLine(this.headerLine));
            this.detectSignAction();
            this.powered = false;
            this.activated = false;
         }

         public SignActionHeader getHeader() {
            return this.syncAndGetHeader(false);
         }

         private SignActionHeader syncAndGetHeader(boolean alwaysCheckHasSignAction) {
            String headerLine = this.lineFunc.getLine(Entry.this.sign, 0);
            if (headerLine.equals(this.headerLine)) {
               if (alwaysCheckHasSignAction) {
                  this.detectSignAction(this.cachedHeader);
               }

               return this.cachedHeader;
            } else {
               this.headerLine = headerLine;
               SignActionHeader header = this.cachedHeader = SignActionHeader.parse(Util.cleanSignLine(headerLine));
               this.detectSignAction(header);
               return header;
            }
         }

         public void updateSignAction() {
            this.syncAndGetHeader(true);
         }

         public boolean hasSignAction() {
            return this.hasSignAction;
         }

         public boolean hasLoadedChangeHandler() {
            return this.hasLoadedChangeHandler;
         }

         public void detectSignAction() {
            this.detectSignAction(this.cachedHeader);
         }

         private void detectSignAction(SignActionHeader header) {
            Optional<SignActionLookupMap.Entry> actionEntry = SignAction.getLookup().lookup(this.createSignActionEvent(header, RailPiece.NONE));
            this.hasSignAction = actionEntry.isPresent();
            this.hasLoadedChangeHandler = (Boolean)actionEntry.map(SignActionLookupMap.Entry::hasLoadedChangedHandler).orElse(false);
         }

         public void setInitialPower(boolean powered) {
            this.powered = powered;
         }

         public void deactivate() {
            if (this.activated) {
               this.activated = false;
               this.handleLoadChange(false);
            }

         }

         public void handleLoadChange(boolean loaded) {
            if (this.hasLoadedChangeHandler) {
               SignAction.handleLoadChange(Entry.this.sign.getSign(), this.front, loaded);
            }

         }

         public void setRedstonePower(SignActionHeader header, boolean newPowerState) {
            SignActionEvent info = this.createSignActionEvent(header, (RailPiece)null);
            SignActionType type = info.getHeader().getRedstoneAction(newPowerState);
            if (this.powered != newPowerState) {
               this.powered = newPowerState;
               if (type != SignActionType.NONE) {
                  SignAction.executeAll(info, type);
               }
            }

            SignAction.executeAll(info, SignActionType.REDSTONE_CHANGE);
         }

         public void setRedstonePowerChanged(SignActionHeader header) {
            SignActionEvent info = this.createSignActionEvent(header, (RailPiece)null);
            SignAction.executeAll(info, SignActionType.REDSTONE_CHANGE);
         }

         public RailLookup.TrackedSign createTrackedSign(RailPiece rail) {
            return this.createTrackedSign(this.getHeader(), rail);
         }

         private RailLookup.TrackedSign createTrackedSign(SignActionHeader header, RailPiece rail) {
            RailLookup.TrackedSign trackedSign = RailLookup.TrackedSign.forRealSign(Entry.this.sign, this.front, rail);
            trackedSign.setCachedHeader(header);
            return trackedSign;
         }

         private SignActionEvent createSignActionEvent(SignActionHeader header, RailPiece rail) {
            return new SignActionEvent(this.createTrackedSign(header, rail));
         }
      }

      @FunctionalInterface
      public interface GetLineFunction {
         String getLine(SignChangeTracker var1, int var2);
      }
   }

   private class RedstoneUpdateTask extends Task {
      public RedstoneUpdateTask(JavaPlugin plugin) {
         super(plugin);
      }

      public void run() {
         SignController.this.pendingRedstoneUpdatesThisTick = 0;
         SignController.this.pendingRedstoneUpdates.forEachAndClear((x$0) -> {
            SignController.this.updateRedstoneNow(x$0);
         });
         SignController.this.ignoreRedstoneUpdates.clear();
         SignController.this.cleanupUnloaded();
      }
   }

   public static final class EntryList {
      public static final SignController.EntryList NONE = new SignController.EntryList(new SignController.Entry[0], true);
      private final SignController.Entry[] values;
      private boolean sorted;

      private EntryList(SignController.Entry[] values, boolean sorted) {
         this.values = values;
         this.sorted = sorted;
      }

      public int count() {
         return this.values.length;
      }

      public SignController.Entry[] unsortedValues() {
         return this.values;
      }

      public SignController.Entry[] values() {
         if (!this.sorted) {
            this.sorted = true;
            Arrays.sort(this.values, Comparator.comparingLong((e) -> {
               return e.blockKey;
            }));
         }

         return this.values;
      }

      public SignController.EntryList add(SignController.Entry entry) {
         SignController.Entry[] values = this.values;
         int len = values.length;
         if (len == 0) {
            return entry.singletonList;
         } else {
            SignController.Entry[] tmp = (SignController.Entry[])Arrays.copyOf(values, len + 1);
            tmp[len] = entry;
            return new SignController.EntryList(tmp, false);
         }
      }

      public boolean contains(SignController.Entry entry) {
         SignController.Entry[] var2 = this.values;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            SignController.Entry value = var2[var4];
            if (value == entry) {
               return true;
            }
         }

         return false;
      }

      public SignController.EntryList filter(Predicate<SignController.Entry> filter) {
         SignController.Entry[] values = this.values;
         int len = values.length;
         int numPassingFilter = 0;

         for(int i = 0; i < len; ++i) {
            if (filter.test(values[i])) {
               ++numPassingFilter;
            }
         }

         if (numPassingFilter == len) {
            return this;
         } else if (numPassingFilter == 0) {
            return NONE;
         } else {
            SignController.Entry[] filteredValues = new SignController.Entry[numPassingFilter];
            int currentIndex = 0;

            for(int i = 0; i < len; ++i) {
               SignController.Entry e = values[i];
               if (filter.test(e)) {
                  filteredValues[currentIndex++] = e;
               }
            }

            return new SignController.EntryList(filteredValues, this.sorted);
         }
      }

      public static SignController.EntryList of(List<SignController.Entry> entries) {
         int count = entries.size();
         if (count == 0) {
            return NONE;
         } else {
            return count == 1 ? ((SignController.Entry)entries.get(0)).singletonList : new SignController.EntryList((SignController.Entry[])entries.toArray(new SignController.Entry[0]), false);
         }
      }

      public static SignController.EntryList createSingleton(SignController.Entry entry) {
         return new SignController.EntryList(new SignController.Entry[]{entry}, true);
      }
   }
}
