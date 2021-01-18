import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public abstract class Network
{
//	private LinkedList<Layer> layers;
	private Layer inputLayer;
	private Layer outputLayer;
	
	public Network(int numInputs, int numOutputs)
	{		
		inputLayer = new Layer(getInputNodes(numInputs));
		outputLayer = new Layer(numOutputs, inputLayer);
	}
	
	public void evaluate()
	{
		Iterator<Double> inputs = getInputs().iterator();
		for (int i = 0; i < inputLayer.getNodes().length; i++)
		{
			inputLayer.getNodes()[i].setValue(inputs.next());
		}
		outputLayer.evaluate();
	}
	
	public void toggleSomeWeights(int maxNumberToToggle)
	{
		Random rand = new Random();
		int numToToggle = rand.nextInt(maxNumberToToggle + 1);
		for (int i = 0; i < numToToggle; i++)
		{
			outputLayer.getNodes()[rand.nextInt(outputLayer.getSize())].toggleOneWeight();
		}
	}
	
	public int getGreatestOutput()
	{
		evaluate();
		
		double maxValue = -1.1;
		int maxValueIndex = -1;
		for (int i = 0; i < outputLayer.getSize(); i++)
		{
			double currentValue = outputLayer.getNodes()[i].getValue();
			if (currentValue > maxValue)
			{
				maxValue = currentValue;
				maxValueIndex = i;
			}
		}
		return maxValueIndex;
	}

	private LinkedList<InputNode> getInputNodes(int numInputs)
	{
		LinkedList<InputNode> inputNodes = new LinkedList<InputNode>();
		for (int i = 0; i <= numInputs; i++)
		{
			inputNodes.add(new InputNode());
		}
		return inputNodes;
	}
	
	private LinkedList<Double> getInputs()
	{
		LinkedList<Double> inputs = new LinkedList<Double>();
		inputs.add(Double.valueOf(1));
		for (double x : getExternalInputs())
		{
			inputs.add(x);
		}
		return inputs;
	}

	protected abstract LinkedList<Double> getExternalInputs();
}
