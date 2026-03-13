package com.volmit.iris.engine.data.io;

public interface MaxDepthIO {
   default int decrementMaxDepth(int maxDepth) {
      if (maxDepth < 0) {
         throw new IllegalArgumentException("negative maximum depth is not allowed");
      } else if (maxDepth == 0) {
         throw new MaxDepthReachedException("reached maximum depth of NBT structure");
      } else {
         --maxDepth;
         return maxDepth;
      }
   }
}
