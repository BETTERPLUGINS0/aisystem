package com.volmit.iris.util.stream.interpolation;

import com.volmit.iris.util.interpolation.InterpolationMethod;
import com.volmit.iris.util.stream.ProceduralStream;

public class InterpolatorFactory<T> {
   private final ProceduralStream<T> stream;

   public InterpolatorFactory(ProceduralStream<T> stream) {
      this.stream = var1;
   }

   public InterpolatingStream<T> with(InterpolationMethod t, int rx) {
      return new InterpolatingStream(this.stream, var2, var1);
   }

   public TrilinearStream<T> trilinear(int rx, int ry, int rz) {
      return new TrilinearStream(this.stream, var1, var2, var3);
   }

   public TricubicStream<T> tricubic(int rx, int ry, int rz) {
      return new TricubicStream(this.stream, var1, var2, var3);
   }

   public BicubicStream<T> bicubic(int rx, int ry) {
      return new BicubicStream(this.stream, var1, var2);
   }

   public BicubicStream<T> bicubic(int r) {
      return this.bicubic(var1, var1);
   }

   public BilinearStream<T> bilinear(int rx, int ry) {
      return new BilinearStream(this.stream, var1, var2);
   }

   public BilinearStream<T> bilinear(int r) {
      return this.bilinear(var1, var1);
   }

   public TriStarcastStream<T> tristarcast(int radius, int checks) {
      return new TriStarcastStream(this.stream, var1, var2);
   }

   public TriStarcastStream<T> tristarcast3(int radius) {
      return this.tristarcast(var1, 3);
   }

   public TriStarcastStream<T> tristarcast6(int radius) {
      return this.tristarcast(var1, 6);
   }

   public TriStarcastStream<T> tristarcast9(int radius) {
      return this.tristarcast(var1, 9);
   }

   public BiStarcastStream<T> bistarcast(int radius, int checks) {
      return new BiStarcastStream(this.stream, var1, var2);
   }

   public BiStarcastStream<T> bistarcast3(int radius) {
      return this.bistarcast(var1, 3);
   }

   public BiStarcastStream<T> bistarcast6(int radius) {
      return this.bistarcast(var1, 6);
   }

   public BiStarcastStream<T> bistarcast9(int radius) {
      return this.bistarcast(var1, 9);
   }

   public BiHermiteStream<T> bihermite(int rx, int ry, double tension, double bias) {
      return new BiHermiteStream(this.stream, var1, var2, var3, var5);
   }

   public BiHermiteStream<T> bihermite(int rx, int ry) {
      return new BiHermiteStream(this.stream, var1, var2);
   }

   public BiHermiteStream<T> bihermite(int r) {
      return this.bihermite(var1, var1);
   }

   public BiHermiteStream<T> bihermite(int r, double tension, double bias) {
      return this.bihermite(var1, var1, var2, var4);
   }
}
