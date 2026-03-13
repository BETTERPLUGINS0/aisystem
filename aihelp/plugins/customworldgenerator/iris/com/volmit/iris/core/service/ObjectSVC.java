package com.volmit.iris.core.service;

import com.volmit.iris.Iris;
import com.volmit.iris.util.plugin.IrisService;
import com.volmit.iris.util.scheduling.J;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class ObjectSVC implements IrisService {
   private final Deque<Map<Block, BlockData>> undos = new ArrayDeque();

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void addChanges(Map<Block, BlockData> oldBlocks) {
      this.undos.add(var1);
   }

   public void revertChanges(int amount) {
      this.loopChange(var1);
   }

   private void loopChange(int amount) {
      if (this.undos.size() > 0) {
         this.revert((Map)this.undos.pollLast());
         if (var1 > 1) {
            J.s(() -> {
               this.loopChange(var1 - 1);
            }, 2);
         }
      }

   }

   private void revert(Map<Block, BlockData> blocks) {
      Iterator var2 = var1.entrySet().iterator();
      Bukkit.getScheduler().runTask(Iris.instance, () -> {
         int var3 = 0;

         while(var2.hasNext()) {
            Entry var4 = (Entry)var2.next();
            BlockData var5 = (BlockData)var4.getValue();
            ((Block)var4.getKey()).setBlockData(var5, false);
            var2.remove();
            ++var3;
            if (var3 > 200) {
               J.s(() -> {
                  this.revert(var1);
               }, 1);
            }
         }

      });
   }

   @Generated
   public Deque<Map<Block, BlockData>> getUndos() {
      return this.undos;
   }
}
