package com.volmit.iris.core.safeguard;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {2, 2, 0},
   k = 1,
   xi = 48,
   d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0011\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\r\u001a\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u0000J\u0010\u0010\u000f\u001a\u00020\n2\b\u0010\u0010\u001a\u0004\u0018\u00010\nJ\u0012\u0010\u0011\u001a\u00020\n2\b\u0010\u000f\u001a\u0004\u0018\u00010\nH\u0002J\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\u0014\u001a\u00020\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fj\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b¨\u0006\u0015"},
   d2 = {"Lcom/volmit/iris/core/safeguard/Mode;", "", "color", "Lcom/volmit/iris/util/format/C;", "<init>", "(Ljava/lang/String;ILcom/volmit/iris/util/format/C;)V", "STABLE", "WARNING", "UNSTABLE", "id", "", "getId", "()Ljava/lang/String;", "highest", "m", "tag", "subTag", "wrap", "trySplash", "", "splash", "core"}
)
public enum Mode {
   @NotNull
   private final C color;
   @NotNull
   private final String id;
   STABLE(C.IRIS),
   WARNING(C.GOLD),
   UNSTABLE(C.RED);

   // $FF: synthetic field
   private static final EnumEntries $ENTRIES = EnumEntriesKt.enumEntries((Enum[])$VALUES);

   private Mode(C var3) {
      this.color = var3;
      String var10001 = this.name().toLowerCase(Locale.ROOT);
      Intrinsics.checkNotNullExpressionValue(var10001, "toLowerCase(...)");
      this.id = var10001;
   }

   @NotNull
   public final String getId() {
      return this.id;
   }

   @NotNull
   public final Mode highest(@NotNull Mode var1) {
      Intrinsics.checkNotNullParameter(var1, "m");
      return var1.ordinal() > this.ordinal() ? var1 : this;
   }

   @NotNull
   public final String tag(@Nullable String var1) {
      String var10000;
      if (var1 != null && !StringsKt.isBlank((CharSequence)var1)) {
         var10000 = this.wrap("Iris");
         return var10000 + " " + this.wrap(var1) + C.GRAY + ": ";
      } else {
         var10000 = this.wrap("Iris");
         return var10000 + C.GRAY + ": ";
      }
   }

   private final String wrap(String var1) {
      return C.BOLD + C.DARK_GRAY + "[" + C.BOLD + this.color + var1 + C.BOLD + C.DARK_GRAY + "]" + C.RESET;
   }

   public final void trySplash() {
      if (IrisSettings.get().getGeneral().isSplashLogoStartup()) {
         this.splash();
      }
   }

   public final void splash() {
      String var1 = Form.repeat(" ", 8);
      String var2 = Form.repeat(" ", 4);
      String[] var4 = new String[]{var1 + C.GRAY + "   @@@@@@@@@@@@@@" + C.DARK_GRAY + "@@@", var1 + C.GRAY + " @@&&&&&&&&&" + C.DARK_GRAY + "&&&&&&" + this.color + "   .(((()))).                     ", var1 + C.GRAY + "@@@&&&&&&&&" + C.DARK_GRAY + "&&&&&" + this.color + "  .((((((())))))).                  ", var1 + C.GRAY + "@@@&&&&&" + C.DARK_GRAY + "&&&&&&&" + this.color + "  ((((((((()))))))))               " + C.GRAY + " @", var1 + C.GRAY + "@@@&&&&" + C.DARK_GRAY + "@@@@@&" + this.color + "    ((((((((-)))))))))              " + C.GRAY + " @@", var1 + C.GRAY + "@@@&&" + this.color + "            ((((((({ }))))))))           " + C.GRAY + " &&@@@", var1 + C.GRAY + "@@" + this.color + "               ((((((((-)))))))))    " + C.DARK_GRAY + "&@@@@@" + C.GRAY + "&&&&@@@", var1 + C.GRAY + "@" + this.color + "                ((((((((()))))))))  " + C.DARK_GRAY + "&&&&&" + C.GRAY + "&&&&&&&@@@", var1 + C.GRAY + this.color + "                  '((((((()))))))'  " + C.DARK_GRAY + "&&&&&" + C.GRAY + "&&&&&&&&@@@", var1 + C.GRAY + this.color + "                     '(((())))'   " + C.DARK_GRAY + "&&&&&&&&" + C.GRAY + "&&&&&&&@@", var1 + C.GRAY + "                               " + C.DARK_GRAY + "@@@" + C.GRAY + "@@@@@@@@@@@@@@"};
      String[] var3 = var4;
      String[] var5 = new String[]{"", "", "", "", "", var2 + this.color + " Iris", var2 + C.GRAY + " by " + this.color + "Volmit Software", var2 + C.GRAY + " v" + this.color + Iris.instance.getDescription().getVersion(), var2 + C.GRAY + " c" + this.color + "c31158578f8912e73244302ced03481353131ad6" + C.GRAY + "/" + this.color + "production"};
      var4 = var5;
      StringBuilder var8 = new StringBuilder("\n\n");
      int var6 = 0;

      for(int var7 = var3.length; var6 < var7; ++var6) {
         var8.append(var3[var6]);
         if (var6 < var4.length) {
            var8.append(var4[var6]);
         }

         var8.append("\n");
      }

      Iris.info(var8.toString());
   }

   @NotNull
   public static EnumEntries<Mode> getEntries() {
      return $ENTRIES;
   }

   // $FF: synthetic method
   private static final Mode[] $values() {
      Mode[] var0 = new Mode[]{STABLE, WARNING, UNSTABLE};
      return var0;
   }
}
