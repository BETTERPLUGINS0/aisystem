package com.volmit.iris.util.stream.interpolation;

import com.volmit.iris.util.stream.ProceduralStream;

public interface Interpolator<T> {
   default InterpolatorFactory<T> into() {
      return this instanceof ProceduralStream ? new InterpolatorFactory((ProceduralStream)this) : null;
   }
}
