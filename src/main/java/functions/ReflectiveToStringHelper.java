package functions;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

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
    private Object target;

    /**
     * Constructs a {@code ReflectiveToStringHelper} with the given object as
     * the target.
     *
     * <p> This constructor retrieves all fields from the given object using
     * type introspection and automatically adds them into the structure.
     *
     * @param target The name for this representation.
     */
    public ReflectiveToStringHelper(Object target) {
        super(target);
        Map<String, Object> upstream = namesToValues();
        addAll(upstream);
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
        String entries = toString(e -> e.getKey() + '=' + get(e.getKey()));
        return name() + '{' + entries + '}';
    }

    /**
     * Returns a {@code String} representation of the target object, formatted
     * using the specified {@code Function}.
     *
     * @param mapper The formatting function.
     * @return A {@code String} representation of the target object.
     */
    public String toString(
            Function<? super Entry<String, Object>, String> mapper)
    {
        return formatEntries(mapper);
    }

    /**
     * Returns a {@code Map} that associates the names of the fields of the
     * target object to their values.
     *
     * @return A map containing the names of the fields of the target object to
     *         their values.
     */
    private Map<String, Object> namesToValues() {
        return ReflectionUtilities.mapFields(
                target.getClass(),
                Field :: getName,
                f -> ReflectionUtilities.getValue(target, f)
        );
    }

}
