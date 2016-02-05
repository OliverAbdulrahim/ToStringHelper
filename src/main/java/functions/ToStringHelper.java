package functions;

import java.util.Objects;

/**
 * A mutable {@code String} representation of an arbitrary object.
 *
 * <p> Consider the example usage of the
 * {@link #add(java.lang.String, java.lang.Object)} operation shown below:
 *
 * <pre>{@code
 *     ToStringHelper helper = new ToStringHelper("An Object")
 *             .add("Some Tag", 12345)
 *             .add("Another Tag", null);
 *     System.out.println(helper);
 * }</pre>
 *
 * Each call to {@code add} specifies two parameters: an {@code Object}
 * property, or value, and a {@code String} tag, or name, to associate with the
 * property. Note the chaining of operations, which is made possible by the fact
 * that {@code add} returns a reference to the calling object. The above would
 * print the following:
 *
 * <pre>{@code
 *     An Object{Some Tag = 12345, Another Tag = null}
 * }</pre>
 *
 * <p> To omit {@code null} values, use the {@link #omitNullValues} method. Like
 * {@link #add(java.lang.String, java.lang.Object) add}, this method returns a
 * reference to the caller, so it is possible to chain calls in a similar way:
 *
 * <pre>{@code
 *     ToStringHelper helper = new ToStringHelper("An Object")
 *             .omitNullValues()
 *             .add("Some Tag", 12345)
 *             .add("Another Tag", null);
 *     System.out.println(helper);
 * }</pre>
 *
 * In this example, the {@code "Another Tag"} {@code String} is associated with
 * a {@code null} value, which causes it to be ignored when the
 * {@code ToStringHelper} is converted into a String. Therefore, the above
 * prints the following:
 *
 * <pre>{@code
 *     An Object{Some Tag = 12345}
 * }</pre>
 *
 * <p> This class allows for the specification of a name in the form of a
 * {@code String} and {@code Class} during construction.
 *
 * @author Oliver Abdulrahim
 */
public class ToStringHelper
    extends AbstractToStringHelper
{

    /**
     * Flag for toggling omission of any {@code null} values when this object is
     * converted into a {@code String}, {@code false} allowing such values and
     * {@code true} disallowing them.
     *
     * By default, this value is specified to be {@code false}.
     */
    private boolean omitNullValues;

    /**
     * Constructs a {@code ToStringHelper} targeting an arbitrary object.
     */
    public ToStringHelper() {
        super(new Object());
    }

    /**
     * Constructs a {@code ToStringHelper} with the given class as the name.
     *
     * @param c The class whose name to use for this representation.
     */
    public ToStringHelper(Class<?> c) {
        super(ReflectionUtilities.newInstance(c));
    }

// ToStringHelper operations

    /**
     * Returns a reference to this object whose {@link #toString()} omits
     * {@code null} values.
     *
     * @return A reference to this object that omits {@code null} values.
     */
    public ToStringHelper omitNullValues() {
        omitNullValues = true;
        return this;
    }

// Override methods

    /**
     * {@inheritDoc}
     *
     * @apiNote This method is overridden here in order to change the return
     *          value to a more concrete type. This allows calls to methods
     *          first defined in this class (or in other words, methods that
     *          exist in the {@code ToStringHelper} class but not the
     *          {@code AbstractToStringHelper} class) to be chained to calls to
     *          this method.
     */
    @Override
    public ToStringHelper add(String tag, Object property) {
        super.add(tag, property);
        return this;
    }

    /**
     * Collects, formats, and returns a {@code String} representation of all
     * tag/property pairs wrapped by this object in {@code "tag = property"}
     * format.
     *
     * @return A {@code String} representation of the object.
     */
    @Override
    public String toString() {
        String entries = toString(
                entry -> !shouldOmit(entry.getValue()),
                entry -> entry.getKey() + " = " + get(entry.getKey())
        );
        return name() + '{' + entries + '}';
    }

    /**
     * Tests if the given property should be omitted based on its nullity and
     * the current null omission flag, returning {@code true} if the object
     * should be ignored, {@code false} otherwise.
     *
     * <p> This method makes no assumptions relating to the existence of the
     * given object in the entries stored in this instance.
     *
     * @param property The object to test.
     * @return {@code true} if the given object should be ignored, {@code false}
     *         otherwise.
     */
    private boolean shouldOmit(Object property) {
        return Objects.isNull(property) && omitNullValues;
    }

}
