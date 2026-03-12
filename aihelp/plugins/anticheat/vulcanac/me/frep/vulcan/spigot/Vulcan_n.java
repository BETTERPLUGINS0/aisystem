package me.frep.vulcan.spigot;

import java.util.ArrayList;
import java.util.Vector;

public class Vulcan_n implements Vulcan_r {
   private long a;
   private int[] b;
   private Vulcan_r c;
   private long d;
   private long[] j;
   private static long[] k = new long[64];
   static int[] e;
   private static ArrayList f;
   private static Vector m;
   private static int i;
   private static Object o;
   private static int h = 52;
   private static int l = 128;
   private static int n = 17;

   public static Object g() {
      return o;
   }

   public static Vulcan_r a(long var0, long var2, Object var4) {
      Vulcan_i.a(var0 > 0L);
      Vulcan_r var5 = d(var0);
      Vulcan_r var6 = d(var2);
      Vulcan_r var7 = Vulcan_i.a(var5, var6);
      if (var4 != null) {
         m.add(var4);
      }

      return var7;
   }

   static Vulcan_r c(long var0) {
      int var2 = (int)a(var0, h, 63, e, k);
      if (var2 < i) {
         Vulcan_r var4 = (Vulcan_r)f.get(var2);
         return var4;
      } else {
         if (f.size() % l == 0) {
            e = (int[])((int[])e.clone());
         }

         Vulcan_n var3 = new Vulcan_n(var0);
         f.add(var3);
         return var3;
      }
   }

   private static Vulcan_r d(long var0) {
      Vulcan_n var2 = new Vulcan_n(var0);
      return var2;
   }

   static void a(Vulcan_i var0) {
      i = f.size();
      c();
      var0.d();
   }

   static void b(Vulcan_i var0) {
      c();
      int[] var10000 = e = new int[64];
      var10000[0] = -62;
      var10000[1] = -43;
      var10000[2] = -30;
      var10000[3] = -40;
      var10000[4] = -21;
      var10000[5] = -29;
      var10000[6] = -11;
      var10000[7] = -12;
      var10000[8] = -50;
      var10000[9] = -12;
      var10000[10] = -36;
      var10000[11] = -44;
      var10000[12] = -26;
      var10000[13] = -29;
      var10000[14] = -31;
      var10000[15] = -12;
      var10000[16] = -41;
      var10000[17] = 11;
      var10000[18] = -17;
      var10000[19] = 12;
      var10000[20] = -36;
      var10000[21] = 12;
      var10000[22] = -7;
      var10000[23] = -40;
      var10000[24] = -12;
      var10000[25] = 21;
      var10000[26] = -27;
      var10000[27] = 12;
      var10000[28] = -19;
      var10000[29] = 7;
      var10000[30] = -19;
      var10000[31] = -6;
      var10000[32] = 30;
      var10000[33] = -17;
      var10000[34] = 29;
      var10000[35] = 17;
      var10000[36] = 12;
      var10000[37] = 6;
      var10000[38] = 26;
      var10000[39] = -1;
      var10000[40] = 1;
      var10000[41] = -13;
      var10000[42] = 29;
      var10000[43] = 40;
      var10000[44] = 43;
      var10000[45] = 31;
      var10000[46] = 36;
      var10000[47] = 19;
      var10000[48] = -4;
      var10000[49] = 19;
      var10000[50] = 17;
      var10000[51] = -9;
      var10000[52] = 4;
      var10000[53] = 27;
      var10000[54] = 13;
      var10000[55] = 44;
      var10000[56] = 36;
      var10000[57] = 41;
      var10000[58] = 50;
      var10000[59] = -2;
      var10000[60] = 9;
      var10000[61] = 2;
      var10000[62] = 62;
      var10000[63] = 40;
      var0.c();
   }

   private Vulcan_n(long var1) {
      this.a = var1;
      this.b = e;
      this.j = k;
   }

   public long a(long var1) {
      long var3 = this.a(8, 55);
      long var5 = this.a ^ var1 ^ this.d;
      this.a = var5;
      if (this.c != null) {
         this.c.a(var1);
      }

      return var3;
   }

   public void b(long var1) {
      this.d = var1;
   }

   public void a(Vulcan_r var1) {
      if (this != var1) {
         if (this.c == null) {
            this.c = var1;
         } else {
            this.c.a(var1);
         }
      }

   }

