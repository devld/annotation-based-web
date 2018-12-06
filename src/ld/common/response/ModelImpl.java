package ld.common.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModelImpl implements Model {

    private Map<String, Object> data = new HashMap<>();

    @Override
    public Object get(String key) {
        return data.get(key);
    }

    @Override
    public void put(String key, Object value) {
        data.put(key, value);
    }

    @Override
    public boolean contains(String key) {
        return data.containsKey(key);
    }

    @Override
    public Set<String> keySet() {
        return data.keySet();
    }
}
