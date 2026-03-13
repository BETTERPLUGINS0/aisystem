package com.volmit.iris.util.math;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

public class RNGV2 extends SecureRandom {
   private static final long serialVersionUID = 5222938581174415179L;
   private static final char[] CHARGEN = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-=!@#$%^&*()_+`~[];',./<>?:\\\"{}|\\\\".toCharArray();
   private final long sx;

   public RNGV2() {
      this.sx = 0L;
   }

   public RNGV2(long seed) {
      this.setSeed(var1);
      this.sx = var1;
   }

   public RNGV2(String seed) {
      this(UUID.nameUUIDFromBytes(var1.getBytes(StandardCharsets.UTF_8)).getLeastSignificantBits() + UUID.nameUUIDFromBytes(var1.getBytes(StandardCharsets.UTF_8)).getMostSignificantBits() + (long)var1.length() * 32564L);
   }

   public RNGV2 nextParallelRNG(int signature) {
      return new RNGV2(this.sx + (long)var1);
   }

   public RNGV2 nextParallelRNG(long signature) {
      return new RNGV2(this.sx + var1);
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

   public short si(int lowerBound, int upperBound) {
      return (short)((int)((float)var1 + this.nextFloat() * (float)(var2 - var1 + 1)));
   }

   public short si(int upperBound) {
      return this.si(0, var1);
   }

   public short si() {
      return this.si(1);
   }

   public float f(float lowerBound, float upperBound) {
      return var1 + this.nextFloat() * (var2 - var1);
   }

   public float f(float upperBound) {
      return this.f(0.0F, var1);
   }

   public float f() {
      return this.f(1.0F);
   }

   public double d(double lowerBound, double upperBound) {
      return var1 + this.nextDouble() * (var3 - var1);
   }

   public double d(double upperBound) {
      return this.d(0.0D, var1);
   }

   public double d() {
      return this.nextDouble();
   }

   public int i(int lowerBound, int upperBound) {
      if (var1 >= var2) {
         throw new IllegalArgumentException("Upper bound must be greater than lower bound");
      } else {
         return var1 + this.nextInt(var2 - var1 + 1);
      }
   }

   public int i(int upperBound) {
      return this.i(0, var1);
   }

   public long l(long lowerBound, long upperBound) {
      return Math.round(this.d((double)var1, (double)var3));
   }

   public long l(long upperBound) {
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
      return this.nextDouble() <= var1;
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
