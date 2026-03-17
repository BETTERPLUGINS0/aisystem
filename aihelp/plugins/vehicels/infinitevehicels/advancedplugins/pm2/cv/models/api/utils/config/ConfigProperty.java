package advancedplugins.pm2.cv.models.api.utils.config;

import java.util.List;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

public enum ConfigProperty implements Property {
   ENGINE("Engine"),
   GENERATOR("Model-Generator"),
   OPTIMIZATION("Network-Optimization"),
   DEFAULT_NAMES(ENGINE, "Default-Animations"),
   SCRIPT_WARNING(ENGINE, "Print-Script-Warnings", false),
   USE_STATE_MACHINE(ENGINE, "Use-State-Machine", false),
   /** @deprecated */
   @Deprecated
   ENGINE_THREADS(ENGINE, "Engine-Threads", 4),
   MAX_ENGINE_THREADS(ENGINE, "Max-Engine-Threads", 10),
   SYNC_CLIENT_TICK_END(ENGINE, "Sync-Client-Tick-End", true),
   LATE_REGISTER(GENERATOR, "Register-Post-Server", true),
   LATE_ASSETS(GENERATOR, "Assets-Post-Server", true),
   LATE_ZIPPING(GENERATOR, "Compile-Post-Server", true),
   ERROR(GENERATOR, "Enable-Error", true),
   DEBUG_LEVEL(GENERATOR, "Debug-Level", 1),
   NAMESPACE(GENERATOR, "Namespace", "infinitemodel"),
   ZIP(GENERATOR, "Create-Zip", true),
   ATLAS(GENERATOR, "Create-Atlas", true),
   SHADER(GENERATOR, "Create-Shader", false),
   MCMETA(GENERATOR, "Create-MC-META", true),
   /** @deprecated */
   @Deprecated
   ITEM_MODEL(GENERATOR, "Item-Model", "FILLED_MAP"),
   ITEM_MODELS(GENERATOR, "Item-Models", List.of("FILLED_MAP")),
   BUNDLE_EVERYTHING(OPTIMIZATION, "Bundle-Everything", false),
   BUNDLE_SIZE(OPTIMIZATION, "Bundle-Size", 512),
   CULL_INTERVAL(OPTIMIZATION, "Cull-Interval", 4),
   CULLING_THREADS(OPTIMIZATION, "Culling-Threads", 4),
   MAX_CULLING_THREADS(OPTIMIZATION, "Max-Culling-Threads", 10),
   VERTICAL_CULL(OPTIMIZATION, "Vertical-Render-Distance"),
   VERTICAL_CULL_ENABLE(VERTICAL_CULL, "Enabled", true),
   VERTICAL_CULL_DISTANCE(VERTICAL_CULL, "Vertical-Render-Distance", 32),
   VERTICAL_CULL_TYPE(VERTICAL_CULL, "Cull-Type", "CULLED"),
   BACKWARDS_CULL(OPTIMIZATION, "Skip-Models-Behind-Viewer"),
   BACKWARDS_CULL_ENABLED(BACKWARDS_CULL, "Enabled", true),
   BACKWARDS_CULL_ANGLE(BACKWARDS_CULL, "View-Angle", 180),
   BACKWARDS_CULL_IGNORE_RADIUS(BACKWARDS_CULL, "Force-Render-Radius", 5),
   BACKWARDS_CULL_TYPE(BACKWARDS_CULL, "Cull-Type", "MOVEMENT_ONLY"),
   BLOCK_CULL(OPTIMIZATION, "Skip-Blocked-Models"),
   BLOCK_CULL_ENABLE(BLOCK_CULL, "Enabled", true),
   BLOCK_CULL_IGNORE_RADIUS(BLOCK_CULL, "Force-Render-Radius", 5),
   BLOCK_CULL_TYPE(BLOCK_CULL, "Cull-Type", "CULLED"),
   BLOCK_CULL_IGNORE_SIZE(BLOCK_CULL, "Force-Render-Size"),
   BLOCK_CULL_USE_PAPER_CLIP(BLOCK_CULL, "Use-Paper-Clip-Method", false),
   BLOCK_CULL_IGNORE_SIZE_WIDTH(BLOCK_CULL_IGNORE_SIZE, "Width", 32),
   BLOCK_CULL_IGNORE_SIZE_HEIGHT(BLOCK_CULL_IGNORE_SIZE, "Height", 32),
   ANIMATION_LOD(OPTIMIZATION, "Reduce-Update-When-Far"),
   ANIMATION_LOD_ENABLED(ANIMATION_LOD, "Enabled", true),
   ANIMATION_LOD_DEFAULT(ANIMATION_LOD, "Active-By-Default", true),
   ANIMATION_LOD_FALLOFF_LENGTH(ANIMATION_LOD, "Falloff-Length", 8),
   ANIMATION_LOD_FALLOFF_MULTIPLIER(ANIMATION_LOD, "Falloff-Multiplier", 0.85D),
   ANIMATION_LOD_IGNORE_DISTANCE(ANIMATION_LOD, "Full-Update-Distance", 16),
   ANIMATION_LOD_IGNORE_MULTIPLIER(ANIMATION_LOD, "Full-Update-Distance-Multiplier-By-Size", 1),
   ANIMATION_LOD_MAX_RATE_MULTIPLIER(ANIMATION_LOD, "Max-Rate-Multiplier", 1),
   ANIMATION_LOD_MIN_RATE_MULTIPLIER(ANIMATION_LOD, "Min-Rate-Multiplier", 0.05D);

