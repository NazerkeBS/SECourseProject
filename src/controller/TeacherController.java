package controller;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import main.Authentication;
import model.Course;
import model.Course_;
import model.Result;
import model.Student;
import model.Teacher;

public class TeacherController {

	private Teacher teacher;

	public TeacherController(Teacher teacher) {
		this.teacher = teacher;
	}

	public void createCourse(String courseCode, String courseName, int credit, String date, String time)
			throws Exception {
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		EntityManager entityManager = emFactory.createEntityManager();
		Authentication auth = new Authentication(entityManager);
		Course c = auth.registerCourse(courseCode, courseName, credit, this.teacher, date, time);
		entityManager.getTransaction().begin();
		entityManager.persist(c);
		entityManager.getTransaction().commit();
		teacher.createCourse(c);
		entityManager.close();
		emFactory.close();
	}

	public String modifyCourse(String courseCode, String newCourseName, int newCredit, String newDate, String newTime)
			throws Exception {

		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		EntityManager entityManager = emFactory.createEntityManager();
		Authentication auth = new Authentication(entityManager);
		entityManager.getTransaction().begin();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Course> courseQuery = cb.createQuery(Course.class);
		Root<Course> course = courseQuery.from(Course.class);
		courseQuery.where(cb.equal(course.get(Course_.courseCode), courseCode));
		courseQuery.select(course);
		Course c = entityManager.createQuery(courseQuery).getSingleResult();
		entityManager.remove(c);
		if (newCredit < 0 && newCredit > 8) {
			return "Credit must be between 1 and 8";
		}

		Course modifiedCourse = auth.modifyCourse(teacher, courseCode, newCourseName, newCredit, newDate, newTime);
		entityManager.persist(modifiedCourse);
		entityManager.getTransaction().commit();
		teacher.modifyCourse(courseCode, newCourseName, newCredit, auth.date(newDate, newTime));

		return "Course Successfully Modified";
	}

	public String deleteCourse(String courseCode) {

		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		EntityManager entityManager = emFactory.createEntityManager();
		Authentication auth = new Authentication(entityManager);

		if (auth.contains(courseCode)) {
			entityManager.getTransaction().begin();
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Course> course = cb.createQuery(Course.class);
			Root<Course> root = course.from(Course.class);
			course.where(cb.equal(root.get(Course_.courseCode), courseCode));
			Course c = entityManager.createQuery(course).getResultList().get(0);
			entityManager.remove(c);
			entityManager.getTransaction().commit();

			teacher.deleteCourse(auth.searchCourse(courseCode));
			return "Successfully Deleted";

		} else {
			return "Course Not Found";
		}
	}

	public Set<Student> listStudents(String courseCode) {
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		EntityManager entityManager = emFactory.createEntityManager();
		Authentication auth = new Authentication(entityManager);
		if (auth.contains(courseCode)) {
			Set<Student> students = auth.searchCourse(courseCode).getStudents();
			if (students.size() != 0) {
				return students;
			}
		}
		return null;
	}

	public List<Course> listCreatedCourses() {
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		EntityManager entityManager = emFactory.createEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Course> query = cb.createQuery(Course.class);

		Root<Course> root = query.from(Course.class);

		query.where(cb.equal(root.get(Course_.teacher), this.teacher));
		query.select(root).distinct(true);
		List<Course> courses = entityManager.createQuery(query).getResultList();
		return courses;
	}

	public String giveGrade(String courseCode, String studentCode, int grade) {
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		EntityManager entityManager = emFactory.createEntityManager();
		Authentication auth = new Authentication(entityManager);
		if (!auth.contains(courseCode)) {
			return "Course code is not found";
		}

		else if (!auth.exists(studentCode)) {
			return "Student is not found";
		}

		else if (grade < 1 && grade > 5) {
			return "Grade must be between 1 and 5";
		}

		else if (auth.gotGrade(studentCode, courseCode)) {
			return "Student has already got a mark";
		} else {
			entityManager.getTransaction().begin();
			Result result = auth.registerResult(auth.searchForExistingStudent(studentCode),
					auth.searchCourse(courseCode), grade);
			entityManager.persist(result);
			entityManager.getTransaction().commit();
			teacher.giveGrade(auth.searchForExistingStudent(studentCode), auth.searchCourse(courseCode), grade);
			entityManager.close();
			emFactory.close();

		}
		return "Successfully Graded!";
	}

}
