package entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Bank {
    public final String name;
    public final int id;

    public Bank(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
