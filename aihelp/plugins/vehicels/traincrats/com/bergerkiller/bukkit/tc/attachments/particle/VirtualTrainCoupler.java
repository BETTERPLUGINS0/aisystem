package com.bergerkiller.bukkit.tc.attachments.particle;

import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.tc.attachments.VirtualSpawnableObject;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;

public abstract class VirtualTrainCoupler extends VirtualSpawnableObject {
   protected VirtualTrainCoupler(AttachmentManager manager) {
      super(manager);
   }

   public abstract void update(Matrix4x4 var1, double var2);

   public static VirtualTrainCoupler create(AttachmentManager manager) {
      return (VirtualTrainCoupler)(CommonCapabilities.HAS_DISPLAY_ENTITY ? new VirtualDisplayTrainCoupler(manager) : new VirtualFishingTrainCoupler(manager));
   }
}
