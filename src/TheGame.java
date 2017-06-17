import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TheGame {
	//= border
	//- valid empty space
	//1 player 1 current pos. X player 1 space.
	//2 player 2 current pos. O player 2 space.
	// P1 and P2 access H:p/10 V:p%10
	//player +100,openant +100 cevre +1 , player remove +1,openant remove +100 cevre +2,kose +3 ,tam kose +6 ,bosluk 0
	static int PlayerOneStarterPosition=43;
	static int PlayerTwoStarterPosition=45;
	static int Maxdepth=3;
	static int HeurasticPlayer=-100;
	static int HeurasticPlayerRemove=3;
	static int HeurasticOpenant=100;
	static int HeurasticOpenantAura=-2;
	static int HeurasticOpenantRemove=100;
	static int HeurasticOpenantRemoveAura=4;
	static int HeurasticInBorder=6;
	static int HeurasticOutBorder=100;
	static int HeurasticEmpty=2;
	
	
	
	 static char[][] board=new char[9][9];//This is 7x7 board
	 static int[][] HeurasticBoard=new int[9][9];//This is 7x7 board
	 static int P1;
	 static int oldP1;
	 static int P2;
	 static int oldP2;
	 static int player;
	 static int ai;
	 static int count;
	 static Scanner input = new Scanner(System.in);
	 
	
	 
	 
	public static void main(String[] args) {
		
		int answer;
			
		while(true){
			System.out.println("Welcome:\n1)Start New Game (Player versus Ai)\n2)Observe a Game(Ai versus Ai)\n3)Start New Game (Player versus Player)\n4)exit\nBy gokhan gobus");
			answer=input.nextInt();
			switch (answer) {
	         case 1:  NewGamePvAi();
	         		  continue;
	         case 2:  ObserveAivAi();
	                  continue;
	         case 3:  NewGamePvP();
	         		  continue;
	         case 4:  return;
	         			
			default: continue;
	     }
			
			
		}
		
		
		
		
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////
	private static void NewGamePvAi(){
		int answer;
		initboard();

		
		System.out.println("Welcome Choose Player 1 or Player 2");
		do{
			System.out.printf("Hint: Enter= 1 or 2\nChoose:");
			answer=input.nextInt();
			if(answer!=1 && answer!=2){
				System.out.printf("inValid input TryAgain\n");
			}
		}while(answer!=1 && answer!=2);
		
		
		if(answer==1){
			player=1;
			ai=2;
		}else{
			player=2;
			ai=1;
		}
		System.out.println("Lets Play...");	
		
		while(true){
			System.out.println("Turn : "+ (++count));
			printboard();
			
			if(player==1){
				if(GameIsOver(player)){
					System.out.println("Game Over Ai Win!!!");
					break;
				}else{
					playerMove(player);
				}
				
				
				if(GameIsOver(ai)){
					System.out.println("Game Over Player Win!!!");
					break;
				}else{
					aiMove(ai,player);
				}
			}else{
				if(GameIsOver(ai)){
					System.out.println("Game Over Player Win!!!");
					break;
				}else{
					aiMove(ai,player);
				}
				if(GameIsOver(player)){
					System.out.println("Game Over Ai Win!!!");
					break;
				}else{
					playerMove(player);
				}
			}
			
			
		}
		
		
	}
		
	
	private static void ObserveAivAi(){
		String answer;
		initboard();

		
		System.out.println("Welcome Observe Match\nEnter .)next player move\nEnter exit) Finish Observe Match\nEnter: ");

		player=1;
		ai=2;
		
		
		while(true){
			answer=input.next();
			
			player=1;
			ai=2;
			if(answer.equals("exit")){
				break;
			}else{
				System.out.println("Turn : "+ (++count));
				printboard();
			
			
				if(GameIsOver(player)){
					System.out.println("Game Over Ai 2 Win!!!");
					break;
				}else{
					aiMove(player,ai);
				}
				if(GameIsOver(ai)){
					System.out.println("Game Over Ai 1 Win!!!");
					break;
				}else{
					aiMove(ai,player);
				}
				System.out.printf("\nEnter: ");
			}
		}
	}
	private static void NewGamePvP(){
		int answer;//it is used
		initboard();

		
		
		System.out.println("Lets Play...(players)");	
		player=1;
		ai=2;
		
		while(true){
			System.out.println("Turn : "+ (++count));
			printboard();
			
			
			if(GameIsOver(player)){
				System.out.println("Game Over Player 2 Win!!!");
				break;
			}else{
				playerMove(player);
			}
			if(GameIsOver(ai)){
				System.out.println("Game Over Player 1 Win!!!");
				break;
			}else{
				playerMove(ai);
			}
			
			
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	public static void playerMove(int p){
		String move;
		boolean go=true;
		//Turn 1;
		System.out.println("Player "+ p +"  Turns - Move");	
		do{
			System.out.printf("\nEnter a valid move: ");
			move = input.next();
			if(inputIsValid(move)){
				if(moveIsValidForStr(move,p)){
					go=false;
				}else{
					go=true;
					System.out.printf("inValid move TryAgain\n");
					printboard();
				}
			}else{
				go=true;
				System.out.printf("inValid input TryAgain\n");
				printboard();
				
			}
			
		}while(go);
			
		doStep1ForPlayer(move,p);
		printboard();
		
		//Turn 2
		System.out.println("Player "+ p +"  Turns - Remove");
		go=true;
		
		do{
			System.out.printf("\nEnter a valid remove: ");
			move = input.next();
			if(inputIsValid(move)){
				if(removeIsValidForStr(move,p)){
					go=false;
				}else{
					go=true;
					System.out.printf("inValid remove TryAgain\n");
					printboard();
				}
			}else{
				go=true;
				System.out.printf("inValid input TryAgain\n");
				printboard();
				
			}
		}while(go);
		
		doStep2ForPlayer(move,p);
		printboard();
	}

	private static void doStep1ForPlayer(String move,int p){
		int h,v;
		h= cn(move.charAt(0));
		v= Character.getNumericValue(move.charAt(1));
		if(p==1){
			board[P1/10][P1%10]='-';
			board[h][v]='1';
			oldP1=P1;
			P1=h*10+v;
		}else{
			board[P2/10][P2%10]='-';
			board[h][v]='2';
			oldP2=P2;
			P2=h*10+v;
		}
	}
	private static void doStep2ForPlayer(String move,int p){
		int h,v;
		h= cn(move.charAt(0));
		v= Character.getNumericValue(move.charAt(1));
		if(p==1){
			board[h][v]='X';
		}else{
			board[h][v]='O';
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////
	public static void aiMove(int me,int op){
		String move;
		
		//Turn 1;
		System.out.println("Player "+ me +"  (Ai)Turns - Move");
		
		
		move=Astar(me,1);//burdaki 1 best move option ý
		System.out.println(move);
		doStep1ForAi(move,me);
		printboard();
		
		//Turn 2
		System.out.println("Player "+ me +"  (Ai)Turns - Remove");
		
		
		move=Astar(op,2);//burdaki 2 yapabildiði best remove
		System.out.println(move);
		
		doStep2ForAi(move,me);
		printboard();
	}


	private static void doStep1ForAi(String move,int p){
		int h,v;
		h= cn(move.charAt(0));
		v= Character.getNumericValue(move.charAt(1));
		if(p==1){
			board[P1/10][P1%10]='-';
			board[h][v]='1';
			oldP1=P1;
			P1=h*10+v;
		}else{
			board[P2/10][P2%10]='-';
			board[h][v]='2';
			oldP2=P2;
			P2=h*10+v;
		}
	}
	private static void doStep2ForAi(String move,int p){
		int h,v;
		h= cn(move.charAt(0));
		v= Character.getNumericValue(move.charAt(1));
		if(p==1){
			board[h][v]='X';
		}else{
			board[h][v]='O';
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	private static void initboard(){//Player 1 start D3(4,3) Player 2 start D5(4,5)
		int h,v;//h=horizantal , v=vertical
		P1=PlayerOneStarterPosition;
		oldP1=PlayerOneStarterPosition;
		P2=PlayerTwoStarterPosition;
		oldP2=PlayerTwoStarterPosition;
		count=0;
		for(h=0;h<9;h++){
			for(v=0;v<9;v++){
				if(h==0 || h==8 || v==0 || v==8){
					board[h][v]='=';
				}else if(h==P1/10 && v==P1%10){//Player 1
					board[h][v]='1';
				}else if(h==P2/10 && v==P2%10){//Player 2
					board[h][v]='2';
				}else{
					board[h][v]='-';
				}
			}
		}
	}
	
	private static void printboard(){
		int h,v;//h=horizantal , v=vertical
		String Lv="==1234567=\n";
		System.out.printf(Lv);
		for(h=0;h<9;h++){
			System.out.print(nc(h));
			for(v=0;v<9;v++){
				System.out.print(board[h][v]);
			}
			System.out.printf("\n");
		}
	}
	
	private static boolean GameIsOver(int p){
		int x;
		
		if(p==1){
			x=P1;
		}else{
			x=P2;
		}

		if(moveIsValid((x/10)+1,x%10,p)){
			return false;
		}

		if(moveIsValid((x/10)-1,x%10,p)){
			return false;
		}

		if(moveIsValid(x/10,(x%10)+1,p)){
			return false;
		}

		if(moveIsValid(x/10,(x%10)-1,p)){
			return false;
		}
		
		return true;
		
		
	}
	private static char nc(int i){
		 switch (i) {
         case 1:  return 'a';
                  
         case 2:  return 'b';
                  
         case 3:  return 'c';
                  
         case 4:  return 'd';
   
         case 5:  return 'e';
         
         case 6:  return 'f';
         
         case 7:  return 'g';
         
		default: return '=';
     }
		
	}
	private static int cn(char i){
		 switch (i) {
        case 'a':  return 1;
                 
        case 'b':  return 2;
                 
        case 'c':  return 3;
                 
        case 'd':  return 4;
  
        case 'e':  return 5;
        
        case 'f':  return 6;
        
        case 'g':  return 7;
        
		default: return 0;
		 }
    }
	private static boolean moveIsValid(int h,int v,int p){

		if(board[h][v]=='-' ){
			return moveIsSameHV(h,v,p);
		}else if(board[h][v]=='X' && p==1){
			return moveIsSameHV(h,v,p);
		}else if(board[h][v]=='O' && p==2){
			return moveIsSameHV(h,v,p);
		}else{
			return false;
		}
		
	}
	private static boolean moveIsSameHV(int h,int v,int p){
		if(p==1){
			if(((P1/10)+1==h || (P1/10)-1==h) && P1%10==v){
				return true;
			}
			if(((P1%10)+1==v || (P1%10)-1==v) && P1/10==h){
				return true;
			}
			return false;
		}else{
			if(((P2/10)+1==h || (P2/10)-1==h) && P2%10==v){
				return true;
			}
			if(((P2%10)+1==v || (P2%10)-1==v) && P2/10==h){
				return true;
			}
			return false;
		}
		
		
	}
	private static boolean moveIsValidForStr(String i,int p){
		int h,v;
		h= cn(i.charAt(0));
		v= Character.getNumericValue(i.charAt(1));
		return moveIsValid(h,v,p);
	}
	private static boolean removeIsValidForStr(String i,int p){
		int h,v;
		h= cn(i.charAt(0));
		v= Character.getNumericValue(i.charAt(1));
		return removeIsValid(h,v,p);
	}
	private static boolean removeIsValid(int h,int v,int p){

		if(board[h][v]=='-' ){
			if(p==1 && P1!=h*10+v && oldP1!=h*10+v ){
				return true;
			}
			if(p==2 && P2!=h*10+v && oldP2!=h*10+v){
				return true;
			}
			return false;
		}else{
			return false;
		}
		
	}
	
	private static boolean inputIsValid(String i){
		if(i.length()==2){
			if(i.charAt(0)=='a' || i.charAt(0)=='b' || i.charAt(0)=='c' || i.charAt(0)=='d' || i.charAt(0)=='e' || i.charAt(0)=='f' || i.charAt(0)=='g'){
				if(i.charAt(1)=='1' || i.charAt(1)=='2' || i.charAt(1)=='3' || i.charAt(1)=='4' || i.charAt(1)=='5' || i.charAt(1)=='6' || i.charAt(1)=='7'){ 
					return true;	
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7
	private static String Astar(int p,int option){// 1 move 2 remove
		AStarNode Sn;
		ArrayList<Integer> arrivedLocationList = new ArrayList<Integer>();
		ArrayList<Integer> moveableLocationList;
		ArrayList<AStarNode> list = new ArrayList<AStarNode>();
	
		boolean check;
		int h,v,plyr;
	
		String move="";
	
		int remove=0,i,k;
	
		int depth=0;
		
	
	
		if(p==1){
			plyr=P1;
		}else{
			plyr=P2;
		}
	
		initHboard(p);
	
	
	
		///S(n) depth 0
		Sn=new AStarNode(null,0,HeurasticBoard[plyr/10][plyr%10],plyr);
		arrivedLocationList.add(plyr);
		list.add(Sn);
		check=false;
		
		while(depth<Maxdepth){///depth 3 kadar yardýra biliyorsa tree yi ac less F cost a gore
			///
			
			remove=list.size();
			for(i=0;i<remove;i++){
			
				moveableLocationList=moveAbleLocations(p,list.get(i).getCord());
				if(moveableLocationList.size()==0){//final move ai is wining (maybe?)
					
					check=true;
					break;
					
				
				}
				for(k=0;k<moveableLocationList.size();k++){
					
					
					if(arrivedLocationList.contains(moveableLocationList.get(k))){
						if(1==moveableLocationList.size()){
							check=true;
							break;
						}
					}else{
						list.add(new AStarNode(list.get(i),list.get(i).getGvalue()+1,HeurasticBoard[(moveableLocationList.get(k))/10][(moveableLocationList.get(k))%10],moveableLocationList.get(k)));
						arrivedLocationList.add(moveableLocationList.get(k));
					}
				}
				
				
				moveableLocationList.clear();
			}
			if(check){
				break;
			}
			for(i=0;i<remove;i++){
				list.remove(0);
			}
		
		
			depth++;
		
		
		}
	
		if(option==1){////For Move
				int min=list.get(0).getFvalue();//patlama move 2 *-
															//   --
				AStarNode goal=list.get(0);
				for(i=1;i<list.size();i++){
					if(min>list.get(i).getFvalue()){
						min=list.get(i).getFvalue();
						goal=list.get(i);
					}
				}
		
				list.clear();
				arrivedLocationList.clear();
				k=getFirstItaParentCord(goal);
				h=k/10;
				v=k%10;
				
				move=Character.toString(nc(h));
				move+=Integer.toString(v);
				return move; //return best move
				
				
		}else{///For Remove
				ArrayList<AStarNode> sortedList=new ArrayList<AStarNode>();
				ArrayList<Integer> possibleChoose=new ArrayList<Integer>();
				
				sortedList.add(list.get(0));
				
				for(i=1;i<list.size();i++){//f value larýna gore sort luyorum
				
					check=true;
					for(k=0;k<sortedList.size();k++){
						if(list.get(i).getFvalue()<sortedList.get(k).getFvalue()){
							sortedList.add(k, list.get(i));
							check=false;
							break;
						}	
					}
				
					if(check){
						sortedList.add(list.get(i));
					}
				
				}
				//stage 1:sn-> best1 or best2 
				if(p==1){
					p=2;
				}else{
					p=1;
				}
				for(i=0;i<sortedList.size();i++){
					k=getFirstItaParentCord(sortedList.get(i));
					
					if(!possibleChoose.contains(k)){
						
						if(removeIsValidForAi(k/10,k%10,p)){
							move=Character.toString(nc(k/10));
							move+=Integer.toString(k%10);
							return move;//
						}
					}
				
				}
				for(i=0;i<sortedList.size();i++){
					k=getSecondItaParentCord(sortedList.get(i));
					
					if(!possibleChoose.contains(k)){
						
						if(removeIsValidForAi(k/10,k%10,p)){
							move=Character.toString(nc(k/10));
							move+=Integer.toString(k%10);
							return move;//
						}
					}
				
				}
				for(i=0;i<sortedList.size();i++){
					k=getThirdItaParentCord(sortedList.get(i));
					
					if(!possibleChoose.contains(k)){
						
						if(removeIsValidForAi(k/10,k%10,p)){
							move=Character.toString(nc(k/10));
							move+=Integer.toString(k%10);
							return move;//
						}
					}
				
				}
			
			
				
			
			
			}
		
		for(i=1;i<8;i++){
			for(k=1;k<8;k++){
				if(removeIsValidForAi(i,k,p)){
					move=Character.toString(nc(i));
					move+=Integer.toString(k);
					return move;//
				}
			}
		}
		System.out.println("Code patladý!!!");
		return move;//kod patlamasýn diye konulmus sus return zaten hic var mý yor buraya egerfunction duzgun cagrýlmýssa
	}
	private static int getFirstItaParentCord(AStarNode s){
	
		boolean go=true;
		do{
		
			if(s.parent==null){
			
				return s.getCord();
			}else{
				if(s.parent.parent==null){
					return s.getCord(); //return goal.parent.cord
				}else{
					s=s.parent;
				}
			}
		}while(go);
	
		return 0;
	}
	private static int getSecondItaParentCord(AStarNode s){
		
		boolean go=true;
		do{
		
			if(s.parent==null){
			
				return s.getCord();
			}else{
				if(s.parent.parent==null){
					return s.getCord(); //return goal.parent.cord
					
				}else{
					if(s.parent.parent.parent==null){
						return s.getCord(); //return goal.parent.cord
					}else{
						s=s.parent;
					}
				}
			}
		}while(go);
	
		return 0;
	}
	private static int getThirdItaParentCord(AStarNode s){
		
		boolean go=true;
		do{
		
			if(s.parent==null){
			
				return s.getCord();
			}else{
				if(s.parent.parent==null){
					return s.getCord(); //return goal.parent.cord
					
				}else{
					if(s.parent.parent.parent==null){
						return s.getCord(); //return goal.parent.cord
					}else{
						if(s.parent.parent.parent.parent==null){
							return s.getCord(); //return goal.parent.cord
						}else{
							s=s.parent;
						}
					}
				}
			}
		}while(go);
	
		return 0;
	}

	private static ArrayList<Integer> moveAbleLocations(int p,int cord){
		Random rand = new Random();
		
		ArrayList<Integer> m=new ArrayList<Integer>();
		ArrayList<Integer> n=new ArrayList<Integer>();
		int t=0;
		//h,v,p
		if(moveIsValidForAi((cord/10)+1,(cord%10),p)){
			t=(((cord/10)+1)*10)+(cord%10);
			m.add(t);
		}
		if(moveIsValidForAi((cord/10)-1,(cord%10),p)){
			t=(((cord/10)-1)*10)+(cord%10);
			m.add(t);
		}
		if(moveIsValidForAi((cord/10),(cord%10)+1,p)){
			t=((cord/10)*10)+((cord%10)+1);
			m.add(t);
		}
		if(moveIsValidForAi((cord/10),(cord%10)-1,p)){
			t=((cord/10)*10)+((cord%10)-1);
			m.add(t);
		}
		
		
		while(m.size()!=0){
			t  = rand.nextInt(100) + 1;
			n.add(m.get(t%m.size()));
			m.remove(t%m.size());
		}
		return n;
	}


	private static boolean moveIsValidForAi(int h,int v,int p){

		if(board[h][v]=='-' ){
			return true;
		}else if(board[h][v]=='X' && p==1){
			return true;
		}else if(board[h][v]=='O' && p==2){
			return true;
		}else{
			return false;
		}
	
	}

	private static boolean removeIsValidForAi(int h,int v,int p){

		if(p==1 && P1!=h*10+v && oldP1!=h*10+v ){
	
			if(board[h][v]=='-' ){
				return true;
			}else{
				return false;
			}
		}
		if(p==2 && P2!=h*10+v && oldP2!=h*10+v){
			
			if(board[h][v]=='-' ){
				return true;
			}else{
				return false;
			}
		}
		

		return false;
		
	
	}


	private static void initHboard(int p){
		//player +100,openant +100 cevre +1 , player remove +1,openant remove +100 cevre +2,kose +3 ,tam kose +6 ,bosluk 0
		int h,v,player,openant;//h=horizantal , v=vertical

		char playerR,openantR;
		if(p==1){
			player=P1;
			playerR='X';
			openant=P2;
			openantR='O';
		}else{
			player=P2;
			playerR='O';
			openant=P1;
			openantR='X';
		}
		for(h=0;h<9;h++){
			for(v=0;v<9;v++){
					HeurasticBoard[h][v]=HeurasticEmpty;
			}
		}
		for(h=0;h<9;h++){
			for(v=0;v<9;v++){
			
				if(h==1 || h==7 || v==1 || v==7){//koseler ust uste olabilir o yuzden ayrý
					HeurasticBoard[h][v]=HeurasticBoard[h][v]+HeurasticInBorder;
				}	
			
				if(h==0 || h==8 || v==0 || v==8){
					HeurasticBoard[h][v]=HeurasticBoard[h][v]+HeurasticOutBorder;
				}else if(h==player/10 && v==player%10){//Player
					HeurasticBoard[h][v]=HeurasticBoard[h][v]+HeurasticPlayer;
				}else if(h==openant/10 && v==openant%10){//Openent
					HeurasticBoard[h][v]=HeurasticBoard[h][v]+HeurasticOpenant;
					HeurasticBoard[h+1][v]=HeurasticBoard[h+1][v]+HeurasticOpenantAura;
					HeurasticBoard[h-1][v]=HeurasticBoard[h-1][v]+HeurasticOpenantAura;
					HeurasticBoard[h][v+1]=HeurasticBoard[h][v+1]+HeurasticOpenantAura;
					HeurasticBoard[h][v-1]=HeurasticBoard[h][v-1]+HeurasticOpenantAura;
				}else if(board[h][v]==playerR){//player remove
					HeurasticBoard[h][v]=HeurasticBoard[h][v]+HeurasticPlayerRemove;
				}else if(board[h][v]==openantR){//openant remove
					HeurasticBoard[h][v]=HeurasticBoard[h][v]+HeurasticOpenantRemove;
					HeurasticBoard[h+1][v]=HeurasticBoard[h+1][v]+HeurasticOpenantRemoveAura;
					HeurasticBoard[h-1][v]=HeurasticBoard[h-1][v]+HeurasticOpenantRemoveAura;
					HeurasticBoard[h][v+1]=HeurasticBoard[h][v+1]+HeurasticOpenantRemoveAura;
					HeurasticBoard[h][v-1]=HeurasticBoard[h][v-1]+HeurasticOpenantRemoveAura;
				}else{
					//bosluk
					//nothing...
				}
			}
		}
	}
	private static void printHboard(){//test icin
		int h,v;//h=horizantal , v=vertical
		String Lv="==1234567=\n";
		System.out.printf(Lv);
		for(h=0;h<9;h++){
			System.out.print(nc(h));
			for(v=0;v<9;v++){
				System.out.print(" "+HeurasticBoard[h][v]+" ");
			}
			System.out.printf("\n");
		}
	}

}
