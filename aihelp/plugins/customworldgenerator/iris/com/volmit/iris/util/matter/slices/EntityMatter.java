package com.volmit.iris.util.matter.slices;

import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.Varint;
import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.MatterEntity;
import com.volmit.iris.util.matter.MatterEntityGroup;
import com.volmit.iris.util.matter.MatterReader;
import com.volmit.iris.util.matter.Sliced;
import com.volmit.iris.util.nbt.io.NBTUtil;
import com.volmit.iris.util.nbt.tag.CompoundTag;
import com.volmit.iris.util.nbt.tag.Tag;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;

@Sliced
public class EntityMatter extends RawMatter<MatterEntityGroup> {
   public static final MatterEntityGroup EMPTY = new MatterEntityGroup();
   private transient KMap<IrisPosition, KList<Entity>> entityCache;

   public EntityMatter() {
      this(1, 1, 1);
   }

   public EntityMatter(int width, int height, int depth) {
      super(var1, var2, var3, MatterEntityGroup.class);
      this.entityCache = new KMap();
      this.registerWriter(World.class, (var0, var1x, var2x, var3x, var4) -> {
         Iterator var5 = var1x.getEntities().iterator();

         while(var5.hasNext()) {
            MatterEntity var6 = (MatterEntity)var5.next();
            Location var7 = new Location(var0, (double)var2x + var6.getXOff(), (double)var3x + var6.getYOff(), (double)var4 + var6.getZOff());
            INMS.get().deserializeEntity(var6.getEntityData(), var7);
         }

      });
      this.registerReader(World.class, (var1x, var2x, var3x, var4) -> {
         IrisPosition var5 = new IrisPosition(var2x, var3x, var4);
         KList var6 = (KList)this.entityCache.get(var5);
         MatterEntityGroup var7 = new MatterEntityGroup();
         if (var6 == null) {
            return null;
         } else {
            Iterator var8 = var6.iterator();

            while(var8.hasNext()) {
               Entity var9 = (Entity)var8.next();
               var7.getEntities().add((Object)(new MatterEntity(Math.abs(var9.getLocation().getX()) - (double)Math.abs(var9.getLocation().getBlockX()), Math.abs(var9.getLocation().getY()) - (double)Math.abs(var9.getLocation().getBlockY()), Math.abs(var9.getLocation().getZ()) - (double)Math.abs(var9.getLocation().getBlockZ()), INMS.get().serializeEntity(var9))));
            }

            return var7;
         }
      });
   }

   public Palette<MatterEntityGroup> getGlobalPalette() {
      return null;
   }

   public synchronized <W> boolean readFrom(W w, int x, int y, int z) {
      if (!(var1 instanceof World)) {
         return super.readFrom(var1, var2, var3, var4);
      } else {
         MatterReader var5 = this.readFrom(World.class);
         if (var5 == null) {
            return false;
         } else {
            this.entityCache = new KMap();
            Iterator var6 = ((World)var1).getNearbyEntities(new BoundingBox((double)var2, (double)var3, (double)var4, (double)(var2 + this.getWidth()), (double)(var3 + this.getHeight()), (double)(var4 + this.getHeight()))).iterator();

            while(var6.hasNext()) {
               Entity var7 = (Entity)var6.next();
               ((KList)this.entityCache.computeIfAbsent(new IrisPosition(var7.getLocation()), (var0) -> {
                  return new KList();
               })).add((Object)var7);
            }

            var6 = this.entityCache.keySet().iterator();

            while(var6.hasNext()) {
               IrisPosition var9 = (IrisPosition)var6.next();
               MatterEntityGroup var8 = (MatterEntityGroup)var5.readMatter(var1, var9.getX(), var9.getY(), var9.getZ());
               if (var8 != null) {
                  this.set(var9.getX() - var2, var9.getY() - var3, var9.getZ() - var4, var8);
               }
            }

            this.entityCache.clear();
            return true;
         }
      }
   }

   public void writeNode(MatterEntityGroup b, DataOutputStream dos) {
      Varint.writeUnsignedVarInt(var1.getEntities().size(), var2);
      Iterator var3 = var1.getEntities().iterator();

      while(var3.hasNext()) {
         MatterEntity var4 = (MatterEntity)var3.next();
         var2.writeByte((int)(var4.getXOff() * 255.0D) + -128);
         var2.writeByte((int)(var4.getYOff() * 255.0D) + -128);
         var2.writeByte((int)(var4.getZOff() * 255.0D) + -128);
         NBTUtil.write((Tag)var4.getEntityData(), (OutputStream)var2, false);
      }

   }

   public MatterEntityGroup readNode(DataInputStream din) {
      MatterEntityGroup var2 = new MatterEntityGroup();
      int var3 = Varint.readUnsignedVarInt((DataInput)var1);

      while(var3-- > 0) {
         var2.getEntities().add((Object)(new MatterEntity((double)((float)(var1.readByte() - -128) / 255.0F), (double)((float)(var1.readByte() - -128) / 255.0F), (double)((float)(var1.readByte() - -128) / 255.0F), (CompoundTag)NBTUtil.read((InputStream)var1, false).getTag())));
      }

      return var2;
   }
}
