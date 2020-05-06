package Group1.FaceReco.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class PresenceId implements Serializable{

	private Timesheet timesheet;
	private Student student;	
	
	public PresenceId() {
		super();
	}
	
	public PresenceId(Timesheet timesheet, Student student) {
		super();
		this.timesheet = timesheet;
		this.student = student;
	}
	@ManyToOne
	public Timesheet getTimesheet() {
		return timesheet;
	}
	public void setTimesheet(Timesheet timesheet) {
		this.timesheet = timesheet;
	}
	
	@ManyToOne
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
	

}
