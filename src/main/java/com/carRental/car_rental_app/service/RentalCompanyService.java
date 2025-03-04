package com.carRental.car_rental_app.service;

import com.carRental.car_rental_app.entity.RentalCompany;
import com.carRental.car_rental_app.repository.RentalCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RentalCompanyService {

    @Autowired
    private RentalCompanyRepository repository;

    //GET ALL
    public List<RentalCompany> getAllRentalCompanies() {
        return repository.findAll();
    }

    //GET BY ID
    public RentalCompany getRentalCompanyById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
    }

    //CREATE
    public RentalCompany createRentalCompany(RentalCompany company) {
        return repository.save(company);
    }

    // UPDATE BY ID
    public RentalCompany updateRentalCompanyById(Long id, RentalCompany updatedCompany) {
        RentalCompany existing = getRentalCompanyById(id);
        existing.setName(updatedCompany.getName());
        existing.setLocation(updatedCompany.getLocation());
        existing.setEmail(updatedCompany.getEmail());
        return repository.save(existing);
    }

    //DELETE BY ID
    public void deleteRentalCompanyById(Long id) {
        repository.deleteById(id);
    }

    // PAGINATION
    public Page<RentalCompany> getRentalCompaniesWithPagination(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    // SORTING
    public List<RentalCompany> getRentalCompaniesWithSorting(String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return repository.findAll(sort);
    }

    // JPQL
    public List<RentalCompany> getRentalCompaniesByLocation(String location) {
        return repository.findByLocation(location);
    }

    //JPQL - count vehicles per company
//    public List<Object[]> countVehiclesPerCompany() {
//        return repository.countVehiclesPerCompany();
//    }

    //JPA
    //  Update name by location (Transactional at service layer)
    @Transactional
    public int updateCompanyNameByLocation(String location, String newName) {
        return repository.updateByLocation(location, newName);
    }
}


