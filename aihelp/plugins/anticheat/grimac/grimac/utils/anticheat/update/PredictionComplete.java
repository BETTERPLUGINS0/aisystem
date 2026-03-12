package ac.grim.grimac.utils.anticheat.update;

import lombok.Generated;

public class PredictionComplete {
   private double offset;
   private PositionUpdate data;
   private boolean checked;
   private int identifier;

   public PredictionComplete(double offset, PositionUpdate update, boolean checked) {
      this.offset = offset;
      this.data = update;
      this.checked = checked;
   }

   @Generated
   public double getOffset() {
      return this.offset;
   }

   @Generated
   public PositionUpdate getData() {
      return this.data;
   }

   @Generated
   public boolean isChecked() {
      return this.checked;
   }

   @Generated
   public int getIdentifier() {
      return this.identifier;
   }

   @Generated
   public void setOffset(double offset) {
      this.offset = offset;
   }

   @Generated
   public void setData(PositionUpdate data) {
      this.data = data;
   }

   @Generated
   public void setChecked(boolean checked) {
      this.checked = checked;
   }

   @Generated
   public void setIdentifier(int identifier) {
      this.identifier = identifier;
   }
}
