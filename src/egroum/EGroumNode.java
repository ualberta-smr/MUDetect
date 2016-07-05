package egroum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression;

import egroum.EGroumDataEdge.Type;
import utils.JavaASTUtil;

public abstract class EGroumNode {
	protected static final String PREFIX_DUMMY = "dummy_";
	public static int numOfNodes = 0;
	private static HashSet<Integer> invocationTypes = new HashSet<>(), controlTypes = new HashSet<>(), literalTypes = new HashSet<>();
	private static HashMap<String, Character> infixExpressionLables = new HashMap<>();
	static {
		invocationTypes.add(ASTNode.ARRAY_ACCESS);
		invocationTypes.add(ASTNode.ARRAY_CREATION);
		invocationTypes.add(ASTNode.ARRAY_INITIALIZER);
		invocationTypes.add(ASTNode.ASSERT_STATEMENT);
		invocationTypes.add(ASTNode.BREAK_STATEMENT);
		invocationTypes.add(ASTNode.CAST_EXPRESSION);
		invocationTypes.add(ASTNode.CLASS_INSTANCE_CREATION);
		invocationTypes.add(ASTNode.CONSTRUCTOR_INVOCATION);
		invocationTypes.add(ASTNode.CONTINUE_STATEMENT);
		invocationTypes.add(ASTNode.INSTANCEOF_EXPRESSION);
		invocationTypes.add(ASTNode.METHOD_INVOCATION);
		invocationTypes.add(ASTNode.RETURN_STATEMENT);
		invocationTypes.add(ASTNode.SUPER_CONSTRUCTOR_INVOCATION);
		invocationTypes.add(ASTNode.SUPER_METHOD_INVOCATION);
		invocationTypes.add(ASTNode.THROW_STATEMENT);
		
		controlTypes.add(ASTNode.CATCH_CLAUSE);
		controlTypes.add(ASTNode.DO_STATEMENT);
		controlTypes.add(ASTNode.ENHANCED_FOR_STATEMENT);
		controlTypes.add(ASTNode.FOR_STATEMENT);
		controlTypes.add(ASTNode.IF_STATEMENT);
		controlTypes.add(ASTNode.SWITCH_STATEMENT);
		controlTypes.add(ASTNode.SYNCHRONIZED_STATEMENT);
		controlTypes.add(ASTNode.TRY_STATEMENT);
		controlTypes.add(ASTNode.WHILE_STATEMENT);
		
		literalTypes.add(ASTNode.BOOLEAN_LITERAL);
		literalTypes.add(ASTNode.CHARACTER_LITERAL);
		literalTypes.add(ASTNode.NULL_LITERAL);
		literalTypes.add(ASTNode.NUMBER_LITERAL);
		literalTypes.add(ASTNode.STRING_LITERAL);
		literalTypes.add(ASTNode.TYPE_LITERAL);
		
		// Arithmetic Operators
		infixExpressionLables.put(InfixExpression.Operator.DIVIDE.toString(), 'a');
		infixExpressionLables.put(InfixExpression.Operator.MINUS.toString(), 'a');
		infixExpressionLables.put(InfixExpression.Operator.PLUS.toString(), 'a');
		infixExpressionLables.put(InfixExpression.Operator.REMAINDER.toString(), 'a');
		infixExpressionLables.put(InfixExpression.Operator.TIMES.toString(), 'a');
		// Equality and Relational Operators
		infixExpressionLables.put(InfixExpression.Operator.EQUALS.toString(), 'r');
		infixExpressionLables.put(InfixExpression.Operator.GREATER.toString(), 'r');
		infixExpressionLables.put(InfixExpression.Operator.GREATER_EQUALS.toString(), 'r');
		infixExpressionLables.put(InfixExpression.Operator.LESS.toString(), 'r');
		infixExpressionLables.put(InfixExpression.Operator.LESS_EQUALS.toString(), 'r');
		infixExpressionLables.put(InfixExpression.Operator.NOT_EQUALS.toString(), 'r');
		// Conditional Operators
		infixExpressionLables.put(InfixExpression.Operator.CONDITIONAL_AND.toString(), 'c');
		infixExpressionLables.put(InfixExpression.Operator.CONDITIONAL_OR.toString(), 'c');
		// Bitwise and Bit Shift Operators
		infixExpressionLables.put(InfixExpression.Operator.AND.toString(), 'b');
		infixExpressionLables.put(InfixExpression.Operator.OR.toString(), 'b');
		infixExpressionLables.put(InfixExpression.Operator.XOR.toString(), 'b');
		infixExpressionLables.put(InfixExpression.Operator.LEFT_SHIFT.toString(), 's');
		infixExpressionLables.put(InfixExpression.Operator.RIGHT_SHIFT_SIGNED.toString(), 's');
		infixExpressionLables.put(InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED.toString(), 's');
	}
	
