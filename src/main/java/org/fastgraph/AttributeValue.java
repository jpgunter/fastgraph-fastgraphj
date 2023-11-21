package org.fastgraph;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode
public class AttributeValue {
    private final String value;
    private final boolean isWild;

    public static AttributeValue ofWild(){
        return new AttributeValue(null, true);
    }

    public static AttributeValue ofValue(String value) {
        return new AttributeValue(value, false);
    }
}
