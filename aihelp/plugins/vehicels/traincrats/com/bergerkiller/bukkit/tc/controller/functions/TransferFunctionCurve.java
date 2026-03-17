package com.bergerkiller.bukkit.tc.controller.functions;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionItem;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TransferFunctionCurve implements TransferFunction, Cloneable {
   public static final TransferFunction.Serializer<TransferFunctionCurve> SERIALIZER = new TransferFunction.Serializer<TransferFunctionCurve>() {
      public String typeId() {
         return "CURVE_GRAPH";
      }

      public String title() {
         return "Curve Graph";
      }

      public TransferFunctionCurve createNew(TransferFunctionHost host) {
         return TransferFunctionCurve.empty();
      }

      public TransferFunctionCurve load(TransferFunctionHost host, ConfigurationNode config) {
         TransferFunctionCurve curve = TransferFunctionCurve.empty();
         Iterator var4 = config.getList("values", String.class).iterator();

         while(true) {
            String value;
            int sep;
            do {
               if (!var4.hasNext()) {
                  return curve;
               }

               value = (String)var4.next();
               sep = value.indexOf(61);
            } while(sep == -1);

            int inputEnd = sep;

            int outputStart;
            for(outputStart = sep + 1; inputEnd > 0 && value.charAt(inputEnd) == ' '; --inputEnd) {
            }

            while(outputStart < value.length() && value.charAt(outputStart) == ' ') {
               ++outputStart;
            }

            String inputTxt = value.substring(0, inputEnd).trim();
            String outputTxt = value.substring(outputStart).trim();

            try {
               double input = Double.parseDouble(inputTxt);
               double output = Double.parseDouble(outputTxt);
               curve.add(input, output);
            } catch (NumberFormatException var15) {
            }
         }
      }

      public void save(TransferFunctionHost host, ConfigurationNode config, TransferFunctionCurve curve) {
         if (!curve.isEmpty()) {
            List<String> values = config.getList("values", String.class);

            for(int i = 0; i < curve.size(); ++i) {
               values.add(curve.getInput(i) + " = " + curve.getOutput(i));
            }
         }

      }
   };
   private double[] v;
   private double previousInput = Double.NaN;
   private boolean inputIncreasing = false;

   public static TransferFunctionCurve empty() {
      return new TransferFunctionCurve(new double[0]);
   }

   public static TransferFunctionCurve.Builder builder() {
      return new TransferFunctionCurve.Builder();
   }

   private TransferFunctionCurve(double[] v) {
      this.v = v;
   }

   public TransferFunction.Serializer<? extends TransferFunction> getSerializer() {
      return SERIALIZER;
   }

   public boolean isEmpty() {
      return this.v.length == 0;
   }

   public int size() {
      return this.v.length >> 1;
   }

   public double getInput(int index) {
      if (index >= 0 && index < this.size()) {
         return this.v[index];
      } else {
         throw new IndexOutOfBoundsException("Index out of range: " + index);
      }
   }

   public double getOutput(int index) {
      int len = this.v.length >> 1;
      if (index >= 0 && index < len) {
         return this.v[index + len];
      } else {
         throw new IndexOutOfBoundsException("Index out of range: " + index);
      }
   }

   public void removeAt(int index) {
      int len = this.v.length >> 1;
      if (index >= 0 && index < len) {
         if (len == 1) {
            this.v = new double[0];
         } else {
            double[] new_v = new double[len - 1 << 1];
            System.arraycopy(this.v, 0, new_v, 0, index);
            System.arraycopy(this.v, index + 1, new_v, index, len - index - 1);
            System.arraycopy(this.v, len, new_v, len - 1, index);
            System.arraycopy(this.v, len + index + 1, new_v, len + index - 1, len - index - 1);
            this.v = new_v;
         }
      } else {
         throw new IndexOutOfBoundsException("Index out of range: " + index);
      }
   }

   public int add(double input, double output) {
      int len = this.v.length >> 1;
      if (len == 0) {
         this.v = new double[]{input, output};
         return 0;
      } else {
         int index = Arrays.binarySearch(this.v, 0, len, input);
         if (index < 0) {
            index = -index - 1;
            this.insertAt(index, input, output);
            return index;
         } else {
            if (index > 0 && this.v[index - 1] == input) {
               --index;
            } else if (index >= len - 1 || this.v[index + 1] != input) {
               this.insertAt(index, input, this.v[index]);
               ++len;
            }

            double dv0 = Math.abs(this.v[index + len] - output);
            double dv1 = Math.abs(this.v[index + len + 1] - output);
            if (dv0 == dv1) {
               if (index > 0) {
                  dv0 = Math.abs(this.v[index + len - 1] - output);
               }

               if (index < len - 1) {
                  dv1 = Math.abs(this.v[index + len + 1] - output);
               }
            }

            if (dv0 < dv1) {
               this.v[index + len] = output;
               return index;
            } else {
               this.v[index + len + 1] = output;
               return index + 1;
            }
         }
      }
   }

   private void insertAt(int index, double input, double output) {
      int len = this.v.length >> 1;
      double[] new_v = new double[len + 1 << 1];
      System.arraycopy(this.v, 0, new_v, 0, index);
      new_v[index] = input;
      System.arraycopy(this.v, index, new_v, index + 1, len - index);
      System.arraycopy(this.v, len, new_v, len + 1, index);
      new_v[index + len + 1] = output;
      System.arraycopy(this.v, len + index, new_v, len + index + 2, len - index);
      this.v = new_v;
   }

   public boolean updateAt(int index, double input, double output) {
      int len = this.v.length >> 1;
      if (index >= 0 && index < len) {
         double preceding;
         if (input < this.v[index] && index > 0) {
            preceding = this.v[index - 1];
            if (input <= preceding) {
               input = preceding;
               if (index > 1 && this.v[index - 2] == preceding) {
                  return false;
               }
            }
         } else if (input > this.v[index] && index < len - 1) {
            preceding = this.v[index + 1];
            if (input >= preceding) {
               input = preceding;
               if (index < len - 2 && this.v[index + 2] == preceding) {
                  return false;
               }
            }
         }

         this.v[index] = input;
         this.v[index + len] = output;
         return true;
      } else {
         throw new IndexOutOfBoundsException("Index out of range: " + index);
      }
   }

   public double map(double input) {
      double previous = this.previousInput;
      this.previousInput = input;
      if (!Double.isNaN(previous) && !(input < previous)) {
         if (input > previous) {
            this.inputIncreasing = true;
         }
      } else {
         this.inputIncreasing = false;
      }

      int len = this.v.length >> 1;
      if (len == 0) {
         return input;
      } else if (len == 1) {
         return this.v[1];
      } else {
         int index = Arrays.binarySearch(this.v, 0, len, input);
         if (index < 0) {
            index = -index - 1;
            if (index == 0) {
               return this.v[len];
            } else if (index == len) {
               return this.v[2 * len - 1];
            } else {
               double input_t0 = this.v[index - 1];
               double input_t1 = this.v[index];
               double theta = (input_t1 - input) / (input_t1 - input_t0);
               return this.v[len + index - 1] * (1.0D - theta) + this.v[len + index] * theta;
            }
         } else {
            if (index > 0 && this.v[index - 1] == input) {
               if (this.inputIncreasing) {
                  --index;
               }
            } else if (index < len - 1 && this.v[index + 1] == input && !this.inputIncreasing) {
               ++index;
            }

            return this.v[index + len];
         }
      }
   }

   public boolean isPure() {
      return true;
   }

   public void forEach(TransferFunctionCurve.EntryConsumer consumer) {
      int len = this.v.length >> 1;

      for(int i = 0; i < len; ++i) {
         consumer.accept(this.v[i], this.v[i + len]);
      }

   }

   public TransferFunctionCurve clone() {
      return new TransferFunctionCurve((double[])this.v.clone());
   }

   public void drawPreview(MapWidgetTransferFunctionItem widget, MapCanvas view) {
      view.draw(MapFont.MINECRAFT, 0, 3, (byte)30, "Curve");
   }

   public void openDialog(TransferFunction.Dialog dialog) {
      dialog.addWidget((new MapWidgetButton() {
         public void onActivate() {
         }
      }).setText("Click").setBounds(5, 5, 80, 13));
   }

   public static class Builder {
      private final TransferFunctionCurve curve = TransferFunctionCurve.empty();

      public TransferFunctionCurve.Builder add(double input, double output) {
         this.curve.add(input, output);
         return this;
      }

      public TransferFunctionCurve build() {
         return this.curve;
      }
   }

   @FunctionalInterface
   public interface EntryConsumer {
      void accept(double var1, double var3);
   }
}
