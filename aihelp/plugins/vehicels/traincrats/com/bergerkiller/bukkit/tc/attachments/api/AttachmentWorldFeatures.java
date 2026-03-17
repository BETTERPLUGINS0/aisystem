package com.bergerkiller.bukkit.tc.attachments.api;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import java.util.function.BiFunction;
import org.bukkit.World;

public class AttachmentWorldFeatures {
   public final boolean MINECART_IMPROVEMENTS;
   private static final BiFunction<World, String, Boolean> hasFeatureMethod = Common.hasCapability("Common:WorldUtil:HasFeatureFlag") ? WorldUtil::hasFeatureFlag : (w, n) -> {
      return false;
   };

   public static AttachmentWorldFeatures of(World world) {
      return new AttachmentWorldFeatures(world);
   }

   private AttachmentWorldFeatures(World world) {
      this.MINECART_IMPROVEMENTS = (Boolean)hasFeatureMethod.apply(world, "minecraft:minecart_improvements");
   }

   public static final class Tracker {
      private World world = null;
      private AttachmentWorldFeatures last = null;

      public AttachmentWorldFeatures get(World world) {
         if (this.world == world) {
            return this.last;
         } else {
            this.world = world;
            return this.last = AttachmentWorldFeatures.of(world);
         }
      }
   }
}
