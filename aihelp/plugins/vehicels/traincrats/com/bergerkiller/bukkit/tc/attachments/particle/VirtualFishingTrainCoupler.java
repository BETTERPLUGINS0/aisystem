package com.bergerkiller.bukkit.tc.attachments.particle;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import org.bukkit.util.Vector;

public class VirtualFishingTrainCoupler extends VirtualTrainCoupler {
   private final VirtualFishingLine line = new VirtualFishingLine();
   private Vector pos1;
   private Vector pos2;

   public VirtualFishingTrainCoupler(AttachmentManager manager) {
      super(manager);
   }

   public void update(Matrix4x4 transform, double length) {
      this.pos2 = transform.toVector();
      Matrix4x4 tmp = transform.clone();
      tmp.translate(0.0D, 0.0D, length);
      this.pos1 = tmp.toVector();
   }

   public void updatePosition(Matrix4x4 transform) {
      throw new UnsupportedOperationException("Must specify a transform with length");
   }

   protected void sendSpawnPackets(AttachmentViewer viewer, Vector motion) {
      this.line.spawn(viewer, this.pos1, this.pos2);
   }

   protected void sendDestroyPackets(AttachmentViewer viewer) {
      this.line.destroy(viewer);
   }

   public void syncPosition(boolean absolute) {
      this.line.updateViewers(this.getViewers(), this.pos1, this.pos2);
   }

   public boolean containsEntityId(int entityId) {
      return false;
   }
}
