package advancedplugins.pm2.cv.models.api.utils.resourcepack;

public enum ResourcePackFeature {
   COLORABLE_ITEM,
   DATA_DRIVEN,
   COMPOSITE_BYPASS;

   private static ResourcePackFeature[] $values() {
      return new ResourcePackFeature[]{COLORABLE_ITEM, DATA_DRIVEN, COMPOSITE_BYPASS};
   }

   // $FF: synthetic method
   private static ResourcePackFeature[] $values$() {
      return new ResourcePackFeature[]{COLORABLE_ITEM, DATA_DRIVEN, COMPOSITE_BYPASS};
   }
}
