package org.fastgraph;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;

public class FastGraphCompliler<T> {

    public static <T> FastGraph<T> compile(List<FastGraphEntry<T>> entries, FastGraphConfig fastGraphConfig) {
        return null;
    }

    private static <T> GraphNode<T> compileNaive(List<FastGraphEntry<T>> entries, FastGraphConfig config) {
        Validate.notEmpty(config.getAttributeEvaluationOrder(), "Evalution order must be defined and not empty");
        GraphNode<T> root = new GraphNode<T>(AttributeValue.ofWild(), null, null);
        List<GraphNode<T>> wildCardPath = config.getAttributeEvaluationOrder().stream()
            .map(attribute -> (GraphNode<T>) attributeWildcard(attribute))
            .collect(Collectors.toList());

        for (int i = 0; i < wildCardPath.size()-1; i++) {
            wildCardPath.get(i).addChildren(wildCardPath.get(i+1));
        }
        root.addChildren(wildCardPath.get(0));
        
        entries.stream()
            .forEach(entry -> attach(entry, root));

        return root;
    }

    private static <T> void attach(FastGraphEntry entry, GraphNode<T> root) {
        String attributeValue = entry.getAttributes().get(root.getAttribute());
        if(attributeValue != null) {
        }
    }

    private static <T> GraphNode<T> attributeWildcard(String attribute) {
        return new GraphNode<T>(AttributeValue.ofWild(), attribute, null);
    }
}
