package rx;

abstract class Item {
	abstract public Result compete(Item it);
	abstract public Result eval(Rock r);
	abstract public Result eval(Scissors r);
	abstract public Result eval(Paper r);
	public String toString() {
		return this.getClass().getSimpleName();
	}
}

class Rock extends Item {
	@Override
	public Result compete(Item it) {
		return it.eval(this);
	}
	public Result eval(Rock r) {
		return Result.DRAW;
	}
	@Override
	public Result eval(Scissors r) {
		return Result.LOSE;
	}
	@Override
	public Result eval(Paper r) {
		return Result.WIN;
	}
}

class Scissors extends Item {
	@Override
	public Result compete(Item it) {
		return it.eval(this);
	}
	public Result eval(Rock r) {
		return Result.WIN;
	}
	@Override
	public Result eval(Scissors r) {
		return Result.DRAW;
	}
	@Override
	public Result eval(Paper r) {
		return Result.LOSE;
	}
}

class Paper extends Item {
	@Override
	public Result compete(Item it) {
		return it.eval(this);
	}
	public Result eval(Rock r) {
		return Result.LOSE;
	}
	@Override
	public Result eval(Scissors r) {
		return Result.WIN;
	}
	@Override
	public Result eval(Paper r) {
		return Result.DRAW;
	}
}


public class DoubleDispatch {
	public static Item randomItem() {
		int i = new java.util.Random().nextInt(3);
		switch(i) {
		case 0:
			return new Rock();
		case 1:
			return new Paper();
		default:
			return new Scissors();
		}
	}
	
	public static Result match(Item a, Item b) {
		return a.compete(b);
	}
	public static void main(String[] args) {
		for(int i = 0; i < 10; i++) {
			Item a = randomItem();
			Item b = randomItem();
			System.out.println(a + " vs " + b + " = " + match(a,b));
		}
	}

}
