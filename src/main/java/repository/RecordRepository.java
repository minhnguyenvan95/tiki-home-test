package repository;

import dto.RecordDetail;
import exception.InvalidRecordDataException;
import exception.PhoneNumberNotFoundException;
import model.Record;
import util.DateUtil;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class RecordRepository {
    private Map<String, List<Record>> usageRecordMap;

    public RecordRepository() {
        this.usageRecordMap = new HashMap<>();
    }

    /**
     * validate deactivationDate for recordDetail
     * @param recordDetail
     * @return
     */
    public boolean validateRecordDetailDeactivationDate(RecordDetail recordDetail) {
        if (recordDetail.getDeactivationDate() == null) {
            if (usageRecordMap.containsKey(recordDetail.getPhoneNumber())) {
                List<Record> records = usageRecordMap.get(recordDetail.getPhoneNumber());

                // validate there is no deactivationDate record in record array for a specific phone number
                return records.stream().noneMatch(
                        record -> record.getDeactivationDate() == null
                );
            }
        }
        return true;
    }

    /**
     * put userRecordDetail to repository
     *
     * @param recordDetail
     * @throws ParseException
     */
    public void put(RecordDetail recordDetail) throws ParseException, InvalidRecordDataException {
        final String phoneNumber = recordDetail.getPhoneNumber();

        // prepare record model
        Record record = new Record();
        record.setActivationDate(DateUtil.parseRecordDate(recordDetail.getActivationDate()));

        if (recordDetail.getDeactivationDate() != null) {
            record.setDeactivationDate(DateUtil.parseRecordDate(recordDetail.getDeactivationDate()));
        }

        // only allow 1 empty deactivation date for a specific phone number
        // a phone number could be used only by one person at a same time
        if (!validateRecordDetailDeactivationDate(recordDetail)) {
            throw new InvalidRecordDataException();
        }

        // check if phoneNumber key is exists
        if (usageRecordMap.containsKey(phoneNumber)) {
            List<Record> recordList = usageRecordMap.get(phoneNumber);
            recordList.add(record);

            // sort after insert by activationDate
            recordList.sort(Comparator.comparing(Record::getActivationDate));
        } else {
            // new arrayList
            ArrayList<Record> recordArrayList = new ArrayList<>();
            recordArrayList.add(record);
            usageRecordMap.put(phoneNumber, recordArrayList);
        }
    }

    /**
     * getRecordList
     *
     * @param phoneNumber
     * @return
     * @throws PhoneNumberNotFoundException
     */
    public List<Record> getRecordList(String phoneNumber) throws PhoneNumberNotFoundException {
        if (usageRecordMap.containsKey(phoneNumber)) {
            return usageRecordMap.get(phoneNumber);
        } else {
            throw new PhoneNumberNotFoundException();
        }
    }

    /**
     * getRecordKeys
     *
     * @return
     */
    public Set<String> getRecordKeys() {
        return usageRecordMap.keySet();
    }
}
