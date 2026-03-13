package libs.com.ryderbelserion.vital.common.config.beans;

import ch.jalu.configme.Comment;
import ch.jalu.configme.beanmapper.ExportName;

public class Plugin {
   @Comment({"This option defines if the plugin/library logs everything,", "Very useful if you have an issue with the plugin!"})
   @ExportName("is_verbose")
   public boolean verbose;
   @Comment({"Controls the format, of the numerical data."})
   @ExportName("number_format")
   public String numberFormat;
   @Comment({"This controls the type of rounding for how the numerical data is rounded.", "", "Available types: up, down, ceiling, floor, half_up, half_down, half_even, unnecessary"})
   public String rounding;

   public final Plugin populate() {
      this.verbose = false;
      this.numberFormat = "#,###.##";
      this.rounding = "half_even";
      return this;
   }

   public void setVerbose(boolean verbose) {
      this.verbose = verbose;
   }

   public void setNumberFormat(String numberFormat) {
      this.numberFormat = numberFormat;
   }

   public void setRounding(String rounding) {
      this.rounding = rounding;
   }

   public boolean isVerbose() {
      return this.verbose;
   }

   public String getNumberFormat() {
      return this.numberFormat;
   }

   public String getRounding() {
      return this.rounding;
   }
}