	protected int id;
	protected ASTNode astNode;
	protected int astNodeType;
	protected String key;
	protected EGroumNode control;
	protected String dataType;
	protected EGroumGraph graph;
	protected ArrayList<EGroumEdge> inEdges = new ArrayList<EGroumEdge>();
	protected ArrayList<EGroumEdge> outEdges = new ArrayList<EGroumEdge>();

	public EGroumNode(ASTNode astNode, int nodeType) {
		this.id = ++numOfNodes;
		this.astNode = astNode;
		this.astNodeType = nodeType;
	}
	
	public EGroumNode(ASTNode astNode, int nodeType, String key) {
		this(astNode, nodeType);
		this.key = key;
	}

	public int getId() {
		return id;
	}

	public EGroumGraph getGraph() {
		return graph;
	}

	public void setGraph(EGroumGraph groum) {
		this.graph = groum;
	}
	
	public String getDataType() {
		return dataType;
	}

	public String getDataName() {
		if (this instanceof EGroumDataNode)
			return ((EGroumDataNode) this).getDataName();
		return null;
	}

	abstract public String getLabel();
	
	abstract public String getExasLabel();

	public int getAstNodeType() {
		return astNodeType;
	}

	public ASTNode getAstNode() {
		return astNode;
	}

	public ArrayList<EGroumEdge> getInEdges() {
		return inEdges;
	}

	public ArrayList<EGroumEdge> getOutEdges() {
		return outEdges;
	}

	public void addOutEdge(EGroumEdge edge) {
		outEdges.add(edge);
	}

	public void addInEdge(EGroumEdge edge) {
		inEdges.add(edge);
	}

	public boolean isLiteral() {
		return JavaASTUtil.isLiteral(astNodeType);
	}

	public void delete() {
		for (EGroumEdge e : inEdges) {
			e.source.outEdges.remove(e);
		}
		inEdges.clear();
		for (EGroumEdge e : outEdges)
			e.target.inEdges.remove(e);
		outEdges.clear();
		control = null;
	}

	public boolean isDefinition() {
		if (this instanceof EGroumDataNode)
			return ((EGroumDataNode) this).isDefinition();
		return false;
	}

	public boolean isDeclaration() {
		return (this instanceof EGroumDataNode) && ((EGroumDataNode) this).isDeclaration;
	}

	public boolean isStatement() {
		return control != null;
	}

	public ArrayList<EGroumNode> getIncomingEmptyNodes() {
		ArrayList<EGroumNode> nodes = new ArrayList<>();
		for (EGroumEdge e : inEdges)
			if (e.source.isEmptyNode())
				nodes.add(e.source);
		return nodes;
	}
	
	ArrayList<EGroumEdge> getInEdgesForExasVectorization() {
		ArrayList<EGroumEdge> edges = new ArrayList<>();
		for (EGroumEdge e : inEdges)
			if (!(e instanceof EGroumDataEdge) || ((EGroumDataEdge) e).type != Type.DEPENDENCE)
				edges.add(e);
		return edges;
	}
	
	ArrayList<EGroumEdge> getOutEdgesForExasVectorization() {
		ArrayList<EGroumEdge> edges = new ArrayList<>();
		for (EGroumEdge e : outEdges)
			if (!(e instanceof EGroumDataEdge) || ((EGroumDataEdge) e).type != Type.DEPENDENCE)
				edges.add(e);
		return edges;
	}

