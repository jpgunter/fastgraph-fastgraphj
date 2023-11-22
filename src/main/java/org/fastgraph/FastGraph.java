package org.fastgraph;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PACKAGE)
public class FastGraph <T> {

    private final int[] graph;
    private final Map<String, Integer>[] attributeValues;
    private final String[] attributeIndexes;
    private final T[] values;

    /*
    non-leaf:
    [0,<nodeAttribute>,<wildcard>,<nextNodeIndex0..N>,]
    leaf:
    [1, <valueIndex>]
     */
    public Optional<T> resolve(Map<String, String> searchAttributes) {
        int currentIndex = 0;
        while(currentIndex < graph.length) {
            int nodeType = graph[currentIndex];
            if (nodeType == 1) {
                int valueIndex = graph[currentIndex+1];
                return Optional.ofNullable(values[valueIndex]);
            }
            //get the attribute and move ahead
            int attrIndex = graph[++currentIndex];
            String attrName = attributeIndexes[attrIndex];
            String attrValue = searchAttributes.get(attrName);
            Integer valueIndex = attributeValues[attrIndex].get(attrValue);
            int offsetToNextNode = 1;//default to wildcard
            if (valueIndex != null) {
                //0 is the first, move over to the start of the value array
                offsetToNextNode += valueIndex + 1;
            }
            currentIndex += offsetToNextNode;
            currentIndex = graph[currentIndex];
        }
        return Optional.empty();
    }
    
    public static <T> FastGraph<T> compile(List<FastGraphEntry<T>> entries, FastGraphConfig FastGraphConfig) {
        return new FastGraphCompliler<T>().compile(entries, FastGraphConfig);
    }
}
