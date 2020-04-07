/**
 * @author Jinyuan Hu
 * Creates a Probability of irregular walking according to the resistace at different direction.
 * Set (PPG)10 as sticky spots.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.regex.Pattern;

public class DlaTools {

	//Move Diffusing Object
	public static Collagen_Monomer Move(Collagen_Monomer moved)
	{		
		Random r = new Random();
		int num = r.nextInt(121);    //According to the resistance
		if (num< 116) {
			moved = DlaTools.moveZ(moved);//System.out.println(moved.m+" "+moved.n+" "+ moved.z);
		}else{                            //5 GXY see as 1 sphere, xy axis 1 step is 0.9 nm, z axis 1 step is 5*0.9 nm
			moved = DlaTools.moveMN(moved);//System.out.println(moved.m+" "+moved.n+" "+ moved.z);
		}
		return moved; 
	}
	
	
	//Move Diffusing Object on a Hexagonal Lattice
	public static Collagen_Monomer moveMN(Collagen_Monomer moved)
	{	
		Random r = new Random();
		int[] anglesx = {1,0,-1,-1,0,1};
		int[] anglesy = {0,1,1,0,-1,-1};

		int num = r.nextInt(anglesx.length);

		moved.m = moved.m + anglesx[num];
		moved.n = moved.n + anglesy[num];

		//Check Boundary Conditions       kill radius
		if(DlaTools.getDistance(moved,Driver.e.origin) > Driver.e.krad*Driver.e.krad)
		{
			moved = DlaTools.generateMNZch();
			//Driver.kills++;
			return moved;
		}
		return moved;

	}
	//Move Diffusing Object in the Z-Axis
	public static Collagen_Monomer moveZ(Collagen_Monomer moved)
	{
		Random r = new Random();
		int[] anglesz = {1,-1};
		int num = r.nextInt(anglesz.length);

		moved.z = moved.z + anglesz[num];

		//Check Boundary Conditions
		if(moved.z > Driver.e.kmaxz || moved.z < Driver.e.kminz)	
		{
			moved = DlaTools.generateMNZch();
			//Driver.kills++;
			return moved;
		}

		return moved;
	}
	//Determine Action Limited Step- Checks interactions 
	public static String checkInteraction(File file, Collagen_Monomer movedMonomer) throws Exception
	{
		int aggsize = Driver.dla.aggregate.size();
		int overlap = 0;	
		Collagen_Chain movedChain = new Collagen_Chain(Collagen_Chain.createChainZ(movedMonomer));

		//Search Dla.Aggregate for potential interactions
		//Overlap: Minimum of two 1's interacting to stick
		for(int j = aggsize-1; j>=0;j--)
		{

			if(DlaTools.getDistance(Driver.dla.aggregate.get(j).chain.get(0), movedMonomer) == 0 
					&& Math.abs(Driver.dla.aggregate.get(j).chain.get(0).z - movedMonomer.z) < Driver.dla.aggregate.get(j).chain.size())
				return "Collision";

			if(DlaTools.getDistance(Driver.dla.aggregate.get(j).chain.get(0), movedMonomer) == 1 
					&&  Math.abs(Driver.dla.aggregate.get(j).chain.get(0).z - movedMonomer.z) 
					< Driver.dla.aggregate.get(j).chain.size())
			{	

				overlap = overlap + DlaTools.checkOverlap(movedChain,Driver.dla.aggregate.get(j));

			}
		}
		
		double p = Math.random();
		if(overlap >= 4)
		{
			for(int j = aggsize-1; j>=0;j--)
				
				if (
						(Math.abs(Driver.dla.aggregate.get(j).chain.get(0).z - movedMonomer.z) == Driver.sequenceLength-2 )
						||Math.abs(Driver.dla.aggregate.get(j).chain.get(0).z - movedMonomer.z) == 0)
				{
					Driver.dla.aggregate.add(movedChain);
					DlaTools.appendCollagen_Chain(file, movedChain);
					Driver.e.UpdateEnv(movedChain);
					return "Add";
				}
		}
		return "Move";		
	}

	//Called in DlaTools.checkInteraction  //ºÏ≤È÷ÿ∫œ
	public static int checkOverlap(Collagen_Chain one, Collagen_Chain two)
	{
		int overlap = 0;
		int j = 0;
		int k = 0;

		while(j < one.chain.size() && k < two.chain.size())
		{
			if(one.chain.get(j).z > two.chain.get(k).z)
				k++;

			else if(one.chain.get(j).z < two.chain.get(k).z)
				j++;

			else if(one.chain.get(j).z == two.chain.get(k).z)
			{
				if(one.chain.get(j).charge == 1 &&  two.chain.get(k).charge == 1)
					overlap++;

				j++;
				k++;
			}
		}

		return overlap;

	}

	public static double getDistance(Collagen_Monomer one, Collagen_Monomer two)
	{
		return (two.m-one.m)*(two.m-one.m) + (two.n-one.n)*(two.n-one.n) + (two.m-one.m)*(two.n-one.n);
	}
	//generates mn- hexagonal coord. and z integer coordinates w/ charge of 0
	public static Collagen_Monomer generateMNZch()
	{	
		//Driver.numMonomers++;
		double theta = Math.random() * 6.28318531;

		double x = Driver.e.brad*Math.cos(theta);
		double y = Driver.e.brad*Math.sin(theta);
		int m = (int)Math.rint((x-(y/1.73205081)));
		int n = (int)Math.rint(2*y/1.73205081);
		Random rand = new Random();
		int r = rand.nextInt(4);
		int z = 0;
		switch(r)
		{
		case 0: z = Driver.e.bmnz;break;
		case 1: z = Driver.e.bmxz;break;
		case 2:
		case 3: z = (int)(Driver.e.bmnz + Math.random()*(Driver.e.bmxz - Driver.e.bmnz + 1));break;//Along Length of Hexagonal Cylinder
		}
		Collagen_Monomer mnz = new Collagen_Monomer(m,n,z,0);

		return mnz;

	}

	public static void appendCollagen_Chain(File file, Collagen_Chain one) throws Exception 
	{
		for(int j = 0; j < one.chain.size(); j++)
		{	
			appendCollagen_Monomer(file,one.chain.get(j));

		}
	}

	public static void appendCollagen_Monomer(File fileName, Collagen_Monomer text) throws Exception 
	{
		long fileLength = fileName.length();
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		raf.seek(fileLength);
		double x = text.m + (text.n*.5);
		double y = .5*text.n*1.73205081; 
		String Atom = "";

		switch(text.charge)
		{
		case 1: Atom = "Cp"; break;
		case 0: Atom = "Co"; break;
		case -1: Atom = "Cn"; break;
		}

		raf.writeBytes(Atom  + " " + x + " " + y + " " + text.z + "\n");
		raf.close();
	}

	public static double[][] readXYZdoubleArray(File xyzfile) throws IOException
	{
		String x, y, z;
		double dx, dy, dz;
		BufferedReader br = 
			new BufferedReader(
					new InputStreamReader(
							new FileInputStream(xyzfile)));
		int filelength = (int)xyzfile.length();
		double[][] agg = new double[filelength][3];
		String line = br.readLine();
		for(int j = 0; line !=null ; j++)
		{
			String xyz = line.substring(2,line.length());
			x = xyz.substring(0,xyz.indexOf(" "));
			String yz = xyz.substring(xyz.indexOf(" ")+1, xyz.length());
			yz.trim();
			y = yz.substring(0,yz.indexOf(" "));
			z = yz.substring(yz.indexOf(" ") + 1,yz.length());
			dx = Double.parseDouble(x);
			dy = Double.parseDouble(y);
			dz = Double.parseDouble(z);
			agg[j][0] = dx;
			agg[j][1] = dy;
			agg[j][2] = dz;		
			line = br.readLine();
		}
		return agg;
	}

	public static void stringTochargeSequence(String Sequence) 
	{

		if(Sequence.length() == 0)
		{
			throw new NoSuchElementException();
		}

		for(int k =0; k < Sequence.length(); k++)
		{
			if(Sequence.charAt(k) == '0')
			{
				Driver.charge.add(0);
			}
			if(Sequence.charAt(k) == '1')
			{
				Driver.charge.add(1);
			}
			if(Sequence.charAt(k) == '-')
			{
				Driver.charge.add(-1);
				k++;
			}
		}

		return;
	}
	//Given an xyxFile, gets the xyz Coords only
	public static void xyzCoords(File xyzFile, File coordLocation,String coordFileName) throws Exception
	{
		File xyzCoords = new File(coordFileName);
		FileReader fr = null;
		
		try {
			fr = new FileReader(xyzFile);
		} catch (FileNotFoundException e) {
			System.out.println("xyzFile not found");
		}
		
		LineNumberReader lr = new LineNumberReader(fr);

		int linenumber = 0;

		while (lr.readLine() != null){
			linenumber++;
		}
		linenumber--;//Due to the xyzFile protocal
		
		fr.close();
		lr.close();
		
		fr = new FileReader(xyzFile);
		lr = new LineNumberReader(fr);

		String regex = " "; 
		Pattern p = Pattern.compile(regex);		
		String line = lr.readLine();
		String[] splitLine = p.split(line);
		if(splitLine.length == 1)
		line = lr.readLine(); //Skip Line Due to xyzFile monomer count

		FileWriter outFile = new FileWriter(xyzCoords);
		PrintWriter out = new PrintWriter(outFile);	
		out.println("3");
		out.println(Integer.toString(linenumber));
		do
		{
			splitLine = p.split(line);
			out.println((double)Float.parseFloat(splitLine[1])+ " " + (double)Float.parseFloat(splitLine[2]) + " " + (double)Float.parseFloat(splitLine[3]));
		}while((line = lr.readLine())!= null);
		out.close();
		//return xyzCoords;

	}
}
