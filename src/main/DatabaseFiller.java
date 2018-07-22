package main;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Course;
import model.Student;
import model.Teacher;

public class DatabaseFiller {

	public static void main(String[] args) throws Exception {

		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		EntityManager entityManager = emfactory.createEntityManager();
		Authentication authentication = new Authentication(entityManager);

		entityManager.getTransaction().begin();

		Teacher teacher1 = authentication.registerTeacher("ic4mqf", "1234".toCharArray(), "John Smith",
				"smith@inf.elte.hu", "Professor");
		Teacher teacher2 = authentication.registerTeacher("cvde1", "1234".toCharArray(), "Joe Know",
				" mith@inf.elte.hu", "Lecturer");
		Teacher teacher3 = authentication.registerTeacher("hld34", "1234".toCharArray(), "Roscoe Tim",
				"smith@inf.elte.hu", "Assistant");
		Teacher teacher4 = authentication.registerTeacher("iqfs5", "1234".toCharArray(), "Partick M", "mit@inf.elte.hu",
				"Professor");

		Student student1 = authentication.registerStudent("asd", "abcd".toCharArray(), "Nazerke Seidan",
				"seinaz1997@gmail.com");
		Student student2 = authentication.registerStudent("fcgygf", "abcd".toCharArray(), "Ahmed Blej",
				"ahmed@gmail.com");
		Student student3 = authentication.registerStudent("yrik35", "abcd".toCharArray(), "Yuri Kim", "yuri@gmail.com");
		Student student4 = authentication.registerStudent("ba75hj", "abcd".toCharArray(), "Dana Sultan",
				"sultand@gmail.com");

		entityManager.persist(teacher1);
		entityManager.persist(teacher2);
		entityManager.persist(teacher3);
		entityManager.persist(teacher4);

		entityManager.persist(student1);
		entityManager.persist(student2);
		entityManager.persist(student3);
		entityManager.persist(student4);

		Course course1 = new Course("PSEII", "Practical Software Engineering", 4, teacher1,
				date("2018-03-19", "11:15:30"));
		Course course2 = new Course("JavaI", "Basic Java", 5, teacher2, date("2018-03-12", "12:15:30"));
		Course course3 = new Course("Prog1", "Programming C++", 3, teacher1, date("2018-04-11", "10:15:30"));
		Course course4 = new Course("Calc", "Calculus III", 3, teacher3, date("2018-02-13", "08:15:30"));

		entityManager.persist(course1);
		entityManager.persist(course2);
		entityManager.persist(course3);
		entityManager.persist(course4);

		
		entityManager.getTransaction().commit();
		entityManager.close();
		emfactory.close();

	}

	private static Date date(String day, String clock) {
		return Date.from(Instant.parse(day + "T" + clock + ".00Z").atZone(ZoneId.systemDefault()).toInstant());
	}
}
