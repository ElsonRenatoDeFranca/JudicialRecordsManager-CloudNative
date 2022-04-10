package com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.service;

import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.entity.JudicialRecord;
import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.exception.JudicialRecordMismatchException;
import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.exception.JudicialRecordNotFoundException;
import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.exception.JudicialRecordNotProvidedException;

import java.util.List;

public interface JudicialRecordsManagerService {
    List<JudicialRecord> findAll();

    JudicialRecord save(JudicialRecord judicialRecord) throws JudicialRecordMismatchException, JudicialRecordNotProvidedException;

    void deleteByNationalIdentificationNumber(String nationalIdentificationNumber) throws JudicialRecordNotFoundException;

    JudicialRecord findByNationalIdentificationNumber(String nationalIdentificationNumber);

}


