import java.util.Random;

enum Result { WIN, DRAW, LOSE } 

abstract class Item {
	abstract public Result compete(Item it);
	abstract public Result eval(Rock it);
	abstract public Result eval(Paper it);
	abstract public Result eval(Scissors it);
	public String toString() {
		return this.getClass().getSimpleName();
	}
}

class Rock extends Item {

	@Override
	public Result compete(Item it) {
		return it.eval(this);
	}

	@Override
	public Result eval(Rock it) {		
		return Result.DRAW;
	}

	@Override
	public Result eval(Paper it) {		
		return Result.LOSE;
	}

	@Override
	public Result eval(Scissors it) {
		return Result.WIN;
	}
	
}

class Paper extends Item {

	@Override
	public Result compete(Item it) {
		return it.eval(this);
	}

	@Override
	public Result eval(Rock it) {		
		return Result.WIN;
	}

	@Override
	public Result eval(Paper it) {		
		return Result.DRAW;
	}

	@Override
	public Result eval(Scissors it) {
		return Result.LOSE;
	}
	
}

class Scissors extends Item {

	@Override
	public Result compete(Item it) {
		return it.eval(this);
	}

	@Override
	public Result eval(Rock it) {		
		return Result.LOSE;
	}

	@Override
	public Result eval(Paper it) {		
		return Result.WIN;
	}

	@Override
	public Result eval(Scissors it) {
		return Result.DRAW;
	}
	
}



public class RoShamBo {
	public static Item getRandom() {
		int i = new Random().nextInt(3);
		switch(i) {		
		case 0:
			return new Scissors();
		case 1:
			return new Paper();
		default:
			return new Rock();
		}
	}
	public static void main(String[] args) {
		for(int i = 0; i < 10; i++) {
			Item a = getRandom();
			Item b = getRandom();
			System.out.println(a + " vs " + b + " = " + a.compete(b));					
		}
	}

}
