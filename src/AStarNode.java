import java.util.ArrayList;
import java.util.List;

public class AStarNode {
	public AStarNode parent;
	private int Gvalue;
	private int Hvalue;
	private int Fvalue;
	private int cord;
	
	public List<AStarNode> child;//??

	
	
	AStarNode(AStarNode p,int g,int h,int c){
		parent=p;
		Gvalue=g;
		Hvalue=h;
		cord=c;
		calculateF();
		child = new ArrayList<AStarNode>();
	}
	private void calculateF(){
		Fvalue=Gvalue+Hvalue;
	}
	public int getHvalue(){
		return Hvalue;
	}
	public int getGvalue(){
		return Gvalue;
	}
	public int getFvalue(){
		return Fvalue;
	}
	public int getCord(){
		return cord;
	}
}
