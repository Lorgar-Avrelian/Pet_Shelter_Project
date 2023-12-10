package sky.pro.Animals.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.sql.Date;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "probation")
public class ProbationPeriod {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long clientId;
    private Long petId;
    private Date lastDate;

    public ProbationPeriod() {
    }

    public ProbationPeriod(Long id, Long clientId, Long petId, Date lastDate) {
        this.id = id;
        this.clientId = clientId;
        this.petId = petId;
        this.lastDate = lastDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProbationPeriod that = (ProbationPeriod) o;
        return Objects.equals(id, that.id) && Objects.equals(clientId, that.clientId) && Objects.equals(petId, that.petId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, petId);
    }

    @Override
    public String toString() {
        return "ProbationPeriod{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", petId=" + petId +
                ", lastDate=" + lastDate +
                '}';
    }
}
