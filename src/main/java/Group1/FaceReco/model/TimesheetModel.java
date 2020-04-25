package Group1.FaceReco.model;

import java.sql.Timestamp;
import java.util.Set;

import Group1.FaceReco.domain.Account;

public class TimesheetModel {
	private long id;
	private String date;
	private Account account;
	private Set<Long> student;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Set<Long> getStudent() {
		return student;
	}
	public void setStudent(Set<Long> student) {
		this.student = student;
	}
	@Override
	public String toString() {
		return "TimesheetModel [id=" + id + ", date=" + date + ", account=" + account + ", student=" + student + "]";
	}
	
}
