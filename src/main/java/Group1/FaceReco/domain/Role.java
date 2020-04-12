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

@Entity(name="role_user")
public class Role {

	private long id;
	private String wording;
	
	private Set<RoleRight> roleRight;
	private Set<Account> account;
	
	@Id
	@GeneratedValue
	@Column(name="pk")
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
	
	@OneToMany(mappedBy = "id.role", fetch = FetchType.EAGER)
	@JsonIgnoreProperties("roleright_role")
	@JsonProperty("role_roleright")
	public Set<RoleRight> getRoleRight() {
		return roleRight;
	}
	
	public void setRoleRight(Set<RoleRight> roleRight) {
		this.roleRight = roleRight;
	}
	
	@OneToMany(mappedBy="role", fetch = FetchType.EAGER)
	@JsonIgnoreProperties("role")
	public Set<Account> getAccount() {
		return account;
	}
	public void setAccount(Set<Account> account) {
		this.account = account;
	}
	
	
	
}
