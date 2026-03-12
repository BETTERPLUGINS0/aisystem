package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math.ed25519;

import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.Utils;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math.Field;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math.FieldElement;
import java.util.Arrays;

public class Ed25519FieldElement extends FieldElement {
   private static final long serialVersionUID = -2455098303824960263L;
   private static final byte[] ZERO = new byte[32];
   final int[] t;

   public Ed25519FieldElement(Field f, int[] t) {
      super(f);
      if (t.length != 10) {
         throw new IllegalArgumentException("Invalid radix-2^51 representation");
      } else {
         this.t = t;
      }
   }

   public boolean isNonZero() {
      byte[] s = this.toByteArray();
      return Utils.equal(s, ZERO) == 0;
   }

   public FieldElement add(FieldElement val) {
      int[] g = ((Ed25519FieldElement)val).t;
      int[] h = new int[10];

      for(int i = 0; i < 10; ++i) {
         h[i] = this.t[i] + g[i];
      }

      return new Ed25519FieldElement(this.f, h);
   }

   public FieldElement subtract(FieldElement val) {
      int[] g = ((Ed25519FieldElement)val).t;
      int[] h = new int[10];

      for(int i = 0; i < 10; ++i) {
         h[i] = this.t[i] - g[i];
      }

      return new Ed25519FieldElement(this.f, h);
   }

   public FieldElement negate() {
      int[] h = new int[10];

      for(int i = 0; i < 10; ++i) {
         h[i] = -this.t[i];
      }

      return new Ed25519FieldElement(this.f, h);
   }

