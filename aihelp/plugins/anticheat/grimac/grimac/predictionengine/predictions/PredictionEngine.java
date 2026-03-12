package ac.grim.grimac.predictionengine.predictions;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.SneakingEstimator;
import ac.grim.grimac.predictionengine.movementtick.MovementTickerPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUseEffects;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.KnownInput;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vec2;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.math.VectorUtils;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import ac.grim.grimac.utils.nmsutil.JumpPower;
import ac.grim.grimac.utils.nmsutil.Riptide;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PredictionEngine {
   private static final boolean USE_EFFECTS_COMPONENT_EXISTS;

   public static Vector3dm clampMovementToHardBorder(GrimPlayer player, Vector3dm outputVel) {
      return outputVel;
   }

   public static Vector3dm transformInputsToVector(GrimPlayer player, Vector3dm theoreticalInput) {
      if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_5)) {
         Vec2 moveVector = (new Vec2((float)theoreticalInput.getX(), (float)theoreticalInput.getZ())).normalized();
         Vec2 input = modifyInput(player, moveVector);
         return new Vector3dm(input.x(), 0.0F, input.y());
      } else {
         float bestPossibleX;
         float bestPossibleZ;
         if (player.isSlowMovement) {
            bestPossibleX = (float)(theoreticalInput.getX() * (double)player.sneakingSpeedMultiplier);
            bestPossibleZ = (float)(theoreticalInput.getZ() * (double)player.sneakingSpeedMultiplier);
         } else {
            bestPossibleX = Math.min(Math.max(-1.0F, (float)Math.round(theoreticalInput.getX())), 1.0F);
            bestPossibleZ = Math.min(Math.max(-1.0F, (float)Math.round(theoreticalInput.getZ())), 1.0F);
         }

         if (player.packetStateData.isSlowedByUsingItem()) {
            bestPossibleX *= 0.2F;
            bestPossibleZ *= 0.2F;
         }

         Vector3dm inputVector = new Vector3dm(bestPossibleX, 0.0F, bestPossibleZ);
         inputVector.multiply(0.98F);
         inputVector = new Vector3dm((float)inputVector.getX(), (float)inputVector.getY(), (float)inputVector.getZ());
         if (inputVector.lengthSquared() > 1.0D) {
            double d0 = Math.sqrt(inputVector.getX() * inputVector.getX() + inputVector.getY() * inputVector.getY() + inputVector.getZ() * inputVector.getZ());
            inputVector = new Vector3dm(inputVector.getX() / d0, inputVector.getY() / d0, inputVector.getZ() / d0);
         }

         return inputVector;
      }
   }

   public static Vec2 modifyInput(GrimPlayer player, Vec2 moveVector) {
      if (moveVector.lengthSquared() == 0.0F) {
         return moveVector;
      } else {
         Vec2 input = moveVector.scale(0.98F);
         if (player.packetStateData.isSlowedByUsingItem() && !player.inVehicle()) {
            input = input.scale(getItemUseSpeedMultiplier(player));
         }

         if (player.isSlowMovement) {
            input = input.scale(player.sneakingSpeedMultiplier);
         }

         return modifyInputSpeedForSquareMovement(input);
      }
   }

   private static Vec2 modifyInputSpeedForSquareMovement(Vec2 input) {
      float length = input.length();
      if (length <= 0.0F) {
         return input;
      } else {
         Vec2 multiplied = input.scale(1.0F / length);
         float distance = distanceToUnitSquare(multiplied);
         float min = Math.min(length * distance, 1.0F);
         return multiplied.scale(min);
      }
   }

   private static float distanceToUnitSquare(Vec2 input) {
      float x = Math.abs(input.x());
      float z = Math.abs(input.y());
      float additional = z > x ? x / z : z / x;
      return GrimMath.sqrt(1.0F + GrimMath.square(additional));
   }

   private static float getItemUseSpeedMultiplier(GrimPlayer player) {
      if (!player.getClientVersion().isOlderThan(ClientVersion.V_1_21_11) && USE_EFFECTS_COMPONENT_EXISTS) {
         ItemStack itemInHand = player.inventory.getItemInHand(player.packetStateData.itemInUseHand);
         ItemUseEffects useEffects = (ItemUseEffects)itemInHand.getComponentOr(ComponentTypes.USE_EFFECTS, (Object)null);
         return useEffects == null ? 0.2F : useEffects.getSpeedMultiplier();
      } else {
         return 0.2F;
      }
   }

   public void guessBestMovement(float speed, GrimPlayer player) {
      Set<VectorData> init = this.fetchPossibleStartTickVectors(player);
      Vector3dm toZeroVec;
      if (player.uncertaintyHandler.influencedByBouncyBlock()) {
         for(Iterator var4 = init.iterator(); var4.hasNext(); player.uncertaintyHandler.nextTickSlimeBlockUncertainty = Math.max(Math.abs(toZeroVec.getY()), player.uncertaintyHandler.nextTickSlimeBlockUncertainty)) {
            VectorData data = (VectorData)var4.next();
            toZeroVec = (new PredictionEngine()).handleStartingVelocityUncertainty(player, data, new Vector3dm(0, -1000000000, 0));
         }
      }

      player.updateVelocityMovementSkipping();
      player.couldSkipTick = player.couldSkipTick || player.pointThreeEstimator.determineCanSkipTick(speed, init);
      List<VectorData> possibleVelocities = this.applyInputsToVelocityPossibilities(player, init, speed);
      if (player.couldSkipTick) {
         this.addZeroPointThreeToPossibilities(speed, player, possibleVelocities);
      }

      this.doPredictions(player, possibleVelocities, speed);
      (new MovementTickerPlayer(player)).move(player.clientVelocity.clone(), player.predictedVelocity.vector);
      this.endOfTick(player, player.gravity);
   }

   private void doPredictions(GrimPlayer player, List<VectorData> possibleVelocities, float speed) {
      possibleVelocities.sort((a, b) -> {
         return this.sortVectorData(a, b, player);
      });
      ((SneakingEstimator)player.checkManager.getPostPredictionCheck(SneakingEstimator.class)).storePossibleVelocities(possibleVelocities);
      double bestInput = Double.MAX_VALUE;
      VectorData bestCollisionVel = null;
      Vector3dm beforeCollisionMovement = null;
      Vector3dm originalClientVel = player.clientVelocity.clone();
      SimpleCollisionBox originalBB = player.boundingBox;
      SimpleCollisionBox pointThreeThanksMojang = player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) ? GetBoundingBox.getBoundingBoxFromPosAndSize(player, player.lastX, player.lastY, player.lastZ, 0.6F, 0.6F) : originalBB;
      player.skippedTickInActualMovement = false;
      Iterator var11 = possibleVelocities.iterator();

      while(var11.hasNext()) {
         VectorData clientVelAfterInput = (VectorData)var11.next();
         Vector3dm primaryPushMovement = this.handleStartingVelocityUncertainty(player, clientVelAfterInput, player.actualMovement);
         Vector3dm bestTheoreticalCollisionResult = VectorUtils.cutBoxToVector(player.actualMovement, (new SimpleCollisionBox(0.0D, Math.min(0.0D, primaryPushMovement.getY()), 0.0D, primaryPushMovement.getX(), Math.max(0.6D, primaryPushMovement.getY()), primaryPushMovement.getZ())).sort());
         if (!(bestTheoreticalCollisionResult.distanceSquared(player.actualMovement) > bestInput) || clientVelAfterInput.isKnockback() || clientVelAfterInput.isExplosion()) {
            if (clientVelAfterInput.isZeroPointZeroThree()) {
               player.boundingBox = pointThreeThanksMojang;
            } else {
               player.boundingBox = originalBB;
            }

            Pair<Vector3dm, Vector3dm> output = this.doSeekingWallCollisions(player, primaryPushMovement, originalClientVel, clientVelAfterInput);
            primaryPushMovement = (Vector3dm)output.first();
            Vector3dm outputVel = clampMovementToHardBorder(player, (Vector3dm)output.second());
            double resultAccuracy = outputVel.distanceSquared(player.actualMovement);
            if (clientVelAfterInput.isZeroPointZeroThree() && resultAccuracy < 1.0E-6D) {
               player.skippedTickInActualMovement = true;
            }

            if (clientVelAfterInput.isKnockback()) {
               player.checkManager.getKnockbackHandler().handlePredictionAnalysis(Math.sqrt(player.uncertaintyHandler.reduceOffset(resultAccuracy)));
            }

            if (clientVelAfterInput.isExplosion()) {
               player.checkManager.getExplosionHandler().handlePredictionAnalysis(Math.sqrt(player.uncertaintyHandler.reduceOffset(resultAccuracy)));
            }

            if ((clientVelAfterInput.isKnockback() || clientVelAfterInput.isExplosion()) && !clientVelAfterInput.isZeroPointZeroThree()) {
               boolean wasVelocityPointThree = player.pointThreeEstimator.determineCanSkipTick(speed, new HashSet(Collections.singletonList(clientVelAfterInput)));
               if (clientVelAfterInput.isKnockback()) {
                  player.checkManager.getKnockbackHandler().setPointThree(wasVelocityPointThree);
               }

               if (clientVelAfterInput.isExplosion()) {
                  player.checkManager.getExplosionHandler().setPointThree(wasVelocityPointThree);
               }
            }

            if (player.packetStateData.isSlowedByUsingItem() && !clientVelAfterInput.isFlipItem()) {
               player.checkManager.getNoSlow().handlePredictionAnalysis(Math.sqrt(player.uncertaintyHandler.reduceOffset(resultAccuracy)));
            }

            if (!player.checkManager.getKnockbackHandler().shouldIgnoreForPrediction(clientVelAfterInput) && !player.checkManager.getExplosionHandler().shouldIgnoreForPrediction(clientVelAfterInput)) {
               if (resultAccuracy < bestInput) {
                  bestCollisionVel = clientVelAfterInput.returnNewModified(outputVel, VectorData.VectorType.BestVelPicked);
                  bestCollisionVel.preUncertainty = clientVelAfterInput;
                  beforeCollisionMovement = primaryPushMovement;
                  if (player.wouldCollisionResultFlagGroundSpoof(primaryPushMovement.getY(), bestCollisionVel.vector.getY())) {
                     resultAccuracy += 1.0E-8D;
                  }

                  bestInput = resultAccuracy;
               }

               if (bestInput < 1.0000000000000002E-10D && !player.checkManager.getKnockbackHandler().wouldFlag() && !player.checkManager.getExplosionHandler().wouldFlag()) {
                  break;
               }
            }
         }
      }

      assert beforeCollisionMovement != null;

      player.clientVelocity = beforeCollisionMovement.clone();
      player.predictedVelocity = bestCollisionVel;
      player.boundingBox = originalBB;
      if (player.predictedVelocity.isZeroPointZeroThree()) {
         player.skippedTickInActualMovement = true;
      }

   }

   private Pair<Vector3dm, Vector3dm> doSeekingWallCollisions(GrimPlayer player, Vector3dm primaryPushMovement, Vector3dm originalClientVel, VectorData clientVelAfterInput) {
      boolean vehicleKB = player.inVehicle() && clientVelAfterInput.isKnockback() && clientVelAfterInput.vector.getY() == 0.0D;
      double xAdditional = Math.signum(primaryPushMovement.getX()) * 1.0E-7D;
      double yAdditional = vehicleKB ? 0.0D : (double)(primaryPushMovement.getY() > 0.0D ? 1 : -1) * 1.0E-7D;
      double zAdditional = Math.signum(primaryPushMovement.getZ()) * 1.0E-7D;
      double testX = primaryPushMovement.getX() + xAdditional;
      double testY = primaryPushMovement.getY() + yAdditional;
      double testZ = primaryPushMovement.getZ() + zAdditional;
      primaryPushMovement = new Vector3dm(testX, testY, testZ);
      Vector3dm outputVel = Collisions.collide(player, primaryPushMovement.getX(), primaryPushMovement.getY(), primaryPushMovement.getZ(), originalClientVel.getY(), clientVelAfterInput);
      if (testX == outputVel.getX()) {
         primaryPushMovement.setX(primaryPushMovement.getX() - xAdditional);
         outputVel.setX(outputVel.getX() - xAdditional);
      }

      if (testY == outputVel.getY()) {
         primaryPushMovement.setY(primaryPushMovement.getY() - yAdditional);
         outputVel.setY(outputVel.getY() - yAdditional);
      }

      if (testZ == outputVel.getZ()) {
         primaryPushMovement.setZ(primaryPushMovement.getZ() - zAdditional);
         outputVel.setZ(outputVel.getZ() - zAdditional);
      }

      return new Pair(primaryPushMovement, outputVel);
   }

   private void addZeroPointThreeToPossibilities(float speed, GrimPlayer player, List<VectorData> possibleVelocities) {
      Set<VectorData> pointThreePossibilities = new HashSet();
      Vector3dm pointThreeVector = new Vector3dm();
      if (!player.pointThreeEstimator.controlsVerticalMovement()) {
         pointThreeVector.setY(player.clientVelocity.getY());
      } else {
         ((Set)pointThreePossibilities).add(new VectorData(new Vector3dm(0.0D, player.clientVelocity.getY(), 0.0D), VectorData.VectorType.ZeroPointZeroThree));
      }

      ((Set)pointThreePossibilities).add(new VectorData(pointThreeVector, VectorData.VectorType.ZeroPointZeroThree));
      if (player.pointThreeEstimator.isNearFluid && !Collisions.isEmpty(player, player.boundingBox.copy().expand(0.4D, 0.0D, 0.4D)) && !player.onGround) {
         ((Set)pointThreePossibilities).add(new VectorData(new Vector3dm(0.0D, 0.3D, 0.0D), VectorData.VectorType.ZeroPointZeroThree));
      }

      if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) && player.isSwimming) {
         pointThreePossibilities = PredictionEngineWater.transformSwimmingVectors(player, (Set)pointThreePossibilities);
      }

      Vector3dm riptideAddition;
      if (player.pointThreeEstimator.isNearClimbable() && (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) || !Collisions.isEmpty(player, player.boundingBox.copy().expand(player.clientVelocity.getX(), 0.0D, player.clientVelocity.getZ()).expand(0.5D, -1.0E-7D, 0.5D)))) {
         riptideAddition = new Vector3dm(0.0D, 0.2D, 0.0D);
         PredictionEngineNormal.staticVectorEndOfTick(player, riptideAddition);
         ((Set)pointThreePossibilities).add(new VectorData(riptideAddition, VectorData.VectorType.ZeroPointZeroThree));
      }

      this.addJumpsToPossibilities(player, (Set)pointThreePossibilities);
      this.addExplosionToPossibilities(player, (Set)pointThreePossibilities);
      if (player.packetStateData.tryingToRiptide) {
         riptideAddition = Riptide.getRiptideVelocity(player);
         ((Set)pointThreePossibilities).add(new VectorData(player.clientVelocity.clone().add(riptideAddition), new VectorData(new Vector3dm(), VectorData.VectorType.ZeroPointZeroThree), VectorData.VectorType.Trident));
      }

      possibleVelocities.addAll(this.applyInputsToVelocityPossibilities(player, (Set)pointThreePossibilities, speed));
   }

   public List<VectorData> applyInputsToVelocityPossibilities(GrimPlayer player, Set<VectorData> possibleVectors, float speed) {
      List<VectorData> returnVectors = new ArrayList();
      this.loopVectors(player, possibleVectors, speed, returnVectors);
      return returnVectors;
   }

   public void addFluidPushingToStartingVectors(GrimPlayer player, Set<VectorData> data) {
      Iterator var3 = data.iterator();

      while(var3.hasNext()) {
         VectorData vectorData = (VectorData)var3.next();
         if (vectorData.isKnockback() && player.baseTickAddition.lengthSquared() != 0.0D) {
            vectorData.vector = vectorData.vector.add(player.baseTickAddition);
         }

         if (vectorData.isKnockback() && player.baseTickWaterPushing.lengthSquared() != 0.0D) {
            if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13)) {
               Vector3dm vec3 = player.baseTickWaterPushing.clone();
               if (Math.abs(vectorData.vector.getX()) < 0.003D && Math.abs(vectorData.vector.getZ()) < 0.003D && player.baseTickWaterPushing.length() < 0.0045000000000000005D) {
                  vec3 = vec3.normalize().multiply(0.0045000000000000005D);
               }

               vectorData.vector = vectorData.vector.add(vec3);
            } else {
               vectorData.vector = vectorData.vector.add(player.baseTickWaterPushing);
            }
         }
      }

   }

   public Set<VectorData> fetchPossibleStartTickVectors(GrimPlayer player) {
      Set<VectorData> velocities = player.getPossibleVelocities();
      this.addExplosionToPossibilities(player, velocities);
      if (player.packetStateData.tryingToRiptide) {
         Vector3dm riptideAddition = Riptide.getRiptideVelocity(player);
         velocities.add(new VectorData(player.clientVelocity.clone().add(riptideAddition), VectorData.VectorType.Trident));
      }

      this.addFluidPushingToStartingVectors(player, velocities);
      this.addAttackSlowToPossibilities(player, velocities);
      this.addNonEffectiveAI(player, velocities);
      this.applyMovementThreshold(player, velocities);
      this.addJumpsToPossibilities(player, velocities);
      return velocities;
   }

   private void addNonEffectiveAI(GrimPlayer player, Set<VectorData> data) {
      if (player.inVehicle() && !player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_5)) {
         VectorData vectorData;
         for(Iterator var3 = data.iterator(); var3.hasNext(); vectorData.vector = vectorData.vector.clone().multiply(0.98D)) {
            vectorData = (VectorData)var3.next();
         }

      }
   }

   private void addAttackSlowToPossibilities(GrimPlayer player, Set<VectorData> velocities) {
      for(int x = 1; x <= Math.min(player.maxAttackSlow, 5); ++x) {
         Iterator var4 = (new HashSet(velocities)).iterator();

         while(var4.hasNext()) {
            VectorData data = (VectorData)var4.next();
            if (player.minAttackSlow > 0) {
               data.vector.setX(data.vector.getX() * 0.6D);
               data.vector.setZ(data.vector.getZ() * 0.6D);
               data.addVectorType(VectorData.VectorType.AttackSlow);
            } else {
               velocities.add(data.returnNewModified(data.vector.clone().multiply(0.6D, 1.0D, 0.6D), VectorData.VectorType.AttackSlow));
            }
         }

         if (player.minAttackSlow > 0) {
            --player.minAttackSlow;
         }
      }

   }

   public void addJumpsToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
   }

   public void applyMovementThreshold(GrimPlayer player, Set<VectorData> velocities) {
      double minimumMovement = 0.003D;
      if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8)) {
         minimumMovement = 0.005D;
      }

      boolean stupidVectors = player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_5) && !player.inVehicle();
      boolean stuckOnEdge = player.uncertaintyHandler.stuckOnEdge.hasOccurredSince(2);
      Set<VectorData> vectors = stupidVectors && stuckOnEdge ? new HashSet(velocities) : velocities;
      Iterator var8 = ((Set)vectors).iterator();

      while(var8.hasNext()) {
         VectorData vector = (VectorData)var8.next();
         if (stupidVectors) {
            if (Collisions.getHorizontalDistanceSqr(vector.vector) < 9.0E-6D) {
               if (stuckOnEdge) {
                  VectorData edgeVector = vector.returnNewModified(vector.vector.clone(), vector.vectorType);
                  if (Math.abs(edgeVector.vector.getY()) < minimumMovement) {
                     edgeVector.vector.setY(0.0D);
                  }

                  velocities.add(edgeVector);
               }

               vector.vector.setX(0.0D);
               vector.vector.setZ(0.0D);
            }
         } else {
            if (Math.abs(vector.vector.getX()) < minimumMovement) {
               vector.vector.setX(0.0D);
            }

            if (Math.abs(vector.vector.getZ()) < minimumMovement) {
               vector.vector.setZ(0.0D);
            }
         }

         if (Math.abs(vector.vector.getY()) < minimumMovement) {
            vector.vector.setY(0.0D);
         }
      }

   }

   public void addExplosionToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
      if (player.likelyExplosions != null || player.firstBreadExplosion != null) {
         Iterator var3 = (new HashSet(existingVelocities)).iterator();

         while(var3.hasNext()) {
            VectorData vector = (VectorData)var3.next();
            if (player.likelyExplosions != null) {
               existingVelocities.add(new VectorData(vector.vector.clone().add(player.likelyExplosions.vector), vector, VectorData.VectorType.Explosion));
            }

            if (player.firstBreadExplosion != null) {
               existingVelocities.add((new VectorData(vector.vector.clone().add(player.firstBreadExplosion.vector), vector, VectorData.VectorType.Explosion)).returnNewModified(vector.vector.clone().add(player.firstBreadExplosion.vector), VectorData.VectorType.FirstBreadExplosion));
            }
         }

      }
   }

   public int sortVectorData(VectorData a, VectorData b, GrimPlayer player) {
      int aScore = 0;
      int bScore = 0;
      if (a.isExplosion()) {
         aScore -= 5;
      }

      if (a.isKnockback()) {
         aScore -= 5;
      }

      if (b.isExplosion()) {
         bScore -= 5;
      }

      if (b.isKnockback()) {
         bScore -= 5;
      }

      if (a.isFirstBreadExplosion()) {
         ++aScore;
      }

      if (b.isFirstBreadExplosion()) {
         ++bScore;
      }

      if (a.isFirstBreadKb()) {
         ++aScore;
      }

      if (b.isFirstBreadKb()) {
         ++bScore;
      }

      if (a.isFlipItem()) {
         aScore += 3;
      }

      if (b.isFlipItem()) {
         bScore += 3;
      }

      if (a.isZeroPointZeroThree()) {
         --aScore;
      }

      if (b.isZeroPointZeroThree()) {
         --bScore;
      }

      label63: {
         if (player.inVehicle()) {
            if (!player.clientControlledVerticalCollision) {
               break label63;
            }
         } else if (!player.onGround) {
            break label63;
         }

         if (a.vector.getY() >= 0.0D) {
            aScore += 2;
         }
      }

      if (player.inVehicle()) {
         if (!player.clientControlledVerticalCollision) {
            return aScore != bScore ? Integer.compare(aScore, bScore) : Double.compare(a.vector.distanceSquared(player.actualMovement), b.vector.distanceSquared(player.actualMovement));
         }
      } else if (!player.onGround) {
         return aScore != bScore ? Integer.compare(aScore, bScore) : Double.compare(a.vector.distanceSquared(player.actualMovement), b.vector.distanceSquared(player.actualMovement));
      }

      if (b.vector.getY() >= 0.0D) {
         bScore += 2;
      }

      return aScore != bScore ? Integer.compare(aScore, bScore) : Double.compare(a.vector.distanceSquared(player.actualMovement), b.vector.distanceSquared(player.actualMovement));
   }

   public Vector3dm handleStartingVelocityUncertainty(GrimPlayer player, VectorData vector, Vector3dm targetVec) {
      double avgColliding = (double)(Integer)Collections.max(player.uncertaintyHandler.collidingEntities);
      double additionHorizontal = player.uncertaintyHandler.getOffsetHorizontal(vector);
      double additionVertical = player.uncertaintyHandler.getVerticalOffset(vector);
      double pistonX = (Double)Collections.max(player.uncertaintyHandler.pistonX);
      double pistonY = (Double)Collections.max(player.uncertaintyHandler.pistonY);
      double pistonZ = (Double)Collections.max(player.uncertaintyHandler.pistonZ);
      additionHorizontal += player.uncertaintyHandler.lastHorizontalOffset;
      additionVertical += player.uncertaintyHandler.lastVerticalOffset;

      VectorData originalVec;
      for(originalVec = vector; originalVec.lastVector != null; originalVec = originalVec.lastVector) {
      }

      double bonusY = 0.0D;
      if (player.uncertaintyHandler.lastFlyingStatusChange.hasOccurredSince(4)) {
         additionHorizontal += 0.3D;
         bonusY += 0.3D;
      }

      if (player.uncertaintyHandler.lastUnderwaterFlyingHack.hasOccurredSince(9)) {
         bonusY += 0.2D;
      }

      if (player.uncertaintyHandler.lastHardCollidingLerpingEntity.hasOccurredSince(2)) {
         additionHorizontal += 0.1D;
         bonusY += 0.1D;
      }

      if (pistonX != 0.0D || pistonY != 0.0D || pistonZ != 0.0D) {
         additionHorizontal += 0.1D;
         bonusY += 0.1D;
      }

      double horizontalFluid = player.pointThreeEstimator.getHorizontalFluidPushingUncertainty(vector);
      additionHorizontal += horizontalFluid;
      Vector3dm uncertainty = new Vector3dm(avgColliding * 0.08D, additionVertical, avgColliding * 0.08D);
      Vector3dm min = new Vector3dm(player.uncertaintyHandler.xNegativeUncertainty - additionHorizontal, -bonusY + player.uncertaintyHandler.yNegativeUncertainty, player.uncertaintyHandler.zNegativeUncertainty - additionHorizontal);
      Vector3dm max = new Vector3dm(player.uncertaintyHandler.xPositiveUncertainty + additionHorizontal, bonusY + player.uncertaintyHandler.yPositiveUncertainty, player.uncertaintyHandler.zPositiveUncertainty + additionHorizontal);
      Vector3dm minVector = vector.vector.clone().add(min.subtract(uncertainty));
      Vector3dm maxVector = vector.vector.clone().add(max.add(uncertainty));
      if (player.uncertaintyHandler.onGroundUncertain && vector.vector.getY() < 0.0D) {
         maxVector.setY(0);
      }

      double gravityOffset = player.pointThreeEstimator.getAdditionalVerticalUncertainty(vector);
      if (gravityOffset > 0.0D) {
         maxVector.setY(maxVector.getY() + gravityOffset);
      } else {
         minVector.setY(minVector.getY() + gravityOffset);
      }

      double verticalFluid = player.pointThreeEstimator.getVerticalFluidPushingUncertainty(vector);
      minVector.setY(minVector.getY() - verticalFluid);
      double bubbleFluid = player.pointThreeEstimator.getVerticalBubbleUncertainty(vector);
      maxVector.setY(maxVector.getY() + bubbleFluid);
      minVector.setY(minVector.getY() - bubbleFluid);
      if (!player.pointThreeEstimator.canPredictNextVerticalMovement()) {
         minVector.setY(minVector.getY() - player.compensatedEntities.self.getAttributeValue(Attributes.GRAVITY));
      }

      if (player.actualMovement.getY() >= 0.0D && player.uncertaintyHandler.influencedByBouncyBlock() && player.uncertaintyHandler.thisTickSlimeBlockUncertainty != 0.0D && !vector.isJump()) {
         if (player.uncertaintyHandler.thisTickSlimeBlockUncertainty > maxVector.getY()) {
            maxVector.setY(player.uncertaintyHandler.thisTickSlimeBlockUncertainty);
         }

         if (minVector.getY() > 0.0D) {
            minVector.setY(0);
         }
      }

      if (vector.isZeroPointZeroThree() && vector.isSwimHop()) {
         minVector.setY(minVector.getY() - 0.06D);
      }

      SimpleCollisionBox box = new SimpleCollisionBox(minVector, maxVector);
      box.sort();
      double levitation = player.pointThreeEstimator.positiveLevitation(maxVector.getY());
      box.combineToMinimum(box.minX, levitation, box.minZ);
      levitation = player.pointThreeEstimator.positiveLevitation(minVector.getY());
      box.combineToMinimum(box.minX, levitation, box.minZ);
      levitation = player.pointThreeEstimator.negativeLevitation(maxVector.getY());
      box.combineToMinimum(box.minX, levitation, box.minZ);
      levitation = player.pointThreeEstimator.negativeLevitation(minVector.getY());
      box.combineToMinimum(box.minX, levitation, box.minZ);
      SneakingEstimator sneaking = (SneakingEstimator)player.checkManager.getPostPredictionCheck(SneakingEstimator.class);
      box.minX += sneaking.getSneakingPotentialHiddenVelocity().minX;
      box.minZ += sneaking.getSneakingPotentialHiddenVelocity().minZ;
      box.maxX += sneaking.getSneakingPotentialHiddenVelocity().maxX;
      box.maxZ += sneaking.getSneakingPotentialHiddenVelocity().maxZ;
      if (player.uncertaintyHandler.fireworksBox != null) {
         double minXdiff = Math.min(0.0D, player.uncertaintyHandler.fireworksBox.minX - originalVec.vector.getX());
         double minYdiff = Math.min(0.0D, player.uncertaintyHandler.fireworksBox.minY - originalVec.vector.getY());
         double minZdiff = Math.min(0.0D, player.uncertaintyHandler.fireworksBox.minZ - originalVec.vector.getZ());
         double maxXdiff = Math.max(0.0D, player.uncertaintyHandler.fireworksBox.maxX - originalVec.vector.getX());
         double maxYdiff = Math.max(0.0D, player.uncertaintyHandler.fireworksBox.maxY - originalVec.vector.getY());
         double maxZdiff = Math.max(0.0D, player.uncertaintyHandler.fireworksBox.maxZ - originalVec.vector.getZ());
         box.expandMin(minXdiff, minYdiff, minZdiff);
         box.expandMax(maxXdiff, maxYdiff, maxZdiff);
      }

      SimpleCollisionBox rod = player.uncertaintyHandler.fishingRodPullBox;
      if (rod != null) {
         box.expandMin(rod.minX, rod.minY, rod.minZ);
         box.expandMax(rod.maxX, rod.maxY, rod.maxZ);
      }

      if (player.uncertaintyHandler.stuckOnEdge.hasOccurredSince(0) || player.uncertaintyHandler.isSteppingOnSlime) {
         box.expandToAbsoluteCoordinates(0.0D, box.maxY, 0.0D);
      }

      if (player.uncertaintyHandler.lastVehicleSwitch.hasOccurredSince(0) || player.uncertaintyHandler.lastHardCollidingLerpingEntity.hasOccurredSince(3) || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) && vector.vector.getY() > 0.0D && vector.isZeroPointZeroThree() && !Collisions.isEmpty(player, GetBoundingBox.getBoundingBoxFromPosAndSize(player, player.lastX, vector.vector.getY() + player.lastY + 0.6D, player.lastZ, 0.6F, 1.26F))) {
         box.expandToAbsoluteCoordinates(0.0D, 0.0D, 0.0D);
      }

      if (player.uncertaintyHandler.lastVehicleSwitch.hasOccurredSince(1)) {
         double trueFriction = player.lastOnGround ? (double)player.friction * 0.91D : 0.91D;
         if (player.wasTouchingLava) {
            trueFriction = 0.5D;
         }

         if (player.wasTouchingWater) {
            trueFriction = 0.96D;
         }

         double maxY = Math.max(box.maxY, box.maxY + (box.maxY - player.gravity) * 0.91D);
         double minY = Math.min(box.minY, box.minY + (box.minY - player.gravity) * 0.91D);
         double minX = Math.min(box.minX, box.minX + -player.speed * trueFriction);
         double minZ = Math.min(box.minZ, box.minZ + -player.speed * trueFriction);
         double maxX = Math.max(box.maxX, box.maxX + player.speed * trueFriction);
         double maxZ = Math.max(box.maxZ, box.maxZ + player.speed * trueFriction);
         box = new SimpleCollisionBox(minX, minY, minZ, maxX, maxY, maxZ);
         box.expand(0.05D, 0.0D, 0.05D);
      }

      if (player.uncertaintyHandler.lastVehicleSwitch.hasOccurredSince(10)) {
         box.expand(0.001D);
      }

      minVector = box.min();
      maxVector = box.max();
      if (pistonX != 0.0D) {
         minVector.setX(Math.min(minVector.getX() - pistonX, pistonX));
         maxVector.setX(Math.max(maxVector.getX() + pistonX, pistonX));
      }

      if (pistonY != 0.0D) {
         minVector.setY(Math.min(minVector.getY() - pistonY, pistonY));
         maxVector.setY(Math.max(maxVector.getY() + pistonY, pistonY));
      }

      if (pistonZ != 0.0D) {
         minVector.setZ(Math.min(minVector.getZ() - pistonZ, pistonZ));
         maxVector.setZ(Math.max(maxVector.getZ() + pistonZ, pistonZ));
      }

      return VectorUtils.cutBoxToVector(targetVec, minVector, maxVector);
   }

   public void endOfTick(GrimPlayer player, double d) {
      player.canSwimHop = this.canSwimHop(player);
      player.lastWasClimbing = 0.0D;
   }

   private void loopVectors(GrimPlayer player, Set<VectorData> possibleVectors, float speed, List<VectorData> returnVectors) {
      int forwardMin = player.isSprinting && !player.isSwimming ? 1 : -1;
      int forwardMax = 1;
      int strafeMin = -1;
      int strafeMax = 1;
      if (player.supportsEndTick()) {
         strafeMax = 0;
         strafeMin = 0;
         forwardMax = 0;
         forwardMin = 0;
         KnownInput knownInput = player.packetStateData.knownInput;
         if (knownInput.forward() || player.isSprinting && !player.isSwimming) {
            ++forwardMax;
            ++forwardMin;
         }

         if (knownInput.backward() && (!player.isSprinting || player.isSwimming)) {
            --forwardMax;
            --forwardMin;
         }

         if (knownInput.left()) {
            ++strafeMax;
            ++strafeMin;
         }

         if (knownInput.right()) {
            --strafeMax;
            --strafeMin;
         }
      }

      for(int loopSlowed = 0; loopSlowed <= 1; ++loopSlowed) {
         label107:
         for(int loopUsingItem = 0; loopUsingItem <= 1; ++loopUsingItem) {
            Iterator var11 = possibleVectors.iterator();

            while(true) {
               VectorData possibleLastTickOutput;
               do {
                  if (!var11.hasNext()) {
                     player.packetStateData.setSlowedByUsingItem(!player.packetStateData.isSlowedByUsingItem());
                     continue label107;
                  }

                  possibleLastTickOutput = (VectorData)var11.next();
               } while(loopSlowed == 1 && !possibleLastTickOutput.isZeroPointZeroThree() && player.isForceSlowMovement());

               for(int strafe = strafeMin; strafe <= strafeMax; ++strafe) {
                  for(int forward = forwardMin; forward <= forwardMax; ++forward) {
                     for(int applyStuckSpeed = 1; applyStuckSpeed >= 0 && (applyStuckSpeed != 0 || !player.isForceStuckSpeed()); --applyStuckSpeed) {
                        Vector3dm input = transformInputsToVector(player, new Vector3dm(strafe, 0, forward));
                        VectorData result = new VectorData(possibleLastTickOutput.vector.clone().add(this.getMovementResultFromInput(player, input, speed, player.yaw)), possibleLastTickOutput, VectorData.VectorType.InputResult);
                        result.input = input;
                        if (applyStuckSpeed != 0) {
                           result = result.returnNewModified(result.vector.clone().multiply(player.stuckSpeedMultiplier), VectorData.VectorType.StuckMultiplier);
                        }

                        result = result.returnNewModified(this.handleOnClimbable(result.vector.clone(), player), VectorData.VectorType.Climbable);
                        if (loopUsingItem == 1) {
                           result = result.returnNewModified(VectorData.VectorType.Flip_Use_Item);
                        }

                        returnVectors.add(result);
                     }
                  }
               }
            }
         }

         player.isSlowMovement = !player.isSlowMovement;
      }

   }

   public boolean canSwimHop(GrimPlayer player) {
      if (player.inVehicle() && player.compensatedEntities.self.getRiding().isBoat) {
         return false;
      } else {
         SimpleCollisionBox oldBox = player.inVehicle() ? GetBoundingBox.getCollisionBoxForPlayer(player, player.lastX, player.lastY, player.lastZ) : GetBoundingBox.getBoundingBoxFromPosAndSize(player, player.lastX, player.lastY, player.lastZ, 0.6F, 1.8F);
         if (!player.compensatedWorld.containsLiquid(oldBox.expand(0.1D, 0.1D, 0.1D))) {
            return false;
         } else {
            SimpleCollisionBox oldBB = player.boundingBox;
            player.boundingBox = player.boundingBox.copy().expand(-player.getMovementThreshold(), 0.0D, -player.getMovementThreshold());
            double pointThreeToGround = Collisions.collide(player, 0.0D, -player.getMovementThreshold(), 0.0D).getY() + 1.0E-7D;
            player.boundingBox = oldBB;
            SimpleCollisionBox newBox = player.inVehicle() ? GetBoundingBox.getCollisionBoxForPlayer(player, player.x, player.y, player.z) : GetBoundingBox.getBoundingBoxFromPosAndSize(player, player.x, player.y, player.z, 0.6F, 1.8F);
            return player.uncertaintyHandler.lastHardCollidingLerpingEntity.hasOccurredSince(3) || !Collisions.isEmpty(player, newBox.expand(player.clientVelocity.getX(), -1.0D * pointThreeToGround, player.clientVelocity.getZ()).expand(0.5D, 0.03D, 0.5D));
         }
      }
   }

   public Vector3dm getMovementResultFromInput(GrimPlayer player, Vector3dm inputVector, float f, float f2) {
      float f2InRadians = GrimMath.radians(f2);
      float f3 = player.trigHandler.sin(f2InRadians);
      float f4 = player.trigHandler.cos(f2InRadians);
      double xResult = inputVector.getX() * (double)f4 - inputVector.getZ() * (double)f3;
      double zResult = inputVector.getZ() * (double)f4 + inputVector.getX() * (double)f3;
      return new Vector3dm(xResult * (double)f, 0.0D, zResult * (double)f);
   }

   public Vector3dm handleOnClimbable(Vector3dm vector, GrimPlayer player) {
      return vector;
   }

   public void doJump(GrimPlayer player, Vector3dm vector) {
      if (player.lastOnGround && !player.onGround) {
         JumpPower.jumpFromGround(player, vector);
      }
   }

   static {
      USE_EFFECTS_COMPONENT_EXISTS = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11);
   }
}
