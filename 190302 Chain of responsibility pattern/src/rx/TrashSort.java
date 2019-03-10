package rx;

import java.util.Arrays;

import net.mindview.util.Enums;

enum TrashType {PAPER, GREEN_GLASS, TRANSPARENT_GLASS, GLASS,
	BATTERY, BIOLOGICAL, MIXED, UNKNOWN1, UNKNOWN2, UNKNOWN3}

class Trash {
	TrashType type;
	public Trash() { 
		this.type = random();
	}
	public Trash(TrashType type) {
		this.type = type;
	}
	public static TrashType random() {
		return Enums.random(TrashType.class);
	}
	public String toString() {
		return "#" + type;
	}
}

public class TrashSort {
	enum TrashHandler {
		PAPER_HANDLER {
			public boolean handle(Trash trash) {
				if (trash.type == TrashType.PAPER) {
					System.out.println("Utilizing as paper.");
					return true;
				}
				return false;
			}
		},
		BOTTLE_HANDLER {
			public boolean handle(Trash trash) {
				if (trash.type == TrashType.GREEN_GLASS) {
					System.out.println("Utilizing as bottle.");
					return true;
				}
				return false;
			}
		},
		GLASS_HANDLER {
			public boolean handle(Trash trash) {
				if (trash.type == TrashType.GLASS) {
					System.out.println("Utilizing as glass.");
					return true;
				}
				return false;
			}
		},
		BATTERY {
			public boolean handle(Trash trash) {
				if (trash.type == TrashType.BATTERY) {
					System.out.println("Utilizing as battery.");
					return true;
				}
				return false;
			}
		},
		BIOLOGICAL_HANDLER {
			public boolean handle(Trash trash) {
				if (trash.type == TrashType.BIOLOGICAL) {
					System.out.println("Utilizing as bio products.");
					return true;
				}
				return false;
			}
		},
		MIXED_HANDLER {
			public boolean handle(Trash trash) {
				System.out.println("Utilizing as mixed trash.");
				return true;
			}
		};
		public abstract boolean handle(Trash trash);
		public static void handleChain(Trash trash) {
			System.out.print(trash);
			for(TrashHandler handler: TrashHandler.values()) {
				System.out.print(".");
				if(handler.handle(trash)) 
					return;
			}
		}
	}


	public static void main(String[] args) {
		Trash[] trashCont = new Trash[10];
		for(int i = 0; i < trashCont.length; i++) {
			trashCont[i] = new Trash();
		}
		System.out.println(Arrays.toString(trashCont));
		for(Trash trash : trashCont) 
			TrashHandler.handleChain(trash);
	}	

}
