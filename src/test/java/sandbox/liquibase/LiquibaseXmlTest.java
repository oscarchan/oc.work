package sandbox.liquibase;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import sandbox.liquibase.xml.Column;
import sandbox.liquibase.xml.DatabaseChangeLog;
import sandbox.liquibase.xml.Delete;
import sandbox.liquibase.xml.Insert;
import sandbox.liquibase.xml.ObjectFactory;
import sandbox.liquibase.xml.Update;
import sandbox.liquibase.xml.DatabaseChangeLog;

public class LiquibaseXmlTest
{
	private static Log log = LogFactory.getLog(LiquibaseXmlTest.class);
	
	private JAXBContext context;
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	
	@Before
	public void setUp() throws JAXBException
	{
		context = JAXBContext.newInstance("sandbox.liquibase.xml");
		marshaller = context.createMarshaller();
		unmarshaller = context.createUnmarshaller();
	}
	
	@Test
	public void testInsert() throws JAXBException
	{
		Insert row = new Insert();
		row.setTableName("packages");
		
		Column column = new Column();
		column.setName("pkg_id");
		column.setValueNumeric("123");

//		JAXBElement<String> where = new JAXBElement<String>(new QName("{http://www.liquibase.org/xml/ns/dbchangelog/1.9}where"), Delete.class, null, null); 
		
		Delete delete = new Delete();
		delete.setTableName("test_table");
		
//		delete.getContent().add(where);
		
		StringWriter writer = new StringWriter();

		DatabaseChangeLog.ChangeSet changeSet = new DatabaseChangeLog.ChangeSet();
		changeSet.setAuthor("ochan");
		changeSet.setId("1234");
		changeSet.getChangeSetChildren().add("comment 1 2 3");
		changeSet.getChangeSetChildren().add(row);
//		changeSet.getChangeSetChildren().add(rollback);
		
		DatabaseChangeLog dbLog = new DatabaseChangeLog();
		dbLog.getChangeSetOrIncludeOrIncludeAll().add(changeSet);
		
		
		marshaller.marshal(dbLog, writer);
		
		log.info("insert: " + writer.toString());
	}
	
	@SuppressWarnings("restriction")
    @Test
	public void testUpdate() throws JAXBException
	{

		// Column
		Column col = new Column();
		col.setName("abc");
		col.setValueNumeric("123");

		// Where
//		JAXBElement<String> where = new JAXBElement<String>(new QName("http://www.liquibase.org/xml/ns/dbchangelog/1.9", "where"), String.class, Update.class, "a=123");
//		JAXBElement<String> where = new JAXBElement<String>(new QName("where"), String.class, Update.class, "a=123");
		ObjectFactory factory = new ObjectFactory();
		JAXBElement<Object> where = factory.createUpdateWhere("a=123");

		// Update
		Update update = new Update();
		update.setTableName("test_table");
		
		update.getContent().add(col);
		update.getContent().add(where);

		// ChangeSet
		DatabaseChangeLog.ChangeSet changeSet = new DatabaseChangeLog.ChangeSet();
		changeSet.getChangeSetChildren().add("comment 1");
		changeSet.getChangeSetChildren().add(update);
		
		DatabaseChangeLog changeLog = new DatabaseChangeLog();
		changeLog.getChangeSetOrIncludeOrIncludeAll().add(changeSet);		
		
		StringWriter writer = new StringWriter();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, " http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-1.9.xsd");
		marshaller.marshal(changeLog, writer);
		
		log.info("update: \n" + writer.toString());

	}
	
	@Test
	public void testChangeSet() throws JAXBException, IOException 
	{
		ClassPathResource resource = new ClassPathResource("liquibase/changelog.xml");
		
		Object object = unmarshaller.unmarshal(resource.getInputStream());
		
		log.info("changeset: " + ToStringBuilder.reflectionToString(object));
		
	}

}
