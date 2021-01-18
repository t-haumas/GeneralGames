import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class NNNode extends Node
{
	private double myValue;
	private Node[] previousNodes;
	private double[] previousWeights;
	private boolean[] prevWeightsEnabled;
	private int previousLayerSize;
	
	public NNNode(Layer previousLayer)
	{
		previousLayerSize = previousLayer.getSize();
		Random rand = new Random();
		myValue = 0;
		previousNodes = new Node[previousLayerSize];
		previousWeights = new double[previousLayerSize];
		prevWeightsEnabled = new boolean[previousLayerSize];
		Iterator<Node> nodeGetter = Arrays.asList(previousLayer.getNodes()).iterator();
		for (int i = 0; i < previousLayerSize; i++)
		{
			previousWeights[i] = rand.nextDouble() * 2 - 1;
			prevWeightsEnabled[i] = false;
			previousNodes[i] = nodeGetter.next();
		}
	}
	
	public double getValue()
	{
		return myValue;
	}
	
	public void evaluate()
	{
		double myNextValue = 0;
		int nodesCounted = 0;
		
		for (int i = 0; i < previousLayerSize; i++)
		{
			if (prevWeightsEnabled[i])
			{
				nodesCounted++;
				myNextValue += previousWeights[i] * previousNodes[i].getValue();
			}
		}
		
		myNextValue /= nodesCounted;
		myValue = myNextValue;
	}
	
	public void toggleOneWeight()
	{
		Random rand = new Random();
		prevWeightsEnabled[rand.nextInt(previousLayerSize)] = ! prevWeightsEnabled[rand.nextInt(previousLayerSize)];
	}
}
