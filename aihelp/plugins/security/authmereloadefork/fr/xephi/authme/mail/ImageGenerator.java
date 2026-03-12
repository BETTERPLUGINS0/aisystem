package fr.xephi.authme.mail;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageGenerator {
   private final String pass;

   public ImageGenerator(String pass) {
      this.pass = pass;
   }

   public BufferedImage generateImage() {
      BufferedImage image = new BufferedImage(200, 60, 13);
      Graphics2D graphics = image.createGraphics();
      graphics.setColor(Color.BLACK);
      graphics.fillRect(0, 0, 200, 40);
      GradientPaint gradientPaint = new GradientPaint(10.0F, 5.0F, Color.WHITE, 20.0F, 10.0F, Color.WHITE, true);
      graphics.setPaint(gradientPaint);
      Font font = new Font("Comic Sans MS", 1, 30);
      graphics.setFont(font);
      graphics.drawString(this.pass, 5, 30);
      graphics.dispose();
      image.flush();
      return image;
   }
}
