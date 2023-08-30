package entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    public final String userName;
    public final String userSurname;
    public final String userMiddlename;

    public User(String name, String surname, String middlename) {
        this.userName = name;
        this.userSurname = surname;
        this.userMiddlename = middlename;
    }
}
