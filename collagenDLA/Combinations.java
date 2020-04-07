/**
 * @author Kenneth McGuinness
 * Creates a Diffusion Limited Aggregation Environment
 */
import java.util.ArrayList;
import java.util.Vector;

public class Combinations{                      //�½�һ����Combinations
	public ArrayList<Vector<Integer>> Array ;    //Array�Ǹ���10��Ԫ�ص�list

	public Combinations(int size)                    //���췽��
	{
		Array = new ArrayList<Vector<Integer>>(size);   
	}
	public Combinations(Combinations combo)     //����һ������
	{
		Array = combo.Array;                     //combo�����array��ֵ��array                    
	}
	//Returns a Combinations object, an ArrayList<Vector<Integer>> of binary values of length 10
	public static Combinations getBinaryArray(int binlength) //��������getbinaryarray������combinations
	{
		
		Combinations combination = new Combinations(Driver.arrayBinComb.length);  //����һ��combination����
			//�½�һ��combination����ֵDriver�µ�arrayBinComb�ĳ���
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
				 int binInt = Character.digit(sb.charAt(b),10); //      charAt(b)�����±��ȡsb���ַ���ת��Ϊ10���ƣ�sb���ַ����ַ����е��±��
				 combo.add(binInt);
			 }
			 //System.out.println(combo);	
			 combination.Array.add(combo);       //combo��ֵ��combination array����
		}
	return combination;

	}
}
