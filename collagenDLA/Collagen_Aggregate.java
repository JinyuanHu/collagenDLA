import java.util.ArrayList;

public class Collagen_Aggregate {
	
	public int length = 0;
	
	public ArrayList<Collagen_Chain> aggregate;
	
	public Collagen_Aggregate() 
	{
		this.aggregate = new ArrayList<Collagen_Chain>();
	}
	
	public Collagen_Aggregate(int size)
	{
		this.aggregate = new ArrayList<Collagen_Chain>(size);
	}
	
	public Collagen_Aggregate(Collagen_Aggregate one)
	{
		this.aggregate = one.aggregate;
	}

	public int length()
	{
		this.length = Driver.e.maxz - Driver.e.minz;
		return length;
	}	
}
