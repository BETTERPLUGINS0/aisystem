package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.framework.Engine;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Nullable;

public interface IObjectPlacer {
   int getHighest(int x, int z, IrisData data);

   int getHighest(int x, int z, IrisData data, boolean ignoreFluid);

   void set(int x, int y, int z, BlockData d);

   BlockData get(int x, int y, int z);

   boolean isPreventingDecay();

   boolean isCarved(int x, int y, int z);

   boolean isSolid(int x, int y, int z);

   boolean isUnderwater(int x, int z);

   int getFluidHeight();

   boolean isDebugSmartBore();

   void setTile(int xx, int yy, int zz, TileData tile);

   <T> void setData(int xx, int yy, int zz, T data);

   @Nullable
   <T> T getData(int xx, int yy, int zz, Class<T> t);

   Engine getEngine();
}
