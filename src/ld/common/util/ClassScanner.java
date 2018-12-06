package ld.common.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ClassScanner {

    public static Set<Class<?>> findClasses(String packageName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            throw new IllegalStateException();
        }
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> files = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            files.add(new File(url.getFile()));
        }
        Set<Class<?>> classes = new HashSet<>();
        for (File dir : files) {
            classes.addAll(findClasses(dir, packageName));
        }
        return classes;
    }

    private static Set<Class<?>> findClasses(File dir, String packageName) throws ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        if (!dir.isDirectory()) {
            return classes;
        }
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.exists()) {
                    continue;
                }
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.isFile()) {
                    if (file.getName().endsWith(".class")) {
                        String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                        classes.add(Class.forName(className));
                    }
                }
            }
        }
        return classes;
    }
}
