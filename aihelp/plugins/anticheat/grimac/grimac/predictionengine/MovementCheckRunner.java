package ac.grim.grimac.predictionengine;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.impl.prediction.Phase;
import ac.grim.grimac.checks.impl.vehicle.VehicleC;
import ac.grim.grimac.checks.type.PositionCheck;
import ac.grim.grimac.manager.SetbackTeleportUtil;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.movementtick.MovementTickerCamel;
import ac.grim.grimac.predictionengine.movementtick.MovementTickerHappyGhast;
import ac.grim.grimac.predictionengine.movementtick.MovementTickerHorse;
import ac.grim.grimac.predictionengine.movementtick.MovementTickerNautilus;
import ac.grim.grimac.predictionengine.movementtick.MovementTickerPig;
import ac.grim.grimac.predictionengine.movementtick.MovementTickerPlayer;
import ac.grim.grimac.predictionengine.movementtick.MovementTickerStrider;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineNormal;
import ac.grim.grimac.predictionengine.predictions.rideable.PredictionEngineBoat;
import ac.grim.grimac.predictionengine.predictions.rideable.PredictionEngineRideableUtils;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.anticheat.update.PositionUpdate;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.SetBackData;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityCamel;
import ac.grim.grimac.utils.data.packetentity.PacketEntityHappyGhast;
import ac.grim.grimac.utils.data.packetentity.PacketEntityHorse;
import ac.grim.grimac.utils.data.packetentity.PacketEntityNautilus;
import ac.grim.grimac.utils.data.packetentity.PacketEntityRideable;
import ac.grim.grimac.utils.data.packetentity.PacketEntityTrackXRot;
import ac.grim.grimac.utils.enums.Pose;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.math.VectorUtils;
import ac.grim.grimac.utils.nmsutil.BoundingBoxSize;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import ac.grim.grimac.utils.nmsutil.Riptide;

public class MovementCheckRunner extends Check implements PositionCheck {
   public static double predictionNanos = 300000.0D;
   public static double longPredictionNanos = 300000.0D;
   private boolean allowSprintJumpingWithElytra = true;

   public MovementCheckRunner(GrimPlayer player) {
      super(player);
   }

   public void processAndCheckMovementPacket(PositionUpdate data) {
      if (this.player.getSetbackTeleportUtil().insideUnloadedChunk()) {
         boolean invalidVehicle = this.player.inVehicle() && (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_9) || this.player.getClientVersion().isOlderThan(ClientVersion.V_1_9));
         if (!invalidVehicle && !data.isTeleport()) {
            this.player.getSetbackTeleportUtil().executeNonSimulatingForceResync();
         }
      }

