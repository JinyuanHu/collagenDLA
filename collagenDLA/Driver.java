
import java.io.File;                    
import java.io.FileWriter;
import java.util.Vector;                  

public class Driver {                     

	public static Collagen_Aggregate dla = new Collagen_Aggregate();   
	public static Environment e = new Environment(10,10); 
	public static Collagen_Monomer lastmove = new Collagen_Monomer();
	public static Collagen_Chain tempCollagen_Chain = new Collagen_Chain();    
	public static Vector<Integer> charge = new Vector<Integer>() ;
	public static int sequenceLength = 0;
	public static int chains = 0;               
	public static Collagen_Monomer origin = new Collagen_Monomer(0,0,0);
	public static File file;
	public static File folder;
	public static String combination = "";

	//Hard coded binary sequences containing a minimum of two 1's because only one 1 forms a sheet
	public static int[] arrayBinComb =  {387,12291,393219};  
	
	public static void main(String[] Args) throws Exception    
	{
			
		//Instantiate Chain Variables     //ʵ����������
		Driver.sequenceLength = 19;       //��������
		System.out.print("Enter number of Chains: ");//������������
		Driver.chains = IO.readInt();    //��ȡ����������ֵ��chains
		System.out.print("Enter number of simulations per combination: ");    //ÿ��combinationģ��Ĵ���
		int simsPercombo = IO.readInt();      
		
		//These start and end values are based on the location within the Driver.arrayBinComb - refer to hard coded array above for location
		System.out.println("Enter Combo: ");    //�ҵ������Ʊ�����ѧ�Ķ�Ӧλ�ӣ�Ȼ������λ�����
		int combost = IO.readInt();
		
		//File checking does not occur - be careful not to overwrite files //����ļ���������ԭ�ļ�
		File dir1 = new File (".");
		Driver.folder = new File(dir1.getCanonicalPath()+ "/xyzFiles");  
		Driver.folder.mkdir();


		//ArrayList of Binary Vector Sequences of Driver.length    
		Combinations combo = new Combinations(Combinations.getBinaryArray(Driver.sequenceLength));
		Driver.charge.addAll(combo.Array.get(combost));
		Driver.combination = Driver.charge.toString(); 			

		//sim iteration# formats filename
		for(int siter = 0; siter < simsPercombo; siter++)
		{
			//Seed the Environment
			Driver.combination = Driver.charge.toString(); 
			Collagen_Chain one = new Collagen_Chain(Collagen_Chain.createChainZ(Driver.origin));
			Driver.dla.aggregate.add(one); 
			Driver.file = new File(Driver.folder,Driver.arrayBinComb[combost]+ "_" + siter + "_" + Driver.combination + ".xyz");	 
			FileWriter writer = new FileWriter(Driver.file);
			writer.write((Driver.chains*Driver.sequenceLength) + "\n");
			writer.close();

			DlaTools.appendCollagen_Chain(Driver.file,one);
			Driver.e.UpdateEnv(one);

			//Cycle Check
			for(int chiter = 1; chiter < Driver.chains; chiter++)
			{		
				String add = null;   // Default initialization
				boolean addchain = false;

				Collagen_Monomer birthmonomer = new Collagen_Monomer(DlaTools.generateMNZch());

				while(!addchain)
				{	
					birthmonomer = DlaTools.Move(birthmonomer);
					Collagen_Monomer temp = new Collagen_Monomer(birthmonomer);		
					

					double distance = DlaTools.getDistance(birthmonomer,Driver.e.origin);

					//Checks interactions if near aggregate 
					if(birthmonomer.z < (Driver.e.maxz+1) && birthmonomer.z> (Driver.e.minz-Driver.sequenceLength-1)) 
					{
						if(distance <= (Driver.e.longestrad + 1)*(Driver.e.longestrad + 1))
						{	
							add = DlaTools.checkInteraction(Driver.file,birthmonomer);
						}								
					}

					if(add == "Add")
					{
						addchain = true;
					}

					else if(add == "Collision")
					{
						birthmonomer = temp;
					}
					//System.out.println(add);
				}
			}
			
			//Reset Environment
			Driver.e.reset();
			Driver.e = new Environment(10,10);
			Driver.dla = new Collagen_Aggregate(Driver.chains);
		}
		Driver.charge.clear();
	}

}