   private final String path;
   private final Object def;

   private ConfigProperty(String param3) {
      this((String)var3, (Object)null);
   }

   private ConfigProperty(ConfigProperty param3, String param4) {
      this((String)(var3.getPath() + "." + var4), (Object)null);
   }

   private ConfigProperty(String param3, @Nullable Object param4) {
      this.path = var3;
      this.def = var4;
   }

   private ConfigProperty(ConfigProperty param3, String param4, @Nullable Object param5) {
      this(var3.getPath() + "." + var4, var5);
   }

   @Generated
   public String getPath() {
      return this.path;
   }

   @Generated
   public Object getDef() {
      return this.def;
   }

   // $FF: synthetic method
   private static ConfigProperty[] $values() {
      return new ConfigProperty[]{ENGINE, GENERATOR, OPTIMIZATION, DEFAULT_NAMES, SCRIPT_WARNING, USE_STATE_MACHINE, ENGINE_THREADS, MAX_ENGINE_THREADS, SYNC_CLIENT_TICK_END, LATE_REGISTER, LATE_ASSETS, LATE_ZIPPING, ERROR, DEBUG_LEVEL, NAMESPACE, ZIP, ATLAS, SHADER, MCMETA, ITEM_MODEL, ITEM_MODELS, BUNDLE_EVERYTHING, BUNDLE_SIZE, CULL_INTERVAL, CULLING_THREADS, MAX_CULLING_THREADS, VERTICAL_CULL, VERTICAL_CULL_ENABLE, VERTICAL_CULL_DISTANCE, VERTICAL_CULL_TYPE, BACKWARDS_CULL, BACKWARDS_CULL_ENABLED, BACKWARDS_CULL_ANGLE, BACKWARDS_CULL_IGNORE_RADIUS, BACKWARDS_CULL_TYPE, BLOCK_CULL, BLOCK_CULL_ENABLE, BLOCK_CULL_IGNORE_RADIUS, BLOCK_CULL_TYPE, BLOCK_CULL_IGNORE_SIZE, BLOCK_CULL_USE_PAPER_CLIP, BLOCK_CULL_IGNORE_SIZE_WIDTH, BLOCK_CULL_IGNORE_SIZE_HEIGHT, ANIMATION_LOD, ANIMATION_LOD_ENABLED, ANIMATION_LOD_DEFAULT, ANIMATION_LOD_FALLOFF_LENGTH, ANIMATION_LOD_FALLOFF_MULTIPLIER, ANIMATION_LOD_IGNORE_DISTANCE, ANIMATION_LOD_IGNORE_MULTIPLIER, ANIMATION_LOD_MAX_RATE_MULTIPLIER, ANIMATION_LOD_MIN_RATE_MULTIPLIER};
   }
}
