package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.paralithic.Expression;
import com.volmit.iris.util.paralithic.eval.parser.Parser;
import com.volmit.iris.util.paralithic.eval.parser.Scope;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.stream.ProceduralStream;
import com.volmit.iris.util.stream.interpolation.Interpolated;
import java.util.Iterator;
import lombok.Generated;

@Desc("Represents an Iris Expression")
public class IrisExpression extends IrisRegistrant {
   @ArrayType(
      type = IrisExpressionLoad.class,
      min = 1
   )
   @Desc("Variables to use in this expression")
   private KList<IrisExpressionLoad> variables = new KList();
   @ArrayType(
      type = IrisExpressionFunction.class,
      min = 1
   )
   @Desc("Functions to use in this expression")
   private KList<IrisExpressionFunction> functions = new KList();
   @Required
   @Desc("The expression. Inherited variables are x, y and z. Avoid using those variable names.")
   private String expression;
   private transient AtomicCache<Expression> expressionCache = new AtomicCache();
   private transient AtomicCache<ProceduralStream<Double>> streamCache = new AtomicCache();

   private Expression expression() {
      return (Expression)this.expressionCache.aquire(() -> {
         Scope var1 = new Scope();
         Parser var2 = new Parser();

         Iterator var3;
         try {
            var3 = this.variables.iterator();

            while(var3.hasNext()) {
               IrisExpressionLoad var4 = (IrisExpressionLoad)var3.next();
               var1.addInvocationVariable(var4.getName());
            }

            var1.addInvocationVariable("x");
            var1.addInvocationVariable("y");
            var1.addInvocationVariable("z");
         } catch (Throwable var6) {
            var6.printStackTrace();
            Iris.error("Script Variable load error in " + this.getLoadFile().getPath());
         }

         var3 = this.functions.iterator();

         while(var3.hasNext()) {
            IrisExpressionFunction var7 = (IrisExpressionFunction)var3.next();
            if (var7.isValid()) {
               var7.setData(this.getLoader());
               var2.registerFunction(var7.getName(), var7);
            }
         }

         try {
            return var2.parse(this.getExpression(), var1);
         } catch (Throwable var5) {
            var5.printStackTrace();
            Iris.error("Script load error in " + this.getLoadFile().getPath());
            return null;
         }
      });
   }

   public ProceduralStream<Double> stream(RNG rng) {
      return (ProceduralStream)this.streamCache.aquire(() -> {
         return ProceduralStream.of((var2, var3) -> {
            return this.evaluate(var1, var2, var3);
         }, (var2, var3, var4) -> {
            return this.evaluate(var1, var2, var3, var4);
         }, Interpolated.DOUBLE);
      });
   }

   public double evaluate(RNG rng, double x, double z) {
      double[] var6 = new double[3 + this.getVariables().size()];
      int var7 = 0;

      IrisExpressionLoad var9;
      for(Iterator var8 = this.getVariables().iterator(); var8.hasNext(); var6[var7++] = var9.getValue(var1, this.getLoader(), var2, var4)) {
         var9 = (IrisExpressionLoad)var8.next();
      }

      var6[var7++] = var2;
      var6[var7++] = var4;
      var6[var7] = -1.0D;
      return this.expression().evaluate(new IrisExpressionFunction.FunctionContext(var1), var6);
   }

   public double evaluate(RNG rng, double x, double y, double z) {
      double[] var8 = new double[3 + this.getVariables().size()];
      int var9 = 0;

      IrisExpressionLoad var11;
      for(Iterator var10 = this.getVariables().iterator(); var10.hasNext(); var8[var9++] = var11.getValue(var1, this.getLoader(), var2, var4, var6)) {
         var11 = (IrisExpressionLoad)var10.next();
      }

      var8[var9++] = var2;
      var8[var9++] = var4;
      var8[var9] = var6;
      return this.expression().evaluate(new IrisExpressionFunction.FunctionContext(var1), var8);
   }

   public String getFolderName() {
      return "expressions";
   }

   public String getTypeName() {
      return "Expression";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public IrisExpression() {
   }

   @Generated
   public IrisExpression(final KList<IrisExpressionLoad> variables, final KList<IrisExpressionFunction> functions, final String expression, final AtomicCache<Expression> expressionCache, final AtomicCache<ProceduralStream<Double>> streamCache) {
      this.variables = var1;
      this.functions = var2;
      this.expression = var3;
      this.expressionCache = var4;
      this.streamCache = var5;
   }

   @Generated
   public KList<IrisExpressionLoad> getVariables() {
      return this.variables;
   }

   @Generated
   public KList<IrisExpressionFunction> getFunctions() {
      return this.functions;
   }

   @Generated
   public String getExpression() {
      return this.expression;
   }

   @Generated
   public AtomicCache<Expression> getExpressionCache() {
      return this.expressionCache;
   }

   @Generated
   public AtomicCache<ProceduralStream<Double>> getStreamCache() {
      return this.streamCache;
   }

   @Generated
   public IrisExpression setVariables(final KList<IrisExpressionLoad> variables) {
      this.variables = var1;
      return this;
   }

   @Generated
   public IrisExpression setFunctions(final KList<IrisExpressionFunction> functions) {
      this.functions = var1;
      return this;
   }

   @Generated
   public IrisExpression setExpression(final String expression) {
      this.expression = var1;
      return this;
   }

   @Generated
   public IrisExpression setExpressionCache(final AtomicCache<Expression> expressionCache) {
      this.expressionCache = var1;
      return this;
   }

   @Generated
   public IrisExpression setStreamCache(final AtomicCache<ProceduralStream<Double>> streamCache) {
      this.streamCache = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getVariables());
      return "IrisExpression(variables=" + var10000 + ", functions=" + String.valueOf(this.getFunctions()) + ", expression=" + this.getExpression() + ", expressionCache=" + String.valueOf(this.getExpressionCache()) + ", streamCache=" + String.valueOf(this.getStreamCache()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisExpression)) {
         return false;
      } else {
         IrisExpression var2 = (IrisExpression)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label47: {
               KList var3 = this.getVariables();
               KList var4 = var2.getVariables();
               if (var3 == null) {
                  if (var4 == null) {
                     break label47;
                  }
               } else if (var3.equals(var4)) {
                  break label47;
               }

               return false;
            }

            KList var5 = this.getFunctions();
            KList var6 = var2.getFunctions();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            String var7 = this.getExpression();
            String var8 = var2.getExpression();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisExpression;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      KList var3 = this.getVariables();
      int var6 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      KList var4 = this.getFunctions();
      var6 = var6 * 59 + (var4 == null ? 43 : var4.hashCode());
      String var5 = this.getExpression();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }
}
