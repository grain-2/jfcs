/*
 *编写者：陈冈
 *高校经费测算系统--课程明细子类
 *编写时间：2006-11-13
 */
package cn.edu.jfcs.model;

public class CourseTreeChildren {
	// 当前课程的父结点
	private CourseTreeParent courseParent;

	private Course course;

	public CourseTreeParent getCourseParent() {
		return courseParent;
	}

	public void setCourseParent(CourseTreeParent courseParent) {
		this.courseParent = courseParent;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

}
