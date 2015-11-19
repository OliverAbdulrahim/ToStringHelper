package functions;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

abstract class AbstractToStringHelper {

    final Map<String, Object> values;
    
    String name;
    
    Object target;
    
    AbstractToStringHelper() {
        this(Object.class);
    }
    
    AbstractToStringHelper(Object target) {
        Class<?> c = target.getClass();
        String name = c.getSimpleName();
        this.name = (name.isEmpty()) 
                ? c.getDeclaringClass() + "$(Anonymous)"
                : name;
        this.values = new TreeMap<>();
        this.target = target;
    }
    
    AbstractToStringHelper add(String tag, Object property) {
        values.put(tag, property);
        return this;
    }
    
    String get(String key) {
        Object value = values.get(key);
        return getAsString(value);
    }
    
    Set<Map.Entry<String, Object>> entrySet() {
        return values.entrySet();
    }
    
    @SuppressWarnings("unchecked")
    private <T> String getAsString(T obj) {
        String objToString = "";
        if (obj == null) {
            objToString = "null";
        }
        else if (obj == this) { // Nice try, pal
            objToString = "(this ToStringHelper)";
        }
        // Writing this broke my heart... why, primitive types, why?!
        else if (obj.getClass().isArray()) {
            if (obj instanceof Object[]) {
                objToString = wrapArray((T[]) obj);
            }
            else if (obj instanceof boolean[]) {
                objToString = Arrays.toString((boolean[]) obj);
            }
            else if (obj instanceof byte[]) {
                objToString = Arrays.toString((byte[]) obj);
            }
            else if (obj instanceof short[]) {
                objToString = Arrays.toString((short[]) obj);
            }
            else if (obj instanceof char[]) {
                objToString = Arrays.toString((char[]) obj);
            }
            else if (obj instanceof int[]) {
                objToString = Arrays.toString((int[]) obj);
            }
            else if (obj instanceof float[]) {
                objToString = Arrays.toString((float[]) obj);
            }
            else if (obj instanceof long[]) {
                objToString = Arrays.toString((long[]) obj);
            }
            else if (obj instanceof double[]) {
                objToString = Arrays.toString((double[]) obj);
            }
        }
        else {
            objToString = obj.toString();
        }
        return objToString;
    }

    private static <T> String wrapArray(T[] array) {
        boolean isMultidimensional = Stream.of(array)
                .allMatch(obj -> obj.getClass().isArray());
        return (isMultidimensional)
                ? Arrays.deepToString(array)
                : Arrays.toString(array);
    }
    
    @Override
    public abstract String toString();
    
}
