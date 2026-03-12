package com.nisovin.shopkeepers.text;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.ChatColor;
import org.checkerframework.checker.nullness.qual.Nullable;

class TextParser {
   private static final TextParser INSTANCE = new TextParser();
   @Nullable
   private TextBuilder root;
   @Nullable
   private TextBuilder last;
   private final StringBuilder stringBuilder = new StringBuilder();

   static Text parse(String input) {
      return INSTANCE._parse(input);
   }

   private TextParser() {
   }

   private void reset() {
      this.root = null;
      this.last = null;
      this.stringBuilder.setLength(0);
   }

   private Text _parse(String input) {
      Validate.notNull(input, (String)"input is null");
      if (input.isEmpty()) {
         return Text.EMPTY;
      } else {
         int length = input.length();

         for(int i = 0; i < length; ++i) {
            char c = input.charAt(i);
            if (TextUtils.isAnyColorChar(c) && i + 1 < length) {
               char c2 = input.charAt(i + 1);
               char c2Lower = Character.toLowerCase(c2);
               String formattingCode = null;
               int skip = 0;
               String hexString;
               if (c2Lower == 'x') {
                  if (i + 13 < length) {
                     hexString = input.substring(i, i + 14);
                     if (TextUtils.isBukkitHexCode(hexString)) {
                        formattingCode = TextUtils.fromBukkitHexCode(hexString);
                        skip = 13;
                     }
                  }
               } else if (c2 == '#') {
                  if (i + 7 < length) {
                     hexString = input.substring(i + 1, i + 8);
                     if (TextUtils.isHexCode(hexString)) {
                        formattingCode = hexString;
                        skip = 7;
                     }
                  }
               } else {
                  ChatColor color = ChatColor.getByChar(c2Lower);
                  if (color != null) {
                     formattingCode = String.valueOf(c2);
                     skip = 1;
                  }
               }

               if (formattingCode != null) {
                  this.next(Text.formatting(formattingCode));
                  i += skip;
                  continue;
               }
            }

            if (c == '{') {
               int placeholderEnd = input.indexOf(125, i + 1);
               if (placeholderEnd != -1) {
                  String placeholderKey = input.substring(i + 1, placeholderEnd);
                  if (!placeholderKey.isEmpty()) {
                     this.next(Text.placeholder(placeholderKey));
                     i = placeholderEnd;
                     continue;
                  }
               }
            }

            this.stringBuilder.append(c);
         }

         this.appendCurrentText();
         Text result = ((TextBuilder)Unsafe.assertNonNull(this.root)).build();
         this.reset();
         return result;
      }
   }

   private void appendCurrentText() {
      if (this.stringBuilder.length() > 0) {
         String text = this.stringBuilder.toString();
         this.stringBuilder.setLength(0);
         this.next(Text.text(text));
      }

   }

   private <T extends TextBuilder> T next(T next) {
      assert next != null;

      this.appendCurrentText();
      if (this.root == null) {
         this.root = next;
      } else {
         ((TextBuilder)Unsafe.assertNonNull(this.last)).next(next);
      }

      this.last = next;
      return next;
   }
}
