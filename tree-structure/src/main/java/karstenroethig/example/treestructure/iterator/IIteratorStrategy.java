package karstenroethig.example.treestructure.iterator;

import java.util.Iterator;

import karstenroethig.example.treestructure.Node;
import karstenroethig.example.treestructure.Tree;

public interface IIteratorStrategy<T>
{
	public Iterator<Node<T>> createIterator(Tree<T> tree);
}
