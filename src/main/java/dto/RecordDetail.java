package dto;

public class RecordDetail {
    private String phoneNumber;
    private String activationDate;
    private String deActivationDate;

    public RecordDetail() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    public String getDeActivationDate() {
        return deActivationDate;
    }

    public void setDeActivationDate(String deActivationDate) {
        this.deActivationDate = deActivationDate;
    }
}
