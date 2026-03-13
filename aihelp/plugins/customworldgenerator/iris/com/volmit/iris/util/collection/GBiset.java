package com.volmit.iris.util.collection;

import java.io.Serializable;

public class GBiset<A, B> implements Serializable {
   private static final long serialVersionUID = 1L;
   private A a;
   private B b;

   public GBiset(A a, B b) {
      this.a = var1;
      this.b = var2;
   }

   public A getA() {
      return this.a;
   }

   public void setA(A a) {
      this.a = var1;
   }

   public B getB() {
      return this.b;
   }

   public void setB(B b) {
      this.b = var1;
   }
}
