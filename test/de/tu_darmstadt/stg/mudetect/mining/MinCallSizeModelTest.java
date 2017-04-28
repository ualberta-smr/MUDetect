package de.tu_darmstadt.stg.mudetect.mining;

import de.tu_darmstadt.stg.mudetect.model.TestAUGBuilder;
import egroum.EGroumDataEdge;
import org.junit.Test;

import java.util.Set;

import static de.tu_darmstadt.stg.mudetect.mining.TestPatternBuilder.somePattern;
import static egroum.EGroumDataEdge.Type.THROW;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static utils.SetUtils.asSet;

public class MinCallSizeModelTest {
    @Test
    public void filtersPatternWithFewerActions() throws Exception {
        Pattern pattern = somePattern(TestAUGBuilder.buildAUG().withActionNode("m()"));

        Set<Pattern> patterns = new MinCallSizeModel(() -> asSet(pattern), 2).getPatterns();

        assertThat(patterns, is(empty()));
    }

    @Test
    public void considersCalls() throws Exception {
        Pattern pattern = somePattern(TestAUGBuilder.buildAUG().withActionNodes("m()", "n()"));

        Set<Pattern> patterns = new MinCallSizeModel(() -> asSet(pattern), 2).getPatterns();

        assertThat(patterns, is(not(empty())));
    }

    @Test
    public void considersCatch() throws Exception {
        Pattern pattern = somePattern(TestAUGBuilder.buildAUG()
                .withActionNodes("m()").withDataNode("SomeException")
                .withDataEdge("m()", THROW, "SomeException"));

        Set<Pattern> patterns = new MinCallSizeModel(() -> asSet(pattern), 2).getPatterns();

        assertThat(patterns, is(not(empty())));
    }
}