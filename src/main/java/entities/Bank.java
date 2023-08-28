package entities;

public class Bank {
    private String name;

    public Bank (String name){
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
