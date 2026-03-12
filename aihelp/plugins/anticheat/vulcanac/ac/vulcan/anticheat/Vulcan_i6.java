package ac.vulcan.anticheat;

import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Vulcan_i6 extends Format {
   private static final long serialVersionUID = 1L;
   public static final int Vulcan_I = 0;
   public static final int Vulcan_g = 1;
   public static final int Vulcan_N = 2;
   public static final int Vulcan_o = 3;
   private static String Vulcan_a;
   private static final Map Vulcan_i;
   private static final Map Vulcan_f;
   private static final Map Vulcan_b;
   private static final Map Vulcan_t;
   private static final Map Vulcan_v;
   private final String Vulcan_Y;
   private final TimeZone Vulcan_P;
   private final boolean Vulcan_T;
   private final Locale Vulcan_X;
   private final boolean Vulcan_F;
   private transient Vulcan_e3[] Vulcan_A;
   private transient int Vulcan_m;
   private static String[] Vulcan_D;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-6310027010411834444L, 4071385891852932640L, (Object)null).a(39376914299110L);
   private static final String[] b;

   public static Vulcan_i6 Vulcan_b(Object[] var0) {
      long var1 = (Long)var0[0];
      var1 ^= a;
      long var3 = var1 ^ 53228914459100L;
      long var5 = var1 ^ 136521674159991L;
      return Vulcan_V(new Object[]{new Long(var5), Vulcan_e(new Object[]{new Long(var3)}), null, null});
   }

   public static Vulcan_i6 Vulcan_o(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 38125717227500L;
      return Vulcan_V(new Object[]{new Long(var4), var3, null, null});
   }

   public static Vulcan_i6 Vulcan_M(Object[] var0) {
      String var4 = (String)var0[0];
      TimeZone var3 = (TimeZone)var0[1];
      long var1 = (Long)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 35517953321615L;
      return Vulcan_V(new Object[]{new Long(var5), var4, var3, null});
   }

   public static Vulcan_i6 Vulcan_f(Object[] var0) {
      long var3 = (Long)var0[0];
      String var2 = (String)var0[1];
      Locale var1 = (Locale)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 98657000928758L;
      return Vulcan_V(new Object[]{new Long(var5), var2, null, var1});
   }

   public static synchronized Vulcan_i6 Vulcan_V(Object[] var0) {
      long var2 = (Long)var0[0];
      String var5 = (String)var0[1];
      TimeZone var1 = (TimeZone)var0[2];
      Locale var4 = (Locale)var0[3];
      var2 ^= a;
      long var6 = var2 ^ 19251216233464L;
      Vulcan_i6 var8 = new Vulcan_i6(var5, var1, var4);
      Vulcan_i6 var9 = (Vulcan_i6)Vulcan_i.get(var8);
      if (var9 == null) {
         var9 = var8;
         var8.Vulcan_w(new Object[]{new Long(var6)});
         Vulcan_i.put(var8, var8);
      }

      return var9;
   }

   public static Vulcan_i6 Vulcan_p(Object[] var0) {
      int var1 = (Integer)var0[0];
      long var2 = (Long)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 2016244930449L;
      return Vulcan_u(new Object[]{new Long(var4), new Integer(var1), null, null});
   }

   public static Vulcan_i6 Vulcan_r(Object[] var0) {
      int var1 = (Integer)var0[0];
      long var2 = (Long)var0[1];
      Locale var4 = (Locale)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 114847797272444L;
      return Vulcan_u(new Object[]{new Long(var5), new Integer(var1), null, var4});
   }

   public static Vulcan_i6 Vulcan_e(Object[] var0) {
      int var4 = (Integer)var0[0];
      long var1 = (Long)var0[1];
      TimeZone var3 = (TimeZone)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 114190104480247L;
      return Vulcan_u(new Object[]{new Long(var5), new Integer(var4), var3, null});
   }

   public static synchronized Vulcan_i6 Vulcan_u(Object[] var0) {
      long var3 = (Long)var0[0];
      int var5 = (Integer)var0[1];
      TimeZone var2 = (TimeZone)var0[2];
      Locale var1 = (Locale)var0[3];
      var3 ^= a;
      long var6 = var3 ^ 108492352383295L;
      String[] var10000 = Vulcan_x();
      Object var9 = new Integer(var5);
      String[] var8 = var10000;
      if (var2 != null) {
         var9 = new Vulcan_eM(var9, var2);
      }

      label45: {
         Locale var17;
         label44: {
            try {
               var17 = var1;
               if (var8 != null) {
                  break label44;
               }

               if (var1 != null) {
                  break label45;
               }
            } catch (ClassCastException var15) {
               throw a(var15);
            }

            var17 = Locale.getDefault();
         }

         var1 = var17;
      }

      Vulcan_eM var16 = new Vulcan_eM(var9, var1);
      Vulcan_i6 var10 = (Vulcan_i6)Vulcan_f.get(var16);

      Vulcan_i6 var18;
      label34: {
         try {
            var18 = var10;
            if (var8 != null) {
               return var18;
            }

            if (var10 != null) {
               break label34;
            }
         } catch (ClassCastException var14) {
            throw a(var14);
         }

         try {
            SimpleDateFormat var11 = (SimpleDateFormat)DateFormat.getDateInstance(var5, var1);
            String var12 = var11.toPattern();
            var10 = Vulcan_V(new Object[]{new Long(var6), var12, var2, var1});
            Vulcan_f.put(var16, var10);
         } catch (ClassCastException var13) {
            throw new IllegalArgumentException(b[3] + var1);
         }
      }

      var18 = var10;
      return var18;
   }

   public static Vulcan_i6 Vulcan_T(Object[] var0) {
      int var3 = (Integer)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 93325829711939L;
      return Vulcan_i(new Object[]{new Integer(var3), new Long(var4), null, null});
   }

   public static Vulcan_i6 Vulcan_n(Object[] var0) {
      long var3 = (Long)var0[0];
      int var2 = (Integer)var0[1];
      Locale var1 = (Locale)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 117504264114856L;
      return Vulcan_i(new Object[]{new Integer(var2), new Long(var5), null, var1});
   }

   public static Vulcan_i6 Vulcan_G(Object[] var0) {
      int var2 = (Integer)var0[0];
      TimeZone var1 = (TimeZone)var0[1];
      long var3 = (Long)var0[2];
      var3 ^= a;
      long var5 = var3 ^ 99373006114508L;
      return Vulcan_i(new Object[]{new Integer(var2), new Long(var5), var1, null});
   }

   public static synchronized Vulcan_i6 Vulcan_i(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static Vulcan_i6 Vulcan_H(Object[] var0) {
      long var1 = (Long)var0[0];
      int var4 = (Integer)var0[1];
      int var3 = (Integer)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 72318180141667L;
      return Vulcan_E(new Object[]{new Integer(var4), new Integer(var3), new Long(var5), null, null});
   }

   public static Vulcan_i6 Vulcan_I(Object[] var0) {
      int var3 = (Integer)var0[0];
      long var4 = (Long)var0[1];
      int var1 = (Integer)var0[2];
      Locale var2 = (Locale)var0[3];
      var4 ^= a;
      long var6 = var4 ^ 86161567781050L;
      return Vulcan_E(new Object[]{new Integer(var3), new Integer(var1), new Long(var6), null, var2});
   }

   public static Vulcan_i6 Vulcan_F(Object[] var0) {
      int var3 = (Integer)var0[0];
      int var2 = (Integer)var0[1];
      TimeZone var1 = (TimeZone)var0[2];
      long var4 = (Long)var0[3];
      var4 ^= a;
      long var6 = var4 ^ 24261880045137L;
      return Vulcan_E(new Object[]{new Integer(var3), new Integer(var2), new Long(var6), var1, null});
   }

   public static synchronized Vulcan_i6 Vulcan_E(Object[] var0) {
      int var6 = (Integer)var0[0];
      int var4 = (Integer)var0[1];
      long var1 = (Long)var0[2];
      TimeZone var3 = (TimeZone)var0[3];
      Locale var5 = (Locale)var0[4];
      var1 ^= a;
      long var7 = var1 ^ 53949298182438L;
      String[] var10000 = Vulcan_x();
      Vulcan_eM var10 = new Vulcan_eM(new Integer(var6), new Integer(var4));
      String[] var9 = var10000;
      if (var3 != null) {
         var10 = new Vulcan_eM(var10, var3);
      }

      Object var17;
      label45: {
         label44: {
            try {
               var17 = var5;
               if (var9 != null) {
                  break label45;
               }

               if (var5 != null) {
                  break label44;
               }
            } catch (ClassCastException var16) {
               throw a(var16);
            }

            var5 = Locale.getDefault();
         }

         var10 = new Vulcan_eM(var10, var5);
         var17 = Vulcan_t.get(var10);
      }

      Vulcan_i6 var11 = (Vulcan_i6)var17;

      Vulcan_i6 var18;
      label34: {
         try {
            var18 = var11;
            if (var9 != null) {
               return var18;
            }

            if (var11 != null) {
               break label34;
            }
         } catch (ClassCastException var15) {
            throw a(var15);
         }

         try {
            SimpleDateFormat var12 = (SimpleDateFormat)DateFormat.getDateTimeInstance(var6, var4, var5);
            String var13 = var12.toPattern();
            var11 = Vulcan_V(new Object[]{new Long(var7), var13, var3, var5});
            Vulcan_t.put(var10, var11);
         } catch (ClassCastException var14) {
            throw new IllegalArgumentException(b[2] + var5);
         }
      }

      var18 = var11;
      return var18;
   }

   static synchronized String Vulcan_S(Object[] var0) {
      TimeZone var6 = (TimeZone)var0[0];
      long var1 = (Long)var0[1];
      boolean var3 = (Boolean)var0[2];
      int var5 = (Integer)var0[3];
      Locale var4 = (Locale)var0[4];
      long var10000 = a ^ var1;
      Vulcan_bI var7 = new Vulcan_bI(var6, var3, var5, var4);
      String var8 = (String)Vulcan_v.get(var7);
      if (var8 == null) {
         var8 = var6.getDisplayName(var3, var5, var4);
         Vulcan_v.put(var7, var8);
      }

      return var8;
   }

   private static synchronized String Vulcan_e(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   protected Vulcan_i6(String var1, TimeZone var2, Locale var3) {
      long var4 = a ^ 8683395281367L;
      super();
      if (var1 == null) {
         throw new IllegalArgumentException(b[4]);
      } else {
         Vulcan_i6 var10000;
         boolean var10001;
         label39: {
            try {
               this.Vulcan_Y = var1;
               var10000 = this;
               if (var2 != null) {
                  var10001 = true;
                  break label39;
               }
            } catch (ClassCastException var7) {
               throw a(var7);
            }

            var10001 = false;
         }

         var10000.Vulcan_T = var10001;
         if (var2 == null) {
            var2 = TimeZone.getDefault();
         }

         label31: {
            try {
               this.Vulcan_P = var2;
               var10000 = this;
               if (var3 != null) {
                  var10001 = true;
                  break label31;
               }
            } catch (ClassCastException var6) {
               throw a(var6);
            }

            var10001 = false;
         }

         var10000.Vulcan_F = var10001;
         if (var3 == null) {
            var3 = Locale.getDefault();
         }

         this.Vulcan_X = var3;
      }
   }

   protected void Vulcan_w(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 127500619625024L;
      long var6 = var2 ^ 76464590395710L;
      List var8 = this.Vulcan_N(new Object[]{new Long(var6)});
      this.Vulcan_A = (Vulcan_e3[])((Vulcan_e3[])var8.toArray(new Vulcan_e3[var8.size()]));
      int var9 = 0;
      int var10 = this.Vulcan_A.length;

      while(true) {
         --var10;
         if (var10 >= 0) {
            var9 += this.Vulcan_A[var10].Vulcan_p(new Object[]{new Long(var4)});
            if (var2 < 0L) {
               break;
            }

            if (var2 >= 0L) {
               continue;
            }
         }

         this.Vulcan_m = var9;
         break;
      }

   }

   protected List Vulcan_N(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 24987601189306L;
      long var6 = var2 ^ 53060778249083L;
      DateFormatSymbols var8 = new DateFormatSymbols(this.Vulcan_X);
      ArrayList var9 = new ArrayList();
      String[] var10 = var8.getEras();
      String[] var11 = var8.getMonths();
      String[] var12 = var8.getShortMonths();
      String[] var13 = var8.getWeekdays();
      String[] var14 = var8.getShortWeekdays();
      String[] var15 = var8.getAmPmStrings();
      int var16 = this.Vulcan_Y.length();
      int[] var17 = new int[1];
      int var18 = 0;

      while(var18 < var16) {
         var17[0] = var18;
         String var19 = this.Vulcan_u(new Object[]{this.Vulcan_Y, var17, new Long(var4)});
         var18 = var17[0];
         int var20 = var19.length();

         int var10000;
         label167: {
            try {
               var10000 = var20;
               if (var2 < 0L) {
                  break label167;
               }

               if (var20 == 0) {
                  break;
               }
            } catch (ClassCastException var25) {
               throw a(var25);
            }

            var10000 = var19.charAt(0);
         }

         Object var21;
         label176: {
            label177: {
               label178: {
                  label158: {
                     label157: {
                        label156: {
                           label155: {
                              label154: {
                                 label153: {
                                    int var22 = var10000;
                                    switch(var22) {
                                    case 39:
                                       break label177;
                                    case 40:
                                    case 41:
                                    case 42:
                                    case 43:
                                    case 44:
                                    case 45:
                                    case 46:
                                    case 47:
                                    case 48:
                                    case 49:
                                    case 50:
                                    case 51:
                                    case 52:
                                    case 53:
                                    case 54:
                                    case 55:
                                    case 56:
                                    case 57:
                                    case 58:
                                    case 59:
                                    case 60:
                                    case 61:
                                    case 62:
                                    case 63:
                                    case 64:
                                    case 65:
                                    case 66:
                                    case 67:
                                    case 73:
                                    case 74:
                                    case 76:
                                    case 78:
                                    case 79:
                                    case 80:
                                    case 81:
                                    case 82:
                                    case 84:
                                    case 85:
                                    case 86:
                                    case 88:
                                    case 89:
                                    case 91:
                                    case 92:
                                    case 93:
                                    case 94:
                                    case 95:
                                    case 96:
                                    case 98:
                                    case 99:
                                    case 101:
                                    case 102:
                                    case 103:
                                    case 105:
                                    case 106:
                                    case 108:
                                    case 110:
                                    case 111:
                                    case 112:
                                    case 113:
                                    case 114:
                                    case 116:
                                    case 117:
                                    case 118:
                                    case 120:
                                    default:
                                       throw new IllegalArgumentException(b[5] + var19);
                                    case 70:
                                       break;
                                    case 71:
                                       var21 = new Vulcan_ef(0, var10);
                                       if (var2 > 0L) {
                                          break label176;
                                       }
                                    case 121:
                                       if (var20 >= 4) {
                                          var21 = this.Vulcan_P(new Object[]{new Integer(1), new Long(var6), new Integer(var20)});
                                          if (var2 >= 0L) {
                                             break label176;
                                          }
                                       }

                                       var21 = Vulcan_iQ.Vulcan_k;
                                       if (var2 >= 0L) {
                                          break label176;
                                       }
                                    case 77:
                                       var10000 = var20;
                                       byte var10001 = 4;
                                       if (var2 >= 0L) {
                                          if (var20 >= 4) {
                                             var21 = new Vulcan_ef(2, var11);
                                             if (var2 >= 0L) {
                                                break label176;
                                             }
                                          }

                                          var10000 = var20;
                                          var10001 = 3;
                                       }

                                       if (var2 > 0L) {
                                          if (var10000 == var10001) {
                                             var21 = new Vulcan_ef(2, var12);
                                             if (var2 >= 0L) {
                                                break label176;
                                             }
                                          }

                                          var10000 = var20;
                                          var10001 = 2;
                                       }

                                       if (var10000 == var10001) {
                                          var21 = Vulcan_M.Vulcan_E;
                                          if (var2 >= 0L) {
                                             break label176;
                                          }
                                       }

                                       var21 = Vulcan_eq.Vulcan_k;
                                       if (var2 > 0L) {
                                          break label176;
                                       }
                                    case 100:
                                       var21 = this.Vulcan_P(new Object[]{new Integer(5), new Long(var6), new Integer(var20)});
                                       if (var2 > 0L) {
                                          break label176;
                                       }
                                    case 104:
                                       var21 = new Vulcan_P(this.Vulcan_P(new Object[]{new Integer(10), new Long(var6), new Integer(var20)}));
                                       if (var2 > 0L) {
                                          break label176;
                                       }
                                    case 72:
                                       var21 = this.Vulcan_P(new Object[]{new Integer(11), new Long(var6), new Integer(var20)});
                                       if (var2 > 0L) {
                                          break label176;
                                       }
                                    case 109:
                                       var21 = this.Vulcan_P(new Object[]{new Integer(12), new Long(var6), new Integer(var20)});
                                       if (var2 >= 0L) {
                                          break label176;
                                       }
                                    case 115:
                                       var21 = this.Vulcan_P(new Object[]{new Integer(13), new Long(var6), new Integer(var20)});
                                       if (var2 > 0L) {
                                          break label176;
                                       }
                                    case 83:
                                       var21 = this.Vulcan_P(new Object[]{new Integer(14), new Long(var6), new Integer(var20)});
                                       if (var2 >= 0L) {
                                          break label176;
                                       }
                                    case 69:
                                       byte var10002;
                                       String[] var10003;
                                       Vulcan_ef var26;
                                       Vulcan_ef var27;
                                       label130: {
                                          try {
                                             var26 = new Vulcan_ef;
                                             var27 = var26;
                                             var10002 = 7;
                                             if (var20 < 4) {
                                                var10003 = var14;
                                                break label130;
                                             }
                                          } catch (ClassCastException var24) {
                                             throw a(var24);
                                          }

                                          var10003 = var13;
                                       }

                                       var27.<init>(var10002, var10003);
                                       var21 = var26;
                                       if (var2 > 0L) {
                                          break label176;
                                       }
                                    case 68:
                                       var21 = this.Vulcan_P(new Object[]{new Integer(6), new Long(var6), new Integer(var20)});
                                       if (var2 >= 0L) {
                                          break label176;
                                       }
                                       break;
                                    case 75:
                                       break label157;
                                    case 87:
                                       break label154;
                                    case 90:
                                       break label178;
                                    case 97:
                                       break label155;
                                    case 107:
                                       break label156;
                                    case 119:
                                       break label153;
                                    case 122:
                                       break label158;
                                    }

                                    var21 = this.Vulcan_P(new Object[]{new Integer(8), new Long(var6), new Integer(var20)});
                                    if (var2 > 0L) {
                                       break label176;
                                    }
                                 }

                                 var21 = this.Vulcan_P(new Object[]{new Integer(3), new Long(var6), new Integer(var20)});
                                 if (var2 > 0L) {
                                    break label176;
                                 }
                              }

                              var21 = this.Vulcan_P(new Object[]{new Integer(4), new Long(var6), new Integer(var20)});
                              if (var2 >= 0L) {
                                 break label176;
                              }
                           }

                           var21 = new Vulcan_ef(9, var15);
                           if (var2 > 0L) {
                              break label176;
                           }
                        }

                        var21 = new Vulcan_r(this.Vulcan_P(new Object[]{new Integer(11), new Long(var6), new Integer(var20)}));
                        if (var2 > 0L) {
                           break label176;
                        }
                     }

                     var21 = this.Vulcan_P(new Object[]{new Integer(10), new Long(var6), new Integer(var20)});
                     if (var2 >= 0L) {
                        break label176;
                     }
                  }

                  if (var20 >= 4) {
                     var21 = new Vulcan_F(this.Vulcan_P, this.Vulcan_T, this.Vulcan_X, 1);
                     if (var2 >= 0L) {
                        break label176;
                     }
                  }

                  var21 = new Vulcan_F(this.Vulcan_P, this.Vulcan_T, this.Vulcan_X, 0);
                  if (var2 > 0L) {
                     break label176;
                  }
               }

               if (var20 == 1) {
                  var21 = Vulcan_j.Vulcan_q;
                  if (var2 > 0L) {
                     break label176;
                  }
               }

               var21 = Vulcan_j.Vulcan_g;
               if (var2 > 0L) {
                  break label176;
               }
            }

            String var23 = var19.substring(1);
            if (var23.length() == 1) {
               var21 = new Vulcan_i0(var23.charAt(0));
               if (var2 >= 0L) {
                  break label176;
               }
            }

            var21 = new Vulcan_X(var23);
            if (var2 <= 0L) {
               throw new IllegalArgumentException(b[5] + var19);
            }
         }

         var9.add(var21);
         ++var18;
         if (var2 < 0L) {
            break;
         }
      }

      return var9;
   }

   protected String Vulcan_u(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   protected Vulcan_iM Vulcan_P(Object[] var1) {
      int var5 = (Integer)var1[0];
      long var3 = (Long)var1[1];
      int var2 = (Integer)var1[2];
      long var10000 = a ^ var3;

      try {
         switch(var2) {
         case 1:
            return new Vulcan_i(var5);
         case 2:
            return new Vulcan_Xk(var5);
         }
      } catch (ClassCastException var6) {
         throw a(var6);
      }
   }

   public StringBuffer format(Object param1, StringBuffer param2, FieldPosition param3) {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_R(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = (Long)var1[1];
      var4 ^= a;
      long var6 = var4 ^ 17219837323969L;
      return this.Vulcan_K(new Object[]{new Date(var2), new Long(var6)});
   }

   public String Vulcan_K(Object[] var1) {
      Date var2 = (Date)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 28724464844252L;
      GregorianCalendar var7 = new GregorianCalendar(this.Vulcan_P, this.Vulcan_X);
      var7.setTime(var2);
      return this.Vulcan_E(new Object[]{var7, new StringBuffer(this.Vulcan_m), new Long(var5)}).toString();
   }

   public String Vulcan_g(Object[] var1) {
      long var2 = (Long)var1[0];
      Calendar var4 = (Calendar)var1[1];
      var2 ^= a;
      long var5 = var2 ^ 72583302559159L;
      return this.Vulcan_q(new Object[]{var4, new Long(var5), new StringBuffer(this.Vulcan_m)}).toString();
   }

   public StringBuffer Vulcan_b(Object[] var1) {
      long var3 = (Long)var1[0];
      long var5 = (Long)var1[1];
      StringBuffer var2 = (StringBuffer)var1[2];
      var5 ^= a;
      long var7 = var5 ^ 53587304293786L;
      return this.Vulcan_A(new Object[]{new Date(var3), new Long(var7), var2});
   }

   public StringBuffer Vulcan_A(Object[] var1) {
      Date var2 = (Date)var1[0];
      long var3 = (Long)var1[1];
      StringBuffer var5 = (StringBuffer)var1[2];
      var3 ^= a;
      long var6 = var3 ^ 93004610672170L;
      GregorianCalendar var8 = new GregorianCalendar(this.Vulcan_P);
      var8.setTime(var2);
      return this.Vulcan_E(new Object[]{var8, var5, new Long(var6)});
   }

   public StringBuffer Vulcan_q(Object[] var1) {
      Calendar var3 = (Calendar)var1[0];
      long var4 = (Long)var1[1];
      StringBuffer var2 = (StringBuffer)var1[2];
      var4 ^= a;
      long var6 = var4 ^ 29678843813524L;
      String[] var8 = Vulcan_x();

      Vulcan_i6 var10000;
      label20: {
         try {
            var10000 = this;
            if (var8 != null) {
               return var10000.Vulcan_E(new Object[]{var3, var2, new Long(var6)});
            }

            if (!this.Vulcan_T) {
               break label20;
            }
         } catch (ClassCastException var9) {
            throw a(var9);
         }

         var3.getTime();
         var3 = (Calendar)var3.clone();
         var3.setTimeZone(this.Vulcan_P);
      }

      var10000 = this;
      return var10000.Vulcan_E(new Object[]{var3, var2, new Long(var6)});
   }

   protected StringBuffer Vulcan_E(Object[] var1) {
      Calendar var3 = (Calendar)var1[0];
      StringBuffer var2 = (StringBuffer)var1[1];
      long var4 = (Long)var1[2];
      var4 ^= a;
      long var6 = var4 ^ 26586024094755L;
      String[] var10000 = Vulcan_x();
      Vulcan_e3[] var9 = this.Vulcan_A;
      String[] var8 = var10000;
      int var10 = this.Vulcan_A.length;
      int var11 = 0;

      label21:
      while(true) {
         if (var11 < var10) {
            var9[var11].Vulcan_e(new Object[]{var2, var3, new Long(var6)});
            ++var11;
            if (var8 == null) {
               continue;
            }
         }

         while(var4 <= 0L) {
            if (var8 == null) {
               continue label21;
            }
         }

         return var2;
      }
   }

   public Object parseObject(String var1, ParsePosition var2) {
      var2.setIndex(0);
      var2.setErrorIndex(0);
      return null;
   }

   public String Vulcan_y(Object[] var1) {
      return this.Vulcan_Y;
   }

   public TimeZone Vulcan_N(Object[] var1) {
      return this.Vulcan_P;
   }

   public boolean Vulcan_D(Object[] var1) {
      return this.Vulcan_T;
   }

   public Locale Vulcan_p(Object[] var1) {
      return this.Vulcan_X;
   }

   public int Vulcan_d(Object[] var1) {
      return this.Vulcan_m;
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      long var1 = a ^ 63316563339774L;
      String[] var10000 = Vulcan_x();
      byte var4 = 0;
      int var7 = var4 + this.Vulcan_Y.hashCode();
      String[] var3 = var10000;
      var7 += this.Vulcan_P.hashCode();

      byte var10001;
      int var8;
      label41: {
         label40: {
            try {
               var8 = var7;
               var10001 = this.Vulcan_T;
               if (var3 != null) {
                  break label41;
               }

               if (var10001 != 0) {
                  break label40;
               }
            } catch (ClassCastException var6) {
               throw a(var6);
            }

            var10001 = 0;
            break label41;
         }

         var10001 = 1;
      }

      var7 = var8 + var10001;
      var7 += this.Vulcan_X.hashCode();

      label31: {
         label30: {
            try {
               var8 = var7;
               var10001 = this.Vulcan_F;
               if (var3 != null) {
                  break label31;
               }

               if (var10001 == 0) {
                  break label30;
               }
            } catch (ClassCastException var5) {
               throw a(var5);
            }

            var10001 = 1;
            break label31;
         }

         var10001 = 0;
      }

      var7 = var8 + var10001;
      return var7;
   }

   public String toString() {
      return b[6] + this.Vulcan_Y + "]";
   }

   private void readObject(ObjectInputStream var1, long var2) {
      var2 ^= a;
      long var4 = var2 ^ 58946256435781L;
      var1.defaultReadObject();
      this.Vulcan_w(new Object[]{new Long(var4)});
   }

   static {
      String[] var5 = new String[8];
      Vulcan_J((String[])null);
      int var3 = 0;
      String var2 = "gOAy{\u0005q\u0012BFvg\u0001%\u0012\u0006\u000eO_{xL!|N\nsu\u0006z\u0012UCzqRoSU^rf\u001c?TNX7x\u001d|SMO-4\u001c|N\nsu\u0006z\u0012QKc`\u0017m\\\u0001LxfRs]BK{qH?\u001cfIO7d\u0013kFDXy4\u001fjAU\ny{\u0006?PD\nya\u001es\u001b{MFrs\u0013s\u0012QKc`\u0017m\\\u0001Ixy\u0002p\\DDc.R";
      int var4 = "gOAy{\u0005q\u0012BFvg\u0001%\u0012\u0006\u000eO_{xL!|N\nsu\u0006z\u0012UCzqRoSU^rf\u001c?TNX7x\u001d|SMO-4\u001c|N\nsu\u0006z\u0012QKc`\u0017m\\\u0001LxfRs]BK{qH?\u001cfIO7d\u0013kFDXy4\u001fjAU\ny{\u0006?PD\nya\u001es\u001b{MFrs\u0013s\u0012QKc`\u0017m\\\u0001Ixy\u0002p\\DDc.R".length();
      char var1 = 15;
      int var0 = -1;

      while(true) {
         int var7;
         int var8;
         byte var10;
         byte var12;
         char[] var10001;
         char[] var10002;
         int var10003;
         char[] var10004;
         int var10005;
         char var22;
         byte var23;
         label83: {
            ++var0;
            var10002 = var2.substring(var0, var0 + var1).toCharArray();
            var10003 = var10002.length;
            var7 = 0;
            var10 = 103;
            var10001 = var10002;
            var8 = var10003;
            if (var10003 <= 1) {
               var10004 = var10002;
               var12 = 103;
               var10005 = var7;
            } else {
               var10 = 103;
               var8 = var10003;
               if (var10003 <= var7) {
                  break label83;
               }

               var10004 = var10002;
               var12 = 103;
               var10005 = var7;
            }

            while(true) {
               var22 = var10004[var10005];
               switch(var7 % 7) {
               case 0:
                  var23 = 85;
                  break;
               case 1:
                  var23 = 70;
                  break;
               case 2:
                  var23 = 77;
                  break;
               case 3:
                  var23 = 112;
                  break;
               case 4:
                  var23 = 115;
                  break;
               case 5:
                  var23 = 21;
                  break;
               default:
                  var23 = 120;
               }

               var10004[var10005] = (char)(var22 ^ var12 ^ var23);
               ++var7;
               if (var10 == 0) {
                  var10005 = var10;
                  var10004 = var10001;
                  var12 = var10;
               } else {
                  if (var8 <= var7) {
                     break;
                  }

                  var10004 = var10001;
                  var12 = var10;
                  var10005 = var7;
               }
            }
         }

         var5[var3++] = (new String(var10001)).intern();
         if ((var0 += var1) >= var4) {
            var2 = "YmtN}>FzJhHT>FD\u001cQc'^X+W?|fNM:@q,aUK\u007f^pofV\\e\u0012";
            var4 = "YmtN}>FzJhHT>FD\u001cQc'^X+W?|fNM:@q,aUK\u007f^pofV\\e\u0012".length();
            var1 = 15;
            var0 = -1;

            while(true) {
               label62: {
                  ++var0;
                  var10002 = var2.substring(var0, var0 + var1).toCharArray();
                  var10003 = var10002.length;
                  var7 = 0;
                  var10 = 74;
                  var10001 = var10002;
                  var8 = var10003;
                  if (var10003 <= 1) {
                     var10004 = var10002;
                     var12 = 74;
                     var10005 = var7;
                  } else {
                     var10 = 74;
                     var8 = var10003;
                     if (var10003 <= var7) {
                        break label62;
                     }

                     var10004 = var10002;
                     var12 = 74;
                     var10005 = var7;
                  }

                  while(true) {
                     var22 = var10004[var10005];
                     switch(var7 % 7) {
                     case 0:
                        var23 = 85;
                        break;
                     case 1:
                        var23 = 70;
                        break;
                     case 2:
                        var23 = 77;
                        break;
                     case 3:
                        var23 = 112;
                        break;
                     case 4:
                        var23 = 115;
                        break;
                     case 5:
                        var23 = 21;
                        break;
                     default:
                        var23 = 120;
                     }

                     var10004[var10005] = (char)(var22 ^ var12 ^ var23);
                     ++var7;
                     if (var10 == 0) {
                        var10005 = var10;
                        var10004 = var10001;
                        var12 = var10;
                     } else {
                        if (var8 <= var7) {
                           break;
                        }

                        var10004 = var10001;
                        var12 = var10;
                        var10005 = var7;
                     }
                  }
               }

               var5[var3++] = (new String(var10001)).intern();
               if ((var0 += var1) >= var4) {
                  b = var5;
                  Vulcan_i = new HashMap(7);
                  Vulcan_f = new HashMap(7);
                  Vulcan_b = new HashMap(7);
                  Vulcan_t = new HashMap(7);
                  Vulcan_v = new HashMap(7);
                  return;
               }

               var1 = var2.charAt(var0);
            }
         }

         var1 = var2.charAt(var0);
      }
   }

   public static void Vulcan_J(String[] var0) {
      Vulcan_D = var0;
   }

   public static String[] Vulcan_x() {
      return Vulcan_D;
   }

   private static ClassCastException a(ClassCastException var0) {
      return var0;
   }
}
