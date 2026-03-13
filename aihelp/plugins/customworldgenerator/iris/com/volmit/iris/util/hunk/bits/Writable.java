package com.volmit.iris.util.hunk.bits;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Writable<T> {
   T readNodeData(DataInputStream din) throws IOException;

   void writeNodeData(DataOutputStream dos, T t) throws IOException;
}
