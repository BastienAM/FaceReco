package Group1.FaceReco.model;

import java.util.Set;

public class TimesheetModel {
	private long id;
	private String date;
	private String wording;
	private Set<Long> student;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWording() {
		return wording;
	}
	public void setWording(String wording) {
		this.wording = wording;
	}
	public Set<Long> getStudent() {
		return student;
	}
	public void setStudent(Set<Long> student) {
		this.student = student;
	}
	@Override
	public String toString() {
		return "TimesheetModel [id=" + id + ", date=" + date + ", student=" + student + "]";
	}
	
}
