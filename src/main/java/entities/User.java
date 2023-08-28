package entities;

public class User {
    private String userFIO;

    public User(String FIO){
        this.userFIO = FIO;
    }

    public String getUserFIO(){
        return this.userFIO;
    }

    public void setUserFIO(String FIO){
        this.userFIO = FIO;
    }
}
