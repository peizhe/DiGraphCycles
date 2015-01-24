import java.util.ArrayList;
import java.util.List;


public class Node {
	static int lastID;
	int id;
	List<Node> neighbours;
	Node()
	{
		id = lastID + 1;
		lastID++;
		neighbours = new ArrayList<Node>();
	}
	void addNeighbour(Node a)
	{
		neighbours.add(a);
	}
	void addNeighbour(Node[] arr)
	{
		for(Node a:arr)
			neighbours.add(a);
	}
	@Override
	public String toString()
	{
		return id+"";
	}
	

}
