package com.lenis0012.bukkit.loginsecurity.modules.captcha;

import com.lenis0012.bukkit.loginsecurity.util.MetaData;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class CaptchaRenderer extends MapRenderer {
   public void render(MapView view, MapCanvas canvas, Player player) {
      if (!MetaData.has(player, "ls_captcha_set") && MetaData.has(player, "ls_captcha_value")) {
         String text = (String)MetaData.get(player, "ls_captcha_value", String.class);
         MetaData.set(player, "ls_captcha_set", true);

         int x;
         int y;
         for(x = 0; x < 128; ++x) {
            for(y = 0; y < 128; ++y) {
               canvas.setPixel(x, y, (byte)0);
            }
         }

         x = 64 - CaptchaFont.getInstance().getHeight() / 2;
         y = 64 - CaptchaFont.getInstance().getWidth(text) / 2;
         canvas.drawText(y, x, CaptchaFont.getInstance(), text);
      }
   }
}
