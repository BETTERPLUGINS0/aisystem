package com.bergerkiller.bukkit.tc.controller.components;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.status.TrainStatus;
import com.bergerkiller.bukkit.tc.controller.status.TrainStatusProvider;
import com.bergerkiller.bukkit.tc.events.MutexZoneConflictEvent;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZone;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZoneCacheWorld;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZoneSlot;
import com.bergerkiller.bukkit.tc.utils.ForwardChunkArea;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ObstacleTracker implements TrainStatusProvider {
   private final MinecartGroup group;
   private double waitDistanceLastSpeedLimit = Double.MAX_VALUE;
   private double waitDistanceLastTrainSpeed = Double.MAX_VALUE;
   private int waitRemainingTicks = Integer.MAX_VALUE;
   private ObstacleTracker.ObstacleSpeedLimit lastObstacleSpeedLimit;
   private List<MutexZone> enteredMutexZones;
   private int tickCounter;

   public ObstacleTracker(MinecartGroup group) {
      this.lastObstacleSpeedLimit = ObstacleTracker.ObstacleSpeedLimit.NONE;
      this.enteredMutexZones = Collections.emptyList();
      this.tickCounter = 0;
      this.group = group;
   }

   public double getSpeedLimit() {
      return this.waitDistanceLastSpeedLimit;
   }

   public int getTickCounter() {
      return this.tickCounter;
   }

   public void update(double trainSpeed) {
      TrainProperties properties = this.group.getProperties();
      ++this.tickCounter;
      Iterator var4 = this.group.head().railLookup().getMutexZones().getNewZones().iterator();

      while(var4.hasNext()) {
         MutexZone newMutexZone = (MutexZone)var4.next();
         this.hardEnterNewMutexZoneIfInside(newMutexZone);
      }

      double searchAheadDistance = Math.max(1.0D, properties.getSpeedLimit() + 0.5D);
      double baseSpeedLimitThisTick;
      double speedLimitLastTick;
      if (properties.getWaitDeceleration() > 0.0D) {
         baseSpeedLimitThisTick = this.waitDistanceLastSpeedLimit == Double.MAX_VALUE ? properties.getSpeedLimit() : this.waitDistanceLastSpeedLimit;
         speedLimitLastTick = Math.min(trainSpeed, baseSpeedLimitThisTick);
         searchAheadDistance += 0.5D * speedLimitLastTick * speedLimitLastTick / properties.getWaitDeceleration();
      }

      speedLimitLastTick = this.waitDistanceLastSpeedLimit == Double.MAX_VALUE ? properties.getSpeedLimit() : this.waitDistanceLastSpeedLimit;
      double acceleration = this.waitDistanceLastTrainSpeed == Double.MAX_VALUE ? trainSpeed : this.waitDistanceLastTrainSpeed;
      baseSpeedLimitThisTick = Math.min(speedLimitLastTick, acceleration);
      this.waitDistanceLastTrainSpeed = trainSpeed;
      boolean checkTrains = properties.getWaitDistance() > 0.0D;
      ObstacleTracker.ObstacleSpeedLimit newDesiredSpeed = this.getDesiredSpeedLimit(searchAheadDistance, properties.getWaitDeceleration(), checkTrains, true, properties.getWaitDistance());
      if (this.waitDistanceLastSpeedLimit <= 1.0E-6D && newDesiredSpeed.speed <= 1.0E-6D) {
         this.waitRemainingTicks = 0;
         this.waitDistanceLastSpeedLimit = newDesiredSpeed.speed;
      } else {
         if (this.waitRemainingTicks != Integer.MAX_VALUE) {
            acceleration = properties.getWaitDelay();
            if (!(acceleration <= 0.0D)) {
               if (++this.waitRemainingTicks >= MathUtil.ceil(acceleration * 20.0D)) {
                  this.waitRemainingTicks = Integer.MAX_VALUE;
               }

               this.waitDistanceLastSpeedLimit = 0.0D;
               return;
            }

            this.waitRemainingTicks = Integer.MAX_VALUE;
         }

         if (newDesiredSpeed.speed >= properties.getSpeedLimit()) {
            if (this.waitDistanceLastSpeedLimit >= newDesiredSpeed.speed) {
               this.waitDistanceLastSpeedLimit = Double.MAX_VALUE;
            }

            if (this.waitDistanceLastSpeedLimit != Double.MAX_VALUE) {
               acceleration = properties.getWaitAcceleration();
               if (acceleration > 0.0D) {
                  this.waitDistanceLastSpeedLimit += acceleration;
                  if (this.waitDistanceLastSpeedLimit >= properties.getSpeedLimit()) {
                     this.waitDistanceLastSpeedLimit = Double.MAX_VALUE;
                  }
               } else {
                  this.waitDistanceLastSpeedLimit = Double.MAX_VALUE;
               }
            }

         } else {
            if (this.waitDistanceLastSpeedLimit == Double.MAX_VALUE) {
               this.waitDistanceLastSpeedLimit = properties.getSpeedLimit();
            }

            acceleration = newDesiredSpeed.speed - this.waitDistanceLastSpeedLimit;
            double deceleration;
            if (acceleration >= 0.0D) {
               deceleration = properties.getWaitAcceleration();
               if (!(deceleration <= 0.0D) && !(deceleration >= acceleration)) {
                  this.waitDistanceLastSpeedLimit += deceleration;
               } else {
                  this.waitDistanceLastSpeedLimit = newDesiredSpeed.speed;
               }
            } else {
               deceleration = properties.getWaitDeceleration();
               if (!(deceleration <= 0.0D) && !(deceleration >= -acceleration) && !newDesiredSpeed.instant) {
                  if (newDesiredSpeed.speed > baseSpeedLimitThisTick) {
                     this.waitDistanceLastSpeedLimit = newDesiredSpeed.speed;
                  } else {
                     this.waitDistanceLastSpeedLimit = baseSpeedLimitThisTick - deceleration;
                  }
               } else {
                  this.waitDistanceLastSpeedLimit = newDesiredSpeed.speed;
               }
            }

         }
      }
   }

   private void hardEnterNewMutexZoneIfInside(MutexZone newMutexZone) {
      boolean isNearby = false;
      Iterator var3 = this.group.iterator();

      while(var3.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var3.next();
         IntVector3 blockPos = member.getRailTracker().getState().positionOfflineBlock().getPosition();
         int radius = (int)(3.0D * (double)((CommonMinecart)member.getEntity()).getWidth());
         if (newMutexZone.isNearby(blockPos, radius)) {
            isNearby = true;
            break;
         }
      }

      if (isNearby) {
         List<RailTracker.TrackedRail> rails = this.group.getRailTracker().getRailInformation();
         if (!rails.isEmpty()) {
            MutexZone[] zones = new MutexZone[]{newMutexZone};
            RailPath.Position firstPosition = ((RailTracker.TrackedRail)rails.get(0)).state.position();
            MutexZoneCacheWorld.MovingPoint movingPoint = new MutexZoneCacheWorld.MovingPoint((cx, cz) -> {
               return zones;
            }, MathUtil.toChunk(firstPosition.posX), MathUtil.toChunk(firstPosition.posZ));
            boolean isInsideZone = false;
            Iterator var8 = rails.iterator();

            while(var8.hasNext()) {
               RailTracker.TrackedRail rail = (RailTracker.TrackedRail)var8.next();
               if (!rail.state.railPiece().isNone()) {
                  RailPath path = rail.getPath();
                  if (!path.isEmpty()) {
                     RailPath.Position start = path.getStartPosition();
                     RailPath.Position end = path.getEndPosition();
                     start.makeAbsolute(rail.state.railBlock());
                     end.makeAbsolute(rail.state.railBlock());
                     MutexZoneCacheWorld.MutexZoneResult result = movingPoint.get(start, end);
                     if (result != null && result.zone == newMutexZone && result.distance <= 0.0D) {
                        isInsideZone = true;
                        break;
                     }
                  }
               }
            }

            if (isInsideZone) {
               newMutexZone.onUsed(this.group);
               MutexZoneSlot.LoadedEnteredGroup entered = newMutexZone.slot.track(this.group, 0.0D);
               Iterator var19 = rails.iterator();

               while(var19.hasNext()) {
                  RailTracker.TrackedRail rail = (RailTracker.TrackedRail)var19.next();
                  if (!rail.state.railPiece().isNone()) {
                     entered.enter(newMutexZone.type, rail.state.railPiece().blockPosition(), true);
                  }
               }

            }
         }
      }
   }

   public List<TrainStatus> getStatusInfo() {
      if (!this.lastObstacleSpeedLimit.hasLimit() && this.enteredMutexZones.isEmpty()) {
         return Collections.emptyList();
      } else {
         List<TrainStatus> statuses = new ArrayList();
         if (!this.enteredMutexZones.isEmpty()) {
            IdentityHashMap<MutexZoneSlot, List<MutexZone>> zones = new IdentityHashMap();
            Iterator var3 = this.enteredMutexZones.iterator();

            while(var3.hasNext()) {
               MutexZone zone = (MutexZone)var3.next();
               zones.compute(zone.slot, (s, curr_zones) -> {
                  ArrayList<MutexZone> newZones = new ArrayList();
                  if (curr_zones != null) {
                     newZones.addAll(curr_zones);
                  }

                  newZones.add(zone);
                  return newZones;
               });
            }

            var3 = zones.entrySet().iterator();

            while(var3.hasNext()) {
               Entry<MutexZoneSlot, List<MutexZone>> e = (Entry)var3.next();
               MutexZoneSlot.LoadedEnteredGroup entered = ((MutexZoneSlot)e.getKey()).findEntered(this.group);
               statuses.add(new TrainStatus.EnteredMutexZone((MutexZoneSlot)e.getKey(), (List)e.getValue(), entered));
            }
         }

         if (this.lastObstacleSpeedLimit.hasLimit()) {
            statuses.add(this.lastObstacleSpeedLimit.getStatus());
         } else if (this.waitRemainingTicks != Integer.MAX_VALUE) {
            double remaining = this.group.getProperties().getWaitDelay() - (double)this.waitRemainingTicks * 0.05D;
            statuses.add(new TrainStatus.WaitingForDelay(remaining));
         }

         return statuses;
      }
   }

   private ObstacleTracker.ObstacleSpeedLimit getDesiredSpeedLimit(double searchAheadDistance, double deceleration, boolean checkTrains, boolean checkRailObstacles, double trainDistance) {
      ObstacleTracker.ObstacleFinder finder = new ObstacleTracker.ObstacleFinder(Math.min(2000.0D, searchAheadDistance), checkTrains, checkRailObstacles, trainDistance);
      List<ObstacleTracker.Obstacle> obstacles = finder.search();
      this.enteredMutexZones = finder.enteredMutexZones;
      return this.lastObstacleSpeedLimit = minimumSpeedLimit(obstacles, deceleration);
   }

   public List<ObstacleTracker.Obstacle> findObstaclesAhead(double distance, boolean checkTrains, boolean checkRailObstacles, double trainDistance) {
      return (new ObstacleTracker.ObstacleFinder(distance, checkTrains, checkRailObstacles, trainDistance)).search();
   }

   public static ObstacleTracker.ObstacleSpeedLimit minimumSpeedLimit(Iterable<ObstacleTracker.Obstacle> obstacles, double deceleration) {
      ObstacleTracker.ObstacleSpeedLimit min = ObstacleTracker.ObstacleSpeedLimit.NONE;
      Iterator var4 = obstacles.iterator();

      while(var4.hasNext()) {
         ObstacleTracker.Obstacle obstacle = (ObstacleTracker.Obstacle)var4.next();
         ObstacleTracker.ObstacleSpeedLimit limit = obstacle.findSpeedLimit(deceleration);
         if (limit.speed < min.speed) {
            min = limit;
         }
      }

      return min;
   }

   public static ObstacleTracker.ObstacleSpeedLimit minimumSpeedLimit(Iterable<ObstacleTracker.ObstacleSpeedLimit> limits) {
      ObstacleTracker.ObstacleSpeedLimit min = ObstacleTracker.ObstacleSpeedLimit.NONE;
      Iterator var2 = limits.iterator();

      while(var2.hasNext()) {
         ObstacleTracker.ObstacleSpeedLimit limit = (ObstacleTracker.ObstacleSpeedLimit)var2.next();
         if (limit.speed < min.speed) {
            min = limit;
         }
      }

      return min;
   }

   public static class ObstacleSpeedLimit {
      public static final ObstacleTracker.ObstacleSpeedLimit NONE = new ObstacleTracker.ObstacleSpeedLimit((ObstacleTracker.Obstacle)null, Double.MAX_VALUE, false);
      public final ObstacleTracker.Obstacle obstacle;
      public final double speed;
      public final boolean instant;

      public ObstacleSpeedLimit(ObstacleTracker.Obstacle obstacle, double speed, boolean instant) {
         this.obstacle = obstacle;
         this.speed = speed;
         this.instant = instant;
      }

      public TrainStatus getStatus() {
         return this.obstacle.createStatus(this);
      }

      public boolean hasLimit() {
         return this.speed != Double.MAX_VALUE;
      }

      public boolean isStopped() {
         return this.instant && this.speed <= 0.0D;
      }

      public String toString() {
         return this.obstacle == null ? "{NONE}" : "{speed=" + this.speed + ", instant=" + this.instant + ", obstacle=" + this.obstacle.getClass().getSimpleName() + "}";
      }
   }

   private class ObstacleFinder {
      final double distance;
      final boolean checkTrains;
      final boolean checkRailObstacles;
      final double trainDistance;
      final double selfCartOffset;
      double waitDistance;
      final double mutexHardDistance;
      final double mutexSoftDistance;
      final double checkDistance;
      double closestHardRailObstacle = Double.MAX_VALUE;
      double lastRailSpeedLimit = Double.MAX_VALUE;
      MutexZone currentMutex = null;
      MutexZoneSlot.LoadedEnteredGroup currentMutexGroup = null;
      boolean currentMutexHard = false;
      double currentMutexSpacing = 0.0D;
      public List<MutexZone> enteredMutexZones = Collections.emptyList();
      List<ObstacleTracker.Obstacle> obstacles = new ArrayList();

      public ObstacleFinder(double distance, boolean checkTrains, boolean checkRailObstacles, double trainDistance) {
         this.distance = distance;
         this.checkTrains = checkTrains;
         this.checkRailObstacles = checkRailObstacles;
         this.trainDistance = trainDistance;
         this.selfCartOffset = 0.5D * (double)((CommonMinecart)ObstacleTracker.this.group.head().getEntity()).getWidth();
         this.waitDistance = distance + trainDistance;
         this.mutexHardDistance = 0.0D;
         this.mutexSoftDistance = 2.0D + distance;
         this.checkDistance = this.selfCartOffset + Math.max(this.mutexSoftDistance, this.waitDistance) + 1.0D;
      }

      public List<ObstacleTracker.Obstacle> search() {
         if (ObstacleTracker.this.group.isEmpty()) {
            ObstacleTracker.this.group.getChunkArea().getForwardChunkArea().reset();
            return Collections.emptyList();
         } else {
            ForwardChunkArea forwardChunks = null;
            if (ObstacleTracker.this.group.getProperties().isKeepingChunksLoaded()) {
               forwardChunks = ObstacleTracker.this.group.getChunkArea().getForwardChunkArea();
               forwardChunks.begin(ObstacleTracker.this.group.getWorld());
            } else {
               ObstacleTracker.this.group.getChunkArea().getForwardChunkArea().reset();
            }

            MutexZoneCacheWorld.MovingPoint mutexZones = ObstacleTracker.this.group.head().railLookup().getMutexZones().track(((CommonMinecart)ObstacleTracker.this.group.head().getEntity()).loc.block());
            if (!(this.distance <= 0.0D) || !(this.trainDistance <= 0.0D) || this.checkRailObstacles && mutexZones.isNear()) {
               RailState startState = ObstacleTracker.this.group.head().discoverRail();
               startState.setMember((MinecartMember)null);
               TrackWalkingPoint iter = new TrackWalkingPoint(startState);
               if (ObstacleTracker.this.group.getProperties().isWaitPredicted()) {
                  iter.setFollowPredictedPath(ObstacleTracker.this.group.head());
               }

               double distanceFromFront;
               label203:
               while((iter.movedTotal <= this.checkDistance + this.currentMutexSpacing || iter.getPredictedRemainingBlockDistance() > 0.0D) && iter.moveFull()) {
                  distanceFromFront = iter.movedTotal - this.selfCartOffset;
                  if (forwardChunks != null) {
                     forwardChunks.addBlock(iter.state.railBlock());
                  }

                  double distanceToMember;
                  if (this.checkRailObstacles) {
                     MutexZone prevMutex = this.currentMutex;
                     if (this.currentMutex != null && !this.currentMutex.containsBlock(iter.state.positionOfflineBlock().getPosition())) {
                        this.currentMutex = null;
                        this.currentMutexSpacing = 0.0D;
                     }

                     boolean checkForNewHardObstacles = distanceFromFront < this.closestHardRailObstacle;
                     if (checkForNewHardObstacles) {
                        double railSpeedLimit = iter.getPredictedSpeedLimit();
                        if (railSpeedLimit < this.lastRailSpeedLimit) {
                           this.lastRailSpeedLimit = railSpeedLimit;
                           this.obstacles.add(new ObstacleTracker.RailObstacle(distanceFromFront, railSpeedLimit, iter.state.railPiece()));
                           if (railSpeedLimit <= 0.0D) {
                              this.closestHardRailObstacle = distanceFromFront;
                              checkForNewHardObstacles = false;
                           }
                        }
                     }

                     if (this.currentMutex == null) {
                        boolean checkForNewMutexes = checkForNewHardObstacles && distanceFromFront < this.mutexSoftDistance;
                        if (prevMutex != null || checkForNewMutexes) {
                           MutexZoneCacheWorld.MutexZoneResult newMutexResult = mutexZones.get(iter);
                           if (newMutexResult != null) {
                              distanceToMember = distanceFromFront + newMutexResult.distance;
                              boolean accept;
                              if (prevMutex != null && prevMutex.slot == newMutexResult.zone.slot) {
                                 accept = true;
                              } else {
                                 accept = checkForNewMutexes && distanceToMember < this.mutexSoftDistance;
                              }

                              if (accept) {
                                 newMutexResult.zone.onUsed(ObstacleTracker.this.group);
                                 this.currentMutex = newMutexResult.zone;
                                 this.currentMutexSpacing = this.currentMutex.getSpacing(ObstacleTracker.this.group);
                                 this.currentMutexGroup = newMutexResult.zone.slot.track(ObstacleTracker.this.group, distanceToMember);
                                 this.currentMutexHard = this.currentMutexGroup.distanceToMutex <= this.mutexHardDistance;
                              }
                           }
                        }
                     }

                     if (this.currentMutex != null) {
                        this.updateCurrentMutex(iter);
                     }
                  }

                  if (this.checkTrains) {
                     Location state_position = null;
                     Location member_position = null;
                     Iterator var20 = iter.state.railPiece().members().iterator();

                     while(true) {
                        while(true) {
                           MinecartMember member;
                           Vector delta;
                           do {
                              do {
                                 do {
                                    do {
                                       if (!var20.hasNext()) {
                                          continue label203;
                                       }

                                       member = (MinecartMember)var20.next();
                                    } while(member.isUnloaded());
                                 } while(((CommonMinecart)member.getEntity()).isRemoved());
                              } while(member.getGroup() == ObstacleTracker.this.group);

                              if (state_position == null) {
                                 state_position = iter.state.positionLocation();
                              }

                              if (member_position == null) {
                                 member_position = ((CommonMinecart)member.getEntity()).getLocation();
                              } else {
                                 ((CommonMinecart)member.getEntity()).getLocation(member_position);
                              }

                              if (iter.movedTotal != 0.0D) {
                                 break;
                              }

                              delta = new Vector(member_position.getX() - state_position.getX(), member_position.getY() - state_position.getY(), member_position.getZ() - state_position.getZ());
                           } while(delta.dot(iter.state.motionVector()) < 0.0D);

                           distanceToMember = member_position.distance(state_position) - (double)((CommonMinecart)member.getEntity()).getWidth() * 0.5D;
                           Vector member_velocity = ((CommonMinecart)member.getEntity()).getVelocity();
                           double speedAhead = Math.min(member_velocity.length(), ((CommonMinecart)member.getEntity()).getMaxSpeed());
                           if (speedAhead < 0.0D) {
                              speedAhead = 0.0D;
                           }

                           if (speedAhead > 1.0E-6D && iter.state.position().motDot(member_velocity) < 0.0D) {
                              this.obstacles.add(new ObstacleTracker.TrainObstacle(distanceFromFront + distanceToMember, this.trainDistance, 0.0D, member));
                           } else {
                              this.obstacles.add(new ObstacleTracker.TrainObstacle(distanceFromFront + distanceToMember, this.trainDistance, speedAhead, member));
                           }
                        }
                     }
                  }
               }

               if (this.currentMutex != null) {
                  distanceFromFront = iter.movedTotal + 64.0D;

                  while(!this.currentMutexGroup.isOccupiedFully() && iter.moveFull()) {
                     if (iter.movedTotal >= distanceFromFront) {
                        distanceFromFront = Double.MAX_VALUE;
                        iter.setLoopFilter(true);
                     }

                     if (forwardChunks != null) {
                        forwardChunks.addBlock(iter.state.railBlock());
                     }

                     IntVector3 currBlockPos = iter.state.positionOfflineBlock().getPosition();
                     if (!this.currentMutex.containsBlock(currBlockPos)) {
                        MutexZoneCacheWorld.MutexZoneResult otherMutex = mutexZones.get(iter);
                        if (otherMutex == null || otherMutex.zone.slot != this.currentMutex.slot) {
                           break;
                        }

                        this.currentMutex = otherMutex.zone;
                        this.currentMutexSpacing = this.currentMutex.getSpacing(ObstacleTracker.this.group);
                        otherMutex.zone.onUsed(ObstacleTracker.this.group);
                     }

                     if (!this.updateCurrentMutex(iter)) {
                        break;
                     }
                  }
               }

               return this.obstacles;
            } else {
               return Collections.emptyList();
            }
         }
      }

      private boolean updateCurrentMutex(TrackWalkingPoint iter) {
         MutexZoneSlot.EnterResult result = this.currentMutexGroup.enter(this.currentMutex.type, iter.state.railPiece().blockPosition(), this.currentMutexHard);
         if (!this.enteredMutexZones.contains(this.currentMutex)) {
            if (this.enteredMutexZones.isEmpty()) {
               this.enteredMutexZones = new ArrayList();
            }

            this.enteredMutexZones.add(this.currentMutex);
         }

         double currentMutexDistance = this.currentMutexGroup.distanceToMutex - this.currentMutexSpacing;
         if (result.isOccupied()) {
            if (currentMutexDistance < this.closestHardRailObstacle) {
               this.closestHardRailObstacle = currentMutexDistance;
               this.obstacles.add(new ObstacleTracker.MutexZoneObstacle(currentMutexDistance, 0.0D, this.currentMutex));
            }

            if (result == MutexZoneSlot.EnterResult.OCCUPIED_DISCOVER) {
               return true;
            } else {
               this.currentMutex = null;
               this.currentMutexGroup = null;
               this.currentMutexSpacing = 0.0D;
               return false;
            }
         } else if (result.isConflict()) {
            if (result == MutexZoneSlot.EnterResult.CONFLICT) {
               MutexZoneConflictEvent conflict = this.currentMutexGroup.getConflict();
               if (TCConfig.logMutexConflicts) {
                  Logger l = ObstacleTracker.this.group.getTrainCarts().getLogger();
                  l.log(Level.WARNING, "[Mutex] Train '" + ObstacleTracker.this.group.getProperties().getTrainName() + "' is in violation inside mutex '" + conflict.getMutexZoneSlot().getNameWithoutWorldUUID() + "' crossing train '" + conflict.getGroupCrossed().getProperties().getTrainName() + "' at rail " + conflict.getRailPosition());
               }

               CommonUtil.callEvent(conflict);
            }

            return true;
         } else {
            return result == MutexZoneSlot.EnterResult.SUCCESS || result != MutexZoneSlot.EnterResult.IGNORED;
         }
      }
   }

   public abstract static class Obstacle {
      public final double distance;
      public final double speed;

      public Obstacle(double distance, double speed) {
         this.distance = distance;
         this.speed = speed;
      }

      public boolean isObstacleMoving() {
         return false;
      }

      protected abstract TrainStatus createStatus(ObstacleTracker.ObstacleSpeedLimit var1);

      public ObstacleTracker.ObstacleSpeedLimit findSpeedLimit(double deceleration) {
         if (this.distance > -1.0E-6D && this.distance < 1.0E-6D) {
            return new ObstacleTracker.ObstacleSpeedLimit(this, Math.max(0.0D, this.speed), true);
         } else if (this.distance <= 0.0D) {
            return this.isObstacleMoving() ? new ObstacleTracker.ObstacleSpeedLimit(this, Math.max(0.0D, this.speed + this.distance), true) : new ObstacleTracker.ObstacleSpeedLimit(this, Math.max(0.0D, this.speed), true);
         } else if (!(deceleration <= 0.0D) && deceleration != Double.MAX_VALUE) {
            double startSpeed = Math.sqrt(2.0D * deceleration * this.distance);

            int numSlowdownTicks;
            for(numSlowdownTicks = MathUtil.ceil(startSpeed / deceleration); (double)((numSlowdownTicks + 1) * numSlowdownTicks) * 0.5D * deceleration > this.distance; --numSlowdownTicks) {
            }

            if (numSlowdownTicks == 0) {
               startSpeed = this.distance + this.speed;
            } else {
               startSpeed = (double)numSlowdownTicks * deceleration + this.speed;
            }

            return new ObstacleTracker.ObstacleSpeedLimit(this, Math.max(0.0D, startSpeed), false);
         } else {
            return new ObstacleTracker.ObstacleSpeedLimit(this, Math.max(0.0D, this.speed + this.distance), true);
         }
      }
   }

   public static class RailObstacle extends ObstacleTracker.Obstacle {
      public final RailPiece rail;

      public RailObstacle(double distance, double speed, RailPiece rail) {
         super(distance, speed);
         this.rail = rail;
      }

      protected TrainStatus createStatus(ObstacleTracker.ObstacleSpeedLimit speedLimit) {
         return (TrainStatus)(speedLimit.isStopped() ? new TrainStatus.WaitingAtRailBlock(this.rail) : new TrainStatus.ApproachingRailSpeedTrap(this.rail, this.distance, this.speed));
      }
   }

   public static class MutexZoneObstacle extends ObstacleTracker.Obstacle {
      public final MutexZone zone;

      public MutexZoneObstacle(double distance, double speed, MutexZone zone) {
         super(distance, speed);
         this.zone = zone;
      }

      protected TrainStatus createStatus(ObstacleTracker.ObstacleSpeedLimit speedLimit) {
         return (TrainStatus)(speedLimit.isStopped() ? new TrainStatus.WaitingForMutexZone(this.zone) : new TrainStatus.ApproachingMutexZone(this.zone, this.distance, this.speed));
      }
   }

   public static class TrainObstacle extends ObstacleTracker.Obstacle {
      public final double fullDistance;
      public final MinecartMember<?> member;

      public TrainObstacle(double fullDistance, double spaceDistance, double speed, MinecartMember<?> member) {
         super(fullDistance - spaceDistance, speed);
         this.fullDistance = fullDistance;
         this.member = member;
      }

      public boolean isObstacleMoving() {
         return true;
      }

      protected TrainStatus createStatus(ObstacleTracker.ObstacleSpeedLimit speedLimit) {
         return (TrainStatus)(speedLimit.isStopped() ? new TrainStatus.WaitingForTrain(this.member, this.fullDistance) : new TrainStatus.FollowingTrain(this.member, this.fullDistance, speedLimit.speed));
      }
   }
}
