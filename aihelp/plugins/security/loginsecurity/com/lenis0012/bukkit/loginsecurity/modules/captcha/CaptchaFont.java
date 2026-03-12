package com.lenis0012.bukkit.loginsecurity.modules.captcha;

import org.bukkit.map.MapFont;
import org.bukkit.map.MapFont.CharacterSprite;

public class CaptchaFont extends MapFont {
   private static CaptchaFont instance = new CaptchaFont();
   private final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
   private final long[] glyphs = new long[]{873191672953123584L, 4568451290205929216L, 4352169401376979968L, 2249097653905661696L, 9171042009665142528L, 9171042009660919552L, 4352169403256044544L, 3689348866281517824L, 2165118766739627520L, 8660475067009015296L, 7450702136920663808L, 1082559384449875712L, 7167337517222683392L, 7162816308363617024L, 2032921560321760256L, 4568451288589012736L, 2176139340078266368L, 4568451289400633088L, 2176090802603892224L, 4552308044222701056L, 3689348814741913344L, 3689348814740524032L, 7161677145800401664L, 7161627326809531136L, 3689348723890724352L, 9179234445941964544L, 4495563827033095680L, 868645024010354432L, 2176135941871320832L, 2176135942575955456L, 4052173359531718656L, 4540507141147139584L, 2019304915460103680L, 4554036528041167872L, 2176139249751105024L, 2176139387137953280L};

   public static CaptchaFont getInstance() {
      return instance;
   }

   private CaptchaFont() {
      for(int i = 0; i < "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length(); ++i) {
         char character = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(i);
         long glyph = this.glyphs[i];
         boolean[] data = new boolean[64];

         int j;
         for(int j = 0; j < data.length; ++j) {
            j = 63 - j;
            data[j] = (glyph >> j & 1L) == 1L;
         }

         boolean[] bigger = new boolean[256];

         for(j = 0; j < data.length; ++j) {
            int row = j / 8;
            int col = 7 - j % 8;
            bigger[row * 2 * 16 + col * 2] = data[j];
            bigger[(row * 2 + 1) * 16 + col * 2] = data[j];
            bigger[(row * 2 + 1) * 16 + col * 2 + 1] = data[j];
            bigger[row * 2 * 16 + col * 2 + 1] = data[j];
         }

         this.setChar(character, new CharacterSprite(16, 16, bigger));
      }

   }
}
