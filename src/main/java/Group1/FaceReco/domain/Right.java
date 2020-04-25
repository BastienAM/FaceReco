package Group1.FaceReco.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name = "right_user")
public class Right {

	private long id;
	private String key;
	private String wording;
	private Set<RoleRight> roleRight;

	@Id
	@GeneratedValue
	@Column(name = "pk")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getWording() {
		return wording;
	}

	public void setWording(String wording) {
		this.wording = wording;
	}

	@OneToMany(mappedBy = "id.right", fetch = FetchType.EAGER)
	@JsonIgnoreProperties("right")
	@JsonProperty("permission")
	public Set<RoleRight> getRoleRight() {
		return roleRight;
	}

	public void setRoleRight(Set<RoleRight> roleRight) {
		this.roleRight = roleRight;
	}
	
}