   public int hashCode() {
      return (int)this.a(8);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof Vulcan_n) {
         return this.a(56) == ((Vulcan_n)var1).a(56);
      } else {
         return false;
      }
   }

   public boolean b(Vulcan_r var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof Vulcan_n) {
         return this.a(56, 63) - ((Vulcan_n)var1).a(56, 63) <= 0L;
      } else {
         return true;
      }
   }

   public int[] b() {
      return this.b;
   }

   private long a(int var1) {
      return this.a(0, var1 - 1);
   }

   private long a(int var1, int var2) {
      return a(this.a, var1, var2, this.b, this.j);
   }

   private static long a(long var0, int var2, int var3, int[] var4, long[] var5) {
      long var6 = 0L;
      int var8 = var4.length;

      int var12;
      for(int var9 = 0; var9 < var8; ++var9) {
         long var10 = var0 & k[var9];
         var12 = var4[var9];
         if (var10 != 0L) {
            if (var12 > 0) {
               var10 >>>= var12;
            } else if (var12 < 0) {
               var10 <<= ~var12 + 1;
            }

            var6 |= var10;
         }
      }

      byte var13 = 64;
      long var14 = var6;
      int var11 = var13 - 1 - var3;
      if (var11 > 0) {
         var14 = var6 << var11;
      }

      var12 = var2 + var13 - 1 - var3;
      if (var12 > 0) {
         var14 >>>= var12;
      }

      return var14;
   }

   private static void c() {
      byte var0 = 0;
      a(0, f.size() - 1, f, new ArrayList(f), var0);
   }

   private static void a(int var0, int var1, ArrayList var2, ArrayList var3, int var4) {
      if (var0 < var1) {
         int var5 = var0 + (var1 - var0) / 2;
         ++var4;
         if (var4 < n) {
            a(var0, var5, var2, var3, var4);
            a(var5 + 1, var1, var2, var3, var4);
         }

         a(var0, var5, var1, var2, var3);
      }

   }

   private static void a(int var0, int var1, int var2, ArrayList var3, ArrayList var4) {
      int var5 = var0;
      int var6 = var1 + 1;

      for(int var7 = var0; var7 <= var2; ++var7) {
         var4.set(var7, var3.get(var7));
      }

      while(var5 <= var1 && var6 <= var2) {
         Vulcan_r var8;
         if (((Vulcan_r)var4.get(var5)).b((Vulcan_r)var4.get(var6))) {
            var8 = (Vulcan_r)var4.get(var5++);
         } else {
            var8 = (Vulcan_r)var4.get(var6++);
         }

         var3.set(var0, var8);
         ++var0;
      }

      while(var5 <= var1) {
         var3.set(var0, var4.get(var5));
         ++var0;
         ++var5;
      }

   }

   static {
      long var0 = 1L;

      for(int var2 = 0; var2 < 64; ++var2) {
         k[var2] = var0;
         var0 <<= 1;
      }

      o = new Object();
      m = new Vector();
      f = new ArrayList();
      a0();
      i = f.size();
      c();
   }

   private static void a0() {
      e = new int[]{-50, -17, -25, -42, -4, -52, -33, -34, 4, -15, -39, -31, -42, -18, -38, -33, -28, -46, 17, -36, -10, -12, -25, -30, 15, -26, -32, 25, -4, -32, 10, 18, 4, 12, -6, -2, -10, 2, -18, 33, 6, 34, 31, -16, 28, 42, 10, 25, 33, 39, 50, 26, 38, 30, 42, 36, 18, 52, 32, 16, -2, 32, 2, 46};
      f.add(new Vulcan_n(-3246859315892219525L));
      f.add(new Vulcan_n(-7840818422197507376L));
      f.add(new Vulcan_n(5832762797150312095L));
      f.add(new Vulcan_n(8658730760957717140L));
      f.add(new Vulcan_n(-4381111557696362201L));
      f.add(new Vulcan_n(1987926171320750974L));
      f.add(new Vulcan_n(-6501258895204884608L));
      f.add(new Vulcan_n(4535490666883743664L));
      f.add(new Vulcan_n(4494581491727310251L));
      f.add(new Vulcan_n(-6839008904194204763L));
      f.add(new Vulcan_n(-24338662210270954L));
      f.add(new Vulcan_n(3420336764143034031L));
      f.add(new Vulcan_n(5956619324887168809L));
      f.add(new Vulcan_n(4546354976957537059L));
      f.add(new Vulcan_n(4636020880738544557L));
      f.add(new Vulcan_n(2997066003440008178L));
      f.add(new Vulcan_n(6372580190447795760L));
      f.add(new Vulcan_n(6960144891056774541L));
      f.add(new Vulcan_n(-1684967542802456047L));
      f.add(new Vulcan_n(8198379953619773944L));
      f.add(new Vulcan_n(3529771128744736764L));
      f.add(new Vulcan_n(-7133534853826904668L));
      f.add(new Vulcan_n(5698891509448036048L));
      f.add(new Vulcan_n(-6072916693157183566L));
      f.add(new Vulcan_n(3200336294748402938L));
      f.add(new Vulcan_n(-7117674554524849967L));
      f.add(new Vulcan_n(8260816684631421988L));
      f.add(new Vulcan_n(5691816994891907038L));
      f.add(new Vulcan_n(4913067976270399667L));
      f.add(new Vulcan_n(-5989397547143762427L));
      f.add(new Vulcan_n(2451577839077953406L));
      f.add(new Vulcan_n(-3643858659485171730L));
      f.add(new Vulcan_n(3537164514039521992L));
      f.add(new Vulcan_n(-1164245402329746497L));
      f.add(new Vulcan_n(3140630669008716601L));
      f.add(new Vulcan_n(7865152033942292214L));
      f.add(new Vulcan_n(-4336313952137556148L));
      f.add(new Vulcan_n(-7234467864057343013L));
      f.add(new Vulcan_n(8923615793631121159L));
      f.add(new Vulcan_n(5661214613433723703L));
      f.add(new Vulcan_n(-1326255019132799977L));
      f.add(new Vulcan_n(9221308539942157990L));
      f.add(new Vulcan_n(6428858334538031981L));
      f.add(new Vulcan_n(-7534471835047293101L));
      f.add(new Vulcan_n(8506699510177757636L));
      f.add(new Vulcan_n(-6801819869347668914L));
      f.add(new Vulcan_n(-6107341510027493413L));
      f.add(new Vulcan_n(-8511637595896780135L));
      f.add(new Vulcan_n(5546681261367581950L));
      f.add(new Vulcan_n(-1607663152566025457L));
      f.add(new Vulcan_n(-1349760316678498045L));
      f.add(new Vulcan_n(1679849296664717109L));
      f.add(new Vulcan_n(-6126275597403167755L));
      f.add(new Vulcan_n(-6573543219815696299L));
      f.add(new Vulcan_n(437543276328152915L));
      f.add(new Vulcan_n(-8560964958344295566L));
      f.add(new Vulcan_n(1190510229105827695L));
      f.add(new Vulcan_n(-6190668452784268878L));
      f.add(new Vulcan_n(1820552568816965236L));
      f.add(new Vulcan_n(-1135172432331306438L));
      f.add(new Vulcan_n(6244377482230064805L));
      f.add(new Vulcan_n(2503913067415715580L));
      f.add(new Vulcan_n(7279273707434269242L));
      f.add(new Vulcan_n(-4761229367001974009L));
      f.add(new Vulcan_n(8911510042082219495L));
      f.add(new Vulcan_n(2174542269826678253L));
      f.add(new Vulcan_n(-6030310454279641935L));
      f.add(new Vulcan_n(8235169924970010166L));
      f.add(new Vulcan_n(3880476956522092985L));
      f.add(new Vulcan_n(938098793701155732L));
      f.add(new Vulcan_n(-3243323980699585456L));
      f.add(new Vulcan_n(4344951491337005293L));
      f.add(new Vulcan_n(-1808869762929507649L));
      f.add(new Vulcan_n(-8344347191732160519L));
      f.add(new Vulcan_n(3697097065601616764L));
      f.add(new Vulcan_n(-4203647633374311167L));
      f.add(new Vulcan_n(4106626312258886850L));
      f.add(new Vulcan_n(8286494598831263177L));
      f.add(new Vulcan_n(-4337490312782454207L));
      f.add(new Vulcan_n(-1719494578739778017L));
      f.add(new Vulcan_n(-2506284626499028719L));
      f.add(new Vulcan_n(-7878223238572625759L));
      f.add(new Vulcan_n(1102933338153773541L));
      f.add(new Vulcan_n(-1953683267419864794L));
      f.add(new Vulcan_n(8436920708915175217L));
      f.add(new Vulcan_n(-539968307259682314L));
      f.add(new Vulcan_n(-5335071217496360090L));
      f.add(new Vulcan_n(1742902130241595589L));
      f.add(new Vulcan_n(-2035585742645637856L));
      f.add(new Vulcan_n(-4068848052597646166L));
      f.add(new Vulcan_n(37588998906994511L));
      f.add(new Vulcan_n(6051087445254422518L));
      f.add(new Vulcan_n(1487919171595564887L));
      f.add(new Vulcan_n(-2039431024389867361L));
      f.add(new Vulcan_n(6171729768009143075L));
      f.add(new Vulcan_n(1972696018254661999L));
      f.add(new Vulcan_n(-7447663552254173562L));
      f.add(new Vulcan_n(2238729633580218578L));
      f.add(new Vulcan_n(1469520415123809335L));
      f.add(new Vulcan_n(-3008196481401898249L));
      f.add(new Vulcan_n(-1668672794722730963L));
      f.add(new Vulcan_n(5401487573763754178L));
      f.add(new Vulcan_n(-3707886645383303522L));
      f.add(new Vulcan_n(3409337215133907774L));
      f.add(new Vulcan_n(3290737081053531188L));
      f.add(new Vulcan_n(-6200822795835513774L));
      f.add(new Vulcan_n(7452316728114257937L));
      f.add(new Vulcan_n(-1692880741432471050L));
      f.add(new Vulcan_n(6730010583986757436L));
      f.add(new Vulcan_n(3386616869323890187L));
      f.add(new Vulcan_n(3494779625769637148L));
      f.add(new Vulcan_n(5678031886362172663L));
      f.add(new Vulcan_n(6248257147953584034L));
      f.add(new Vulcan_n(5375561929175637849L));
      f.add(new Vulcan_n(4011799474753076072L));
      f.add(new Vulcan_n(1788578346761886087L));
      f.add(new Vulcan_n(-5917620444800558619L));
      f.add(new Vulcan_n(378992337089730708L));
      f.add(new Vulcan_n(-8524613665724937478L));
      f.add(new Vulcan_n(-7629297809745747141L));
      f.add(new Vulcan_n(-1064999447945880122L));
      f.add(new Vulcan_n(8356843598696943278L));
      f.add(new Vulcan_n(7642890537016534881L));
      f.add(new Vulcan_n(-1563041799933201559L));
      f.add(new Vulcan_n(6503869822399575256L));
      f.add(new Vulcan_n(-2027050453586372592L));
      f.add(new Vulcan_n(-5295627378454476089L));
      f.add(new Vulcan_n(8345937978551648409L));
      f.add(new Vulcan_n(7905476981222865138L));
      f.add(new Vulcan_n(3088213712715564036L));
      f.add(new Vulcan_n(9151224391821254155L));
      f.add(new Vulcan_n(-3662112466727317701L));
      f.add(new Vulcan_n(-3858472223484968071L));
      f.add(new Vulcan_n(8019814596171802577L));
      f.add(new Vulcan_n(5539817102421273038L));
      f.add(new Vulcan_n(-7381040754571389715L));
      f.add(new Vulcan_n(-7022584238599546206L));
      f.add(new Vulcan_n(-2713418331341925647L));
      f.add(new Vulcan_n(-999411524823531830L));
      f.add(new Vulcan_n(7734486427020496714L));
      f.add(new Vulcan_n(6288465574656912594L));
      f.add(new Vulcan_n(7174219738119564056L));
      f.add(new Vulcan_n(-2036944940810695075L));
      f.add(new Vulcan_n(5908723482299355263L));
      f.add(new Vulcan_n(2262399983996563012L));
      f.add(new Vulcan_n(-430400209410408794L));
      f.add(new Vulcan_n(-6390653719517117221L));
      f.add(new Vulcan_n(-4008896995544072495L));
      f.add(new Vulcan_n(-3340921521028718138L));
      f.add(new Vulcan_n(-3566372127964254915L));
      f.add(new Vulcan_n(449516683146855901L));
      f.add(new Vulcan_n(9186745585272647662L));
      f.add(new Vulcan_n(950214285648537077L));
      f.add(new Vulcan_n(3137571277518309136L));
      f.add(new Vulcan_n(228897442472509706L));
      f.add(new Vulcan_n(-6281538700980074519L));
      f.add(new Vulcan_n(-8741227986961350064L));
      f.add(new Vulcan_n(-4724827917382515652L));
      f.add(new Vulcan_n(6702676961596084993L));
      e = (int[])e.clone();
      f.add(new Vulcan_n(5146775375866843080L));
      f.add(new Vulcan_n(8534117419712981165L));
      f.add(new Vulcan_n(7881628988733396347L));
      f.add(new Vulcan_n(-6468613819067236839L));
      f.add(new Vulcan_n(-3353057605193797493L));
      f.add(new Vulcan_n(9148820102507045416L));
      f.add(new Vulcan_n(-7665282736971398706L));
      f.add(new Vulcan_n(3206512686537251510L));
      f.add(new Vulcan_n(-116400190511666790L));
      f.add(new Vulcan_n(6824707891438767405L));
      f.add(new Vulcan_n(-9058862796044566089L));
      f.add(new Vulcan_n(8394643472427344939L));
      f.add(new Vulcan_n(6817223796542422690L));
      f.add(new Vulcan_n(8519398422263907436L));
      f.add(new Vulcan_n(-8382245909410326481L));
      f.add(new Vulcan_n(2768535463608233206L));
      f.add(new Vulcan_n(-2389831083848495691L));
      f.add(new Vulcan_n(-838372441803591352L));
      f.add(new Vulcan_n(7234191107466678750L));
      f.add(new Vulcan_n(8706274329561815910L));
      f.add(new Vulcan_n(6500828993886059004L));
      f.add(new Vulcan_n(-9105644215436152995L));
      f.add(new Vulcan_n(6811813281565639284L));
      f.add(new Vulcan_n(-5141671454751560055L));
      f.add(new Vulcan_n(-3502350897961167226L));
      f.add(new Vulcan_n(-690481641140579490L));
      f.add(new Vulcan_n(7425705244483758341L));
      f.add(new Vulcan_n(4243309889946151171L));
      f.add(new Vulcan_n(282226767236831664L));
      f.add(new Vulcan_n(-7373193234250008916L));
      f.add(new Vulcan_n(-3677009937806582988L));
      f.add(new Vulcan_n(5490621086211382249L));
      f.add(new Vulcan_n(8811517325571687327L));
      f.add(new Vulcan_n(-5985289636925725123L));
      f.add(new Vulcan_n(3664584696130582946L));
      f.add(new Vulcan_n(-2986544591317897101L));
      f.add(new Vulcan_n(2423574188288646147L));
      f.add(new Vulcan_n(3324956920530491420L));
      f.add(new Vulcan_n(7172973919717084298L));
      f.add(new Vulcan_n(8891872716419860290L));
      f.add(new Vulcan_n(-9142053858159020972L));
      f.add(new Vulcan_n(-8008336455839428896L));
      f.add(new Vulcan_n(926945578524519379L));
      f.add(new Vulcan_n(4456570357754981118L));
      f.add(new Vulcan_n(-7424457689324446244L));
      f.add(new Vulcan_n(-5595611660098906967L));
      f.add(new Vulcan_n(7766923263715932060L));
      f.add(new Vulcan_n(8367353486767549760L));
      f.add(new Vulcan_n(-6158957467150120417L));
      f.add(new Vulcan_n(9065967150345299387L));
      f.add(new Vulcan_n(-798223042440233243L));
      f.add(new Vulcan_n(2496296188041985791L));
      f.add(new Vulcan_n(-411380561961891909L));
      f.add(new Vulcan_n(2613044388455458869L));
      f.add(new Vulcan_n(5655791945719802033L));
      f.add(new Vulcan_n(8647835508569134072L));
      f.add(new Vulcan_n(1223780229493209722L));
      f.add(new Vulcan_n(6989409604578121774L));
      f.add(new Vulcan_n(3572588911837741074L));
      f.add(new Vulcan_n(2085124812452165623L));
      f.add(new Vulcan_n(9119250568750227849L));
      f.add(new Vulcan_n(-4610339784289499310L));
      f.add(new Vulcan_n(6844738499029123038L));
      f.add(new Vulcan_n(5552762180157462953L));
      f.add(new Vulcan_n(-8402573233116286229L));
      f.add(new Vulcan_n(7337835960918689847L));
      f.add(new Vulcan_n(3269214799054195080L));
      f.add(new Vulcan_n(7294088549117575386L));
      f.add(new Vulcan_n(2339115877571666850L));
      f.add(new Vulcan_n(-7703078247884082753L));
      f.add(new Vulcan_n(4671973409134290426L));
      f.add(new Vulcan_n(4366552606418889962L));
      f.add(new Vulcan_n(855432490861801217L));
      f.add(new Vulcan_n(-8612641208182480319L));
      f.add(new Vulcan_n(-2705470953644266857L));
      f.add(new Vulcan_n(-2958969446409928158L));
      f.add(new Vulcan_n(-4329635262741032672L));
      f.add(new Vulcan_n(-2720611609188544763L));
      f.add(new Vulcan_n(2558643443414170734L));
      f.add(new Vulcan_n(9222511381525693063L));
      f.add(new Vulcan_n(1836905644270376036L));
      f.add(new Vulcan_n(-6501304068569156862L));
      f.add(new Vulcan_n(6392322749153878979L));
      f.add(new Vulcan_n(-6792736843161457312L));
      f.add(new Vulcan_n(2818012059592306091L));
      f.add(new Vulcan_n(-4556420945721290524L));
      f.add(new Vulcan_n(662349479289054787L));
      f.add(new Vulcan_n(865268501352677343L));
      f.add(new Vulcan_n(2737387643114521419L));
      f.add(new Vulcan_n(-7443670259030904342L));
      f.add(new Vulcan_n(8212870299149364209L));
      f.add(new Vulcan_n(3393645192180652804L));
      f.add(new Vulcan_n(-4105367051769432433L));
      f.add(new Vulcan_n(4937129677857951728L));
      f.add(new Vulcan_n(-7965525821952161917L));
      f.add(new Vulcan_n(-3896284320704804237L));
      f.add(new Vulcan_n(-1077095546010218133L));
      f.add(new Vulcan_n(6570860941635993997L));
      f.add(new Vulcan_n(6986655939368807525L));
      f.add(new Vulcan_n(-5239950088538406057L));
      f.add(new Vulcan_n(-490359792250816841L));
      f.add(new Vulcan_n(-8527178998091671724L));
      f.add(new Vulcan_n(-4995226608260986451L));
      f.add(new Vulcan_n(-5648369124321572944L));
      f.add(new Vulcan_n(1878051870870684148L));
      f.add(new Vulcan_n(7596740442285624347L));
      f.add(new Vulcan_n(6872405543929500729L));
      f.add(new Vulcan_n(-1264565564725750752L));
      f.add(new Vulcan_n(-761328393029248568L));
      f.add(new Vulcan_n(-3750950187442903880L));
      f.add(new Vulcan_n(1368002931784452145L));
      f.add(new Vulcan_n(3613489717048764442L));
      f.add(new Vulcan_n(-8387669429344786798L));
      f.add(new Vulcan_n(4041081177970705105L));
      f.add(new Vulcan_n(7392110664250196938L));
      f.add(new Vulcan_n(3752900255540187010L));
      f.add(new Vulcan_n(4517697179385786412L));
      f.add(new Vulcan_n(-1154283017241087459L));
      f.add(new Vulcan_n(6316695667353183028L));
      f.add(new Vulcan_n(-8501215936675361506L));
      f.add(new Vulcan_n(-1408613016308376717L));
      f.add(new Vulcan_n(-1840198590433704541L));
      f.add(new Vulcan_n(-2088582653451942180L));
      f.add(new Vulcan_n(-2850241657197758563L));
      f.add(new Vulcan_n(6806892683872042549L));
      f.add(new Vulcan_n(-8109624256937771632L));
      e = (int[])e.clone();
      f.add(new Vulcan_n(26934779702657254L));
      f.add(new Vulcan_n(5346473677754194382L));
      f.add(new Vulcan_n(6302073902847127169L));
      f.add(new Vulcan_n(-5633470338752552318L));
      f.add(new Vulcan_n(1820524316551391309L));
      f.add(new Vulcan_n(3996272463932706451L));
      f.add(new Vulcan_n(8528178788875585441L));
      f.add(new Vulcan_n(-7981387423163376490L));
      f.add(new Vulcan_n(7861079398590508736L));
      f.add(new Vulcan_n(-5322263769659523834L));
      f.add(new Vulcan_n(8224810901520581525L));
      f.add(new Vulcan_n(892279456886221336L));
      f.add(new Vulcan_n(-7093452521897695992L));
      f.add(new Vulcan_n(2470239292427202645L));
      f.add(new Vulcan_n(-7679456035090386921L));
      f.add(new Vulcan_n(-7527667312775136713L));
      f.add(new Vulcan_n(8918009852416006498L));
      f.add(new Vulcan_n(882024809940401508L));
      f.add(new Vulcan_n(-2166388975132648878L));
      f.add(new Vulcan_n(4003778435428580244L));
      f.add(new Vulcan_n(8347324286125752750L));
      f.add(new Vulcan_n(-4651434801728327359L));
      f.add(new Vulcan_n(-5718237124934406585L));
      f.add(new Vulcan_n(7278293290792849324L));
      f.add(new Vulcan_n(-7707450869974648243L));
      f.add(new Vulcan_n(665811082205642946L));
      f.add(new Vulcan_n(7775858404403542130L));
      f.add(new Vulcan_n(8777085989643430259L));
      f.add(new Vulcan_n(-3957792965022912525L));
      f.add(new Vulcan_n(-2752075216606704461L));
      f.add(new Vulcan_n(-6263362552928656871L));
      f.add(new Vulcan_n(-5874617173464102842L));
      f.add(new Vulcan_n(3508053859690017542L));
      f.add(new Vulcan_n(-7016264237662916975L));
      f.add(new Vulcan_n(3892559964724952094L));
      f.add(new Vulcan_n(5023161033569650256L));
      f.add(new Vulcan_n(2369186144682756564L));
      f.add(new Vulcan_n(1137549253326177717L));
      f.add(new Vulcan_n(-2607631788871188991L));
      f.add(new Vulcan_n(1267054826771079898L));
      f.add(new Vulcan_n(-6352208016183382871L));
      f.add(new Vulcan_n(462754362654122564L));
      f.add(new Vulcan_n(-3814835883610907297L));
      f.add(new Vulcan_n(-6822846268885923843L));
      f.add(new Vulcan_n(7050855994937901173L));
      f.add(new Vulcan_n(-3028062719733117758L));
      f.add(new Vulcan_n(3462464640324471698L));
      f.add(new Vulcan_n(3018592203927871908L));
      f.add(new Vulcan_n(-5419086798678408092L));
      f.add(new Vulcan_n(-4879911324059668737L));
      f.add(new Vulcan_n(-3808717401429207630L));
      f.add(new Vulcan_n(-3156155284839119160L));
      f.add(new Vulcan_n(1521607608945232544L));
      f.add(new Vulcan_n(1062526059065653966L));
      f.add(new Vulcan_n(8166593923834276277L));
      f.add(new Vulcan_n(2794211737722088846L));
      f.add(new Vulcan_n(-6274982062576080623L));
      f.add(new Vulcan_n(-3161742361842357973L));
      f.add(new Vulcan_n(-5241402448299038010L));
      f.add(new Vulcan_n(-7197914530576883120L));
      f.add(new Vulcan_n(3903939674731005416L));
      f.add(new Vulcan_n(3134647460424657362L));
      f.add(new Vulcan_n(-8996699996575004947L));
      f.add(new Vulcan_n(7640056935531816520L));
      f.add(new Vulcan_n(3092701684231958332L));
      f.add(new Vulcan_n(-3878546193085552425L));
      f.add(new Vulcan_n(-2295784891816492138L));
      f.add(new Vulcan_n(6129194271064022857L));
      f.add(new Vulcan_n(8699834804929431515L));
      f.add(new Vulcan_n(2827540207210324281L));
      f.add(new Vulcan_n(-1264574360885881824L));
      f.add(new Vulcan_n(-4494604421865184280L));
      f.add(new Vulcan_n(8436929779886104337L));
      f.add(new Vulcan_n(-8418131246058328546L));
      f.add(new Vulcan_n(6912041892528332663L));
      f.add(new Vulcan_n(7376104555573936818L));
      f.add(new Vulcan_n(-7762234678403115434L));
      f.add(new Vulcan_n(2335593556466529984L));
      f.add(new Vulcan_n(-7862122473546438930L));
      f.add(new Vulcan_n(6024403729940377192L));
      f.add(new Vulcan_n(-5458548217699075174L));
      f.add(new Vulcan_n(4195769677659807855L));
      f.add(new Vulcan_n(3005748286132068715L));
      f.add(new Vulcan_n(-213048390934515658L));
      f.add(new Vulcan_n(2019044159428577106L));
      f.add(new Vulcan_n(-630556049798669708L));
      f.add(new Vulcan_n(-4182249630052442055L));
      f.add(new Vulcan_n(-67326862010384609L));
      f.add(new Vulcan_n(4859412182900130923L));
      f.add(new Vulcan_n(-6873688354094600196L));
      f.add(new Vulcan_n(7206618283480451777L));
      f.add(new Vulcan_n(-6123066669940634694L));
      f.add(new Vulcan_n(-383908448786226452L));
      f.add(new Vulcan_n(-1580457390765561205L));
      f.add(new Vulcan_n(600829813176390975L));
      f.add(new Vulcan_n(402442624731237193L));
      f.add(new Vulcan_n(8858238095877034798L));
      f.add(new Vulcan_n(-8213689711828934777L));
      f.add(new Vulcan_n(-8015443014622070869L));
      f.add(new Vulcan_n(-6769946286392963714L));
      e = (int[])e.clone();
      f.add(new Vulcan_n(-9180435957552987827L));
      f.add(new Vulcan_n(-163036971067723344L));
      f.add(new Vulcan_n(7420076509690816760L));
      f.add(new Vulcan_n(1158605832361109952L));
      f.add(new Vulcan_n(-6327402630418892532L));
      f.add(new Vulcan_n(5222556730964524683L));
      f.add(new Vulcan_n(-1304591634595973498L));
      f.add(new Vulcan_n(9004177967869829412L));
      f.add(new Vulcan_n(-5409135623096170035L));
      f.add(new Vulcan_n(4138056355799272051L));
      f.add(new Vulcan_n(-1980617419282881743L));
      f.add(new Vulcan_n(-2992008463410463590L));
      f.add(new Vulcan_n(-8501550636310751123L));
      f.add(new Vulcan_n(-7504288975170584411L));
      f.add(new Vulcan_n(3439062754700554304L));
      f.add(new Vulcan_n(3837549120563784704L));
      f.add(new Vulcan_n(895171736646112429L));
      f.add(new Vulcan_n(-8814469156901466786L));
      f.add(new Vulcan_n(-8417749506193079506L));
      f.add(new Vulcan_n(4878227365081450242L));
      f.add(new Vulcan_n(6407047134835681204L));
      f.add(new Vulcan_n(-5917620169922782779L));
      f.add(new Vulcan_n(-3417791180152240691L));
      f.add(new Vulcan_n(1373175307749707636L));
      f.add(new Vulcan_n(3364406056567894783L));
      f.add(new Vulcan_n(6718710585882727536L));
      f.add(new Vulcan_n(-5636890824941714020L));
      f.add(new Vulcan_n(395415480512690553L));
      f.add(new Vulcan_n(6936203983883289686L));
      f.add(new Vulcan_n(-7414446211562675640L));
      f.add(new Vulcan_n(2364269181852058933L));
      f.add(new Vulcan_n(6258368955311178341L));
      f.add(new Vulcan_n(1879250244531716840L));
      f.add(new Vulcan_n(-3127854527798705360L));
      f.add(new Vulcan_n(6574379591531748518L));
      f.add(new Vulcan_n(5054203125608589937L));
      f.add(new Vulcan_n(2992647594224029301L));
      f.add(new Vulcan_n(-8688603488046452812L));
      f.add(new Vulcan_n(5740898605103151408L));
      f.add(new Vulcan_n(4674309893702038188L));
      f.add(new Vulcan_n(12638527885996598L));
      f.add(new Vulcan_n(-4458137884979409890L));
      f.add(new Vulcan_n(5973581448802751232L));
      f.add(new Vulcan_n(-7001526173246943851L));
      f.add(new Vulcan_n(8678059785017804495L));
      f.add(new Vulcan_n(-349222295456930517L));
      f.add(new Vulcan_n(6817124548022973788L));
      f.add(new Vulcan_n(-6862227641608045697L));
      f.add(new Vulcan_n(-6243911174843876271L));
      f.add(new Vulcan_n(-6419035396688656240L));
      f.add(new Vulcan_n(-9161141981488857453L));
      f.add(new Vulcan_n(-3937410930232088725L));
      f.add(new Vulcan_n(8490252180752168471L));
      f.add(new Vulcan_n(1357786870576133791L));
      f.add(new Vulcan_n(7076784491617160543L));
      f.add(new Vulcan_n(1557791580974740518L));
      f.add(new Vulcan_n(1005988396749285085L));
      f.add(new Vulcan_n(-3094395961079603701L));
      f.add(new Vulcan_n(8870308187776382496L));
      f.add(new Vulcan_n(5528643139262524662L));
      f.add(new Vulcan_n(8556790209785981679L));
      f.add(new Vulcan_n(2791566937589826160L));
      f.add(new Vulcan_n(-502729206159409557L));
      f.add(new Vulcan_n(-5254194431430477640L));
      f.add(new Vulcan_n(-4597304785415101435L));
      f.add(new Vulcan_n(7030136504128156750L));
      f.add(new Vulcan_n(-3941024670086545414L));
      f.add(new Vulcan_n(6831318065046060010L));
      f.add(new Vulcan_n(-4609249515326645586L));
      f.add(new Vulcan_n(-656514914439406013L));
      f.add(new Vulcan_n(3009666293291283603L));
      f.add(new Vulcan_n(523607337405166399L));
      f.add(new Vulcan_n(4945080473113181373L));
      f.add(new Vulcan_n(-653081370010043874L));
      f.add(new Vulcan_n(-6970453789026995239L));
      f.add(new Vulcan_n(-861721547328985940L));
      f.add(new Vulcan_n(1768188409635401003L));
      f.add(new Vulcan_n(-7072772215003899537L));
      f.add(new Vulcan_n(5989832351830760359L));
      f.add(new Vulcan_n(6857012451406520923L));
      f.add(new Vulcan_n(-1485481447899582368L));
      f.add(new Vulcan_n(4163316858564466212L));
      f.add(new Vulcan_n(-5201675055835907104L));
      f.add(new Vulcan_n(6393157906079144791L));
      f.add(new Vulcan_n(-5377503195871171288L));
      f.add(new Vulcan_n(1814888388041425855L));
      f.add(new Vulcan_n(552211788485151388L));
      f.add(new Vulcan_n(-6485876465071630633L));
      f.add(new Vulcan_n(5760342693499800477L));
      f.add(new Vulcan_n(2400782796413133920L));
      f.add(new Vulcan_n(5478518901776327053L));
      f.add(new Vulcan_n(-640088315966111724L));
      f.add(new Vulcan_n(-755167044251980317L));
      f.add(new Vulcan_n(-813573579705370321L));
      f.add(new Vulcan_n(4490338934329443455L));
      f.add(new Vulcan_n(2227945106226577676L));
      f.add(new Vulcan_n(9102030768220476918L));
      f.add(new Vulcan_n(1201476913795144210L));
      f.add(new Vulcan_n(-9191755193555856098L));
      f.add(new Vulcan_n(3817827401212154118L));
      f.add(new Vulcan_n(-6059020648505385059L));
      f.add(new Vulcan_n(4398112673564544061L));
      f.add(new Vulcan_n(5352141853197293788L));
      f.add(new Vulcan_n(5358235876121973423L));
      f.add(new Vulcan_n(3603002684832249675L));
      f.add(new Vulcan_n(950664215691071645L));
      f.add(new Vulcan_n(7607183130475327826L));
      f.add(new Vulcan_n(-4748826611134960351L));
      f.add(new Vulcan_n(350958358708402827L));
      f.add(new Vulcan_n(-4856474173667482063L));
      f.add(new Vulcan_n(-7844302350007868732L));
      f.add(new Vulcan_n(6603924458929858786L));
      f.add(new Vulcan_n(7010726269764993914L));
      f.add(new Vulcan_n(875685324482061641L));
      f.add(new Vulcan_n(3079235193308243454L));
      f.add(new Vulcan_n(-1775145844503175838L));
      f.add(new Vulcan_n(3351453818355397506L));
      f.add(new Vulcan_n(-160424318491150615L));
      f.add(new Vulcan_n(-5672262642978028765L));
      f.add(new Vulcan_n(2562766285677472284L));
      f.add(new Vulcan_n(-6457812301251475058L));
      f.add(new Vulcan_n(4158678587301830987L));
      f.add(new Vulcan_n(-1113773781859054238L));
      f.add(new Vulcan_n(-7651513194231331665L));
      f.add(new Vulcan_n(-1228765212845905356L));
      f.add(new Vulcan_n(-4144064212979250890L));
      f.add(new Vulcan_n(-377838968259674831L));
      f.add(new Vulcan_n(4762128723456763981L));
      f.add(new Vulcan_n(-5930974379025303300L));
      f.add(new Vulcan_n(1138705769524769317L));
      f.add(new Vulcan_n(2765364511008654513L));
      f.add(new Vulcan_n(-2095835837679420063L));
      f.add(new Vulcan_n(-2562673357817205370L));
      f.add(new Vulcan_n(7648233196091506473L));
      f.add(new Vulcan_n(4122445040847003550L));
      f.add(new Vulcan_n(-3043977085537042302L));
      f.add(new Vulcan_n(-5085137906674977947L));
      f.add(new Vulcan_n(-8873883970437087217L));
      f.add(new Vulcan_n(-1424768452538381892L));
      f.add(new Vulcan_n(-2362428040836599711L));
      f.add(new Vulcan_n(-1207841085844124833L));
      f.add(new Vulcan_n(1008380289891854323L));
      f.add(new Vulcan_n(-8442636007646086553L));
      f.add(new Vulcan_n(-8427595466325869141L));
      f.add(new Vulcan_n(9148828898063065640L));
      f.add(new Vulcan_n(-8144507591789701861L));
      f.add(new Vulcan_n(-1235595079847654542L));
      f.add(new Vulcan_n(-3156218677041843190L));
      f.add(new Vulcan_n(141254375119299323L));
      f.add(new Vulcan_n(-6965636920164197144L));
      f.add(new Vulcan_n(3242459584267422740L));
      f.add(new Vulcan_n(-7185006384128812195L));
      f.add(new Vulcan_n(4669553275553868779L));
      f.add(new Vulcan_n(7998963818714031333L));
      f.add(new Vulcan_n(7132013455536893688L));
      f.add(new Vulcan_n(4316227738523642444L));
      f.add(new Vulcan_n(-3221414257327164077L));
      f.add(new Vulcan_n(-2071896567763043399L));
      f.add(new Vulcan_n(424849726679119113L));
      f.add(new Vulcan_n(7774504460626286096L));
      f.add(new Vulcan_n(-1659626611879592141L));
      f.add(new Vulcan_n(-6978603809206346859L));
      f.add(new Vulcan_n(1030145539984150758L));
      f.add(new Vulcan_n(3354725411887180708L));
      f.add(new Vulcan_n(-1328381029403803819L));
      f.add(new Vulcan_n(3900721891186665835L));
      f.add(new Vulcan_n(1877714882969194159L));
      f.add(new Vulcan_n(1030114725731552721L));
      f.add(new Vulcan_n(-2766379711326698381L));
      f.add(new Vulcan_n(7121336921731091683L));
      f.add(new Vulcan_n(2448148971626249915L));
      f.add(new Vulcan_n(-5892187480116458247L));
      f.add(new Vulcan_n(-689401554632537830L));
      f.add(new Vulcan_n(5282230145927719789L));
      f.add(new Vulcan_n(2285162620498618905L));
      f.add(new Vulcan_n(4186490770250835730L));
      f.add(new Vulcan_n(499832899614359915L));
      f.add(new Vulcan_n(5008379899397035350L));
      f.add(new Vulcan_n(8744448561320568153L));
      f.add(new Vulcan_n(160507617272405800L));
      f.add(new Vulcan_n(2815916950899826537L));
      f.add(new Vulcan_n(-3180363831809015454L));
      f.add(new Vulcan_n(4258817439536559252L));
      f.add(new Vulcan_n(8055646103624093112L));
      f.add(new Vulcan_n(9164391101539957L));
      f.add(new Vulcan_n(1541354904272108153L));
      f.add(new Vulcan_n(7472373896086621018L));
      f.add(new Vulcan_n(7807433778501498157L));
      f.add(new Vulcan_n(-7828247470783919753L));
      f.add(new Vulcan_n(7978327011114523501L));
      f.add(new Vulcan_n(6518176804483004265L));
      f.add(new Vulcan_n(3818379255818018831L));
      f.add(new Vulcan_n(7760195352389969596L));
      f.add(new Vulcan_n(4016491518369276445L));
      f.add(new Vulcan_n(1542021522929222505L));
      e = (int[])e.clone();
      f.add(new Vulcan_n(-2022636667535992272L));
      f.add(new Vulcan_n(1983973626551268121L));
      f.add(new Vulcan_n(3698561006867452892L));
      f.add(new Vulcan_n(-2906052018893175891L));
      f.add(new Vulcan_n(-8672322919557178503L));
      f.add(new Vulcan_n(-2793787703558672616L));
      f.add(new Vulcan_n(1975240993390300493L));
      f.add(new Vulcan_n(-220890963660199363L));
      f.add(new Vulcan_n(168677806825678705L));
      f.add(new Vulcan_n(2256387880450684942L));
      f.add(new Vulcan_n(-6607647733402530930L));
      f.add(new Vulcan_n(8730837254339214743L));
      f.add(new Vulcan_n(-6223922418097570614L));
      f.add(new Vulcan_n(5945673908515882376L));
      f.add(new Vulcan_n(7456898246569625833L));
      f.add(new Vulcan_n(-8273789414250885317L));
      f.add(new Vulcan_n(288509458130292865L));
      f.add(new Vulcan_n(-419471962137222156L));
      f.add(new Vulcan_n(-1626462781219688379L));
      f.add(new Vulcan_n(-3597439685074095828L));
      f.add(new Vulcan_n(3244213881088847136L));
      f.add(new Vulcan_n(6931239928257138903L));
      f.add(new Vulcan_n(-2076782040354622639L));
      f.add(new Vulcan_n(311391491859813039L));
      f.add(new Vulcan_n(-9219097696722783880L));
      f.add(new Vulcan_n(4850358645724417414L));
      f.add(new Vulcan_n(8483218114206722472L));
      f.add(new Vulcan_n(-1283123900292524154L));
      f.add(new Vulcan_n(3996263667839553171L));
      f.add(new Vulcan_n(-6856838064099577361L));
      f.add(new Vulcan_n(2284499202875832894L));
      f.add(new Vulcan_n(-6807033287699012108L));
      f.add(new Vulcan_n(-5091491643285594372L));
      f.add(new Vulcan_n(-839862759397789038L));
      f.add(new Vulcan_n(-6809492652091480226L));
      f.add(new Vulcan_n(836754670181550191L));
      f.add(new Vulcan_n(4849423581633187543L));
      f.add(new Vulcan_n(1608102135920271091L));
      f.add(new Vulcan_n(7901573640030047736L));
      f.add(new Vulcan_n(-1063787289120672635L));
      f.add(new Vulcan_n(-7451439048975697202L));
      f.add(new Vulcan_n(2238703159711620942L));
      f.add(new Vulcan_n(-2650116043158679578L));
      f.add(new Vulcan_n(7168279120484390146L));
      f.add(new Vulcan_n(146139013836961920L));
      f.add(new Vulcan_n(-5462556059031372625L));
      f.add(new Vulcan_n(7174512941692113309L));
      f.add(new Vulcan_n(1166074022075833915L));
      f.add(new Vulcan_n(6087237988491005435L));
      f.add(new Vulcan_n(581682592034791315L));
      f.add(new Vulcan_n(906519588742009018L));
      f.add(new Vulcan_n(769224409991094085L));
      f.add(new Vulcan_n(-5668941101867268052L));
      f.add(new Vulcan_n(8343168027167300782L));
      f.add(new Vulcan_n(5375746809115389394L));
      f.add(new Vulcan_n(-5291770584871198378L));
      f.add(new Vulcan_n(2653075628510749637L));
      f.add(new Vulcan_n(5075956373428557539L));
      f.add(new Vulcan_n(4727900009667104480L));
      f.add(new Vulcan_n(-5276637051209140413L));
      f.add(new Vulcan_n(8023874181845466683L));
      f.add(new Vulcan_n(6878622660597783756L));
      f.add(new Vulcan_n(-2808646615489730111L));
      f.add(new Vulcan_n(-230686513313875207L));
      f.add(new Vulcan_n(7833518903063121923L));
      f.add(new Vulcan_n(-153362218617858449L));
      f.add(new Vulcan_n(-7933796967087028078L));
      f.add(new Vulcan_n(7482868585977805182L));
      f.add(new Vulcan_n(1289654859064257875L));
      f.add(new Vulcan_n(7493632341210835089L));
      f.add(new Vulcan_n(3682480734876094094L));
      f.add(new Vulcan_n(-3039510718651915805L));
      f.add(new Vulcan_n(-1175506918381844538L));
      f.add(new Vulcan_n(-8271780275379876709L));
      f.add(new Vulcan_n(-3165223462099655988L));
      f.add(new Vulcan_n(2364570387279646433L));
      f.add(new Vulcan_n(667639859699029588L));
      f.add(new Vulcan_n(-6147015948924311917L));
      f.add(new Vulcan_n(3938843776800646166L));
      f.add(new Vulcan_n(647414785530666919L));
      f.add(new Vulcan_n(-5533459554914744771L));
      f.add(new Vulcan_n(-6485523008565657188L));
      f.add(new Vulcan_n(1241867523879047069L));
      f.add(new Vulcan_n(-827560270136810382L));
      e = (int[])e.clone();
      f.add(new Vulcan_n(2989803301978135231L));
      f.add(new Vulcan_n(3774426820534334916L));
      f.add(new Vulcan_n(-4389366546048775760L));
      f.add(new Vulcan_n(-8469928253034718805L));
      f.add(new Vulcan_n(1731332905470392704L));
      f.add(new Vulcan_n(8604718427086692226L));
      f.add(new Vulcan_n(-7279163613866852569L));
      f.add(new Vulcan_n(7830694680592833573L));
      f.add(new Vulcan_n(396490208149942043L));
      f.add(new Vulcan_n(6110017927853145523L));
      f.add(new Vulcan_n(-7525358426201303514L));
      f.add(new Vulcan_n(2158782785119574578L));
      f.add(new Vulcan_n(-7028267575177732757L));
      f.add(new Vulcan_n(-216808631872274129L));
      f.add(new Vulcan_n(6746476609478447524L));
      f.add(new Vulcan_n(6674588471709943521L));
      f.add(new Vulcan_n(1126726180040657442L));
      f.add(new Vulcan_n(7570073337511087491L));
      f.add(new Vulcan_n(-1473660922987711387L));
      f.add(new Vulcan_n(-4139509366776374375L));
      f.add(new Vulcan_n(4715709262367856284L));
      f.add(new Vulcan_n(-7120327813971825438L));
      f.add(new Vulcan_n(-5149887718351550002L));
      f.add(new Vulcan_n(-8299156028669072973L));
      f.add(new Vulcan_n(3527468806703397368L));
      f.add(new Vulcan_n(7018912492196157038L));
      f.add(new Vulcan_n(-333197450791394719L));
      f.add(new Vulcan_n(-6254518592166793094L));
      f.add(new Vulcan_n(-9145135576481918024L));
      f.add(new Vulcan_n(-3809413961036101099L));
      f.add(new Vulcan_n(8889627101926270560L));
      f.add(new Vulcan_n(383632595509947803L));
      f.add(new Vulcan_n(2320009642217163747L));
      f.add(new Vulcan_n(4853495741377754795L));
      f.add(new Vulcan_n(-2876195717884321319L));
      f.add(new Vulcan_n(5383623006956443992L));
      f.add(new Vulcan_n(261928383891477685L));
      f.add(new Vulcan_n(-6261172341030338277L));
      f.add(new Vulcan_n(4882768358697042885L));
      f.add(new Vulcan_n(6442680909497117070L));
      f.add(new Vulcan_n(-4160201756936651017L));
      f.add(new Vulcan_n(-5877848529242476171L));
      f.add(new Vulcan_n(2863673929852365090L));
      f.add(new Vulcan_n(1811283487622350061L));
      f.add(new Vulcan_n(7478935170493752183L));
      f.add(new Vulcan_n(-3274140869077215645L));
      f.add(new Vulcan_n(-1002880061663718224L));
      f.add(new Vulcan_n(1038204491532583326L));
      f.add(new Vulcan_n(-877576952065501316L));
      f.add(new Vulcan_n(1936004031800756943L));
      f.add(new Vulcan_n(-5513820729003107064L));
      f.add(new Vulcan_n(1650212829974846077L));
      f.add(new Vulcan_n(-4468321076980180280L));
      f.add(new Vulcan_n(-8527946309330423952L));
      f.add(new Vulcan_n(9016065692323069240L));
      f.add(new Vulcan_n(-4495893149991563355L));
      f.add(new Vulcan_n(-4277728543203154185L));
      f.add(new Vulcan_n(4810420670971657766L));
      f.add(new Vulcan_n(8364989664456797318L));
      f.add(new Vulcan_n(6061289072987516444L));
      f.add(new Vulcan_n(2309986111675902772L));
      f.add(new Vulcan_n(-3388416673522016655L));
      f.add(new Vulcan_n(6676407848436982561L));
      f.add(new Vulcan_n(4968835132252486068L));
      f.add(new Vulcan_n(-8955385363388235746L));
      f.add(new Vulcan_n(6799399150917154194L));
      f.add(new Vulcan_n(4168414237268474376L));
      f.add(new Vulcan_n(6287157881274968124L));
      f.add(new Vulcan_n(-9029538674917245775L));
      f.add(new Vulcan_n(6504915852149714114L));
      f.add(new Vulcan_n(6728659868778340392L));
      f.add(new Vulcan_n(6158291886404696963L));
      f.add(new Vulcan_n(-5972019066859901071L));
      f.add(new Vulcan_n(-1969879590020740238L));
      f.add(new Vulcan_n(6162612153704830637L));
      f.add(new Vulcan_n(6892985871986047146L));
      f.add(new Vulcan_n(57379444726533973L));
      f.add(new Vulcan_n(-5873469836014279721L));
      f.add(new Vulcan_n(2288492356529769537L));
      f.add(new Vulcan_n(-2775842203590569784L));
      f.add(new Vulcan_n(-453249813244923000L));
      f.add(new Vulcan_n(-893208982286044900L));
      f.add(new Vulcan_n(5745308180746081846L));
      f.add(new Vulcan_n(-8388966428398215349L));
      f.add(new Vulcan_n(2733192798586447669L));
      f.add(new Vulcan_n(3573594496519526772L));
      f.add(new Vulcan_n(-6124770178061143758L));
      f.add(new Vulcan_n(3942909390223485805L));
      f.add(new Vulcan_n(-8598442809347278082L));
      f.add(new Vulcan_n(3213487975601431642L));
      f.add(new Vulcan_n(4836192850784067564L));
      f.add(new Vulcan_n(487196039008589340L));
      f.add(new Vulcan_n(5954305056045488184L));
      f.add(new Vulcan_n(-9069724432482565700L));
      f.add(new Vulcan_n(-2180389947326662497L));
      f.add(new Vulcan_n(6664546845156453755L));
      f.add(new Vulcan_n(5047685845473038868L));
      f.add(new Vulcan_n(3643075147340316768L));
      f.add(new Vulcan_n(-7935300156956489898L));
      f.add(new Vulcan_n(-8681238954918921275L));
      f.add(new Vulcan_n(2750141023477267885L));
      f.add(new Vulcan_n(-3941033741057343494L));
      f.add(new Vulcan_n(5229351660709539534L));
      f.add(new Vulcan_n(-3641595361079349289L));
      f.add(new Vulcan_n(3353368265368926183L));
      f.add(new Vulcan_n(-5470045865392303511L));
      f.add(new Vulcan_n(9173712900558243561L));
      f.add(new Vulcan_n(1203686299510685060L));
      f.add(new Vulcan_n(-3951897509048949153L));
      f.add(new Vulcan_n(8748324337313351642L));
      f.add(new Vulcan_n(-8964698754307911347L));
      f.add(new Vulcan_n(-7425041296211766533L));
      f.add(new Vulcan_n(3095190913634606946L));
      f.add(new Vulcan_n(-8917032160686224512L));
      f.add(new Vulcan_n(7167236821628736142L));
      f.add(new Vulcan_n(-1879230247300019937L));
      f.add(new Vulcan_n(-1548539232146894245L));
      f.add(new Vulcan_n(5143023292198781921L));
      e = (int[])e.clone();
      f.add(new Vulcan_n(-4588776200525018653L));
      f.add(new Vulcan_n(5163030355466469109L));
      f.add(new Vulcan_n(-4314709751888469484L));
      f.add(new Vulcan_n(3958579124353868318L));
      f.add(new Vulcan_n(-8308436376804378349L));
      f.add(new Vulcan_n(-7753914704587390704L));
      f.add(new Vulcan_n(1526541369804408468L));
      f.add(new Vulcan_n(-6551410319833284096L));
      f.add(new Vulcan_n(779407194217006821L));
      f.add(new Vulcan_n(-5051511137713899685L));
      f.add(new Vulcan_n(-6641870145538987239L));
      f.add(new Vulcan_n(-2814796745217328953L));
      f.add(new Vulcan_n(6621803306985536037L));
      f.add(new Vulcan_n(-8734912110965470161L));
      f.add(new Vulcan_n(6449264491120381716L));
      f.add(new Vulcan_n(-296329581218796363L));
      f.add(new Vulcan_n(-4314736703235991362L));
      f.add(new Vulcan_n(5595516279356278337L));
      f.add(new Vulcan_n(-8874788446970382271L));
      f.add(new Vulcan_n(317896736629744646L));
      f.add(new Vulcan_n(-8283503412779016670L));
      f.add(new Vulcan_n(8646973588930624212L));
      f.add(new Vulcan_n(-3266135622440052973L));
      f.add(new Vulcan_n(-7809328988432299064L));
      f.add(new Vulcan_n(-8510515914060180017L));
      f.add(new Vulcan_n(4980855261212510562L));
      f.add(new Vulcan_n(688928691049613774L));
      f.add(new Vulcan_n(-6016825861255117571L));
      f.add(new Vulcan_n(808585129947614411L));
      f.add(new Vulcan_n(-7269500266072677091L));
      f.add(new Vulcan_n(3482678494575696323L));
      f.add(new Vulcan_n(-6101465363889373081L));
      f.add(new Vulcan_n(427161508867363933L));
      f.add(new Vulcan_n(-9177086820560332767L));
      f.add(new Vulcan_n(-1237963786537181299L));
      f.add(new Vulcan_n(-178569955445012419L));
      f.add(new Vulcan_n(-8095398177311093860L));
      f.add(new Vulcan_n(8291995227482554804L));
      f.add(new Vulcan_n(7056786262889691582L));
      f.add(new Vulcan_n(275703045114130052L));
      f.add(new Vulcan_n(1256521563801675641L));
      f.add(new Vulcan_n(-7079744081100316940L));
      f.add(new Vulcan_n(-2617668846609037205L));
      f.add(new Vulcan_n(-6749896994166899421L));
      f.add(new Vulcan_n(4518906096970113024L));
      f.add(new Vulcan_n(5257536132951995590L));
      f.add(new Vulcan_n(8425919770123232346L));
      f.add(new Vulcan_n(-4795027916522764232L));
      f.add(new Vulcan_n(2420499067268595524L));
      f.add(new Vulcan_n(2397476693427109341L));
      f.add(new Vulcan_n(-5619992242588093108L));
      f.add(new Vulcan_n(5308069522590488399L));
      f.add(new Vulcan_n(4014832180290407224L));
      f.add(new Vulcan_n(2528156803207879181L));
      f.add(new Vulcan_n(-5471945982200226454L));
      f.add(new Vulcan_n(8780908591663092712L));
      f.add(new Vulcan_n(561979477704599320L));
      f.add(new Vulcan_n(-1264960264792293294L));
      f.add(new Vulcan_n(-3607854942112107824L));
      f.add(new Vulcan_n(-2988589049503700319L));
      f.add(new Vulcan_n(-2265156660106341580L));
      f.add(new Vulcan_n(-2757835615665747003L));
      f.add(new Vulcan_n(4861905325484203549L));
      f.add(new Vulcan_n(-7753053138958582276L));
      f.add(new Vulcan_n(4780146228675936732L));
      f.add(new Vulcan_n(9190509635506029687L));
      f.add(new Vulcan_n(6790754637468297141L));
      f.add(new Vulcan_n(2118619906546374908L));
      f.add(new Vulcan_n(-39545462185622762L));
      f.add(new Vulcan_n(3538120743310580291L));
      f.add(new Vulcan_n(3878649721227991847L));
      f.add(new Vulcan_n(5593395245905073131L));
      f.add(new Vulcan_n(1378165498343441288L));
      f.add(new Vulcan_n(698952780696886489L));
      f.add(new Vulcan_n(-5793015253175356142L));
      f.add(new Vulcan_n(582190038984806982L));
      f.add(new Vulcan_n(-6870656676574101873L));
      f.add(new Vulcan_n(-4888727416766912573L));
      f.add(new Vulcan_n(-6117942615526550044L));
      f.add(new Vulcan_n(5436617929709862894L));
      f.add(new Vulcan_n(-8448526953287836953L));
      f.add(new Vulcan_n(-7160380136570407637L));
      f.add(new Vulcan_n(-3599447336890022919L));
      f.add(new Vulcan_n(7210512109822899795L));
      f.add(new Vulcan_n(-6085739738647226021L));
      f.add(new Vulcan_n(-4958834770949274312L));
      f.add(new Vulcan_n(-8728494219376227002L));
      f.add(new Vulcan_n(666105230529679235L));
      f.add(new Vulcan_n(1902951674423780394L));
      f.add(new Vulcan_n(3496061325191558013L));
      f.add(new Vulcan_n(5831825683053178572L));
      f.add(new Vulcan_n(4003302962391950512L));
      f.add(new Vulcan_n(-2970853970541071701L));
      f.add(new Vulcan_n(-1123255638903493731L));
      f.add(new Vulcan_n(7904864149323316053L));
      f.add(new Vulcan_n(8411015710782006828L));
      f.add(new Vulcan_n(-3655222351002243979L));
      f.add(new Vulcan_n(-1432197813611986853L));
      f.add(new Vulcan_n(-1989036155252662788L));
      f.add(new Vulcan_n(1254670956321869152L));
      f.add(new Vulcan_n(-1903169742837790744L));
      f.add(new Vulcan_n(846435845513056328L));
      f.add(new Vulcan_n(4562412067697732524L));
      f.add(new Vulcan_n(-4911427986777590384L));
      f.add(new Vulcan_n(8315622884826927489L));
      f.add(new Vulcan_n(7746528968703115656L));
      f.add(new Vulcan_n(8948374813664394459L));
      f.add(new Vulcan_n(6373121804112038017L));
      f.add(new Vulcan_n(-2972705425474602134L));
      f.add(new Vulcan_n(-192561025277306193L));
      f.add(new Vulcan_n(946019511789141923L));
      f.add(new Vulcan_n(8371055622873344122L));
      f.add(new Vulcan_n(-6322115190766433022L));
      f.add(new Vulcan_n(-4607070797123277106L));
      f.add(new Vulcan_n(4208062976847317722L));
      f.add(new Vulcan_n(1531781487093301662L));
      f.add(new Vulcan_n(-6376298526225147768L));
      f.add(new Vulcan_n(6955790629032225940L));
      f.add(new Vulcan_n(-7273987039514375482L));
      f.add(new Vulcan_n(-2813501899162699113L));
      f.add(new Vulcan_n(-3731947681307605816L));
      f.add(new Vulcan_n(23981831249581550L));
      f.add(new Vulcan_n(836478997828625273L));
      f.add(new Vulcan_n(-853570841950788661L));
      f.add(new Vulcan_n(-1195356215369878850L));
      f.add(new Vulcan_n(-5110161336665334891L));
      f.add(new Vulcan_n(9107043532612782204L));
      f.add(new Vulcan_n(-8981245177680767126L));
      f.add(new Vulcan_n(-1018595758496593408L));
      f.add(new Vulcan_n(7759139586643022034L));
      f.add(new Vulcan_n(-264978019071452609L));
      f.add(new Vulcan_n(-7430031394803218983L));
      f.add(new Vulcan_n(6012397310381724871L));
      f.add(new Vulcan_n(329576874116516376L));
      f.add(new Vulcan_n(6850424448637499602L));
      f.add(new Vulcan_n(-4246030127806189119L));
      f.add(new Vulcan_n(-6096799871912277902L));
      f.add(new Vulcan_n(-6995466844876069631L));
      f.add(new Vulcan_n(-760500577231307438L));
      f.add(new Vulcan_n(3190618389733401035L));
      f.add(new Vulcan_n(-8595806048569166140L));
      f.add(new Vulcan_n(-7661223713766608179L));
      f.add(new Vulcan_n(1554928505968774271L));
      f.add(new Vulcan_n(-8875475334295350205L));
      f.add(new Vulcan_n(-7962867797479761032L));
      f.add(new Vulcan_n(-6966107998145586964L));
      f.add(new Vulcan_n(1611779630141899005L));
      f.add(new Vulcan_n(1247807834463548271L));
      f.add(new Vulcan_n(1832569901694450759L));
      f.add(new Vulcan_n(1614019700443073039L));
      f.add(new Vulcan_n(74206056837056707L));
      f.add(new Vulcan_n(-3731986718552524310L));
      f.add(new Vulcan_n(-3445382912827499751L));
      f.add(new Vulcan_n(-7611483738119981932L));
      f.add(new Vulcan_n(2838878043990430270L));
      f.add(new Vulcan_n(-2941472574336234510L));
      f.add(new Vulcan_n(-7873797449193262099L));
      e = (int[])e.clone();
      f.add(new Vulcan_n(-9010659287608630180L));
      f.add(new Vulcan_n(-7546220386105983752L));
      f.add(new Vulcan_n(-5659338015362759307L));
      f.add(new Vulcan_n(7430593071326537112L));
      f.add(new Vulcan_n(8575541088014814653L));
      f.add(new Vulcan_n(-9010659287608630148L));
      f.add(new Vulcan_n(-5770570008397142971L));
      f.add(new Vulcan_n(2963265643796082353L));
      f.add(new Vulcan_n(-318106183782094479L));
      f.add(new Vulcan_n(7883251884362126446L));
      f.add(new Vulcan_n(-1223720696145309256L));
      f.add(new Vulcan_n(730434652118975269L));
      f.add(new Vulcan_n(7002323335028983380L));
      f.add(new Vulcan_n(-4449750682882864964L));
      f.add(new Vulcan_n(-7540898751500197812L));
      f.add(new Vulcan_n(-1789533788700209531L));
      f.add(new Vulcan_n(-4505111055356112941L));
      f.add(new Vulcan_n(6204342615741243932L));
      f.add(new Vulcan_n(2722213472950705000L));
      f.add(new Vulcan_n(6776564568136443475L));
      f.add(new Vulcan_n(8112557473110795791L));
      f.add(new Vulcan_n(-2016204170224853572L));
      f.add(new Vulcan_n(-5472168503931503334L));
      f.add(new Vulcan_n(8066505403871171769L));
      f.add(new Vulcan_n(8998035697648920959L));
      f.add(new Vulcan_n(-5127117355466278846L));
      f.add(new Vulcan_n(5932932202155492750L));
      f.add(new Vulcan_n(3111507436012658235L));
      f.add(new Vulcan_n(-735605284250656504L));
      f.add(new Vulcan_n(-4629743078695538679L));
      f.add(new Vulcan_n(294776621637249956L));
      f.add(new Vulcan_n(-4046015096914012310L));
      f.add(new Vulcan_n(-6771675696947871875L));
      f.add(new Vulcan_n(-5698447210458119942L));
      f.add(new Vulcan_n(4285115523064196837L));
      f.add(new Vulcan_n(4886562209157782578L));
      f.add(new Vulcan_n(6679287665944263693L));
      f.add(new Vulcan_n(222535833536953973L));
      f.add(new Vulcan_n(9196769209592756163L));
      f.add(new Vulcan_n(5419127763458332098L));
      f.add(new Vulcan_n(8027331112880627337L));
      f.add(new Vulcan_n(1401704397551328874L));
      f.add(new Vulcan_n(1387059131505036672L));
      f.add(new Vulcan_n(-2161219207082674836L));
      f.add(new Vulcan_n(-1537751523585105745L));
      f.add(new Vulcan_n(4074420804898606568L));
      f.add(new Vulcan_n(-5577663930448982851L));
      f.add(new Vulcan_n(-5643622639517801246L));
      f.add(new Vulcan_n(-6490773087628084168L));
      f.add(new Vulcan_n(-3312001940376673712L));
      f.add(new Vulcan_n(-5242896854284790469L));
      f.add(new Vulcan_n(7168087184502994190L));
      f.add(new Vulcan_n(-6090889409507038877L));
      f.add(new Vulcan_n(257051182896641553L));
      f.add(new Vulcan_n(2878993285277580781L));
      f.add(new Vulcan_n(-901821013498115515L));
      f.add(new Vulcan_n(-4014227863231252088L));
      f.add(new Vulcan_n(6092482357686894911L));
      f.add(new Vulcan_n(-196337120445105058L));
      f.add(new Vulcan_n(1124636078485514441L));
      f.add(new Vulcan_n(-5730419819504316394L));
      f.add(new Vulcan_n(6412574676607858986L));
      f.add(new Vulcan_n(3453403908068011805L));
      f.add(new Vulcan_n(667631063606138452L));
      f.add(new Vulcan_n(5059812481198136311L));
      f.add(new Vulcan_n(-6262744281506148121L));
      f.add(new Vulcan_n(-2884136301876814845L));
      f.add(new Vulcan_n(8696504172727659475L));
      f.add(new Vulcan_n(3367773230928555260L));
      f.add(new Vulcan_n(3931001390765427079L));
      f.add(new Vulcan_n(5884967613460891029L));
      f.add(new Vulcan_n(-44835019833577155L));
      f.add(new Vulcan_n(-1180563308358236548L));
      f.add(new Vulcan_n(-6530380474961393968L));
      f.add(new Vulcan_n(-7544074705131065428L));
      f.add(new Vulcan_n(-7288166572867075224L));
      f.add(new Vulcan_n(-5301899671055710983L));
      f.add(new Vulcan_n(1856828826581289934L));
      f.add(new Vulcan_n(3520804166410153754L));
      f.add(new Vulcan_n(-5464261727827290223L));
      f.add(new Vulcan_n(-7468724327811858028L));
      f.add(new Vulcan_n(-673844515070386562L));
      f.add(new Vulcan_n(3863088075739718027L));
      f.add(new Vulcan_n(-9027894604682775077L));
      f.add(new Vulcan_n(8396396265639861539L));
      e = new int[]{-20, -45, -54, -49, -46, -21, -21, -32, -32, -36, -45, -47, -23, -25, -1, 1, -17, -14, -3, -13, 20, 3, -32, -26, -33, -19, 21, 21, -19, -34, -12, 14, 13, 17, -28, 23, -15, -22, 25, 32, 32, -7, 12, -10, 19, 36, 45, 19, 7, 26, 46, 15, 49, 10, 32, 45, 54, 33, 47, 22, -1, 1, 28, 34};
   }
}
