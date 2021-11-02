package karstenroethig.example.treestructure;

public class Leaf<T>
{
	private T data;

	public Leaf() {}

	public Leaf(T data)
	{
		setData(data);
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

		sb.append("Leaf {data=" );
		sb.append(getData() != null ? getData().toString() : "null");
		sb.append("}");

		return sb.toString();
	}
}
