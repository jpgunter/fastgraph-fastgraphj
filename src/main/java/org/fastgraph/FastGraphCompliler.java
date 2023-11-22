package org.fastgraph;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;

public class FastGraphCompliler<T> {

    public static <T> FastGraph<T> compile(List<FastGraphEntry<T>> entries, FastGraphConfig fastGraphConfig) {
        return null;
    }

    public static <T> GraphNode<T> compileNaive(List<FastGraphEntry<T>> entries, FastGraphConfig config) {
        Validate.notEmpty(config.getAttributeEvaluationOrder(), "Evalution order must be defined and not empty");
        GraphNode<T> root = new GraphNode<T>(AttributeValue.ofWild(), null, null);
        List<GraphNode<T>> wildCardPath = config.getAttributeEvaluationOrder().stream()
            .map(attribute -> (GraphNode<T>) attributeWildcard(attribute))
            .collect(Collectors.toList());

        for (int i = 0; i < wildCardPath.size()-1; i++) {
            wildCardPath.get(i).addChildren(wildCardPath.get(i+1));
        }
        root.addChildren(wildCardPath.get(0));
        wildCardPath.get(wildCardPath.size()-1).addChildren(new GraphNode<>(AttributeValue.ofWild(), null, null));
        
        entries.stream()
            .forEach(entry -> insertIntoGraph(entry, root, config));

        return root;
    }

    @VisibleForTesting
    private static <T> void insertIntoGraph(FastGraphEntry<T> entry, GraphNode<T> root, FastGraphConfig config) {
        GraphNode<T> currentNode = root.getChild(AttributeValue.ofWild());
        for (int i = 0; i < config.getAttributeEvaluationOrder().size() - 1; i++) {
            String currentAttribute = config.getAttributeEvaluationOrder().get(i);
            String nextAttribute = config.getAttributeEvaluationOrder().get(i+1);

            String attributeValueString = entry.getAttributes().get(currentAttribute);
            if(attributeValueString == null) {
                currentNode = currentNode.getChild(AttributeValue.ofWild());
                continue;
            }
            AttributeValue attributeValue = AttributeValue.ofValue(attributeValueString);
            GraphNode<T> nextNode = currentNode.getChild(attributeValue);
            if(nextNode == null) {
                nextNode = new GraphNode<>(attributeValue, nextAttribute, null);
                currentNode.addChildren(nextNode);
            }
            currentNode = nextNode;
        }
        //now attach the value
        String lastAttribute = config.getAttributeEvaluationOrder().get(config.getAttributeEvaluationOrder().size()-1);
        String attributeValueString = entry.getAttributes().get(lastAttribute);
        AttributeValue attributeValue;
        if(attributeValueString == null) {
            attributeValue = AttributeValue.ofWild();
        } else {
            attributeValue = AttributeValue.ofValue(attributeValueString);
        }
        currentNode.addChildren(new GraphNode<>(attributeValue, null, entry.getValue()));
    }

    private static <T> GraphNode<T> attributeWildcard(String attribute) {
        return new GraphNode<T>(AttributeValue.ofWild(), attribute, null);
    }
}
