package com.volmit.iris.util.math;

import com.volmit.iris.util.documentation.Exclusive;
import com.volmit.iris.util.documentation.Inclusive;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RNG extends Random {
   public static final RNG r = new RNG();
   private static final char[] CHARGEN = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-=!@#$%^&*()_+`~[];',./<>?:\\\"{}|\\\\".toCharArray();
   private static final long serialVersionUID = 5222938581174415179L;
   private final long sx;

   public RNG() {
      this.sx = 0L;
   }

   public RNG(long seed) {
      super(var1);
      this.sx = var1;
   }

   public RNG(String seed) {
      this(UUID.nameUUIDFromBytes(var1.getBytes(StandardCharsets.UTF_8)).getLeastSignificantBits() + UUID.nameUUIDFromBytes(var1.getBytes(StandardCharsets.UTF_8)).getMostSignificantBits() + (long)var1.length() * 32564L);
   }

   public RNG nextParallelRNG(int signature) {
      return new RNG(this.sx + (long)var1);
   }

   public RNG nextParallelRNG(long signature) {
      return new RNG(this.sx + var1);
   }

   public String s(int length) {
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.append(this.c());
      }

      return var2.toString();
   }

   public char c() {
      return CHARGEN[this.i(CHARGEN.length - 1)];
   }

   public <T> T e(Class<T> t) {
      Object[] var2 = var1.getEnumConstants();
      return var2[this.i(var2.length)];
   }

   public boolean b() {
      return this.nextBoolean();
   }

   public boolean b(double percent) {
      return this.d() > var1;
   }

   public short si(@Inclusive int lowerBound, @Inclusive int upperBound) {
      return (short)((int)((float)var1 + this.nextFloat() * (float)(var2 - var1 + 1)));
   }

   public short si(@Inclusive int upperBound) {
      return this.si(0, var1);
   }

   public short si() {
      return this.si(1);
   }

   public float f(@Inclusive float lowerBound, @Exclusive float upperBound) {
      return var1 + this.nextFloat() * (var2 - var1);
   }

   public float f(@Exclusive float upperBound) {
      return this.f(0.0F, var1);
   }

   public float f() {
      return this.f(1.0F);
   }

   public double d(@Inclusive double lowerBound, @Exclusive double upperBound) {
      return var1 > var3 ? M.lerp(var3, var1, this.nextDouble()) : M.lerp(var1, var3, this.nextDouble());
   }

   public double d(@Exclusive double upperBound) {
      return this.d(0.0D, var1);
   }

   public double d() {
      return this.d(1.0D);
   }

   public int i(@Inclusive int lowerBound, @Exclusive int upperBound) {
      return (int)Math.floor(this.d((double)var1, (double)var2));
   }

   public int i(@Exclusive int upperBound) {
      return this.i(Math.min(var1, 0), Math.max(0, var1));
   }

   public long l(@Inclusive long lowerBound, @Exclusive long upperBound) {
      return Math.round(this.d((double)var1, (double)var3));
   }

   public long l(@Exclusive long upperBound) {
      return this.l(0L, var1);
   }

   public int imax() {
      return this.i(Integer.MIN_VALUE, Integer.MAX_VALUE);
   }

   public long lmax() {
      return this.l(Long.MIN_VALUE, Long.MAX_VALUE);
   }

   public float fmax() {
      return this.f(Float.MIN_VALUE, Float.MAX_VALUE);
   }

   public double dmax() {
      return this.d(Double.MIN_VALUE, Double.MAX_VALUE);
   }

   public short simax() {
      return this.si(-32768, 32767);
   }

   public boolean chance(double chance) {
      return var1 >= this.nextDouble();
   }

   public <T> T pick(List<T> pieces) {
      if (var1.isEmpty()) {
         return null;
      } else {
         return var1.size() == 1 ? var1.get(0) : var1.get(this.nextInt(var1.size()));
      }
   }

   public long getSeed() {
      return this.sx;
   }
}
