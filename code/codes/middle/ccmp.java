
class ccmp{
	public static String mac="110100100111101111000001101010111110100100010011";
	public static String masterkey="z3dg35dg";
	public static String ANounce="";
    public static String CNounce="";
    public static String Tk="";
    public static char [] temp=new char[200];
    public static String workN(int n) {
    	n=n%1048576;
    	n=n+1048576;
    	n=n%1048576;
    	int i=19;
    	for(int j=0;j<20;j++) {
    		temp[j]='0';
    	}
    	while (n>0) {
    		temp[i]=(char)(n%2+'0');
    		n/=2;
    		i--;
    	}
    	String s="";
    	for(i=0;i<20;i++) {
    		s=s+temp[i];
    	}
    	return s;
    }
    public static String workT() {
    	int i=19;
    	int Ak=Integer.valueOf(ANounce);
    	int Ck=Integer.valueOf(CNounce);
    	for(int j=0;j<128;j++) {
    		temp[j]='0';
    	}
    	while (Ak>0) {
    		temp[i]=(char)(Ak%2+'0');
    		Ak/=2;
    		i--;
    	}
    	i=39;
    	while (Ck>0) {
    		temp[i]=(char)(Ck%2+'0');
    		Ck/=2;
    		i--;
    	}
    	int co=0;
    	for(int k=0;k<masterkey.length();k++) {
    		co=co*10+masterkey.charAt(k);
    		co=co%1048576;
    	}
    	i=59;
    	while (co>0) {
    		temp[i]=(char)(co%2+'0');
    		co/=2;
    		i--;
    	}
    	Tk="";
    	for(int j=0;j<60;j++) {
    		Tk=Tk+temp[j];
    	}
    	return Tk;
    }
	public static String StringT(int Nonce) {
		return mac+workN(Nonce)+workT();
	}
	public static String xo(String tt,int Nonce) {
		String text=tt;
		
		String s="",s1=StringT(Nonce);
	
		for(int i=0;i<128;i++) {
			s=s+(((text.charAt(i)-'0')^(s1.charAt(i)-'0')));
		}
		return s;
	}
}
