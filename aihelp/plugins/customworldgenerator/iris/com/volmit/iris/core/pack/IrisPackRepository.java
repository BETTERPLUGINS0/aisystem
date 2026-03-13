package com.volmit.iris.core.pack;

import com.volmit.iris.Iris;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.jobs.DownloadJob;
import com.volmit.iris.util.scheduling.jobs.Job;
import com.volmit.iris.util.scheduling.jobs.JobCollection;
import com.volmit.iris.util.scheduling.jobs.SingleJob;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.Generated;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;

public class IrisPackRepository {
   private String user;
   private String repo;
   private String branch;
   private String tag;

   public static IrisPackRepository from(String g) {
      IrisPackRepository var2;
      if (var0.startsWith("https://github.com/")) {
         String var3 = var0.split("\\Qgithub.com/\\E")[1];
         var2 = builder().user(var3.split("\\Q/\\E")[0]).repo(var3.split("\\Q/\\E")[1]).build();
         if (var0.contains("/tree/")) {
            var2.setBranch(var0.split("/tree/")[1]);
         }

         return var2;
      } else if (var0.contains("/")) {
         String[] var1 = var0.split("\\Q/\\E");
         if (var1.length == 1) {
            return from(var0);
         } else if (var1.length == 2) {
            return builder().user(var1[0]).repo(var1[1]).build();
         } else if (var1.length >= 3) {
            var2 = builder().user(var1[0]).repo(var1[1]).build();
            if (var1[2].startsWith("#")) {
               var2.setTag(var1[2].substring(1));
            } else {
               var2.setBranch(var1[2]);
            }

            return var2;
         } else {
            return null;
         }
      } else {
         return builder().user("IrisDimensions").repo(var0).branch(var0.equals("overworld") ? "stable" : "master").build();
      }
   }

   public String toURL() {
      return !this.tag.trim().isEmpty() ? "https://codeload.github.com/" + this.user + "/" + this.repo + "/zip/refs/tags/" + this.tag : "https://codeload.github.com/" + this.user + "/" + this.repo + "/zip/refs/heads/" + this.branch;
   }

   public void install(VolmitSender sender, Runnable whenComplete) {
      File var3 = Iris.instance.getDataFolderNoCreate(new String[]{"packs", this.getRepo()});
      if (!var3.exists()) {
         File var4 = new File(Iris.getTemp(), "dltk-" + String.valueOf(UUID.randomUUID()) + ".zip");
         File var5 = new File(Iris.getTemp(), "extk-" + String.valueOf(UUID.randomUUID()));
         (new JobCollection(Form.capitalize(this.getRepo()), new Job[]{new DownloadJob(this.toURL(), var3), new SingleJob("Extracting", () -> {
            ZipUtil.unpack(var4, var5);
         }), new SingleJob("Installing", () -> {
            try {
               FileUtils.copyDirectory(var5.listFiles()[0], var3);
            } catch (IOException var3x) {
               var3x.printStackTrace();
            }

         })})).execute(var1, var2);
      } else {
         var1.sendMessage("Pack already exists!");
      }

   }

   @Generated
   private static String $default$user() {
      return "IrisDimensions";
   }

   @Generated
   private static String $default$repo() {
      return "overworld";
   }

   @Generated
   private static String $default$branch() {
      return "master";
   }

   @Generated
   private static String $default$tag() {
      return "";
   }

   @Generated
   IrisPackRepository(final String user, final String repo, final String branch, final String tag) {
      this.user = var1;
      this.repo = var2;
      this.branch = var3;
      this.tag = var4;
   }

   @Generated
   public static IrisPackRepository.IrisPackRepositoryBuilder builder() {
      return new IrisPackRepository.IrisPackRepositoryBuilder();
   }

   @Generated
   public String getUser() {
      return this.user;
   }

   @Generated
   public String getRepo() {
      return this.repo;
   }

   @Generated
   public String getBranch() {
      return this.branch;
   }

   @Generated
   public String getTag() {
      return this.tag;
   }

   @Generated
   public void setUser(final String user) {
      this.user = var1;
   }

   @Generated
   public void setRepo(final String repo) {
      this.repo = var1;
   }

   @Generated
   public void setBranch(final String branch) {
      this.branch = var1;
   }

   @Generated
   public void setTag(final String tag) {
      this.tag = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisPackRepository)) {
         return false;
      } else {
         IrisPackRepository var2 = (IrisPackRepository)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label59: {
               String var3 = this.getUser();
               String var4 = var2.getUser();
               if (var3 == null) {
                  if (var4 == null) {
                     break label59;
                  }
               } else if (var3.equals(var4)) {
                  break label59;
               }

               return false;
            }

            String var5 = this.getRepo();
            String var6 = var2.getRepo();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            String var7 = this.getBranch();
            String var8 = var2.getBranch();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            String var9 = this.getTag();
            String var10 = var2.getTag();
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
      return var1 instanceof IrisPackRepository;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      String var3 = this.getUser();
      int var7 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      String var4 = this.getRepo();
      var7 = var7 * 59 + (var4 == null ? 43 : var4.hashCode());
      String var5 = this.getBranch();
      var7 = var7 * 59 + (var5 == null ? 43 : var5.hashCode());
      String var6 = this.getTag();
      var7 = var7 * 59 + (var6 == null ? 43 : var6.hashCode());
      return var7;
   }

   @Generated
   public String toString() {
      String var10000 = this.getUser();
      return "IrisPackRepository(user=" + var10000 + ", repo=" + this.getRepo() + ", branch=" + this.getBranch() + ", tag=" + this.getTag() + ")";
   }

   @Generated
   public static class IrisPackRepositoryBuilder {
      @Generated
      private boolean user$set;
      @Generated
      private String user$value;
      @Generated
      private boolean repo$set;
      @Generated
      private String repo$value;
      @Generated
      private boolean branch$set;
      @Generated
      private String branch$value;
      @Generated
      private boolean tag$set;
      @Generated
      private String tag$value;

      @Generated
      IrisPackRepositoryBuilder() {
      }

      @Generated
      public IrisPackRepository.IrisPackRepositoryBuilder user(final String user) {
         this.user$value = var1;
         this.user$set = true;
         return this;
      }

      @Generated
      public IrisPackRepository.IrisPackRepositoryBuilder repo(final String repo) {
         this.repo$value = var1;
         this.repo$set = true;
         return this;
      }

      @Generated
      public IrisPackRepository.IrisPackRepositoryBuilder branch(final String branch) {
         this.branch$value = var1;
         this.branch$set = true;
         return this;
      }

      @Generated
      public IrisPackRepository.IrisPackRepositoryBuilder tag(final String tag) {
         this.tag$value = var1;
         this.tag$set = true;
         return this;
      }

      @Generated
      public IrisPackRepository build() {
         String var1 = this.user$value;
         if (!this.user$set) {
            var1 = IrisPackRepository.$default$user();
         }

         String var2 = this.repo$value;
         if (!this.repo$set) {
            var2 = IrisPackRepository.$default$repo();
         }

         String var3 = this.branch$value;
         if (!this.branch$set) {
            var3 = IrisPackRepository.$default$branch();
         }

         String var4 = this.tag$value;
         if (!this.tag$set) {
            var4 = IrisPackRepository.$default$tag();
         }

         return new IrisPackRepository(var1, var2, var3, var4);
      }

      @Generated
      public String toString() {
         return "IrisPackRepository.IrisPackRepositoryBuilder(user$value=" + this.user$value + ", repo$value=" + this.repo$value + ", branch$value=" + this.branch$value + ", tag$value=" + this.tag$value + ")";
      }
   }
}
