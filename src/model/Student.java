package model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
public class Student extends User {

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Course> courses = new HashSet<>();
	private int credits;

	public Student() {
	}

	public Student(String code, byte[] password, byte[] salt, String name, String email) {
		super(code, password, salt, name, email);

	}

	public void takeCourse(Course course) {

		courses.add(course);
		credits += course.getCredit();
		course.addStudentToCourse(this);

	}

	public void dropCourse(Course course) {
		credits -= course.getCredit();
		courses.remove(course);
		course.removeStudentFromCourse(this);

	}

	public int howManyCredits() {
		return credits;
	}

	public Set<Course> getCourses() {
		return courses;
	}

}
