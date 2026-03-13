package com.volmit.iris.core.service;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.edit.DustRevealer;
import com.volmit.iris.core.link.WorldEditLink;
import com.volmit.iris.core.wand.WandSelection;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.data.Cuboid;
import com.volmit.iris.util.data.registry.Particles;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.matter.Matter;
import com.volmit.iris.util.matter.WorldMatter;
import com.volmit.iris.util.plugin.IrisService;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.plugin.chunk.TicketHolder;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.SR;
import com.volmit.iris.util.scheduling.jobs.Job;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class WandSVC implements IrisService {
   private static final int MS_PER_TICK = Integer.parseInt(System.getProperty("iris.ms_per_tick", "30"));
   private static ItemStack dust;
   private static ItemStack wand;

   public static void pasteSchematic(IrisObject s, Location at) {
      var0.place(var1);
   }

   public static IrisObject createSchematic(Player p, boolean legacy) {
      if (!isHoldingWand(var0)) {
         return null;
      } else {
         try {
            Location[] var2 = getCuboid(var0);
            if (var2 != null && var2[0] != null && var2[1] != null) {
               final Cuboid var3 = new Cuboid(var2[0], var2[1]);
               final IrisObject var4 = new IrisObject(var3.getSizeX(), var3.getSizeY(), var3.getSizeZ());
               final Iterator var5 = var3.chunkedIterator();
               final int var6 = var3.getSizeX() * var3.getSizeY() * var3.getSizeZ();
               final CountDownLatch var7 = new CountDownLatch(1);
               final TicketHolder var8 = Iris.tickets.getHolder(var0.getWorld());
               (new Job() {
                  private int i;
                  private Chunk chunk;

                  public String getName() {
                     return "Scanning Selection";
                  }

                  public void execute() {
                     SR var10001 = new SR() {
                        // $FF: synthetic field
                        final <undefinedtype> this$0;

                        {
                           this.this$0 = var1;
                        }

                        public void run() {
                           long var1 = M.ms() + (long)WandSVC.MS_PER_TICK;

                           while(var1 > M.ms()) {
                              if (!this.this$0.val$it.hasNext()) {
                                 if (this.this$0.chunk != null) {
                                    this.this$0.val$holder.removeTicket(this.this$0.chunk);
                                    this.this$0.chunk = null;
                                 }

                                 this.cancel();
                                 this.this$0.val$latch.countDown();
                                 return;
                              }

                              try {
                                 Block var3 = (Block)this.this$0.val$it.next();
                                 Chunk var4 = var3.getChunk();
                                 if (this.this$0.chunk == null) {
                                    this.this$0.chunk = var4;
                                    this.this$0.val$holder.addTicket(this.this$0.chunk);
                                 } else if (this.this$0.chunk != var4) {
                                    this.this$0.val$holder.removeTicket(this.this$0.chunk);
                                    this.this$0.val$holder.addTicket(var4);
                                    this.this$0.chunk = var4;
                                 }

                                 if (!var3.getType().equals(Material.AIR)) {
                                    BlockVector var5 = var3.getLocation().subtract(this.this$0.val$c.getLowerNE().toVector()).toVector().toBlockVector();
                                    this.this$0.val$s.setUnsigned(var5.getBlockX(), var5.getBlockY(), var5.getBlockZ(), var3, this.this$0.val$legacy);
                                 }
                              } finally {
                                 ++this.this$0.i;
                              }
                           }

                        }
                     };

                     try {
                        var7.await();
                     } catch (InterruptedException var2) {
                     }

                  }

                  public void completeWork() {
                  }

                  public int getTotalWork() {
                     return var6;
                  }

                  public int getWorkCompleted() {
                     return this.i;
                  }
               }).execute(new VolmitSender(var0), true, () -> {
               });

               try {
                  var7.await();
               } catch (InterruptedException var10) {
               }

               return var4;
            } else {
               return null;
            }
         } catch (Throwable var11) {
            var11.printStackTrace();
            Iris.reportError(var11);
            return null;
         }
      }
   }

   public static Matter createMatterSchem(Player p) {
      if (!isHoldingWand(var0)) {
         return null;
      } else {
         try {
            Location[] var1 = getCuboid(var0);
            return WorldMatter.createMatter(var0.getName(), var1[0], var1[1]);
         } catch (Throwable var2) {
            var2.printStackTrace();
            Iris.reportError(var2);
            return null;
         }
      }
   }

   public static Location stringToLocation(String s) {
      try {
         String[] var1 = var0.split("\\Q in \\E");
         if (var1.length != 2) {
            return null;
         } else {
            String[] var2 = var1[0].split("\\Q,\\E");
            return var2.length != 3 ? null : new Location(Bukkit.getWorld(var1[1]), (double)Integer.parseInt(var2[0]), (double)Integer.parseInt(var2[1]), (double)Integer.parseInt(var2[2]));
         }
      } catch (Throwable var3) {
         Iris.reportError(var3);
         return null;
      }
   }

   public static String locationToString(Location loc) {
      if (var0 == null) {
         return "<#>";
      } else {
         int var10000 = var0.getBlockX();
         return var10000 + "," + var0.getBlockY() + "," + var0.getBlockZ() + " in " + var0.getWorld().getName();
      }
   }

   public static ItemStack createWand() {
      return createWand((Location)null, (Location)null);
   }

   public static ItemStack createDust() {
      ItemStack var0 = new ItemStack(Material.GLOWSTONE_DUST);
      var0.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
      ItemMeta var1 = var0.getItemMeta();
      String var10001 = String.valueOf(C.BOLD);
      var1.setDisplayName(var10001 + String.valueOf(C.YELLOW) + "Dust of Revealing");
      var1.setUnbreakable(true);
      var1.addItemFlags(ItemFlag.values());
      var1.setLore((new KList()).qadd("Right click on a block to reveal it's placement structure!"));
      var0.setItemMeta(var1);
      return var0;
   }

   public static int findWand(Inventory inventory) {
      ItemStack var1 = createWand();
      ItemMeta var2 = var1.getItemMeta();
      var2.setLore(new ArrayList());
      var1.setItemMeta(var2);

      for(int var3 = 0; var3 < var0.getSize(); ++var3) {
         ItemStack var4 = var0.getItem(var3);
         if (var4 != null) {
            var2 = var4.getItemMeta();
            var2.setLore(new ArrayList());
            var4.setItemMeta(var2);
            if (var1.isSimilar(var4)) {
               return var3;
            }
         }
      }

      return -1;
   }

   public static ItemStack createWand(Location a, Location b) {
      ItemStack var2 = new ItemStack(Material.BLAZE_ROD);
      var2.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
      ItemMeta var3 = var2.getItemMeta();
      String var10001 = String.valueOf(C.BOLD);
      var3.setDisplayName(var10001 + String.valueOf(C.GOLD) + "Wand of Iris");
      var3.setUnbreakable(true);
      var3.addItemFlags(ItemFlag.values());
      var3.setLore((new KList()).add((Object[])(locationToString(var0), locationToString(var1))));
      var2.setItemMeta(var3);
      return var2;
   }

   public static Location[] getCuboidFromItem(ItemStack is) {
      ItemMeta var1 = var0.getItemMeta();
      return new Location[]{stringToLocation((String)var1.getLore().get(0)), stringToLocation((String)var1.getLore().get(1))};
   }

   public static Location[] getCuboid(Player p) {
      if (isHoldingIrisWand(var0)) {
         return getCuboidFromItem(var0.getInventory().getItemInMainHand());
      } else {
         if (IrisSettings.get().getWorld().worldEditWandCUI) {
            Cuboid var1 = WorldEditLink.getSelection(var0);
            if (var1 != null) {
               return new Location[]{var1.getLowerNE(), var1.getUpperSW()};
            }
         }

         return null;
      }
   }

   public static boolean isHoldingWand(Player p) {
      return isHoldingIrisWand(var0) || IrisSettings.get().getWorld().worldEditWandCUI && WorldEditLink.getSelection(var0) != null;
   }

   public static boolean isHoldingIrisWand(Player p) {
      ItemStack var1 = var0.getInventory().getItemInMainHand();
      return var1 != null && isWand(var1);
   }

   public static boolean isWand(ItemStack is) {
      if (var0.getItemMeta() == null) {
         return false;
      } else {
         return var0.getType().equals(wand.getType()) && var0.getItemMeta().getDisplayName().equals(wand.getItemMeta().getDisplayName()) && var0.getItemMeta().getEnchants().equals(wand.getItemMeta().getEnchants()) && var0.getItemMeta().getItemFlags().equals(wand.getItemMeta().getItemFlags());
      }
   }

   public void onEnable() {
      wand = createWand();
      dust = createDust();
      J.ar(() -> {
         Iterator var1 = Bukkit.getOnlinePlayers().iterator();

         while(var1.hasNext()) {
            Player var2 = (Player)var1.next();
            this.tick(var2);
         }

      }, 0);
   }

   public void onDisable() {
   }

   public void tick(Player p) {
      try {
         try {
            if (IrisSettings.get().getWorld().worldEditWandCUI && isHoldingWand(var1) || isWand(var1.getInventory().getItemInMainHand())) {
               Location[] var2 = getCuboid(var1);
               if (var2 == null || var2[0] == null || var2[1] == null) {
                  return;
               }

               (new WandSelection(new Cuboid(var2[0], var2[1]), var1)).draw();
            }
         } catch (Throwable var3) {
            Iris.reportError(var3);
         }
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

   }

   public void draw(Cuboid d, Player p) {
      this.draw(new Location[]{var1.getLowerNE(), var1.getUpperSW()}, var2);
   }

   public void draw(Location[] d, Player p) {
      Vector var3 = Vector.getRandom().subtract(Vector.getRandom()).normalize().clone().multiply(0.65D);
      var1[0].getWorld().spawnParticle(Particles.CRIT_MAGIC, var1[0], 1, 0.5D + var3.getX(), 0.5D + var3.getY(), 0.5D + var3.getZ(), 0.0D, (Object)null, false);
      Vector var4 = Vector.getRandom().subtract(Vector.getRandom()).normalize().clone().multiply(0.65D);
      var1[1].getWorld().spawnParticle(Particles.CRIT_MAGIC, var1[1], 1, 0.5D + var4.getX(), 0.5D + var4.getY(), 0.5D + var4.getZ(), 0.0D, (Object)null, false);
      if (var1[0].getWorld().equals(var1[1].getWorld())) {
         if (!(var1[0].distanceSquared(var1[1]) > 4096.0D)) {
            int var5 = Math.min(var1[0].getBlockX(), var1[1].getBlockX());
            int var6 = Math.min(var1[0].getBlockY(), var1[1].getBlockY());
            int var7 = Math.min(var1[0].getBlockZ(), var1[1].getBlockZ());
            int var8 = Math.max(var1[0].getBlockX(), var1[1].getBlockX());
            int var9 = Math.max(var1[0].getBlockY(), var1[1].getBlockY());
            int var10 = Math.max(var1[0].getBlockZ(), var1[1].getBlockZ());

            for(double var11 = (double)(var5 - 1); var11 < (double)(var8 + 1); var11 += 0.25D) {
               for(double var13 = (double)(var6 - 1); var13 < (double)(var9 + 1); var13 += 0.25D) {
                  for(double var15 = (double)(var7 - 1); var15 < (double)(var10 + 1); var15 += 0.25D) {
                     if (M.r(0.2D)) {
                        boolean var17 = var11 == (double)var5 || var11 == (double)var8;
                        boolean var18 = var13 == (double)var6 || var13 == (double)var9;
                        boolean var19 = var15 == (double)var7 || var15 == (double)var10;
                        if (var17 && var18 || var17 && var19 || var19 && var18) {
                           Vector var20 = new Vector(0, 0, 0);
                           if (var11 == (double)var5) {
                              var20.add(new Vector(-0.55D, 0.0D, 0.0D));
                           }

                           if (var13 == (double)var6) {
                              var20.add(new Vector(0.0D, -0.55D, 0.0D));
                           }

                           if (var15 == (double)var7) {
                              var20.add(new Vector(0.0D, 0.0D, -0.55D));
                           }

                           if (var11 == (double)var8) {
                              var20.add(new Vector(0.55D, 0.0D, 0.0D));
                           }

                           if (var13 == (double)var9) {
                              var20.add(new Vector(0.0D, 0.55D, 0.0D));
                           }

                           if (var15 == (double)var10) {
                              var20.add(new Vector(0.0D, 0.0D, 0.55D));
                           }

                           Location var21 = (new Location(var1[0].getWorld(), var11, var13, var15)).clone().add(0.5D, 0.5D, 0.5D).clone().add(var20);
                           Color var22 = Color.getHSBColor((float)(0.5D + Math.sin((var11 + var13 + var15 + (double)((float)var2.getTicksLived() / 2.0F)) / 20.0D) / 2.0D), 1.0F, 1.0F);
                           int var23 = var22.getRed();
                           int var24 = var22.getGreen();
                           int var25 = var22.getBlue();
                           var2.spawnParticle(Particles.REDSTONE, var21.getX(), var21.getY(), var21.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D, new DustOptions(org.bukkit.Color.fromRGB(var23, var24, var25), 0.75F));
                        }
                     }
                  }
               }
            }

         }
      }
   }

   @EventHandler
   public void on(PlayerInteractEvent e) {
      if (var1.getHand() == EquipmentSlot.HAND) {
         try {
            if (isHoldingIrisWand(var1.getPlayer())) {
               if (var1.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                  var1.setCancelled(true);
                  var1.getPlayer().getInventory().setItemInMainHand(this.update(true, ((Block)Objects.requireNonNull(var1.getClickedBlock())).getLocation(), var1.getPlayer().getInventory().getItemInMainHand()));
                  var1.getPlayer().playSound(var1.getClickedBlock().getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1.0F, 0.67F);
                  var1.getPlayer().updateInventory();
               } else if (var1.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                  var1.setCancelled(true);
                  var1.getPlayer().getInventory().setItemInMainHand(this.update(false, ((Block)Objects.requireNonNull(var1.getClickedBlock())).getLocation(), var1.getPlayer().getInventory().getItemInMainHand()));
                  var1.getPlayer().playSound(var1.getClickedBlock().getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1.0F, 1.17F);
                  var1.getPlayer().updateInventory();
               }
            }

            if (this.isHoldingDust(var1.getPlayer()) && var1.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
               var1.setCancelled(true);
               var1.getPlayer().playSound(((Block)Objects.requireNonNull(var1.getClickedBlock())).getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 2.0F, 1.97F);
               DustRevealer.spawn(var1.getClickedBlock(), new VolmitSender(var1.getPlayer(), Iris.instance.getTag()));
            }
         } catch (Throwable var3) {
            Iris.reportError(var3);
         }

      }
   }

   public boolean isHoldingDust(Player p) {
      ItemStack var2 = var1.getInventory().getItemInMainHand();
      return var2 != null && this.isDust(var2);
   }

   public boolean isDust(ItemStack is) {
      return var1.isSimilar(dust);
   }

   public ItemStack update(boolean left, Location a, ItemStack item) {
      if (!isWand(var3)) {
         return var3;
      } else {
         Location[] var4 = getCuboidFromItem(var3);
         Location var5 = var1 ? var4[1] : var4[0];
         if (var5 != null && !var5.getWorld().getName().equals(var2.getWorld().getName())) {
            var5 = null;
         }

         return createWand(var1 ? var2 : var5, var1 ? var5 : var2);
      }
   }
}
