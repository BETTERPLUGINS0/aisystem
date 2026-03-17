package com.bergerkiller.bukkit.tc.controller.components;

import com.bergerkiller.bukkit.common.ToggledState;
import com.bergerkiller.bukkit.common.collections.ImplicitlySharedList;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.detector.DetectorRegion;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.properties.IPropertiesHolder;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.utils.modlist.ModificationTrackedList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.bukkit.block.Block;

public abstract class SignTracker {
   private static final ArrayList<SignTracker.ActiveSign> tmpSignBuffer = new ArrayList();
   private Set<Object> offlineLoadedSkippedSignKeys = Collections.emptySet();
   private Set<Object> offlineLoadedActiveSignKeys = Collections.emptySet();
   private final Map<Object, SignTracker.ActiveSign> activeSignsByKey = new LinkedHashMap();
   private final ImplicitlySharedList<SignTracker.ActiveSign> activeSigns = new ImplicitlySharedList();
   protected ImplicitlySharedList<DetectorRegion> detectorRegions = new ImplicitlySharedList();
   protected final ToggledState needsUpdate = new ToggledState();
   protected final SignSkipTracker signSkipTracker;

   protected SignTracker(IPropertiesHolder owner) {
      this.signSkipTracker = new SignSkipTracker(owner);
   }

   public abstract TrainCarts.Provider getOwner();

   public SignSkipTracker getSignSkipTracker() {
      return this.signSkipTracker;
   }

   public ImplicitlySharedList<SignTracker.ActiveSign> getActiveTrackedSigns() {
      return this.activeSigns;
   }

   public Collection<DetectorRegion> getActiveDetectorRegions() {
      return this.detectorRegions;
   }

   public void addOfflineSkippedSignKey(Object signUniqueKey) {
      if (this.offlineLoadedSkippedSignKeys.isEmpty()) {
         this.offlineLoadedSkippedSignKeys = new HashSet();
      }

      this.offlineLoadedSkippedSignKeys.add(signUniqueKey);
   }

   protected void addOfflineActiveSignKey(Object signUniqueKey) {
      if (this.offlineLoadedActiveSignKeys.isEmpty()) {
         this.offlineLoadedActiveSignKeys = new HashSet();
      }

      this.offlineLoadedActiveSignKeys.add(signUniqueKey);
   }

   protected void clearOfflineActiveSignKeys() {
      this.offlineLoadedActiveSignKeys = Collections.emptySet();
      this.offlineLoadedSkippedSignKeys = Collections.emptySet();
   }

   protected void onSignVisitStart(List<SignTracker.ActiveSign> signs) {
      if (!signs.isEmpty()) {
         if (!this.offlineLoadedActiveSignKeys.isEmpty()) {
            this.signSkipTracker.loadSigns((List)signs.stream().filter((s) -> {
               return this.offlineLoadedActiveSignKeys.contains(s.getUniqueKey());
            }).collect(Collectors.toList()));
         } else if (!this.offlineLoadedSkippedSignKeys.isEmpty()) {
            this.signSkipTracker.loadSigns(Collections.emptyList());
         }

         if (!this.offlineLoadedSkippedSignKeys.isEmpty()) {
            Iterator var2 = signs.iterator();

            while(var2.hasNext()) {
               SignTracker.ActiveSign sign = (SignTracker.ActiveSign)var2.next();
               if (this.offlineLoadedSkippedSignKeys.contains(sign.getUniqueKey())) {
                  this.signSkipTracker.setSkipped(sign);
               }
            }
         }
      }

      this.signSkipTracker.onSignVisitStart(signs);
   }

   public boolean isSkipped(RailLookup.TrackedSign sign) {
      return this.signSkipTracker.isSkipped(sign);
   }

   public boolean containsSign(RailLookup.TrackedSign sign) {
      if (sign != null) {
         SignTracker.ActiveSign existing = (SignTracker.ActiveSign)this.activeSignsByKey.get(sign.getUniqueKey());
         if (existing == null) {
            return false;
         }

         if (sign == existing.sign) {
            return true;
         }

         if (sign.isRealSign() && existing.sign.isRealSign()) {
            return sign.signBlock.equals(existing.sign.signBlock);
         }
      }

      return false;
   }

