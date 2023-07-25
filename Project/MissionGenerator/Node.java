package MissionGenerator;

import java.util.Objects;

public class Node {
	
	public int f, h, g, x, y;
	
	public Node(int x, int y){
		this.y = y;
		this.x = x;
	}
		@Override 
		public boolean equals (Object o) {
			if((o instanceof Node)) {
				Node n = (Node) o;
				if((x == n.x) && (y == n.y)){
					return true;
				}
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(x,y);
		}
}
