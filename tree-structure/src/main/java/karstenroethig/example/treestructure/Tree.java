package karstenroethig.example.treestructure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import karstenroethig.example.treestructure.iterator.IIteratorStrategy;

public class Tree<T>
{
	private Node<T> rootNode;

	public Tree() {}

	public Tree(Node<T> rootNode)
	{
		this.rootNode = rootNode;
	}

	public List<Node<T>> getNodes(IIteratorStrategy<T> navigationStrategy)
	{
		List<Node<T>> nodes = new ArrayList<>();
		Iterator<Node<T>> iterator = navigationStrategy.createIterator(this);

		while (iterator.hasNext())
		{
			nodes.add(iterator.next());
		}

		return nodes;
	}

	public Node<T> getRootNode()
	{
		return rootNode;
	}

	public void setRootNode(Node<T> rootNode)
	{
		this.rootNode = rootNode;
	}
}
