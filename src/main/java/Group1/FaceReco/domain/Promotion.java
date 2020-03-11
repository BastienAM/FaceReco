package Group1.FaceReco.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Promotion {
	private long id;
	private String wording;
	
	@Id
	@GeneratedValue
	@Column(name="pk") // [XxXxXx] Permet de garder les majuscules, sinon tout est convertit en minuscule
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	//@Column(name="[Wording]")
	public String getWording() {
		return wording;
	}
	public void setWording(String wording) {
		this.wording = wording;
	}
	
	@Override
	public String toString() {
		return "Promotion [id=" + id + ", wording=" + wording + "]";
	}
}
