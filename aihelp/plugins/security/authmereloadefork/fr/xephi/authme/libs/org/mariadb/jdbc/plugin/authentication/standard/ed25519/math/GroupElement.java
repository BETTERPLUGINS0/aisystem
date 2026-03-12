package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.math;

import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519.Utils;
import java.io.Serializable;
import java.util.Arrays;

public class GroupElement implements Serializable {
   private static final long serialVersionUID = 2395879087349587L;
   final Curve curve;
   final GroupElement.Representation repr;
   final FieldElement X;
   final FieldElement Y;
   final FieldElement Z;
   final FieldElement T;
   final GroupElement[][] precmp;
   final GroupElement[] dblPrecmp;

   public GroupElement(Curve curve, GroupElement.Representation repr, FieldElement X, FieldElement Y, FieldElement Z, FieldElement T) {
      this(curve, repr, X, Y, Z, T, false);
   }

   public GroupElement(Curve curve, GroupElement.Representation repr, FieldElement X, FieldElement Y, FieldElement Z, FieldElement T, boolean precomputeDouble) {
      this.curve = curve;
      this.repr = repr;
      this.X = X;
      this.Y = Y;
      this.Z = Z;
      this.T = T;
      this.precmp = null;
      this.dblPrecmp = precomputeDouble ? this.precomputeDouble() : null;
   }

   public GroupElement(Curve curve, byte[] s) {
      this(curve, s, false);
   }

   public GroupElement(Curve curve, byte[] s, boolean precomputeSingleAndDouble) {
      FieldElement y = curve.getField().fromByteArray(s);
      FieldElement yy = y.square();
      FieldElement u = yy.subtractOne();
      FieldElement v = yy.multiply(curve.getD()).addOne();
      FieldElement v3 = v.square().multiply(v);
      FieldElement x = v3.square().multiply(v).multiply(u);
      x = x.pow22523();
      x = v3.multiply(u).multiply(x);
      FieldElement vxx = x.square().multiply(v);
      FieldElement check = vxx.subtract(u);
      if (check.isNonZero()) {
         check = vxx.add(u);
         if (check.isNonZero()) {
            throw new IllegalArgumentException("not a valid GroupElement");
         }

         x = x.multiply(curve.getI());
      }

      if ((x.isNegative() ? 1 : 0) != Utils.bit(s, curve.getField().getb() - 1)) {
         x = x.negate();
      }

      this.curve = curve;
      this.repr = GroupElement.Representation.P3;
      this.X = x;
      this.Y = y;
      this.Z = curve.getField().ONE;
      this.T = this.X.multiply(this.Y);
      if (precomputeSingleAndDouble) {
         this.precmp = this.precomputeSingle();
         this.dblPrecmp = this.precomputeDouble();
      } else {
         this.precmp = null;
         this.dblPrecmp = null;
      }

   }

   public static GroupElement p2(Curve curve, FieldElement X, FieldElement Y, FieldElement Z) {
      return new GroupElement(curve, GroupElement.Representation.P2, X, Y, Z, (FieldElement)null);
   }

   public static GroupElement p3(Curve curve, FieldElement X, FieldElement Y, FieldElement Z, FieldElement T) {
      return p3(curve, X, Y, Z, T, false);
   }

   public static GroupElement p3(Curve curve, FieldElement X, FieldElement Y, FieldElement Z, FieldElement T, boolean precomputeDoubleOnly) {
      return new GroupElement(curve, GroupElement.Representation.P3, X, Y, Z, T, precomputeDoubleOnly);
   }

   public static GroupElement p1p1(Curve curve, FieldElement X, FieldElement Y, FieldElement Z, FieldElement T) {
      return new GroupElement(curve, GroupElement.Representation.P1P1, X, Y, Z, T);
   }

   public static GroupElement precomp(Curve curve, FieldElement ypx, FieldElement ymx, FieldElement xy2d) {
      return new GroupElement(curve, GroupElement.Representation.PRECOMP, ypx, ymx, xy2d, (FieldElement)null);
   }

