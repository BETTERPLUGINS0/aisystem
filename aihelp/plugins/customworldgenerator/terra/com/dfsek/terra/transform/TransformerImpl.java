package com.dfsek.terra.transform;

import com.dfsek.terra.api.transform.Transform;
import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.api.transform.Validator;
import com.dfsek.terra.api.transform.exception.AttemptsFailedException;
import com.dfsek.terra.api.transform.exception.TransformException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class TransformerImpl<F, T> implements Transformer<F, T> {
   private final LinkedHashMap<Transform<F, T>, List<Validator<T>>> transformers;

   private TransformerImpl(LinkedHashMap<Transform<F, T>, List<Validator<T>>> transformer) {
      this.transformers = transformer;
   }

   public T translate(F from) {
      List<Throwable> exceptions = new ArrayList();
      Iterator var3 = this.transformers.entrySet().iterator();

      while(var3.hasNext()) {
         Entry transform = (Entry)var3.next();

         try {
            T result = ((Transform)transform.getKey()).transform(from);
            Iterator var6 = ((List)transform.getValue()).iterator();

            Validator validator;
            do {
               if (!var6.hasNext()) {
                  return result;
               }

               validator = (Validator)var6.next();
            } while(validator.validate(result));

            throw new TransformException("Failed to validate result: " + result.toString());
         } catch (Exception var8) {
            exceptions.add(var8);
         }
      }

      throw new AttemptsFailedException("Could not transform input; all attempts failed: " + from.toString() + "\n", exceptions);
   }

   public static final class Builder<F, T> {
      private final LinkedHashMap<Transform<F, T>, List<Validator<T>>> transforms = new LinkedHashMap();

      @SafeVarargs
      public final TransformerImpl.Builder<F, T> addTransform(Transform<F, T> transform, Validator<T>... validators) {
         this.transforms.put(transform, Arrays.asList(validators));
         return this;
      }

      public TransformerImpl<F, T> build() {
         return new TransformerImpl(this.transforms);
      }
   }
}
