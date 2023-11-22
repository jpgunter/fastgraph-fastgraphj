package org.fastgraph;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

public class FastGraphTest {

    @Test
    public void testResolve(){
        String[] attributes = new String[]{"color", "size"};
        Map<String, Integer>[] attributeValueMap = new Map[]{
                ImmutableMap.of("red", 0, "blue", 1),
                ImmutableMap.of("large", 0, "small", 1)
        };
        BigDecimal[] values = new BigDecimal[]{
                BigDecimal.valueOf(1),
                BigDecimal.valueOf(2),
                BigDecimal.valueOf(3),
                BigDecimal.valueOf(4),
                BigDecimal.valueOf(5)
        };
        int[] graph = new int[]{0,0,5,12,19,0,1,10,-1,-1,1,2,0,1,10,17,10,1,0,0,1,10,10,24,1,4};
        FastGraph<BigDecimal> fastGraph = FastGraph.<BigDecimal>builder()
                .graph(graph)
                .attributeValues(attributeValueMap)
                .values(values)
                .attributeIndexes(attributes)
                .build();
        Map<String, String> testSearch = ImmutableMap.of(
                "color", "blue",
                "size", "small"
        );
        BigDecimal result = fastGraph.resolve(testSearch).orElse(null);
        Assertions.assertEquals(values[4], result);
    }
}
