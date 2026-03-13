package com.volmit.iris.util.matter.slices.container;

import com.volmit.iris.engine.object.IrisJigsawStructure;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.math.Position2;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.Unmodifiable;

public class JigsawStructuresContainer {
   private final Map<String, List<Position2>> map = new KMap();

   public JigsawStructuresContainer() {
   }

   public JigsawStructuresContainer(DataInputStream din) {
      int var2 = var1.readInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = var1.readInt();
         KList var5 = new KList(var4);

         for(int var6 = 0; var6 < var4; ++var6) {
            var5.add((Object)(new Position2(var1.readInt(), var1.readInt())));
         }

         this.map.put(var1.readUTF(), var5);
      }

   }

   public void write(DataOutputStream dos) {
      var1.writeInt(this.map.size());
      Iterator var2 = this.map.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         List var4 = (List)this.map.get(var3);
         var1.writeInt(var4.size());
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            Position2 var6 = (Position2)var5.next();
            var1.writeInt(var6.getX());
            var1.writeInt(var6.getZ());
         }

         var1.writeUTF(var3);
      }

   }

   @Unmodifiable
   public Set<String> getStructures() {
      return Collections.unmodifiableSet(this.map.keySet());
   }

   @Unmodifiable
   public List<Position2> getPositions(String structure) {
      return Collections.unmodifiableList((List)this.map.get(var1));
   }

   @ChunkCoordinates
   public void add(IrisJigsawStructure structure, Position2 pos) {
      ((List)this.map.computeIfAbsent(var1.getLoadKey(), (var0) -> {
         return new KList();
      })).add(var2);
   }
}
