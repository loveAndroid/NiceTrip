package azir.proxy;

public class Real implements Inter{
	
	@Override
	public String get(){
		return "i am real";
	}
	
	
	public int get2(){
		return 1;
	}
	
}
