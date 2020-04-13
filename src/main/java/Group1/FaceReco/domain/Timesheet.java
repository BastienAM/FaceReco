package Group1.FaceReco.domain;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Timesheet {
	private long id;
	private Timestamp date;
	private Account account;
	private Set<Presence> presence;

	@Id
	@GeneratedValue
	@Column(name="pk")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	@ManyToOne(optional=false)
    @JoinColumn(name="fk_account", nullable=false)
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@OneToMany(mappedBy = "id.timesheet", fetch = FetchType.EAGER)
	@JsonIgnoreProperties({"timesheet", "signature"})
	@JsonProperty("presence")
	public Set<Presence> getPresence() {
		return presence;
	}

	public void setPresence(Set<Presence> presence) {
		this.presence = presence;
	}

}
