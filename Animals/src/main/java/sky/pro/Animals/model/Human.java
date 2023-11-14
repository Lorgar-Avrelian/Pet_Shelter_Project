package sky.pro.Animals.model;

import java.util.Objects;

public class Human {
    private String fio;
    private String address;
    private String birthday;
    private String passport;

    public Human(String fio, String address, String birthday, String passport) {
        this.fio = fio;
        this.address = address;
        this.birthday = birthday;
        this.passport = passport;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Human human = (Human) o;
        return Objects.equals(fio, human.fio) && Objects.equals(address, human.address) && Objects.equals(birthday, human.birthday) && Objects.equals(passport, human.passport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fio, address, birthday, passport);
    }

    @Override
    public String toString() {
        return "human{" +
                "fio='" + fio + '\'' +
                ", address='" + address + '\'' +
                ", birthday='" + birthday + '\'' +
                ", passport='" + passport + '\'' +
                '}';
    }
}