	public boolean isEmptyNode() {
		return this instanceof EGroumActionNode && ((EGroumActionNode) this).name.equals("empty");
	}

	private void adjustControl(EGroumNode empty, int index) {
		EGroumControlEdge e = (EGroumControlEdge) getInEdge(control);
		control.outEdges.remove(e);
		e.source = empty.control;
		empty.control.outEdges.add(index, e);
		e.label = empty.getInEdge(empty.control).getLabel();
		control = empty.control;
	}

	public EGroumEdge getInEdge(EGroumNode node) {
		for (EGroumEdge e : inEdges)
			if (e.source == node)
				return e;
		return null;
	}

	public void adjustControl(EGroumNode node, EGroumNode empty) {
		int i = 0;
		while (outEdges.get(i).target != node) {
			i++;
		}
		int index = empty.control.getOutEdgeIndex(empty);
		while (i < outEdges.size() && !outEdges.get(i).target.isEmptyNode()) {
			index++;
			outEdges.get(i).target.adjustControl(empty, index);
		}
	}

	public ArrayList<EGroumEdge> getInDependences() {
		ArrayList<EGroumEdge> es = new ArrayList<>();
		for (EGroumEdge e : inEdges)
			if (e instanceof EGroumDataEdge && ((EGroumDataEdge) e).type == Type.DEPENDENCE)
				es.add(e);
		return es;
	}

	public int getOutEdgeIndex(EGroumNode node) {
		int i = 0;
		while (i < outEdges.size()) {
			if (outEdges.get(i).target == node)
				return i;
			i++;
		}
		return -1;
	}

	public boolean isSame(EGroumNode node) {
		if (key == null && node.key != null)
			return false;
		if (!key.equals(node.key))
			return false;
		if (this instanceof EGroumActionNode)
			return ((EGroumActionNode) this).isSame(node);
		if (this instanceof EGroumDataNode)
			return ((EGroumDataNode) this).isSame(node);
		if (this instanceof EGroumControlNode)
			return ((EGroumControlNode) this).isSame(node);
		return false;
	}

	public EGroumNode getQualifier() {
		if (this instanceof EGroumDataNode)
			return ((EGroumDataNode) this).getQualifier();
		return null;
	}

	public EGroumNode getDefinition() {
		if (this instanceof EGroumDataNode && this.inEdges.size() == 1 && this.inEdges.get(0) instanceof EGroumDataEdge) {
			EGroumDataEdge e = (EGroumDataEdge) this.inEdges.get(0);
			if (e.type == Type.REFERENCE)
				return e.source;
		}
		return null;
	}

	public ArrayList<EGroumNode> getDefinitions() {
		ArrayList<EGroumNode> defs = new ArrayList<>();
		if (this instanceof EGroumDataNode) {
			for (EGroumEdge e : this.inEdges) {
				if (e instanceof EGroumDataEdge && ((EGroumDataEdge) e).type == Type.REFERENCE)
					defs.add(e.source);
			}
		}
		return defs;
	}

	public HashSet<EGroumNode> getCatchClauses() {
		HashSet<EGroumNode> ccs = new HashSet<>();
		for (EGroumEdge e : inEdges) {
			if (e.source.astNodeType == ASTNode.CATCH_CLAUSE)
				ccs.add(e.source);
			if (e instanceof EGroumControlEdge)
				ccs.addAll(e.source.getCatchClauses());
		}
		return ccs;
	}

	public ArrayList<EGroumNode> getReferences() {
		ArrayList<EGroumNode> refs = new ArrayList<>();
		if (this instanceof EGroumDataNode) {
			for (EGroumEdge e : this.outEdges) {
				if (e instanceof EGroumDataEdge && ((EGroumDataEdge) e).type == Type.REFERENCE)
					refs.add(e.target);
			}
		}
		return refs;
	}

	public boolean hasInEdge(EGroumEdge edge) {
		for (EGroumEdge e : inEdges)
			if (e.source == edge.source && e.getLabel().equals(edge.getLabel()))
				return true;
		return false;
	}

