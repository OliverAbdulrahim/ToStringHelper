package functions;

import java.util.Objects;
import java.util.stream.Collectors;

public class ToStringHelper 
    extends AbstractToStringHelper
{

    private boolean omitNullValues;

    public ToStringHelper() {
        super();
        omitNullValues = false;
    }
    
    public ToStringHelper(String name) {
        super(name);
        omitNullValues = false;
    }
    
    public ToStringHelper omitNullValues() {
        omitNullValues = true;
        return this;
    }
    
    @Override
    public ToStringHelper add(String tag, Object property) {
        super.add(tag, property);
        return this;
    }

    @Override
    public String toString() {
        String mappedValues = entrySet()
                .stream()
                .filter(entry -> !shouldOmit(entry.getValue()))
                .map(entry -> entry.getKey() + '=' + get(entry.getKey()))
                .collect(Collectors.joining(", "));
        return name + '{' + mappedValues + '}';
    }
    
    private boolean shouldOmit(Object property) {
        return Objects.isNull(property) && omitNullValues;
    }
    
}
