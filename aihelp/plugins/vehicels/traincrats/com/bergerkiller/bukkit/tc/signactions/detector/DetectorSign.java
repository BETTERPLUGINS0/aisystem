package com.bergerkiller.bukkit.tc.signactions.detector;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.detector.DetectorListener;
import com.bergerkiller.bukkit.tc.detector.DetectorRegion;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.offline.sign.OfflineSign;
import com.bergerkiller.bukkit.tc.offline.sign.OfflineSignStore;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.signactions.SignActionDetector;
import com.bergerkiller.bukkit.tc.statements.Statement;
import java.util.Iterator;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class DetectorSign implements DetectorListener {
   private final OfflineSignStore store;
   private final OfflineSign sign;
   private DetectorSign.Metadata metadata;

   public DetectorSign(OfflineSignStore store, OfflineSign sign, DetectorSign.Metadata metadata) {
      this.store = store;
      this.sign = sign;
      this.metadata = metadata;
   }

   public IntVector3 getLocation() {
      return this.sign.getPosition();
   }

   public boolean isRemoved() {
      return this.metadata.owner != this;
   }

   public void remove() {
      this.store.remove(this.sign, DetectorSign.Metadata.class);
   }

   public void loadChunks(World world) {
      int cx = MathUtil.toChunk(this.sign.getPosition().x);
      int cz = MathUtil.toChunk(this.sign.getPosition().z);
      WorldUtil.loadChunks(world, cx, cz, 3);
   }

   public boolean validate(SignActionEvent event) {
      return SignActionDetector.INSTANCE.match(event);
   }

   public boolean isLoaded(World world) {
      return world != null;
   }

   public SignActionEvent initSignEvent() {
      Block signBlock = this.sign.getLoadedBlock();
      if (signBlock != null) {
         this.loadChunks(signBlock.getWorld());
         Sign sign = BlockUtil.getSign(signBlock);
         if (sign != null) {
            SignActionEvent event = new SignActionEvent(RailLookup.TrackedSign.forRealSign((Sign)sign, this.sign.isFrontText(), (RailPiece)null));
            if (this.validate(event)) {
               return event;
            }
         }

         this.remove();
         return null;
      } else {
         return null;
      }
   }

   public void onLeave(MinecartGroup group) {
      if (this.metadata.isLeverDown) {
         SignActionEvent event = this.initSignEvent();
         if (event != null && event.isTrainSign() && this.isLeverUpCheckNeeded(event, group)) {
            this.updateGroups(event);
         }
      }

   }

   public void onEnter(MinecartGroup group) {
      if (!this.metadata.isLeverDown && !this.isRemoved()) {
         SignActionEvent event = this.initSignEvent();
         if (event != null && event.isTrainSign() && this.isDown(event, (MinecartMember)null, group)) {
            this.store.putIfPresent((OfflineSign)this.sign, this.metadata = this.metadata.setLeverDown(true));
            event.setLevers(true);
         }
      }

   }

   public void onLeave(MinecartMember<?> member) {
      if (this.metadata.isLeverDown) {
         SignActionEvent event = this.initSignEvent();
         if (event != null && event.isCartSign() && this.isLeverUpCheckNeeded(event, member)) {
            this.updateMembers(event);
         }
      }

   }

   public void onEnter(MinecartMember<?> member) {
      if (!this.metadata.isLeverDown && !this.isRemoved()) {
         SignActionEvent event = this.initSignEvent();
         if (event != null && event.isCartSign() && this.isDown(event, member, (MinecartGroup)null)) {
            this.store.putIfPresent((OfflineSign)this.sign, this.metadata = this.metadata.setLeverDown(true));
            event.setLevers(true);
         }
      }

   }

   public boolean updateMembers(SignActionEvent event) {
      if (this.isRemoved()) {
         event.setLevers(false);
         return false;
      } else {
         Iterator var2 = this.metadata.region.getMembers().iterator();

         MinecartMember mm;
         do {
            if (!var2.hasNext()) {
               this.store.putIfPresent((OfflineSign)this.sign, this.metadata = this.metadata.setLeverDown(false));
               event.setLevers(false);
               return false;
            }

            mm = (MinecartMember)var2.next();
         } while(!this.isDown(event, mm, (MinecartGroup)null));

         this.store.putIfPresent((OfflineSign)this.sign, this.metadata = this.metadata.setLeverDown(true));
         event.setLevers(true);
         return true;
      }
   }

   public boolean updateGroups(SignActionEvent event) {
      if (this.isRemoved()) {
         event.setLevers(false);
         return false;
      } else {
         Iterator var2 = this.metadata.region.getGroups().iterator();

         MinecartGroup g;
         do {
            if (!var2.hasNext()) {
               this.store.putIfPresent((OfflineSign)this.sign, this.metadata = this.metadata.setLeverDown(false));
               event.setLevers(false);
               return false;
            }

            g = (MinecartGroup)var2.next();
         } while(!this.isDown(event, (MinecartMember)null, g));

         this.store.putIfPresent((OfflineSign)this.sign, this.metadata = this.metadata.setLeverDown(true));
         event.setLevers(true);
         return true;
      }
   }

   public void onUpdate(MinecartMember<?> member) {
      SignActionEvent event = this.initSignEvent();
      if (event != null) {
         this.updateMembers(event);
      }

   }

   public void onUpdate(MinecartGroup group) {
      SignActionEvent event = this.initSignEvent();
      if (event != null) {
         this.updateGroups(event);
      }

   }

   public boolean isLeverUpCheckNeeded(SignActionEvent event, MinecartMember<?> member) {
      return !this.metadata.region.hasMembers() || this.isDown(event, member, (MinecartGroup)null);
   }

   public boolean isLeverUpCheckNeeded(SignActionEvent event, MinecartGroup group) {
      return !this.metadata.region.hasGroups() || this.isDown(event, (MinecartMember)null, group);
   }

   public boolean isDown(SignActionEvent event, MinecartMember<?> member, MinecartGroup group) {
      if (member != null) {
         event.setMember(member);
      } else if (group != null) {
         event.setGroup(group);
      } else {
         event.setGroup((MinecartGroup)null);
      }

      boolean firstEmpty = false;
      if (event.getLine(2).isEmpty()) {
         firstEmpty = true;
      } else if (Statement.has(member, group, event.getLine(2), event)) {
         return true;
      }

      return event.getLine(3).isEmpty() ? firstEmpty : Statement.has(member, group, event.getLine(3), event);
   }

   public void onRegister(DetectorRegion region) {
   }

   public void onUnregister(DetectorRegion region) {
   }

   public void onUnload(MinecartGroup group) {
      this.onLeave(group);
   }

   public static class Metadata {
      public final OfflineBlock otherSign;
      public final boolean otherSignFront;
      public final DetectorRegion region;
      public final boolean isLeverDown;
      public DetectorSign owner;

      public Metadata(RailLookup.TrackedSign otherSign, DetectorRegion region, boolean isLeverDown) {
         this(OfflineBlock.of(otherSign.signBlock), ((RailLookup.TrackedRealSign)otherSign).isFrontText(), region, isLeverDown);
      }

      public Metadata(OfflineBlock otherSign, boolean otherSignFront, DetectorRegion region, boolean isLeverDown) {
         this(otherSign, otherSignFront, region, isLeverDown, (DetectorSign)null);
      }

      private Metadata(OfflineBlock otherSign, boolean otherSignFront, DetectorRegion region, boolean isLeverDown, DetectorSign owner) {
         this.otherSign = otherSign;
         this.otherSignFront = otherSignFront;
         this.region = region;
         this.isLeverDown = isLeverDown;
         this.owner = owner;
      }

      public DetectorSign.Metadata setLeverDown(boolean down) {
         return down == this.isLeverDown ? this : new DetectorSign.Metadata(this.otherSign, this.otherSignFront, this.region, down, this.owner);
      }
   }
}
