package fr.xephi.authme.libs.com.google.thirdparty.publicsuffix;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;

@Beta
@GwtCompatible
public enum PublicSuffixType {
   PRIVATE(':', ','),
   REGISTRY('!', '?');

   private final char innerNodeCode;
   private final char leafNodeCode;

   private PublicSuffixType(char innerNodeCode, char leafNodeCode) {
      this.innerNodeCode = innerNodeCode;
      this.leafNodeCode = leafNodeCode;
   }

   char getLeafNodeCode() {
      return this.leafNodeCode;
   }

   char getInnerNodeCode() {
      return this.innerNodeCode;
   }

   static PublicSuffixType fromCode(char code) {
      PublicSuffixType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PublicSuffixType value = var1[var3];
         if (value.getInnerNodeCode() == code || value.getLeafNodeCode() == code) {
            return value;
         }
      }

      throw new IllegalArgumentException((new StringBuilder(38)).append("No enum corresponding to given code: ").append(code).toString());
   }

   // $FF: synthetic method
   private static PublicSuffixType[] $values() {
      return new PublicSuffixType[]{PRIVATE, REGISTRY};
   }
}
