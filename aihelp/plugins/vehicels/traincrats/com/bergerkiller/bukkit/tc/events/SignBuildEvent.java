package com.bergerkiller.bukkit.tc.events;

import com.bergerkiller.bukkit.common.events.SignEditTextEvent;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.tc.PowerState;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.utils.FakeSign;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.SignChangeEvent;

public class SignBuildEvent extends SignChangeActionEvent {
   private static final HandlerList handlers = new HandlerList();
   private final SignAction action;

   public SignBuildEvent(Player player, RailLookup.TrackedSign sign, boolean interactive) {
      super(player, sign, interactive);
      this.action = SignAction.getSignAction(this);
   }

   public SignBuildEvent(Player player, RailLookup.TrackedSign sign, boolean interactive, SignAction action) {
      super(player, sign, interactive);
      this.action = action;
   }

   /** @deprecated */
   @Deprecated
   public SignBuildEvent(SignChangeEvent event, boolean interactive) {
      super(event, interactive);
      this.action = SignAction.getSignAction(this);
   }

   /** @deprecated */
   @Deprecated
   public SignBuildEvent(SignChangeActionEvent event) {
      super(event);
      this.action = SignAction.getSignAction(event);
   }

   protected SignBuildEvent(Cancellable event, Player player, RailLookup.TrackedSign sign, boolean interactive) {
      super(event, player, sign, interactive);
      this.action = SignAction.getSignAction(this);
   }

   public boolean hasRegisteredAction() {
      return this.action != null;
   }

   public SignAction getRegisteredAction() {
      return this.action;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static class BKCLSignEditBuildEvent extends SignBuildEvent {
      public static SignBuildEvent create(SignEditTextEvent event, boolean interactive) {
         return new SignBuildEvent.BKCLSignEditBuildEvent(event, interactive);
      }

      private BKCLSignEditBuildEvent(SignEditTextEvent event, boolean interactive) {
         super(event, event.getPlayer(), new SignBuildEvent.BKCLSignEditBuildEvent.TrackedEditedSign(event), interactive);
      }

      private static class TrackedEditedSign extends RailLookup.TrackedRealSign {
         private final SignEditTextEvent event;
         private final boolean front;

         public TrackedEditedSign(final SignEditTextEvent event) {
            super(FakeSign.create(event.getBlock()), event.getBlock(), RailPiece.NONE);
            this.front = event.getSide().isFront();
            ((FakeSign)this.sign).setHandler(new FakeSign.HandlerSignFallback(this.signBlock) {
               public String getFrontLine(int index) {
                  return TrackedEditedSign.this.front ? event.getLine(index) : super.getFrontLine(index);
               }

               public void setFrontLine(int index, String text) {
                  if (TrackedEditedSign.this.front) {
                     event.setLine(index, text);
                  } else {
                     super.setFrontLine(index, text);
                  }

               }

               public String getBackLine(int index) {
                  return TrackedEditedSign.this.front ? super.getBackLine(index) : event.getLine(index);
               }

               public void setBackLine(int index, String text) {
                  if (TrackedEditedSign.this.front) {
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
   }
}
