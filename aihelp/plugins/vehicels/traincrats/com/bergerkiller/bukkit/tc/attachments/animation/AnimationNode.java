package com.bergerkiller.bukkit.tc.attachments.animation;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.bukkit.util.Vector;

public class AnimationNode implements Cloneable {
   private final Vector _position;
   private Vector _rotationVec;
   private Quaternion _rotationQuat;
   private final boolean _active;
   private final double _duration;
   private final boolean _hasValidDuration;
   private final String _scene;

   public AnimationNode(Vector position, Quaternion rotationQuaternion, boolean active, double duration) {
      this._position = position;
      this._rotationVec = null;
      this._rotationQuat = rotationQuaternion;
      this._active = active;
      this._hasValidDuration = !Double.isNaN(duration);
      this._duration = this._hasValidDuration ? duration : 0.0D;
      this._scene = null;
   }

   public AnimationNode(Vector position, Vector rotationVector, boolean active, double duration) {
      this._position = position;
      this._rotationVec = rotationVector;
      this._rotationQuat = null;
      this._active = active;
      this._hasValidDuration = !Double.isNaN(duration);
      this._duration = this._hasValidDuration ? duration : 0.0D;
      this._scene = null;
   }

   public AnimationNode(Vector position, Vector rotationVector, boolean active, double duration, String scene) {
      this._position = position;
      this._rotationVec = rotationVector;
      this._rotationQuat = null;
      this._active = active;
      this._hasValidDuration = !Double.isNaN(duration);
      this._duration = this._hasValidDuration ? duration : 0.0D;
      this._scene = scene;
   }

   public String getSceneMarker() {
      return this._scene;
   }

   public boolean hasSceneMarker() {
      return this._scene != null;
   }

   public AnimationNode setSceneMarker(String sceneName) {
      if (LogicUtil.bothNullOrEqual(this._scene, sceneName)) {
         return this;
      } else {
         sceneName = sceneName.trim();
         if (sceneName.isEmpty()) {
            sceneName = null;
         } else {
            sceneName = sceneName.replace(' ', '_');
            sceneName = sceneName.replace('\t', '_');
         }

         return new AnimationNode(this._position, this._rotationVec, this._active, this._duration, sceneName);
      }
   }

   public Vector getPosition() {
      return this._position;
   }

   public Vector getRotationVector() {
      if (this._rotationVec == null) {
         this._rotationVec = this._rotationQuat.getYawPitchRoll();
      }

      return this._rotationVec;
   }

   public Quaternion getRotationQuaternion() {
      if (this._rotationQuat == null) {
         this._rotationQuat = Quaternion.fromYawPitchRoll(this._rotationVec);
      }

      return this._rotationQuat;
   }

   public double getDuration() {
      return this._duration;
   }

   public boolean hasValidDuration() {
      return this._hasValidDuration;
   }

   public boolean isActive() {
      return this._active;
   }

   public void apply(Matrix4x4 transform) {
      transform.translate(this.getPosition());
      transform.rotate(this.getRotationQuaternion());
   }

   public String serializeToString() {
      Vector pos = this.getPosition();
      Vector ypr = this.getRotationVector().clone();
      ypr.setX(MathUtil.round(ypr.getX(), 6));
      ypr.setY(MathUtil.round(ypr.getY(), 6));
      ypr.setZ(MathUtil.round(ypr.getZ(), 6));
      String scene = this.getSceneMarker();
      StringBuilder builder = new StringBuilder(90);
      builder.append("t=").append(this._duration);
      if (!this.isActive()) {
         builder.append(" active=0");
      }

      if (pos.getX() != 0.0D) {
         builder.append(" x=").append(pos.getX());
      }

      if (pos.getY() != 0.0D) {
         builder.append(" y=").append(pos.getY());
      }

      if (pos.getZ() != 0.0D) {
         builder.append(" z=").append(pos.getZ());
      }

      if (ypr.getX() != 0.0D) {
         builder.append(" pitch=").append(ypr.getX());
      }

      if (ypr.getY() != 0.0D) {
         builder.append(" yaw=").append(ypr.getY());
      }

      if (ypr.getZ() != 0.0D) {
         builder.append(" roll=").append(ypr.getZ());
      }

      if (scene != null) {
         builder.append(" scene=" + scene);
      }

      return builder.toString();
   }

   public AnimationNode cloneWithoutSceneMarker() {
      return new AnimationNode(this._position.clone(), this._rotationVec.clone(), this._active, this._duration);
   }

   public AnimationNode clone() {
      return new AnimationNode(this._position.clone(), this._rotationVec.clone(), this._active, this._duration, this._scene);
   }

   public String toString() {
      return this.serializeToString();
   }

   public static AnimationNode parseFromString(String config) {
      return (new AnimationNode.Parser(config)).parse();
   }

   public static AnimationNode[] parseAllFromStrings(List<String> configList) {
      AnimationNode[] nodes = new AnimationNode[configList.size()];

      for(int i = 0; i < nodes.length; ++i) {
         nodes[i] = parseFromString((String)configList.get(i));
      }

      return nodes;
   }

