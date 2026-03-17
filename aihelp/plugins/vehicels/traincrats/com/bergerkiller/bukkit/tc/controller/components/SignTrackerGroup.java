package com.bergerkiller.bukkit.tc.controller.components;

import com.bergerkiller.bukkit.common.ToggledState;
import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.detector.DetectorRegion;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.utils.modlist.ModificationTrackedEmptyList;
import com.bergerkiller.bukkit.tc.utils.modlist.ModificationTrackedList;
import com.bergerkiller.bukkit.tc.utils.modlist.ModificationTrackedList2D;
import java.util.Iterator;
import java.util.List;
import org.bukkit.block.Block;

public class SignTrackerGroup extends SignTracker {
   private final MinecartGroup owner;
   private final ToggledState needsPositionUpdate = new ToggledState(true);
   private final ModificationTrackedList2D<SignTracker.ActiveSign> liveActiveSigns = new ModificationTrackedList2D();

   public SignTrackerGroup(MinecartGroup owner) {
      super(owner);
      this.owner = owner;
   }

   public MinecartGroup getOwner() {
      return this.owner;
   }

   protected void onSignChange(SignTracker.ActiveSign sign, boolean active) {
      sign.executeEventForGroup(active ? SignActionType.GROUP_ENTER : SignActionType.GROUP_LEAVE, this.owner);
   }

   protected void onLoadedChange(SignTracker.ActiveSign sign, boolean loaded) {
      sign.executeEventForGroup(loaded ? SignActionType.GROUP_RELOAD : SignActionType.GROUP_UNLOAD, this.owner);
   }

   /** @deprecated */
   @Deprecated
   public MinecartMember<?> getMemberFromRails(Block railsBlock) {
      return this.owner.getRailTracker().getMemberFromRails(railsBlock);
   }

   /** @deprecated */
   @Deprecated
   public MinecartMember<?> getMemberFromRails(IntVector3 railsBlockPosition) {
      return this.owner.getRailTracker().getMemberFromRails(railsBlockPosition);
   }

   public void clear(SignTracker.ClearMode clearMode) {
      Iterator var2 = this.owner.iterator();

      while(var2.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var2.next();
         member.getSignTracker().clear(clearMode);
      }

      super.clear(clearMode);
      this.detectorRegions.clear();
   }

   public void unload(SignTracker.ClearMode clearMode) {
      Iterator var2;
      if (!this.detectorRegions.isEmpty()) {
         var2 = this.detectorRegions.iterator();

         while(var2.hasNext()) {
            DetectorRegion region = (DetectorRegion)var2.next();
            region.unload(this.owner);
         }

         this.detectorRegions.clear();
      }

      this.clear(clearMode);
      this.signSkipTracker.unloadSigns();
      var2 = this.owner.iterator();

      while(var2.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var2.next();
         member.getSignTracker().signSkipTracker.unloadSigns();
      }

   }

   /** @deprecated */
   @Deprecated
   public boolean isOnRails(Block railsBlock) {
      return this.owner.getRailTracker().isOnRails(railsBlock);
   }

   public void onMemberRemoved(MinecartMember<?> member) {
      this.removeDetectorRegionsOf(member);
      this.updatePosition();
      this.liveActiveSigns.removeList(member.getSignTracker().liveActiveSigns);
   }

   private void removeDetectorRegionsOf(MinecartMember<?> member) {
      if (!this.detectorRegions.isEmpty()) {
         Iterator iter = member.getSignTracker().detectorRegions.cloneAsIterable().iterator();

         DetectorRegion region;
         while(iter.hasNext()) {
            region = (DetectorRegion)iter.next();
            region.remove(member);
         }

         member.getSignTracker().detectorRegions.clear();
         iter = this.detectorRegions.iterator();

         while(iter.hasNext()) {
            region = (DetectorRegion)iter.next();
            boolean used = false;
            Iterator var5 = this.owner.iterator();

            while(var5.hasNext()) {
               MinecartMember<?> otherMember = (MinecartMember)var5.next();
               if (otherMember != member && otherMember.getSignTracker().detectorRegions.contains(region)) {
                  used = true;
                  break;
               }
            }

            if (!used) {
               iter.remove();
            }
         }

      }
   }

