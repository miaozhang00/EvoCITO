package lcs;

public class LCS {

	public static int[][] genLCS(String str1, String str2){
		
		int[][] opt = new int[str2.length()+1][str1.length()+1];
		int[][] temp = new int[str2.length()+1][str1.length()+1];
		
		for(int i=0; i <= str2.length(); i++){
			opt[i][0] = 0;
		}
		
		for(int j=0; j <= str1.length(); j++){
			opt[0][j] = 0;
		}
		
		for(int j=1; j <= str1.length(); j++){
			for(int i=1; i <= str2.length(); i++){
				if(str2.charAt(i-1)== str1.charAt(j-1)){
					opt[i][j] = opt[i-1][j-1]+1;
					temp[i][j] = 1;
				}else{
					opt[i][j] = ( opt[i-1][j] >= opt[i][j-1] ? opt[i-1][j] : opt[i][j-1] );
					temp[i][j] = ( opt[i-1][j] >= opt[i][j-1] ? 0 : -1);
				}
			}
		}
		
		return temp;
	}
	
	public static void printLCS(int[][] b, String str1, int i, int j){
		
		LinkLCS linkLCS = new LinkLCS();
		if(i==0 || j==0){
			return;
		}
		if(b[i][j] == 1){			
			linkLCS.addFirstNode(str1.charAt(j-1));
			printLCS(b, str1, i-1, j-1); 					
		}else if(b[i][j] == 0){
			printLCS(b, str1, i-1, j);
		}else
			printLCS(b, str1, i, j-1);
		
		linkLCS.displayAllNodes();  
	}
	
	public static void main(String[] args) {  
		  
        String str1 = new String("abcbdab");  
        String str2 = new String("bdcabad");  
        
        int [][] temp = genLCS(str1, str2);              
        printLCS(temp,str1,str1.length(),str2.length() );
    }  


	public static class Node {  
		protected Node next; //指针域  
		protected char data;//数据域  
      
		public Node( char data) {  
          this.data = data;  
		}  
      
		//显示此节点  
		public void display() {  
			System.out.print(data + " ");  
		}  
	}  
	
	public static class LinkLCS {  
		public Node first; // 定义一个头结点  
		private int pos = 0;// 节点的位置  
 
		public LinkLCS() {  
          this. first = null;  
		}  
 
		// 插入一个头节点  
		public void addFirstNode(char data) {  
			Node node = new Node(data);  
			node. next = first;  
			first = node;  
		}
		
		public void displayAllNodes() {  
	          Node current = first;  
	           while (current != null) {  
	              current.display();  
	              current = current. next;  
	          }  
	     }  
	}
}