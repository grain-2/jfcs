/*
 *编写者：陈冈
 *高校经费测算系统--课程父类
 *编写时间：2006-11-13
 */
package cn.edu.jfcs.model;

import java.util.ArrayList;
import java.util.List;

public class CourseTreeParent {
	private String coursename;

	private List<CourseTreeChildren> courseTreeChildren;

	public CourseTreeParent(String coursename) {
		this.coursename = coursename;
		courseTreeChildren = new ArrayList<CourseTreeChildren>();
	}

	// 添加子结点
	public void add(CourseTreeChildren course) {
		courseTreeChildren.add(course);
		// 设置子结点的父结点
		course.setCourseParent(this);
	}

	public List<CourseTreeChildren> getCourseTreeChildren() {
		return courseTreeChildren;
	}

	public void setCourseTreeChildren(List<CourseTreeChildren> courseTreeChildren) {
		this.courseTreeChildren = courseTreeChildren;
	}

	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

}
