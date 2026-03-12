package me.SuperRonanCraft.BetterRTP.player.rtp.effects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import me.SuperRonanCraft.BetterRTP.versions.AsyncHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.ParticleData;

public class RTPEffect_Particles {
   private boolean enabled;
   private final List<ParticleEffect> effects = new ArrayList();
   private String shape;
   private final int precision = 16;
   public static String[] shapeTypes = new String[]{"SCAN", "EXPLODE", "TELEPORT"};

   void load() {
      FileOther.FILETYPE config = this.getPl().getFiles().getType(FileOther.FILETYPE.EFFECTS);
      this.enabled = config.getBoolean("Particles.Enabled");
      if (this.enabled) {
         Object types;
         if (config.isList("Particles.Type")) {
            types = config.getStringList("Particles.Type");
         } else {
            types = new ArrayList();
            ((List)types).add(config.getString("Particles.Type"));
         }

         Object typeTrying = null;

         try {
            Iterator var4 = ((List)types).iterator();

            while(var4.hasNext()) {
               String type = (String)var4.next();
               this.effects.add(ParticleEffect.valueOf(type.toUpperCase()));
            }
         } catch (NullPointerException | IllegalArgumentException var6) {
            this.effects.clear();
            this.effects.add(ParticleEffect.ASH);
            this.getPl().getLogger().severe("The particle '" + typeTrying + "' doesn't exist! Default particle enabled... Try using '/rtp info particles' to get a list of available particles");
         } catch (NoClassDefFoundError | ExceptionInInitializerError var7) {
            this.effects.clear();
            this.getPl().getLogger().severe("The particle '" + typeTrying + "' created a fatal error when loading particles! Your MC version isn't supported!");
            this.enabled = false;
         }

         this.shape = config.getString("Particles.Shape").toUpperCase();
         if (!Arrays.asList(shapeTypes).contains(this.shape)) {
            this.getPl().getLogger().severe("The particle shape '" + this.shape + "' doesn't exist! Default particle shape enabled...");
            this.getPl().getLogger().severe("Try using '/rtp info shapes' to get a list of shapes, or: " + Arrays.asList(shapeTypes));
            this.shape = shapeTypes[0];
         }

      }
   }

   public void display(Player p) {
      if (this.enabled) {
         AsyncHandler.async(() -> {
            try {
               String var2 = this.shape;
               byte var3 = -1;
               switch(var2.hashCode()) {
               case -591166271:
                  if (var2.equals("EXPLODE")) {
                     var3 = 1;
                  }
                  break;
               case -577575125:
                  if (var2.equals("TELEPORT")) {
                     var3 = 0;
                  }
                  break;
               case 2539133:
                  if (var2.equals("SCAN")) {
                     var3 = 3;
                  }
               }

               switch(var3) {
               case 0:
                  this.partTeleport(p);
                  break;
               case 1:
                  this.partExplosion(p);
                  break;
               case 2:
               case 3:
               default:
                  this.partScan(p);
               }
            } catch (Exception var4) {
               var4.printStackTrace();
            }

         });
      }
   }

   private void partScan(Player p) {
      Location loc = p.getLocation().add(new Vector(0.0D, 1.75D, 0.0D));

      for(int index = 1; index < 16; ++index) {
         Vector vec = this.getVecCircle(index);
         Iterator var5 = this.effects.iterator();

         while(var5.hasNext()) {
            ParticleEffect effect = (ParticleEffect)var5.next();
            effect.display(loc.clone().add(vec), new Vector(0.0D, -0.125D, 0.0D), 0.15F, 0, (ParticleData)null, (Player[])(p));
         }
      }

   }

   private void partTeleport(Player p) {
      Location loc = p.getLocation();

      for(float y = 2.5F; y > 0.0F; y -= 0.25F) {
         for(int index = 1; index < 16; ++index) {
            Vector vec = this.getVecCircle(index).add(new Vector(0.0F, y, 0.0F));
            Iterator var6 = this.effects.iterator();

            while(var6.hasNext()) {
               ParticleEffect effect = (ParticleEffect)var6.next();
               effect.display(loc.clone().add(vec), p);
            }
         }
      }

   }

   private void partExplosion(Player p) {
      Location loc = p.getLocation().add(new Vector(0, 1, 0));

      for(int index = 1; index < 16; ++index) {
         Vector vec = this.getVecCircle(index);
         Iterator var5 = this.effects.iterator();

         while(var5.hasNext()) {
            ParticleEffect effect = (ParticleEffect)var5.next();
            effect.display(loc.clone().add(vec), vec, 1.5F, 0, (ParticleData)null, (Player[])(p));
         }
      }

   }

   private Vector getVecCircle(int index) {
      double p1 = (double)index * 3.141592653589793D / 8.0D;
      double p2 = (double)(index - 1) * 3.141592653589793D / 8.0D;
      int radius = 3;
      double x1 = Math.cos(p1) * (double)radius;
      double x2 = Math.cos(p2) * (double)radius;
      double z1 = Math.sin(p1) * (double)radius;
      double z2 = Math.sin(p2) * (double)radius;
      return new Vector(x2 - x1, 0.0D, z2 - z1);
   }

   private BetterRTP getPl() {
      return BetterRTP.getInstance();
   }
}
