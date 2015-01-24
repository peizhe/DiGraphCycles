import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* Basic idea:
 * For every edge AB,
 * Find all paths from B to A
 * 
 * To do this
 * 1) We do a DFS from B
 * 2) We also do a DFS from A, but in the opposite direction of the directed edge
 * 
 * By 1) We find paths to all nodes (unique) that can be reached from B
 * By 2) We find paths from all (unique) nodes that have a path to A
 * 
 * Then for each leaf node in the tree obtained by 1), we see if that node in the tree obtained by 2)
 * We also check if A is already there in the tree obtained from B (which means no need to look for it in the tree of A)*/


public class AllCycles {
	static Node nodes[];
	static List<Edge> edges;
	static List<Node> cycleNodes;
	static List<Node> consideredNodes;
	static List<Node> neighbourOf[];	//lists all nodes to whom a node is a neighbour, ie for a node A, all B such that B -> A
	static List<Node> neighbour[];
	static List<List<Node>> cycles = new ArrayList<List<Node>>();
	public static void main(String args[]) throws IOException
	{
		Node.lastID = -1;
		initialise();

		System.out.println("The graph (adjacency list):");
		dispGraph();

		edges = findAllEdges();

		boolean cyclesFound = false;

		neighbourList();
		for(Edge e : edges)
		{
			
			Node dest = e.from;
			Node source = e.to;

			Set<Node> notExpanded = new HashSet<Node>();
			Tree<Node> destTree = new Tree<Node>(dest);
			notExpanded.add(dest);
			while(notExpanded.size() != 0)
			{
				Object ch[] = notExpanded.toArray();
				for(Node n : neighbourOf[((Node)ch[0]).id])
				{
					if(!destTree.contains(n))
					{
						destTree.addLeaf((Node)ch[0], n);
						notExpanded.add(n);
					}

				}
				notExpanded.remove((Node)ch[0]);
			}


			notExpanded = new HashSet<Node>();
			Tree<Node> sourceTree = new Tree<Node>(source);
			notExpanded.add(source);
			while(notExpanded.size() != 0)
			{
				Object ch[] = notExpanded.toArray();
				for(Node n : neighbour[((Node)ch[0]).id])
				{
					if(!sourceTree.contains(n))
					{
						sourceTree.addLeaf((Node)ch[0], n);
						notExpanded.add(n);
					}

				}
				notExpanded.remove((Node)ch[0]);
			}


			for(Node n : sourceTree.getNodes())
			{
				if(destTree.contains(n))
				{
					List<Node> a1 = sourceTree.pathToNode(n);
					if(a1.contains(dest))
					{
						List<Node> cycle = new ArrayList<Node>();

						cycle.addAll(a1.subList(0, a1.indexOf(dest)));
						cycle.add(dest);
						if(isMinimalCycle(cycle) && isUniqueCycle(cycle))
						{
							if(!cyclesFound)
								System.out.println("\nCycles:");
							cyclesFound = true;
							cycles.add(cycle);
							System.out.println(cycle);
						}
						continue;
					}
					List<Node> a2 =  destTree.pathToNode(n);
					Collections.reverse(a2);
					List<Node> cycle = new ArrayList<Node>();
					cycle.addAll(a1);
					a2.remove(a2.get(0));
					cycle.addAll(a2);
					if(isMinimalCycle(cycle) && isUniqueCycle(cycle))
					{
						if(!cyclesFound)
							System.out.println("\nCycles:");
						cyclesFound = true;
						cycles.add(cycle);
						System.out.println(cycle);
					}
				}				
			}
		}
		if(!cyclesFound)
			System.out.println("\nNo cycles found");
	}

	static void initialise() throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("How many nodes? ");
		int n = Integer.parseInt(br.readLine());
		nodes = new Node[n];
		for(int i = 0; i < nodes.length ; i++)
		{
			nodes[i]= new Node();
		}
		System.out.println(n + " nodes created. Numbering starts from 0");
		System.out.println("Insert edges (Enter -1 to exit at any time)...");
		while(true)
		{
			System.out.print("\nEnter \"from\" node: ");
			int from = Integer.parseInt(br.readLine());
			if(from == -1)
				break;
			System.out.print("Enter \"to\" node: ");
			int to = Integer.parseInt(br.readLine());
			if(to == -1)
				break;
			nodes[from].addNeighbour(new Node[]{nodes[to]});

		}
		System.out.println();

