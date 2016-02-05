package functions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The {@code ReflectionUtilities} class contains utility methods that perform
 * reflective operations.
 *
 * <p> Notable operations include:
 *   <ul>
 *     <li> {@link #invoke(Object, Method, Object...)}, which wraps the
 *          invocation of a given {@code Method}. Exists because
 *          {@code RuntimeException} is ugly.
 *     <li> {@link #newInstance(Class)}, which facilitates for creating
 *          instances from a generic type.
 *     <li> {@link #mapFields(Class, Function, Function)}, which wraps the
 *          creation of {@code Map} objects from the fields declared in the
 *          given {@code Class}.
 *   </ul>
 *
 * @author Oliver Abdulrahim
 */
public final class ReflectionUtilities {

    private static final Logger LOG =
            Logger.getLogger(ReflectionUtilities.class.getName());

    /**
     * Invokes the given method from the given object using the given (optional)
     * arguments, returning
     *
     * @param obj The object to invoke the method on. This value may be
     *        {@code null} if the supplied method is {@code static}.
     * @param m The method to invoke using the given data.
     * @param args The arguments to invoke the given method with. This value is
     *        not required to be supplied.
     * @return The return value of the method, or {@code null} if the method is
     *         {@code void}.
     */
    public static Object invoke(Object obj, Method m, Object... args) {
        try {
            m.setAccessible(true);
            return m.invoke(obj, args);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Sets the value of the given field to the given value within the given
     * object.
     *
     * @param obj The object the given {@code Field} is declared in. This value
     *        may be {@code null} if the {@code Field} is {@code static}.
     * @param f The {@code Field} whose value to set to the given
     *        {@code Object}.
     * @param value The value to assign to the given {@code Field}.
     */
    public static void set(Object obj, Field f, Object value) {
        try {
            f.setAccessible(true);
            f.set(obj, value);
        }
        catch (IllegalAccessException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates and returns an instance of the given {@code Class}.
     *
     * @param c The {@code class} of the instance to create.
     * @param <T> The type of the instance to create.
     * @return An instance of the given {@code class}.
     */
    public static <T> T newInstance(Class<? extends T> c) {
        T obj = null;
        try {
            obj = c.newInstance();
        }
        catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ReflectionUtilities.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return obj;
    }

    /**
     * Returns the value stored within the given {@code Field}. Primitive values
     * are wrapped in their respective object before they are returned.
     *
     * @param obj The object from which to extract the value of the given
     *        {@code Field} in. This value may be {@code null} if the given
     *        {@code Field} is declared static.
     * @param f The field whose value to retrieve.
     * @return The value stored within the given {@code Field}.
     */
    public static Object getValue(Object obj, Field f) {
        Object value = null;
        try {
            f.setAccessible(true);
            value = f.get(obj);
        }
        catch (IllegalAccessException ex) {
            Logger.getLogger(ReflectionUtilities.class.getName())
                    .log(Level.SEVERE, "Can't access field = " + f, ex);
        }
        return value;
    }

    /**
     * Maps all fields contained within the given {@code Class} using the given
     * key and value mapping functions.
     *
     * @param c The class whose values to map.
     * @param keyMapper A mapping function to produce keys for the new
     *        {@code Map}.
     * @param valueMapper A mapping function to produce values for the new
     *        {@code Map}.
     * @param <K> The output type of the key mapping function, and type of the
     *        keys of the returned {@code Map}.
     * @param <V> The output type of the value mapping function, and the type of
     *        the values of the returned {@code Map}.
     * @return A {@code Map} whose keys and values are the result of the given
     *         functions and the fields of the given {@code Class}.
     */
    public static <K, V> Map<K, V> mapFields(
            Class<?> c,
            Function<? super Field, K> keyMapper,
            Function<? super Field, V> valueMapper)
    {
        return fields(c)
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * Returns a non-parallel {@code Stream} containing the fields declared in
     * the given {@code Class}.
     *
     * @param c The {@code Class} whose fields to populate a {@code Stream}
     *        with.
     * @return A {@code Stream} of the fields declared in the given
     *         {@code Class}.
     */
    public static Stream<Field> fields(Class<?> c) {
        return fields(c, dummyPredicate());
    }

    /**
     * Returns a non-parallel {@code Stream} containing the fields declared in
     * the given {@code Class}, filtered by the given {@code Predicate}.
     *
     * @param c The class whose fields to {@code Stream}.
     * @param finisher The filter to apply after constructing the
     *        {@code Stream}.
     * @return A {@code Stream} of the fields declared in the given
     *         {@code Class}.
     */
    public static Stream<Field> fields(
            Class<?> c,
            Predicate<? super Field> finisher)
    {
        return streamFor(c.getDeclaredFields(), finisher);
    }

    /**
     * Returns a non-parallel {@code Stream} containing the methods declared in
     * the given {@code Class}.
     *
     * @param c The {@code Class} whose methods to populate a {@code Stream}
     *        with.
     * @return A {@code Stream} of the methods declared in the given
     *         {@code Class}.
     */
    public static Stream<Method> methods(Class<?> c) {
        return methods(c, dummyPredicate());
    }

    /**
     * Returns a non-parallel {@code Stream} containing the methods declared in
     * the given {@code Class}.
     *
     * @param c The {@code Class} whose methods to populate a {@code Stream}
     *        with.
     * @return A {@code Stream} of the methods declared in the given
     *         {@code Class}.
     */
    public static Stream<Method> methods(
            Class<?> c,
            Predicate<? super Method> finisher)
    {
        Method[] methods =
                (c.isInterface()) ? c.getMethods() : c.getDeclaredMethods();
        return streamFor(methods, finisher);
    }

    /**
     * Returns {@code true} if the given {@code Method} is considered to be a
     * "getter," {@code false} otherwise.
     *
     * @param m The method to test.
     * @return {@code true} if the given {@code Method} is a getter,
     *         {@code false} otherwise.
     * @implSpec This implementation simply tests if the substring {@code "get"}
     *           prefixes the given method's name.
     */
    public static boolean isGetter(Method m) {
        return startsWith(m, "get");
    }

    /**
     * Returns {@code true} if the given {@code Method} is considered to be a
     * "setter," {@code false} otherwise.
     *
     * @param m The method to test.
     * @return {@code true} if the given {@code Method} is a setter,
     *         {@code false} otherwise.
     * @implSpec This implementation simply tests if the substring {@code "set"}
     *           prefixes the given method's name.
     */
    public static boolean isSetter(Method m) {
        return startsWith(m, "set");
    }

    /**
     * Returns {@code true} if the given method's name starts with the given
     * {@code prefix}, {@code false} otherwise.
     *
     * @param m The method to test.
     * @param prefix The substring to test.
     * @return {@code true} if the given method's name starts with the given
     *         {@code prefix}, {@code false} otherwise.
     */
    public static boolean startsWith(Method m, String prefix) {
        return m.getName().startsWith(prefix);
    }

    /**
     * Returns the {@code Method} determined to be the getter for the given
     * {@code Field}.
     *
     * @param c The {@code Class} whose methods to introspect.
     * @param f The {@code Field} whose getter to find.
     * @return The {@code Method} determined to be the getter for the given
     *         {@code Field}.
     * @throws NoSuchMethodError if there is no {@code Method} determined to be
     *         the getter for the given {@code Field}.
     * @implSpec This implementation simply tests if the substring {@code "get"}
     *           prefixes the given method's name.
     */
    public static Method getterFor(Class<?> c, Field f) {
        return methods(c)
                .filter(ReflectionUtilities :: isGetter)
                .filter(method -> method.getReturnType().equals(f.getType()))
                .findFirst()
                .orElseThrow(NoSuchMethodError :: new);
    }

    /**
     * Returns a {@code Stream} containing the elements of the given array,
     * filtered by the given {@code Predicate}.
     *
     * @param data The array whose elements to stream.
     * @param finisher The filter to apply after constructing the
     *        {@code Stream}.
     * @param <T> The type of the elements in the array and the type of the
     *        elements in the returned {@code Stream}.
     * @return A {@code Stream} containing the elements of the given array.
     */
    private static <T> Stream<T> streamFor(
            T[] data,
            Predicate<? super T> finisher)
    {
        return Arrays
                .stream(data)
                .filter(finisher);
    }

    /**
     * Returns a non-operative {@code Predicate} whose
     * {@link Predicate#test(Object) test method} returns {@code true} for any
     * argument.
     *
     * @param <T> The type of the input of the {@code Predicate}.
     * @return A {@code Predicate} that returns {@code true} for any and all
     *         arguments.
     */
    private static <T> Predicate<? super T> dummyPredicate() {
        return no_op -> true;
    }

    /**
     * Don't let anyone instantiate this class.
     */
    private ReflectionUtilities() {
        throw new InstantiationError("No instances allowed, pal!");
    }

}
