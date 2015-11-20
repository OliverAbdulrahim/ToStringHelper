package functions;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An immutable {@code String} representation of an given object. 
 * 
 * <p> This class allows for the specification of a target object during 
 * construction. The fields of this object are mapped to their respective values
 * automatically.
 * 
 * @author Oliver Abdulrahim
 */
public class AutoToStringHelper
    extends AbstractToStringHelper 
{
    
    /**
     * The object that this helper represents.
     */
    private final Object target;
    
    /**
     * Constructs an {@code AutoToStringHelper} with the given object as the
     * target. 
     * 
     * <p> This constructor retrieves all fields from the given object using 
     * type introspection and automatically adds them into the structure.
     * 
     * @param target The name for this representation.
     */
    public AutoToStringHelper(Object target) {
        super(target.getClass());
        this.target = target;
        introspectFields();
    }
    
    /**
     * Adds all fields contained within the {@link #target} object using type 
     * introspection and maps their names to their respective values.
     */
    private void introspectFields() {
        Field[] targetFields = target.getClass().getDeclaredFields();
        Map<String, Object> upstream = Stream.of(targetFields)
                .collect(Collectors.toMap(Field :: getName, this :: getValue));
        values.putAll(upstream);
    }
    
    /**
     * Returns the value stored within the given field, setting it to accessible
     * if necessary.
     * 
     * @param f The field whose data to return.
     * @return The value stored within the given field
     */
    private Object getValue(final Field f) {
        Object value = null;
        try {
            f.setAccessible(true);
            value = f.get(target);
        } 
        catch (IllegalAccessException | IllegalArgumentException ex) {
            Logger.getLogger(AutoToStringHelper.class.getName()).log(
                    Level.SEVERE, "No value or inaccessible field = " + f, ex);
        }
        return value;
    }
    
    /**
     * Returns a {@code String} representation of the target object.
     * 
     * @return A formatted {@code String} containing all fields paired with 
     *         their data of the object specified during construction.
     */
    @Override
    public String toString() {
        String mappedValues = entries()
                .stream()
                .map(entry -> entry.getKey() + " = " + get(entry.getKey()))
                .collect(Collectors.joining(", "));
        return name + '{' + mappedValues + '}';
    }

}
