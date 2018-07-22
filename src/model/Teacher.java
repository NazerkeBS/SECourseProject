package model;


import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Teacher extends User{

private String role;

@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
private Set<Course> courses;


  public Teacher() {  
  }  
	public Teacher(String code,byte[] password, byte[] salt, String name, String email, String role) {
		super(code,password,salt,name,email);
		this.role = role;
		courses = new HashSet<>();
	}

	public String getRole() {
		return role;
	}

	public void createCourse(Course course) {
		courses.add(course);
	}
	
	public void modifyCourse(String courseCode, String newCourseName, int newCredit, Date newStartTime) {
		for(Iterator<Course> it = courses.iterator(); it.hasNext();) {
		 Course c = it.next();
			if(c.getCourseCode().equals(courseCode)) {
			courses.remove(c);
			c.setCourseName(newCourseName);
			c.setCredit(newCredit);
			c.setStartTime(newStartTime);
			c.setCourseCode(courseCode);
			c.setTeacher(this);
			    courses.add(c);
		}	 
		}
	}
	
   	public void deleteCourse (Course course) {
   		courses.remove(course);
   	}
   	
   	public int howManyStudents(Course course) {
   		return course.howManyStudents();
   	}
   	
	public void giveGrade(Student student, Course course, int grade) {
		Result result = new Result();
		result.setGrade(grade);
		result.setCourse(course);
		result.setStudent(student);
	}
	
	public Set<Course> getCreatedCourses() {
		return this.courses;
	}
	
}
