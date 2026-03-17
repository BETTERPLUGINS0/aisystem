package advancedplugins.pm2.cv.models.api.model.nrpc.nms;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

public interface FakeBlockDisplayEntity extends FakeDisplayEntity {
   void setBlock(BlockData var1);

   default void setBlock(@NotNull Material material) {
      if (!material.isBlock()) {
         throw new IllegalArgumentException("material must be a block");
      } else {
         this.setBlock(material.createBlockData());
      }
   }
}
