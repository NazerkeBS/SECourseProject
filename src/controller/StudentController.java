package controller;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import main.Authentication;
import model.Course;
import model.Course_;
import model.Result;
import model.Result_;
import model.Student;
import model.Student_;

public class StudentController {

	private Student student;

	public StudentController(Student student) {
		this.student = student;
	}

	public String takeCourse(String courseCode) {
		if (courseCode.isEmpty()) {
			return "Enter course code";
		}

		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		EntityManager entityManager = emFactory.createEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Student> query = cb.createQuery(Student.class);
		Root<Student> root = query.from(Student.class);
		query.where(cb.equal(root.get(Student_.code), student.getCode()));
		query.select(root);
		Student s = entityManager.createQuery(query).getSingleResult();

		CriteriaQuery<Course> cquery = cb.createQuery(Course.class);
		Root<Course> croot = cquery.from(Course.class);
		cquery.select(croot);
		List<Course> courses = entityManager.createQuery(cquery).getResultList();

		for (Course c : courses) {
			if (c.getCourseCode().equals(courseCode)) {
				entityManager.getTransaction().begin();
				s.takeCourse(c);
				entityManager.getTransaction().commit();
				return "Registered successfully!";
			}
		}

		return "Course Not Found";
	}

	public String dropCourse(String courseCode) {
		
		if (courseCode.isEmpty()) {
			return "Enter course code";
		}
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		EntityManager entityManager = emFactory.createEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Student> query = cb.createQuery(Student.class);
		Root<Student> root = query.from(Student.class);
		query.where(cb.equal(root.get(Student_.code), student.getCode()));
		query.select(root);
		Student s = entityManager.createQuery(query).getSingleResult();

		CriteriaQuery<Course> cquery = cb.createQuery(Course.class);
		Root<Course> croot = cquery.from(Course.class);
		cquery.select(croot);
		List<Course> courses = entityManager.createQuery(cquery).getResultList();
		
		List<Tuple> grades = listGrades();
		
		for (Course c : courses) {
			if (c.getCourseCode().equals(courseCode)) {
				
				for(Tuple t : grades) {
					if(c.getCourseName().equals(t.get(0))) {
						return "You have got a mark, so can't drop it";
					}
				}
				entityManager.getTransaction().begin();
				s.dropCourse(c);
				entityManager.getTransaction().commit();
		     	return "Dropped successfully!";
			}
		}
		return " Course Not Found";
	}

	public List<Course> listAllSubjects() {
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		EntityManager entityManager = emFactory.createEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Course> query = cb.createQuery(Course.class);
		Root<Course> root = query.from(Course.class);
		query.select(root);
		List<Course> courses = entityManager.createQuery(query).getResultList();
		return courses;
	}

	public Set<Course> registeredSubjects() {
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		EntityManager entityManager = emFactory.createEntityManager();
		Authentication auth = new Authentication(entityManager);
		if (auth.exists(student.getCode())) {
			Set<Course> courses = auth.searchForExistingStudent(student.getCode()).getCourses();
			if (courses.size() != 0) {
				return courses;
			}
		}
		return null;

	}

	public List<Tuple> listGrades() {
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("LearningSystem_PU");
		EntityManager entityManager = emFactory.createEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> gradeQuery = cb.createTupleQuery();
		Root<Result> result = gradeQuery.from(Result.class);
		gradeQuery.where(cb.equal(result.get(Result_.student), this.student));
		gradeQuery.multiselect(result.get(Result_.course).get(Course_.courseName), result.get(Result_.grade));
		List<Tuple> results = entityManager.createQuery(gradeQuery).getResultList();

		return results;
	}

}
