package advancedplugins.pm2.cv.models.api.model.rpc.generator.util;

import advancedplugins.pm2.cv.models.api.utils.math.Axis;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.processed.ProcessedJoint;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Map.Entry;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class RotationSolver {
   private static final List<Vector3d> CARDINAL_AXES = new ArrayList<Vector3d>() {
      {
         this.add(new Vector3d(1.0D, 0.0D, 0.0D));
         this.add(new Vector3d(0.0D, 1.0D, 0.0D));
         this.add(new Vector3d(0.0D, 0.0D, 1.0D));
         this.add(new Vector3d(-1.0D, 0.0D, 0.0D));
         this.add(new Vector3d(0.0D, -1.0D, 0.0D));
         this.add(new Vector3d(0.0D, 0.0D, -1.0D));
      }
   };
   private final Int2ObjectMap<ProcessedJoint.Cube> cubes = new Int2ObjectOpenHashMap();
   private final Int2ObjectMap<List<Vector3d>> axes = new Int2ObjectOpenHashMap();

   private RotationSolver() {
   }

   public static void solve(Collection<ItemGroup> var0, Collection<ProcessedJoint.Cube> var1) {
      RotationSolver var2 = new RotationSolver();
      var2.initialize(var0, var1);
      var2.solve(var0);
   }

   private static <T, R extends Collection<S>, S> Map<T, R> fetch(Map<T, R> var0) {
      PriorityQueue var1 = new PriorityQueue(Comparator.comparing((var0x) -> {
         return ((Collection)var0x.right()).size();
      }).reversed());
      var0.forEach((var1x, var2x) -> {
         var1.add(Pair.of(var1x, var2x));
      });
      Object2ObjectLinkedOpenHashMap var2 = new Object2ObjectLinkedOpenHashMap();

      while(!var1.isEmpty()) {
         Pair var3 = (Pair)var1.poll();
         if (((Collection)var3.right()).isEmpty()) {
            break;
         }

         var1.clear();
         var0.remove(var3.left());
         var2.put(var3.left(), (Collection)var3.right());
         var0.forEach((var2x, var3x) -> {
            var3x.removeAll((Collection)var3.right());
            var1.add(Pair.of(var2x, var3x));
         });
      }

      return var2;
   }

   private static int getGroupId(double var0) {
      while(var0 < 0.0D) {
         var0 += 360.0D;
      }

      int var2 = (int)Math.round(var0 * 10000.0D);
      return var2 % 225000;
   }

   private static boolean isGroupable(Vector3d var0) {
      Axis[] var1 = Axis.values();
      Axis[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Axis var5 = var2[var4];
         if (getGroupId(Math.acos(var0.dot(var5.getVector())) * 57.295780181884766D) != 0) {
            return false;
         }
      }

      return true;
   }

   private static Vector3d getClosestCardinal(Vector3d var0) {
      if (var0 != null && CARDINAL_AXES != null && !CARDINAL_AXES.isEmpty()) {
         double var1 = Double.NEGATIVE_INFINITY;
         Vector3d var3 = (Vector3d)CARDINAL_AXES.get(0);
         Iterator var4 = CARDINAL_AXES.iterator();

         while(var4.hasNext()) {
            Vector3d var5 = (Vector3d)var4.next();
            if (var5 != null) {
               double var6 = var0.dot(var5);
               if (var6 > var1) {
                  var1 = var6;
                  var3 = var5;
               }
            }
         }

         return var3;
      } else {
         System.out.println("No cardinal axes: " + String.valueOf(var0));
         return null;
      }
   }

   private static boolean isLegal(ProcessedJoint.Cube var0) {
      return legalId(var0.getRotation().x) + legalId(var0.getRotation().y) + legalId(var0.getRotation().z) < 2;
   }

   private static int legalId(double var0) {
      if (MathUtils.isInterval(var0, 90.0D)) {
         return 0;
      } else {
         return MathUtils.isInterval(var0, 22.5D) ? 1 : 999;
      }
   }

   private void initialize(Collection<ItemGroup> var1, Collection<ProcessedJoint.Cube> var2) {
      int var3 = 0;
      ArrayList var4 = new ArrayList();
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         ProcessedJoint.Cube var6 = (ProcessedJoint.Cube)var5.next();
         if (isLegal(var6)) {
            var4.add(IllegalRotationSolver.solve(var6));
         } else {
            this.cubes.put(var3++, var6);
         }
      }

      if (!var4.isEmpty()) {
         var1.add(new ItemGroup(-1, new Quaterniond(), new Vector3d(), var4));
      }

   }

   private void solve(Collection<ItemGroup> var1) {
      Map var2 = this.groupByAxis();
      List var3 = this.groupByModulo(var2);
      Map var4 = this.combineGroup(var3);
      this.fixGroups(var1, var4);
   }

   private void simpleConvert(Collection<ItemGroup> var1, List<PlaneGroup> var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         PlaneGroup var4 = (PlaneGroup)var3.next();
         ArrayList var5 = new ArrayList();
         this.rotateCubes(var5, var4.cubes(), var4.invOrigin());
         var1.add(new ItemGroup(0, new Quaterniond(var4.origin()), MathUtils.toEulerXYZ(var4.origin()), var5));
      }

   }

   private List<Vector3d> toAxes(int var1) {
      ProcessedJoint.Cube var2 = (ProcessedJoint.Cube)this.cubes.get(var1);
      Vector3d var3 = MathUtils.fixVector((new HashedVector3d(Axis.X, 1.0D, 0.0D, 0.0D)).rotate(var2.getQuaternion()));
      Vector3d var4 = MathUtils.fixVector((new HashedVector3d(Axis.Y, 0.0D, 1.0D, 0.0D)).rotate(var2.getQuaternion()));
      Vector3d var5 = MathUtils.fixVector((new HashedVector3d(Axis.Z, 0.0D, 0.0D, 1.0D)).rotate(var2.getQuaternion()));
      return Arrays.asList(var3, var4, var5);
   }

   private Map<Vector3d, IntLinkedOpenHashSet> groupByAxis() {
      Object2ObjectLinkedOpenHashMap var1 = new Object2ObjectLinkedOpenHashMap();
      IntIterator var2 = this.cubes.keySet().iterator();

      while(var2.hasNext()) {
         int var3 = (Integer)var2.next();
         List var4 = this.toAxes(var3);
         this.axes.put(var3, var4);
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            Vector3d var6 = (Vector3d)var5.next();
            ((IntLinkedOpenHashSet)var1.computeIfAbsent(var6, (var0) -> {
               return new IntLinkedOpenHashSet();
            })).add(var3);
         }
      }

      return fetch(var1);
   }

   private List<PlaneGroup> groupByModulo(Map<Vector3d, IntLinkedOpenHashSet> var1) {
      Object2ObjectLinkedOpenHashMap var2 = new Object2ObjectLinkedOpenHashMap();
      ArrayList var3 = new ArrayList();
      Iterator var4 = var1.entrySet().iterator();

      int var5;
      while(var4.hasNext()) {
         Entry var6 = (Entry)var4.next();
         Quaterniond var7 = null;
         IntListIterator var8 = ((IntLinkedOpenHashSet)var6.getValue()).iterator();

         while(var8.hasNext()) {
            Integer var9 = (Integer)var8.next();
            var5 = var9;
            ProcessedJoint.Cube var10 = (ProcessedJoint.Cube)this.cubes.get(var5);
            double var11;
            if (var7 == null) {
               var7 = var10.getQuaternion().invert(new Quaterniond());
               var11 = 0.0D;
            } else {
               Quaterniond var13 = var7.mul(var10.getQuaternion(), new Quaterniond());
               Vector3d var14 = new Vector3d(var13.x, var13.y, var13.z);
               double var15 = Math.signum(var14.dot((Vector3dc)var6.getKey()));
               var11 = var15 * 2.0D * Math.acos(var13.w) * 57.29577951308232D;
            }

            int var22 = getGroupId(var11);
            ((IntSet)((Int2ObjectMap)var2.computeIfAbsent((Vector3d)var6.getKey(), (var0) -> {
               return new Int2ObjectLinkedOpenHashMap();
            })).computeIfAbsent(var22, (var0) -> {
               return new IntLinkedOpenHashSet();
            })).add(var5);
         }
      }

      ObjectBidirectionalIterator var17 = var2.entrySet().iterator();

      while(var17.hasNext()) {
         Entry var18 = (Entry)var17.next();
         ObjectIterator var19 = ((Int2ObjectMap)var18.getValue()).int2ObjectEntrySet().iterator();

         while(var19.hasNext()) {
            it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry var20 = (it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry)var19.next();
            var5 = ((IntSet)var20.getValue()).iterator().nextInt();
            Quaterniond var21 = ((ProcessedJoint.Cube)this.cubes.get(var5)).getQuaternion();
            var3.add(new PlaneGroup(new Vector3d((double)var20.getKey()), var20.getIntKey(), new Quaterniond(var21), var21.invert(new Quaterniond()), (IntSet)var20.getValue()));
         }
      }

      return var3;
   }

   private Map<PlaneGroup, Set<PlaneGroup>> combineGroup(List<PlaneGroup> var1) {
      Object2ObjectLinkedOpenHashMap var2 = new Object2ObjectLinkedOpenHashMap();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         PlaneGroup var4 = (PlaneGroup)var1.get(var3);
         Set var5 = (Set)var2.computeIfAbsent(var4, (var0) -> {
            return new ObjectLinkedOpenHashSet();
         });
         var5.add(var4);

         label55:
         for(int var6 = var3 + 1; var6 < var1.size(); ++var6) {
            PlaneGroup var7 = (PlaneGroup)var1.get(var6);
            Vector3d var8;
            if (MathUtils.isSimilar(var4.axis().dot(var7.axis()), -1.0D)) {
               List var9 = (List)this.axes.get(var4.cubes().iterator().nextInt());
               List var10 = (List)this.axes.get(var7.cubes().iterator().nextInt());

               for(int var11 = 0; var11 < 3; ++var11) {
                  Vector3d var12 = (Vector3d)var9.get(var11);
                  var8 = (Vector3d)var10.get(var11);
                  if (getGroupId(Math.acos(var12.dot(var8)) * 57.29577951308232D) != 0) {
                     continue label55;
                  }
               }
            } else {
               Iterator var14 = var5.iterator();

               while(var14.hasNext()) {
                  PlaneGroup var15 = (PlaneGroup)var14.next();
                  double var16 = var15.axis().dot(var7.axis());
                  if (!MathUtils.isSimilar(var16, 0.0D) && !MathUtils.isSimilar(var16, -1.0D)) {
                     continue label55;
                  }

                  var8 = var7.axis().rotate(var15.invOrigin(), new Vector3d());
                  Vector3d var13 = var15.axis().rotate(var7.invOrigin(), new Vector3d());
                  if (!isGroupable(var8) || !isGroupable(var13)) {
                     continue label55;
                  }
               }
            }

            var5.add(var7);
         }
      }

      return fetch(var2);
   }

   private void fixGroups(Collection<ItemGroup> var1, Map<PlaneGroup, Set<PlaneGroup>> var2) {
      Iterator var3 = var2.entrySet().iterator();

      while(true) {
         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            Set var5 = (Set)var4.getValue();
            if (var5.size() < 2) {
               PlaneGroup var22 = (PlaneGroup)var5.iterator().next();
               var1.add(new ItemGroup(0, new Quaterniond(var22.origin()), MathUtils.toEulerXYZ(var22.origin()), this.rotateCubes(new ArrayList(), var22.cubes(), var22.invOrigin())));
            } else {
               Iterator var6 = var5.iterator();
               PlaneGroup var7 = (PlaneGroup)var6.next();
               Vector3d var8 = var7.axis();
               Vector3d var9 = null;
               int var10 = 0;

               while(var6.hasNext()) {
                  ++var10;
                  var9 = ((PlaneGroup)var6.next()).axis();
                  if (!MathUtils.isSimilar(Math.abs(var8.dot(var9)), 1.0D)) {
                     break;
                  }
               }

               if (!MathUtils.isSimilar(Math.abs(var8.dot(var9)), 1.0D)) {
                  Vector3d var23 = getClosestCardinal(var8);
                  Quaterniond var24;
                  Vector3d var25;
                  if (var8.lengthSquared() < 1.0E-10D) {
                     var24 = new Quaterniond();
                     var25 = new Vector3d(var9);
                  } else {
                     var24 = var8.rotationTo(var23, new Quaterniond());
                     var25 = var9.rotate(var24, new Vector3d());
                  }

                  Vector3d var14 = getClosestCardinal(var25);
                  Quaterniond var15 = var25.rotationTo(var14, new Quaterniond());
                  Quaterniond var16 = var15.mul(var24);
                  Quaterniond var17 = var16.invert(new Quaterniond());
                  Vector3d var18 = MathUtils.fixEuler(MathUtils.toEulerXYZ(var17));
                  ArrayList var19 = new ArrayList();
                  Iterator var20 = var5.iterator();

                  while(var20.hasNext()) {
                     PlaneGroup var21 = (PlaneGroup)var20.next();
                     this.rotateCubes(var19, var21.cubes(), var16);
                  }
               } else {
                  ArrayList var11 = new ArrayList();
                  Iterator var12 = var5.iterator();

                  while(var12.hasNext()) {
                     PlaneGroup var13 = (PlaneGroup)var12.next();
                     this.rotateCubes(var11, var13.cubes(), var7.invOrigin());
                  }

                  var1.add(new ItemGroup(0, new Quaterniond(var7.origin()), MathUtils.toEulerXYZ(var7.origin()), var11));
               }
            }
         }

         return;
      }
   }

   private List<ProcessedJoint.Cube> rotateCubes(List<ProcessedJoint.Cube> var1, IntSet var2, Quaterniond var3) {
      IntIterator var4 = var2.iterator();

      while(var4.hasNext()) {
         int var5 = (Integer)var4.next();
         ProcessedJoint.Cube var6 = (ProcessedJoint.Cube)this.cubes.get(var5);
         var6.rotate(var3);
         var1.add(IllegalRotationSolver.solve(var6));
      }

      return var1;
   }
}