   public boolean removeSign(RailLookup.TrackedSign sign) {
      if (sign == null) {
         return false;
      } else {
         SignTracker.ActiveSign removed = (SignTracker.ActiveSign)this.activeSignsByKey.remove(sign.getUniqueKey());
         if (removed != null) {
            this.activeSigns.remove(removed);
            this.onSignChange(removed, false);
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean hasSigns() {
      return !this.activeSigns.isEmpty();
   }

   public final void clear() {
      this.clear(SignTracker.ClearMode.LEAVE);
   }

   public void clear(SignTracker.ClearMode clearMode) {
      if (!this.activeSignsByKey.isEmpty()) {
         int maxResetIterCtr = 100;
         int expectedCount = this.activeSignsByKey.size();
         Iterator iter = this.activeSignsByKey.values().iterator();

         while(iter.hasNext()) {
            SignTracker.ActiveSign sign = (SignTracker.ActiveSign)iter.next();
            iter.remove();
            this.activeSigns.remove(sign);
            --expectedCount;
            clearMode.eventHandler.accept(this, sign);
            if (expectedCount != this.activeSignsByKey.size()) {
               expectedCount = this.activeSignsByKey.size();
               iter = this.activeSignsByKey.values().iterator();
               --maxResetIterCtr;
               if (maxResetIterCtr <= 0) {
                  this.getOwner().getTrainCarts().log(Level.WARNING, "[SignTracker] Number of iteration reset attempts exceeded limit");
                  break;
               }
            }
         }

         this.activeSigns.clear();
         this.activeSignsByKey.clear();
      }

   }

   public void update() {
      this.needsUpdate.set();
   }

   public void clearUpdates() {
      this.needsUpdate.clear();
   }

   /** @deprecated */
   @Deprecated
   public abstract boolean isOnRails(Block var1);

   protected abstract void onSignChange(SignTracker.ActiveSign var1, boolean var2);

   protected abstract void onLoadedChange(SignTracker.ActiveSign var1, boolean var2);

   protected void updateActiveSigns(Supplier<ModificationTrackedList<SignTracker.ActiveSign>> activeSignListSupplier) {
      int limit = 1000;

      while(!this.tryUpdateActiveSigns((ModificationTrackedList)activeSignListSupplier.get())) {
         --limit;
         if (limit == 0) {
            this.getOwner().getTrainCarts().getLogger().log(Level.SEVERE, "Reached limit of loops updating active signs");
            break;
         }
      }

   }

   private boolean tryUpdateActiveSigns(ModificationTrackedList<SignTracker.ActiveSign> list) {
      int mod_start = list.getModCount();
      boolean hadSigns = !this.activeSigns.isEmpty();
      Iterator iter;
      SignTracker.ActiveSign newActiveSign;
      if (list.isEmpty()) {
         if (hadSigns) {
            iter = this.activeSignsByKey.values().iterator();

            while(iter.hasNext()) {
               newActiveSign = (SignTracker.ActiveSign)iter.next();
               this.activeSigns.remove(newActiveSign);
               iter.remove();
               this.onSignChange(newActiveSign, false);
               if (list.getModCount() != mod_start) {
                  return false;
               }
            }
         }

         return true;
      } else {
         this.activeSigns.forEach((a) -> {
            a.detected = false;
         });
         iter = list.iterator();

         while(true) {
            while(iter.hasNext()) {
               newActiveSign = (SignTracker.ActiveSign)iter.next();
               SignTracker.ActiveSign currActiveSign = (SignTracker.ActiveSign)this.activeSignsByKey.computeIfAbsent(newActiveSign.getUniqueKey(), (u) -> {
                  return new SignTracker.ActiveSign(newActiveSign.sign, (RailState)null);
               });
               currActiveSign.detected = true;
               if (currActiveSign.enterState == null) {
                  currActiveSign.enterState = newActiveSign.enterState;
                  if (this.offlineLoadedActiveSignKeys.contains(currActiveSign.getUniqueKey())) {
                     this.activeSigns.add(currActiveSign);
                     this.signSkipTracker.setSkipped(currActiveSign, false);
                     this.onLoadedChange(currActiveSign, true);
                  } else if (this.signSkipTracker.onSignVisit(currActiveSign)) {
                     this.activeSigns.add(currActiveSign);
                     this.onSignChange(currActiveSign, true);
                  }
               } else {
                  if (currActiveSign.getAction() == newActiveSign.sign.getAction()) {
                     if (currActiveSign.sign == newActiveSign.sign) {
                        continue;
                     }

                     if (currActiveSign.sign.hasIdenticalText(newActiveSign.sign)) {
                        currActiveSign.setSign(newActiveSign.sign);
                        continue;
                     }
                  }

                  SignAction action = currActiveSign.getAction();
                  boolean fireEvents = true;
                  if (action != null && newActiveSign.sign.getAction() == action) {
                     SignActionEvent event = newActiveSign.sign.createEvent(SignActionType.NONE);
                     fireEvents = action.signTextChanged(event);
                  }

                  if (fireEvents) {
                     this.onSignChange(currActiveSign, false);
                  }

                  currActiveSign.setSign(newActiveSign.sign);
                  if (fireEvents) {
                     this.onSignChange(currActiveSign, true);
                  }
               }

               if (list.getModCount() != mod_start) {
                  return false;
               }
            }

            if (hadSigns) {
               this.forEachActiveSignSafe((currActiveSignx) -> {
                  if (!currActiveSignx.detected) {
                     SignTracker.ActiveSign removed = (SignTracker.ActiveSign)this.activeSignsByKey.remove(currActiveSignx.getUniqueKey());
                     if (removed != null) {
                        this.activeSigns.remove(removed);
                     }

                     if (removed == currActiveSignx) {
                        this.onSignChange(currActiveSignx, false);
                     }
                  }

               });
               if (list.getModCount() != mod_start) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   private void forEachActiveSignSafe(Consumer<SignTracker.ActiveSign> action) {
      List<SignTracker.ActiveSign> buffer = tmpSignBuffer;
      if (buffer.isEmpty()) {
         buffer.addAll(this.activeSigns);

         try {
            buffer.forEach(action);
         } finally {
            buffer.clear();
         }
      } else {
         ImplicitlySharedList copy = this.activeSigns.clone();

         try {
            copy.forEach(action);
         } catch (Throwable var11) {
            if (copy != null) {
               try {
                  copy.close();
               } catch (Throwable var9) {
                  var11.addSuppressed(var9);
               }
            }

            throw var11;
         }

         if (copy != null) {
            copy.close();
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public Collection<Block> getActiveSigns() {
      return (Collection)this.getActiveTrackedSigns().stream().map((s) -> {
         return s.sign;
      }).filter(RailLookup.TrackedSign::isRealSign).map((s) -> {
         return s.signBlock;
      }).collect(StreamUtil.toUnmodifiableList());
   }

   /** @deprecated */
   @Deprecated
   public boolean containsSign(Block signblock) {
      SignTracker.ActiveSign sign = (SignTracker.ActiveSign)this.activeSignsByKey.get(signblock);
      return sign != null && sign.sign.isRealSign();
   }

   /** @deprecated */
   @Deprecated
   public boolean removeSign(Block signBlock) {
      SignTracker.ActiveSign removed = (SignTracker.ActiveSign)this.activeSignsByKey.remove(signBlock);
      if (removed != null && removed.sign.isRealSign()) {
         this.activeSigns.remove(removed);
         this.onSignChange(removed, false);
         return true;
      } else {
         this.activeSignsByKey.put(signBlock, removed);
         return false;
      }
   }

   public static final class ActiveSign {
      private RailLookup.TrackedSign sign;
      private SignAction action;
      private Object uniqueKey;
      private RailState enterState;
      private boolean detected;

      public ActiveSign(RailLookup.TrackedSign sign, RailState enterState) {
         this.sign = sign;
         this.action = sign.getAction();
         this.uniqueKey = sign.getUniqueKey();
         this.enterState = enterState;
         this.detected = true;
      }

      public RailLookup.TrackedSign getSign() {
         return this.sign;
      }

      public SignAction getAction() {
         return this.action;
      }

      private void setSign(RailLookup.TrackedSign sign) {
         this.sign = sign;
         this.action = sign.getAction();
         this.uniqueKey = sign.getUniqueKey();
      }

      public Object getUniqueKey() {
         return this.uniqueKey;
      }

      public RailState getEnterState() {
         return this.enterState;
      }

      public void executeEventForMember(SignActionType action, MinecartMember<?> member) {
         this.sign.executeEventForMember(action, member, this.enterState);
      }

      public void executeEventForGroup(SignActionType action, MinecartGroup group) {
         this.sign.executeEventForGroup(action, group, this.enterState);
      }
   }

   public static enum ClearMode {
      UNLOAD((tracker, sign) -> {
         tracker.onLoadedChange(sign, false);
      }),
      LEAVE((tracker, sign) -> {
         tracker.onSignChange(sign, false);
      }),
      SILENT((tracker, sign) -> {
      });

      private final BiConsumer<SignTracker, SignTracker.ActiveSign> eventHandler;

      private ClearMode(BiConsumer<SignTracker, SignTracker.ActiveSign> eventHandler) {
         this.eventHandler = eventHandler;
      }

      // $FF: synthetic method
      private static SignTracker.ClearMode[] $values() {
         return new SignTracker.ClearMode[]{UNLOAD, LEAVE, SILENT};
      }
   }
}
