class Edge
{
	Node from;
	Node to;
	Edge()
	{
		from = null;
		to = null;
	}
	Edge(Node from, Node to)
	{
		this.from = from;
		this.to= to;
	}
	@Override
	public String toString()
	{
		return from.toString() + " -> " + to.toString();
	}
	
}
