package sky.pro.Animals.entity;

import java.sql.Date;
import java.util.Objects;

public class DailyReportList {
    private Long id;
    private Date date;
    private Long clientId;

    public DailyReportList(Long id, Date date, Long clientId) {
        this.id = id;
        this.date = date;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyReportList that = (DailyReportList) o;
        return Objects.equals(id, that.id) && Objects.equals(date, that.date) && Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, clientId);
    }

    @Override
    public String toString() {
        return "DailyReportList{" +
                "id=" + id +
                ", date=" + date +
                ", clientId=" + clientId +
                '}';
    }
}
