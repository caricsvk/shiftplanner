package milo.shiftplanner.agents;

import milo.utils.rest.jaxbadapters.LocalDateTimeToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@XmlRootElement
public class Agent implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	@NotEmpty
	private String name;
	@NotNull
	@Email
	private String email;
	private String phone;
	private Timestamp created;

	@PrePersist
	private void prePersist() {
		setCreated(LocalDateTime.now());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name != null && name.length() > 255 ? name.substring(0, 254) : name;
	}

	@XmlJavaTypeAdapter(LocalDateTimeToString.class)
	public LocalDateTime getCreated() {
		return created == null ? null : created.toLocalDateTime();
	}

	public void setCreated(LocalDateTime created) {
		this.created = created == null ? null : Timestamp.valueOf(created);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Agent{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				", created=" + created +
				'}';
	}
}
