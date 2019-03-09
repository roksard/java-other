package using.enummap;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;
import java.util.Set;

import competitor.Competitor;
import net.mindview.util.Enums;
import rx.Result;

public enum RoShamBo5 implements Competitor<RoShamBo5> {
	PAPER, SCISSORS, ROCK;
	static EnumMap<RoShamBo5,EnumMap<RoShamBo5,Result>> 
	table = new EnumMap<RoShamBo5,EnumMap<RoShamBo5,Result>>(RoShamBo5.class);
	static {
		for(RoShamBo5 en : RoShamBo5.values()) {
			table.put(en, new EnumMap<RoShamBo5,Result>(RoShamBo5.class));
		}
		initRow(RoShamBo5.ROCK, Result.DRAW, Result.WIN, Result.LOSE);
		initRow(RoShamBo5.SCISSORS, Result.LOSE, Result.DRAW, Result.WIN);
		initRow(RoShamBo5.PAPER, Result.WIN, Result.LOSE, Result.DRAW);
	}
	
	static void  initRow(RoShamBo5 en, Result vRock, Result vScissors, 
			Result vPaper) {
		EnumMap<RoShamBo5,Result> map = 
				table.get(en);
		map.put(RoShamBo5.ROCK, vRock);
		map.put(RoShamBo5.SCISSORS, vScissors);
		map.put(RoShamBo5.PAPER, vPaper);
	}
	
	@Override
	public Result compete(RoShamBo5 arg) {
		return table.get(this).get(arg);
	}
	
	public static <T extends Enum<T> & Competitor<T>> void play(Class<T> classId, 
			int number) {
		for(int i = 0; i < number; i++) {
			T a = Enums.random(classId.getEnumConstants());
			T b = Enums.random(classId.getEnumConstants());
			System.out.println(a + " vs " + b + " = " + a.compete(b));
		}
		
	}
	
	public static void main(String[] args) {
		play(RoShamBo5.class, 20);
	}

}
