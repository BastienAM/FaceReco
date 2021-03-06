package Group1.FaceReco.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name = "group_student")
public class Group implements Serializable {
	private long id;
	private String wording;
	private Promotion promotion;
	private Set<Student> student;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getWording() {
		return wording;
	}

	public void setWording(String wording) {
		this.wording = wording;
	}

	@ManyToOne(optional=false)
	@JoinColumn(name = "fk_promotion", nullable = false)
	@JsonIgnoreProperties("group")
	public Promotion getPromotion() {
		return promotion;
	}

	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}
	@OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
	@JsonIgnore
	public Set<Student> getStudent() {
		return student;
	}

	public void setStudent(Set<Student> student) {
		this.student = student;
	}
}
