
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*Reused code (modified) from ycoppel@google.com (Yohann Coppel)*/

public class Tree<T> {

  private T head;

  private ArrayList<Tree<T>> subtrees = new ArrayList<Tree<T>>();
  private List<T> leafNodes = new ArrayList<T>();
  private Set<T> allNodes = new HashSet<T>();
  private Tree<T> parentTree = null;

  private HashMap<T, Tree<T>> locate = new HashMap<T, Tree<T>>();
  private HashMap<T, T> parents = new HashMap<T, T>();
  
  public Tree(T head) {
    this.head = head;
    locate.put(head, this);
    leafNodes.add(head);
    allNodes.add(head);
  }
  
  public Collection<T> getNodes()
  {
	  return allNodes;
  }
  
  public List<T> getLeafNodes()
  {
	  return leafNodes;
  }
  
  
  
  public T getParentNode(T node)
  {
	  if(parents.containsKey(node))
		  return parents.get(node);
	  return null;
  }
  
  public boolean contains(T node)
  {
	  return locate.containsKey(node);
  }

  public void addLeaf(T root, T leaf) {
    if (locate.containsKey(root)) {
      locate.get(root).addLeaf(leaf);
      parents.put(leaf, root);
      if(leafNodes.contains(root))
    	  leafNodes.remove(root);
      if(!leafNodes.contains(leaf))
    	  leafNodes.add(leaf);
      allNodes.add(leaf);
    } else {
      addLeaf(root).addLeaf(leaf);
    }
  }

  public Tree<T> addLeaf(T leaf) {
    Tree<T> t = new Tree<T>(leaf);
    subtrees.add(t);
    t.parentTree = this;
    t.locate = this.locate;
    locate.put(leaf, t);
    parents.put(leaf, head);
    if(leafNodes.contains(head))
  	  leafNodes.remove(head);
    if(!leafNodes.contains(leaf))
    	leafNodes.add(leaf);
    allNodes.add(leaf);
    return t;
  }

  public T getHead() {
    return head;
  }

  public Tree<T> getTree(T element) {
    return locate.get(element);
  }

  public Tree<T> getParent() {
    return parentTree;
  }
  
  
  public List<T> pathToNode(T node)
  {
	  List<T> path = new ArrayList<T>();
	  while(node != head)
	  {
		  path.add(node);
		  node = getParentNode(node);
	  }
	  path.add(head);
	  Collections.reverse(path);
	  return path;
  }

  public Collection<T> getSuccessors(T root) {
    Collection<T> successors = new ArrayList<T>();
    Tree<T> tree = getTree(root);
    if (null != tree) {
      for (Tree<T> leaf : tree.subtrees) {
        successors.add(leaf.head);
      }
    }
    return successors;
  }

  public Collection<Tree<T>> getSubTrees() {
    return subtrees;
  }

  public static <T> Collection<T> getSuccessors(T of, Collection<Tree<T>> in) {
    for (Tree<T> tree : in) {
      if (tree.locate.containsKey(of)) {
        return tree.getSuccessors(of);
      }
    }
    return new ArrayList<T>();
  }

  @Override
  public String toString() {
    return printTree(0);
  }

  private static final int indent = 2;

  private String printTree(int increment) {
    String s = "";
    String inc = "";
    for (int i = 0; i < increment; ++i) {
      inc = inc + " ";
    }
    s = inc + head;
    for (Tree<T> child : subtrees) {
      s += "\n" + child.printTree(increment + indent);
    }
    return s;
  }
}