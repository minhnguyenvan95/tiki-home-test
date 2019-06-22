package dto;

import util.DateUtil;

import java.util.Date;

public class RealActivationDateDetail {
    private String phoneNumber;
    private Date realActivationDate;

    public RealActivationDateDetail() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getRealActivationDate() {
        return realActivationDate;
    }

    public void setRealActivationDate(Date realActivationDate) {
        this.realActivationDate = realActivationDate;
    }

    @Override
    public String toString() {
        return String.format(
                "=== Real Activation Date === %s, %s",
                phoneNumber,
                DateUtil.formatDate(realActivationDate)
        );
    }
}
