import dto.RealActivationDateDetail;
import exception.CsvServiceException;
import exception.HomeTestException;
import exception.InvalidRecordDataException;
import exception.RecordServiceException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import repository.RecordRepository;
import service.CsvReaderService;
import service.RecordService;
import util.DateUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserRecordServiceTests {

    private Logger logger = Logger.getLogger(getClass());

    private static final String CSV_FILE_PATH = "input.csv";

    private RecordRepository recordRepository;
    private RecordService recordService;
    private CsvReaderService csvReaderService;

    public UserRecordServiceTests() throws Exception {
        recordRepository = new RecordRepository();
        recordService = new RecordService(recordRepository);
        // csv reader service will read csv data and index into the repository
        csvReaderService = new CsvReaderService(recordRepository);

        try {
            csvReaderService.readCsvFile(CSV_FILE_PATH, 1);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void findRealActivationDateTest() throws RecordServiceException {
        final String phoneNumber = "0987000001";
        RealActivationDateDetail realActivationDate = recordService.findRealActivationDate(phoneNumber);

        logger.debug(realActivationDate);
        Assert.assertEquals(
                DateUtil.formatDate(realActivationDate.getRealActivationDate()),
                "2016-06-01"
        );
    }

    @Test
    public void listUniquePhoneNumberWithRealActivationDateTest() {
        List<ImmutablePair<RealActivationDateDetail, Throwable>> immutablePairs = recordService.listRealActivationDate();

        immutablePairs.forEach(realActivationDateDetailThrowableImmutablePair -> {
            RealActivationDateDetail left = realActivationDateDetailThrowableImmutablePair.getLeft();

            if (left != null) {
                logger.debug(left);
            } else {
                realActivationDateDetailThrowableImmutablePair.getRight().printStackTrace();
            }

        });

        Assert.assertTrue(
                immutablePairs.stream()
                        .map(ImmutablePair::getRight)
                        .filter(Objects::isNull)
                        .collect(Collectors.toList())
                        .size() > 0
        );
    }
}