   public FieldElement multiply(FieldElement val) {
      int[] g = ((Ed25519FieldElement)val).t;
      int g1_19 = 19 * g[1];
      int g2_19 = 19 * g[2];
      int g3_19 = 19 * g[3];
      int g4_19 = 19 * g[4];
      int g5_19 = 19 * g[5];
      int g6_19 = 19 * g[6];
      int g7_19 = 19 * g[7];
      int g8_19 = 19 * g[8];
      int g9_19 = 19 * g[9];
      int f1_2 = 2 * this.t[1];
      int f3_2 = 2 * this.t[3];
      int f5_2 = 2 * this.t[5];
      int f7_2 = 2 * this.t[7];
      int f9_2 = 2 * this.t[9];
      long f0g0 = (long)this.t[0] * (long)g[0];
      long f0g1 = (long)this.t[0] * (long)g[1];
      long f0g2 = (long)this.t[0] * (long)g[2];
      long f0g3 = (long)this.t[0] * (long)g[3];
      long f0g4 = (long)this.t[0] * (long)g[4];
      long f0g5 = (long)this.t[0] * (long)g[5];
      long f0g6 = (long)this.t[0] * (long)g[6];
      long f0g7 = (long)this.t[0] * (long)g[7];
      long f0g8 = (long)this.t[0] * (long)g[8];
      long f0g9 = (long)this.t[0] * (long)g[9];
      long f1g0 = (long)this.t[1] * (long)g[0];
      long f1g1_2 = (long)f1_2 * (long)g[1];
      long f1g2 = (long)this.t[1] * (long)g[2];
      long f1g3_2 = (long)f1_2 * (long)g[3];
      long f1g4 = (long)this.t[1] * (long)g[4];
      long f1g5_2 = (long)f1_2 * (long)g[5];
      long f1g6 = (long)this.t[1] * (long)g[6];
      long f1g7_2 = (long)f1_2 * (long)g[7];
      long f1g8 = (long)this.t[1] * (long)g[8];
      long f1g9_38 = (long)f1_2 * (long)g9_19;
      long f2g0 = (long)this.t[2] * (long)g[0];
      long f2g1 = (long)this.t[2] * (long)g[1];
      long f2g2 = (long)this.t[2] * (long)g[2];
      long f2g3 = (long)this.t[2] * (long)g[3];
      long f2g4 = (long)this.t[2] * (long)g[4];
      long f2g5 = (long)this.t[2] * (long)g[5];
      long f2g6 = (long)this.t[2] * (long)g[6];
      long f2g7 = (long)this.t[2] * (long)g[7];
      long f2g8_19 = (long)this.t[2] * (long)g8_19;
      long f2g9_19 = (long)this.t[2] * (long)g9_19;
      long f3g0 = (long)this.t[3] * (long)g[0];
      long f3g1_2 = (long)f3_2 * (long)g[1];
      long f3g2 = (long)this.t[3] * (long)g[2];
      long f3g3_2 = (long)f3_2 * (long)g[3];
      long f3g4 = (long)this.t[3] * (long)g[4];
      long f3g5_2 = (long)f3_2 * (long)g[5];
      long f3g6 = (long)this.t[3] * (long)g[6];
      long f3g7_38 = (long)f3_2 * (long)g7_19;
      long f3g8_19 = (long)this.t[3] * (long)g8_19;
      long f3g9_38 = (long)f3_2 * (long)g9_19;
      long f4g0 = (long)this.t[4] * (long)g[0];
      long f4g1 = (long)this.t[4] * (long)g[1];
      long f4g2 = (long)this.t[4] * (long)g[2];
      long f4g3 = (long)this.t[4] * (long)g[3];
      long f4g4 = (long)this.t[4] * (long)g[4];
      long f4g5 = (long)this.t[4] * (long)g[5];
      long f4g6_19 = (long)this.t[4] * (long)g6_19;
      long f4g7_19 = (long)this.t[4] * (long)g7_19;
      long f4g8_19 = (long)this.t[4] * (long)g8_19;
      long f4g9_19 = (long)this.t[4] * (long)g9_19;
      long f5g0 = (long)this.t[5] * (long)g[0];
      long f5g1_2 = (long)f5_2 * (long)g[1];
      long f5g2 = (long)this.t[5] * (long)g[2];
      long f5g3_2 = (long)f5_2 * (long)g[3];
      long f5g4 = (long)this.t[5] * (long)g[4];
      long f5g5_38 = (long)f5_2 * (long)g5_19;
      long f5g6_19 = (long)this.t[5] * (long)g6_19;
      long f5g7_38 = (long)f5_2 * (long)g7_19;
      long f5g8_19 = (long)this.t[5] * (long)g8_19;
      long f5g9_38 = (long)f5_2 * (long)g9_19;
      long f6g0 = (long)this.t[6] * (long)g[0];
      long f6g1 = (long)this.t[6] * (long)g[1];
      long f6g2 = (long)this.t[6] * (long)g[2];
      long f6g3 = (long)this.t[6] * (long)g[3];
      long f6g4_19 = (long)this.t[6] * (long)g4_19;
      long f6g5_19 = (long)this.t[6] * (long)g5_19;
      long f6g6_19 = (long)this.t[6] * (long)g6_19;
      long f6g7_19 = (long)this.t[6] * (long)g7_19;
      long f6g8_19 = (long)this.t[6] * (long)g8_19;
      long f6g9_19 = (long)this.t[6] * (long)g9_19;
      long f7g0 = (long)this.t[7] * (long)g[0];
      long f7g1_2 = (long)f7_2 * (long)g[1];
      long f7g2 = (long)this.t[7] * (long)g[2];
      long f7g3_38 = (long)f7_2 * (long)g3_19;
      long f7g4_19 = (long)this.t[7] * (long)g4_19;
      long f7g5_38 = (long)f7_2 * (long)g5_19;
      long f7g6_19 = (long)this.t[7] * (long)g6_19;
      long f7g7_38 = (long)f7_2 * (long)g7_19;
      long f7g8_19 = (long)this.t[7] * (long)g8_19;
      long f7g9_38 = (long)f7_2 * (long)g9_19;
      long f8g0 = (long)this.t[8] * (long)g[0];
      long f8g1 = (long)this.t[8] * (long)g[1];
      long f8g2_19 = (long)this.t[8] * (long)g2_19;
      long f8g3_19 = (long)this.t[8] * (long)g3_19;
      long f8g4_19 = (long)this.t[8] * (long)g4_19;
      long f8g5_19 = (long)this.t[8] * (long)g5_19;
      long f8g6_19 = (long)this.t[8] * (long)g6_19;
      long f8g7_19 = (long)this.t[8] * (long)g7_19;
      long f8g8_19 = (long)this.t[8] * (long)g8_19;
      long f8g9_19 = (long)this.t[8] * (long)g9_19;
      long f9g0 = (long)this.t[9] * (long)g[0];
      long f9g1_38 = (long)f9_2 * (long)g1_19;
      long f9g2_19 = (long)this.t[9] * (long)g2_19;
      long f9g3_38 = (long)f9_2 * (long)g3_19;
      long f9g4_19 = (long)this.t[9] * (long)g4_19;
      long f9g5_38 = (long)f9_2 * (long)g5_19;
      long f9g6_19 = (long)this.t[9] * (long)g6_19;
      long f9g7_38 = (long)f9_2 * (long)g7_19;
      long f9g8_19 = (long)this.t[9] * (long)g8_19;
      long f9g9_38 = (long)f9_2 * (long)g9_19;
      long h0 = f0g0 + f1g9_38 + f2g8_19 + f3g7_38 + f4g6_19 + f5g5_38 + f6g4_19 + f7g3_38 + f8g2_19 + f9g1_38;
      long h1 = f0g1 + f1g0 + f2g9_19 + f3g8_19 + f4g7_19 + f5g6_19 + f6g5_19 + f7g4_19 + f8g3_19 + f9g2_19;
      long h2 = f0g2 + f1g1_2 + f2g0 + f3g9_38 + f4g8_19 + f5g7_38 + f6g6_19 + f7g5_38 + f8g4_19 + f9g3_38;
      long h3 = f0g3 + f1g2 + f2g1 + f3g0 + f4g9_19 + f5g8_19 + f6g7_19 + f7g6_19 + f8g5_19 + f9g4_19;
      long h4 = f0g4 + f1g3_2 + f2g2 + f3g1_2 + f4g0 + f5g9_38 + f6g8_19 + f7g7_38 + f8g6_19 + f9g5_38;
      long h5 = f0g5 + f1g4 + f2g3 + f3g2 + f4g1 + f5g0 + f6g9_19 + f7g8_19 + f8g7_19 + f9g6_19;
      long h6 = f0g6 + f1g5_2 + f2g4 + f3g3_2 + f4g2 + f5g1_2 + f6g0 + f7g9_38 + f8g8_19 + f9g7_38;
      long h7 = f0g7 + f1g6 + f2g5 + f3g4 + f4g3 + f5g2 + f6g1 + f7g0 + f8g9_19 + f9g8_19;
      long h8 = f0g8 + f1g7_2 + f2g6 + f3g5_2 + f4g4 + f5g3_2 + f6g2 + f7g1_2 + f8g0 + f9g9_38;
      long h9 = f0g9 + f1g8 + f2g7 + f3g6 + f4g5 + f5g4 + f6g3 + f7g2 + f8g1 + f9g0;
      long carry0 = h0 + 33554432L >> 26;
      h1 += carry0;
      h0 -= carry0 << 26;
      long carry4 = h4 + 33554432L >> 26;
      h5 += carry4;
      h4 -= carry4 << 26;
      long carry1 = h1 + 16777216L >> 25;
      h2 += carry1;
      h1 -= carry1 << 25;
      long carry5 = h5 + 16777216L >> 25;
      h6 += carry5;
      h5 -= carry5 << 25;
      long carry2 = h2 + 33554432L >> 26;
      h3 += carry2;
      h2 -= carry2 << 26;
      long carry6 = h6 + 33554432L >> 26;
      h7 += carry6;
      h6 -= carry6 << 26;
      long carry3 = h3 + 16777216L >> 25;
      h4 += carry3;
      h3 -= carry3 << 25;
      long carry7 = h7 + 16777216L >> 25;
      h8 += carry7;
      h7 -= carry7 << 25;
      carry4 = h4 + 33554432L >> 26;
      h5 += carry4;
      h4 -= carry4 << 26;
      long carry8 = h8 + 33554432L >> 26;
      h9 += carry8;
      h8 -= carry8 << 26;
      long carry9 = h9 + 16777216L >> 25;
      h0 += carry9 * 19L;
      h9 -= carry9 << 25;
      carry0 = h0 + 33554432L >> 26;
      h1 += carry0;
      h0 -= carry0 << 26;
      int[] h = new int[]{(int)h0, (int)h1, (int)h2, (int)h3, (int)h4, (int)h5, (int)h6, (int)h7, (int)h8, (int)h9};
      return new Ed25519FieldElement(this.f, h);
   }

