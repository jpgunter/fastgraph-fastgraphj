package org.fastgraph;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FastGraphConfig {
    public final List<String> attributeEvaluationOrder;
}
