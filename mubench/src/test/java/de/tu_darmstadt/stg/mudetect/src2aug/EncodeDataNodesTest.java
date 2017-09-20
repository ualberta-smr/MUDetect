package de.tu_darmstadt.stg.mudetect.src2aug;

import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageExample;
import org.junit.Test;

import static de.tu_darmstadt.stg.mudetect.model.AUGTestUtils.dataNodeWithLabel;
import static de.tu_darmstadt.stg.mudetect.model.AUGTestUtils.hasNode;
import static de.tu_darmstadt.stg.mudetect.src2aug.AUGBuilderTestUtils.buildAUG;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class EncodeDataNodesTest {
    @Test
    public void addsDataNodeForParameter() throws Exception {
        APIUsageExample aug = AUGBuilderTestUtils.buildAUG("void m(Object o) { o.hashCode(); }");

        assertThat(aug, hasNode(dataNodeWithLabel("Object")));
    }

    @Test
    public void addsDataNodeForVariable() throws Exception {
        APIUsageExample aug = AUGBuilderTestUtils.buildAUG("String m() { String self = toString(); return self; }");

        assertThat(aug, hasNode(dataNodeWithLabel("String")));
    }

    @Test
    public void addsDataNodeForImplicitReference() throws Exception {
        APIUsageExample aug = AUGBuilderTestUtils.buildAUG("String m() { return toString(); }");

        assertThat(aug, hasNode(dataNodeWithLabel("String")));
    }

    @Test
    public void noDataNodeForUnusedReturnValue() throws Exception {
        APIUsageExample aug = AUGBuilderTestUtils.buildAUG("void m() { toString(); }");

        assertThat(aug, not(hasNode(dataNodeWithLabel("String"))));
    }

    @Test
    public void noDataNodeForUnusedVariable() throws Exception {
        APIUsageExample aug = AUGBuilderTestUtils.buildAUG("void m() { String s = toString(); }");

        assertThat(aug, not(hasNode(dataNodeWithLabel("String"))));
    }

    @Test
    public void noDataNodeForUnusedLiteral() throws Exception {
        APIUsageExample aug = AUGBuilderTestUtils.buildAUG("void m() { String s = \"foo\"; }");

        assertThat(aug, not(hasNode(dataNodeWithLabel("String"))));
    }
}
