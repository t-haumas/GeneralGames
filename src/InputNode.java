
public class InputNode extends Node
{	
	public InputNode()
	{
		value = 0;
	}

	@Override
	// I really should make an inputlayer that extends layer.
	public void evaluate()
	{
		throw new RuntimeException("Cannot evaluate an input node.");
	}

	@Override
	public void toggleOneWeight() {
		throw new RuntimeException("Cannot toggle an input node's weight.");
		
	}
}
