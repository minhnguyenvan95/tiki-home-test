package repository;

import dto.RecordDetail;
import exception.PhoneNumberNotFoundException;
import model.Record;
import util.DateUtil;

import java.text.ParseException;
import java.util.*;

public class RecordRepository {
    private Map<String, List<Record>> usageRecordMap;

    public RecordRepository() {
        this.usageRecordMap = new HashMap<>();
    }

    /**
     * put userRecordDetail to repository
     *
     * @param recordDetail
     * @throws ParseException
     */
    public void put(RecordDetail recordDetail) throws ParseException {
        final String phoneNumber = recordDetail.getPhoneNumber();

        // prepare record model
        Record record = new Record();
        record.setActivationDate(DateUtil.parseRecordDate(recordDetail.getActivationDate()));

        if (recordDetail.getDeActivationDate() != null) {
            record.setDeActivationDate(DateUtil.parseRecordDate(recordDetail.getDeActivationDate()));
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
}
