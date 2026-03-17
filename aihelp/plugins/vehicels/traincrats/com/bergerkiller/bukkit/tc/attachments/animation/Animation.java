package com.bergerkiller.bukkit.tc.attachments.animation;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.bukkit.util.Vector;

public class Animation implements Cloneable {
   private AnimationOptions _options;
   private final AnimationNode[] _nodes;
   private final Map<String, Animation.Scene> _scenes;
   private final Animation.Scene _entireAnimationScene;
   private Animation.Scene _currentScene;
   private Animation.MovementSpeedController _speedControl;
   private double _time;
   private boolean _startedPlaying;
   private boolean _reachedEnd;

   protected Animation(Animation source) {
      this._options = source._options.clone();
      this._nodes = source._nodes;
      this._scenes = source._scenes;
      this._entireAnimationScene = source._entireAnimationScene;
      this._currentScene = source._currentScene;
      this._speedControl = null;
      this._time = source._time;
      this._startedPlaying = source._startedPlaying;
      this._reachedEnd = source._reachedEnd;
   }

   public Animation(String name, String... nodes_config) {
      this(name, Arrays.asList(nodes_config));
   }

   public Animation(String name, List<String> nodes_config) {
      this(name, AnimationNode.parseAllFromStrings(nodes_config));
   }

   public Animation(String name, AnimationNode[] nodes) {
      this._options = new AnimationOptions(name);
      this._nodes = nodes;
      this._time = 0.0D;
      this._startedPlaying = false;
      this._reachedEnd = false;
      this._scenes = new LinkedHashMap();
      String lastSceneName = null;
      int lastSceneBegin = -1;
      double lastSceneDuration = 0.0D;

      int i;
      AnimationNode node;
      for(i = 0; i < nodes.length; ++i) {
         node = nodes[i];
         if (node.hasSceneMarker() && !node.getSceneMarker().equals(lastSceneName)) {
            if (lastSceneName != null) {
               this._scenes.put(lastSceneName, new Animation.Scene(lastSceneBegin, i - 1, lastSceneDuration));
            }

            lastSceneName = node.getSceneMarker();
            lastSceneDuration = node.getDuration();
            lastSceneBegin = i;
         } else {
            lastSceneDuration += node.getDuration();
         }
      }

      if (lastSceneName != null) {
         this._scenes.put(lastSceneName, new Animation.Scene(lastSceneBegin, nodes.length - 1, lastSceneDuration));
      }

      if (nodes.length > 0) {
         double total = 0.0D;
         AnimationNode[] var10 = nodes;
         int var6 = nodes.length;

         for(i = 0; i < var6; ++i) {
            node = var10[i];
            total += node.getDuration();
         }

         this._entireAnimationScene = new Animation.Scene(0, nodes.length - 1, total);
      } else {
         this._entireAnimationScene = new Animation.Scene(0, 0, 0.0D);
      }

      this._currentScene = this._entireAnimationScene;
   }

   public final AnimationOptions getOptions() {
      return this._options;
   }

   public final Set<String> getSceneNames() {
      return Collections.unmodifiableSet(this._scenes.keySet());
   }

   public Animation setOptions(AnimationOptions options) {
      double old_delay = this._options.getDelay();
      this._options = options;
      this._time -= this._options.getDelay() - old_delay;
      this.updateScene(this.createScene(options));
      this._reachedEnd = false;
      if (!this._options.hasMovementControlledOption()) {
         this._speedControl = null;
      }

      return this;
   }

   public boolean hasReachedEnd() {
      return this._reachedEnd;
   }

   public Animation applyOptions(AnimationOptions options) {
      double old_delay = this._options.getDelay();
      this._options.apply(options);
      this._time -= this._options.getDelay() - old_delay;
      this.updateScene(this.createScene(options));
      this._reachedEnd = false;
      if (!this._options.hasMovementControlledOption()) {
         this._speedControl = null;
      }

      return this;
   }

