package com.lucasdecas.hackerdetection.infrastructure;

import com.lucasdecas.hackerdetection.exceptions.NoReadableFileException;
import com.lucasdecas.hackerdetection.services.DetectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LogfileReader {

    private final Logger LOGGER = LoggerFactory.getLogger(LogfileReader.class);

    private String filePath;
    private DetectorService detectorService;

    @Autowired
    public LogfileReader(DetectorService detectorService, String filePath){
        this.detectorService = detectorService;
        this.filePath = filePath;
    }

    public void openFile() throws Exception {
            File file = new File(filePath);
            if(file.exists() && file.canRead()){
                long fileLength = file.length();
                readNewInputs(file,0L);
                while(true){
                    if(fileLength<file.length()){
                        readNewInputs(file,fileLength);
                        fileLength=file.length();
                    }
                }
            } else{
                throw new NoReadableFileException();
        }
    }

    public void readNewInputs(File file, Long fileLength) throws IOException {
        String line;
        BufferedReader in = new BufferedReader(new FileReader(file));
        try {
            in.skip(fileLength);
            while ((line = in.readLine()) != null) {
                String ip = detectorService.parseLine(line);
                if (ip != null) {
                    System.out.println(ip);
                }
            }
        } catch(Exception e){
            LOGGER.error("Error while reading from file");
        } finally {
            in.close();
        }
    }

}
