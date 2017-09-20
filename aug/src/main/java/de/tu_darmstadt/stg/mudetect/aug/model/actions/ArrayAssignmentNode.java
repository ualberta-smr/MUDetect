package de.tu_darmstadt.stg.mudetect.aug.model.actions;

public class ArrayAssignmentNode extends MethodCallNode {
    public ArrayAssignmentNode(String arrayTypeName) {
        super(arrayTypeName, "arrayset()");
    }

    public ArrayAssignmentNode(String arrayTypeName, int sourceLineNumber) {
        super(arrayTypeName, "arrayset()", sourceLineNumber);
    }

    @Override
    public boolean isCoreAction() {
        return false;
    }
}
