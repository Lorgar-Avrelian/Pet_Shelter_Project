package sky.pro.Animals.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.util.Arrays;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "daily_report")
public class DailyReport {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private byte[] photo;
    private String text;
    private Date date;
    private Long clientId;
    private boolean brows;

    public DailyReport() {
    }

    public DailyReport(Long id, byte[] photo, String text, Date date, Long clientId, boolean brows) {
        this.id = id;
        this.photo = photo;
        this.text = text;
        this.date = date;
        this.clientId = clientId;
        this.brows = brows;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public boolean isBrows() {
        return brows;
    }

    public void setBrows(boolean brows) {
        this.brows = brows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyReport that = (DailyReport) o;
        return Objects.equals(id, that.id) && Objects.equals(date, that.date) && Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, clientId);
    }

    @Override
    public String toString() {
        return "DailyReport{" +
                "id=" + id +
                ", photo=" + Arrays.toString(photo) +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", clientId=" + clientId +
                ", brows=" + brows +
                '}';
    }
}