   public void start() {
      if (this._options.isReversed()) {
         this._time = this._currentScene.duration();
         if (this._nodes.length >= 1) {
            this._time -= this._nodes[this._currentScene.nodeEndIndex()].getDuration();
         }
      } else {
         this._time = 0.0D;
      }

      this._time -= this._options.getDelay();
      this._startedPlaying = false;
      this._reachedEnd = false;
   }

   public boolean isSame(Animation animation) {
      return animation.getOptions().getName().equals(this.getOptions().getName());
   }

   public AnimationNode[] getNodeArray() {
      return this._nodes;
   }

   public AnimationNode getNode(int index) {
      return this._nodes[index];
   }

   public int getNodeCount() {
      return this._nodes.length;
   }

   public Animation clone() {
      return new Animation(this);
   }

   public AnimationNode update(double dt, Matrix4x4 speedControlTransform) {
      if (this._nodes.length == 0) {
         this._startedPlaying = false;
         this._reachedEnd = true;
         return null;
      } else {
         Animation.Scene scene = this._currentScene;
         if (scene.isSingleFrame()) {
            this._startedPlaying = true;
            this._reachedEnd = true;
         }

         if (this._reachedEnd) {
            return this._nodes[this._options.isReversed() ? scene.nodeBeginIndex() : scene.nodeEndIndex()];
         } else {
            if (this._options.isMovementControlled()) {
               Animation.MovementSpeedController control = this._speedControl;
               if (control == null) {
                  this._speedControl = new Animation.MovementSpeedController(speedControlTransform);
                  dt = 0.0D;
               } else {
                  dt = control.update(speedControlTransform);
               }
            }

            double curr_time = this._time;
            this._time += dt * this._options.getSpeed();
            AnimationNode endNode;
            double animEnd;
            if (!this._startedPlaying) {
               if (!this._options.isLooped()) {
                  endNode = this._nodes[scene.nodeEndIndex()];
                  animEnd = scene.duration() - endNode.getDuration();
                  if (this._options.isReversed()) {
                     if (curr_time > animEnd) {
                        if (this._time < 0.0D) {
                           this._time = 0.0D;
                        }

                        return null;
                     }
                  } else if (curr_time < 0.0D) {
                     if (this._time > animEnd) {
                        this._time = animEnd;
                     }

                     return null;
                  }
               }

               this._startedPlaying = true;
            }

            if (this._options.isLooped()) {
               this._time %= scene.duration();
               if (this._time < 0.0D) {
                  this._time += scene.duration();
               }
            } else if (this._options.isReversed()) {
               if (curr_time == 0.0D) {
                  this._time = 0.0D;
                  this._reachedEnd = true;
                  return this._nodes[scene.nodeBeginIndex()];
               }

               if (this._time < 0.0D) {
                  this._time = 0.0D;
               }
            } else {
               endNode = this._nodes[scene.nodeEndIndex()];
               animEnd = scene.duration() - endNode.getDuration();
               if (curr_time == animEnd) {
                  this._time = animEnd;
                  this._reachedEnd = true;
                  return endNode;
               }

               if (curr_time > animEnd) {
                  if (this._time >= scene.duration()) {
                     this._time -= scene.duration();
                     if (this._time > animEnd) {
                        this._time = animEnd;
                     }
                  }
               } else if (this._time > animEnd) {
                  this._time = animEnd;
               }
            }

            return this.findPlayPosition(scene, curr_time).toNode();
         }
      }
   }

   private Animation.PlayPosition findPlayPosition(Animation.Scene scene, double elapsedTime) {
      if (scene.isSingleFrame()) {
         return new Animation.PlayPosition(elapsedTime, scene.nodeBeginIndex(), this._nodes[scene.nodeBeginIndex()]);
      } else {
         boolean playReversed = this._options.isReversed();
         Iterator var5 = scene.iteratePlayPositions(this._nodes, this._options.isLooped()).iterator();

         Animation.PlayPosition result;
         do {
            if (!var5.hasNext()) {
               return new Animation.PlayPosition(scene.duration(), scene.nodeEndIndex(), this._nodes[scene.nodeEndIndex()]);
            }

            Animation.PlayPosition scenePosition = (Animation.PlayPosition)var5.next();
            result = scenePosition.findPosition(elapsedTime, playReversed);
         } while(result == null);

         return result;
      }
   }

