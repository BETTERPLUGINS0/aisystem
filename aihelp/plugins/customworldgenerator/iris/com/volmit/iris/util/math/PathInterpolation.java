package com.volmit.iris.util.math;

import java.util.List;
import org.bukkit.util.Vector;

public interface PathInterpolation {
   void setNodes(List<INode> nodes);

   Vector getPosition(double position);

   Vector get1stDerivative(double position);

   double arcLength(double positionA, double positionB);

   int getSegment(double position);
}
