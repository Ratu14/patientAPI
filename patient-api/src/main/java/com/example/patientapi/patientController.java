package com.example.patientapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootApplication
@RestController

@CrossOrigin(origins = "*")

public class patientController {


	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(patientController.class, args);
	}


	@PostMapping("/add_patient")
	public ResponseEntity<String> addPatient(@RequestBody patient patient) {
		try {
			String sql = "INSERT INTO patientList (serial_no, uhid, patient_name, investigation, bill, date) VALUES (?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, patient.getSerialNo(), patient.getUhid(), patient.getPatientName(),
					patient.getInvestigation(), patient.getBill(), patient.getDate());
			return ResponseEntity.status(HttpStatus.OK).body("Patient added successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	class PatientRowMapper implements RowMapper<patient> {
		@Override
		public patient mapRow(ResultSet rs, int rowNum) throws SQLException {
			return mapResultSetToPatient(rs);
		}
	}


	@GetMapping("/patients/six_months_old")
	public ResponseEntity<List<patient>> getPatientsSixMonthsOld() {
		try {
			LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
			String formattedDate = sixMonthsAgo.format(DateTimeFormatter.ofPattern("dd-MM-yy"));
			String sql = "SELECT * FROM patientList WHERE STR_TO_DATE(date, '%d-%m-%y') = STR_TO_DATE(?, '%d-%m-%y')";
			List<patient> patients = jdbcTemplate.query(sql, new PatientRowMapper(), formattedDate);
			return ResponseEntity.status(HttpStatus.OK).body(patients);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}



	@GetMapping("/allpatients")
	public ResponseEntity<List<patient>> getAllPatients() {
		try {
			String sql = "SELECT * FROM patientList";
			List<patient> patients = jdbcTemplate.query(sql, new PatientRowMapper());
			return ResponseEntity.status(HttpStatus.OK).body(patients);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}


	@GetMapping("/patients/one_year_old")
	public ResponseEntity<List<patient>> getPatientsOneYearOld() {
		try {
			LocalDate oneYearAgo = LocalDate.now().minusYears(1);
			String formattedDate = oneYearAgo.format(DateTimeFormatter.ofPattern("dd-MM-yy"));
			String sql = "SELECT * FROM patientList WHERE STR_TO_DATE(date, '%d-%m-%y') = STR_TO_DATE(?, '%d-%m-%y')";
			List<patient> patients = jdbcTemplate.query(sql, new PatientRowMapper(), formattedDate);
			return ResponseEntity.status(HttpStatus.OK).body(patients);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// Helper method to map ResultSet to Patient object
	private patient mapResultSetToPatient(ResultSet rs) throws SQLException {
		patient patient = new patient();
		patient.setSerialNo(rs.getInt("serial_no"));
		patient.setUhid(rs.getString("uhid"));
		patient.setPatientName(rs.getString("patient_name"));
		patient.setInvestigation(rs.getString("investigation"));
		patient.setBill(rs.getString("bill"));
		patient.setDate(rs.getString("date"));
		return patient;
	}
}