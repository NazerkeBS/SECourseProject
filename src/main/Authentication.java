package main;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import model.Course;
import model.Course_;
import model.PasswordPair;
import model.Result;
import model.Result_;
import model.Student;
import model.Student_;
import model.Teacher;
import model.Teacher_;
import model.User;
import model.User_;


public class Authentication {

	private static final SecureRandom RAND = new SecureRandom();
	private EntityManager entityManager;
	
	
	
	public Authentication(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
	
	public boolean authenticate(String code, char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
     CriteriaBuilder cb = entityManager.getCriteriaBuilder();
     CriteriaQuery<User> userQuery = cb.createQuery(User.class);
		Root<User> user = userQuery.from(User.class);
		userQuery.where(cb.equal(user.get(User_.code), code));
		userQuery.select(user);
		User u = entityManager.createQuery(userQuery).getSingleResult();
		byte[] hash = hashPassword(password, u.getSalt());
		return constantTimeEquals(hash, u.getPassword());
	 
	}
	
	public byte[] hashPassword(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PBEKeySpec spec = new PBEKeySpec(password, salt, 2000, 256);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		return skf.generateSecret(spec).getEncoded();
	}

	private boolean constantTimeEquals(byte[] a, byte[] b) {
		if (a.length != b.length) {
			return false;
		}

		int result = 0;
		for (int i = 0; i < a.length; i++) {
			result |= a[i] ^ b[i];
		}

		return result == 0;
	}
	
	private PasswordPair hashPassword(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] salt = new byte[256];
		RAND.nextBytes(salt);
		return new PasswordPair(hashPassword(password, salt), salt);
	}

	public Teacher registerTeacher(String code, char[] password, String name, String email, String role) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PasswordPair p = hashPassword(password);
		return new Teacher(code, p.getPassword(), p.getSalt(), name, email,role);
	}

	public Student registerStudent(String code, char[] password, String name, String email) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PasswordPair p = hashPassword(password);
		return new Student(code, p.getPassword(), p.getSalt(), name, email);
	}

   public Course registerCourse(String courseCode, String courseName, int credit, Teacher teacher, String startTime, String oclock) throws Exception{
     Date parsedStartTime = date(startTime, oclock);
   return new Course(courseCode, courseName, credit, teacher, parsedStartTime);
}
   
   public Result registerResult(Student student, Course course, int grade) {
	   return new Result(student, course, grade);
   }
   
   
   public Course modifyCourse(Teacher teacher, String courseCode, String newCourseName, int newCredit, String newTime,String oclock) throws Exception {
		   Date newStartTime = date(newTime,oclock);
		   teacher.modifyCourse(courseCode, newCourseName, newCredit, newStartTime);
		   return new Course(courseCode, newCourseName, newCredit,teacher, newStartTime); 
	}
   
   public Date date(String day, String clock) {
		return Date.from(Instant.parse(day + "T" + clock + ":00Z"));    
   }
   
	public Course searchCourse(String courseCode) {
		if(contains(courseCode)) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Course> courseQuery = cb.createQuery(Course.class);
		Root<Course> course = courseQuery.from(Course.class);
		courseQuery.where(cb.equal(course.get(Course_.courseCode), courseCode));
		courseQuery.select(course);
		Course c = entityManager.createQuery(courseQuery).getSingleResult();
		return c;
		}
        return null;
	}
	
	public boolean contains(String courseCode) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Course> courseQuery = cb.createQuery(Course.class);
		Root<Course> course = courseQuery.from(Course.class);
		courseQuery.where(cb.equal(course.get(Course_.courseCode), courseCode));
		courseQuery.select(course);
		List<Course> results = entityManager.createQuery(courseQuery).getResultList();
		if(results.size() == 0 || results.size() > 1)
		{
			return false;
		}
		
		return true;
	}
	
	
	  public Student searchForExistingStudent(String studentCode) {
	        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	        CriteriaQuery<Student> cquery = cb.createQuery(Student.class);
	        Root<Student> person = cquery.from(Student.class);
	        cquery.where(person.get(Student_.code).in(studentCode));
	        cquery.select(person);
	        List<Student> resultList = entityManager.createQuery(cquery).getResultList();
	        return resultList.isEmpty() ? null : resultList.get(0);
	    
	  }
	
	
	  public Teacher searchForExistingTeacher(String teacherCode) {
	        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	        CriteriaQuery<Teacher> cquery = cb.createQuery(Teacher.class);
	        Root<Teacher> person = cquery.from(Teacher.class);
	        cquery.where(person.get(Teacher_.code).in(teacherCode));
	        cquery.select(person);
	        List<Teacher> resultList = entityManager.createQuery(cquery).getResultList();
	        return resultList.isEmpty() ? null : resultList.get(0);
	    
	  }
	
	public boolean exists(String code) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Student> studentQuery = cb.createQuery(Student.class);
		Root<Student> student = studentQuery.from(Student.class);
		studentQuery.where(cb.equal(student.get(User_.code), code));
		studentQuery.select(student);
		List<Student> results = entityManager.createQuery(studentQuery).getResultList();
		if(results.size() == 0)
		{
			return false;
		}
		
		return true;
	}
	
	public boolean gotGrade(String studentCode, String courseCode) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Result> studentQuery = cb.createQuery(Result.class);
		Root<Result> student = studentQuery.from(Result.class);
		studentQuery.where(cb.equal(student.get(Result_.student).get(Student_.code), studentCode)) ;
		studentQuery.select(student);
		List<Result> results = entityManager.createQuery(studentQuery).getResultList();
		if(results.size() != 0)
		{
			return true;
		}
		
		return false;
		
	}
	
}