		/*
//		Sample graph: 
		nodes = new Node[7];
		for(int i = 0; i < nodes.length ; i++)
		{
			nodes[i]= new Node();

		}

		nodes[0].addNeighbour(new Node[]{nodes[1], nodes[2]});
		nodes[1].addNeighbour(new Node[]{nodes[3]});
		nodes[2].addNeighbour(new Node[]{nodes[1], nodes[4]});
		nodes[3].addNeighbour(new Node[]{nodes[5]});
		nodes[4].addNeighbour(new Node[]{nodes[0], nodes[1], nodes[2], nodes[3]});
		nodes[5].addNeighbour(new Node[]{nodes[6]});
		nodes[6].addNeighbour(new Node[]{nodes[3], nodes[4]});*/
		
		//Another sample graph:
		/*nodes = new Node[4];
		for(int i = 0; i < nodes.length ; i++)
		{
			nodes[i]= new Node();
		}

		nodes[0].addNeighbour(new Node[]{nodes[1]});
		nodes[1].addNeighbour(new Node[]{nodes[2],nodes[0]});
		nodes[2].addNeighbour(new Node[]{nodes[3], nodes[1]});
		nodes[3].addNeighbour(new Node[]{nodes[0]});
		
		//another example that wouldn't work with leafNodes
		/*nodes = new Node[8];
		for(int i = 0; i < nodes.length ; i++)
		{
			nodes[i]= new Node();

		}

		nodes[0].addNeighbour(new Node[]{nodes[1], nodes[5]});
		nodes[1].addNeighbour(new Node[]{nodes[2], nodes[0]});
		nodes[2].addNeighbour(new Node[]{nodes[3]});
		nodes[3].addNeighbour(new Node[]{nodes[4]});
		nodes[4].addNeighbour(new Node[]{nodes[7]});
		nodes[5].addNeighbour(new Node[]{nodes[6]});
		nodes[6].addNeighbour(new Node[]{nodes[7]});
		nodes[7].addNeighbour(new Node[]{nodes[4]});*/
		
		

	}

	static boolean isUniqueCycle(List<Node> cycle)
	{
		for (List<Node> c : cycles)
			if(c.containsAll(cycle) && cycle.containsAll(c))
				return false;
		return true;
	}

	static boolean isMinimalCycle(List<Node> cycle)
	{
		for(Node a : cycle)
		{
			List<Node> tmp = new ArrayList<Node>();
			tmp.addAll(cycle);
			tmp.remove(a);
			if(tmp.contains(a))
				return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	static void neighbourList()
	{
		neighbourOf = new List[nodes.length];
		neighbour = new List[nodes.length];
		for(int i = 0; i < nodes.length; i ++)
		{
			neighbourOf[i] = new ArrayList<Node>();
			neighbour[i] = new ArrayList<Node>();
		}
		for(Node n : nodes)
		{
			if(n != null)
				for(Edge e : edges)
				{
					if(e.to == n)
						neighbourOf[n.id].add(e.from);
					if(e.from == n)
						neighbour[n.id].add(e.to);
				}
		}
	}

	static List<Edge> getNeighbourEdges(Node a)
	{

		List<Edge> neighbourEdges = new ArrayList<Edge>();

		for(Edge e : edges)
		{
			if(!consideredNodes.contains(e.from) && e.from.id == a.id)
				neighbourEdges.add(e);
		}
		consideredNodes.add(a);
		return neighbourEdges;
	}


	static List<Edge> findAllEdges()
	{
		List<Edge> allEdges = new ArrayList<Edge>();
		for(int i = 0; i < nodes.length; i++)
		{
			for(Node a : nodes[i].neighbours)
				allEdges.add(new Edge(nodes[i],a));
		}
		return allEdges;
	}
	
	static void dispGraph()
	{
		for(int i = 0; i < nodes.length; i++)
		{
			System.out.print(nodes[i]+" -> ");
			for(Node a : nodes[i].neighbours)
				System.out.print(" "+a);
			System.out.println("");
		}
	}
}
