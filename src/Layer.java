import java.util.Arrays;
import java.util.LinkedList;

public class Layer
{
	public static boolean anyInstantiated = false;
	
	private Node[] nodes;
	private int size;
	
	public Layer(int numNodes, Layer prevLayer)
	{
		size = numNodes;
		nodes = new Node[numNodes];
		if (anyInstantiated)
		{
			for (int i = 0; i < size; i++)
			{
				nodes[i] = new NNNode(prevLayer);
			}
		}
		else
		{
			throw new RuntimeException("A not input layer has been instantiated but there's no input layer.");
		}
		anyInstantiated = true;
	}
	
	public Layer(LinkedList<InputNode> myNodes)
	{
		if (anyInstantiated)
		{
			throw new RuntimeException("A second input layer is trying to be made.");
		}
		for (int i = 0; i < myNodes.size(); i++)
		{
			nodes[i] = myNodes.get(i);
		}
		size = myNodes.size();
		anyInstantiated = true;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public Node[] getNodes()
	{
		return nodes;
	}

	public void evaluate()
	{
		for (Node node : nodes)
		{
			node.evaluate();
		}
	}
}
