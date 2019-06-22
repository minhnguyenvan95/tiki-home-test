package model;

import java.util.Date;

public class Record {
    //TODO: could store subscriber info here : idCard, address, name
    private Date activationDate;
    private Date deActivationDate;

    public Record() {
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public Date getDeActivationDate() {
        return deActivationDate;
    }

    public void setDeActivationDate(Date deActivationDate) {
        this.deActivationDate = deActivationDate;
    }

}
