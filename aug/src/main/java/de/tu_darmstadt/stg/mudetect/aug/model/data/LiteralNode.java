package de.tu_darmstadt.stg.mudetect.aug.model.data;

import de.tu_darmstadt.stg.mudetect.aug.model.BaseNode;
import de.tu_darmstadt.stg.mudetect.aug.model.DataNode;

public class LiteralNode extends BaseNode implements DataNode {
    private final String dataType;
    private final String dataValue;

    public LiteralNode(String dataType, String dataValue) {
        this.dataType = dataType;
        this.dataValue = dataValue;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getValue() {
        return dataValue;
    }

    @Override
    public String getType() {
        return dataType;
    }

    @Override
    public String getLabel() {
        return getType();
    }
}
