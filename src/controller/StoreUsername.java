package controller;

public class StoreUsername {

	private static String teacherUsername;
	private static String studentUsername;

	public StoreUsername() {

	}

	public static void setTeacherUsername(String username) {
		teacherUsername = username;
	}

	public static void setStudentUsername(String username) {
		studentUsername = username;
	}

	public static String getTeacherUsername() {
		return teacherUsername;
	}

	public static String getStudentUsername() {
		return studentUsername;
	}

}
