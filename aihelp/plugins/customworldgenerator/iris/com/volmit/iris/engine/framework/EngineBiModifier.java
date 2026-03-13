package com.volmit.iris.engine.framework;

import com.volmit.iris.util.hunk.Hunk;

public interface EngineBiModifier<A, B> extends EngineComponent {
   void modify(int x, int z, Hunk<A> a, Hunk<B> b);
}
