package org.fastgraph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FastGraphTest {
    
    @Test
    public void testGraph() {
        Assertions.assertNull(FastGraph.compile(null, null));
    }
}