   public FieldElement square() {
      int f0 = this.t[0];
      int f1 = this.t[1];
      int f2 = this.t[2];
      int f3 = this.t[3];
      int f4 = this.t[4];
      int f5 = this.t[5];
      int f6 = this.t[6];
      int f7 = this.t[7];
      int f8 = this.t[8];
      int f9 = this.t[9];
      int f0_2 = 2 * f0;
      int f1_2 = 2 * f1;
      int f2_2 = 2 * f2;
      int f3_2 = 2 * f3;
      int f4_2 = 2 * f4;
      int f5_2 = 2 * f5;
      int f6_2 = 2 * f6;
      int f7_2 = 2 * f7;
      int f5_38 = 38 * f5;
      int f6_19 = 19 * f6;
      int f7_38 = 38 * f7;
      int f8_19 = 19 * f8;
      int f9_38 = 38 * f9;
      long f0f0 = (long)f0 * (long)f0;
      long f0f1_2 = (long)f0_2 * (long)f1;
      long f0f2_2 = (long)f0_2 * (long)f2;
      long f0f3_2 = (long)f0_2 * (long)f3;
      long f0f4_2 = (long)f0_2 * (long)f4;
      long f0f5_2 = (long)f0_2 * (long)f5;
      long f0f6_2 = (long)f0_2 * (long)f6;
      long f0f7_2 = (long)f0_2 * (long)f7;
      long f0f8_2 = (long)f0_2 * (long)f8;
      long f0f9_2 = (long)f0_2 * (long)f9;
      long f1f1_2 = (long)f1_2 * (long)f1;
      long f1f2_2 = (long)f1_2 * (long)f2;
      long f1f3_4 = (long)f1_2 * (long)f3_2;
      long f1f4_2 = (long)f1_2 * (long)f4;
      long f1f5_4 = (long)f1_2 * (long)f5_2;
      long f1f6_2 = (long)f1_2 * (long)f6;
      long f1f7_4 = (long)f1_2 * (long)f7_2;
      long f1f8_2 = (long)f1_2 * (long)f8;
      long f1f9_76 = (long)f1_2 * (long)f9_38;
      long f2f2 = (long)f2 * (long)f2;
      long f2f3_2 = (long)f2_2 * (long)f3;
      long f2f4_2 = (long)f2_2 * (long)f4;
      long f2f5_2 = (long)f2_2 * (long)f5;
      long f2f6_2 = (long)f2_2 * (long)f6;
      long f2f7_2 = (long)f2_2 * (long)f7;
      long f2f8_38 = (long)f2_2 * (long)f8_19;
      long f2f9_38 = (long)f2 * (long)f9_38;
      long f3f3_2 = (long)f3_2 * (long)f3;
      long f3f4_2 = (long)f3_2 * (long)f4;
      long f3f5_4 = (long)f3_2 * (long)f5_2;
      long f3f6_2 = (long)f3_2 * (long)f6;
      long f3f7_76 = (long)f3_2 * (long)f7_38;
      long f3f8_38 = (long)f3_2 * (long)f8_19;
      long f3f9_76 = (long)f3_2 * (long)f9_38;
      long f4f4 = (long)f4 * (long)f4;
      long f4f5_2 = (long)f4_2 * (long)f5;
      long f4f6_38 = (long)f4_2 * (long)f6_19;
      long f4f7_38 = (long)f4 * (long)f7_38;
      long f4f8_38 = (long)f4_2 * (long)f8_19;
      long f4f9_38 = (long)f4 * (long)f9_38;
      long f5f5_38 = (long)f5 * (long)f5_38;
      long f5f6_38 = (long)f5_2 * (long)f6_19;
      long f5f7_76 = (long)f5_2 * (long)f7_38;
      long f5f8_38 = (long)f5_2 * (long)f8_19;
      long f5f9_76 = (long)f5_2 * (long)f9_38;
      long f6f6_19 = (long)f6 * (long)f6_19;
      long f6f7_38 = (long)f6 * (long)f7_38;
      long f6f8_38 = (long)f6_2 * (long)f8_19;
      long f6f9_38 = (long)f6 * (long)f9_38;
      long f7f7_38 = (long)f7 * (long)f7_38;
      long f7f8_38 = (long)f7_2 * (long)f8_19;
      long f7f9_76 = (long)f7_2 * (long)f9_38;
      long f8f8_19 = (long)f8 * (long)f8_19;
      long f8f9_38 = (long)f8 * (long)f9_38;
      long f9f9_38 = (long)f9 * (long)f9_38;
      long h0 = f0f0 + f1f9_76 + f2f8_38 + f3f7_76 + f4f6_38 + f5f5_38;
      long h1 = f0f1_2 + f2f9_38 + f3f8_38 + f4f7_38 + f5f6_38;
      long h2 = f0f2_2 + f1f1_2 + f3f9_76 + f4f8_38 + f5f7_76 + f6f6_19;
      long h3 = f0f3_2 + f1f2_2 + f4f9_38 + f5f8_38 + f6f7_38;
      long h4 = f0f4_2 + f1f3_4 + f2f2 + f5f9_76 + f6f8_38 + f7f7_38;
      long h5 = f0f5_2 + f1f4_2 + f2f3_2 + f6f9_38 + f7f8_38;
      long h6 = f0f6_2 + f1f5_4 + f2f4_2 + f3f3_2 + f7f9_76 + f8f8_19;
      long h7 = f0f7_2 + f1f6_2 + f2f5_2 + f3f4_2 + f8f9_38;
      long h8 = f0f8_2 + f1f7_4 + f2f6_2 + f3f5_4 + f4f4 + f9f9_38;
      long h9 = f0f9_2 + f1f8_2 + f2f7_2 + f3f6_2 + f4f5_2;
      long carry0 = h0 + 33554432L >> 26;
      h1 += carry0;
      h0 -= carry0 << 26;
      long carry4 = h4 + 33554432L >> 26;
      h5 += carry4;
      h4 -= carry4 << 26;
      long carry1 = h1 + 16777216L >> 25;
      h2 += carry1;
      h1 -= carry1 << 25;
      long carry5 = h5 + 16777216L >> 25;
      h6 += carry5;
      h5 -= carry5 << 25;
      long carry2 = h2 + 33554432L >> 26;
      h3 += carry2;
      h2 -= carry2 << 26;
      long carry6 = h6 + 33554432L >> 26;
      h7 += carry6;
      h6 -= carry6 << 26;
      long carry3 = h3 + 16777216L >> 25;
      h4 += carry3;
      h3 -= carry3 << 25;
      long carry7 = h7 + 16777216L >> 25;
      h8 += carry7;
      h7 -= carry7 << 25;
      carry4 = h4 + 33554432L >> 26;
      h5 += carry4;
      h4 -= carry4 << 26;
      long carry8 = h8 + 33554432L >> 26;
      h9 += carry8;
      h8 -= carry8 << 26;
      long carry9 = h9 + 16777216L >> 25;
      h0 += carry9 * 19L;
      h9 -= carry9 << 25;
      carry0 = h0 + 33554432L >> 26;
      h1 += carry0;
      h0 -= carry0 << 26;
      int[] h = new int[]{(int)h0, (int)h1, (int)h2, (int)h3, (int)h4, (int)h5, (int)h6, (int)h7, (int)h8, (int)h9};
      return new Ed25519FieldElement(this.f, h);
   }