   public static GroupElement cached(Curve curve, FieldElement YpX, FieldElement YmX, FieldElement Z, FieldElement T2d) {
      return new GroupElement(curve, GroupElement.Representation.CACHED, YpX, YmX, Z, T2d);
   }

   static byte[] toRadix16(byte[] a) {
      byte[] e = new byte[64];

      int i;
      for(i = 0; i < 32; ++i) {
         e[2 * i] = (byte)(a[i] & 15);
         e[2 * i + 1] = (byte)(a[i] >> 4 & 15);
      }

      int carry = 0;

      for(i = 0; i < 63; ++i) {
         e[i] = (byte)(e[i] + carry);
         carry = e[i] + 8;
         carry >>= 4;
         e[i] = (byte)(e[i] - (carry << 4));
      }

      e[63] = (byte)(e[63] + carry);
      return e;
   }

   static byte[] slide(byte[] a) {
      byte[] r = new byte[256];

      int i;
      for(i = 0; i < 256; ++i) {
         r[i] = (byte)(1 & a[i >> 3] >> (i & 7));
      }

      for(i = 0; i < 256; ++i) {
         if (r[i] != 0) {
            for(int b = 1; b <= 6 && i + b < 256; ++b) {
               if (r[i + b] != 0) {
                  if (r[i] + (r[i + b] << b) <= 15) {
                     r[i] = (byte)(r[i] + (r[i + b] << b));
                     r[i + b] = 0;
                  } else {
                     if (r[i] - (r[i + b] << b) < -15) {
                        break;
                     }

                     r[i] = (byte)(r[i] - (r[i + b] << b));

                     for(int k = i + b; k < 256; ++k) {
                        if (r[k] == 0) {
                           r[k] = 1;
                           break;
                        }

                        r[k] = 0;
                     }
                  }
               }
            }
         }
      }

      return r;
   }

   public Curve getCurve() {
      return this.curve;
   }

   public GroupElement.Representation getRepresentation() {
      return this.repr;
   }

   public FieldElement getX() {
      return this.X;
   }

   public FieldElement getY() {
      return this.Y;
   }

   public FieldElement getZ() {
      return this.Z;
   }

   public FieldElement getT() {
      return this.T;
   }

   public byte[] toByteArray() {
      switch(this.repr) {
      case P2:
      case P3:
         FieldElement recip = this.Z.invert();
         FieldElement x = this.X.multiply(recip);
         FieldElement y = this.Y.multiply(recip);
         byte[] s = y.toByteArray();
         s[s.length - 1] = (byte)(s[s.length - 1] | (x.isNegative() ? -128 : 0));
         return s;
      default:
         return this.toP2().toByteArray();
      }
   }

   public GroupElement toP2() {
      return this.toRep(GroupElement.Representation.P2);
   }

   public GroupElement toP3() {
      return this.toRep(GroupElement.Representation.P3);
   }

   public GroupElement toP3PrecomputeDouble() {
      return this.toRep(GroupElement.Representation.P3PrecomputedDouble);
   }

   public GroupElement toCached() {
      return this.toRep(GroupElement.Representation.CACHED);
   }