   private double findTime(Animation.Scene scene, Animation.PlayPosition playPosition) {
      double time = 0.0D;

      Animation.PlayPosition scenePosition;
      for(Iterator var5 = scene.iteratePlayPositions(this._nodes, this._options.isLooped()).iterator(); var5.hasNext(); time = scenePosition.elapsedTime()) {
         scenePosition = (Animation.PlayPosition)var5.next();
         if (scenePosition.node0Index() == playPosition.node0Index()) {
            return scenePosition.elapsedTime() + playPosition.deltaTime();
         }
      }

      return time;
   }

   private void updateScene(Animation.Scene scene) {
      Animation.PlayPosition playPosition = this.findPlayPosition(this._currentScene, this._time);
      if (scene.containsPosition(playPosition)) {
         this._time = this.findTime(scene, playPosition);
      } else {
         this._time = this._options.isReversed() ? scene.duration() : 0.0D;
      }

      this._currentScene = scene;
   }

   private Animation.Scene createScene(AnimationOptions options) {
      if (this._nodes.length != 0 && options.hasSceneOption()) {
         if (options.isSingleScene()) {
            return (Animation.Scene)this._scenes.getOrDefault(options.getSceneBegin(), this._entireAnimationScene);
         } else {
            int beginIndex = 0;
            int endIndex = this._nodes.length - 1;
            if (options.getSceneBegin() != null) {
               beginIndex = ((Animation.Scene)this._scenes.getOrDefault(options.getSceneBegin(), this._entireAnimationScene)).nodeBeginIndex();
            }

            if (options.getSceneEnd() != null) {
               endIndex = ((Animation.Scene)this._scenes.getOrDefault(options.getSceneEnd(), this._entireAnimationScene)).nodeEndIndex();
            }

            double duration = 0.0D;

            Animation.PlayPosition position;
            for(Iterator var6 = Animation.PlayPosition.iterate(this._nodes, beginIndex, endIndex, true).iterator(); var6.hasNext(); duration = position.elapsedTime()) {
               position = (Animation.PlayPosition)var6.next();
            }

            return new Animation.Scene(beginIndex, endIndex, duration);
         }
      } else {
         return this._entireAnimationScene;
      }
   }

   public void saveToConfig(ConfigurationNode config) {
      this.getOptions().saveToConfig(config);
      List<String> nodes_str = new ArrayList(this._nodes.length);
      AnimationNode[] var3 = this._nodes;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         AnimationNode node = var3[var5];
         nodes_str.add(node.serializeToString());
      }

