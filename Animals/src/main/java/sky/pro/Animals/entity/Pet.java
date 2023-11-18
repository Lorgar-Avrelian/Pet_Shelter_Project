package sky.pro.Animals.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

import static javax.persistence.GenerationType.*;

@Entity
@Table(name = "pet")
public class Pet {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private LocalDate birthday;
    private boolean alive;
    private String petVariety;
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;
    public Pet() {
    }

    public Pet(Long id, String name, LocalDate birthday, boolean alive, String petVariety, Client client) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.alive = alive;
        this.petVariety = petVariety;
        this.client = client;
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
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public String getPetVariety() {
        return petVariety;
    }

    public void setPetVariety(String petVariety) {
        this.petVariety = petVariety;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return Objects.equals(id, pet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", alive=" + alive +
                ", petVariety='" + petVariety + '\'' +
                ", client=" + client +
                '}';
    }
}
