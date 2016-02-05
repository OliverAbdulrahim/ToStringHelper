package functions;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * An immutable {@code String} representation of a given object. Objects of this
 * class wrap the data of an object specified during construction automatically
 * using type introspection.
 *
 * <p> As an example, consider the {@code Person} class outlined below:
 *
 * <pre>{@code
 *     class Person {
 *
 *         enum Gender {
 *             MALE, FEMALE;
 *         }
 *
 *         private final String name;
 *
 *         private final Gender gender;
 *
 *         private final int age;
 *
 *         Person(String name, Gender gender, int age) {
 *             this.name = name;
 *             this.gender = gender;
 *             this.age = age;
 *         }
 *
 *     }
 * }</pre>
 *
 * <p> Now consider the following code that constructs an object of this class
 * using a {@code Person} object as the target:
 *
 * <pre>{@code
 *     Person p = new Person("Daniel", Person.Gender.MALE, 18);
 *     ReflectiveToStringHelper auto = new ReflectiveToStringHelper(p);
 *     System.out.println(auto);
 * }</pre>
 *
 * During construction, the {@code auto} object automatically maps the
 * identifiers of the all fields in {@code p} to their values. The above would
 * print the following:
 *
 * <pre>{@code
 *     Person{gender = MALE, name = Daniel, age = 18}
 * }</pre>
 *
 * It is therefore possible to trivially override the {@code toString} method in
 * the {@code Person} class as such using an {@code ReflectiveToStringHelper}:

* <pre>{@code
 *     class Person {
 *         ...
 *         ＠Override
 *         public String toString() {
 *             return new ReflectiveToStringHelper(this).toString();
 *         }
 *         ...
 *     }
 * }</pre>
 *
 * @author Oliver Abdulrahim
 */
public class ReflectiveToStringHelper
    extends AbstractToStringHelper
{

    /**
     * The object that this helper represents.
     */
    private final Object target;

    /**
     * Constructs an {@code ReflectiveToStringHelper} with the given object as the
     * target.
     *
     * <p> This constructor retrieves all fields from the given object using
     * type introspection and automatically adds them into the structure.
     *
     * @param target The name for this representation.
     */
    public ReflectiveToStringHelper(Object target) {
        super(target.getClass());
        this.target = target;
        introspectFields();
    }

// Internal operations

    /**
     * Adds all fields contained within the {@link #target} object using type
     * introspection and maps their names to their respective values.
     */
    private void introspectFields() {
        Field[] targetFields = target.getClass().getDeclaredFields();
        Map<String, Object> upstream = Arrays
                .stream(targetFields)
                .collect(Collectors.toMap(Field :: getName, this :: getValue));
        addAll(upstream);
    }

    /**
     * Returns the value stored within the given field, setting it to accessible
     * if necessary.
     *
     * @param f The field whose data to return.
     * @return The value stored within the given field.
     */
    private Object getValue(Field f) {
        Object value = null;
        try {
            f.setAccessible(true);
            value = f.get(target);
        }
        catch (IllegalAccessException ex) {
            Logger.getLogger(ReflectiveToStringHelper.class.getName())
                    .log(Level.SEVERE, "Can't access field = " + f, ex);
        }
        return value;
    }

// Override methods

    /**
     * Returns a {@code String} representation of the target object.
     *
     * @return A formatted {@code String} containing all fields paired with
     *         their data of the object specified during construction.
     */
    @Override
    public String toString() {
        String mappedEntries = entries()
                .stream()
                .map(entry -> entry.getKey() + " = " + get(entry.getKey()))
                .collect(Collectors.joining(", "));
        return getName() + '{' + mappedEntries + '}';
    }

}