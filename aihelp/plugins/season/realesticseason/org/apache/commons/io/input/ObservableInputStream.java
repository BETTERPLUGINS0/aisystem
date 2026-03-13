package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.IOUtils;

public class ObservableInputStream extends ProxyInputStream {
   private final List<ObservableInputStream.Observer> observers;

   public ObservableInputStream(InputStream var1) {
      this(var1, (List)(new ArrayList()));
   }

   private ObservableInputStream(InputStream var1, List<ObservableInputStream.Observer> var2) {
      super(var1);
      this.observers = var2;
   }

   public ObservableInputStream(InputStream var1, ObservableInputStream.Observer... var2) {
      this(var1, Arrays.asList(var2));
   }

   public void add(ObservableInputStream.Observer var1) {
      this.observers.add(var1);
   }

   public void close() {
      IOException var1 = null;

      try {
         super.close();
      } catch (IOException var3) {
         var1 = var3;
      }

      if (var1 == null) {
         this.noteClosed();
      } else {
         this.noteError(var1);
      }

   }

   public void consume() {
      byte[] var1 = IOUtils.byteArray();

      while(this.read(var1) != -1) {
      }

   }

   public List<ObservableInputStream.Observer> getObservers() {
      return this.observers;
   }

   protected void noteClosed() {
      Iterator var1 = this.getObservers().iterator();

      while(var1.hasNext()) {
         ObservableInputStream.Observer var2 = (ObservableInputStream.Observer)var1.next();
         var2.closed();
      }

   }

   protected void noteDataByte(int var1) {
      Iterator var2 = this.getObservers().iterator();

      while(var2.hasNext()) {
         ObservableInputStream.Observer var3 = (ObservableInputStream.Observer)var2.next();
         var3.data(var1);
      }

   }

   protected void noteDataBytes(byte[] var1, int var2, int var3) {
      Iterator var4 = this.getObservers().iterator();

      while(var4.hasNext()) {
         ObservableInputStream.Observer var5 = (ObservableInputStream.Observer)var4.next();
         var5.data(var1, var2, var3);
      }

   }

   protected void noteError(IOException var1) {
      Iterator var2 = this.getObservers().iterator();

      while(var2.hasNext()) {
         ObservableInputStream.Observer var3 = (ObservableInputStream.Observer)var2.next();
         var3.error(var1);
      }

   }

   protected void noteFinished() {
      Iterator var1 = this.getObservers().iterator();

      while(var1.hasNext()) {
         ObservableInputStream.Observer var2 = (ObservableInputStream.Observer)var1.next();
         var2.finished();
      }

   }

   private void notify(byte[] var1, int var2, int var3, IOException var4) {
      if (var4 != null) {
         this.noteError(var4);
         throw var4;
      } else {
         if (var3 == -1) {
            this.noteFinished();
         } else if (var3 > 0) {
            this.noteDataBytes(var1, var2, var3);
         }

      }
   }

   public int read() {
      int var1 = 0;
      IOException var2 = null;

      try {
         var1 = super.read();
      } catch (IOException var4) {
         var2 = var4;
      }

      if (var2 != null) {
         this.noteError(var2);
         throw var2;
      } else {
         if (var1 == -1) {
            this.noteFinished();
         } else {
            this.noteDataByte(var1);
         }

         return var1;
      }
   }

   public int read(byte[] var1) {
      int var2 = 0;
      IOException var3 = null;

      try {
         var2 = super.read(var1);
      } catch (IOException var5) {
         var3 = var5;
      }

      this.notify(var1, 0, var2, var3);
      return var2;
   }

   public int read(byte[] var1, int var2, int var3) {
      int var4 = 0;
      IOException var5 = null;

      try {
         var4 = super.read(var1, var2, var3);
      } catch (IOException var7) {
         var5 = var7;
      }

      this.notify(var1, var2, var4, var5);
      return var4;
   }

   public void remove(ObservableInputStream.Observer var1) {
      this.observers.remove(var1);
   }

   public void removeAllObservers() {
      this.observers.clear();
   }

   public abstract static class Observer {
      public void closed() {
      }

      public void data(byte[] var1, int var2, int var3) {
      }

      public void data(int var1) {
      }

      public void error(IOException var1) {
         throw var1;
      }

      public void finished() {
      }
   }
}