   private GroupElement toRep(GroupElement.Representation repr) {
      switch(this.repr) {
      case P2:
         switch(repr) {
         case P2:
            return p2(this.curve, this.X, this.Y, this.Z);
         default:
            throw new IllegalArgumentException();
         }
      case P3:
         switch(repr) {
         case P2:
            return p2(this.curve, this.X, this.Y, this.Z);
         case P3:
            return p3(this.curve, this.X, this.Y, this.Z, this.T);
         case CACHED:
            return cached(this.curve, this.Y.add(this.X), this.Y.subtract(this.X), this.Z, this.T.multiply(this.curve.get2D()));
         default:
            throw new IllegalArgumentException();
         }
      case CACHED:
         switch(repr) {
         case CACHED:
            return cached(this.curve, this.X, this.Y, this.Z, this.T);
         default:
            throw new IllegalArgumentException();
         }
      case P3PrecomputedDouble:
      default:
         throw new UnsupportedOperationException();
      case P1P1:
         switch(repr) {
         case P2:
            return p2(this.curve, this.X.multiply(this.T), this.Y.multiply(this.Z), this.Z.multiply(this.T));
         case P3:
            return p3(this.curve, this.X.multiply(this.T), this.Y.multiply(this.Z), this.Z.multiply(this.T), this.X.multiply(this.Y), false);
         case CACHED:
         default:
            throw new IllegalArgumentException();
         case P3PrecomputedDouble:
            return p3(this.curve, this.X.multiply(this.T), this.Y.multiply(this.Z), this.Z.multiply(this.T), this.X.multiply(this.Y), true);
         case P1P1:
            return p1p1(this.curve, this.X, this.Y, this.Z, this.T);
         }
      case PRECOMP:
         switch(repr) {
         case PRECOMP:
            return precomp(this.curve, this.X, this.Y, this.Z);
         default:
            throw new IllegalArgumentException();
         }
      }
   }

   private GroupElement[][] precomputeSingle() {
      GroupElement[][] precmp = new GroupElement[32][8];
      GroupElement Bi = this;

      for(int i = 0; i < 32; ++i) {
         GroupElement Bij = Bi;

         int k;
         for(k = 0; k < 8; ++k) {
            FieldElement recip = Bij.Z.invert();
            FieldElement x = Bij.X.multiply(recip);
            FieldElement y = Bij.Y.multiply(recip);
            precmp[i][k] = precomp(this.curve, y.add(x), y.subtract(x), x.multiply(y).multiply(this.curve.get2D()));
            Bij = Bij.add(Bi.toCached()).toP3();
         }

         for(k = 0; k < 8; ++k) {
            Bi = Bi.add(Bi.toCached()).toP3();
         }
      }

      return precmp;
   }

   private GroupElement[] precomputeDouble() {
      GroupElement[] dblPrecmp = new GroupElement[8];
      GroupElement Bi = this;

      for(int i = 0; i < 8; ++i) {
         FieldElement recip = Bi.Z.invert();
         FieldElement x = Bi.X.multiply(recip);
         FieldElement y = Bi.Y.multiply(recip);
         dblPrecmp[i] = precomp(this.curve, y.add(x), y.subtract(x), x.multiply(y).multiply(this.curve.get2D()));
         Bi = this.add(this.add(Bi.toCached()).toP3().toCached()).toP3();
      }

      return dblPrecmp;
   }

   public GroupElement dbl() {
      switch(this.repr) {
      case P2:
      case P3:
         FieldElement XX = this.X.square();
         FieldElement YY = this.Y.square();
         FieldElement B = this.Z.squareAndDouble();
         FieldElement A = this.X.add(this.Y);
         FieldElement AA = A.square();
         FieldElement Yn = YY.add(XX);
         FieldElement Zn = YY.subtract(XX);
         return p1p1(this.curve, AA.subtract(Yn), Yn, Zn, B.subtract(Zn));
      default:
         throw new UnsupportedOperationException();
      }
   }

   private GroupElement madd(GroupElement q) {
      if (this.repr != GroupElement.Representation.P3) {
         throw new UnsupportedOperationException();
      } else if (q.repr != GroupElement.Representation.PRECOMP) {
         throw new IllegalArgumentException();
      } else {
         FieldElement YpX = this.Y.add(this.X);
         FieldElement YmX = this.Y.subtract(this.X);
         FieldElement A = YpX.multiply(q.X);
         FieldElement B = YmX.multiply(q.Y);
         FieldElement C = q.Z.multiply(this.T);
         FieldElement D = this.Z.add(this.Z);
         return p1p1(this.curve, A.subtract(B), A.add(B), D.add(C), D.subtract(C));
      }
   }

