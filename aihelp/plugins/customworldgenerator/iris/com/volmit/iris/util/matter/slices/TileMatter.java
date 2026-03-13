package com.volmit.iris.util.matter.slices;

import com.volmit.iris.engine.object.TileData;
import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.Sliced;
import com.volmit.iris.util.matter.TileWrapper;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import org.bukkit.Location;
import org.bukkit.World;

@Sliced
public class TileMatter extends RawMatter<TileWrapper> {
   public TileMatter() {
      this(1, 1, 1);
   }

   public TileMatter(int width, int height, int depth) {
      super(var1, var2, var3, TileWrapper.class);
      this.registerWriter(World.class, (var0, var1x, var2x, var3x, var4) -> {
         TileData.setTileState(var0.getBlockAt(new Location(var0, (double)var2x, (double)var3x, (double)var4)), var1x.getData());
      });
      this.registerReader(World.class, (var0, var1x, var2x, var3x) -> {
         return new TileWrapper(TileData.getTileState(var0.getBlockAt(new Location(var0, (double)var1x, (double)var2x, (double)var3x)), false));
      });
   }

   public Palette<TileWrapper> getGlobalPalette() {
      return null;
   }

   public void writeNode(TileWrapper b, DataOutputStream dos) {
      var1.getData().toBinary(var2);
   }

   public TileWrapper readNode(DataInputStream din) {
      return new TileWrapper(TileData.read(var1));
   }
}
