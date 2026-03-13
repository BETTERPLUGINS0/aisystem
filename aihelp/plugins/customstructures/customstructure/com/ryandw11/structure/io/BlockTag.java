package com.ryandw11.structure.io;

import java.util.List;
import me.ryandw11.ods.Tag;
import me.ryandw11.ods.tags.IntTag;
import me.ryandw11.ods.tags.ObjectTag;
import me.ryandw11.ods.tags.StringTag;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class BlockTag extends ObjectTag {
   public BlockTag(String name, List<Tag<?>> value) {
      super(name, value);
   }

   public BlockTag(String name) {
      super(name);
   }

   public BlockTag(Material type, Location location) {
      int var10001 = location.getBlockX();
      super(var10001 + ";" + location.getBlockY() + ";" + location.getBlockZ());
      this.addTag(new StringTag("type", type.name()));
      this.addTag(new IntTag("x", location.getBlockX()));
      this.addTag(new IntTag("y", location.getBlockY()));
      this.addTag(new IntTag("z", location.getBlockZ()));
   }

   public BlockTag(ObjectTag objTag) {
      String var10001 = String.valueOf(objTag.getTag("x").getValue());
      super(var10001 + ";" + String.valueOf(objTag.getTag("y").getValue()) + ";" + String.valueOf(objTag.getTag("z")));
      this.addTag(objTag.getTag("type"));
      this.addTag(objTag.getTag("x"));
      this.addTag(objTag.getTag("y"));
      this.addTag(objTag.getTag("z"));
   }

   public Location getLocation() {
      IntTag x = (IntTag)this.getTag("x");
      IntTag y = (IntTag)this.getTag("y");
      IntTag z = (IntTag)this.getTag("z");
      return new Location((World)null, (double)x.getValue(), (double)y.getValue(), (double)z.getValue());
   }

   public Location getLocation(World w) {
      IntTag x = (IntTag)this.getTag("x");
      IntTag y = (IntTag)this.getTag("y");
      IntTag z = (IntTag)this.getTag("z");
      return new Location(w, (double)x.getValue(), (double)y.getValue(), (double)z.getValue());
   }

   public Material getType() {
      StringTag tag = (StringTag)this.getTag("type");
      return Material.valueOf(tag.getValue());
   }
}
