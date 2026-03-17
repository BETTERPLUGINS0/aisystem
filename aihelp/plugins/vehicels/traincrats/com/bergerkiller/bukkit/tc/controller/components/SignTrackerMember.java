package com.bergerkiller.bukkit.tc.controller.components;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.detector.DetectorRegion;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.utils.modlist.ModificationTrackedArrayList;
import com.bergerkiller.bukkit.tc.utils.modlist.ModificationTrackedList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.block.Block;

public class SignTrackerMember extends SignTracker {
   private final MinecartMember<?> owner;
   protected final ModificationTrackedList<SignTracker.ActiveSign> liveActiveSigns = new ModificationTrackedArrayList();

   public SignTrackerMember(MinecartMember<?> owner) {
      super(owner);
      this.owner = owner;
   }

   public MinecartMember<?> getOwner() {
      return this.owner;
   }

   public boolean addToDetectorRegion(DetectorRegion region) {
      if (!region.add(this.owner)) {
         return false;
      } else {
         this.detectorRegions.add(region);
         List<DetectorRegion> groupRegions = this.owner.getGroup().getSignTracker().detectorRegions;
         if (!groupRegions.contains(region)) {
            groupRegions.add(region);
         }

         return true;
      }
   }

   public void addOfflineActiveSignKey(Object signUniqueKey) {
      super.addOfflineActiveSignKey(signUniqueKey);
      this.owner.getGroup().getSignTracker().addOfflineActiveSignKey(signUniqueKey);
   }

   public void clear(SignTracker.ClearMode clearMode) {
      super.clear(clearMode);
      if (!this.detectorRegions.isEmpty()) {
         Iterator var2 = this.detectorRegions.cloneAsIterable().iterator();

         while(var2.hasNext()) {
            DetectorRegion region = (DetectorRegion)var2.next();
            region.remove(this.owner);
         }

         this.detectorRegions.clear();
      }

   }

   /** @deprecated */
   @Deprecated
   public boolean isOnRails(Block railsBlock) {
      return this.owner.getRailTracker().isOnRails(railsBlock);
   }

   protected void onSignChange(SignTracker.ActiveSign sign, boolean active) {
      sign.executeEventForMember(active ? SignActionType.MEMBER_ENTER : SignActionType.MEMBER_LEAVE, this.owner);
   }

   protected void onLoadedChange(SignTracker.ActiveSign sign, boolean loaded) {
   }

   public void update() {
      super.update();
      if (!this.owner.isUnloaded()) {
         MinecartGroup group = this.owner.getGroup();
         if (group != null) {
            group.getSignTracker().update();
         }
      }

   }
}
