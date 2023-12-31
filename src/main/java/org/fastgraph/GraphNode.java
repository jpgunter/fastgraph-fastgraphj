package org.fastgraph;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import lombok.Value;
import lombok.RequiredArgsConstructor;

@Value
@RequiredArgsConstructor
public class GraphNode<T> {
    Map<AttributeValue, GraphNode<T>> children = new HashMap<>();
    AttributeValue attributeValue;
    String attribute;
    T value;

    @SafeVarargs
    public final void addChildren(GraphNode<T>... newChildren) {
        Stream.of(newChildren)
            .forEach(child -> this.children.put(child.getAttributeValue(), child));
    }

    public GraphNode<T> getChild(AttributeValue attributeValue) {
        return children.get(attributeValue);
    }

    public boolean hasChild(AttributeValue attributeValue) {
        return children.containsKey(attributeValue);
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public GraphNode<T> withValue(AttributeValue attributeValue) {
        return new GraphNode<>(attributeValue, attribute, value);
    }
}
