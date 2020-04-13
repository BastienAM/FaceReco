package Group1.FaceReco.domain;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@AssociationOverrides({
		@AssociationOverride(name = "id.timesheet", joinColumns = @JoinColumn(name = "pk_fk_timesheet")),
		@AssociationOverride(name = "id.student", joinColumns = @JoinColumn(name = "pk_fk_student")) })
public class Presence {

	private PresenceId id;
	private boolean present;

	@EmbeddedId
	@JsonIgnore
	public PresenceId getId() {
		return id;
	}

	public void setId(PresenceId id) {
		this.id = id;
	}

	public boolean isPresent() {
		return present;
	}

	public void setPresent(boolean present) {
		this.present = present;
	}

	@Transient
	@JsonProperty("timesheet")
	@JsonIgnoreProperties({ "presence" })
	public Timesheet getTimesheet() {
		return id.getTimesheet();
	}

	public void setTimesheet(Timesheet timesheet) {
		this.id.setTimesheet(timesheet);
	}

	@Transient
	@JsonProperty("student")
	@JsonIgnoreProperties({ "presence" })
	public Student getStudent() {
		return this.id.getStudent();
	}

	public void setStudent(Student student) {
		this.id.setStudent(student);
	}

}