      config.set("nodes", nodes_str);
   }

   public void saveToParentConfig(ConfigurationNode parentConfig) {
      this.saveToConfig(parentConfig.getNode(this.getOptions().getName()));
   }

   public static Animation loadFromConfig(ConfigurationNode config) {
      String name = config.getName();
      List<String> nodes_str = config.getList("nodes", String.class);
      Animation animation = new Animation(name, nodes_str);
      animation.getOptions().loadFromConfig(config);
      return animation;
   }

   public static final class Scene {
      private final int _nodeBegin;
      private final int _nodeEnd;
      private final double _duration;

      public Scene(int nodeBegin, int nodeEnd, double duration) {
         this._nodeBegin = nodeBegin;
         this._nodeEnd = nodeEnd;
         this._duration = duration;
      }

      public int nodeBeginIndex() {
         return this._nodeBegin;
      }

      public int nodeEndIndex() {
         return this._nodeEnd;
      }

      public boolean isInsideOut() {
         return this._nodeBegin > this._nodeEnd;
      }

      public double duration() {
         return this._duration;
      }

      public boolean isSingleFrame() {
         return this._nodeBegin == this._nodeEnd || this._duration <= 1.0E-20D;
      }

      public boolean containsPosition(Animation.PlayPosition playPosition) {
         if (!(playPosition instanceof Animation.PlayPositionBetween)) {
            return this.isNodePlayed(playPosition.node0Index());
         } else {
            Animation.PlayPositionBetween between = (Animation.PlayPositionBetween)playPosition;
            return this.isNodePlayed(between.node0Index()) && this.isNodePlayed(between.node1Index());
         }
      }

      private boolean isNodePlayed(int nodeIndex) {
         if (this.isInsideOut()) {
            return nodeIndex >= this._nodeBegin || nodeIndex <= this._nodeEnd;
         } else {
            return nodeIndex >= this._nodeBegin && nodeIndex <= this._nodeEnd;
         }
      }

      public Iterable<Animation.PlayPosition> iteratePlayPositions(AnimationNode[] nodes, boolean looped) {
         return Animation.PlayPosition.iterate(nodes, this.nodeBeginIndex(), this.nodeEndIndex(), looped);
      }

      public String toString() {
         return "Scene{duration=" + this._duration + ", start=" + this._nodeBegin + ", end=" + this._nodeEnd + "}";
      }
   }

   private static final class MovementSpeedController {
      private final Vector prevPosition;
      private final Vector prevForward;

      public MovementSpeedController(Matrix4x4 initial) {
         this.prevPosition = initial.toVector();
         this.prevForward = initial.getRotation().forwardVector();
      }

      public double update(Matrix4x4 transform) {
         Vector newPosition = transform.toVector();
         Vector diff = newPosition.clone().subtract(this.prevPosition);
         double d = diff.dot(this.prevForward);
         MathUtil.setVector(this.prevPosition, newPosition);
         MathUtil.setVector(this.prevForward, transform.getRotation().forwardVector());
         return d;
      }
   }

   public static class PlayPosition {
      private final double elapsedTime;
      private final AnimationNode node0;
      private final int node0Index;

      public PlayPosition(double elapsedTime, int node0Index, AnimationNode node0) {
         this.elapsedTime = elapsedTime;
         this.node0Index = node0Index;
         this.node0 = node0;
      }

      public double elapsedTime() {
         return this.elapsedTime;
      }

      public int node0Index() {
         return this.node0Index;
      }

      public AnimationNode node0() {
         return this.node0;
      }

      public AnimationNode toNode() {
         return this.node0;
      }

      public double deltaTime() {
         return 0.0D;
      }

      public Animation.PlayPosition findPosition(double elapsedTime, boolean playReversed) {
         return this.elapsedTime() == elapsedTime ? this : null;
      }

      public String toString() {
         return "Position{t=" + this.elapsedTime() + ", @ " + this.node0Index() + "}";
      }

      public static Iterable<Animation.PlayPosition> iterate(AnimationNode[] nodes, int nodeBeginIndex, int nodeEndIndex, boolean looped) {
         if (nodeBeginIndex == nodeEndIndex) {
            return Collections.singletonList(new Animation.PlayPosition(0.0D, nodeBeginIndex, nodes[nodeBeginIndex]));
         } else {
            return nodeBeginIndex > nodeEndIndex ? () -> {
               return new Iterator<Animation.PlayPosition>() {
                  private double totalElapsedTime = 0.0D;
                  private int nodeIndex = nodeBeginIndex;
                  private boolean isLoopedToBeginning = false;
                  private boolean isLoopedToEndOfAnimation = false;

                  public boolean hasNext() {
                     return this.nodeIndex >= 0;
                  }

                  public Animation.PlayPosition next() {
                     int currNodeIndex = this.nodeIndex;
                     if (currNodeIndex < 0) {
                        throw new NoSuchElementException();
                     } else {
                        AnimationNode currNode = nodes[currNodeIndex];
                        double currElapsed = this.totalElapsedTime;
                        this.totalElapsedTime += currNode.getDuration();
                        if (this.isLoopedToBeginning) {
                           this.nodeIndex = -1;
                           return new Animation.PlayPosition(currElapsed, currNodeIndex, currNode);
                        } else {
                           int nextNodeIndex = currNodeIndex + 1;
                           if (this.isLoopedToEndOfAnimation) {
                              if (nextNodeIndex > nodeEndIndex) {
                                 if (!looped) {
                                    this.nodeIndex = -1;
                                    return new Animation.PlayPosition(currElapsed, currNodeIndex, currNode);
                                 }

                                 this.isLoopedToBeginning = true;
                                 nextNodeIndex = nodeBeginIndex;
                              }
                           } else if (nextNodeIndex >= nodes.length) {
                              this.isLoopedToEndOfAnimation = true;
                              nextNodeIndex = 0;
                           }

                           this.nodeIndex = nextNodeIndex;
                           return new Animation.PlayPositionBetween(currElapsed, 0.0D, currNodeIndex, currNode, nextNodeIndex, nodes[nextNodeIndex]);
                        }
                     }
                  }
               };
            } : () -> {
               return new Iterator<Animation.PlayPosition>() {
                  private double totalElapsedTime = 0.0D;
                  private int nodeIndex = nodeBeginIndex;
                  private boolean isLoopedToBeginning = false;

                  public boolean hasNext() {
                     return this.nodeIndex >= 0;
                  }

                  public Animation.PlayPosition next() {
                     int currNodeIndex = this.nodeIndex;
                     if (currNodeIndex < 0) {
                        throw new NoSuchElementException();
                     } else {
                        AnimationNode currNode = nodes[currNodeIndex];
                        double currElapsed = this.totalElapsedTime;
                        this.totalElapsedTime += currNode.getDuration();
                        int nextNodeIndex = currNodeIndex + 1;
                        if (this.isLoopedToBeginning) {
                           this.nodeIndex = -1;
                           return new Animation.PlayPosition(currElapsed, currNodeIndex, currNode);
                        } else {
                           if (nextNodeIndex > nodeEndIndex) {
                              if (!looped) {
                                 this.nodeIndex = -1;
                                 return new Animation.PlayPosition(currElapsed, currNodeIndex, currNode);
                              }

                              this.isLoopedToBeginning = true;
                              nextNodeIndex = nodeBeginIndex;
                           }

                           this.nodeIndex = nextNodeIndex;
                           return new Animation.PlayPositionBetween(currElapsed, 0.0D, currNodeIndex, currNode, nextNodeIndex, nodes[nextNodeIndex]);
                        }
                     }
                  }
               };
            };
         }
      }
   }

   public static class PlayPositionBetween extends Animation.PlayPosition {
      public final double theta;
      public final AnimationNode node1;
      public final int node1Index;

      public PlayPositionBetween(double elapsedTime, double theta, int node0Index, AnimationNode node0, int node1Index, AnimationNode node1) {
         super(elapsedTime, node0Index, node0);
         this.theta = theta;
         this.node1 = node1;
         this.node1Index = node1Index;
      }

      public int node1Index() {
         return this.node1Index;
      }

      public AnimationNode node1() {
         return this.node1;
      }

      public double theta() {
         return this.theta;
      }

      public Animation.PlayPosition findPosition(double elapsedTime, boolean playReversed) {
         double delta = elapsedTime - this.elapsedTime();
         double duration = this.node0().getDuration();
         if (delta == 0.0D) {
            return !(duration > 0.0D) && !playReversed ? new Animation.PlayPosition(elapsedTime, this.node1Index(), this.node1()) : new Animation.PlayPosition(elapsedTime, this.node0Index(), this.node0());
         } else if (delta == duration) {
            return new Animation.PlayPosition(elapsedTime, this.node1Index(), this.node1());
         } else {
            return delta < duration ? new Animation.PlayPositionBetween(elapsedTime, delta / duration, this.node0Index(), this.node0(), this.node1Index(), this.node1()) : null;
         }
      }

      public AnimationNode toNode() {
         return AnimationNode.interpolate(this.node0(), this.node1(), this.theta());
      }

      public double deltaTime() {
         return this.theta * this.node0().getDuration();
      }

      public String toString() {
         return "PositionBetween{t=" + this.elapsedTime() + ", [" + this.node0Index() + " / " + this.node1Index() + "] @ " + this.theta() + "}";
      }
   }
}
