package com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.service;

import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.entity.JudicialRecord;
import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.exception.JudicialRecordMismatchException;
import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.exception.JudicialRecordNotFoundException;
import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.exception.JudicialRecordNotProvidedException;
import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.repository.JudicialRecordManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JudicialRecordsManagerServiceImpl implements JudicialRecordsManagerService {

    private final JudicialRecordManagerRepository judicialRecordManagerRepository;

    private static final String JUDICIAL_RECORD_NOT_FOUND_EXCEPTION_MESSAGE = "Judicial record not found.";
    private static final String JUDICIAL_RECORD_ALREADY_EXISTS_EXCEPTION_MESSAGE = "Judicial record already exists.";
    private static final String JUDICIAL_RECORD_NOT_PROVIDED_EXCEPTION_MESSAGE = "Judicial record not provided.";


    public JudicialRecordsManagerServiceImpl(JudicialRecordManagerRepository judicialRecordManagerRepository) {
        this.judicialRecordManagerRepository = judicialRecordManagerRepository;
    }

    @Override
    public List<JudicialRecord> findAll() {
        return judicialRecordManagerRepository.findAll();
    }

    @Override
    public JudicialRecord save(JudicialRecord judicialRecord) throws JudicialRecordMismatchException, JudicialRecordNotProvidedException {

        if (judicialRecord != null) {
            if (judicialRecordAlreadyExists(judicialRecord.getNationalIdentificationNumber())) {
                throw new JudicialRecordMismatchException(JUDICIAL_RECORD_ALREADY_EXISTS_EXCEPTION_MESSAGE);
            }
            return judicialRecordManagerRepository.save(judicialRecord);
        } else {
            throw new JudicialRecordNotProvidedException(JUDICIAL_RECORD_NOT_PROVIDED_EXCEPTION_MESSAGE);
        }

    }

    @Override
    public void deleteByNationalIdentificationNumber(String nationalIdentificationNumber) throws JudicialRecordNotFoundException {
        if (judicialRecordAlreadyExists(nationalIdentificationNumber)) {
            judicialRecordManagerRepository.deleteByNationalIdentificationNumber(nationalIdentificationNumber);
        } else {
            throw new JudicialRecordNotFoundException(JUDICIAL_RECORD_NOT_FOUND_EXCEPTION_MESSAGE);
        }

    }

    @Override
    public JudicialRecord findByNationalIdentificationNumber(String nationalIdentificationNumber) {
        return judicialRecordManagerRepository.findByNationalIdentificationNumber(nationalIdentificationNumber);
    }


    private boolean judicialRecordAlreadyExists(String nationalIdentificationNumber) {
        JudicialRecord judicialRecord = findByNationalIdentificationNumber(nationalIdentificationNumber);
        return judicialRecord != null;
    }

}