   private GroupElement msub(GroupElement q) {
      if (this.repr != GroupElement.Representation.P3) {
         throw new UnsupportedOperationException();
      } else if (q.repr != GroupElement.Representation.PRECOMP) {
         throw new IllegalArgumentException();
      } else {
         FieldElement YpX = this.Y.add(this.X);
         FieldElement YmX = this.Y.subtract(this.X);
         FieldElement A = YpX.multiply(q.Y);
         FieldElement B = YmX.multiply(q.X);
         FieldElement C = q.Z.multiply(this.T);
         FieldElement D = this.Z.add(this.Z);
         return p1p1(this.curve, A.subtract(B), A.add(B), D.subtract(C), D.add(C));
      }
   }

   public GroupElement add(GroupElement q) {
      if (this.repr != GroupElement.Representation.P3) {
         throw new UnsupportedOperationException();
      } else if (q.repr != GroupElement.Representation.CACHED) {
         throw new IllegalArgumentException();
      } else {
         FieldElement YpX = this.Y.add(this.X);
         FieldElement YmX = this.Y.subtract(this.X);
         FieldElement A = YpX.multiply(q.X);
         FieldElement B = YmX.multiply(q.Y);
         FieldElement C = q.T.multiply(this.T);
         FieldElement ZZ = this.Z.multiply(q.Z);
         FieldElement D = ZZ.add(ZZ);
         return p1p1(this.curve, A.subtract(B), A.add(B), D.add(C), D.subtract(C));
      }
   }

   public GroupElement sub(GroupElement q) {
      if (this.repr != GroupElement.Representation.P3) {
         throw new UnsupportedOperationException();
      } else if (q.repr != GroupElement.Representation.CACHED) {
         throw new IllegalArgumentException();
      } else {
         FieldElement YpX = this.Y.add(this.X);
         FieldElement YmX = this.Y.subtract(this.X);
         FieldElement A = YpX.multiply(q.Y);
         FieldElement B = YmX.multiply(q.X);
         FieldElement C = q.T.multiply(this.T);
         FieldElement ZZ = this.Z.multiply(q.Z);
         FieldElement D = ZZ.add(ZZ);
         return p1p1(this.curve, A.subtract(B), A.add(B), D.subtract(C), D.add(C));
      }
   }

   public GroupElement negate() {
      if (this.repr != GroupElement.Representation.P3) {
         throw new UnsupportedOperationException();
      } else {
         return this.curve.getZero(GroupElement.Representation.P3).sub(this.toCached()).toP3PrecomputeDouble();
      }
   }