      long start = System.nanoTime();
      this.check(data);
      long length = System.nanoTime() - start;
      if (!this.player.disableGrim) {
         predictionNanos = predictionNanos * 499.0D / 500.0D + (double)length / 500.0D;
         longPredictionNanos = longPredictionNanos * 19999.0D / 20000.0D + (double)length / 20000.0D;
      }

   }

   private void handleTeleport(PositionUpdate update) {
      this.player.lastX = this.player.x;
      this.player.lastY = this.player.y;
      this.player.lastZ = this.player.z;
      if (!this.player.inVehicle()) {
         if (update.getTeleportData() == null) {
            this.player.clientVelocity.setX(0);
            this.player.clientVelocity.setY(0);
            this.player.clientVelocity.setZ(0);
            this.player.lastWasClimbing = 0.0D;
            this.player.canSwimHop = false;
         } else {
            SetBackData setback = update.getSetback();
            if (setback != null && setback.getVelocity() != null) {
               this.player.clientVelocity.setX(setback.getVelocity().getX());
               this.player.clientVelocity.setY(setback.getVelocity().getY());
               this.player.clientVelocity.setZ(setback.getVelocity().getZ());
            } else {
               update.getTeleportData().modifyVector(this.player, this.player.clientVelocity);
            }
         }
      }

      this.player.uncertaintyHandler.lastTeleportTicks.reset();
      this.player.checkManager.getExplosionHandler().forceExempt();
      this.player.checkManager.getKnockbackHandler().forceExempt();
      this.player.boundingBox = GetBoundingBox.getCollisionBoxForPlayer(this.player, this.player.x, this.player.y, this.player.z);
      PredictionComplete predictionComplete = new PredictionComplete(0.0D, update, true);
      this.player.getSetbackTeleportUtil().onPredictionComplete(predictionComplete);
      ((Phase)this.player.checkManager.getPostPredictionCheck(Phase.class)).onPredictionComplete(predictionComplete);
      this.player.uncertaintyHandler.lastHorizontalOffset = 0.0D;
      this.player.uncertaintyHandler.lastVerticalOffset = 0.0D;
   }

   private void check(PositionUpdate update) {
      if (update.isTeleport()) {
         this.handleTeleport(update);
      } else {
         ++this.player.movementPackets;
         this.player.onGround = update.isOnGround();
         boolean lavaBugFix;
         if (!this.player.isFlying && this.player.isSneaking && Collisions.isAboveGround(this.player)) {
            double posX = Math.max(0.05D, GrimMath.clamp(this.player.actualMovement.getX(), -16.0D, 16.0D) + 0.05D);
            double posZ = Math.max(0.05D, GrimMath.clamp(this.player.actualMovement.getZ(), -16.0D, 16.0D) + 0.05D);
            double negX = Math.min(-0.05D, GrimMath.clamp(this.player.actualMovement.getX(), -16.0D, 16.0D) - 0.05D);
            double negZ = Math.min(-0.05D, GrimMath.clamp(this.player.actualMovement.getZ(), -16.0D, 16.0D) - 0.05D);
            Vector3dm NE = Collisions.maybeBackOffFromEdge(new Vector3dm(posX, 0.0D, negZ), this.player, true);
            Vector3dm NW = Collisions.maybeBackOffFromEdge(new Vector3dm(negX, 0.0D, negZ), this.player, true);
            Vector3dm SE = Collisions.maybeBackOffFromEdge(new Vector3dm(posX, 0.0D, posZ), this.player, true);
            Vector3dm SW = Collisions.maybeBackOffFromEdge(new Vector3dm(negX, 0.0D, posZ), this.player, true);
            boolean isEast = NE.getX() != posX || SE.getX() != posX;
            boolean isWest = NW.getX() != negX || SW.getX() != negX;
            boolean isNorth = NE.getZ() != negZ || NW.getZ() != negZ;
            lavaBugFix = SE.getZ() != posZ || SW.getZ() != posZ;
            if (isEast) {
               this.player.uncertaintyHandler.lastStuckEast.reset();
            }

            if (isWest) {
               this.player.uncertaintyHandler.lastStuckWest.reset();
            }

            if (isNorth) {
               this.player.uncertaintyHandler.lastStuckNorth.reset();
            }

            if (lavaBugFix) {
               this.player.uncertaintyHandler.lastStuckSouth.reset();
            }

            if (isEast || isWest || lavaBugFix || isNorth) {
               this.player.uncertaintyHandler.stuckOnEdge.reset();
            }
         }

         this.player.compensatedWorld.tickPlayerInPistonPushingArea();
         this.player.compensatedEntities.tick();
         if (this.player.vehicleData.wasVehicleSwitch || this.player.vehicleData.lastDummy) {
            this.player.uncertaintyHandler.lastVehicleSwitch.reset();
         }

         if (this.player.vehicleData.lastDummy) {
            this.player.clientVelocity.multiply(0.98D);
         }

         PacketEntity riding = this.player.compensatedEntities.self.getRiding();
         if (this.player.vehicleData.wasVehicleSwitch || this.player.vehicleData.lastDummy) {
            update.setTeleport(true);
            this.player.vehicleData.lastDummy = false;
            this.player.vehicleData.wasVehicleSwitch = false;
            if (riding == null) {
               if ((new Vector3dm(this.player.lastX, this.player.lastY, this.player.lastZ)).distance(new Vector3dm(this.player.x, this.player.y, this.player.z)) > 3.0D) {
                  this.player.getSetbackTeleportUtil().executeForceResync();
               }

               this.handleTeleport(update);
               if (this.player.isClimbing) {
                  Vector3dm ladder = this.player.clientVelocity.clone().setY(0.2D);
                  PredictionEngineNormal.staticVectorEndOfTick(this.player, ladder);
                  this.player.lastWasClimbing = ladder.getY();
               }

               return;
            }

            SimpleCollisionBox interTruePositions = riding.getPossibleCollisionBoxes();
            float scale = (float)riding.getAttributeValue(Attributes.SCALE);
            float width = BoundingBoxSize.getWidth(this.player, riding) * scale;
            float height = BoundingBoxSize.getHeight(this.player, riding) * scale;
            interTruePositions.expand((double)(-width), 0.0D, (double)(-width));
            interTruePositions.expandMax(0.0D, (double)(-height), 0.0D);
            Vector3dm cutTo = VectorUtils.cutBoxToVector(this.player.x, this.player.y, this.player.z, interTruePositions);
            this.player.lastX = cutTo.getX();
            this.player.lastY = cutTo.getY();
            this.player.lastZ = cutTo.getZ();
            this.player.boundingBox = GetBoundingBox.getCollisionBoxForPlayer(this.player, this.player.lastX, this.player.lastY, this.player.lastZ);
         }

         if (this.player.isInBed != this.player.lastInBed) {
            update.setTeleport(true);
         }

         this.player.lastInBed = this.player.isInBed;
         if (!this.player.isInBed) {
            if (!this.player.inVehicle()) {
               this.player.speed = this.player.compensatedEntities.self.getAttributeValue(Attributes.MOVEMENT_SPEED);
               if (this.player.hasGravity != this.player.playerEntityHasGravity) {
                  this.player.pointThreeEstimator.updatePlayerGravity();
               }

               this.player.hasGravity = this.player.playerEntityHasGravity;
            }

            boolean clientClaimsRiptide;
            if (this.player.inVehicle()) {
               this.player.checkManager.getExplosionHandler().forceExempt();
               riding.setPositionRaw(this.player, new SimpleCollisionBox(this.player.x, this.player.y, this.player.z, this.player.x, this.player.y, this.player.z));
               if (riding instanceof PacketEntityTrackXRot) {
                  PacketEntityTrackXRot boat = (PacketEntityTrackXRot)riding;
                  boat.packetYaw = this.player.yaw;
                  boat.interpYaw = this.player.yaw;
                  boat.steps = 0;
               }

               if (this.player.hasGravity != riding.hasGravity) {
                  this.player.pointThreeEstimator.updatePlayerGravity();
               }

               this.player.hasGravity = riding.hasGravity;
               if (riding instanceof PacketEntityRideable) {
                  VehicleC vehicleC = (VehicleC)this.player.checkManager.getCheck(VehicleC.class);
                  ItemType requiredItem = riding.type == EntityTypes.PIG ? ItemTypes.CARROT_ON_A_STICK : ItemTypes.WARPED_FUNGUS_ON_A_STICK;
                  ItemStack mainHand = this.player.inventory.getHeldItem();
                  ItemStack offHand = this.player.inventory.getOffHand();
                  clientClaimsRiptide = mainHand.getType() == requiredItem;
                  boolean correctOffhand = offHand.getType() == requiredItem;
                  if (!clientClaimsRiptide && !correctOffhand) {
                     vehicleC.flagAndAlert();
                  } else {
                     vehicleC.reward();
                  }
               }
            }

            if (this.player.isFlying) {
               this.player.fallDistance = 0.0D;
               this.player.uncertaintyHandler.lastFlyingTicks.reset();
            }

            this.player.isClimbing = Collisions.onClimbable(this.player, this.player.lastX, this.player.lastY, this.player.lastZ);
            this.player.clientControlledVerticalCollision = Math.abs(this.player.y % 0.015625D) < 1.0E-5D;
            this.player.actualMovement = new Vector3dm(this.player.x - this.player.lastX, this.player.y - this.player.lastY, this.player.z - this.player.lastZ);
            if (this.player.isSprinting != this.player.lastSprinting) {
               this.player.compensatedEntities.hasSprintingAttributeEnabled = this.player.isSprinting;
            }

            this.player.lastJumping = this.player.isJumping;
            this.player.isJumping = this.player.packetStateData.knownInput.jump();
            boolean oldFlying = this.player.isFlying;
            boolean oldGliding = this.player.isGliding;
            boolean oldSprinting = this.player.isSprinting;
            boolean oldSneaking = this.player.isSneaking;
            GrimPlayer var10000;
            if (this.player.inVehicle()) {
               this.player.isFlying = false;
               this.player.isGliding = false;
               var10000 = this.player;
               var10000.isSprinting &= riding instanceof PacketEntityCamel;
               this.player.isSneaking = false;
               if (riding.type != EntityTypes.PIG && riding.type != EntityTypes.STRIDER) {
                  this.player.isClimbing = false;
               }
            }

            if (!this.player.inVehicle()) {
               var10000 = this.player;
               var10000.speed += this.player.compensatedEntities.hasSprintingAttributeEnabled ? this.player.speed * 0.30000001192092896D : 0.0D;
            }

            clientClaimsRiptide = this.player.packetStateData.tryingToRiptide;
            boolean isGlitchy;
            if (this.player.packetStateData.tryingToRiptide) {
               long currentTime = System.currentTimeMillis();
               isGlitchy = this.player.isInWaterOrRain();
               if (currentTime - this.player.packetStateData.lastRiptide < 450L || !isGlitchy) {
                  this.player.packetStateData.tryingToRiptide = false;
               }

               this.player.packetStateData.lastRiptide = currentTime;
            }

            SimpleCollisionBox steppingOnBB = GetBoundingBox.getCollisionBoxForPlayer(this.player, this.player.x, this.player.y, this.player.z).expand(this.player.getMovementThreshold()).offset(0.0D, -1.0D, 0.0D);
            Collisions.hasMaterial(this.player, steppingOnBB, (pair) -> {
               WrappedBlockState data = (WrappedBlockState)pair.first();
               if (data.getType() == StateTypes.SLIME_BLOCK && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8)) {
                  this.player.uncertaintyHandler.isSteppingOnSlime = true;
                  this.player.uncertaintyHandler.isSteppingOnBouncyBlock = true;
               }

               if (data.getType() == StateTypes.HONEY_BLOCK) {
                  if (this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_14) && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8)) {
                     this.player.uncertaintyHandler.isSteppingOnBouncyBlock = true;
                  }

                  this.player.uncertaintyHandler.isSteppingOnHoney = true;
               }

               if (BlockTags.BEDS.contains(data.getType()) && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_12)) {
                  this.player.uncertaintyHandler.isSteppingOnBouncyBlock = true;
               }

               if (BlockTags.ICE.contains(data.getType())) {
                  this.player.uncertaintyHandler.isSteppingOnIce = true;
               }

               if (data.getType() == StateTypes.BUBBLE_COLUMN && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13)) {
                  this.player.uncertaintyHandler.isSteppingNearBubbleColumn = true;
               }

               if (data.getType() == StateTypes.SCAFFOLDING) {
                  this.player.uncertaintyHandler.isSteppingNearScaffolding = true;
               }

               return false;
            });
            this.player.uncertaintyHandler.thisTickSlimeBlockUncertainty = this.player.uncertaintyHandler.nextTickSlimeBlockUncertainty;
            this.player.uncertaintyHandler.nextTickSlimeBlockUncertainty = 0.0D;
            SimpleCollisionBox expandedBB = GetBoundingBox.getBoundingBoxFromPosAndSize(this.player, this.player.lastX, this.player.lastY, this.player.lastZ, 0.001F, 0.001F);
            if (this.player.actualMovement.lengthSquared() < 2500.0D) {
               expandedBB.expandToAbsoluteCoordinates(this.player.x, this.player.y, this.player.z);
            }

            expandedBB.expand((double)(Pose.STANDING.width / 2.0F), 0.0D, (double)(Pose.STANDING.width / 2.0F));
            expandedBB.expandMax(0.0D, (double)Pose.STANDING.height, 0.0D);
            isGlitchy = this.player.uncertaintyHandler.isNearGlitchyBlock;
            this.player.uncertaintyHandler.isNearGlitchyBlock = this.player.getClientVersion().isOlderThan(ClientVersion.V_1_9) && Collisions.hasMaterial(this.player, expandedBB.copy().expand(0.2D), (checkData) -> {
               return BlockTags.ANVIL.contains(((WrappedBlockState)checkData.first()).getType()) || ((WrappedBlockState)checkData.first()).getType() == StateTypes.CHEST || ((WrappedBlockState)checkData.first()).getType() == StateTypes.TRAPPED_CHEST;
            });
            this.player.uncertaintyHandler.isOrWasNearGlitchyBlock = isGlitchy || this.player.uncertaintyHandler.isNearGlitchyBlock;
            this.player.uncertaintyHandler.checkForHardCollision();
            if (this.player.isFlying != this.player.wasFlying) {
               this.player.uncertaintyHandler.lastFlyingStatusChange.reset();
            }

            if (!this.player.inVehicle() && (Math.abs(this.player.x) == 2.9999999E7D || Math.abs(this.player.z) == 2.9999999E7D)) {
               this.player.uncertaintyHandler.lastThirtyMillionHardBorder.reset();
            }

            if (this.player.isFlying && this.player.getClientVersion().isOlderThan(ClientVersion.V_1_13) && this.player.compensatedWorld.containsLiquid(this.player.boundingBox)) {
               this.player.uncertaintyHandler.lastUnderwaterFlyingHack.reset();
            }

            boolean couldBeStuckSpeed = Collisions.checkStuckSpeed(this.player, this.player.getMovementThreshold());
            boolean couldLeaveStuckSpeed = this.player.isPointThree() && Collisions.checkStuckSpeed(this.player, -this.player.getMovementThreshold());
            this.player.uncertaintyHandler.claimingLeftStuckSpeed = !this.player.inVehicle() && this.player.stuckSpeedMultiplier.getX() < 1.0D && !couldLeaveStuckSpeed;
            if (couldBeStuckSpeed) {
               this.player.uncertaintyHandler.lastStuckSpeedMultiplier.reset();
            }

            this.player.startTickClientVel = this.player.clientVelocity;
            boolean wasChecked = false;
            if (!this.player.compensatedEntities.self.isDead && (riding == null || !riding.isDead)) {
               if (this.player.disableGrim || PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_8) && this.player.gamemode == GameMode.SPECTATOR || this.player.isFlying || this.player.isExemptElytra() && this.player.isGliding) {
                  this.player.predictedVelocity = new VectorData(this.player.actualMovement, VectorData.VectorType.Spectator);
                  this.player.clientVelocity = this.player.actualMovement.clone();
                  this.player.gravity = 0.0D;
                  this.player.friction = 0.91F;
                  PredictionEngineNormal.staticVectorEndOfTick(this.player, this.player.clientVelocity);
               } else if (riding == null) {
                  wasChecked = true;
                  this.player.depthStriderLevel = (float)this.player.compensatedEntities.self.getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY);
                  this.player.sneakingSpeedMultiplier = (float)this.player.compensatedEntities.self.getAttributeValue(Attributes.SNEAKING_SPEED);
                  this.player.verticalCollision = false;
                  if (this.player.lastOnGround && this.player.packetStateData.tryingToRiptide && !this.player.inVehicle()) {
                     Vector3dm pushingMovement = Collisions.collide(this.player, 0.0D, 1.1999999284744263D, 0.0D);
                     this.player.verticalCollision = pushingMovement.getY() != 1.1999999284744263D;
                     double currentY = this.player.clientVelocity.getY();
                     if (this.likelyGroundRiptide(pushingMovement)) {
                        this.player.uncertaintyHandler.thisTickSlimeBlockUncertainty = Math.abs(Riptide.getRiptideVelocity(this.player).getY()) + (currentY > 0.0D ? currentY : 0.0D);
                        this.player.uncertaintyHandler.nextTickSlimeBlockUncertainty = Math.abs(Riptide.getRiptideVelocity(this.player).getY()) + (currentY > 0.0D ? currentY : 0.0D);
                        this.player.lastOnGround = false;
                        var10000 = this.player;
                        var10000.lastY += pushingMovement.getY();
                        PlayerBaseTick.updatePlayerPose(this.player);
                        this.player.boundingBox = GetBoundingBox.getPlayerBoundingBox(this.player, this.player.lastX, this.player.lastY, this.player.lastZ);
                        this.player.actualMovement = new Vector3dm(this.player.x - this.player.lastX, this.player.y - this.player.lastY, this.player.z - this.player.lastZ);
                        this.player.couldSkipTick = true;
                        Collisions.handleInsideBlocks(this.player);
                     }
                  }

                  PlayerBaseTick.doBaseTick(this.player);
                  (new MovementTickerPlayer(this.player)).livingEntityAIStep();
                  PlayerBaseTick.updatePowderSnow(this.player);
                  PlayerBaseTick.updatePlayerPose(this.player);
               } else if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9) && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
                  wasChecked = true;
                  if (riding.isBoat) {
                     PlayerBaseTick.doBaseTick(this.player);
                     (new PredictionEngineBoat(this.player)).guessBestMovement(0.1F, this.player);
                  } else if (riding instanceof PacketEntityNautilus) {
                     PlayerBaseTick.doBaseTick(this.player);
                     (new MovementTickerNautilus(this.player)).livingEntityAIStep();
                  } else if (riding instanceof PacketEntityCamel) {
                     PlayerBaseTick.doBaseTick(this.player);
                     (new MovementTickerCamel(this.player)).livingEntityAIStep();
                  } else if (riding instanceof PacketEntityHappyGhast) {
                     PlayerBaseTick.doBaseTick(this.player);
                     (new MovementTickerHappyGhast(this.player)).livingEntityAIStep();
                  } else if (riding instanceof PacketEntityHorse) {
                     PlayerBaseTick.doBaseTick(this.player);
                     (new MovementTickerHorse(this.player)).livingEntityAIStep();
                  } else if (riding.type == EntityTypes.PIG) {
                     PlayerBaseTick.doBaseTick(this.player);
                     (new MovementTickerPig(this.player)).livingEntityAIStep();
                  } else if (riding.type == EntityTypes.STRIDER) {
                     PlayerBaseTick.doBaseTick(this.player);
                     (new MovementTickerStrider(this.player)).livingEntityAIStep();
                     MovementTickerStrider.floatStrider(this.player);
                     Collisions.handleInsideBlocks(this.player);
                  } else {
                     wasChecked = false;
                  }
               }
            } else {
               this.player.predictedVelocity = new VectorData(new Vector3dm(), VectorData.VectorType.Dead);
               this.player.clientVelocity = new Vector3dm();
            }

            double offset = this.player.predictedVelocity.vector.distance(this.player.actualMovement);
            offset = this.player.uncertaintyHandler.reduceOffset(offset);
            if (this.player.packetStateData.tryingToRiptide != clientClaimsRiptide) {
               this.player.getSetbackTeleportUtil().executeForceResync();
            }

            if (this.player.getSetbackTeleportUtil().getRequiredSetBack() != null && this.player.getSetbackTeleportUtil().getRequiredSetBack().getTicksComplete() == 1) {
               Vector3dm setbackVel = this.player.getSetbackTeleportUtil().getRequiredSetBack().getVelocity();
               if (this.player.predictedVelocity.isJump() && !this.player.wasTouchingLava && !this.player.wasTouchingWater && (setbackVel != null && setbackVel.getY() >= 0.0D || !Collisions.slowCouldPointThreeHitGround(this.player, this.player.lastX, this.player.lastY, this.player.lastZ))) {
                  this.player.getSetbackTeleportUtil().executeForceResync();
               }

               lavaBugFix = this.player.wasTouchingLava && this.player.predictedVelocity.isJump() && this.player.predictedVelocity.vector.getY() < 0.06D && this.player.predictedVelocity.vector.getY() > -0.02D;
               if (!this.player.predictedVelocity.isKnockback() && !lavaBugFix && this.player.getSetbackTeleportUtil().getRequiredSetBack().getVelocity() != null) {
                  this.player.getSetbackTeleportUtil().executeForceResync();
               }
            }

            if (this.player.getSetbackTeleportUtil().blockOffsets) {
               offset = 0.0D;
            }

            if (this.player.skippedTickInActualMovement || !wasChecked) {
               this.player.uncertaintyHandler.lastPointThree.reset();
            }

            this.player.checkManager.onPredictionFinish(new PredictionComplete(offset, update, wasChecked));
            this.player.wasLastPredictionCompleteChecked = wasChecked;
            if (this.player.platformPlayer != null && this.player.isGliding && this.player.predictedVelocity.isJump() && this.player.isSprinting && !this.allowSprintJumpingWithElytra) {
               SetbackTeleportUtil.SetbackPosWithVector lastKnownGoodPosition = this.player.getSetbackTeleportUtil().lastKnownGoodPosition;
               lastKnownGoodPosition.setVector(lastKnownGoodPosition.getVector().multiply(0.546D, 1.0D, 0.546D));
               this.player.getSetbackTeleportUtil().executeNonSimulatingSetback();
            }

            if (!wasChecked) {
               this.player.checkManager.getExplosionHandler().forceExempt();
               this.player.checkManager.getKnockbackHandler().forceExempt();
            }

            this.player.lastOnGround = this.player.onGround;
            this.player.lastSprinting = this.player.isSprinting;
            this.player.lastSprintingForSpeed = this.player.isSprinting;
            this.player.wasFlying = this.player.isFlying;
            this.player.wasGliding = this.player.isGliding;
            this.player.wasSwimming = this.player.isSwimming;
            this.player.wasSneaking = this.player.isSneaking;
            this.player.packetStateData.tryingToRiptide = false;
            if (this.player.inVehicle()) {
               this.player.isFlying = oldFlying;
               this.player.isGliding = oldGliding;
               this.player.isSprinting = oldSprinting;
               this.player.isSneaking = oldSneaking;
            }

            --this.player.riptideSpinAttackTicks;
            if (this.player.predictedVelocity.isTrident()) {
               this.player.riptideSpinAttackTicks = 20;
            }

            this.player.uncertaintyHandler.lastMovementWasZeroPointZeroThree = !this.player.inVehicle() && this.player.skippedTickInActualMovement;
            this.player.uncertaintyHandler.lastMovementWasUnknown003VectorReset = !this.player.inVehicle() && this.player.couldSkipTick && this.player.predictedVelocity.isKnockback() && !this.player.predictedVelocity.isSetbackKb(this.player);
            this.player.couldSkipTick = false;
            this.player.uncertaintyHandler.wasZeroPointThreeVertically = !this.player.inVehicle() && (this.player.uncertaintyHandler.lastMovementWasZeroPointZeroThree && this.player.pointThreeEstimator.controlsVerticalMovement() || !this.player.pointThreeEstimator.canPredictNextVerticalMovement() || !this.player.pointThreeEstimator.isWasAlwaysCertain());
            this.player.uncertaintyHandler.lastPacketWasGroundPacket = this.player.uncertaintyHandler.onGroundUncertain;
            this.player.uncertaintyHandler.onGroundUncertain = false;
            PredictionEngineRideableUtils.applyPendingJumps(this.player);
            this.player.vehicleData.vehicleForward = (float)Math.min(0.98D, Math.max(-0.98D, (double)this.player.vehicleData.nextVehicleForward));
            this.player.vehicleData.vehicleHorizontal = (float)Math.min(0.98D, Math.max(-0.98D, (double)this.player.vehicleData.nextVehicleHorizontal));
            this.player.dashableEntities.tick();
            this.player.minAttackSlow = 0;
            this.player.maxAttackSlow = 0;
            this.player.likelyKB = null;
            this.player.firstBreadKB = null;
            this.player.firstBreadExplosion = null;
            this.player.likelyExplosions = null;
            this.player.trigHandler.setOffset(offset);
            this.player.pointThreeEstimator.endOfTickTick();
         }
      }
   }

   private boolean likelyGroundRiptide(Vector3dm pushingMovement) {
      double riptideYResult = Riptide.getRiptideVelocity(this.player).getY();
      double riptideDiffToBase = Math.abs(this.player.actualMovement.getY() - riptideYResult);
      double riptideDiffToGround = Math.abs(this.player.actualMovement.getY() - riptideYResult - pushingMovement.getY());
      return riptideDiffToGround < riptideDiffToBase;
   }

   public void onReload(ConfigManager config) {
      this.allowSprintJumpingWithElytra = config.getBooleanElse("exploit.allow-sprint-jumping-when-using-elytra", true);
   }
}
