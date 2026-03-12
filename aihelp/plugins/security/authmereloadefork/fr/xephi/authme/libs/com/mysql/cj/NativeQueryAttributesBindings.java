package fr.xephi.authme.libs.com.mysql.cj;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class NativeQueryAttributesBindings implements QueryAttributesBindings {
   Session session = null;
   private List<NativeQueryBindValue> bindAttributes = new ArrayList();

   public NativeQueryAttributesBindings(Session sess) {
      this.session = sess;
   }

   public void setAttribute(String name, Object value) {
      MysqlType defaultMysqlType = value == null ? MysqlType.NULL : (MysqlType)NativeQueryBindings.DEFAULT_MYSQL_TYPES.get(value.getClass());
      Object val = value;
      if (defaultMysqlType == null) {
         Optional<MysqlType> mysqlType = NativeQueryBindings.DEFAULT_MYSQL_TYPES.entrySet().stream().filter((m) -> {
            return ((Class)m.getKey()).isAssignableFrom(value.getClass());
         }).map((m) -> {
            return (MysqlType)m.getValue();
         }).findFirst();
         if (mysqlType.isPresent()) {
            defaultMysqlType = (MysqlType)mysqlType.get();
         } else {
            defaultMysqlType = MysqlType.VARCHAR;
            val = value.toString();
         }
      }

      NativeQueryBindValue bv = new NativeQueryBindValue(this.session);
      bv.setName(name);
      bv.setBinding(val, defaultMysqlType, 0, (AtomicBoolean)null);
      this.bindAttributes.add(bv);
   }

   public int getCount() {
      return this.bindAttributes.size();
   }

   public BindValue getAttributeValue(int index) {
      return (BindValue)this.bindAttributes.get(index);
   }

   public void runThroughAll(Consumer<BindValue> bindAttribute) {
      this.bindAttributes.forEach(bindAttribute::accept);
   }

   public void clearAttributes() {
      this.bindAttributes.clear();
   }
}
