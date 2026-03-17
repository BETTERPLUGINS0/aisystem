package com.bergerkiller.bukkit.tc.attachments.particle;

import com.bergerkiller.bukkit.common.math.OrientedBoundingBox;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import org.bukkit.ChatColor;
import org.bukkit.util.Vector;

public class VirtualFishingBoundingBox extends VirtualBoundingBox {
   private final VirtualFishingBoundingBox.BBOXLine line_btm_nx = new VirtualFishingBoundingBox.BBOXLine((c) -> {
      return c.btm_nx_nz;
   }, (c) -> {
      return c.btm_nx_pz;
   });
   private final VirtualFishingBoundingBox.BBOXLine line_btm_px = new VirtualFishingBoundingBox.BBOXLine((c) -> {
      return c.btm_px_nz;
   }, (c) -> {
      return c.btm_nx_nz;
   });
   private final VirtualFishingBoundingBox.BBOXLine line_btm_nz = new VirtualFishingBoundingBox.BBOXLine((c) -> {
      return c.btm_px_pz;
   }, (c) -> {
      return c.btm_px_nz;
   });
   private final VirtualFishingBoundingBox.BBOXLine line_btm_pz = new VirtualFishingBoundingBox.BBOXLine((c) -> {
      return c.btm_nx_pz;
   }, (c) -> {
      return c.btm_px_pz;
   });
   private final VirtualFishingBoundingBox.BBOXLine line_top_nx = new VirtualFishingBoundingBox.BBOXLine((c) -> {
      return c.top_nx_nz;
   }, (c) -> {
      return c.top_nx_pz;
   });
   private final VirtualFishingBoundingBox.BBOXLine line_top_px = new VirtualFishingBoundingBox.BBOXLine((c) -> {
      return c.top_px_nz;
   }, (c) -> {
      return c.top_nx_nz;
   });
   private final VirtualFishingBoundingBox.BBOXLine line_top_nz = new VirtualFishingBoundingBox.BBOXLine((c) -> {
      return c.top_px_pz;
   }, (c) -> {
      return c.top_px_nz;
   });
   private final VirtualFishingBoundingBox.BBOXLine line_top_pz = new VirtualFishingBoundingBox.BBOXLine((c) -> {
      return c.top_nx_pz;
   }, (c) -> {
      return c.top_px_pz;
   });
   private final VirtualFishingBoundingBox.BBOXLine line_vrt_nxnz = new VirtualFishingBoundingBox.BBOXLine((c) -> {
      return c.top_nx_nz;
   }, (c) -> {
      return c.btm_nx_nz;
   });
   private final VirtualFishingBoundingBox.BBOXLine line_vrt_pxnz = new VirtualFishingBoundingBox.BBOXLine((c) -> {
      return c.top_px_nz;
   }, (c) -> {
      return c.btm_px_nz;
   });
   private final VirtualFishingBoundingBox.BBOXLine line_vrt_pxpz = new VirtualFishingBoundingBox.BBOXLine((c) -> {
      return c.top_px_pz;
   }, (c) -> {
      return c.btm_px_pz;
   });
   private final VirtualFishingBoundingBox.BBOXLine line_vrt_nxpz = new VirtualFishingBoundingBox.BBOXLine((c) -> {
      return c.top_nx_pz;
   }, (c) -> {
      return c.btm_nx_pz;
   });
   private final List<VirtualFishingBoundingBox.BBOXLine> lines;
   private VirtualFishingBoundingBox.ComputedCorners corners;
   private boolean linesSpawned;

   public VirtualFishingBoundingBox(AttachmentManager manager) {
      super(manager);
      this.lines = Arrays.asList(this.line_btm_nx, this.line_btm_nz, this.line_btm_px, this.line_btm_pz, this.line_top_nx, this.line_top_nz, this.line_top_px, this.line_top_pz, this.line_vrt_nxnz, this.line_vrt_pxnz, this.line_vrt_pxpz, this.line_vrt_nxpz);
      this.corners = null;
      this.linesSpawned = false;
   }

   public boolean containsEntityId(int entityId) {
      return false;
   }

   protected void sendSpawnPackets(AttachmentViewer viewer, Vector motion) {
      ArrayList<UUID> uuids = new ArrayList(24);
      if (this.linesSpawned) {
         this.lines.forEach((bboxline) -> {
            bboxline.spawn(viewer, this.corners, uuids);
         });
      } else {
         this.lines.forEach((bboxline) -> {
            bboxline.spawnWithoutLine(viewer, this.corners, uuids);
         });
      }

      viewer.sendDisableCollision((Iterable)uuids);
   }

   protected void sendDestroyPackets(AttachmentViewer viewer) {
      this.lines.forEach((line) -> {
         line.destroy(viewer);
      });
   }

