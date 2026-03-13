package org.yaml.snakeyaml;

import java.io.StringWriter;
import java.io.Writer;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

/** @deprecated */
public class JavaBeanDumper {
   private boolean useGlobalTag;
   private DumperOptions.FlowStyle flowStyle;
   private DumperOptions options;
   private Representer representer;
   private final BeanAccess beanAccess;

   public JavaBeanDumper(boolean useGlobalTag, BeanAccess beanAccess) {
      this.useGlobalTag = useGlobalTag;
      this.beanAccess = beanAccess;
      this.flowStyle = DumperOptions.FlowStyle.BLOCK;
   }

   public JavaBeanDumper(boolean useGlobalTag) {
      this(useGlobalTag, BeanAccess.DEFAULT);
   }

   public JavaBeanDumper(BeanAccess beanAccess) {
      this(false, beanAccess);
   }

   public JavaBeanDumper() {
      this(BeanAccess.DEFAULT);
   }

   public JavaBeanDumper(Representer representer, DumperOptions options) {
      if (representer == null) {
         throw new NullPointerException("Representer must be provided.");
      } else if (options == null) {
         throw new NullPointerException("DumperOptions must be provided.");
      } else {
         this.options = options;
         this.representer = representer;
         this.beanAccess = null;
      }
   }

   public void dump(Object data, Writer output) {
      DumperOptions doptions;
      if (this.options == null) {
         doptions = new DumperOptions();
         if (!this.useGlobalTag) {
            doptions.setExplicitRoot(Tag.MAP);
         }

         doptions.setDefaultFlowStyle(this.flowStyle);
      } else {
         doptions = this.options;
      }

      Representer repr;
      if (this.representer == null) {
         repr = new Representer();
         repr.getPropertyUtils().setBeanAccess(this.beanAccess);
      } else {
         repr = this.representer;
      }

      Yaml dumper = new Yaml(repr, doptions);
      dumper.dump(data, output);
   }

   public String dump(Object data) {
      StringWriter buffer = new StringWriter();
      this.dump(data, buffer);
      return buffer.toString();
   }

   public boolean isUseGlobalTag() {
      return this.useGlobalTag;
   }

   public void setUseGlobalTag(boolean useGlobalTag) {
      this.useGlobalTag = useGlobalTag;
   }

   public DumperOptions.FlowStyle getFlowStyle() {
      return this.flowStyle;
   }

   public void setFlowStyle(DumperOptions.FlowStyle flowStyle) {
      this.flowStyle = flowStyle;
   }
}
