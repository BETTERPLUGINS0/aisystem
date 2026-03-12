package ac.grim.grimac.checks.impl.velocity;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.data.Pair;
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
   name = "AntiKB",
   alternativeName = "AntiKnockback",
   configName = "Knockback",
   setback = 10.0D,
   decay = 0.025D
)
public class KnockbackHandler extends Check implements PostPredictionCheck {
   private final Deque<VelocityData> firstBreadMap = new LinkedList();
   private final Deque<VelocityData> lastKnockbackKnownTaken = new LinkedList();
   private VelocityData firstBreadOnlyKnockback = null;
   private boolean knockbackPointThree = false;
   private double offsetToFlag;
   private double maxAdv;
   private double immediate;
   private double ceiling;
   private double multiplier;
   private double threshold;

   public KnockbackHandler(GrimPlayer player) {
      super(player);
   }

   public void onPacketSend(PacketSendEvent event) {
      if (event.getPacketType() == PacketType.Play.Server.ENTITY_VELOCITY) {
         WrapperPlayServerEntityVelocity velocity = new WrapperPlayServerEntityVelocity(event);
         int entityId = velocity.getEntityId();
         if (this.player.compensatedEntities.serverPlayerVehicle != null && entityId != this.player.compensatedEntities.serverPlayerVehicle) {
            return;
         }

         if (this.player.compensatedEntities.serverPlayerVehicle == null && entityId != this.player.entityID) {
            return;
         }

         Vector3d playerVelocity = velocity.getVelocity();
         if (playerVelocity.getY() == -0.04D) {
            velocity.setVelocity(playerVelocity.add(new Vector3d(0.0D, 1.25E-4D, 0.0D)));
            playerVelocity = velocity.getVelocity();
            event.markForReEncode(true);
         }

         playerVelocity = VectorPrecisionConverter.convert(this.player.getClientVersion(), playerVelocity);
         this.player.sendTransaction();
         this.addPlayerKnockback(entityId, this.player.lastTransactionSent.get(), new Vector3dm(playerVelocity.getX(), playerVelocity.getY(), playerVelocity.getZ()));
         List var10000 = event.getTasksAfterSend();
         GrimPlayer var10001 = this.player;
         Objects.requireNonNull(var10001);
         var10000.add(var10001::sendTransaction);
      }

   }

   @NotNull
   public Pair<VelocityData, Vector3dm> getFutureKnockback() {
      VelocityData data;
      if (!this.firstBreadMap.isEmpty()) {
         data = (VelocityData)this.firstBreadMap.peek();
         return new Pair(data, data != null ? data.vector : null);
      } else if (!this.lastKnockbackKnownTaken.isEmpty()) {
         data = (VelocityData)this.lastKnockbackKnownTaken.peek();
         return new Pair(data, data != null ? data.vector : null);
      } else if (this.player.firstBreadKB != null && this.player.likelyKB == null) {
         data = this.player.firstBreadKB;
         return new Pair(data, data.vector.clone());
      } else if (this.player.likelyKB != null) {
         data = this.player.likelyKB;
         return new Pair(data, data.vector.clone());
      } else {
         return new Pair((Object)null, (Object)null);
      }
   }

   private void addPlayerKnockback(int entityID, int breadOne, Vector3dm knockback) {
      this.firstBreadMap.add(new VelocityData(entityID, breadOne, this.player.getSetbackTeleportUtil().isSendingSetback, knockback));
   }

   public VelocityData calculateRequiredKB(int entityID, int transaction, boolean isJustTesting) {
      this.tickKnockback(transaction);
      VelocityData returnLastKB = null;
      Iterator var5 = this.lastKnockbackKnownTaken.iterator();

      while(var5.hasNext()) {
         VelocityData data = (VelocityData)var5.next();
         if (data.entityID == entityID) {
            returnLastKB = data;
         }
      }

      if (!isJustTesting) {
         this.lastKnockbackKnownTaken.clear();
      }

      return returnLastKB;
   }

   private void tickKnockback(int transactionID) {
      this.firstBreadOnlyKnockback = null;
      if (!this.firstBreadMap.isEmpty()) {
         for(VelocityData data = (VelocityData)this.firstBreadMap.peek(); data != null; data = (VelocityData)this.firstBreadMap.peek()) {
            if (data.transaction == transactionID) {
               this.firstBreadOnlyKnockback = new VelocityData(data.entityID, data.transaction, data.isSetback, data.vector);
               break;
            }

            if (data.transaction >= transactionID) {
               break;
            }

            if (this.firstBreadOnlyKnockback != null) {
               this.lastKnockbackKnownTaken.add(new VelocityData(data.entityID, data.transaction, data.vector, data.isSetback, data.offset));
            } else {
               this.lastKnockbackKnownTaken.add(new VelocityData(data.entityID, data.transaction, data.isSetback, data.vector));
            }

            this.firstBreadOnlyKnockback = null;
            this.firstBreadMap.poll();
         }

      }
   }

