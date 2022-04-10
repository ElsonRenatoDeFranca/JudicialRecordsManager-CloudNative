package com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.controller;

import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.entity.JudicialRecord;
import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.exception.JudicialRecordMismatchException;
import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.exception.JudicialRecordNotFoundException;
import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.exception.JudicialRecordNotProvidedException;
import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.service.JudicialRecordsManagerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JudicialRecordsManagerControllerTest {

    private static final String JUDICIAL_RECORD_NOT_PROVIDED_EXCEPTION_MESSAGE = "Judicial record not provided";
    private static final String JUDICIAL_RECORD_NOT_FOUND_EXCEPTION_MESSAGE = "Judicial record not found";

    @Mock
    private JudicialRecordsManagerService judicialRecordsManagerService;

    @InjectMocks
    private JudicialRecordsManagerController judicialRecordsManagerController;

    @Test
    public void shouldReturnANotEmptyListWhenFindAllIsCalledAndThereIsAtLeastOneItemInTheDatabase() {
        when(judicialRecordsManagerService.findAll()).thenReturn(createNotEmptyJudicialRecordMockList());

        ResponseEntity<List<JudicialRecord>> actualCriminalOffence = this.judicialRecordsManagerController.findAll();

        assertThat(actualCriminalOffence).isNotNull();
        assertThat(actualCriminalOffence.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnAnEmptyListWhenFindAllIsCalledAndThereIsNoItemInDatabase() {
        when(judicialRecordsManagerService.findAll()).thenReturn(createEmptyJudicialRecordMockList());

        ResponseEntity<List<JudicialRecord>> actualJudicialRecords = this.judicialRecordsManagerController.findAll();

        assertThat(actualJudicialRecords).isNotNull();
        assertThat(actualJudicialRecords.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnNotNullWhenFindByNationalIdentificationNumberIsCalled() {
        JudicialRecord expectedJudicialRecord = createJudicialRecordMock();

        when(judicialRecordsManagerService.findByNationalIdentificationNumber(any())).thenReturn(expectedJudicialRecord);

        ResponseEntity<JudicialRecord> actualPerson = this.judicialRecordsManagerController.findByNationalIdentificationNumber(expectedJudicialRecord.getNationalIdentificationNumber());

        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualPerson.getBody()).isEqualTo(expectedJudicialRecord);
    }

    @Test
    public void shouldDeleteAnExistingJudicialRecordFromTheDatabaseWhenDeleteByIdIsCalled() throws JudicialRecordNotFoundException {
        JudicialRecord expectedJudicialRecord = createJudicialRecordMock();

        doNothing().when(judicialRecordsManagerService).deleteByNationalIdentificationNumber(any());

        this.judicialRecordsManagerController.deleteByNationalIdentificationNumber(expectedJudicialRecord.getNationalIdentificationNumber());

        verify(judicialRecordsManagerService, atLeast(1)).deleteByNationalIdentificationNumber(any());
    }

    @Test
    public void shouldSaveANewJudicialRecordToTheDatabaseWhenSaveMethodIsCalled() throws JudicialRecordMismatchException, JudicialRecordNotProvidedException {
        JudicialRecord expectedJudicialRecord = createJudicialRecordMock();

        when(judicialRecordsManagerService.save(any())).thenReturn(expectedJudicialRecord);

        ResponseEntity<JudicialRecord> actualJudicialRecord = this.judicialRecordsManagerController.save(expectedJudicialRecord);

        assertThat(actualJudicialRecord).isNotNull();
        assertThat(actualJudicialRecord.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(judicialRecordsManagerService, atLeast(1)).save(any());
    }


    @Test
    public void shouldThrowJudicialRecordNotProvidedExceptionWhenTriedToSaveWithoutPassingAnyElementInTheBody() throws JudicialRecordNotProvidedException, JudicialRecordMismatchException {
        JudicialRecord expectedJudicialRecord = createJudicialRecordMock();

        when(judicialRecordsManagerService.save(any())).thenThrow(new JudicialRecordNotProvidedException(JUDICIAL_RECORD_NOT_PROVIDED_EXCEPTION_MESSAGE));

        Throwable exception = assertThrows(JudicialRecordNotProvidedException.class,
                () -> this.judicialRecordsManagerService.save(expectedJudicialRecord));

        assertThat(JUDICIAL_RECORD_NOT_PROVIDED_EXCEPTION_MESSAGE).isEqualTo(exception.getMessage());
    }

    @Test
    public void shouldThrowJudicialRecordNotFoundExceptionWhenTriedToDeleteAJudicialRecordThatDoesNotExist() throws JudicialRecordNotFoundException {
        JudicialRecord expectedJudicialRecord = createJudicialRecordMock();

        doThrow(new JudicialRecordNotFoundException(JUDICIAL_RECORD_NOT_FOUND_EXCEPTION_MESSAGE)).when(judicialRecordsManagerService).deleteByNationalIdentificationNumber(any());

        Throwable exception = assertThrows(JudicialRecordNotFoundException.class,
                () -> this.judicialRecordsManagerService.deleteByNationalIdentificationNumber(expectedJudicialRecord.getNationalIdentificationNumber()));

        assertThat(JUDICIAL_RECORD_NOT_FOUND_EXCEPTION_MESSAGE).isEqualTo(exception.getMessage());

    }


    private List<JudicialRecord> createNotEmptyJudicialRecordMockList() {
        return Arrays.asList(createJudicialRecordMock(), createJudicialRecordMock());
    }

    private List<JudicialRecord> createEmptyJudicialRecordMockList() {
        return Collections.emptyList();
    }

    private JudicialRecord createJudicialRecordMock() {
        return JudicialRecord.builder()
                .id(1L)
                .nationalIdentificationNumber("90001")
                .build();
    }


}