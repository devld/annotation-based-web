package ld.common.response;

import java.util.Set;

public interface Model {

    Object get(String key);

    void put(String key, Object value);

    boolean contains(String key);

    Set<String> keySet();

}
