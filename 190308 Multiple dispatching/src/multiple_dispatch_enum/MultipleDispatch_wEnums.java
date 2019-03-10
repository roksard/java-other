package multiple_dispatch_enum;

import rx.Result;
import static rx.Result.*;

import competitor.Competitor;
import net.mindview.util.Enums;



enum RoShamBo2 implements Competitor<RoShamBo2> {
	ROCK(DRAW, WIN, LOSE),
	SCISSORS(LOSE, DRAW, WIN),
	PAPER(WIN, LOSE, DRAW);
	Result vsRock;
	Result vsScissors;
	Result vsPaper;
	RoShamBo2(Result rock, Result scissors, Result paper) {
		vsRock = rock;
		vsScissors = scissors;
		vsPaper = paper;
	}

	@Override
	public Result compete(RoShamBo2 arg) {
		switch(arg) {
		case ROCK:
			return vsRock;
		case SCISSORS:
			return vsScissors;
		case PAPER:
			return vsPaper;
		default:
			throw new RuntimeException("Unexpected value");
		}
	}	
}


public class MultipleDispatch_wEnums {
	public static <T extends Enum<T> & Competitor<T>> 
	void match(Class<T> cla, int number) {
		for(int i = 0; i < number; i++) {
			T a = Enums.random(cla.getEnumConstants());
			T b = Enums.random(cla.getEnumConstants());
			System.out.println(a + " vs " + b + " = " + a.compete(b));
		}
	}
	public static void main(String[] args) {
		match(RoShamBo2.class, 10);
	}

}

