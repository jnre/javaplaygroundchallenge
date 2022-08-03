package com.example.swechallenge.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.swechallenge.OffsetBasedPageRequest;
import com.example.swechallenge.message.ResponseMessage;
import com.example.swechallenge.model.Income;
import com.example.swechallenge.repository.IncomeRepository;
import com.example.swechallenge.service.CSVHelper;
import com.example.swechallenge.service.CSVService;

@RestController

public class IncomeController {
	@Autowired
	CSVService CSVService;
	@Autowired
	IncomeRepository IncomeRepository;

	@GetMapping("/users")
	public ResponseEntity<Map<String,List<Income>>> getAllUsers(
			@RequestParam Map<String, String> params) {
		try {

			List<Income> users = new ArrayList<Income>();
			BigDecimal minSalary = new BigDecimal(0.00);
			BigDecimal maxSalary = new BigDecimal(4000.00);
			Integer offset = 0;
			Integer limit = Integer.MAX_VALUE;
			String sort = "";
			if (params.containsKey("min")) {
				minSalary = new BigDecimal(params.get("min"));
			}
			if (params.containsKey("max")) {
				maxSalary = new BigDecimal(params.get("max"));
			}
			if (params.containsKey("limit")) {

				limit = Integer.parseInt(params.get("limit"));
			}
			if (params.containsKey("offset")) {

				offset = Integer.parseInt(params.get("offset"));
			}
			if (params.containsKey("sort")) {

				sort = (params.get("sort"));
				// System.out.println(minSalary);
				// System.out.println(maxSalary);
				// System.out.println(limit);
				// System.out.println(offset);
				// System.out.println(sort);
				Pageable pageable = new OffsetBasedPageRequest(offset, limit, Sort.Direction.ASC, sort);
				IncomeRepository.findByRangeSalaryPaginationSorting(minSalary, maxSalary, pageable)
						.forEach(users::add);
			} else {

				IncomeRepository.findByRangeSalaryPagination(minSalary, maxSalary, limit, offset).forEach(users::add);
			}
			// IncomeRepository.findByRangeSalary(minSalary, maxSalary).forEach(users::add);

			if (users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			Map<String,List<Income>> formatedUsers = new HashMap<>();
			formatedUsers.put("results", users);
			return new ResponseEntity<>(formatedUsers, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestPart("file") MultipartFile file) {
		Integer message = 1;
		if (CSVHelper.hasCSVFormat(file)) {
			try {
				CSVService.save(file);
				message = 1;
				return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.OK);
				// return ResponseEntity.status(HttpStatus.OK).body(new
				// ResponseMessage(message));
			} catch (Exception e) {
				message = 0;
				// message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.EXPECTATION_FAILED);
				// return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new
				// ResponseMessage(message));
			}
		}
		// message = "Please upload a csv file!";
		message = 0;
		return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.BAD_REQUEST);
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
		// ResponseMessage(message));
	}

}
