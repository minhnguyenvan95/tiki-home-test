import dto.RealActivationDateDetail;
import exception.HomeTestException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.log4j.Logger;
import repository.RecordRepository;
import service.CsvReaderService;
import service.RecordService;
import util.DateUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class Main {

    private static final Logger logger = Logger.getLogger("Main");

    private static final String CSV_FILE_PATH = "input.csv";

    public static void main(String[] args) {

        RecordRepository recordRepository = new RecordRepository();
        RecordService recordService = new RecordService(recordRepository);
        // csv reader service will read csv data and index into the repository
        CsvReaderService csvReaderService = new CsvReaderService(recordRepository);

        try {
            // prepare data
            csvReaderService.readCsvFile(CSV_FILE_PATH, 1);

            // list real activation date for prepared data
            List<ImmutablePair<RealActivationDateDetail, Throwable>> immutablePairs = recordService.listRealActivationDate();
            writeListRealActivationDate(immutablePairs);

        } catch (IOException e) {
            logger.error(String.format("Something went wrong when trying to read csv file: %s", CSV_FILE_PATH));
            e.printStackTrace();
        } catch (HomeTestException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeListRealActivationDate(List<ImmutablePair<RealActivationDateDetail, Throwable>> immutablePairs) throws FileNotFoundException, UnsupportedEncodingException {
        // write to file
        PrintWriter writer = new PrintWriter("out.csv", "UTF-8");

        immutablePairs.forEach(pair -> {
            RealActivationDateDetail left = pair.getLeft();
            if (left != null) {
                logger.debug(left);
                // real activation date dto
                writer.println(
                        String.format("%s,%s",
                                left.getPhoneNumber(),
                                DateUtil.formatDate(left.getRealActivationDate())
                        )
                );
            } else {
                pair.getRight().printStackTrace();
            }
        });

        writer.close();
    }
}