   public FieldElement squareAndDouble() {
      int f0 = this.t[0];
      int f1 = this.t[1];
      int f2 = this.t[2];
      int f3 = this.t[3];
      int f4 = this.t[4];
      int f5 = this.t[5];
      int f6 = this.t[6];
      int f7 = this.t[7];
      int f8 = this.t[8];
      int f9 = this.t[9];
      int f0_2 = 2 * f0;
      int f1_2 = 2 * f1;
      int f2_2 = 2 * f2;
      int f3_2 = 2 * f3;
      int f4_2 = 2 * f4;
      int f5_2 = 2 * f5;
      int f6_2 = 2 * f6;
      int f7_2 = 2 * f7;
      int f5_38 = 38 * f5;
      int f6_19 = 19 * f6;
      int f7_38 = 38 * f7;
      int f8_19 = 19 * f8;
      int f9_38 = 38 * f9;
      long f0f0 = (long)f0 * (long)f0;
      long f0f1_2 = (long)f0_2 * (long)f1;
      long f0f2_2 = (long)f0_2 * (long)f2;
      long f0f3_2 = (long)f0_2 * (long)f3;
      long f0f4_2 = (long)f0_2 * (long)f4;
      long f0f5_2 = (long)f0_2 * (long)f5;
      long f0f6_2 = (long)f0_2 * (long)f6;
      long f0f7_2 = (long)f0_2 * (long)f7;
      long f0f8_2 = (long)f0_2 * (long)f8;
      long f0f9_2 = (long)f0_2 * (long)f9;
      long f1f1_2 = (long)f1_2 * (long)f1;
      long f1f2_2 = (long)f1_2 * (long)f2;
      long f1f3_4 = (long)f1_2 * (long)f3_2;
      long f1f4_2 = (long)f1_2 * (long)f4;
      long f1f5_4 = (long)f1_2 * (long)f5_2;
      long f1f6_2 = (long)f1_2 * (long)f6;
      long f1f7_4 = (long)f1_2 * (long)f7_2;
      long f1f8_2 = (long)f1_2 * (long)f8;
      long f1f9_76 = (long)f1_2 * (long)f9_38;
      long f2f2 = (long)f2 * (long)f2;
      long f2f3_2 = (long)f2_2 * (long)f3;
      long f2f4_2 = (long)f2_2 * (long)f4;
      long f2f5_2 = (long)f2_2 * (long)f5;
      long f2f6_2 = (long)f2_2 * (long)f6;
      long f2f7_2 = (long)f2_2 * (long)f7;
      long f2f8_38 = (long)f2_2 * (long)f8_19;
      long f2f9_38 = (long)f2 * (long)f9_38;
      long f3f3_2 = (long)f3_2 * (long)f3;
      long f3f4_2 = (long)f3_2 * (long)f4;
      long f3f5_4 = (long)f3_2 * (long)f5_2;
      long f3f6_2 = (long)f3_2 * (long)f6;
      long f3f7_76 = (long)f3_2 * (long)f7_38;
      long f3f8_38 = (long)f3_2 * (long)f8_19;
      long f3f9_76 = (long)f3_2 * (long)f9_38;
      long f4f4 = (long)f4 * (long)f4;
      long f4f5_2 = (long)f4_2 * (long)f5;
      long f4f6_38 = (long)f4_2 * (long)f6_19;
      long f4f7_38 = (long)f4 * (long)f7_38;
      long f4f8_38 = (long)f4_2 * (long)f8_19;
      long f4f9_38 = (long)f4 * (long)f9_38;
      long f5f5_38 = (long)f5 * (long)f5_38;
      long f5f6_38 = (long)f5_2 * (long)f6_19;
      long f5f7_76 = (long)f5_2 * (long)f7_38;
      long f5f8_38 = (long)f5_2 * (long)f8_19;
      long f5f9_76 = (long)f5_2 * (long)f9_38;
      long f6f6_19 = (long)f6 * (long)f6_19;
      long f6f7_38 = (long)f6 * (long)f7_38;
      long f6f8_38 = (long)f6_2 * (long)f8_19;
      long f6f9_38 = (long)f6 * (long)f9_38;
      long f7f7_38 = (long)f7 * (long)f7_38;
      long f7f8_38 = (long)f7_2 * (long)f8_19;
      long f7f9_76 = (long)f7_2 * (long)f9_38;
      long f8f8_19 = (long)f8 * (long)f8_19;
      long f8f9_38 = (long)f8 * (long)f9_38;
      long f9f9_38 = (long)f9 * (long)f9_38;
      long h0 = f0f0 + f1f9_76 + f2f8_38 + f3f7_76 + f4f6_38 + f5f5_38;
      long h1 = f0f1_2 + f2f9_38 + f3f8_38 + f4f7_38 + f5f6_38;
      long h2 = f0f2_2 + f1f1_2 + f3f9_76 + f4f8_38 + f5f7_76 + f6f6_19;
      long h3 = f0f3_2 + f1f2_2 + f4f9_38 + f5f8_38 + f6f7_38;
      long h4 = f0f4_2 + f1f3_4 + f2f2 + f5f9_76 + f6f8_38 + f7f7_38;
      long h5 = f0f5_2 + f1f4_2 + f2f3_2 + f6f9_38 + f7f8_38;
      long h6 = f0f6_2 + f1f5_4 + f2f4_2 + f3f3_2 + f7f9_76 + f8f8_19;
      long h7 = f0f7_2 + f1f6_2 + f2f5_2 + f3f4_2 + f8f9_38;
      long h8 = f0f8_2 + f1f7_4 + f2f6_2 + f3f5_4 + f4f4 + f9f9_38;
      long h9 = f0f9_2 + f1f8_2 + f2f7_2 + f3f6_2 + f4f5_2;
      h0 += h0;
      h1 += h1;
      h2 += h2;
      h3 += h3;
      h4 += h4;
      h5 += h5;
      h6 += h6;
      h7 += h7;
      h8 += h8;
      h9 += h9;
      long carry0 = h0 + 33554432L >> 26;
      h1 += carry0;
      h0 -= carry0 << 26;
      long carry4 = h4 + 33554432L >> 26;
      h5 += carry4;
      h4 -= carry4 << 26;
      long carry1 = h1 + 16777216L >> 25;
      h2 += carry1;
      h1 -= carry1 << 25;
      long carry5 = h5 + 16777216L >> 25;
      h6 += carry5;
      h5 -= carry5 << 25;
      long carry2 = h2 + 33554432L >> 26;
      h3 += carry2;
      h2 -= carry2 << 26;
      long carry6 = h6 + 33554432L >> 26;
      h7 += carry6;
      h6 -= carry6 << 26;
      long carry3 = h3 + 16777216L >> 25;
      h4 += carry3;
      h3 -= carry3 << 25;
      long carry7 = h7 + 16777216L >> 25;
      h8 += carry7;
      h7 -= carry7 << 25;
      carry4 = h4 + 33554432L >> 26;
      h5 += carry4;
      h4 -= carry4 << 26;
      long carry8 = h8 + 33554432L >> 26;
      h9 += carry8;
      h8 -= carry8 << 26;
      long carry9 = h9 + 16777216L >> 25;
      h0 += carry9 * 19L;
      h9 -= carry9 << 25;
      carry0 = h0 + 33554432L >> 26;
      h1 += carry0;
      h0 -= carry0 << 26;
      int[] h = new int[]{(int)h0, (int)h1, (int)h2, (int)h3, (int)h4, (int)h5, (int)h6, (int)h7, (int)h8, (int)h9};
      return new Ed25519FieldElement(this.f, h);
   }

