package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.IOExceptionList;
import org.apache.commons.io.IOIndexedException;

public class FilterCollectionWriter extends Writer {
   protected final Collection<Writer> EMPTY_WRITERS = Collections.emptyList();
   protected final Collection<Writer> writers;

   protected FilterCollectionWriter(Collection<Writer> var1) {
      this.writers = var1 == null ? this.EMPTY_WRITERS : var1;
   }

   protected FilterCollectionWriter(Writer... var1) {
      this.writers = (Collection)(var1 == null ? this.EMPTY_WRITERS : Arrays.asList(var1));
   }

   private List<Exception> add(List<Exception> var1, int var2, IOException var3) {
      if (var1 == null) {
         var1 = new ArrayList();
      }

      ((List)var1).add(new IOIndexedException(var2, var3));
      return (List)var1;
   }

   public Writer append(char var1) {
      List var2 = null;
      int var3 = 0;

      for(Iterator var4 = this.writers.iterator(); var4.hasNext(); ++var3) {
         Writer var5 = (Writer)var4.next();
         if (var5 != null) {
            try {
               var5.append(var1);
            } catch (IOException var7) {
               var2 = this.add(var2, var3, var7);
            }
         }
      }

      if (this.notEmpty(var2)) {
         throw new IOExceptionList("append", var2);
      } else {
         return this;
      }
   }

   public Writer append(CharSequence var1) {
      List var2 = null;
      int var3 = 0;

      for(Iterator var4 = this.writers.iterator(); var4.hasNext(); ++var3) {
         Writer var5 = (Writer)var4.next();
         if (var5 != null) {
            try {
               var5.append(var1);
            } catch (IOException var7) {
               var2 = this.add(var2, var3, var7);
            }
         }
      }

      if (this.notEmpty(var2)) {
         throw new IOExceptionList("append", var2);
      } else {
         return this;
      }
   }

   public Writer append(CharSequence var1, int var2, int var3) {
      List var4 = null;
      int var5 = 0;

      for(Iterator var6 = this.writers.iterator(); var6.hasNext(); ++var5) {
         Writer var7 = (Writer)var6.next();
         if (var7 != null) {
            try {
               var7.append(var1, var2, var3);
            } catch (IOException var9) {
               var4 = this.add(var4, var5, var9);
            }
         }
      }

      if (this.notEmpty(var4)) {
         throw new IOExceptionList("append", var4);
      } else {
         return this;
      }
   }

   public void close() {
      List var1 = null;
      int var2 = 0;

      for(Iterator var3 = this.writers.iterator(); var3.hasNext(); ++var2) {
         Writer var4 = (Writer)var3.next();
         if (var4 != null) {
            try {
               var4.close();
            } catch (IOException var6) {
               var1 = this.add(var1, var2, var6);
            }
         }
      }

      if (this.notEmpty(var1)) {
         throw new IOExceptionList("close", var1);
      }
   }

   public void flush() {
      List var1 = null;
      int var2 = 0;

      for(Iterator var3 = this.writers.iterator(); var3.hasNext(); ++var2) {
         Writer var4 = (Writer)var3.next();
         if (var4 != null) {
            try {
               var4.flush();
            } catch (IOException var6) {
               var1 = this.add(var1, var2, var6);
            }
         }
      }

      if (this.notEmpty(var1)) {
         throw new IOExceptionList("flush", var1);
      }
   }

   private boolean notEmpty(List<Exception> var1) {
      return var1 != null && !var1.isEmpty();
   }

   public void write(char[] var1) {
      List var2 = null;
      int var3 = 0;

      for(Iterator var4 = this.writers.iterator(); var4.hasNext(); ++var3) {
         Writer var5 = (Writer)var4.next();
         if (var5 != null) {
            try {
               var5.write(var1);
            } catch (IOException var7) {
               var2 = this.add(var2, var3, var7);
            }
         }
      }

      if (this.notEmpty(var2)) {
         throw new IOExceptionList("write", var2);
      }
   }

   public void write(char[] var1, int var2, int var3) {
      List var4 = null;
      int var5 = 0;

      for(Iterator var6 = this.writers.iterator(); var6.hasNext(); ++var5) {
         Writer var7 = (Writer)var6.next();
         if (var7 != null) {
            try {
               var7.write(var1, var2, var3);
            } catch (IOException var9) {
               var4 = this.add(var4, var5, var9);
            }
         }
      }

      if (this.notEmpty(var4)) {
         throw new IOExceptionList("write", var4);
      }
   }

   public void write(int var1) {
      List var2 = null;
      int var3 = 0;

      for(Iterator var4 = this.writers.iterator(); var4.hasNext(); ++var3) {
         Writer var5 = (Writer)var4.next();
         if (var5 != null) {
            try {
               var5.write(var1);
            } catch (IOException var7) {
               var2 = this.add(var2, var3, var7);
            }
         }
      }

      if (this.notEmpty(var2)) {
         throw new IOExceptionList("write", var2);
      }
   }

   public void write(String var1) {
      List var2 = null;
      int var3 = 0;

      for(Iterator var4 = this.writers.iterator(); var4.hasNext(); ++var3) {
         Writer var5 = (Writer)var4.next();
         if (var5 != null) {
            try {
               var5.write(var1);
            } catch (IOException var7) {
               var2 = this.add(var2, var3, var7);
            }
         }
      }

      if (this.notEmpty(var2)) {
         throw new IOExceptionList("write", var2);
      }
   }

   public void write(String var1, int var2, int var3) {
      List var4 = null;
      int var5 = 0;

      for(Iterator var6 = this.writers.iterator(); var6.hasNext(); ++var5) {
         Writer var7 = (Writer)var6.next();
         if (var7 != null) {
            try {
               var7.write(var1, var2, var3);
            } catch (IOException var9) {
               var4 = this.add(var4, var5, var9);
            }
         }
      }

      if (this.notEmpty(var4)) {
         throw new IOExceptionList("write", var4);
      }
   }
}
