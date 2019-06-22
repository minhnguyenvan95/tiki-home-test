package service;

import dto.RecordDetail;
import exception.CsvServiceException;
import exception.IllegalCsvRecordFormatException;
import org.apache.log4j.Logger;
import repository.RecordRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

public class CsvReaderService {

    private Logger logger = Logger.getLogger(getClass());

    private RecordRepository recordRepository;

    public CsvReaderService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    /**
     * getRecordRepository
     * @return
     */
    public RecordRepository getRecordRepository() {
        return recordRepository;
    }

    /**
     * readCsvFile to prepare recordRepository
     *
     * @param filePath
     * @param numberOfHeaderRowToBeSkip
     * @throws IOException
     */
    public void readCsvFile(String filePath, int numberOfHeaderRowToBeSkip) throws IOException, CsvServiceException {
        File file = new File(filePath);
        FileReader fileReader = new FileReader(file);

        // Handle IOException and release the resource with try-with-resources
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            // numberOfHeaderRowToBeSkip before read the file
            for (int i = 0; i < numberOfHeaderRowToBeSkip; i++) {
                String s = bufferedReader.readLine();
                if (s != null) {
                    logger.debug(String.format("=== Skip line : %d === %s", i, s));
                }
            }

            String line;
            // Read file and index into repository
            while ((line = bufferedReader.readLine()) != null) {
                logger.debug(line);
                RecordDetail recordDetail = parseRecordDetail(line);

                // index into repository
                try {
                    recordRepository.put(recordDetail);
                } catch (ParseException e) {
                    logger.error(String.format("Something went wrong went trying to index : %s", line));
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * return RecordDetail
     * @param line
     * @return
     * @throws IllegalCsvRecordFormatException
     */
    private RecordDetail parseRecordDetail(String line) throws IllegalCsvRecordFormatException {
        String[] parts = line.split(",");

        // FIXME: each record must have at least 2 argument
        if (parts.length >= 2) {
            //prepare dto
            RecordDetail recordDetail = new RecordDetail();

            recordDetail.setPhoneNumber(parts[0]);
            recordDetail.setActivationDate(parts[1]);

            if (parts.length == 3) {
                recordDetail.setDeActivationDate(parts[2]);
            }
            return recordDetail;
        } else {
            throw new IllegalCsvRecordFormatException();
        }
    }
}
