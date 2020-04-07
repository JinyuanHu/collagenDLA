class Collagen_Monomer
	{
       public int m,n,z;
       public int charge;
	   
	   public Collagen_Monomer()
	   {
		   m = 0;
		   n = 0;
		   z = 0;
		   charge = 0;
	   }
	   public Collagen_Monomer(int cm, int cn, int cz)
	   {
		   m = cm;
		   n = cn;
		   z = cz;
	   }
	   public Collagen_Monomer(int cm, int cn, int cz, int ch)
	   {
		   m = cm;
		   n = cn;
		   z = cz;
		   charge = ch;
	   }
	   	   
	   public Collagen_Monomer(Collagen_Monomer xyz)
	   {
	   m = xyz.m;
	   n = xyz.n;
	   z = xyz.z;
	   charge = xyz.charge;
	   }
	   
	   public Collagen_Monomer(int[] mnzch)
	   {
		   m = mnzch[0];
		   n = mnzch[1];
		   z = mnzch[2];
		   charge = mnzch[3];
	   }

	}
