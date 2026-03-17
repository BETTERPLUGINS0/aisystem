/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PathUtil {
    private static final Pattern pattern = Pattern.compile("[^\\\\](\\.)");
    private static final Pattern indexPattern = Pattern.compile(".*\\[(-?[0-9]+)\\]");

    public static List<PathSegment> splitPath(String string) {
        ArrayList<PathSegment> arrayList = new ArrayList<PathSegment>();
        Matcher matcher = pattern.matcher(string);
        int n = 0;
        while (matcher.find(n)) {
            arrayList.add(new PathSegment(string.substring(n, matcher.end() - 1).replace("\\.", ".")));
            n = matcher.end();
        }
        arrayList.add(new PathSegment(string.substring(n).replace("\\.", ".")));
        return arrayList;
    }

    public static class PathSegment {
        private final String path;
        private final Integer index;

        private PathSegment(String string) {
            Matcher matcher = indexPattern.matcher(string);
            if (matcher.find()) {
                this.path = string.substring(0, string.indexOf("["));
                this.index = Integer.parseInt(matcher.group(1));
            } else {
                this.path = string;
                this.index = null;
            }
        }

        public String getPath() {
            return this.path;
        }

        public int getIndex() {
            return this.index;
        }

        public boolean hasIndex() {
            return this.index != null;
        }

        public String toString() {
            return "PathSegment [path=" + this.path + ", index=" + this.index + "]";
        }
    }
}

