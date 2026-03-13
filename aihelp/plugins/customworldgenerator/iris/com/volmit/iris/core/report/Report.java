package com.volmit.iris.core.report;

import lombok.Generated;

public class Report {
   private final ReportType type;
   private final String title;
   private final String message;
   private final String suggestion;

   public String toString() {
      String var10000 = String.valueOf(this.type);
      return var10000 + ": " + this.title + ": " + this.message + ": Suggestion: " + this.suggestion;
   }

   @Generated
   private static ReportType $default$type() {
      return ReportType.NOTICE;
   }

   @Generated
   private static String $default$title() {
      return "Problem...";
   }

   @Generated
   private static String $default$message() {
      return "No Message";
   }

   @Generated
   private static String $default$suggestion() {
      return "No Suggestion";
   }

   @Generated
   Report(final ReportType type, final String title, final String message, final String suggestion) {
      this.type = var1;
      this.title = var2;
      this.message = var3;
      this.suggestion = var4;
   }

   @Generated
   public static Report.ReportBuilder builder() {
      return new Report.ReportBuilder();
   }

   @Generated
   public ReportType getType() {
      return this.type;
   }

   @Generated
   public String getTitle() {
      return this.title;
   }

   @Generated
   public String getMessage() {
      return this.message;
   }

   @Generated
   public String getSuggestion() {
      return this.suggestion;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Report)) {
         return false;
      } else {
         Report var2 = (Report)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label59: {
               ReportType var3 = this.getType();
               ReportType var4 = var2.getType();
               if (var3 == null) {
                  if (var4 == null) {
                     break label59;
                  }
               } else if (var3.equals(var4)) {
                  break label59;
               }

               return false;
            }

            String var5 = this.getTitle();
            String var6 = var2.getTitle();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            String var7 = this.getMessage();
            String var8 = var2.getMessage();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            String var9 = this.getSuggestion();
            String var10 = var2.getSuggestion();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof Report;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      ReportType var3 = this.getType();
      int var7 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      String var4 = this.getTitle();
      var7 = var7 * 59 + (var4 == null ? 43 : var4.hashCode());
      String var5 = this.getMessage();
      var7 = var7 * 59 + (var5 == null ? 43 : var5.hashCode());
      String var6 = this.getSuggestion();
      var7 = var7 * 59 + (var6 == null ? 43 : var6.hashCode());
      return var7;
   }

   @Generated
   public static class ReportBuilder {
      @Generated
      private boolean type$set;
      @Generated
      private ReportType type$value;
      @Generated
      private boolean title$set;
      @Generated
      private String title$value;
      @Generated
      private boolean message$set;
      @Generated
      private String message$value;
      @Generated
      private boolean suggestion$set;
      @Generated
      private String suggestion$value;

      @Generated
      ReportBuilder() {
      }

      @Generated
      public Report.ReportBuilder type(final ReportType type) {
         this.type$value = var1;
         this.type$set = true;
         return this;
      }

      @Generated
      public Report.ReportBuilder title(final String title) {
         this.title$value = var1;
         this.title$set = true;
         return this;
      }

      @Generated
      public Report.ReportBuilder message(final String message) {
         this.message$value = var1;
         this.message$set = true;
         return this;
      }

      @Generated
      public Report.ReportBuilder suggestion(final String suggestion) {
         this.suggestion$value = var1;
         this.suggestion$set = true;
         return this;
      }

      @Generated
      public Report build() {
         ReportType var1 = this.type$value;
         if (!this.type$set) {
            var1 = Report.$default$type();
         }

         String var2 = this.title$value;
         if (!this.title$set) {
            var2 = Report.$default$title();
         }

         String var3 = this.message$value;
         if (!this.message$set) {
            var3 = Report.$default$message();
         }

         String var4 = this.suggestion$value;
         if (!this.suggestion$set) {
            var4 = Report.$default$suggestion();
         }

         return new Report(var1, var2, var3, var4);
      }

      @Generated
      public String toString() {
         String var10000 = String.valueOf(this.type$value);
         return "Report.ReportBuilder(type$value=" + var10000 + ", title$value=" + this.title$value + ", message$value=" + this.message$value + ", suggestion$value=" + this.suggestion$value + ")";
      }
   }
}
