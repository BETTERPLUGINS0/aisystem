package com.bergerkiller.bukkit.tc.controller.components;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.bases.mutable.LocationAbstract;
import com.bergerkiller.bukkit.common.bases.mutable.VectorAbstract;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.utils.BlockIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class RailPath {
   private static final double SMALL_ADVANCE_MIN_MOT = 1.0E-6D;
   public static final RailPath EMPTY = new RailPath(new RailPath.Point[0]);
   private final RailPath.Point[] points;
   private final RailPath.Segment[] segments;
   private final double totalDistance;

   private RailPath(RailPath.Point[] points) {
      this.points = points;
      if (points.length < 2) {
         this.segments = new RailPath.Segment[0];
         this.totalDistance = 0.0D;
      } else {
         double distance = 0.0D;
         this.segments = new RailPath.Segment[points.length - 1];

         int i;
         for(i = 0; i < this.segments.length; ++i) {
            this.segments[i] = new RailPath.Segment(points[i], points[i + 1]);
            distance += this.segments[i].l;
         }

         for(i = 0; i < this.segments.length - 1; ++i) {
            this.segments[i].next = this.segments[i + 1];
            this.segments[i + 1].prev = this.segments[i];
         }

         for(i = 0; i < this.segments.length; ++i) {
            this.segments[i].postinit();
         }

         this.totalDistance = distance;
      }

   }

   public double getTotalDistance() {
      return this.totalDistance;
   }

   public RailPath.Point[] getPoints() {
      return this.points;
   }

   public RailPath.Segment[] getSegments() {
      return this.segments;
   }

   public RailPath.Position getStartPosition() {
      RailPath.Segment firstSegment = this.segments[0];
      RailPath.Position p = new RailPath.Position();
      p.relative = true;
      p.posX = firstSegment.p0.x;
      p.posY = firstSegment.p0.y;
      p.posZ = firstSegment.p0.z;
      p.wheelSegment = firstSegment;
      p.wheelTheta = 0.0D;
      p.motX = -firstSegment.mot.getX();
      p.motY = -firstSegment.mot.getY();
      p.motZ = -firstSegment.mot.getZ();
      return p;
   }

   public RailPath.Position getEndPosition() {
      RailPath.Segment lastSegment = this.segments[this.segments.length - 1];
      RailPath.Position p = new RailPath.Position();
      p.relative = true;
      p.posX = lastSegment.p1.x;
      p.posY = lastSegment.p1.y;
      p.posZ = lastSegment.p1.z;
      p.wheelSegment = lastSegment;
      p.wheelTheta = 1.0D;
      p.motX = lastSegment.mot.getX();
      p.motY = lastSegment.mot.getY();
      p.motZ = lastSegment.mot.getZ();
      return p;
   }

   public RailPath.Position getEndOfPath(Block railBlock, RailPath.Position position) {
      RailPath.Segment s = position.wheelSegment;
      if (s == null) {
         throw new IllegalArgumentException("Input position was never moved or snapped to a path!");
      } else {
         RailPath.Position end = position.motDot(s.p_offset) > 0.0D ? this.getEndPosition() : this.getStartPosition();
         end.makeAbsolute(railBlock);
         return end;
      }
   }

   public boolean isEmpty() {
      return this.segments.length == 0;
   }

   public RailPath.ProximityInfo getProximityInfo(Vector position, Vector motionVector) {
      RailPath.ProximityInfo info = new RailPath.ProximityInfo();

      for(int i = 0; i < this.segments.length; ++i) {
         RailPath.Segment tmpSegment = this.segments[i];
         if (!tmpSegment.isZeroLength()) {
            double tmpTheta = tmpSegment.calcTheta(position);
            double tmpDistSquared = tmpSegment.calcDistanceSquared(position, tmpTheta);
            if (tmpDistSquared < info.distanceSquared) {
               info.distanceSquared = tmpDistSquared;
               if (tmpTheta < tmpSegment.end_theta_threshold && i == 0) {
                  info.canMoveForward = tmpSegment.mot.dot(motionVector) >= 0.0D;
               } else if (1.0D - tmpTheta < tmpSegment.end_theta_threshold && i == this.segments.length - 1) {
                  info.canMoveForward = tmpSegment.mot.dot(motionVector) <= 0.0D;
               } else {
                  info.canMoveForward = true;
               }
            }
         }
      }

      return info;
   }

   public double distanceSquared(Vector position) {
      double closestDistance = Double.MAX_VALUE;

      for(int i = 0; i < this.segments.length; ++i) {
         RailPath.Segment tmpSegment = this.segments[i];
         if (!tmpSegment.isZeroLength()) {
            double tmpTheta = tmpSegment.calcTheta(position);
            double tmpDistSquared = tmpSegment.calcDistanceSquared(position, tmpTheta);
            if (tmpDistSquared < closestDistance) {
               closestDistance = tmpDistSquared;
            }
         }
      }

      return closestDistance;
   }

   public RailPath.Segment findSegment(Vector position) {
      if (this.segments.length == 0) {
         return null;
      } else if (this.segments.length == 1) {
         return this.segments[0];
      } else {
         RailPath.Segment s = null;
         double closestDistance = Double.MAX_VALUE;

         for(int i = 0; i < this.segments.length; ++i) {
            RailPath.Segment tmpSegment = this.segments[i];
            if (!tmpSegment.isZeroLength()) {
               double tmpTheta = tmpSegment.calcTheta(position);
               double tmpDistSquared = tmpSegment.calcDistanceSquared(position, tmpTheta);
               if (tmpDistSquared < closestDistance) {
                  closestDistance = tmpDistSquared;
                  s = tmpSegment;
               }
            }
         }

         return s;
      }
   }

   public RailPath.Segment findSegment(Vector position, Block rails) {
      if (this.segments.length == 0) {
         return null;
      } else if (this.segments.length == 1) {
         return this.segments[0];
      } else {
         Vector relPos = position.clone();
         relPos.setX(relPos.getX() - (double)rails.getX());
         relPos.setY(relPos.getY() - (double)rails.getY());
         relPos.setZ(relPos.getZ() - (double)rails.getZ());
         return this.findSegment(relPos);
      }
   }

   public void snap(RailPath.Position position, Block railsBlock) {
      this.move(position, railsBlock, 0.0D);
   }

   public double move(RailState state, double distance) {
      return this.move(state.position(), state.railBlock(), distance);
   }

   public double move(RailPath.Position position, Block railBlock, double distance) {
      position.assertAbsolute();
      position.makeRelative(railBlock);
      double result = this.moveRelative(position, distance);
      position.makeAbsolute(railBlock);
      return result;
   }

   /** @deprecated */
   @Deprecated
   public double move(Vector position, Vector direction, Block railsBlock, double distance) {
      position.setX(position.getX() - (double)railsBlock.getX());
      position.setY(position.getY() - (double)railsBlock.getY());
      position.setZ(position.getZ() - (double)railsBlock.getZ());
      double result = this.moveRelative(position, direction, distance);
      position.setX(position.getX() + (double)railsBlock.getX());
      position.setY(position.getY() + (double)railsBlock.getY());
      position.setZ(position.getZ() + (double)railsBlock.getZ());
      return result;
   }

   /** @deprecated */
   @Deprecated
   public double moveRelative(Vector position, Vector direction, double distance) {
      RailPath.Position tmp = new RailPath.Position();
      tmp.relative = true;
      tmp.posX = position.getX();
      tmp.posY = position.getY();
      tmp.posZ = position.getZ();
      tmp.motX = direction.getX();
      tmp.motY = direction.getY();
      tmp.motZ = direction.getZ();
      double result = this.moveRelative(tmp, distance);
      position.setX(tmp.posX);
      position.setY(tmp.posY);
      position.setZ(tmp.posZ);
      direction.setX(tmp.motX);
      direction.setY(tmp.motY);
      direction.setZ(tmp.motZ);
      return result;
   }

   public double moveRelative(RailPath.Position position, double distance) {
      position.assertRelative();
      if (this.segments.length == 0) {
         return 0.0D;
      } else {
         int segmentIndex;
         double remainingDistance;
         if (this.segments.length == 1) {
            RailPath.Segment s = this.segments[0];
            if (s.isZeroLength()) {
               s.calcPosition(position, 0.0D);
               return 0.0D;
            } else {
               double theta = s.calcTheta(position);
               s.calcPosition(position, theta);
               segmentIndex = s.calcDirection(position);
               if (segmentIndex == 1) {
                  if (theta >= 1.0D) {
                     return 0.0D;
                  } else {
                     if (theta < 0.0D) {
                        theta = 0.0D;
                     }

                     remainingDistance = s.l * (1.0D - theta);
                     if (distance >= remainingDistance) {
                        s.calcPosition(position, 1.0D);
                        return remainingDistance;
                     } else {
                        s.calcPosition(position, theta + distance * s.linv);
                        return distance;
                     }
                  }
               } else if (theta <= 0.0D) {
                  return 0.0D;
               } else {
                  if (theta > 1.0D) {
                     theta = 1.0D;
                  }

                  remainingDistance = s.l * theta;
                  if (distance >= remainingDistance) {
                     s.calcPosition(position, 0.0D);
                     return remainingDistance;
                  } else {
                     s.calcPosition(position, theta - distance * s.linv);
                     return distance;
                  }
               }
            }
         } else {
            double theta = 0.0D;
            RailPath.Segment s = null;
            segmentIndex = -1;
            remainingDistance = Double.MAX_VALUE;

            for(int i = 0; i < this.segments.length; ++i) {
               RailPath.Segment tmpSegment = this.segments[i];
               if (!tmpSegment.isZeroLength()) {
                  double tmpTheta = tmpSegment.calcTheta(position);
                  double tmpDistSquared = tmpSegment.calcDistanceSquared(position, tmpTheta);
                  if (tmpDistSquared < remainingDistance) {
                     remainingDistance = tmpDistSquared;
                     theta = tmpTheta;
                     s = tmpSegment;
                     segmentIndex = i;
                  }
               }
            }

            if (s == null) {
               return 0.0D;
            } else if (distance <= 0.0D) {
               s.calcPosition(position, theta);
               s.calcDirection(position);
               return 0.0D;
            } else {
               int order = s.calcDirection(position);
               double moved = 0.0D;

               while(distance > 0.0D) {
                  s.calcPosition(position, theta);
                  if (!s.isZeroLength()) {
                     double remainingDistance;
                     if (order == 1) {
                        if (theta < 0.0D) {
                           theta = 0.0D;
                        }

                        if (theta < 1.0D) {
                           remainingDistance = s.l * (1.0D - theta);
                           if (!(distance >= remainingDistance)) {
                              s.calcPosition(position, theta + distance * s.linv);
                              moved += distance;
                              distance = 0.0D;
                              break;
                           }

                           s.calcPosition(position, 1.0D);
                           moved += remainingDistance;
                           distance -= remainingDistance;
                        }
                     } else {
                        if (theta > 1.0D) {
                           theta = 1.0D;
                        }

                        if (theta > 0.0D) {
                           remainingDistance = s.l * theta;
                           if (!(distance >= remainingDistance)) {
                              s.calcPosition(position, theta - distance * s.linv);
                              moved += distance;
                              distance = 0.0D;
                              break;
                           }

                           s.calcPosition(position, 0.0D);
                           moved += remainingDistance;
                           distance -= remainingDistance;
                        }
                     }
                  }

                  segmentIndex += order;
                  if (segmentIndex < 0 || segmentIndex >= this.segments.length) {
                     break;
                  }

                  s = this.segments[segmentIndex];
                  theta = s.calcTheta(position);
                  Vector mot = s.mot;
                  if (order > 0) {
                     position.motX = mot.getX();
                     position.motY = mot.getY();
                     position.motZ = mot.getZ();
                  } else {
                     position.motX = -mot.getX();
                     position.motY = -mot.getY();
                     position.motZ = -mot.getZ();
                  }
               }

               return moved;
            }
         }
      }
   }

   public void forAllBlocks(IntVector3 railsBlock, Consumer<IntVector3> blockConsumer) {
      BlockIterator iter = null;
      IntVector3 last = null;
      RailPath.Segment[] var5 = this.segments;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         RailPath.Segment segment = var5[var7];
         IntVector3 firstBlock = segment.containedInsideBlock;
         if (firstBlock != null) {
            firstBlock = firstBlock.add(railsBlock);
            if (last == null || !last.isSame(firstBlock)) {
               last = firstBlock;
               blockConsumer.accept(firstBlock);
            }
         } else {
            if (iter == null) {
               iter = new BlockIterator(railsBlock, segment);
            } else {
               iter.reset(railsBlock, segment);
            }

            if (iter.next()) {
               firstBlock = iter.block();
               if (last == null || !last.isSame(firstBlock)) {
                  last = firstBlock;
                  blockConsumer.accept(firstBlock);
               }

               if (iter.next()) {
                  do {
                     last = iter.block();
                     blockConsumer.accept(last);
                  } while(iter.next());
               } else {
                  segment.containedInsideBlock = firstBlock.subtract(railsBlock);
               }
            }
         }
      }

   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof RailPath)) {
         return false;
      } else {
         RailPath other = (RailPath)o;
         if (other.points.length != this.points.length) {
            return false;
         } else {
            for(int i = 0; i < this.points.length; ++i) {
               RailPath.Point p1 = this.points[i];
               RailPath.Point p2 = other.points[i];
               if (p1.x != p2.x || p1.y != p2.y || p1.z != p2.z) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public String toString() {
      StringBuilder str = new StringBuilder();
      str.append("RailPath[npoints=").append(this.points.length + "]:");
      RailPath.Point[] var2 = this.points;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         RailPath.Point p = var2[var4];
         str.append("\n  - ").append(p.toString());
      }

      return str.toString();
   }

   public String stringifyEndPoints(Block railBlock) {
      if (this.isEmpty()) {
         return "RailPath{EMPTY}";
      } else {
         RailPath.Position a = this.getStartPosition();
         RailPath.Position b = this.getEndPosition();
         a.makeAbsolute(railBlock);
         b.makeAbsolute(railBlock);
         return "RailPath{[ " + a.posX + " / " + a.posY + " / " + a.posZ + " ] => [ " + b.posX + " / " + b.posY + " / " + b.posZ + " ]}";
      }
   }

   public static RailPath create(Vector... pointVectors) {
      RailPath.Point[] points = new RailPath.Point[pointVectors.length];

      for(int i = 0; i < pointVectors.length; ++i) {
         Vector v = pointVectors[i];
         points[i] = new RailPath.Point(v.getX(), v.getY(), v.getZ());
      }

      return create(points);
   }

   public static RailPath create(RailPath.Point... points) {
      if (points.length < 2) {
         throw new IllegalArgumentException("Paths must have at least 2 points");
      } else {
         return new RailPath(points);
      }
   }

   public static RailPath offset(RailPath original_path, Vector position_offset) {
      if (original_path.isEmpty()) {
         return EMPTY;
      } else {
         RailPath.Point[] originalPoints = original_path.getPoints();
         RailPath.Point[] points_offset = new RailPath.Point[originalPoints.length];

         for(int i = 0; i < originalPoints.length; ++i) {
            RailPath.Point original = originalPoints[i];
            points_offset[i] = new RailPath.Point(original.x + position_offset.getX(), original.y + position_offset.getY(), original.z + position_offset.getZ(), original.up_x, original.up_y, original.up_z);
         }

         return create(points_offset);
      }
   }

   public static class Point {
      public final double x;
      public final double y;
      public final double z;
      public final double up_x;
      public final double up_y;
      public final double up_z;

      public Point(Vector v) {
         this(v.getX(), v.getY(), v.getZ());
      }

      public Point(Vector v, Vector up) {
         this(v.getX(), v.getY(), v.getZ(), up.getX(), up.getY(), up.getZ());
      }

      public Point(Vector v, double up_x, double up_y, double up_z) {
         this(v.getX(), v.getY(), v.getZ(), up_x, up_y, up_z);
      }

      public Point(Vector v, BlockFace face) {
         this(v.getX(), v.getY(), v.getZ(), face);
      }

      public Point(double x, double y, double z) {
         this(x, y, z, BlockFace.UP);
      }

      public Point(double x, double y, double z, BlockFace face) {
         this(x, y, z, (double)face.getModX(), (double)face.getModY(), (double)face.getModZ());
      }

      public Point(double x, double y, double z, double up_x, double up_y, double up_z) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.up_x = up_x;
         this.up_y = up_y;
         this.up_z = up_z;
      }

      public boolean isVertical() {
         double EP = 1.0E-5D;
         return this.x >= -1.0E-5D && this.x <= 1.0E-5D && this.z >= -1.0E-5D && this.z <= 1.0E-5D;
      }

      public double distanceSquared(Vector position) {
         double dx = position.getX() - this.x;
         double dy = position.getY() - this.y;
         double dz = position.getZ() - this.z;
         return dx * dx + dy * dy + dz * dz;
      }

      public double distanceSquared(RailPath.Position position) {
         double dx = position.posX - this.x;
         double dy = position.posY - this.y;
         double dz = position.posZ - this.z;
         return dx * dx + dy * dy + dz * dz;
      }

      public double dot(Vector vector) {
         return this.x * vector.getX() + this.y * vector.getY() + this.z * vector.getZ();
      }

      public final Vector up() {
         return new Vector(this.up_x, this.up_y, this.up_z);
      }

      public final Vector toVector() {
         return new Vector(this.x, this.y, this.z);
      }

      public final void toVector(Vector v) {
         v.setX(this.x);
         v.setY(this.y);
         v.setZ(this.z);
      }

      public final Location getLocation(Block railsBlock) {
         return new Location(railsBlock.getWorld(), (double)railsBlock.getX() + this.x, (double)railsBlock.getY() + this.y, (double)railsBlock.getZ() + this.z);
      }

      public String toString() {
         return "[v={" + this.x + "/" + this.y + "/" + this.z + "} up={" + this.up_x + "/" + this.up_y + "/" + this.up_z + "}]";
      }
   }

   public static class Segment {
      public final RailPath.Point p0;
      public final RailPath.Point p1;
      public final Vector p_offset;
      public final Vector mot;
      private final Vector mot_dt;
      public final Quaternion p0_orientation;
      public final Quaternion p1_orientation;
      public final boolean has_changing_up_orientation;
      public final boolean has_vertical_slope;
      private final double end_theta_threshold;
      public final double l;
      public final double ls;
      public final double linv;
      private RailPath.Segment prev;
      private RailPath.Segment next;
      /** @deprecated */
      @Deprecated
      public final RailPath.Point dt;
      /** @deprecated */
      @Deprecated
      public final RailPath.Point dt_norm;
      private IntVector3 containedInsideBlock = null;

      public Segment(RailPath.Point p0, RailPath.Point p1) {
         this.p_offset = new Vector(p1.x - p0.x, p1.y - p0.y, p1.z - p0.z);
         this.ls = this.p_offset.lengthSquared();
         this.l = Math.sqrt(this.ls);
         if (this.l <= 1.0E-20D) {
            this.linv = 0.0D;
            this.mot = new Vector();
            this.mot_dt = new Vector();
         } else {
            this.linv = 1.0D / this.l;
            this.mot = this.p_offset.clone().multiply(this.linv);
            this.mot_dt = this.p_offset.clone().multiply(1.0D / this.ls);
         }

         this.has_vertical_slope = this.mot.getY() < -1.0E-10D || this.mot.getY() > 1.0E-10D;
         this.end_theta_threshold = Math.min(0.001D, 1.0E-4D * this.linv);
         this.dt = new RailPath.Point(this.p_offset);
         this.dt_norm = new RailPath.Point(this.mot);
         Vector up0 = this.mot.clone().crossProduct(p0.up()).crossProduct(this.mot).normalize();
         Vector up1 = this.mot.clone().crossProduct(p1.up()).crossProduct(this.mot).normalize();
         this.p0 = new RailPath.Point(p0.x, p0.y, p0.z, up0.getX(), up0.getY(), up0.getZ());
         this.p1 = new RailPath.Point(p1.x, p1.y, p1.z, up1.getX(), up1.getY(), up1.getZ());
         this.has_changing_up_orientation = up0.distanceSquared(up1) > 1.0E-6D;
         this.p0_orientation = new Quaternion();
         this.p1_orientation = new Quaternion();
      }

      public void postinit() {
         Quaternion mid_up0 = Quaternion.fromLookDirection(this.mot, this.p0.up());
         Quaternion mid_up1;
         if (this.prev != null && !this.prev.isZeroLength()) {
            mid_up1 = Quaternion.fromLookDirection(this.prev.mot, this.prev.p1.up());
            this.p0_orientation.setTo(Quaternion.slerp(mid_up1, mid_up0, 0.5D));
         } else {
            this.p0_orientation.setTo(mid_up0);
         }

         mid_up1 = Quaternion.fromLookDirection(this.mot, this.p1.up());
         if (this.next != null && !this.next.isZeroLength()) {
            Quaternion next_up = Quaternion.fromLookDirection(this.next.mot, this.next.p0.up());
            this.p1_orientation.setTo(Quaternion.slerp(next_up, mid_up1, 0.5D));
         } else {
            this.p1_orientation.setTo(mid_up1);
         }

      }

      public final boolean isZeroLength() {
         return this.l <= 1.0E-5D;
      }

      public final Location getLocation(Block railsBlock, double theta) {
         return new Location(railsBlock.getWorld(), (double)railsBlock.getX() + this.p0.x + this.p_offset.getX() * theta, (double)railsBlock.getY() + this.p0.y + this.p_offset.getY() * theta, (double)railsBlock.getZ() + this.p0.z + this.p_offset.getZ() * theta);
      }

      private final int isHeadingToPrev(RailPath.Position position) {
         if (this.prev != null) {
            double dot = position.motDot(this.prev.mot);
            if (dot < -1.0E-7D) {
               return 1;
            } else {
               return dot > 1.0E-7D ? -1 : this.prev.isHeadingToPrev(position);
            }
         } else {
            return 0;
         }
      }

      private final int isHeadingToNext(RailPath.Position position) {
         if (this.next != null) {
            double dot = position.motDot(this.next.mot);
            if (dot > 1.0E-7D) {
               return 1;
            } else {
               return dot < -1.0E-7D ? -1 : this.next.isHeadingToNext(position);
            }
         } else {
            return 0;
         }
      }

      public final int calcDirection(RailPath.Position position) {
         Vector mot = this.mot;
         double dot = position.motDot(mot);
         if (dot <= 1.0E-8D && dot >= -1.0E-8D) {
            int order = this.isHeadingToPrev(position) - this.isHeadingToNext(position);
            if (order > 0) {
               dot = -1.0D;
            } else if (order < 0) {
               dot = 1.0D;
            } else {
               dot = this.p1.distanceSquared(position) - this.p0.distanceSquared(position);
               if (dot <= 1.0E-8D && dot >= -1.0E-8D) {
               }

               if (position.reverse) {
                  dot = -dot;
               }
            }
         }

         if (dot >= 0.0D) {
            position.motX = mot.getX();
            position.motY = mot.getY();
            position.motZ = mot.getZ();
            return 1;
         } else {
            position.motX = -mot.getX();
            position.motY = -mot.getY();
            position.motZ = -mot.getZ();
            return -1;
         }
      }

      public final double calcDistanceSquared(Vector position) {
         return this.calcDistanceSquared(position, this.calcTheta(position));
      }

      public final double calcDistanceSquared(Vector position, double theta) {
         Vector segmentPosition = new Vector();
         this.calcPosition(segmentPosition, theta);
         segmentPosition.subtract(position);
         return segmentPosition.lengthSquared();
      }

      public final double calcDistanceSquared(RailPath.Position position, double theta) {
         Vector segmentPosition = new Vector();
         this.calcPosition(segmentPosition, theta);
         segmentPosition.setX(segmentPosition.getX() - position.posX);
         segmentPosition.setY(segmentPosition.getY() - position.posY);
         segmentPosition.setZ(segmentPosition.getZ() - position.posZ);
         return segmentPosition.lengthSquared();
      }

      public final double calcDistanceSquared(double x, double y, double z) {
         return this.calcDistanceSquared(x, y, z, this.calcTheta(x, y, z));
      }

      public final double calcDistanceSquared(double x, double y, double z, double theta) {
         double dx;
         double dy;
         double dz;
         if (theta <= 0.0D) {
            dx = this.p0.x;
            dy = this.p0.y;
            dz = this.p0.z;
         } else if (theta >= 1.0D) {
            dx = this.p1.x;
            dy = this.p1.y;
            dz = this.p1.z;
         } else {
            dx = this.p0.x + this.p_offset.getX() * theta;
            dy = this.p0.y + this.p_offset.getY() * theta;
            dz = this.p0.z + this.p_offset.getZ() * theta;
         }

         dx -= x;
         dy -= y;
         dz -= z;
         dx *= dx;
         dy *= dy;
         dz *= dz;
         return dx + dy + dz;
      }

      public void calcPosition(Vector position, double theta) {
         if (theta <= 0.0D) {
            this.p0.toVector(position);
         } else if (theta >= 1.0D) {
            this.p1.toVector(position);
         } else {
            position.setX(this.p0.x + this.p_offset.getX() * theta);
            position.setY(this.p0.y + this.p_offset.getY() * theta);
            position.setZ(this.p0.z + this.p_offset.getZ() * theta);
         }

      }

      public void calcPosition(RailPath.Position position, double theta) {
         position.wheelSegment = this;
         position.wheelTheta = theta;
         if (theta <= 0.0D) {
            position.posX = this.p0.x;
            position.posY = this.p0.y;
            position.posZ = this.p0.z;
         } else if (theta >= 1.0D) {
            position.posX = this.p1.x;
            position.posY = this.p1.y;
            position.posZ = this.p1.z;
         } else {
            position.posX = this.p0.x + this.p_offset.getX() * theta;
            position.posY = this.p0.y + this.p_offset.getY() * theta;
            position.posZ = this.p0.z + this.p_offset.getZ() * theta;
         }

      }

      public Quaternion calcWheelOrientation(double theta) {
         if (theta <= 0.0D) {
            return this.p0_orientation;
         } else {
            return theta >= 1.0D ? this.p1_orientation : Quaternion.slerp(this.p0_orientation, this.p1_orientation, theta);
         }
      }

      public final double calcTheta(Vector position) {
         return this.calcTheta(position.getX(), position.getY(), position.getZ());
      }

      public final double calcTheta(RailPath.Position position) {
         return this.calcTheta(position.posX, position.posY, position.posZ);
      }

      public final double calcTheta(double x, double y, double z) {
         RailPath.Point p0 = this.p0;
         Vector mot = this.mot_dt;
         return -((p0.x - x) * mot.getX() + (p0.y - y) * mot.getY() + (p0.z - z) * mot.getZ());
      }
   }

   public static final class Position {
      public double posX;
      public double posY;
      public double posZ;
      public double motX;
      public double motY;
      public double motZ;
      private RailPath.Segment wheelSegment;
      private double wheelTheta;
      public boolean reverse = false;
      public boolean relative = true;

      public void makeRelative(Block railBlock) {
         if (!this.relative) {
            this.relative = true;
            this.posX -= (double)railBlock.getX();
            this.posY -= (double)railBlock.getY();
            this.posZ -= (double)railBlock.getZ();
         }

      }

      public void makeAbsolute(Block railBlock) {
         if (this.relative) {
            this.relative = false;
            this.posX += (double)railBlock.getX();
            this.posY += (double)railBlock.getY();
            this.posZ += (double)railBlock.getZ();
         }

      }

      public final void assertRelative() {
         if (!this.relative) {
            throw new IllegalStateException("Rail Position must be in relative coordinates");
         }
      }

      public final void assertAbsolute() {
         if (this.relative) {
            throw new IllegalStateException("Rail Position must be in absolute world coordinates");
         }
      }

      public void smallAdvance() {
         if (this.motX > 1.0E-6D) {
            this.posX = Math.nextUp(this.posX);
         } else if (this.motX < -1.0E-6D) {
            this.posX = Math.nextDown(this.posX);
         }

         if (this.motY > 1.0E-6D) {
            this.posY = Math.nextUp(this.posY);
         } else if (this.motY < -1.0E-6D) {
            this.posY = Math.nextDown(this.posY);
         }

         if (this.motZ > 1.0E-6D) {
            this.posZ = Math.nextUp(this.posZ);
         } else if (this.motZ < -1.0E-6D) {
            this.posZ = Math.nextDown(this.posZ);
         }

      }

      public Quaternion getWheelOrientation() {
         RailPath.Segment s = this.wheelSegment;
         return s == null ? Quaternion.fromLookDirection(this.getMotion(), new Vector(0, 1, 0)) : s.calcWheelOrientation(this.wheelTheta);
      }

      public void move(double distance) {
         this.posX += distance * this.motX;
         this.posY += distance * this.motY;
         this.posZ += distance * this.motZ;
      }

      public double distance(RailPath.Position position) {
         if (this.relative) {
            position.assertRelative();
         } else {
            position.assertAbsolute();
         }

         return MathUtil.distance(this.posX, this.posY, this.posZ, position.posX, position.posY, position.posZ);
      }

      public double distance(Location location) {
         this.assertAbsolute();
         return MathUtil.distance(this.posX, this.posY, this.posZ, location.getX(), location.getY(), location.getZ());
      }

      public double distanceSquared(Location location) {
         this.assertAbsolute();
         return MathUtil.distanceSquared(this.posX, this.posY, this.posZ, location.getX(), location.getY(), location.getZ());
      }

      public double distance(LocationAbstract location) {
         this.assertAbsolute();
         return MathUtil.distance(this.posX, this.posY, this.posZ, location.getX(), location.getY(), location.getZ());
      }

      public double distanceSquaredAtRail(Block railBlock, RailPath.Position pos) {
         if (pos.relative == this.relative) {
            return MathUtil.distanceSquared(this.posX, this.posY, this.posZ, pos.posX, pos.posY, pos.posZ);
         } else {
            return this.relative ? MathUtil.distanceSquared(this.posX, this.posY, this.posZ, pos.posX - (double)railBlock.getX(), pos.posY - (double)railBlock.getY(), pos.posZ - (double)railBlock.getZ()) : MathUtil.distanceSquared(this.posX - (double)railBlock.getX(), this.posY - (double)railBlock.getY(), this.posZ - (double)railBlock.getZ(), pos.posX, pos.posY, pos.posZ);
         }
      }

      public double distanceSquared(RailPath.Position pos) {
         if (pos.relative != this.relative) {
            throw new IllegalStateException("Self and pos must both be relative or both be absolute");
         } else {
            return MathUtil.distanceSquared(this.posX, this.posY, this.posZ, pos.posX, pos.posY, pos.posZ);
         }
      }

      public double distanceSquared(LocationAbstract pos) {
         if (this.relative) {
            throw new IllegalStateException("Self position must be absolute");
         } else {
            return pos.distanceSquared(this.posX, this.posY, this.posZ);
         }
      }

      public Location toLocation(World world) {
         this.assertAbsolute();
         return new Location(world, this.posX, this.posY, this.posZ);
      }

      public Location toLocation(Block railsBlock) {
         return this.relative ? new Location(railsBlock.getWorld(), (double)railsBlock.getX() + this.posX, (double)railsBlock.getY() + this.posY, (double)railsBlock.getZ() + this.posZ) : new Location(railsBlock.getWorld(), this.posX, this.posY, this.posZ);
      }

      public void getLocation(Location location) {
         this.assertAbsolute();
         location.setX(this.posX);
         location.setY(this.posY);
         location.setZ(this.posZ);
      }

      public void setLocation(Location location) {
         this.relative = false;
         this.posX = location.getX();
         this.posY = location.getY();
         this.posZ = location.getZ();
      }

      public void setLocation(LocationAbstract location) {
         this.relative = false;
         this.posX = location.getX();
         this.posY = location.getY();
         this.posZ = location.getZ();
      }

      public void setLocationMidOf(Block block) {
         this.relative = false;
         this.posX = (double)block.getX() + 0.5D;
         this.posY = (double)block.getY() + 0.5D;
         this.posZ = (double)block.getZ() + 0.5D;
      }

      public BlockFace getMotionFace() {
         return Util.vecToFace(this.motX, this.motY, this.motZ, false);
      }

      public BlockFace getMotionFaceWithSubCardinal() {
         return Util.vecToFace(this.motX, this.motY, this.motZ, true);
      }

      public double motDot(RailPath.Position pos) {
         return this.motX * pos.motX + this.motY * pos.motY + this.motZ * pos.motZ;
      }

      public double motDot(Vector v) {
         return this.motX * v.getX() + this.motY * v.getY() + this.motZ * v.getZ();
      }

      public double motDot(BlockFace face) {
         return this.motX * (double)face.getModX() + this.motY * (double)face.getModY() + this.motZ * (double)face.getModZ();
      }

      public double motDot(RailPath.Point point) {
         return this.motX * point.x + this.motY * point.y + this.motZ * point.z;
      }

      public double motDot(double dx, double dy, double dz) {
         return this.motX * dx + this.motY * dy + this.motZ * dz;
      }

      public double motLength() {
         return Math.sqrt(this.motLengthSquared());
      }

      public double motLengthSquared() {
         return this.motX * this.motX + this.motY * this.motY + this.motZ * this.motZ;
      }

      public Vector getMotion() {
         return new Vector(this.motX, this.motY, this.motZ);
      }

      public Vector getMotion(Vector v) {
         v.setX(this.motX);
         v.setY(this.motY);
         v.setZ(this.motZ);
         return v;
      }

      public void setMotion(VectorAbstract movement) {
         this.motX = movement.getX();
         this.motY = movement.getY();
         this.motZ = movement.getZ();
      }

      public void setMotion(Vector movement) {
         if (Double.isNaN(movement.getX())) {
            throw new IllegalArgumentException("Motion vector is NaN");
         } else {
            this.motX = movement.getX();
            this.motY = movement.getY();
            this.motZ = movement.getZ();
         }
      }

      public void setMotion(BlockFace movement) {
         this.motX = (double)movement.getModX();
         this.motY = (double)movement.getModY();
         this.motZ = (double)movement.getModZ();
      }

      public void invertMotion() {
         this.motX = -this.motX;
         this.motY = -this.motY;
         this.motZ = -this.motZ;
      }

      public void normalizeMotion() {
         double n = MathUtil.getNormalizationFactor(this.motX, this.motY, this.motZ);
         if (Double.isInfinite(n)) {
            this.motX = 0.0D;
            this.motY = -1.0D;
            this.motZ = 0.0D;
         } else {
            this.motX *= n;
            this.motY *= n;
            this.motZ *= n;
         }

      }

      public void copyTo(RailPath.Position p) {
         p.posX = this.posX;
         p.posY = this.posY;
         p.posZ = this.posZ;
         p.motX = this.motX;
         p.motY = this.motY;
         p.motZ = this.motZ;
         p.wheelSegment = this.wheelSegment;
         p.wheelTheta = this.wheelTheta;
         p.reverse = this.reverse;
         p.relative = this.relative;
      }

      public RailPath.Position clone() {
         RailPath.Position p = new RailPath.Position();
         this.copyTo(p);
         return p;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof RailPath.Position)) {
            return false;
         } else {
            RailPath.Position other = (RailPath.Position)o;
            return this.posX == other.posX && this.posY == other.posY && this.posZ == other.posZ && this.motX == other.motX && this.motY == other.motY && this.motZ == other.motZ && this.relative == other.relative && this.reverse == other.reverse;
         }
      }

      public String toString() {
         return (this.relative ? "{rel_pos={" : "{pos={") + MathUtil.round(this.posX, 4) + "/" + MathUtil.round(this.posY, 4) + "/" + MathUtil.round(this.posZ, 4) + "}, mot={" + MathUtil.round(this.motX, 4) + "/" + MathUtil.round(this.motY, 4) + "/" + MathUtil.round(this.motZ, 4) + "}, f=" + this.getMotionFace().name() + "}";
      }

      public static RailPath.Position fromPosDir(Vector position, Vector direction) {
         RailPath.Position p = new RailPath.Position();
         p.relative = false;
         p.posX = position.getX();
         p.posY = position.getY();
         p.posZ = position.getZ();
         p.motX = direction.getX();
         p.motY = direction.getY();
         p.motZ = direction.getZ();
         return p;
      }

      public static RailPath.Position fromTo(Location from, Location to) {
         RailPath.Position p = new RailPath.Position();
         p.relative = false;
         p.posX = from.getX();
         p.posY = from.getY();
         p.posZ = from.getZ();
         p.motX = to.getX() - p.posX;
         p.motY = to.getY() - p.posY;
         p.motZ = to.getZ() - p.posZ;
         return p;
      }

      public static RailPath.Position fromLocation(Location positionWithDirection) {
         RailPath.Position p = new RailPath.Position();
         p.relative = false;
         p.posX = positionWithDirection.getX();
         p.posY = positionWithDirection.getY();
         p.posZ = positionWithDirection.getZ();
         Vector dir = positionWithDirection.getDirection();
         p.motX = dir.getX();
         p.motY = dir.getY();
         p.motZ = dir.getZ();
         return p;
      }
   }

   public static class ProximityInfo implements Comparable<RailPath.ProximityInfo> {
      private static final double DIST_DIFF_SQ_THRESHOLD = 1.0E-6D;
      public double distanceSquared = Double.MAX_VALUE;
      public boolean canMoveForward = false;

      public int compareTo(RailPath.ProximityInfo o) {
         double diffDistSq = this.distanceSquared - o.distanceSquared;
         if (diffDistSq > 1.0E-6D) {
            return 1;
         } else {
            return diffDistSq < -1.0E-6D ? -1 : Boolean.compare(o.canMoveForward, this.canMoveForward);
         }
      }
   }

   public static class Builder {
      private List<RailPath.Point> points = new ArrayList(3);
      private double default_up_x = 0.0D;
      private double default_up_y = 1.0D;
      private double default_up_z = 0.0D;

      public RailPath.Builder up(BlockFace up) {
         return this.up((double)up.getModX(), (double)up.getModY(), (double)up.getModZ());
      }

      public RailPath.Builder up(double up_x, double up_y, double up_z) {
         this.default_up_x = up_x;
         this.default_up_y = up_y;
         this.default_up_z = up_z;
         return this;
      }

      public RailPath.Builder add(double x, double y, double z) {
         return this.add(new RailPath.Point(x, y, z, this.default_up_x, this.default_up_y, this.default_up_z));
      }

      public RailPath.Builder add(double x, double y, double z, double up_x, double up_y, double up_z) {
         return this.add(new RailPath.Point(x, y, z, up_x, up_y, up_z));
      }

      public RailPath.Builder add(double x, double y, double z, BlockFace face) {
         return this.add(new RailPath.Point(x, y, z, face));
      }

      public RailPath.Builder add(Vector point) {
         return this.add(new RailPath.Point(point, this.default_up_x, this.default_up_y, this.default_up_z));
      }

      public RailPath.Builder add(Vector point, double up_x, double up_y, double up_z) {
         return this.add(new RailPath.Point(point, up_x, up_y, up_z));
      }

      public RailPath.Builder add(Vector point, BlockFace face) {
         return this.add(new RailPath.Point(point, face));
      }

      public RailPath.Builder add(RailPath.Point point) {
         this.points.add(point);
         return this;
      }

      public RailPath build() {
         return RailPath.create((RailPath.Point[])this.points.toArray(new RailPath.Point[this.points.size()]));
      }
   }
}