   public FieldElement invert() {
      FieldElement t0 = this.square();
      FieldElement t1 = t0.square();
      t1 = t1.square();
      t1 = this.multiply(t1);
      t0 = t0.multiply(t1);
      FieldElement t2 = t0.square();
      t1 = t1.multiply(t2);
      t2 = t1.square();

      int i;
      for(i = 1; i < 5; ++i) {
         t2 = t2.square();
      }

      t1 = t2.multiply(t1);
      t2 = t1.square();

      for(i = 1; i < 10; ++i) {
         t2 = t2.square();
      }

      t2 = t2.multiply(t1);
      FieldElement t3 = t2.square();

      for(i = 1; i < 20; ++i) {
         t3 = t3.square();
      }

      t2 = t3.multiply(t2);
      t2 = t2.square();

      for(i = 1; i < 10; ++i) {
         t2 = t2.square();
      }

      t1 = t2.multiply(t1);
      t2 = t1.square();

      for(i = 1; i < 50; ++i) {
         t2 = t2.square();
      }

      t2 = t2.multiply(t1);
      t3 = t2.square();

      for(i = 1; i < 100; ++i) {
         t3 = t3.square();
      }

      t2 = t3.multiply(t2);
      t2 = t2.square();

      for(i = 1; i < 50; ++i) {
         t2 = t2.square();
      }

      t1 = t2.multiply(t1);
      t1 = t1.square();

      for(i = 1; i < 5; ++i) {
         t1 = t1.square();
      }

      return t1.multiply(t0);
   }

