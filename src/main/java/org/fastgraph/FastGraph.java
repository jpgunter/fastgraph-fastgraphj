package org.fastgraph;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PACKAGE)
public class FastGraph <T> {

    private final Long[] graph;
    private final Map<String, Long> attributeValues;
    

    public Optional<T> resolve(Map<String, String> attributes) {
        return null;
    }
    
    public static <T> FastGraph<T> compile(List<FastGraphEntry<T>> entries, FastGraphConfig FastGraphConfig) {
        return FastGraphCompliler.compile(entries, FastGraphConfig);
    }
}
