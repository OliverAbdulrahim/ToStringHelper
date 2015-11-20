package functions;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A mutable {@code String} representation of an arbitrary object. 
 * 
 * <p> This class allows for the specification of a name during construction as 
 * well as chaining of the {@link #add(java.lang.String, java.lang.Object)} 
 * operation.
 * 
 * @author Oliver Abdulrahim
 */
public class ToStringHelper 
    extends AbstractToStringHelper
{
    
    /**
     * Flag for toggling omission of any {@code null} values when this object is
     * converted into a {@code String}, {@code false} allowing such values and 
     * {@code false} disallowing them.
     * 
     * By default, this value is specified to be {@code false}.
     */
    private boolean omitNullValues;
    
    /**
     * Constructs a {@code ToStringHelper} with an arbitrary object as 
     * the target.
     */
    public ToStringHelper() {
        super();
    }
    
    /**
     * Constructs a {@code ToStringHelper} with the given class as the name.
     * 
     * @param name The class whose name to use for this representation.
     */
    public ToStringHelper(Class<?> name) {
        super(name);
    }
    
    /**
     * Constructs a {@code ToStringHelper} with the given name.
     * 
     * @param name The name for this representation.
     */
    public ToStringHelper(String name) {
        super(name);
    }
    
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
    
    /**
     * Associates the given property with the given tag within this 
     * representation.
     * 
     * @param tag The name to associate with the given property.
     * @param property The property to add with the given name.
     * @return This object (after the add operation is complete), allowing for
     *         chaining of operations.
     */
    @Override
    public ToStringHelper add(String tag, Object property) {
        super.add(tag, property);
        return this;
    }
    
    /**
     * Constructs and returns a {@code String} representation of data wrapped
     * by this object.
     * 
     * @return A {@code String} representation of the object.
     */
    @Override
    public String toString() {
        String mappedValues = entries()
                .stream()
                .filter(entry -> !shouldOmit(entry.getValue()))
                .map(entry -> entry.getKey() + " = " + get(entry.getKey()))
                .collect(Collectors.joining(", "));
        return name + '{' + mappedValues + '}';
    }
    
    /**
     * Tests if the given property should be omitted based on its nullity and
     * the current null omission flag, returning {@code true} if the object 
     * should be ignored, {@code false} otherwise.
     * 
     * <p> This method makes no assumptions relating to the existence of the
     * given object in the {@link #values} of this instance.
     * 
     * @param property The object to test.
     * @return {@code 
     */
    private boolean shouldOmit(Object property) {
        return Objects.isNull(property) && omitNullValues;
    }
    
}
