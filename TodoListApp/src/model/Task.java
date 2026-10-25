package model;

import java.io.Serializable;

/** Represents one record in the Name/Payment/Status/Enter/Leave form. */
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private String bid;
    private String name;
    private String payment;
    private String status;
    private String enter;
    private String leave;

    public String getBid() { return bid; }
    public void setBid(String bid) { this.bid = bid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPayment() { return payment; }
    public void setPayment(String payment) { this.payment = payment; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getEnter() { return enter; }
    public void setEnter(String enter) { this.enter = enter; }
    public String getLeave() { return leave; }
    public void setLeave(String leave) { this.leave = leave; }

    @Override
    public String toString() {
        return "Task{" + "bid=" + bid + ", name=" + name + ", payment=" + payment
                + ", status=" + status + ", enter=" + enter + ", leave=" + leave + '}';
    }
}