   protected void applyGlowing(ChatColor color) {
      if (this.linesSpawned != (color != null)) {
         this.linesSpawned = !this.linesSpawned;
         if (this.hasViewers()) {
            if (this.linesSpawned) {
               this.forAllViewers((v) -> {
                  this.lines.forEach((line) -> {
                     line.spawnLine(v, this.corners);
                  });
               });
            } else {
               this.forAllViewers((v) -> {
                  this.lines.forEach((line) -> {
                     line.destroyLine(v);
                  });
               });
            }
         }
      }

   }

   public void update(OrientedBoundingBox boundingBox) {
      this.corners = new VirtualFishingBoundingBox.ComputedCorners(boundingBox);
   }

   public void syncPosition(boolean absolute) {
      this.lines.forEach((bboxline) -> {
         bboxline.updateViewers(this.getViewers(), this.corners);
      });
   }

   private static class BBOXLine extends VirtualFishingLine {
      private final Function<VirtualFishingBoundingBox.ComputedCorners, Vector> pos1func;
      private final Function<VirtualFishingBoundingBox.ComputedCorners, Vector> pos2func;

      public BBOXLine(Function<VirtualFishingBoundingBox.ComputedCorners, Vector> pos1func, Function<VirtualFishingBoundingBox.ComputedCorners, Vector> pos2func) {
         this.pos1func = pos1func;
         this.pos2func = pos2func;
      }

      public void spawn(AttachmentViewer viewer, VirtualFishingBoundingBox.ComputedCorners corners, List<UUID> uuids) {
         Vector p1 = (Vector)this.pos1func.apply(corners);
         Vector p2 = (Vector)this.pos2func.apply(corners);
         this.spawnWithoutLineCollectUUIDs(viewer, p1, p2, uuids);
         this.spawnLine(viewer, p1, p2);
      }

      public void spawnWithoutLine(AttachmentViewer viewer, VirtualFishingBoundingBox.ComputedCorners corners, List<UUID> uuids) {
         this.spawnWithoutLineCollectUUIDs(viewer, (Vector)this.pos1func.apply(corners), (Vector)this.pos2func.apply(corners), uuids);
      }

      public void spawnLine(AttachmentViewer viewer, VirtualFishingBoundingBox.ComputedCorners corners) {
         this.spawnLine(viewer, (Vector)this.pos1func.apply(corners), (Vector)this.pos2func.apply(corners));
      }

      public void updateViewers(Iterable<AttachmentViewer> viewers, VirtualFishingBoundingBox.ComputedCorners corners) {
         this.updateViewers(viewers, (Vector)this.pos1func.apply(corners), (Vector)this.pos2func.apply(corners));
      }
   }

   private static class ComputedCorners {
      public final Vector btm_nx_nz;
      public final Vector btm_px_nz;
      public final Vector btm_px_pz;
      public final Vector btm_nx_pz;
      public final Vector top_nx_nz;
      public final Vector top_px_nz;
      public final Vector top_px_pz;
      public final Vector top_nx_pz;

      public ComputedCorners(OrientedBoundingBox boundingBox) {
         Vector hsize = boundingBox.getSize().clone().multiply(0.5D);
         this.btm_nx_nz = new Vector(-hsize.getX(), -hsize.getY(), -hsize.getZ());
         this.btm_px_nz = new Vector(hsize.getX(), -hsize.getY(), -hsize.getZ());
         this.btm_px_pz = new Vector(hsize.getX(), -hsize.getY(), hsize.getZ());
         this.btm_nx_pz = new Vector(-hsize.getX(), -hsize.getY(), hsize.getZ());
         this.top_nx_nz = new Vector(-hsize.getX(), hsize.getY(), -hsize.getZ());
         this.top_px_nz = new Vector(hsize.getX(), hsize.getY(), -hsize.getZ());
         this.top_px_pz = new Vector(hsize.getX(), hsize.getY(), hsize.getZ());
         this.top_nx_pz = new Vector(-hsize.getX(), hsize.getY(), hsize.getZ());
         Quaternion orientation = boundingBox.getOrientation();
         orientation.transformPoint(this.btm_nx_nz);
         orientation.transformPoint(this.btm_px_nz);
         orientation.transformPoint(this.btm_px_pz);
         orientation.transformPoint(this.btm_nx_pz);
         orientation.transformPoint(this.top_nx_nz);
         orientation.transformPoint(this.top_px_nz);
         orientation.transformPoint(this.top_px_pz);
         orientation.transformPoint(this.top_nx_pz);
         Vector offset = boundingBox.getPosition();
         this.btm_nx_nz.add(offset);
         this.btm_px_nz.add(offset);
         this.btm_px_pz.add(offset);
         this.btm_nx_pz.add(offset);
         this.top_nx_nz.add(offset);
         this.top_px_nz.add(offset);
         this.top_px_pz.add(offset);
         this.top_nx_pz.add(offset);
      }
   }
}
