/**
 * @author Kenneth McGuinness
 * Creates a Diffusion Limited Aggregation Environment
 */
import java.util.ArrayList;
import java.util.Vector;

public class Combinations{                      //新建一个类Combinations
	public ArrayList<Vector<Integer>> Array ;    //Array是个含10个元素的list

	public Combinations(int size)                    //构造方法
	{
		Array = new ArrayList<Vector<Integer>>(size);   
	}
	public Combinations(Combinations combo)     //传入一个对象
	{
		Array = combo.Array;                     //combo对象的array赋值给array                    
	}
	//Returns a Combinations object, an ArrayList<Vector<Integer>> of binary values of length 10
	public static Combinations getBinaryArray(int binlength) //构建方法getbinaryarray，返回combinations
	{
		
		Combinations combination = new Combinations(Driver.arrayBinComb.length);  //创建一个combination对象
			//新建一个combination，赋值Driver下的arrayBinComb的长度
		for(int seq = 0; seq < Driver.arrayBinComb.length; seq++)
		{
			 String sb = Integer.toBinaryString(Driver.arrayBinComb[seq]);
			                
			 //Adds extra zeroes to (string)binary to create a string of length binlength
			 int zeros = binlength-sb.length(); 
			 if(sb.length() < binlength)
			 {
				 for(int l = 0; l< (zeros); l++)
				 {
					 sb = "0"+sb;
				 }
			 }
			
			 Vector<Integer> combo = new Vector<Integer>(binlength);
			
			 //Creates a Vector<Integer> from a string of binary;
			 for(int b = 0; b < binlength; b++)
			 {
				 int binInt = Character.digit(sb.charAt(b),10); //      charAt(b)更具下标获取sb的字符，转化为10进制，sb中字符在字符串中的下标的
				 combo.add(binInt);
			 }
			 //System.out.println(combo);	
			 combination.Array.add(combo);       //combo赋值给combination array属性
		}
	return combination;

	}
}
