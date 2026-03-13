package me.casperge.realisticseasons.calendar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Year {
   private List<Month> months = new ArrayList();

   public Year(List<Month> var1) {
      this.months = var1;
   }

   public List<Month> getMonths() {
      return this.months;
   }

   public Month getNextMonth(Month var1) {
      for(int var2 = 0; var2 < this.months.size(); ++var2) {
         if (((Month)this.months.get(var2)).getName().equals(var1.getName())) {
            if (var2 + 1 == this.months.size()) {
               return (Month)this.months.get(0);
            }

            return (Month)this.months.get(var2 + 1);
         }
      }

      return (Month)this.months.get(0);
   }

   public Month getMonth(int var1) {
      return (Month)this.months.get(var1 - 1);
   }

   public Month getMonth(String var1) {
      Iterator var2 = this.months.iterator();

      Month var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Month)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }
}
