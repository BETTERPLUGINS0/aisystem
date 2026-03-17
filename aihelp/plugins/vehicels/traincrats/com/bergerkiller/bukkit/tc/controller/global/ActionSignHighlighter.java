package com.bergerkiller.bukkit.tc.controller.global;

import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.block.BlockRayTrace;
import com.bergerkiller.bukkit.common.block.BlockRayTrace.HitResult;
import com.bergerkiller.bukkit.common.component.LibraryComponent;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.common.wrappers.Brightness;
import com.bergerkiller.bukkit.common.wrappers.ChatText;
import com.bergerkiller.bukkit.common.wrappers.HumanHand;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.VirtualDisplayBlockEntity;
import com.bergerkiller.bukkit.tc.attachments.VirtualDisplayTextEntity;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.utils.ListCallbackCollector;
import com.bergerkiller.generated.net.minecraft.world.entity.DisplayHandle;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ActionSignHighlighter implements LibraryComponent {
   private static final Material LEVER_TYPE = MaterialUtil.getFirst(new String[]{"LEVER", "LEGACY_LEVER"});
   private static final ChatColor[] HIGHLIGHT_COLORS;
   private final TrainCarts plugin;
   private final Task updateTask;
   private final Listener listener;
   private final Map<Player, ActionSignHighlighter.PlayerViewedBlockTracker> trackers = new IdentityHashMap();
   private boolean enabled = false;

   public ActionSignHighlighter(TrainCarts plugin) {
      this.plugin = plugin;
      this.updateTask = new Task(plugin) {
         int stateCtr = 0;

         public void run() {
            int state = ++this.stateCtr;
            Iterator var2 = Bukkit.getOnlinePlayers().iterator();

            while(var2.hasNext()) {
               Player player = (Player)var2.next();
               ActionSignHighlighter.PlayerViewedBlockTracker tracker = (ActionSignHighlighter.PlayerViewedBlockTracker)ActionSignHighlighter.this.trackers.computeIfAbsent(player, (x$0) -> {
                  return ActionSignHighlighter.this.new PlayerViewedBlockTracker(x$0);
               });
               tracker.state = state;
               tracker.update();
            }

            ActionSignHighlighter.this.trackers.values().removeIf((trackerx) -> {
               if (trackerx.state == state) {
                  return false;
               } else {
                  trackerx.resetAndClearViewedBlock();
                  return true;
               }
            });
         }
      };
      this.listener = new Listener() {
         @EventHandler(
            priority = EventPriority.MONITOR
         )
         public void onPlayerInteract(PlayerInteractEvent event) {
            ActionSignHighlighter.this.invalidateBlock(event.getPlayer(), event.getClickedBlock());
         }
      };
   }

   public void updateEnabled() {
      if (TCConfig.debugOutputLevers) {
         if (!this.enabled) {
            this.enabled = true;
            this.updateTask.start(1L, 1L);
            this.plugin.register(this.listener);
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
         this.enabled = false;
         this.trackers.values().forEach(ActionSignHighlighter.PlayerViewedBlockTracker::resetAndClearViewedBlock);
         this.trackers.clear();
         this.updateTask.stop();
         CommonUtil.unregisterListener(this.listener);
      }

   }

   private void invalidateBlock(Player player, Block block) {
      if (block != null) {
         ActionSignHighlighter.PlayerViewedBlockTracker tracker = (ActionSignHighlighter.PlayerViewedBlockTracker)this.trackers.get(player);
         if (tracker != null && tracker.lastHighlightedBlock != null) {
            if (block.equals(tracker.lastHighlightedBlock)) {
               tracker.reset();
            } else if (block.equals(tracker.lastHighlightedBlock.getRelative(tracker.lastHighlightedFace))) {
               tracker.reset();
            }
         }
      }
   }

   private static boolean isRayTraceDifferent(BlockRayTrace a, BlockRayTrace b) {
      if (a.getWorld() != b.getWorld()) {
         return true;
      } else if (a.getStartPosition().distanceSquared(b.getStartPosition()) > 1.0E-4D) {
         return true;
      } else {
         return a.getEndPosition().distanceSquared(b.getEndPosition()) > 1.0E-4D;
      }
   }

   static {
      HIGHLIGHT_COLORS = new ChatColor[]{ChatColor.RED, ChatColor.GREEN, ChatColor.AQUA, ChatColor.YELLOW, ChatColor.BLUE, ChatColor.LIGHT_PURPLE, ChatColor.WHITE};
   }

   private final class PlayerViewedBlockTracker {
      private final AttachmentViewer viewer;
      private int state = -1;
      private ActionSignHighlighter.ViewedBlock lastViewedBlock = null;
      private BlockRayTrace lastRayTrace = null;
      private HitResult lastHitResult = null;
      private Block lastHighlightedBlock = null;
      private BlockFace lastHighlightedFace = null;

      public PlayerViewedBlockTracker(Player player) {
         this.viewer = ActionSignHighlighter.this.plugin.getAttachmentViewer(player);
      }

      public void onViewedBlockChanged(ActionSignHighlighter.ViewedBlock previousViewedBlock, ActionSignHighlighter.ViewedBlock newViewedBlock) {
         if (previousViewedBlock != null) {
            previousViewedBlock.hide(this.viewer);
         }

         if (newViewedBlock != null) {
            newViewedBlock.show(this.viewer);
         }

      }

      public void reset() {
         this.lastRayTrace = null;
         this.lastHitResult = null;
         this.lastHighlightedBlock = null;
         this.lastHighlightedFace = null;
      }

      public void resetAndClearViewedBlock() {
         this.reset();
         this.clearViewedBlock();
      }

      public void clearViewedBlock() {
         ActionSignHighlighter.ViewedBlock lastViewedBlockTmp = this.lastViewedBlock;
         if (lastViewedBlockTmp != null) {
            this.lastViewedBlock = null;
            this.onViewedBlockChanged(lastViewedBlockTmp, (ActionSignHighlighter.ViewedBlock)null);
         }

      }

      public void update() {
         if (this.viewer.supportsDisplayEntities()) {
            ItemStack mainHandItem = HumanHand.getItemInMainHand(this.viewer.getPlayer());
            if (mainHandItem != null && mainHandItem.getType() == ActionSignHighlighter.LEVER_TYPE) {
               BlockRayTrace rayTrace = BlockRayTrace.fromEyeOf(this.viewer.getPlayer());
               if (this.lastRayTrace == null || ActionSignHighlighter.isRayTraceDifferent(this.lastRayTrace, rayTrace)) {
                  this.lastRayTrace = rayTrace;
                  HitResult hit = rayTrace.rayTrace();
                  if (hit == null) {
                     this.lastHitResult = null;
                     this.lastHighlightedBlock = null;
                     this.lastHighlightedFace = null;
                     this.clearViewedBlock();
                  } else if (this.lastHitResult == null || !this.lastHitResult.getHitBlock().equals(hit.getHitBlock()) || this.lastHitResult.getHitFace() != hit.getHitFace()) {
                     this.lastHitResult = hit;
                     Block highlightedBlock = hit.getHitBlock();
                     BlockFace highlightedFace = hit.getHitFace();
                     BlockData blockDataAtFace = WorldUtil.getBlockData(highlightedBlock);
                     if (blockDataAtFace.getType() == ActionSignHighlighter.LEVER_TYPE) {
                        highlightedFace = blockDataAtFace.getAttachedFace();
                        highlightedBlock = highlightedBlock.getRelative(highlightedFace);
                        highlightedFace = highlightedFace.getOppositeFace();
                     } else {
                        blockDataAtFace = WorldUtil.getBlockData(highlightedBlock.getRelative(highlightedFace));
                     }

                     if (this.lastHighlightedBlock == null || !this.lastHighlightedBlock.equals(highlightedBlock) || this.lastHighlightedFace != highlightedFace) {
                        this.lastHighlightedBlock = highlightedBlock;
                        this.lastHighlightedFace = highlightedFace;
                        if (blockDataAtFace.getType() == ActionSignHighlighter.LEVER_TYPE) {
                           if (blockDataAtFace.getAttachedFace() != highlightedFace.getOppositeFace()) {
                              this.clearViewedBlock();
                              return;
                           }
                        } else if (blockDataAtFace.getType() != Material.AIR) {
                           this.clearViewedBlock();
                           return;
                        }

                        ListCallbackCollector<ActionSignHighlighter.HighlightedSign> highlightedSignsTmp = new ListCallbackCollector();
                        int colorWheelIdx = 0;
                        Iterator var9 = ActionSignHighlighter.this.plugin.getTrackedSignLookup().getOutputtingTrackedSigns(highlightedBlock).iterator();

                        while(var9.hasNext()) {
                           RailLookup.TrackedSign sign = (RailLookup.TrackedSign)var9.next();
                           SignAction action = sign.getAction();
                           if (action != null) {
                              String outputDescription = action.getDescriptiveOutputName(sign.createEvent(SignActionType.NONE));
                              if (outputDescription != null) {
                                 ChatColor color = ActionSignHighlighter.HIGHLIGHT_COLORS[colorWheelIdx++ % ActionSignHighlighter.HIGHLIGHT_COLORS.length];
                                 highlightedSignsTmp.accept(new ActionSignHighlighter.HighlightedSign(sign, color, outputDescription));
                              }
                           }
                        }

                        List<ActionSignHighlighter.HighlightedSign> highlightedSigns = highlightedSignsTmp.result();
                        if (highlightedSigns.isEmpty()) {
                           this.clearViewedBlock();
                        } else {
                           ActionSignHighlighter.ViewedBlock lastViewedBlockTmp = this.lastViewedBlock;
                           this.lastViewedBlock = ActionSignHighlighter.this.new ViewedBlock(highlightedBlock, highlightedFace, blockDataAtFace, highlightedSigns);
                           this.onViewedBlockChanged(lastViewedBlockTmp, this.lastViewedBlock);
                        }
                     }
                  }
               }
            } else {
               this.resetAndClearViewedBlock();
            }
         }
      }
   }

   private final class ViewedBlock {
      public final Block block;
      public final BlockFace face;
      public final BlockData blockDataAtFace;
      public final List<ActionSignHighlighter.HighlightedSign> highlightedSigns;
      VirtualDisplayBlockEntity highlightLeverPos;
      VirtualDisplayTextEntity signDisplay;

      public ViewedBlock(Block block, BlockFace face, BlockData blockDataAtFace, List<ActionSignHighlighter.HighlightedSign> highlightedSigns) {
         this.block = block;
         this.face = face;
         this.blockDataAtFace = blockDataAtFace;
         this.highlightedSigns = highlightedSigns;
      }

      public void hide(AttachmentViewer viewer) {
         if (this.highlightLeverPos != null) {
            this.highlightLeverPos.destroy(viewer);
         }

         if (this.signDisplay != null) {
            this.signDisplay.destroy(viewer);
         }

         this.highlightedSigns.forEach(ActionSignHighlighter.HighlightedSign::hideDebug);
      }

      public void show(AttachmentViewer viewer) {
         BlockFace labelFace = BlockFace.SELF;
         if (this.canShowLabel(BlockFace.UP)) {
            labelFace = BlockFace.UP;
         } else if (this.canShowLabel(this.face)) {
            labelFace = this.face;
         } else if (this.canShowLabel(FaceUtil.rotate(this.face, 2))) {
            labelFace = FaceUtil.rotate(this.face, 2);
         } else if (this.canShowLabel(FaceUtil.rotate(this.face, -2))) {
            labelFace = FaceUtil.rotate(this.face, -2);
         } else if (this.canShowLabel(BlockFace.DOWN)) {
            labelFace = BlockFace.DOWN;
         }

         Vector labelPosition;
         if (this.blockDataAtFace.getType() == ActionSignHighlighter.LEVER_TYPE) {
            Matrix4x4 mx = new Matrix4x4();
            mx.translate(this.block.getRelative(this.face).getLocation().toVector());
            mx.translate(0.5D, 0.0D, 0.5D);
            this.highlightLeverPos = new VirtualDisplayBlockEntity((AttachmentManager)null);
            this.highlightLeverPos.updatePosition(mx);
            this.highlightLeverPos.setBlockData(this.blockDataAtFace);
         } else {
            labelPosition = new Vector(0.5D, 0.5D, 0.5D);
            Vector d = new Vector(0.5D, -0.5D, -0.01D);
            Matrix4x4 mxx = new Matrix4x4();
            mxx.translate(this.block.getLocation().toVector());
            mxx.translate(0.5D, 0.5D, 0.5D);
            mxx.translate(FaceUtil.faceToVector(this.face).multiply(0.5D));
            mxx.rotate(Quaternion.fromLookDirection(FaceUtil.faceToVector(this.face.getOppositeFace())));
            d = d.clone().multiply(labelPosition);
            mxx.translate(d);
            this.highlightLeverPos = new VirtualDisplayBlockEntity((AttachmentManager)null);
            this.highlightLeverPos.updatePosition(mxx);
            this.highlightLeverPos.setBlockData(BlockData.fromMaterial(Material.LEVER));
            this.highlightLeverPos.setScale(labelPosition);
         }

         this.highlightLeverPos.setGlowColor(ChatColor.RED);
         this.highlightLeverPos.setBrightness(Brightness.FULL_ALL);
         this.highlightLeverPos.spawn(viewer, new Vector());
         this.signDisplay = new VirtualDisplayTextEntity((AttachmentManager)null);
         labelPosition = this.block.getLocation().toVector();
         MathUtil.addToVector(labelPosition, 0.5D, 0.5D, 0.5D);
         labelPosition.add(FaceUtil.faceToVector(this.face));
         labelPosition.add(FaceUtil.faceToVector(labelFace).multiply(0.5D));
         Matrix4x4 m = new Matrix4x4();
         m.translate(labelPosition);
         this.signDisplay.updatePosition(m);
         this.signDisplay.getMetadata().set(DisplayHandle.DATA_BILLBOARD_RENDER_CONSTRAINTS, (byte)3);
         this.signDisplay.setScale(new Vector(0.25D, 0.25D, 0.25D));
         this.signDisplay.setText(ChatText.fromMessage((String)this.highlightedSigns.stream().map((s) -> {
            return s.highlightColor + s.outputDescription;
         }).collect(Collectors.joining("\n"))));
         this.signDisplay.setBackgroundColor(Color.fromARGB(128, 64, 64, 64));
         this.signDisplay.setBrightness(Brightness.FULL_ALL);
         this.signDisplay.spawn(viewer, new Vector());
         this.highlightedSigns.forEach((s) -> {
            s.showDebug(viewer);
         });
      }

      private boolean canShowLabel(BlockFace face) {
         Block block = this.block.getRelative(this.face).getRelative(face);
         return block.getType() == Material.AIR;
      }
   }

   private static final class HighlightedSign {
      public final RailLookup.TrackedSign sign;
      public final ChatColor highlightColor;
      public final String outputDescription;
      public Runnable despawnHighlightCallback = () -> {
      };

      public HighlightedSign(RailLookup.TrackedSign sign, ChatColor highlightColor, String outputDescription) {
         this.sign = sign;
         this.highlightColor = highlightColor;
         this.outputDescription = outputDescription;
      }

      public void showDebug(final AttachmentViewer viewer) {
         this.despawnHighlightCallback = this.sign.showDebugHighlight(viewer, new RailLookup.TrackedSign.DebugDisplayOptions() {
            public ChatColor getTeamColor() {
               return HighlightedSign.this.highlightColor;
            }

            public TrainCarts getTrainCarts() {
               return viewer.getTrainCarts();
            }
         });
      }

      public void hideDebug() {
         this.despawnHighlightCallback.run();
      }
   }
}
