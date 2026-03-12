package fr.xephi.authme.data.limbo.persistence;

import java.util.HashMap;
import java.util.Map;

class SegmentNameBuilder {
   private final int length;
   private final int distribution;
   private final String prefix;
   private final Map<Character, Character> charToSegmentChar;

   SegmentNameBuilder(SegmentSize partition) {
      this.length = partition.getLength();
      this.distribution = partition.getDistribution();
      this.prefix = "seg" + partition.getTotalSegments() + "-";
      this.charToSegmentChar = buildCharMap(this.distribution);
   }

   String createSegmentName(String uuid) {
      return this.distribution == 16 ? this.prefix + uuid.substring(0, this.length) : this.prefix + this.buildSegmentName(uuid.substring(0, this.length).toCharArray());
   }

   String getPrefix() {
      return this.prefix;
   }

   private String buildSegmentName(char[] chars) {
      if (chars.length == 1) {
         return String.valueOf(this.charToSegmentChar.get(chars[0]));
      } else {
         StringBuilder sb = new StringBuilder(chars.length);
         char[] var3 = chars;
         int var4 = chars.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            char chr = var3[var5];
            sb.append(this.charToSegmentChar.get(chr));
         }

         return sb.toString();
      }
   }

   private static Map<Character, Character> buildCharMap(int distribution) {
      char[] hexChars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
      int divisor = 16 / distribution;
      Map<Character, Character> charToSegmentChar = new HashMap();

      for(int i = 0; i < hexChars.length; ++i) {
         int mappedChar = i / divisor;
         charToSegmentChar.put(hexChars[i], hexChars[mappedChar]);
      }

      return charToSegmentChar;
   }
}
