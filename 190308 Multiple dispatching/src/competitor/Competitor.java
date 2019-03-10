package competitor;

import rx.Result;

public interface Competitor<T extends Competitor<T>> {
	Result compete(T arg);
}
