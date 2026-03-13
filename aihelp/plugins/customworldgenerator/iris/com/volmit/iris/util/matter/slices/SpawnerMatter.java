package com.volmit.iris.util.matter.slices;

import com.volmit.iris.engine.object.IrisSpawner;
import com.volmit.iris.util.matter.Sliced;

@Sliced
public class SpawnerMatter extends RegistryMatter<IrisSpawner> {
   public SpawnerMatter() {
      this(1, 1, 1);
   }

   public SpawnerMatter(int width, int height, int depth) {
      super(var1, var2, var3, IrisSpawner.class, new IrisSpawner());
   }
}
