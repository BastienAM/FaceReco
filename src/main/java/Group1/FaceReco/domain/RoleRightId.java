package Group1.FaceReco.domain;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class RoleRightId implements Serializable{
	private Role role;
	private Right right;
	
	@ManyToOne
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@ManyToOne
	public Right getRight() {
		return right;
	}

	public void setRight(Right right) {
		this.right = right;
	}
	
}
