package model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String courseCode;
	private String courseName;
	private int credit;

	@ManyToOne
	private Teacher teacher;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<Student> students = new HashSet<>();

	public Course() {
	}

	public Course(String courseCode, String courseName, int credit, Teacher teacher, Date startTime) throws Exception {
		this.courseCode = courseCode;
		this.courseName = courseName;
		if (credit > 0 && credit < 9) {
			this.credit = credit;
		} else {
			throw new Exception("credit must be between 1 and 8");
		}
		this.teacher = teacher;
		this.startTime = startTime;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		if (credit > 0 && credit < 9)
			this.credit = credit;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void addStudentToCourse(Student student) {
		students.add(student);

	}

	public void removeStudentFromCourse(Student student) {
		students.remove(student);

	}

	public int howManyStudents() {
		return students.size();
	}

	public Set<Student> getStudents() {
		return students;
	}

	public int getId() {
		return this.id;
	}

}
