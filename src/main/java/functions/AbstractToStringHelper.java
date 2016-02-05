package functions;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A mutable {@code String} representation of an arbitrary object.
 *
 * <p> This class imposes a single {@code abstract} method: {@link #toString()}.
 * The general contract of this method is to return the data represented by the
 * object using an arbitrary formatting sequence.
 *
 * @author Oliver Abdulrahim
 */
public abstract class AbstractToStringHelper {

    /**
     * Maps {@code String} tags to {@code Object} properties. The keys stored in
     * this {@code Map} are formatted with respect to their properties as
     * specified in the {@link #toString()} method for this class.
     */
    private final Map<String, Object> values;

    /**
     * The name of this representation as specified during construction. By
     * default, this is the name of the {@code Object} class.
     */
    private final Object target;

    /**
     * Constructs an {@code AbstractToStringHelper} with the given class as the
     * name.
     *
     * <p> If the given class is anonymous, this constructor appends
     * {@code "$Anonymous"} with the name of its super class.
     *
     * @param target The object that this helper represents.
     */
    protected AbstractToStringHelper(Object target) {
        this.target = target;
        this.values = new LinkedHashMap<>();
    }

// Mapping operations

    /**
     * Returns a {@code String} containing the name of the class of this
     * representation.
     *
     * @return The name of this representation.
     */
    protected String name() {
        return formatClass(target.getClass());
    }

    /**
     * Adds all the entries in the given {@code Map} into this object
     */
    protected void addAll(Map<String, Object> values) {
        this.values.putAll(values);
    }

// Formatting operations

    /**
     * Specifies formatting for the name of a given class, returning a
     * {@code String} essentially containing the simple name of the given class.
     *
     * <p> If the given class is anonymous, this method returns a {@code String}
     * with the characters {@code "$Anonymous"} prepended with the name of the
     * super class with a maximum depth of one.
     *
     * @param c The class to convert to a {@code String}.
     * @return A {@code String} formatted to contain the name of the given
     *         class.
     */
    private static String formatClass(Class<?> c) {
        String name = c.getCanonicalName();
        // If the canonical name of the class is null, then the given class is
        // anonymous.
        if (name == null) {
            Class<?> superClass = c.getSuperclass();
            name = superClass.getCanonicalName() + "$Anonymous";
        }
        // Removes any package names - what remains is essentially the simple
        // name of the class.
        name = name.substring(name.lastIndexOf('.') + 1);
        return name;
    }

    /**
     * Returns a {@code String} representation of the property, or value, mapped
     * to the given tag, or key. Wraps the given value if it is an array or
     * {@code null}.
     *
     * @param tag The key whose associated value to return.
     * @return A formatted property mapped to the given tag.
     * @see #formatObject(Object)
     */
    public String get(String tag) {
        Object value = values.get(tag);
        return formatObject(value);
    }

    /**
     * Returns a {@code String} representation of the given object, formatting
     * as necessary.
     *
     * <p> If the given object is an array, then this method returns a
     * comma-separated view of the elements contained within it. If the given
     * object is {@code null}, then this method returns a {@code String} with
     * the characters {@code "null"}. Otherwise, returns the value returned by
     * the objects {@code toString} method.
     *
     * @param obj The object to convert to a {@code String}.
     * @return A {@code String} representation of the given object.
     */
    private String formatObject(Object obj) {
        String objToString = "";
        if (obj == null) {
            objToString = "null";
        }
        else if (obj == this) {
            objToString = "(this ToStringHelper)";
        }
        else if (obj instanceof String) {
            objToString = '\"' + obj.toString() + '\"';
        }
        // Writing this broke my heart... why, primitive types, why?!
        else if (obj.getClass().isArray()) {
            if (obj instanceof Object[]) {
                objToString = Arrays.deepToString((Object[]) obj);
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

    /**
     * Returns a {@code String} containing the names of all fields in the target
     * object paired with their data.
     *
     * @param mapper The function to format each entry by.
     * @return A {@code String} containing the names of all fields in the target
     *         object paired with their data.
     */
    protected String formatEntries(
            Function<? super Entry<String, Object>, String> mapper)
    {
        return values.entrySet()
                .stream()
                .map(mapper)
                .collect(Collectors.joining(", "));
    }

// Abstract methods

    /**
     * Constructs and returns a {@code String} representation of the object,
     * which includes values wrapped by this one.
     *
     * <p> The general contract of this method is to return the data represented
     * by the object using an arbitrary formatting sequence.
     *
     * @return A {@code String} representation of the object.
     */
    @Override
    public abstract String toString();

}
