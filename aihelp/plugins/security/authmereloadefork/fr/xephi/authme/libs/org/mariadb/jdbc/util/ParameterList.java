package fr.xephi.authme.libs.org.mariadb.jdbc.util;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.Parameter;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.Parameters;
import java.util.Arrays;

public class ParameterList implements Parameters, Cloneable {
   Parameter[] elementData;
   int length;

   public ParameterList(int defaultSize) {
      this.elementData = new Parameter[defaultSize];
      this.length = 0;
   }

   public ParameterList() {
      this.elementData = new Parameter[10];
      this.length = 0;
   }

   public Parameter get(int index) {
      if (index >= this.length) {
         throw new ArrayIndexOutOfBoundsException("wrong index " + index + " length:" + this.length);
      } else {
         return this.elementData[index];
      }
   }

   public boolean containsKey(int index) {
      if (index >= 0 && this.length > index) {
         return this.elementData[index] != null;
      } else {
         return false;
      }
   }

   public void set(int index, Parameter element) {
      if (this.elementData.length <= index) {
         this.grow(index + 1);
      }

      this.elementData[index] = element;
      if (index >= this.length) {
         this.length = index + 1;
      }

   }

   public int size() {
      return this.length;
   }

   private void grow(int minLength) {
      int currLength = this.elementData.length;
      int newLength = Math.max(currLength + (currLength >> 1), minLength);
      this.elementData = (Parameter[])Arrays.copyOf(this.elementData, newLength);
   }

   public ParameterList clone() {
      ParameterList param = new ParameterList(this.length);
      if (this.length > 0) {
         System.arraycopy(this.elementData, 0, param.elementData, 0, this.length);
      }

      param.length = this.length;
      return param;
   }
}
