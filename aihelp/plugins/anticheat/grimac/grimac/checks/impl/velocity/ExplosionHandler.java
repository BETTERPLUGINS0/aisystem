package ac.grim.grimac.checks.impl.velocity;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateValue;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerExplosion;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.data.VelocityData;
import ac.grim.grimac.utils.math.Vector3dm;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lombok.Generated;

@CheckData(
   name = "AntiExplosion",
   configName = "Explosion",
   setback = 10.0D
)
public class ExplosionHandler extends Check implements PostPredictionCheck {
   private final Deque<VelocityData> firstBreadMap = new LinkedList();
   private VelocityData lastExplosionsKnownTaken = null;
   private VelocityData firstBreadAddedExplosion = null;
   private boolean explosionPointThree = false;
   private double offsetToFlag;

   public ExplosionHandler(GrimPlayer player) {
      super(player);
   }

   public void onPacketSend(PacketSendEvent event) {
      if (event.getPacketType() == PacketType.Play.Server.EXPLOSION) {
         WrapperPlayServerExplosion explosion = new WrapperPlayServerExplosion(event);
         boolean hasBlocks = PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_21_2);
         if (hasBlocks) {
            this.handleBlockExplosions(explosion);
         }

         Vector3d velocity = explosion.getKnockback();
         if (velocity != null && (velocity.x != 0.0D || velocity.y != 0.0D || velocity.z != 0.0D)) {
            if (!hasBlocks || explosion.getRecords().isEmpty()) {
               this.player.sendTransaction();
            }

            this.addPlayerExplosion(this.player.lastTransactionSent.get(), velocity);
            List var10000 = event.getTasksAfterSend();
            GrimPlayer var10001 = this.player;
            Objects.requireNonNull(var10001);
            var10000.add(var10001::sendTransaction);
         }
      }

   }

   private void handleBlockExplosions(WrapperPlayServerExplosion explosion) {
      WrapperPlayServerExplosion.BlockInteraction blockInteraction = explosion.getBlockInteraction();
      boolean shouldDestroy = blockInteraction != WrapperPlayServerExplosion.BlockInteraction.KEEP_BLOCKS;
      if (!explosion.getRecords().isEmpty() && shouldDestroy) {
         this.player.sendTransaction();
         this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> {
            Iterator var3 = explosion.getRecords().iterator();

            while(true) {
               while(var3.hasNext()) {
                  Vector3i record = (Vector3i)var3.next();
                  if (blockInteraction != WrapperPlayServerExplosion.BlockInteraction.TRIGGER_BLOCKS) {
                     this.player.compensatedWorld.updateBlock(record.x, record.y, record.z, 0);
                  } else {
                     WrappedBlockState state = this.player.compensatedWorld.getBlock(record);
                     StateType type = state.getType();
                     if (!BlockTags.CANDLES.contains(type) && !BlockTags.CANDLE_CAKES.contains(type)) {
                        if (type != StateTypes.BELL) {
                           boolean canFlip = state.hasProperty(StateValue.POWERED) && !state.isPowered() || type == StateTypes.LEVER;
                           if (canFlip) {
                              this.player.compensatedWorld.tickOpenable(record.x, record.y, record.z);
                           }
                        }
                     } else {
                        state.setLit(false);
                     }
                  }
               }

               return;
            }
         });
      }
   }

   public VelocityData getFutureExplosion() {
      if (!this.firstBreadMap.isEmpty()) {
         return (VelocityData)this.firstBreadMap.peek();
      } else if (this.lastExplosionsKnownTaken != null) {
         return this.lastExplosionsKnownTaken;
      } else if (this.player.firstBreadExplosion != null && this.player.likelyExplosions == null) {
         return this.player.firstBreadExplosion;
      } else {
         return this.player.likelyExplosions != null ? this.player.likelyExplosions : null;
      }
   }

   public boolean shouldIgnoreForPrediction(VectorData data) {
      if (data.isExplosion() && data.isFirstBreadExplosion()) {
         return this.player.firstBreadExplosion.offset > this.offsetToFlag;
      } else {
         return false;
      }
   }

   public boolean wouldFlag() {
      return this.player.likelyExplosions != null && this.player.likelyExplosions.offset > this.offsetToFlag || this.player.firstBreadExplosion != null && this.player.firstBreadExplosion.offset > this.offsetToFlag;
   }

   public void addPlayerExplosion(int breadOne, Vector3d explosion) {
      this.firstBreadMap.add(new VelocityData(-1, breadOne, this.player.getSetbackTeleportUtil().isSendingSetback, new Vector3dm(explosion.getX(), explosion.getY(), explosion.getZ())));
   }

   public void setPointThree(boolean isPointThree) {
      this.explosionPointThree = this.explosionPointThree || isPointThree;
   }

   public void handlePredictionAnalysis(double offset) {
      if (this.player.firstBreadExplosion != null) {
         this.player.firstBreadExplosion.offset = Math.min(this.player.firstBreadExplosion.offset, offset);
      }

      if (this.player.likelyExplosions != null) {
         this.player.likelyExplosions.offset = Math.min(this.player.likelyExplosions.offset, offset);
      }

   }

   public void forceExempt() {
      if (this.player.firstBreadExplosion != null) {
         this.player.firstBreadExplosion.offset = 0.0D;
      }

      if (this.player.likelyExplosions != null) {
         this.player.likelyExplosions.offset = 0.0D;
      }

   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      double offset = predictionComplete.getOffset();
      boolean wasZero = this.explosionPointThree;
      this.explosionPointThree = false;
      if (this.player.likelyExplosions == null && this.player.firstBreadExplosion == null) {
         this.firstBreadAddedExplosion = null;
      } else {
         int minTrans = Math.min(this.player.likelyExplosions != null ? this.player.likelyExplosions.transaction : Integer.MAX_VALUE, this.player.firstBreadExplosion != null ? this.player.firstBreadExplosion.transaction : Integer.MAX_VALUE);
         int kbTrans = Math.max(this.player.likelyKB != null ? this.player.likelyKB.transaction : Integer.MIN_VALUE, this.player.firstBreadKB != null ? this.player.firstBreadKB.transaction : Integer.MIN_VALUE);
         if (this.player.predictedVelocity.isFirstBreadExplosion()) {
            this.firstBreadAddedExplosion = null;
            this.firstBreadMap.poll();
         }

         if (wasZero || this.player.predictedVelocity.isExplosion() || minTrans < kbTrans) {
            if (this.player.firstBreadExplosion != null) {
               this.player.firstBreadExplosion.offset = Math.min(this.player.firstBreadExplosion.offset, offset);
            }

            if (this.player.likelyExplosions != null) {
               this.player.likelyExplosions.offset = Math.min(this.player.likelyExplosions.offset, offset);
            }
         }

         if (this.player.likelyExplosions != null && !this.player.compensatedEntities.self.isDead) {
            if (this.player.likelyExplosions.offset > this.offsetToFlag) {
               this.flagAndAlertWithSetback(this.player.likelyExplosions.offset == 2.147483647E9D ? "ignored explosion" : "o: " + this.formatOffset(offset));
            } else {
               this.reward();
            }
         }

      }
   }

   public VelocityData getPossibleExplosions(int lastTransaction, boolean isJustTesting) {
      this.handleTransactionPacket(lastTransaction);
      if (this.lastExplosionsKnownTaken == null) {
         return null;
      } else {
         VelocityData returnLastExplosion = this.lastExplosionsKnownTaken;
         if (!isJustTesting) {
            this.lastExplosionsKnownTaken = null;
         }

         return returnLastExplosion;
      }
   }

   private void handleTransactionPacket(int transactionID) {
      for(VelocityData data = (VelocityData)this.firstBreadMap.peek(); data != null; data = (VelocityData)this.firstBreadMap.peek()) {
         if (data.transaction == transactionID) {
            if (this.lastExplosionsKnownTaken != null) {
               this.firstBreadAddedExplosion = new VelocityData(-1, data.transaction, data.isSetback, this.lastExplosionsKnownTaken.vector.clone().add(data.vector));
            } else {
               this.firstBreadAddedExplosion = new VelocityData(-1, data.transaction, data.isSetback, data.vector);
            }
            break;
         }

         if (data.transaction >= transactionID) {
            break;
         }

         if (this.lastExplosionsKnownTaken != null) {
            this.lastExplosionsKnownTaken.vector.add(data.vector);
         } else {
            this.lastExplosionsKnownTaken = new VelocityData(-1, data.transaction, data.isSetback, data.vector);
         }

         this.firstBreadAddedExplosion = null;
         this.firstBreadMap.poll();
      }

   }

   public VelocityData getFirstBreadAddedExplosion(int lastTransaction) {
      this.handleTransactionPacket(lastTransaction);
      return this.firstBreadAddedExplosion;
   }

   public void onReload(ConfigManager config) {
      this.offsetToFlag = config.getDoubleElse("Explosion.threshold", 1.0E-5D);
   }

   @Generated
   public boolean isExplosionPointThree() {
      return this.explosionPointThree;
   }
}