	public boolean hasInNode(EGroumNode preNode) {
		for (EGroumEdge e : inEdges)
			if (e.source == preNode && (!(e instanceof EGroumDataEdge) || ((EGroumDataEdge) e).type != Type.DEPENDENCE))
				return true;
		return false;
	}

	public boolean hasOutNode(EGroumNode target) {
		for (EGroumEdge e : outEdges)
			if (e.target == target)
				return true;
		return false;
	}

	public boolean isAssignment() {
		return astNodeType == ASTNode.ASSIGNMENT;
	}

	public HashSet<EGroumNode> getInNodes() {
		HashSet<EGroumNode> nodes = new HashSet<>();
		for (EGroumEdge e : this.inEdges)
			nodes.add(e.source);
		return nodes;
	}

	public HashSet<EGroumNode> getOutNodes() {
		HashSet<EGroumNode> nodes = new HashSet<>();
		for (EGroumEdge e : this.outEdges)
			nodes.add(e.target);
		return nodes;
	}

	public boolean isCoreAction() {
		return isCoreAction(astNodeType);
	}

	public static boolean isCoreAction(int astNodeType) {
		return invocationTypes.contains(astNodeType);
	}

	public void buildControlClosure(HashSet<EGroumNode> doneNodes) {
		if (this instanceof EGroumControlNode) {
			((EGroumControlNode) this).buildControlClosure(doneNodes);
		} else if (this instanceof EGroumActionNode)
			((EGroumActionNode) this).buildControlClosure(doneNodes);
		else
			doneNodes.add(this);
	}

	public void buildDataClosure(HashSet<EGroumNode> doneNodes) {
		if (this instanceof EGroumActionNode || this instanceof EGroumDataNode) {
			for (EGroumEdge e : new HashSet<EGroumEdge>(this.inEdges)) {
				if (!(e instanceof EGroumDataEdge))
					continue;
				if (!(e.source instanceof EGroumDataNode))
					continue;
				EGroumDataEdge de = (EGroumDataEdge) e;
				if (this instanceof EGroumActionNode && de.type != Type.RECEIVER && de.type != Type.PARAMETER)
					continue;
				if (this instanceof EGroumDataNode && de.type != Type.QUALIFIER)
					continue;
				for (EGroumNode def : de.source.getDefinitions()) {
					for (EGroumEdge e1 : def.inEdges) {
						if (e1 instanceof EGroumDataEdge && ((EGroumDataEdge) e1).type == Type.DEFINITION) {
							if (!doneNodes.contains(e1.source))
								e1.source.buildDataClosure(doneNodes);
							for (EGroumEdge e2 : e1.source.inEdges)
								if (e2 instanceof EGroumDataEdge && ((EGroumDataEdge) e2).getType() == Type.PARAMETER && !this.hasInEdge(e2))
									new EGroumDataEdge(e2.source, this, de.type);
							break;
						}
					}
				}
			}
		}
		doneNodes.add(this);
	}

	public void buildPreSequentialNodes(HashSet<EGroumNode> doneNodes, HashMap<EGroumNode, HashSet<EGroumNode>> preNodesOfNode) {
		HashSet<EGroumNode> preNodes = new HashSet<>();
		for (EGroumEdge e : this.inEdges) {
			preNodes.add(e.source);
			if (!doneNodes.contains(e.source))
				e.source.buildPreSequentialNodes(doneNodes, preNodesOfNode);
			preNodes.addAll(preNodesOfNode.get(e.source));
		}
		preNodesOfNode.put(this, preNodes);
		doneNodes.add(this);
	}

	public static EGroumNode createNode(EGroumNode node) {
		if (node instanceof EGroumActionNode)
			return new EGroumActionNode((EGroumActionNode) node);
		if (node instanceof EGroumControlNode)
			return new EGroumControlNode((EGroumControlNode) node);
		if (node instanceof EGroumDataNode)
			return new EGroumDataNode((EGroumDataNode) node);
		return null;
	}

	public static boolean isThisMethodCall(String label) {
		return label.startsWith("this.") && label.endsWith("()");
	}
}
