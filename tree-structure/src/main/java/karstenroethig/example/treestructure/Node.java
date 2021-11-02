package karstenroethig.example.treestructure;

import java.util.ArrayList;
import java.util.List;

public class Node<T>
{
	private List<Node<T>> childs;
	private T data;

	public Node() {}

	public Node(T data)
	{
		setData(data);
	}

	public List<Node<T>> getChilds()
	{
		if (childs == null)
			childs = new ArrayList<>();

		return childs;
	}

	public void setChilds(List<Node<T>> childs)
	{
		this.childs = childs;
	}

	public int getNumberOfChilds()
	{
		if (childs == null)
			return 0;

		return childs.size();
	}

	public void addChild(Node<T> child)
	{
		if (childs == null)
			childs = new ArrayList<>();

		childs.add( child );
	}

	public void insertChildAt(int index, Node<T> child) throws IndexOutOfBoundsException
	{
		if (index == getNumberOfChilds())
			addChild( child );

		else
		{
			childs.get(index); // just to throw the exception and stop here
			childs.add(index, child);
		}
	}

	public void removeChildAt(int index) throws IndexOutOfBoundsException
	{
		getChilds().remove(index);
	}

	public T getData()
	{
		return data;
	}

	public void setData(T data)
	{
		this.data = data;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("Node {data=");
		sb.append(getData() != null ? getData().toString() : "null");
		sb.append(",[");

		int count = 0;

		for (Node<T> child : getChilds())
		{
			if (count > 0)
				sb.append(",");

			sb.append(child != null ? child.toString() : "null");
			count++;
		}

		sb.append("]}");

		return sb.toString();
	}
}
