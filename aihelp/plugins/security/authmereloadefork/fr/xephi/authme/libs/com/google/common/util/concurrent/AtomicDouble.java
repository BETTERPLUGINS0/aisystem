package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.j2objc.annotations.ReflectionSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
@ReflectionSupport(ReflectionSupport.Level.FULL)
public class AtomicDouble extends Number implements Serializable {
   private static final long serialVersionUID = 0L;
   private transient volatile long value;
   private static final AtomicLongFieldUpdater<AtomicDouble> updater = AtomicLongFieldUpdater.newUpdater(AtomicDouble.class, "value");

   public AtomicDouble(double initialValue) {
      this.value = Double.doubleToRawLongBits(initialValue);
   }

   public AtomicDouble() {
   }

   public final double get() {
      return Double.longBitsToDouble(this.value);
   }

   public final void set(double newValue) {
      long next = Double.doubleToRawLongBits(newValue);
      this.value = next;
   }

   public final void lazySet(double newValue) {
      long next = Double.doubleToRawLongBits(newValue);
      updater.lazySet(this, next);
   }

   public final double getAndSet(double newValue) {
      long next = Double.doubleToRawLongBits(newValue);
      return Double.longBitsToDouble(updater.getAndSet(this, next));
   }

   public final boolean compareAndSet(double expect, double update) {
      return updater.compareAndSet(this, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
   }

   public final boolean weakCompareAndSet(double expect, double update) {
      return updater.weakCompareAndSet(this, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
   }

   @CanIgnoreReturnValue
   public final double getAndAdd(double delta) {
      return this.getAndAccumulate(delta, Double::sum);
   }

   @CanIgnoreReturnValue
   public final double addAndGet(double delta) {
      return this.accumulateAndGet(delta, Double::sum);
   }

   @CanIgnoreReturnValue
   public final double getAndAccumulate(double x, DoubleBinaryOperator accumulatorFunction) {
      Preconditions.checkNotNull(accumulatorFunction);
      return this.getAndUpdate((oldValue) -> {
         return accumulatorFunction.applyAsDouble(oldValue, x);
      });
   }

   @CanIgnoreReturnValue
   public final double accumulateAndGet(double x, DoubleBinaryOperator accumulatorFunction) {
      Preconditions.checkNotNull(accumulatorFunction);
      return this.updateAndGet((oldValue) -> {
         return accumulatorFunction.applyAsDouble(oldValue, x);
      });
   }

   @CanIgnoreReturnValue
   public final double getAndUpdate(DoubleUnaryOperator updateFunction) {
      long current;
      double currentVal;
      long next;
      do {
         current = this.value;
         currentVal = Double.longBitsToDouble(current);
         double nextVal = updateFunction.applyAsDouble(currentVal);
         next = Double.doubleToRawLongBits(nextVal);
      } while(!updater.compareAndSet(this, current, next));

      return currentVal;
   }

   @CanIgnoreReturnValue
   public final double updateAndGet(DoubleUnaryOperator updateFunction) {
      long current;
      double nextVal;
      long next;
      do {
         current = this.value;
         double currentVal = Double.longBitsToDouble(current);
         nextVal = updateFunction.applyAsDouble(currentVal);
         next = Double.doubleToRawLongBits(nextVal);
      } while(!updater.compareAndSet(this, current, next));

      return nextVal;
   }

   public String toString() {
      return Double.toString(this.get());
   }

   public int intValue() {
      return (int)this.get();
   }

   public long longValue() {
      return (long)this.get();
   }

   public float floatValue() {
      return (float)this.get();
   }

   public double doubleValue() {
      return this.get();
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();
      s.writeDouble(this.get());
   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      this.set(s.readDouble());
   }
}
