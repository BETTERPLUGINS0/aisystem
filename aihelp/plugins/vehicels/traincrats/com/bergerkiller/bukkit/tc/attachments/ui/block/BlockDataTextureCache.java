package com.bergerkiller.bukkit.tc.attachments.ui.block;

import com.bergerkiller.bukkit.common.bases.IntVector2;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.util.Model;
import com.bergerkiller.bukkit.common.math.Vector3;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.TCConfig;
import java.util.HashMap;
import java.util.Map;

public class BlockDataTextureCache {
   private static final Map<IntVector2, BlockDataTextureCache> textureCaches = new HashMap();
   private final Map<BlockData, MapTexture> blockTextures = new HashMap();
   private final int width;
   private final int height;
   private final float scale;
   private final int off_x;
   private final int off_y;

   private BlockDataTextureCache(IntVector2 key) {
      this.width = key.x;
      this.height = key.z;
      this.scale = (float)Math.max(this.width, this.height) / 25.7F;
      this.off_x = (int)(this.scale * 24.0F);
      this.off_y = (int)(this.scale * 20.0F);
   }

   public MapTexture get(BlockData data) {
      return (MapTexture)this.blockTextures.computeIfAbsent(data, (d) -> {
         MapTexture texture = MapTexture.createEmpty(this.width, this.height);
         Model model = TCConfig.resourcePack.getBlockModel(d);
         texture.setLightOptions(0.0F, 1.0F, new Vector3(-1.0D, 1.0D, -1.0D));
         texture.drawModel(model, this.scale, this.off_x, this.off_y, 225.0F, -60.0F);
         return texture;
      });
   }

   public static BlockDataTextureCache get(int width, int height) {
      return (BlockDataTextureCache)textureCaches.computeIfAbsent(new IntVector2(width, height), BlockDataTextureCache::new);
   }
}
