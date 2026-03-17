package com.bergerkiller.bukkit.tc.events;

import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.tc.PowerState;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.utils.FakeSign;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeActionEvent extends SignActionEvent {
   private final Cancellable event;
   private final Player player;
   private final boolean interactive;

   public SignChangeActionEvent(SignChangeEvent event, boolean interactive) {
      this(event, event.getPlayer(), new SignChangeActionEvent.TrackedChangingSign(event), interactive);
   }

   public SignChangeActionEvent(SignChangeEvent event) {
      this(event, event.getPlayer(), new SignChangeActionEvent.TrackedChangingSign(event), true);
   }

   public SignChangeActionEvent(Player player, RailLookup.TrackedSign sign, boolean interactive) {
      this(new SignChangeActionEvent.MockCancellable(), player, sign, interactive);
   }

   public SignChangeActionEvent(Player player, RailLookup.TrackedSign sign) {
      this(new SignChangeActionEvent.MockCancellable(), player, sign, true);
   }

   protected SignChangeActionEvent(SignChangeActionEvent event) {
      this(event.event, event.player, event.getTrackedSign(), event.interactive);
   }

   protected SignChangeActionEvent(Cancellable event, Player player, RailLookup.TrackedSign sign, boolean interactive) {
      super(sign);
      this.event = event;
      this.player = player;
      this.interactive = interactive;
   }

   public Player getPlayer() {
      return this.player;
   }

   public boolean isInteractive() {
      return this.interactive;
   }

   public void setCancelled(boolean cancelled) {
      super.setCancelled(cancelled);
      this.event.setCancelled(cancelled);
   }

   private static class TrackedChangingSign extends RailLookup.TrackedRealSign {
      private final SignChangeEvent event;
      private final boolean front;

      public TrackedChangingSign(final SignChangeEvent event) {
         super(FakeSign.create(event.getBlock()), event.getBlock(), RailPiece.NONE);
         this.front = BlockUtil.isChangingFrontLines(event);
         ((FakeSign)this.sign).setHandler(new FakeSign.HandlerSignFallback(this.signBlock) {
            public String getFrontLine(int index) {
               return TrackedChangingSign.this.front ? event.getLine(index) : super.getFrontLine(index);
            }

            public void setFrontLine(int index, String text) {
               if (TrackedChangingSign.this.front) {
                  event.setLine(index, text);
               } else {
                  super.setFrontLine(index, text);
               }

            }

            public String getBackLine(int index) {
               return TrackedChangingSign.this.front ? super.getBackLine(index) : event.getLine(index);
            }

            public void setBackLine(int index, String text) {
               if (TrackedChangingSign.this.front) {
                  super.setBackLine(index, text);
               } else {
                  event.setLine(index, text);
               }

            }
         });
         this.rail = null;
         this.event = event;
      }

      public boolean isFrontText() {
         return this.front;
      }

      public boolean verify() {
         return false;
      }

      public boolean isRemoved() {
         return !(Boolean)MaterialUtil.ISSIGN.get(this.event.getBlock());
      }

      public BlockFace getFacing() {
         return BlockUtil.getFacing(this.event.getBlock());
      }

      public Block getAttachedBlock() {
         return BlockUtil.getAttachedBlock(this.event.getBlock());
      }

      public String[] getExtraLines() {
         return new String[0];
      }

      public PowerState getPower(BlockFace from) {
         return PowerState.get(this.signBlock, from, this.getAction() != null ? PowerState.Options.SIGN_CONNECT_WIRE : PowerState.Options.SIGN);
      }

      public String getLine(int index) throws IndexOutOfBoundsException {
         return this.event.getLine(index);
      }

      public void setLine(int index, String line) throws IndexOutOfBoundsException {
         this.event.setLine(index, line);
      }

      public Object getUniqueKey() {
         return this;
      }
   }

   private static class MockCancellable implements Cancellable {
      private boolean cancelled;

      private MockCancellable() {
         this.cancelled = false;
      }

      public boolean isCancelled() {
         return this.cancelled;
      }

      public void setCancelled(boolean b) {
         this.cancelled = b;
      }

      // $FF: synthetic method
      MockCancellable(Object x0) {
         this();
      }
   }
}
