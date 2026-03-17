package com.bergerkiller.bukkit.tc.controller.components;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentNameLookup;
import com.bergerkiller.bukkit.tc.attachments.config.SavedAttachmentModel;
import com.bergerkiller.bukkit.tc.attachments.config.SavedAttachmentModelStore;
import com.bergerkiller.bukkit.tc.attachments.helper.AttachmentUpdateTransformHelper;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.utils.SetCallbackCollector;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.Player;

public class AttachmentControllerGroup implements SavedAttachmentModelStore.ModelUsing, AttachmentNameLookup.Supplier {
   public static final int ABSOLUTE_UPDATE_INTERVAL = 200;
   public static final int MOVEMENT_UPDATE_INTERVAL = 3;
   private final MinecartGroup group;
   private int movementCounter;
   private int ticksSinceLocationSync = 0;
   private SoftReference<AttachmentNameLookup> cachedByNameLookup = new SoftReference((Object)null);

   public AttachmentControllerGroup(MinecartGroup group) {
      this.group = group;
   }

   public MinecartGroup getGroup() {
      return this.group;
   }

   public void syncPrePositionUpdate(AttachmentUpdateTransformHelper updater) {
      Iterator var2 = this.group.iterator();

      while(var2.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var2.next();
         member.getAttachments().syncPrePositionUpdate(updater);
      }

   }

   public void syncPositionAbsolute() {
      this.ticksSinceLocationSync = 0;
      Iterator var1 = this.group.iterator();

      while(var1.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var1.next();
         member.getAttachments().syncMovement(true);
      }

   }

   public void syncPostPositionUpdate() {
      Iterator var1 = this.group.iterator();

      while(var1.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var1.next();
         member.getAttachments().syncPostPositionUpdate();
      }

      boolean isUpdateTick = false;
      if (++this.movementCounter >= 3) {
         this.movementCounter = 0;
         isUpdateTick = true;
      }

      if (++this.ticksSinceLocationSync > 200) {
         this.ticksSinceLocationSync = 0;
         Iterator var6 = this.group.iterator();

         while(var6.hasNext()) {
            MinecartMember<?> member = (MinecartMember)var6.next();
            member.getAttachments().syncMovement(true);
         }
      } else {
         boolean needsSync = isUpdateTick;
         MinecartMember member;
         Iterator var8;
         if (!isUpdateTick) {
            label69: {
               var8 = this.group.iterator();

               do {
                  do {
                     if (!var8.hasNext()) {
                        break label69;
                     }

                     member = (MinecartMember)var8.next();
                  } while(member.isUnloaded());
               } while(!((CommonMinecart)member.getEntity()).isPositionChanged() && !((CommonMinecart)member.getEntity()).getDataWatcher().isChanged());

               needsSync = true;
            }
         }

         if (needsSync) {
            var8 = this.group.iterator();

            while(var8.hasNext()) {
               member = (MinecartMember)var8.next();
               member.getAttachments().syncMovement(false);
            }
         }
      }

   }

   public void syncRespawn() {
      List<AttachmentControllerGroup.RespawnedMember> members = new ArrayList(this.group.size());
      Iterator var2 = this.group.iterator();

      while(var2.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var2.next();
         members.add(new AttachmentControllerGroup.RespawnedMember(member));
      }

      members.forEach(AttachmentControllerGroup.RespawnedMember::hide);
      this.group.getTrainCarts().getTrainUpdateController().syncPositions((Collection)Collections.singletonList(this.group));
      members.forEach(AttachmentControllerGroup.RespawnedMember::show);
   }

   public void getUsedModels(SetCallbackCollector<SavedAttachmentModel> collector) {
      Iterator var2 = this.group.iterator();

      while(var2.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var2.next();
         member.getAttachments().getUsedModels(collector);
      }

   }

   public AttachmentNameLookup getNameLookup() {
      AttachmentNameLookup cached = (AttachmentNameLookup)this.cachedByNameLookup.get();
      if (cached == null || !cached.isValid()) {
         ArrayList<AttachmentNameLookup> components = new ArrayList(this.group.size());
         Iterator var3 = this.group.iterator();

         while(var3.hasNext()) {
            MinecartMember<?> member = (MinecartMember)var3.next();
            components.add(member.getAttachments().getNameLookup());
         }

         this.cachedByNameLookup = new SoftReference(cached = AttachmentNameLookup.merge(components));
      }

      return cached;
   }

   public void notifyGroupCompositionChanged() {
      AttachmentNameLookup cached = (AttachmentNameLookup)this.cachedByNameLookup.get();
      if (cached != null) {
         cached.invalidate();
      }

   }

   private static class RespawnedMember {
      public final MinecartMember<?> member;
      private List<Player> players;

      public RespawnedMember(MinecartMember<?> member) {
         this.member = member;
         this.players = Collections.emptyList();
      }

      public void hide() {
         synchronized(this.member.getAttachments()) {
            this.players = new ArrayList(this.member.getAttachments().getViewers());
            this.member.getAttachments().makeHiddenForAll();
         }
      }

      public void show() {
         synchronized(this.member.getAttachments()) {
            Iterator var2 = this.players.iterator();

            while(var2.hasNext()) {
               Player viewer = (Player)var2.next();
               if (!this.member.getAttachments().isViewer(viewer)) {
                  this.member.getAttachments().makeVisible(viewer);
               }
            }

         }
      }
   }
}
