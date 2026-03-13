package org.terraform.utils;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.data.TerraformWorld;

public class BiomePainter extends JFrame {
   static final int inGameWidth = 6000;
   static final int windowWidth = 1000;
   static final int offsets = 5000;
   int seed = 0;

   public BiomePainter() {
      super("Biome test");
      if (this.seed == 0) {
         this.seed = (new Random()).nextInt(269286925);
      }

      this.setContentPane(new BiomePainter.DrawPane());
      this.setDefaultCloseOperation(3);
      this.setSize(1000, 1000);
      this.setVisible(true);
   }

   public static void main(String[] args) {
      new BiomePainter();
   }

   class DrawPane extends JPanel {
      final JLabel l;
      final BufferedImage i;

      public DrawPane() {
         this.l = new JLabel("Seed: " + BiomePainter.this.seed);
         this.l.setFont(this.l.getFont().deriveFont(1, 25.0F));
         this.setLayout(new FlowLayout(0));
         this.add(this.l);
         this.i = new BufferedImage(1000, 1000, 1);
         this.draw(this.i.getGraphics());
      }

      void draw(@NotNull Graphics g) {
         TerraformWorld world = TerraformWorld.get("world", (long)BiomePainter.this.seed);

         for(int x = 0; x < 1000; ++x) {
            for(int z = 0; z < 1000; ++z) {
               int realX = (int)Math.round((double)x / 1000.0D * 6000.0D + 5000.0D);
               int realZ = (int)Math.round((double)z / 1000.0D * 6000.0D + 5000.0D);
               BiomeBank biome = world.getBiomeBank(realX, realZ);
               switch(biome) {
               case PLAINS:
                  g.setColor(new Color(100, 150, 100));
                  break;
               case TAIGA:
                  g.setColor(new Color(0, 100, 50));
                  break;
               case DESERT:
                  g.setColor(Color.YELLOW);
                  break;
               case JUNGLE:
                  g.setColor(new Color(100, 255, 100));
                  break;
               case SAVANNA:
                  g.setColor(new Color(200, 200, 100));
                  break;
               case SWAMP:
                  g.setColor(Color.MAGENTA);
                  break;
               case BADLANDS:
                  g.setColor(Color.orange);
                  break;
               case FOREST:
                  g.setColor(Color.green);
                  break;
               case DARK_FOREST:
                  g.setColor(Color.red);
                  break;
               case BAMBOO_FOREST:
                  g.setColor(new Color(100, 150, 0));
                  break;
               case MUDFLATS:
                  g.setColor(Color.darkGray);
                  break;
               default:
                  g.setColor(Color.PINK);
               }

               if (biome.name().contains("OCEAN")) {
                  g.setColor(Color.blue);
               } else if (biome.name().contains("MOUNTAIN")) {
                  g.setColor(Color.gray);
               } else if (biome.name().contains("BEACH")) {
                  g.setColor(Color.yellow);
               } else if (!biome.name().contains("ICY") && !biome.name().contains("SNOWY")) {
                  if (biome.name().contains("RIVER")) {
                     g.setColor(Color.blue);
                  }
               } else {
                  g.setColor(Color.white);
               }

               g.drawRect(x, z, 1, 1);
            }
         }

      }

      public void paintComponent(@NotNull Graphics g) {
         g.drawImage(this.i, 0, 0, (ImageObserver)null);
      }
   }
}
