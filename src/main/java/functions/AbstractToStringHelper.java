package functions;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A mutable {@code String} representation of an arbitrary object.
 *
 * <p> This class imposes a single {@code abstract} method: {@link #toString()}.
 * The general contract of this method is to return the data represented by the
 * object using an arbitrary formatting sequence.
 *
 * @author Oliver Abdulrahim
 */
abstract class AbstractToStringHelper {

    /**
     * Specifies a default name used if one is not provided during construction.
     *
     * @see #AbstractToStringHelper()
     */
    private static final String DEFAULT_NAME = formatClass(Object.class);

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
    private final Optional<String> name;

    /**
     * Constructs an {@code AbstractToStringHelper} with the {@code Object}
     * class as the name.
     */
    AbstractToStringHelper() {
        this(DEFAULT_NAME);
    }

    /**
     * Constructs an {@code AbstractToStringHelper} with the given class as the
     * name.
     *
     * <p> If the given class is anonymous, this constructor appends
     * {@code "$Anonymous"} with the name of its super class.
     *
     * @param target The class that this helper represents.
     */
    AbstractToStringHelper(Class<?> target) {
        this(formatClass(target));
    }

    /**
     * Constructs an {@code AbstractToStringHelper} with the given name.
     */
    AbstractToStringHelper(String name) {
        this.name = Optional.ofNullable(name);
        this.values = new LinkedHashMap<>();
    }

// Mapping operations

    /**
     * Returns the name of this representation, or the {@link #DEFAULT_NAME
     * default name} if one was not specified at construction.
     *
     * @return The name of this representation.
     */
    String getName() {
        return name.orElse(DEFAULT_NAME);
    }

    /**
     * Associates the given property with the given tag within this
     * representation.
     *
     * @param tag The name to associate with the given property.
     * @param property The property to add with the given name.
     * @return This object (after the add operation is complete), allowing for
     *         chaining of operations.
     */
    AbstractToStringHelper add(String tag, Object property) {
        values.put(tag, property);
        return this;
    }

    /**
     * Adds all the entries in the given {@code Map} into this object
     */
    void addAll(Map<String, Object> values) {
        this.values.putAll(values);
    }

    /**
     * Returns an set view of the entries of tags and properties contained
     * within this object.
     *
     * @return An set containing the tag-property entries of this object.
     */
    Set<Map.Entry<String, Object>> entries() {
        return values.entrySet();
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
     * {@code null}. See the {@link #formatObject(java.lang.Object)} method for
     * a more formal description for the {@code String}s returned by this
     * method.
     *
     * @param tag The key whose associated value to return.
     * @return A formatted property mapped to the given tag.
     * @see #formatObject(java.lang.Object)
     */
    String get(String tag) {
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
        // Writing this broke my heart... why, primitive types, why?!
        else if (obj.getClass().isArray()) {
            if (obj instanceof Object[]) {
                objToString = wrapArray((Object[]) obj);
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
     * Wraps the given array into a {@code String}. If the array is
     * multidimensional, then the returned {@code String} will contain a
     * representation of the elements in the array.
     *
     * @param <T> The type of the elements in the array.
     * @param array The array to wrap with a {@code String}.
     * @return A {@code String} representation of the given array.
     */
    private static <T> String wrapArray(T[] array) {
        boolean isMultidimensional = Stream.of(array)
                .allMatch(obj -> obj.getClass().isArray());
        return (isMultidimensional)
                ? Arrays.deepToString(array)
                : Arrays.toString(array);
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
