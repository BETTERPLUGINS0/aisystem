package com.bergerkiller.bukkit.tc.attachments;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract class VirtualSpawnableObject {
   protected final AttachmentManager manager;
   private final ArrayList<AttachmentViewer> viewers = new ArrayList();
   private List<AttachmentViewer> viewersPendingGlowColorRemoval = Collections.emptyList();
   private ChatColor glowColor = null;

   public VirtualSpawnableObject(AttachmentManager manager) {
      this.manager = manager;
   }

   protected abstract void sendSpawnPackets(AttachmentViewer var1, Vector var2);

   protected abstract void sendDestroyPackets(AttachmentViewer var1);

   protected void applyGlowing(ChatColor color) {
   }

   protected void applyGlowColorForViewer(AttachmentViewer viewer, ChatColor color) {
   }

   public abstract void updatePosition(Matrix4x4 var1);

   public abstract void syncPosition(boolean var1);

   public abstract boolean containsEntityId(int var1);

   public final void setGlowColor(ChatColor color) {
      if (this.glowColor != color) {
         this.glowColor = color;
         this.applyGlowing(color);
         if (color == null) {
            this.viewersPendingGlowColorRemoval = (List)(this.viewers.isEmpty() ? Collections.emptyList() : new ArrayList(this.viewers));
         } else {
            this.viewersPendingGlowColorRemoval = Collections.emptyList();
            this.forAllViewers((viewer) -> {
               this.applyGlowColorForViewer(viewer, color);
            });
         }
      }

   }

   public void setUseMinecartInterpolation(boolean use) {
   }

   public final ChatColor getGlowColor() {
      return this.glowColor;
   }

   /** @deprecated */
   @Deprecated
   public void addViewerWithoutSpawning(Player viewer) {
      this.addViewerWithoutSpawning(this.asAttachmentViewer(viewer));
   }

   public void addViewerWithoutSpawning(AttachmentViewer viewer) {
      if (!this.viewers.contains(viewer)) {
         this.viewers.add(viewer);
      }

   }

   public boolean hasViewers() {
      return !this.viewers.isEmpty();
   }

   public Collection<AttachmentViewer> getViewers() {
      return this.viewers;
   }

   public void forAllViewers(Consumer<? super AttachmentViewer> action) {
      this.viewers.forEach(action);
   }

   /** @deprecated */
   @Deprecated
   public boolean isViewer(Player viewer) {
      return this.isViewer(this.asAttachmentViewer(viewer));
   }

   public boolean isViewer(AttachmentViewer viewer) {
      return this.viewers.contains(viewer);
   }

   /** @deprecated */
   @Deprecated
   public void spawn(Player viewer, Vector motion) {
      this.spawn(this.asAttachmentViewer(viewer), motion);
   }

   public void spawn(AttachmentViewer viewer, Vector motion) {
      if (this.viewers.contains(viewer)) {
         this.destroy(viewer);
      }

      this.viewers.add(viewer);
      this.sendSpawnPackets(viewer, motion);
      if (this.glowColor != null) {
         this.applyGlowColorForViewer(viewer, this.glowColor);
      }

   }

   public void destroyForAll() {
      Iterator var1 = this.viewers.iterator();

      while(var1.hasNext()) {
         AttachmentViewer viewer = (AttachmentViewer)var1.next();
         this.sendDestroyPackets(viewer);
         if (this.viewersPendingGlowColorRemoval.contains(viewer)) {
            this.applyGlowColorForViewer(viewer, (ChatColor)null);
         }
      }

      this.viewers.clear();
      this.viewersPendingGlowColorRemoval = Collections.emptyList();
   }

   /** @deprecated */
   @Deprecated
   public void destroy(Player viewer) {
      this.destroy(this.asAttachmentViewer(viewer));
   }

   public void destroy(AttachmentViewer viewer) {
      this.viewers.remove(viewer);
      this.sendDestroyPackets(viewer);
      if (this.viewersPendingGlowColorRemoval.remove(viewer)) {
         this.applyGlowColorForViewer(viewer, (ChatColor)null);
      }

   }

   public void broadcast(CommonPacket packet) {
      this.viewers.forEach((v) -> {
         v.send(packet);
      });
   }

   public void broadcast(PacketHandle packet) {
      this.viewers.forEach((v) -> {
         v.send(packet);
      });
   }

   private AttachmentViewer asAttachmentViewer(Player player) {
      return this.manager != null ? this.manager.asAttachmentViewer(player) : AttachmentViewer.fallback(player);
   }
}
