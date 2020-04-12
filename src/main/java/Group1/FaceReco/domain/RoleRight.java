package Group1.FaceReco.domain;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name="role_right")
@AssociationOverrides({
	@AssociationOverride(name = "id.role", 
		joinColumns = @JoinColumn(name = "pk_fk_role_user")),
	@AssociationOverride(name = "id.right", 
		joinColumns = @JoinColumn(name = "pk_fk_right_user")) })
public class RoleRight {
	
	private RoleRightId id;
	private boolean allow;
	
	@EmbeddedId
	@JsonIgnore
	public RoleRightId getId() {
		return id;
	}
	public void setId(RoleRightId id) {
		this.id = id;
	}
	public boolean isAllow() {
		return allow;
	}
	public void setAllow(boolean allow) {
		this.allow = allow;
	}
	
	@Transient
	@JsonProperty("roleright_role")
	@JsonIgnoreProperties({"role_roleright"})
	public Role getRole() {
		return getId().getRole();
	}
	public void setRole(Role role) {
		this.getId().setRole(role);
	}
	
	@Transient
	@JsonProperty("roleright_right")
	@JsonIgnoreProperties({"right_roleright"})
	public Right getRight() {
		return getId().getRight();
	}
	public void setRight(Right right) {
		this.getId().setRight(right);
	}
	
	
	
}
