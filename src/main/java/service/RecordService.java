package service;

import com.sun.tools.javac.util.Pair;
import dto.RealActivationDateDetail;
import exception.PeriodNotFoundException;
import exception.RecordServiceException;
import model.Record;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.log4j.Logger;
import repository.RecordRepository;
import util.DateUtil;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecordService {

    private static final int MINIMUM_DAY_PHONE_NUMBER_COULD_BE_USE_BY_ANOTHER = 30; // 30 days

    private Logger logger = Logger.getLogger(getClass());

    private RecordRepository recordRepository;

    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }


    public RealActivationDateDetail findRealActivationDate(String phoneNumber) throws RecordServiceException {

        List<Record> records = recordRepository.getRecordList(phoneNumber);

        if (records.size() > 0) {
            // record list have more than 1 item
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
                        RealActivationDateDetail realActivationDateDetail = new RealActivationDateDetail();
                        realActivationDateDetail.setPhoneNumber(phoneNumber);
                        realActivationDateDetail.setRealActivationDate(recordCurrent.getActivationDate());
                        return realActivationDateDetail;
                    }
                }
            }

            // records list contain only 1 item
            RealActivationDateDetail realActivationDateDetail = new RealActivationDateDetail();
            realActivationDateDetail.setPhoneNumber(phoneNumber);
            realActivationDateDetail.setRealActivationDate(records.get(0).getActivationDate());
            return realActivationDateDetail;
        } else {
            throw new PeriodNotFoundException();
        }
    }

    /**
     * listRealActivationDate with result or exception
     * @return
     */
    public List<ImmutablePair<RealActivationDateDetail, Throwable>> listRealActivationDate() {
        return recordRepository.getRecordKeys().stream()
                .map(String::valueOf)
                .map(phoneNumber -> {
                    ImmutablePair<RealActivationDateDetail, Throwable> pair;
                    try {
                        // find realActivationDate for each key
                        RealActivationDateDetail realActivationDate = findRealActivationDate(phoneNumber);
                        pair = ImmutablePair.of(realActivationDate, null);
                    } catch (RecordServiceException e) {
                        pair = ImmutablePair.of(null, e);
                    }
                    return pair;
                })
                .collect(Collectors.toList());
    }
}
