package org.fastgraph;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import lombok.Value;
import lombok.RequiredArgsConstructor;

@Value
@RequiredArgsConstructor
public class GraphNode<T> {
    private final Map<AttributeValue, GraphNode<T>> children = new HashMap<>();
    private final AttributeValue attributeValue;
    private final String attribute;
    private final T value;

    public void addChildren(GraphNode<T>... newChildren) {
        Stream.of(newChildren)
            .forEach(child -> this.children.put(child.getAttributeValue(), child));
    }
}
