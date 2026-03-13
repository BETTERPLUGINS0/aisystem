package com.volmit.iris.engine.framework;

import com.volmit.iris.util.hunk.Hunk;

public abstract class EngineAssignedBiModifier<A, B> extends EngineAssignedComponent implements EngineBiModifier<A, B> {
   public EngineAssignedBiModifier(Engine engine, String name) {
      super(var1, var2);
   }

   public abstract void onModify(int x, int z, Hunk<A> a, Hunk<B> b);

   public void modify(int x, int z, Hunk<A> a, Hunk<B> b) {
      this.onModify(var1, var2, var3, var4);
   }
}