   public FieldElement pow22523() {
      FieldElement t0 = this.square();
      FieldElement t1 = t0.square();
      t1 = t1.square();
      t1 = this.multiply(t1);
      t0 = t0.multiply(t1);
      t0 = t0.square();
      t0 = t1.multiply(t0);
      t1 = t0.square();

      int i;
      for(i = 1; i < 5; ++i) {
         t1 = t1.square();
      }

      t0 = t1.multiply(t0);
      t1 = t0.square();

      for(i = 1; i < 10; ++i) {
         t1 = t1.square();
      }

      t1 = t1.multiply(t0);
      FieldElement t2 = t1.square();

      for(i = 1; i < 20; ++i) {
         t2 = t2.square();
      }

      t1 = t2.multiply(t1);
      t1 = t1.square();

      for(i = 1; i < 10; ++i) {
         t1 = t1.square();
      }

      t0 = t1.multiply(t0);
      t1 = t0.square();

      for(i = 1; i < 50; ++i) {
         t1 = t1.square();
      }

      t1 = t1.multiply(t0);
      t2 = t1.square();

      for(i = 1; i < 100; ++i) {
         t2 = t2.square();
      }

      t1 = t2.multiply(t1);
      t1 = t1.square();

      for(i = 1; i < 50; ++i) {
         t1 = t1.square();
      }

      t0 = t1.multiply(t0);
      t0 = t0.square();
      t0 = t0.square();
      return this.multiply(t0);
   }

   public FieldElement cmov(FieldElement val, int b) {
      Ed25519FieldElement that = (Ed25519FieldElement)val;
      b = -b;
      int[] result = new int[10];

      for(int i = 0; i < 10; ++i) {
         result[i] = this.t[i];
         int x = this.t[i] ^ that.t[i];
         x &= b;
         result[i] ^= x;
      }

      return new Ed25519FieldElement(this.f, result);
   }

   public int hashCode() {
      return Arrays.hashCode(this.t);
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof Ed25519FieldElement)) {
         return false;
      } else {
         Ed25519FieldElement fe = (Ed25519FieldElement)obj;
         return 1 == Utils.equal(this.toByteArray(), fe.toByteArray());
      }
   }

   public String toString() {
      return "[Ed25519FieldElement val=" + Utils.bytesToHex(this.toByteArray()) + "]";
   }
}
