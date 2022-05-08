package dk.au.mad22spring.janesbuns.models;

import java.sql.Date;
import java.sql.Time;
import java.util.List;



public class Order {
    public enum Status {
        RECEIVED,
        CANCELLED,
        CONFIRMED,
        SENT
    }

    public List<CreamBun> creamBuns;
    public String date;
    public String time;
    public Status status;
    public String userUid;

    public Order() {
    }

    public Order(List<CreamBun> creamBuns, String date, String time, Status status) {
        this.creamBuns = creamBuns;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Order(List<CreamBun> creamBuns, String date, String time, Status status, String userUid) {
        this.creamBuns = creamBuns;
        this.date = date;
        this.time = time;
        this.status = status;
        this.userUid = userUid;
    }

    public void setStatus(Status status1) {
        this.status = status1;
    }
}
