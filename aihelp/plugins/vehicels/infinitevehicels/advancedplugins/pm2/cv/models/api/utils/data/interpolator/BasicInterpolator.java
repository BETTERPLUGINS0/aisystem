package advancedplugins.pm2.cv.models.api.utils.data.interpolator;

public class BasicInterpolator<IN> extends Interpolator<IN, IN> {
   public BasicInterpolator() {
      this.setParseFunc((var0) -> {
         return var0;
      });
   }
}
