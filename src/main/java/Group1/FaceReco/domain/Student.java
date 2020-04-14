package Group1.FaceReco.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Student {
	private long number;
	
	private String lastName;
	private String firstName;
	private Group group;
	private Set<Presence> presence;
	private Set<Signature> signature;

	@Id
	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}
	
	@Column(name = "last_name")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "first_name")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "fk_group_student", nullable = false)
	@JsonIgnoreProperties("student")
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@OneToMany(mappedBy = "id.student", fetch = FetchType.EAGER)
	@JsonIgnoreProperties("student")
	@JsonProperty("presence")
	@JsonIgnore
	public Set<Presence> getPresence() {
		return presence;
	}

	public void setPresence(Set<Presence> presence) {
		this.presence = presence;
	}

	@OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
	@JsonIgnoreProperties("student")
	@JsonIgnore
	public Set<Signature> getSignature() {
		return signature;
	}

	public void setSignature(Set<Signature> signature) {
		this.signature = signature;
	}
}
