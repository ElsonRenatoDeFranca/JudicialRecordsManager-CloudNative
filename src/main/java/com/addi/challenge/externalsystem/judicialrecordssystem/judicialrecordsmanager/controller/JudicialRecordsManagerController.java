package com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.controller;

import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.entity.JudicialRecord;
import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.exception.JudicialRecordMismatchException;
import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.exception.JudicialRecordNotFoundException;
import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.exception.JudicialRecordNotProvidedException;
import com.addi.challenge.externalsystem.judicialrecordssystem.judicialrecordsmanager.service.JudicialRecordsManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/judicialrecords")
@Slf4j
public class JudicialRecordsManagerController {

    private final JudicialRecordsManagerService judicialRecordsManagerService;

    public JudicialRecordsManagerController(JudicialRecordsManagerService judicialRecordsManagerService) {
        this.judicialRecordsManagerService = judicialRecordsManagerService;
    }

    @GetMapping
    @Operation(summary = "This is to fetch all judicial records in the Judicial Manager System")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetched all judicial records",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503",
                    description = "The service is not available",
                    content = @Content)

    })
    public ResponseEntity<List<JudicialRecord>> findAll() {
        return new ResponseEntity<>(judicialRecordsManagerService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "This is to associate a judicial record to a person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Saved a judicial record to database",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "406",
                    description = "The correct information was not sent to database",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503",
                    description = "The service is not available",
                    content = @Content)
    })
    public ResponseEntity<JudicialRecord> save(@RequestBody JudicialRecord judicialRecord) {
        try {
            JudicialRecord savedJudicialRecord = judicialRecordsManagerService.save(judicialRecord);
            return new ResponseEntity<>(savedJudicialRecord, HttpStatus.CREATED);
        } catch (JudicialRecordMismatchException | JudicialRecordNotProvidedException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{nationalIdentificationNumber}")
    @ResponseBody
    @Operation(summary = "This is to fetch a judicial recordby using its national ID number as key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetched a judicial record successfully.",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "The judicial record was not found at database",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503",
                    description = "The service is not available",
                    content = @Content)
    })
    public ResponseEntity<JudicialRecord> findByNationalIdentificationNumber(@PathVariable("nationalIdentificationNumber") String nationalIdentificationNumber) {
        JudicialRecord judicialRecords = judicialRecordsManagerService.findByNationalIdentificationNumber(nationalIdentificationNumber);
        return new ResponseEntity<>(judicialRecords, HttpStatus.OK);
    }

    @DeleteMapping("/{nationalIdentificationNumber}")
    @Operation(summary = "This is to delete a specific judicial record by using its national ID number as key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Deleted the judicial record.",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "The judicial record was not found.",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503",
                    description = "The service is not available",
                    content = @Content)
    })
    void deleteByNationalIdentificationNumber(@PathVariable String nationalIdentificationNumber) {
        try {
            judicialRecordsManagerService.deleteByNationalIdentificationNumber(nationalIdentificationNumber);
        } catch (JudicialRecordNotFoundException e) {
            log.info(e.getMessage());
        }
    }
}
