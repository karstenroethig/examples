package karstenroethig.examples.jaxb;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import karstenroethig.examples.jaxb.model.jaxb.project.CommentXml;
import karstenroethig.examples.jaxb.model.jaxb.project.IssueXml;
import karstenroethig.examples.jaxb.model.jaxb.project.ProjectXml;

public class JaxbTest
{
	@Test
	public void testReadWrite() throws Exception
	{
		ProjectXml project = new ProjectXml();
		project.setKey( "KEY" );
		project.setName( "projectname" );

		IssueXml issue = new IssueXml();
		issue.setId(42l);
		issue.setTitle("some title");
		issue.setDescription("a description\nover\nmultiple\nlines");
		issue.setCreatedDatetime(LocalDateTime.now());
		issue.setDueDate(LocalDate.now());
		project.getIssues().add(issue);

		CommentXml comment1 = new CommentXml();
		comment1.setId(69l);
		comment1.setText("my first comment");
		comment1.setCreatedDatetime(LocalDateTime.now());
		comment1.setDeleted(true);
		issue.getComments().add(comment1);

		CommentXml comment2 = new CommentXml();
		comment2.setId(83l);
		comment2.setText("another comment\nover\nmultiple\nlines");
		comment2.setCreatedDatetime(LocalDateTime.now());
		issue.getComments().add(comment2);

		/*
		 * Marshal
		 */
		Path pathToXmlFile = Paths.get("target", System.currentTimeMillis() + ".xml");

		JAXBContext context = JAXBContext.newInstance(ProjectXml.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(project, pathToXmlFile.toFile());

		/*
		 * Unmarshal
		 */
		Unmarshaller unmarshaller = context.createUnmarshaller();
		ProjectXml proj = (ProjectXml)unmarshaller.unmarshal(pathToXmlFile.toFile());

		assertEquals(project.getKey(), proj.getKey());
		assertEquals(project.getName(), proj.getName());
		assertEquals(1, proj.getIssues().size());

		IssueXml iss = proj.getIssues().get(0);
		assertEquals(issue.getId(), iss.getId());
		assertEquals(issue.getTitle(), iss.getTitle());
		assertEquals(issue.getDescription(), iss.getDescription());
		assertEquals(issue.getCreatedDatetime(), iss.getCreatedDatetime());
		assertEquals(issue.getDueDate(), iss.getDueDate());
		assertEquals(2, iss.getComments().size());

		CommentXml com1 = iss.getComments().get(0);
		assertEquals(comment1.getId(), com1.getId());
		assertEquals(comment1.getText(), com1.getText());
		assertEquals(comment1.getCreatedDatetime(), com1.getCreatedDatetime());
		assertEquals(true, com1.isDeleted());

		CommentXml com2 = iss.getComments().get(1);
		assertEquals(comment2.getId(), com2.getId());
		assertEquals(comment2.getText(), com2.getText());
		assertEquals(comment2.getCreatedDatetime(), com2.getCreatedDatetime());
		assertEquals(false, com2.isDeleted());
	}
}
