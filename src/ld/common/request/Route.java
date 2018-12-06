package ld.common.request;

import ld.common.util.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Route implements Comparable<Route> {

    private final String urlPattern;

    private final HttpMethod[] methods;

    private Pattern pattern;
    private List<String> keys;

    Route(String urlPattern, HttpMethod[] methods) {
        if (Utils.isEmptyStr(urlPattern)) {
            throw new IllegalArgumentException();
        }
        this.urlPattern = urlPattern;
        compilePattern(urlPattern);
        if (methods == null) {
            this.methods = new HttpMethod[]{};
        } else {
            this.methods = Arrays.copyOf(methods, methods.length);
        }
    }

    public boolean matched(HttpMethod method, String url) {
        boolean flag = false;
        if (this.methods.length == 0) {
            flag = true;
        } else {
            for (HttpMethod m : methods) {
                if (m == method) {
                    flag = true;
                    break;
                }
            }
        }
        return flag && pattern.matcher(url).find();
    }

    public Map<String, String> parseUrlParams(String url) {
        Matcher matcher = pattern.matcher(url);
        Map<String, String> params = new HashMap<>();
        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                params.put(keys.get(i - 1), matcher.group(i));
            }
        }
        return params;
    }

    private void compilePattern(String urlPattern) {
        String temp = urlPattern.replaceAll("\\(", "\\(");
        temp = temp.replaceAll("\\)", "\\)");
        Pattern pattern = Pattern.compile("\\{([A-z0-9]+)}");
        Matcher matcher = pattern.matcher(temp);
        List<String> paramKeys = new ArrayList<>();
        while (matcher.find()) {
            paramKeys.add(matcher.group(1));
        }
        this.keys = paramKeys;
        temp = "^" + temp.replaceAll("\\{[A-z0-9]+}", "([^/]+?)") + "/?$";
        this.pattern = Pattern.compile(temp);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Route)) {
            return false;
        }
        Route route = (Route) obj;
        return urlPattern.equals(route.urlPattern) && Arrays.equals(methods, route.methods);
    }

    @Override
    public String toString() {
        return Arrays.toString(methods) + " " + urlPattern;
    }

    @Override
    public int hashCode() {
        int result = urlPattern.hashCode();
        result = 31 * result + Arrays.hashCode(methods);
        return result;
    }

    @Override
    public int compareTo(Route o) {
        return -Integer.compare(Utils.countMatches(this.urlPattern, "/"), Utils.countMatches(o.urlPattern, "/"));
    }
}
