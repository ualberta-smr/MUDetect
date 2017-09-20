package de.tu_darmstadt.stg.mudetect.stresstests;

import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageExample;
import de.tu_darmstadt.stg.mudetect.aug.model.patterns.APIUsagePattern;
import de.tu_darmstadt.stg.mudetect.matcher.EquallyLabelledNodeMatcher;
import de.tu_darmstadt.stg.mudetect.model.TestAUGBuilder;
import de.tu_darmstadt.stg.mudetect.overlapsfinder.AlternativeMappingsOverlapsFinder;
import org.junit.Test;

import static de.tu_darmstadt.stg.mudetect.aug.model.controlflow.ConditionEdge.ConditionType.SELECTION;
import static de.tu_darmstadt.stg.mudetect.aug.model.Edge.Type.DEFINITION;
import static de.tu_darmstadt.stg.mudetect.aug.model.Edge.Type.PARAMETER;
import static de.tu_darmstadt.stg.mudetect.mining.TestPatternBuilder.somePattern;
import static de.tu_darmstadt.stg.mudetect.model.TestAUGBuilder.buildAUG;
import static de.tu_darmstadt.stg.mudetect.src2aug.AUGBuilderTestUtils.buildAUGsForClass;

public class EqualsMethodsTest {
    @Test(timeout = 60000)
    public void equalsMethod() throws Exception {
        APIUsageExample target = buildAUGsForClass("import java.io.IOException;\n" +
                "import java.text.Collator;\n" +
                "\n" +
                "import org.apache.lucene.index.IndexReader;\n" +
                "import org.apache.lucene.util.ToStringUtils;\n" +
                "" +
                "class TermRangeQuery {\n" +
                "  private String lowerTerm;\n" +
                "  private String upperTerm;\n" +
                "  private Object collator;\n" +
                "  private String field;\n" +
                "  private boolean includeLower;\n" +
                "  private boolean includeUpper;\n" +
                "" +
                "  public boolean equals(Object obj) {\n" +
                "    if (this == obj)\n" +
                "      return true;\n" +
                "    if (!super.equals(obj))\n" +
                "      return false;\n" +
                "    if (getClass() != obj.getClass())\n" +
                "      return false;\n" +
                "    TermRangeQuery other = (TermRangeQuery) obj;\n" +
                "    if (collator == null) {\n" +
                "      if (other.collator != null)\n" +
                "        return false;\n" +
                "    } else if (!collator.equals(other.collator))\n" +
                "      return false;\n" +
                "    if (field == null) {\n" +
                "      if (other.field != null)\n" +
                "        return false;\n" +
                "    } else if (!field.equals(other.field))\n" +
                "      return false;\n" +
                "    if (includeLower != other.includeLower)\n" +
                "      return false;\n" +
                "    if (includeUpper != other.includeUpper)\n" +
                "      return false;\n" +
                "    if (lowerTerm == null) {\n" +
                "      if (other.lowerTerm != null)\n" +
                "        return false;\n" +
                "    } else if (!lowerTerm.equals(other.lowerTerm))\n" +
                "      return false;\n" +
                "    if (upperTerm == null) {\n" +
                "      if (other.upperTerm != null)\n" +
                "        return false;\n" +
                "    } else if (!upperTerm.equals(other.upperTerm))\n" +
                "      return false;\n" +
                "    return true;\n" +
                "  }\n" +
                "}").iterator().next();

        TestAUGBuilder patternAUG = buildAUG("pattern")
                .withActionNode("eq", "Object.equals()")
                .withActionNode("r1", "return")
                .withActionNode("r2", "return")
                .withActionNode("r3", "return")
                .withDataNode("b1", "boolean")
                .withDataNode("b2", "boolean")
                .withDataNode("b3", "boolean")
                .withDataNode("b4", "boolean")
                .withDataNode("b5", "boolean")
                .withDataNode("b6", "boolean")
                .withDataNode("b7", "boolean")
                .withDataNode("b8", "boolean")
                .withDataEdge("b1", PARAMETER, "r1")
                .withCondEdge("b2", SELECTION, "r1")
                .withCondEdge("b2", SELECTION, "eq")
                .withCondEdge("b3", SELECTION, "r1")
                .withCondEdge("b3", SELECTION, "eq")
                .withCondEdge("b3", SELECTION, "r2")
                .withCondEdge("b4", SELECTION, "r1")
                .withCondEdge("b4", SELECTION, "eq")
                .withCondEdge("b4", SELECTION, "r2")
                .withCondEdge("b4", SELECTION, "r3")
                .withDataEdge("b5", PARAMETER, "r2")
                .withDataEdge("b6", PARAMETER, "r3")
                .withDataEdge("eq", DEFINITION, "b7")
                .withDataEdge("eq", DEFINITION, "b8");
        APIUsagePattern pattern = somePattern(patternAUG);

        new AlternativeMappingsOverlapsFinder(new EquallyLabelledNodeMatcher()).findOverlaps(target, pattern);
    }
}
