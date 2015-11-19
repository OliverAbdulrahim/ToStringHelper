package functions;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoToStringHelper
    extends AbstractToStringHelper 
{
    
    public AutoToStringHelper(Object target) {
        super(target);
        scrapeContents();
    }

    private void scrapeContents() {
        Field[] targetFields = target.getClass().getDeclaredFields();
        Map<String, Object> upstream = Stream.of(targetFields)
                .collect(Collectors.toMap(Field :: getName, this :: getValue));
        values.putAll(upstream);
    }
    
    private Object getValue(final Field f) {
        f.setAccessible(true);
        Object value = null;
        try {
            value = f.get(target);
        } 
        catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(AutoToStringHelper.class.getName()).log(Level.SEVERE, 
                    "Could not retrieve value field = " + f, ex);
        }
        return value;
    }
    
    @Override
    public String toString() {
        String mappedValues = entrySet()
                .stream()
                .map(entry -> entry.getKey() + '=' + get(entry.getKey()))
                .collect(Collectors.joining(", "));
        return name + '{' + mappedValues + '}';
    }
    
}
