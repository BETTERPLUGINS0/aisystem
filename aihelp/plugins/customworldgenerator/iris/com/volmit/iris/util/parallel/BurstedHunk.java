package com.volmit.iris.util.parallel;

import com.volmit.iris.util.hunk.Hunk;

public interface BurstedHunk<T> extends Hunk<T> {
   int getOffsetX();

   int getOffsetY();

   int getOffsetZ();
}
