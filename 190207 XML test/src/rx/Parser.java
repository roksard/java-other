package rx;

import java.io.File;
import javax.xml.bind.JAXBException;

public interface Parser {
	Object getObject(File file, Class c) throws JAXBException;
	void saveObject(File file, Object o) throws JAXBException;
}
