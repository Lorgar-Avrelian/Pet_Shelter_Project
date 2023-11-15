package sky.pro.Animals.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

import static javax.persistence.GenerationType.*;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String fio;
    private String address;
    private LocalDate birthday;
    private String passport;
    private Integer chatId;
    @OneToMany(mappedBy = "client")
    private Collection<Long> pets;

    public Client() {
    }

    public Client(Long id, String fio, String address, LocalDate birthday, String passport, Integer chatId, Collection<Long> pets) {
        this.id = id;
        this.fio = fio;
        this.address = address;
        this.birthday = birthday;
        this.passport = passport;
        this.chatId = chatId;
        this.pets = pets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Collection<Long> getPets() {
        return pets;
    }

    public void setPets(Collection<Long> pets) {
        this.pets = pets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", fio='" + fio + '\'' +
                ", address='" + address + '\'' +
                ", birthday=" + birthday +
                ", passport='" + passport + '\'' +
                ", chatId=" + chatId +
                ", pets=" + pets +
                '}';
    }
}
