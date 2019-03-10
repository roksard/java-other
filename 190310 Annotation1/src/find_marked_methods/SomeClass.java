package find_marked_methods;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SomeClass {
	@UseCase(id = 1, description = "A's description")
	public void methodA() {}
	@UseCase(id = 2)
	public void methodB() {}
	//not annotated
	public void methodC() {}
	@UseCase(id = 4)
	private void methodD() {}
	
	public static void printUseCases(List<Integer> ids, Class<?> cl, Class<? extends UseCase> annotation) {
		List<Integer> idsMod = new LinkedList<Integer>(ids);
		for(Method m : cl.getDeclaredMethods()) {
			UseCase annt = m.getAnnotation(annotation);
			if (annt != null) {
				System.out.println("Found: [" + m.getName() + "] " + annt.id() + " " + annt.description());
				idsMod.remove(new Integer(annt.id()));
			}
		}
		for(int i : idsMod) {
			System.out.println("Not found: " + i);
		}
	}
	public static void main(String[] args) {
		printUseCases(Arrays.asList(new Integer[] {0, 1, 2, 3, 4, 5}), SomeClass.class, UseCase.class);
	}

}
