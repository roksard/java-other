package rx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JaxbParserTest {
    private Parser parser;
    private File file;

    //@Before
    public void setUp() throws Exception {
        parser = new JaxbParser();
        file = new File("src\\person.xml");
    }

   // @Test
    public void testGetObject() throws Exception {
        Person person = (Person) parser.getObject(file, Person.class);
        System.out.println(person);
    }

   // @Test
    public void testSaveObject() throws Exception {
        Person person = new Person();
        person.setName("Oleg");
        person.setAge(19);

        Person friend1 = new Person();
        Person friend2 = new Person();

        friend1.setName("Viktor");
        friend1.setAge(20);

        friend2.setName("Stepan");
        friend2.setAge(15);

        List<Person> friends = new ArrayList<Person>();
        friends.add(friend1);
        friends.add(friend2);

        person.setFriends(friends);

        parser.saveObject(file,person);
    }
    public static void main(String[] args) throws Exception {
    	JaxbParserTest test = new JaxbParserTest();
    	test.setUp();
    	test.testSaveObject();
    	test.testGetObject();
    }
}
