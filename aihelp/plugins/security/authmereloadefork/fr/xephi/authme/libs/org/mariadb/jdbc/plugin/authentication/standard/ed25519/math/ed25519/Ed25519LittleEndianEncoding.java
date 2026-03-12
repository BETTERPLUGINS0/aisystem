package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math.ed25519;

import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math.Encoding;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math.FieldElement;

public class Ed25519LittleEndianEncoding extends Encoding {
   static int load_3(byte[] in, int offset) {
      int result = in[offset++] & 255;
      result |= (in[offset++] & 255) << 8;
      result |= (in[offset] & 255) << 16;
      return result;
   }

   static long load_4(byte[] in, int offset) {
      int result = in[offset++] & 255;
      result |= (in[offset++] & 255) << 8;
      result |= (in[offset++] & 255) << 16;
      result |= in[offset] << 24;
      return (long)result & 4294967295L;
   }

   public byte[] encode(FieldElement x) {
      int[] h = ((Ed25519FieldElement)x).t;
      int h0 = h[0];
      int h1 = h[1];
      int h2 = h[2];
      int h3 = h[3];
      int h4 = h[4];
      int h5 = h[5];
      int h6 = h[6];
      int h7 = h[7];
      int h8 = h[8];
      int h9 = h[9];
      int q = 19 * h9 + 16777216 >> 25;
      q = h0 + q >> 26;
      q = h1 + q >> 25;
      q = h2 + q >> 26;
      q = h3 + q >> 25;
      q = h4 + q >> 26;
      q = h5 + q >> 25;
      q = h6 + q >> 26;
      q = h7 + q >> 25;
      q = h8 + q >> 26;
      q = h9 + q >> 25;
      h0 += 19 * q;
      int carry0 = h0 >> 26;
      h1 += carry0;
      h0 -= carry0 << 26;
      int carry1 = h1 >> 25;
      h2 += carry1;
      h1 -= carry1 << 25;
      int carry2 = h2 >> 26;
      h3 += carry2;
      h2 -= carry2 << 26;
      int carry3 = h3 >> 25;
      h4 += carry3;
      h3 -= carry3 << 25;
      int carry4 = h4 >> 26;
      h5 += carry4;
      h4 -= carry4 << 26;
      int carry5 = h5 >> 25;
      h6 += carry5;
      h5 -= carry5 << 25;
      int carry6 = h6 >> 26;
      h7 += carry6;
      h6 -= carry6 << 26;
      int carry7 = h7 >> 25;
      h8 += carry7;
      h7 -= carry7 << 25;
      int carry8 = h8 >> 26;
      h9 += carry8;
      h8 -= carry8 << 26;
      int carry9 = h9 >> 25;
      h9 -= carry9 << 25;
      byte[] s = new byte[]{(byte)h0, (byte)(h0 >> 8), (byte)(h0 >> 16), (byte)(h0 >> 24 | h1 << 2), (byte)(h1 >> 6), (byte)(h1 >> 14), (byte)(h1 >> 22 | h2 << 3), (byte)(h2 >> 5), (byte)(h2 >> 13), (byte)(h2 >> 21 | h3 << 5), (byte)(h3 >> 3), (byte)(h3 >> 11), (byte)(h3 >> 19 | h4 << 6), (byte)(h4 >> 2), (byte)(h4 >> 10), (byte)(h4 >> 18), (byte)h5, (byte)(h5 >> 8), (byte)(h5 >> 16), (byte)(h5 >> 24 | h6 << 1), (byte)(h6 >> 7), (byte)(h6 >> 15), (byte)(h6 >> 23 | h7 << 3), (byte)(h7 >> 5), (byte)(h7 >> 13), (byte)(h7 >> 21 | h8 << 4), (byte)(h8 >> 4), (byte)(h8 >> 12), (byte)(h8 >> 20 | h9 << 6), (byte)(h9 >> 2), (byte)(h9 >> 10), (byte)(h9 >> 18)};
      return s;
   }

   public FieldElement decode(byte[] in) {
      long h0 = load_4(in, 0);
      long h1 = (long)load_3(in, 4) << 6;
      long h2 = (long)load_3(in, 7) << 5;
      long h3 = (long)load_3(in, 10) << 3;
      long h4 = (long)load_3(in, 13) << 2;
      long h5 = load_4(in, 16);
      long h6 = (long)load_3(in, 20) << 7;
      long h7 = (long)load_3(in, 23) << 5;
      long h8 = (long)load_3(in, 26) << 4;
      long h9 = (long)((load_3(in, 29) & 8388607) << 2);
      long carry9 = h9 + 16777216L >> 25;
      h0 += carry9 * 19L;
      h9 -= carry9 << 25;
      long carry1 = h1 + 16777216L >> 25;
      h2 += carry1;
      h1 -= carry1 << 25;
      long carry3 = h3 + 16777216L >> 25;
      h4 += carry3;
      h3 -= carry3 << 25;
      long carry5 = h5 + 16777216L >> 25;
      h6 += carry5;
      h5 -= carry5 << 25;
      long carry7 = h7 + 16777216L >> 25;
      h8 += carry7;
      h7 -= carry7 << 25;
      long carry0 = h0 + 33554432L >> 26;
      h1 += carry0;
      h0 -= carry0 << 26;
      long carry2 = h2 + 33554432L >> 26;
      h3 += carry2;
      h2 -= carry2 << 26;
      long carry4 = h4 + 33554432L >> 26;
      h5 += carry4;
      h4 -= carry4 << 26;
      long carry6 = h6 + 33554432L >> 26;
      h7 += carry6;
      h6 -= carry6 << 26;
      long carry8 = h8 + 33554432L >> 26;
      h9 += carry8;
      h8 -= carry8 << 26;
      int[] h = new int[]{(int)h0, (int)h1, (int)h2, (int)h3, (int)h4, (int)h5, (int)h6, (int)h7, (int)h8, (int)h9};
      return new Ed25519FieldElement(this.f, h);
   }

   public boolean isNegative(FieldElement x) {
      byte[] s = this.encode(x);
      return (s[0] & 1) != 0;
   }
}