   public static AnimationNode interpolate(AnimationNode nodeA, AnimationNode nodeB, double theta) {
      if (theta <= 0.0D) {
         return nodeA;
      } else if (theta >= 1.0D) {
         return nodeB;
      } else {
         Vector lerp_position = MathUtil.lerp(nodeA.getPosition(), nodeB.getPosition(), theta);
         Quaternion lerp_rotation = Quaternion.slerp(nodeA.getRotationQuaternion(), nodeB.getRotationQuaternion(), theta);
         return new AnimationNode(lerp_position, lerp_rotation, nodeA.isActive(), 1.0D);
      }
   }

   public static AnimationNode identity() {
      return new AnimationNode(new Vector(), new Quaternion(), true, 1.0D);
   }

   public static AnimationNode average(Collection<AnimationNode> nodes) {
      if (nodes.size() == 1) {
         return (AnimationNode)nodes.iterator().next();
      } else if (nodes.isEmpty()) {
         return identity();
      } else {
         double fact = 1.0D / (double)nodes.size();
         Vector pos = new Vector();
         Vector rot = new Vector();
         int num_active = 0;
         double duration = 0.0D;
         Iterator var8 = nodes.iterator();

         while(var8.hasNext()) {
            AnimationNode node = (AnimationNode)var8.next();
            pos.setX(pos.getX() + fact * node.getPosition().getX());
            pos.setY(pos.getY() + fact * node.getPosition().getY());
            pos.setZ(pos.getZ() + fact * node.getPosition().getZ());
            rot.setX(rot.getX() + fact * node.getRotationVector().getX());
            rot.setY(rot.getY() + fact * node.getRotationVector().getY());
            rot.setZ(rot.getZ() + fact * node.getRotationVector().getZ());
            duration += fact * node.getDuration();
            if (node.isActive()) {
               ++num_active;
            }
         }

         rot.setX(MathUtil.wrapAngle(rot.getX()));
         rot.setY(MathUtil.wrapAngle(rot.getY()));
         rot.setZ(MathUtil.wrapAngle(rot.getZ()));
         return new AnimationNode(pos, rot, num_active >= nodes.size() >> 1, duration);
      }
   }

   private static boolean isNumericChar(char ch) {
      return Character.isDigit(ch) || ch == '.' || ch == '-';
   }

   private static class Parser {
      private final String config;
      private final int config_length;
      private int index = 0;
      private Vector position = new Vector();
      private Vector rotation = new Vector();
      private String scene = null;
      private boolean active = true;
      private double duration = Double.NaN;

      public Parser(String config) {
         this.config = config;
         this.config_length = config.length();
      }

      private void skip(AnimationNode.Parser.CharFilter filter) {
         while(this.index < this.config_length && filter.filter(this.config.charAt(this.index))) {
            ++this.index;
         }

      }

      private String nextName() {
         this.skip((ch) -> {
            return !Character.isLetter(ch);
         });
         int name_start = this.index;
         this.skip(Character::isLetter);
         return this.config.substring(name_start, this.index);
      }

      public AnimationNode parse() {
         while(true) {
            if (this.index < this.config_length) {
               String name = this.nextName();
               if (name.isEmpty()) {
                  continue;
               }

               int valueStart;
               if ("scene".equals(name)) {
                  this.skip((ch) -> {
                     return ch == ' ' || ch == '\t';
                  });
                  if (this.index < this.config_length) {
                     if (this.config.charAt(this.index) == '=') {
                        ++this.index;
                     }

                     this.skip((ch) -> {
                        return ch == ' ' || ch == '\t';
                     });
                     valueStart = this.index;
                     this.skip((ch) -> {
                        return ch != ' ' && ch != '\t';
                     });
                     this.scene = this.config.substring(valueStart, this.index);
                     if (this.scene.isEmpty()) {
                        this.scene = null;
                     }
                     continue;
                  }
               } else {
                  this.skip((ch) -> {
                     return !AnimationNode.isNumericChar(ch);
                  });
                  valueStart = this.index;
                  this.skip((x$0) -> {
                     return AnimationNode.isNumericChar(x$0);
                  });
                  int value_end = this.index;
                  if (valueStart < this.config_length) {
                     double value;
                     try {
                        value = Double.parseDouble(this.config.substring(valueStart, value_end));
                     } catch (NumberFormatException var7) {
                        value = 0.0D;
                     }

                     if ("t".equals(name)) {
                        this.duration = value;
                        continue;
                     }

                     if ("x".equals(name)) {
                        this.position.setX(value);
                        continue;
                     }

                     if ("y".equals(name)) {
                        this.position.setY(value);
                        continue;
                     }

                     if ("z".equals(name)) {
                        this.position.setZ(value);
                        continue;
                     }

                     if ("pitch".equals(name)) {
                        this.rotation.setX(value);
                        continue;
                     }

                     if ("yaw".equals(name)) {
                        this.rotation.setY(value);
                        continue;
                     }

                     if ("roll".equals(name)) {
                        this.rotation.setZ(value);
                        continue;
                     }

                     if ("active".equals(name)) {
                        this.active = value != 0.0D;
                     }
                     continue;
                  }
               }
            }

            return new AnimationNode(this.position, this.rotation, this.active, this.duration, this.scene);
         }
      }

      @FunctionalInterface
      private interface CharFilter {
         boolean filter(char var1);
      }
   }
}
