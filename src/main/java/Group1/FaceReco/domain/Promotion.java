package Group1.FaceReco.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Promotion implements Serializable {
	private long id;
	private String wording;
	private Set<Group> group;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk") // [XxXxXx] Permet de garder les majuscules, sinon tout est convertit en
							// minuscule
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	// @Column(name="[Wording]")
	public String getWording() {
		return wording;
	}

	public void setWording(String wording) {
		this.wording = wording;
	}

	@OneToMany(mappedBy="promotion", fetch = FetchType.EAGER)
	@JsonIgnoreProperties("promotion")
	public Set<Group> getGroup() {
		return group;
	}

	public void setGroup(Set<Group> group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "Promotion [id=" + id + ", wording=" + wording + "]";
	}
}