   public int hashCode() {
      return Arrays.hashCode(this.toByteArray());
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof GroupElement)) {
         return false;
      } else {
         GroupElement ge = (GroupElement)obj;
         if (!this.repr.equals(ge.repr)) {
            try {
               ge = ge.toRep(this.repr);
            } catch (RuntimeException var13) {
               return false;
            }
         }

         switch(this.repr) {
         case P2:
         case P3:
            if (this.Z.equals(ge.Z)) {
               return this.X.equals(ge.X) && this.Y.equals(ge.Y);
            } else {
               FieldElement x1 = this.X.multiply(ge.Z);
               FieldElement y1 = this.Y.multiply(ge.Z);
               FieldElement x2 = ge.X.multiply(this.Z);
               FieldElement y2 = ge.Y.multiply(this.Z);
               return x1.equals(x2) && y1.equals(y2);
            }
         case CACHED:
            if (this.Z.equals(ge.Z)) {
               return this.X.equals(ge.X) && this.Y.equals(ge.Y) && this.T.equals(ge.T);
            }

            FieldElement x3 = this.X.multiply(ge.Z);
            FieldElement y3 = this.Y.multiply(ge.Z);
            FieldElement t3 = this.T.multiply(ge.Z);
            FieldElement x4 = ge.X.multiply(this.Z);
            FieldElement y4 = ge.Y.multiply(this.Z);
            FieldElement t4 = ge.T.multiply(this.Z);
            return x3.equals(x4) && y3.equals(y4) && t3.equals(t4);
         case P3PrecomputedDouble:
         default:
            return false;
         case P1P1:
            return this.toP2().equals(ge);
         case PRECOMP:
            return this.X.equals(ge.X) && this.Y.equals(ge.Y) && this.Z.equals(ge.Z);
         }
      }
   }

   GroupElement cmov(GroupElement u, int b) {
      return precomp(this.curve, this.X.cmov(u.X, b), this.Y.cmov(u.Y, b), this.Z.cmov(u.Z, b));
   }

   GroupElement select(int pos, int b) {
      int bnegative = Utils.negative(b);
      int babs = b - ((-bnegative & b) << 1);
      GroupElement t = this.curve.getZero(GroupElement.Representation.PRECOMP).cmov(this.precmp[pos][0], Utils.equal(babs, 1)).cmov(this.precmp[pos][1], Utils.equal(babs, 2)).cmov(this.precmp[pos][2], Utils.equal(babs, 3)).cmov(this.precmp[pos][3], Utils.equal(babs, 4)).cmov(this.precmp[pos][4], Utils.equal(babs, 5)).cmov(this.precmp[pos][5], Utils.equal(babs, 6)).cmov(this.precmp[pos][6], Utils.equal(babs, 7)).cmov(this.precmp[pos][7], Utils.equal(babs, 8));
      GroupElement tminus = precomp(this.curve, t.Y, t.X, t.Z.negate());
      return t.cmov(tminus, bnegative);
   }

   public GroupElement scalarMultiply(byte[] a) {
      byte[] e = toRadix16(a);
      GroupElement h = this.curve.getZero(GroupElement.Representation.P3);

      GroupElement t;
      int i;
      for(i = 1; i < 64; i += 2) {
         t = this.select(i / 2, e[i]);
         h = h.madd(t).toP3();
      }

      h = h.dbl().toP2().dbl().toP2().dbl().toP2().dbl().toP3();

      for(i = 0; i < 64; i += 2) {
         t = this.select(i / 2, e[i]);
         h = h.madd(t).toP3();
      }

      return h;
   }

   public GroupElement doubleScalarMultiplyVariableTime(GroupElement A, byte[] a, byte[] b) {
      byte[] aslide = slide(a);
      byte[] bslide = slide(b);
      GroupElement r = this.curve.getZero(GroupElement.Representation.P2);

      int i;
      for(i = 255; i >= 0 && aslide[i] == 0 && bslide[i] == 0; --i) {
      }

      while(i >= 0) {
         GroupElement t = r.dbl();
         if (aslide[i] > 0) {
            t = t.toP3().madd(A.dblPrecmp[aslide[i] / 2]);
         } else if (aslide[i] < 0) {
            t = t.toP3().msub(A.dblPrecmp[-aslide[i] / 2]);
         }

         if (bslide[i] > 0) {
            t = t.toP3().madd(this.dblPrecmp[bslide[i] / 2]);
         } else if (bslide[i] < 0) {
            t = t.toP3().msub(this.dblPrecmp[-bslide[i] / 2]);
         }

         r = t.toP2();
         --i;
      }

      return r;
   }

   public boolean isOnCurve() {
      return this.isOnCurve(this.curve);
   }

   public boolean isOnCurve(Curve curve) {
      switch(this.repr) {
      case P2:
      case P3:
         FieldElement recip = this.Z.invert();
         FieldElement x = this.X.multiply(recip);
         FieldElement y = this.Y.multiply(recip);
         FieldElement xx = x.square();
         FieldElement yy = y.square();
         FieldElement dxxyy = curve.getD().multiply(xx).multiply(yy);
         return curve.getField().ONE.add(dxxyy).add(xx).equals(yy);
      default:
         return this.toP2().isOnCurve(curve);
      }
   }

   public String toString() {
      return "[GroupElement\nX=" + this.X + "\nY=" + this.Y + "\nZ=" + this.Z + "\nT=" + this.T + "\n]";
   }

   public static enum Representation {
      P2,
      P3,
      P3PrecomputedDouble,
      P1P1,
      PRECOMP,
      CACHED;
   }
}
