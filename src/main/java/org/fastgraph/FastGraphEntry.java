package org.fastgraph;

import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FastGraphEntry<T> {
    private final Map<String, String> attributes;
    private final T value;
}
