package ac.grim.grimac.utils.data.attribute;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import ac.grim.grimac.utils.latency.CompensatedEntities;
import ac.grim.grimac.utils.math.GrimMath;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class ValuedAttribute {
   private static final Function<Double, Double> DEFAULT_GET_REWRITE = Function.identity();
   private final Attribute attribute;
   private final double min;
   private final double max;
   private final double defaultValue;
   private WrapperPlayServerUpdateAttributes.Property lastProperty;
   private double value;
   private BiFunction<Double, Double, Double> setRewriter;
   private Function<Double, Double> getRewriter;

   private ValuedAttribute(Attribute attribute, double defaultValue, double min, double max) {
      if (!(defaultValue < min) && !(defaultValue > max)) {
         this.attribute = attribute;
         this.defaultValue = defaultValue;
         this.value = defaultValue;
         this.min = min;
         this.max = max;
         this.getRewriter = DEFAULT_GET_REWRITE;
      } else {
         throw new IllegalArgumentException("Default value must be between min and max!");
      }
   }

   public static ValuedAttribute ranged(Attribute attribute, double defaultValue, double min, double max) {
      return new ValuedAttribute(attribute, defaultValue, min, max);
   }

   public ValuedAttribute withSetRewriter(BiFunction<Double, Double, Double> rewriteFunction) {
      this.setRewriter = rewriteFunction;
      return this;
   }

   public ValuedAttribute requiredVersion(GrimPlayer player, ClientVersion requiredVersion) {
      this.withSetRewriter((oldValue, newValue) -> {
         return player.getClientVersion().isOlderThan(requiredVersion) ? oldValue : newValue;
      });
      return this;
   }

   public ValuedAttribute withGetRewriter(Function<Double, Double> getRewriteFunction) {
      this.getRewriter = getRewriteFunction;
      return this;
   }

   public Attribute attribute() {
      return this.attribute;
   }

   public void reset() {
      this.value = this.defaultValue;
      this.lastProperty = null;
   }

   public double get() {
      return (Double)this.getRewriter.apply(this.value);
   }

   public void override(double value) {
      this.value = value;
   }

   /** @deprecated */
   @Deprecated
   public Optional<WrapperPlayServerUpdateAttributes.Property> property() {
      return Optional.ofNullable(this.lastProperty);
   }

   public void recalculate() {
      this.with(this.lastProperty);
   }

   public double with(WrapperPlayServerUpdateAttributes.Property property) {
      double baseValue = property.getValue();
      double additionSum = 0.0D;
      double multiplyBaseSum = 0.0D;
      double multiplyTotalProduct = 1.0D;
      List<WrapperPlayServerUpdateAttributes.PropertyModifier> modifiers = property.getModifiers();
      modifiers.removeIf((modifierx) -> {
         return modifierx.getUUID().equals(CompensatedEntities.SPRINTING_MODIFIER_UUID) || modifierx.getName().getKey().equals("sprinting");
      });
      Iterator var11 = modifiers.iterator();

      while(var11.hasNext()) {
         WrapperPlayServerUpdateAttributes.PropertyModifier modifier = (WrapperPlayServerUpdateAttributes.PropertyModifier)var11.next();
         switch(modifier.getOperation()) {
         case ADDITION:
            additionSum += modifier.getAmount();
            break;
         case MULTIPLY_BASE:
            multiplyBaseSum += modifier.getAmount();
            break;
         case MULTIPLY_TOTAL:
            multiplyTotalProduct *= 1.0D + modifier.getAmount();
         }
      }

      double newValue = GrimMath.clamp((baseValue + additionSum) * (1.0D + multiplyBaseSum) * multiplyTotalProduct, this.min, this.max);
      if (this.setRewriter != null) {
         newValue = (Double)this.setRewriter.apply(this.value, newValue);
      }

      if (!(newValue < this.min) && !(newValue > this.max)) {
         this.lastProperty = property;
         return this.value = newValue;
      } else {
         throw new IllegalArgumentException("New value must be between min and max!");
      }
   }
}
