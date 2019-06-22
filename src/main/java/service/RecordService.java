package service;

import exception.PeriodNotFoundException;
import exception.RecordServiceException;
import model.Record;
import org.apache.log4j.Logger;
import repository.RecordRepository;
import util.DateUtil;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RecordService {

    private static final int MINIMUM_DAY_PHONE_NUMBER_COULD_BE_USE_BY_ANOTHER = 30; // 30 days

    private Logger logger = Logger.getLogger(getClass());

    private RecordRepository recordRepository;

    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }


    public Date findRealActivationDate(String phoneNumber) throws RecordServiceException {

        List<Record> records = recordRepository.getRecordList(phoneNumber);

        if (records.size() > 0) {
            if (records.size() > 1) {

                // make sure all item in list have been sorted by activationDate ASC
                for (int i = records.size() - 1; i > 0; i--) {
                    Record recordCurrent = records.get(i);
                    Record recordPrevious = records.get(i - 1);

                    // compare currentActivationDate to previousActivationDate
                    long dateDiff = DateUtil.getDateDiff(
                            recordPrevious.getDeActivationDate(),
                            recordCurrent.getActivationDate(),
                            TimeUnit.DAYS
                    );

                    // check if phoneNumber have change their owner by the MINIMUM_DAY_PHONE_NUMBER_COULD_BE_USE_BY_ANOTHER condition
                    if (dateDiff >= MINIMUM_DAY_PHONE_NUMBER_COULD_BE_USE_BY_ANOTHER) {
                        return recordCurrent.getActivationDate();
                    }
                }
            }

            return records.get(0).getActivationDate();
        } else {
            throw new PeriodNotFoundException();
        }
    }
}
