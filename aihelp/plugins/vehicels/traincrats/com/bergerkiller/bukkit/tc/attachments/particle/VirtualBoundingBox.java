package com.bergerkiller.bukkit.tc.attachments.particle;

import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.OrientedBoundingBox;
import com.bergerkiller.bukkit.tc.attachments.VirtualSpawnableObject;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;

public abstract class VirtualBoundingBox extends VirtualSpawnableObject {
   protected VirtualBoundingBox(AttachmentManager manager) {
      super(manager);
   }

   public abstract void update(OrientedBoundingBox var1);

   /** @deprecated */
   @Deprecated
   public final void updatePosition(Matrix4x4 transform) {
      throw new UnsupportedOperationException("Must specify a bounding box");
   }

   public static VirtualBoundingBox create(AttachmentManager manager) {
      return (VirtualBoundingBox)(CommonCapabilities.HAS_DISPLAY_ENTITY ? new VirtualHybridBoundingBox(manager) : new VirtualFishingBoundingBox(manager));
   }
}
