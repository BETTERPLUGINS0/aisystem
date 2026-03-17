package advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type;

import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import java.util.ArrayList;
import java.util.List;

public class ScriptKeyframe extends AbstractKeyframe<List<ScriptKeyframe.Script>> {
   private final List<ScriptKeyframe.Script> script = new ArrayList();

   public List<ScriptKeyframe.Script> getValue(int var1, IAnimationProperty var2) {
      return this.script;
   }

   public List<ScriptKeyframe.Script> getScript() {
      return this.script;
   }

   public static record Script(String reader, String script) {
      public Script(String reader, String script) {
         this.reader = var1;
         this.script = var2;
      }

      public static ScriptKeyframe.Script from(String var0) {
         String[] var1 = var0.split(":", 2);
         return var1.length <= 1 ? new ScriptKeyframe.Script("imodel", var1[0]) : new ScriptKeyframe.Script(var1[0], var1[1]);
      }

      public String reader() {
         return this.reader;
      }

      public String script() {
         return this.script;
      }
   }
}
