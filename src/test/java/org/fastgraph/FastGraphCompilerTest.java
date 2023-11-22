package org.fastgraph;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FastGraphCompilerTest {
    
    @Test
    public void testGraph() {
        List<FastGraphEntry<BigDecimal>> entries = ImmutableList.of(
            testEntry("red", "large", BigDecimal.valueOf(1)),
            testEntry("blue", "small", BigDecimal.valueOf(2)),
            testEntry(null, null, BigDecimal.valueOf(3)),
            testEntry(null, "large", BigDecimal.valueOf(4)),
            testEntry("red", null, BigDecimal.valueOf(5))
        );

        FastGraphConfig config  = FastGraphConfig.builder()
                .attributeEvaluationOrder(ImmutableList.of("color", "size"))
                .build();

        GraphNode<BigDecimal> root = new FastGraphCompliler<BigDecimal>().compileNaive(entries, config);

        BigDecimal price = root.getChild(AttributeValue.ofWild())
                .getChild(AttributeValue.ofWild())
                .getChild(AttributeValue.ofWild())
                .getValue();

        Assertions.assertEquals(BigDecimal.valueOf(3), price);
    }

    private FastGraphEntry<BigDecimal> testEntry(String color, String size, BigDecimal value) {
        Map<String, String> attributes = new HashMap<>();
        Optional.ofNullable(color).ifPresent(c -> attributes.put("color", c));
        Optional.ofNullable(size).ifPresent(s -> attributes.put("size", s));
        return FastGraphEntry.<BigDecimal>builder()
                .attributes(attributes)
                .value(value)
                .build();
    }
}
