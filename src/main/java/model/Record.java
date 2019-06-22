package model;

import java.util.Date;

public class Record {
    //TODO: could store subscriber info here : idCard, address, name
    private Date activationDate;
    private Date deactivationDate;

    public Record() {
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public Date getDeactivationDate() {
        return deactivationDate;
    }

    public void setDeactivationDate(Date deactivationDate) {
        this.deactivationDate = deactivationDate;
    }

}
