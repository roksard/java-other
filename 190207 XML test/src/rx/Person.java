package rx;

import java.util.List;
import java.util.Random;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "person")
@XmlType(propOrder = {"name", "age", "friends", "y"})
public class Person {
	String name;
	int age;
	List<Person> friends;
	int y;
	
	public int getY() {
		return y;
	}
	
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
	public List<Person> getFriends() {
		return friends;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAge(int age) {
		this.age = age;
	}

	@XmlElement(name = "friend")
	@XmlElementWrapper
	public void setFriends(List<Person> friends) {
		this.friends = friends;
	}
	@Override
	public String toString() {
		return name + " " + " age: " + age + " friends: " + friends;
	}

}
