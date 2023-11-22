package org.fastgraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;

public class FastGraphCompliler<T> {

    public FastGraph<T> compile(List<FastGraphEntry<T>> entries, FastGraphConfig fastGraphConfig) {
        List<String> evalOrder = fastGraphConfig.getAttributeEvaluationOrder();

        Map<String, Integer>[] attributeValues = (Map<String, Integer>[]) evalOrder.stream()
                .map(v -> new HashMap<String, Integer>())
                .collect(Collectors.toList())
                .toArray();
        String[] attributeIndexes = (String[]) evalOrder.toArray();
        AtomicInteger nextValueIndex = new AtomicInteger(0);
        Map<T, Integer> valueToIndex = entries.stream()
                .map(FastGraphEntry::getValue)
                .collect(Collectors.toMap(Function.identity(), e -> nextValueIndex.getAndIncrement()));
        T[] values = (T[]) entries.stream()
                .map(FastGraphEntry::getValue)
                .toArray();

        entries.stream()
                .map(FastGraphEntry::getAttributes)
                .forEach(m -> addToValues(m, attributeValues, attributeIndexes));
        return null;
    }

    private void addToValues(Map<String, String> m, Map<String, Integer>[] attributeValues, String[] attributeIndexes) {

    }

    public GraphNode<T> compileNaive(List<FastGraphEntry<T>> entries, FastGraphConfig config) {
        Validate.notEmpty(config.getAttributeEvaluationOrder(), "Evalution order must be defined and not empty");
        GraphNode<T> root = new GraphNode<T>(AttributeValue.ofWild(), null, null);
        List<GraphNode<T>> wildCardPath = config.getAttributeEvaluationOrder().stream()
            .map(this::attributeWildcard)
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
    private void insertIntoGraph(FastGraphEntry<T> entry, GraphNode<T> root, FastGraphConfig config) {
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

    private GraphNode<T> attributeWildcard(String attribute) {
        return new GraphNode<>(AttributeValue.ofWild(), attribute, null);
    }
}
