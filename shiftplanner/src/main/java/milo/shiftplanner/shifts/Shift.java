package milo.shiftplanner.shifts;

import milo.shiftplanner.agents.Agent;
import milo.utils.rest.jaxbadapters.DurationToSeconds;
import milo.utils.rest.jaxbadapters.LocalDateTimeToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = Shift.FIND_BY_AGENT, query = "SELECT entity FROM Shift entity WHERE agent.id = :agentId ORDER BY id ASC"),
		@NamedQuery(name = Shift.FIND_BY_STATE, query = "SELECT entity FROM Shift entity WHERE state = :state ORDER BY entity.start DESC"),
		@NamedQuery(name = Shift.FIND_OVERLAPPED, query = "SELECT entity FROM Shift entity WHERE state = :state AND entity.start <= :now ORDER BY entity.start DESC")
})
public class Shift implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_AGENT = "Shift.findByAgent";
	public static final String FIND_BY_STATE = "Shift.findByState";
	public static final String FIND_OVERLAPPED = "Shift.findOverlapped";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@NotNull
	private Agent agent;
	@NotNull
	private Timestamp start;
	@Enumerated(EnumType.STRING)
	private State state = State.PLANNED;
	private Timestamp created;
	private Timestamp updated;
	// following to are used for statistics query optimization
	private Timestamp end;
	private Long duration;

	@PrePersist
	private void prePersist() {
		created = new Timestamp(System.currentTimeMillis());
	}

	@PreUpdate
	private void preUpdate() {
		updated = new Timestamp(System.currentTimeMillis());
		setDuration();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@XmlJavaTypeAdapter(LocalDateTimeToString.class)
	public LocalDateTime getCreated() {
		return created == null ? null : created.toLocalDateTime();
	}

	@XmlJavaTypeAdapter(LocalDateTimeToString.class)
	public LocalDateTime getUpdated() {
		return updated == null ? null : updated.toLocalDateTime();
	}

	@XmlJavaTypeAdapter(LocalDateTimeToString.class)
	public LocalDateTime getStart() {
		return start == null ? null : start.toLocalDateTime();
	}

	public void setStart(LocalDateTime start) {
		this.start = start == null ? null : Timestamp.valueOf(start);
	}

	@XmlJavaTypeAdapter(LocalDateTimeToString.class)
	public LocalDateTime getEnd() {
		return end == null ? null : end.toLocalDateTime();
	}

	public void setEnd(LocalDateTime end) {
		this.end = end == null ? null : Timestamp.valueOf(end);
	}

	@XmlAttribute
	@XmlJavaTypeAdapter(DurationToSeconds.class)
	public Duration getDuration() {
		if (State.PLANNED.equals(this.getState())) {
			return null;
		} else if (this.end == null) {
			return Duration.between(this.getStart(), LocalDateTime.now());
		} else {
			return Duration.between(this.getStart(), this.getEnd());
		}
	}

	public void setDuration() {
		Duration duration = getDuration();
		this.duration = duration == null ? null : duration.getSeconds();
	}

	@Override
	public String toString() {
		return "Shift{" +
				"id=" + id +
				", agent=" + agent +
				", start=" + start +
				", state=" + state +
				", created=" + created +
				", updated=" + updated +
				", end=" + end +
				", duration=" + getDuration() +
				'}';
	}

	public enum State {
		PLANNED, DEPLOYED
	}
}