   public void updatePosition() {
      this.needsPositionUpdate.set();
   }

   /** @deprecated */
   @Deprecated
   public boolean removeSign(Block signBlock) {
      if (!super.removeSign(signBlock)) {
         return false;
      } else {
         Iterator var2 = this.owner.iterator();

         while(var2.hasNext()) {
            MinecartMember<?> member = (MinecartMember)var2.next();
            member.getSignTracker().removeSign(signBlock);
         }

         return true;
      }
   }

   public boolean removeSign(RailLookup.TrackedSign sign) {
      if (!super.removeSign(sign)) {
         return false;
      } else {
         Iterator var2 = this.owner.iterator();

         while(var2.hasNext()) {
            MinecartMember<?> member = (MinecartMember)var2.next();
            member.getSignTracker().removeSign(sign);
         }

         return true;
      }
   }

   protected void clearOfflineActiveSignKeys() {
      super.clearOfflineActiveSignKeys();
      Iterator var1 = this.owner.iterator();

      while(var1.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var1.next();
         member.getSignTracker().clearOfflineActiveSignKeys();
      }

   }

   public void refresh() {
      if (this.owner.isEmpty()) {
         this.clearOfflineActiveSignKeys();
         this.clear();
      } else {
         Iterator var1;
         MinecartMember member;
         Iterator iter;
         DetectorRegion region;
         if (this.needsPositionUpdate.clear()) {
            var1 = this.owner.iterator();

            while(var1.hasNext()) {
               member = (MinecartMember)var1.next();
               member.getSignTracker().liveActiveSigns.clear();
            }

            var1 = this.owner.getRailTracker().getRailInformation().iterator();

            label224:
            while(true) {
               RailLookup.TrackedSign[] signs;
               int var6;
               int var7;
               RailTracker.TrackedRail info;
               do {
                  do {
                     if (!var1.hasNext()) {
                        var1 = this.owner.iterator();

                        while(var1.hasNext()) {
                           member = (MinecartMember)var1.next();
                           member.getSignTracker().onSignVisitStart(member.getSignTracker().liveActiveSigns);
                        }

                        this.liveActiveSigns.resetLists();
                        var1 = this.owner.iterator();

                        while(var1.hasNext()) {
                           member = (MinecartMember)var1.next();
                           this.liveActiveSigns.addListIfNotEmpty(member.getSignTracker().liveActiveSigns);
                        }

                        this.onSignVisitStart(this.liveActiveSigns);
                        MinecartMember[] var12 = this.owner.toArray();
                        int var14 = var12.length;

                        for(int var15 = 0; var15 < var14; ++var15) {
                           MinecartMember<?> member = var12[var15];
                           if (!member.isUnloaded() && member.getGroup() == this.owner) {
                              SignTrackerMember tracker = member.getSignTracker();
                              tracker.updateActiveSigns(() -> {
                                 return tracker.getOwner().isUnloaded() ? ModificationTrackedEmptyList.emptyList() : tracker.liveActiveSigns;
                              });
                           }
                        }

                        this.updateActiveSigns(() -> {
                           return (ModificationTrackedList)(this.owner.isUnloaded() ? ModificationTrackedEmptyList.emptyList() : this.liveActiveSigns);
                        });
                        List<RailTracker.TrackedRail> rails = this.getOwner().getRailTracker().getRailInformation();
                        int var27;
                        DetectorRegion region;
                        if (!this.detectorRegions.isEmpty()) {
                           MinecartMember<?>[] members = this.getOwner().toArray();
                           MinecartMember[] var17 = members;
                           int i = members.length;

                           for(var27 = 0; var27 < i; ++var27) {
                              MinecartMember<?> member = var17[var27];
                              member.getSignTracker().detectorRegions.clear();
                           }

                           String currentWorldName = this.getOwner().getWorld().getName();
                           i = this.detectorRegions.size() - 1;

                           label176:
                           while(true) {
                              int var38;
                              if (i < 0) {
                                 iter = rails.iterator();

                                 while(iter.hasNext()) {
                                    RailTracker.TrackedRail rail = (RailTracker.TrackedRail)iter.next();
                                    Iterator var34 = this.detectorRegions.cloneAsIterable().iterator();

                                    while(var34.hasNext()) {
                                       region = (DetectorRegion)var34.next();
                                       if (region.getCoordinates().contains(rail.state.railPiece().blockPosition())) {
                                          List<DetectorRegion> memberRegions = rail.member.getSignTracker().detectorRegions;
                                          if (!memberRegions.contains(region)) {
                                             memberRegions.add(region);
                                             region.add(rail.member);
                                          }
                                       }
                                    }
                                 }

                                 iter = this.detectorRegions.iterator();

                                 while(true) {
                                    if (!iter.hasNext()) {
                                       break label176;
                                    }

                                    region = (DetectorRegion)iter.next();
                                    boolean foundMember = false;
                                    MinecartMember[] var39 = members;
                                    var38 = members.length;

                                    for(int var41 = 0; var41 < var38; ++var41) {
                                       MinecartMember<?> member = var39[var41];
                                       if (member.getSignTracker().detectorRegions.contains(region)) {
                                          foundMember = true;
                                       } else {
                                          region.remove(member);
                                       }
                                    }

                                    if (!foundMember) {
                                       iter.remove();
                                    }
                                 }
                              }

                              region = (DetectorRegion)this.detectorRegions.get(i);
                              if (!region.getWorldName().equals(currentWorldName)) {
                                 MinecartMember[] var32 = members;
                                 var7 = members.length;

                                 for(var38 = 0; var38 < var7; ++var38) {
                                    MinecartMember<?> member = var32[var38];
                                    region.remove(member);
                                 }

                                 this.detectorRegions.remove(i);
                              }

                              --i;
                           }
                        }

                        Iterator var18 = rails.iterator();

                        while(var18.hasNext()) {
                           RailTracker.TrackedRail rail = (RailTracker.TrackedRail)var18.next();
                           DetectorRegion[] var30 = rail.state.railPiece().detectorRegions();
                           var27 = var30.length;

                           for(var6 = 0; var6 < var27; ++var6) {
                              region = var30[var6];
                              rail.member.getSignTracker().addToDetectorRegion(region);
                           }
                        }
                        break label224;
                     }

                     info = (RailTracker.TrackedRail)var1.next();
                  } while(info.state.railType() == RailType.NONE);

                  signs = info.state.railSigns();
               } while(signs.length <= 0);

               ModificationTrackedList<SignTracker.ActiveSign> memberSigns = info.member.getSignTracker().liveActiveSigns;
               RailLookup.TrackedSign[] var5 = signs;
               var6 = signs.length;

               for(var7 = 0; var7 < var6; ++var7) {
                  RailLookup.TrackedSign sign = var5[var7];
                  if (sign.getAction() != null || !sign.getHeader().isEmpty()) {
                     memberSigns.add(new SignTracker.ActiveSign(sign, info.state));
                  }
               }
            }
         }

         if (this.needsUpdate.clear()) {
            var1 = this.getActiveTrackedSigns().cloneAsIterable().iterator();

            while(var1.hasNext()) {
               SignTracker.ActiveSign activeSign = (SignTracker.ActiveSign)var1.next();
               activeSign.executeEventForGroup(SignActionType.GROUP_UPDATE, this.owner);
            }

            var1 = this.detectorRegions.cloneAsIterable().iterator();

            while(var1.hasNext()) {
               DetectorRegion region = (DetectorRegion)var1.next();
               region.update(this.owner);
            }

            var1 = this.owner.iterator();

            label116:
            while(true) {
               SignTrackerMember tracker;
               do {
                  if (!var1.hasNext()) {
                     break label116;
                  }

                  member = (MinecartMember)var1.next();
                  tracker = member.getSignTracker();
               } while(!tracker.needsUpdate.clear());

               iter = tracker.getActiveTrackedSigns().iterator();

               while(iter.hasNext()) {
                  SignTracker.ActiveSign activeSign = (SignTracker.ActiveSign)iter.next();
                  activeSign.executeEventForMember(SignActionType.MEMBER_UPDATE, tracker.getOwner());
               }

               iter = tracker.detectorRegions.cloneAsIterable().iterator();

               while(iter.hasNext()) {
                  region = (DetectorRegion)iter.next();
                  region.update(tracker.getOwner());
               }
            }
         }

         this.clearOfflineActiveSignKeys();
      }
   }
}
