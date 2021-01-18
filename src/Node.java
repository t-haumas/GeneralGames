
public abstract class Node
{
	protected double value;

	public double getValue()
	{
		return value;
	}
	
	public void setValue(double value)
	{
		this.value = value;
	}
	
	public abstract void evaluate();
	
	public abstract void toggleOneWeight();
}