   public void forceExempt() {
      if (this.player.firstBreadKB != null) {
         this.player.firstBreadKB.offset = 0.0D;
      }

      if (this.player.likelyKB != null) {
         this.player.likelyKB.offset = 0.0D;
      }

   }

   public void setPointThree(boolean isPointThree) {
      this.knockbackPointThree = this.knockbackPointThree || isPointThree;
   }

   public void handlePredictionAnalysis(double offset) {
      if (this.player.firstBreadKB != null) {
         this.player.firstBreadKB.offset = Math.min(this.player.firstBreadKB.offset, offset);
      }

      if (this.player.likelyKB != null) {
         this.player.likelyKB.offset = Math.min(this.player.likelyKB.offset, offset);
      }

   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      double offset = predictionComplete.getOffset();
      if (predictionComplete.isChecked() && !predictionComplete.getData().isTeleport()) {
         boolean wasZero = this.knockbackPointThree;
         this.knockbackPointThree = false;
         if (this.player.likelyKB != null || this.player.firstBreadKB != null) {
            if (this.player.predictedVelocity.isFirstBreadKb()) {
               this.firstBreadOnlyKnockback = null;
               this.firstBreadMap.poll();
            }

            if (wasZero || this.player.predictedVelocity.isKnockback()) {
               if (this.player.firstBreadKB != null) {
                  this.player.firstBreadKB.offset = Math.min(this.player.firstBreadKB.offset, offset);
               }

               if (this.player.likelyKB != null) {
                  this.player.likelyKB.offset = Math.min(this.player.likelyKB.offset, offset);
               }
            }

            if (this.player.likelyKB != null) {
               if (this.player.likelyKB.offset > this.offsetToFlag) {
                  this.threshold = Math.min(this.threshold + this.player.likelyKB.offset, this.ceiling);
                  if (this.player.likelyKB.isSetback) {
                     if (!this.isNoSetbackPermission()) {
                        this.player.getSetbackTeleportUtil().executeViolationSetback();
                     }
                  } else if (this.flagAndAlert(this.player.likelyKB.offset == 2.147483647E9D ? "ignored knockback" : "o: " + this.formatOffset(this.player.likelyKB.offset))) {
                     if (this.player.likelyKB.offset >= this.immediate || this.threshold >= this.maxAdv) {
                        this.setbackIfAboveSetbackVL();
                     }
                  } else {
                     this.reward();
                  }
               } else if (this.threshold > 0.05D) {
                  this.threshold *= this.multiplier;
               }
            }

         }
      } else {
         this.forceExempt();
      }
   }

   public boolean shouldIgnoreForPrediction(VectorData data) {
      if (data.isKnockback() && data.isFirstBreadKb()) {
         return this.player.firstBreadKB.offset > this.offsetToFlag;
      } else {
         return false;
      }
   }

   public boolean wouldFlag() {
      return this.player.likelyKB != null && this.player.likelyKB.offset > this.offsetToFlag || this.player.firstBreadKB != null && this.player.firstBreadKB.offset > this.offsetToFlag;
   }

   public VelocityData calculateFirstBreadKnockback(int entityID, int transaction) {
      this.tickKnockback(transaction);
      return this.firstBreadOnlyKnockback != null && this.firstBreadOnlyKnockback.entityID == entityID ? this.firstBreadOnlyKnockback : null;
   }

   public void onReload(ConfigManager config) {
      this.offsetToFlag = config.getDoubleElse("Knockback.threshold", 0.001D);
      this.maxAdv = config.getDoubleElse("Knockback.max-advantage", 1.0D);
      this.immediate = config.getDoubleElse("Knockback.immediate-setback-threshold", 0.1D);
      this.multiplier = config.getDoubleElse("Knockback.setback-decay-multiplier", 0.999D);
      this.ceiling = config.getDoubleElse("Knockback.max-ceiling", 4.0D);
      if (this.maxAdv < 0.0D) {
         this.maxAdv = Double.MAX_VALUE;
      }

      if (this.immediate < 0.0D) {
         this.immediate = Double.MAX_VALUE;
      }

   }

   @Generated
   public boolean isKnockbackPointThree() {
      return this.knockbackPointThree;
   }
}
