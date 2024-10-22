package com.ozcelikkahve.ozcelikcoffee.Controllers;

import com.ozcelikkahve.ozcelikcoffee.Models.Employees;
import com.ozcelikkahve.ozcelikcoffee.Repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees") // Temel URI belirleniyor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Tüm çalışanları getir
    @GetMapping
    public List<Employees> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Belirli bir çalışanı getir
    @GetMapping("/{id}")
    public Optional<Employees> getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById((id));
    }

    // Yeni bir çalışan ekle
    @PostMapping
    public Employees createEmployee(@RequestBody Employees employee) {
        return employeeRepository.save(employee);
    }

    // Var olan bir çalışanı güncelle
    @PutMapping("/{id}")
    public Employees updateEmployee(@PathVariable Long id, @RequestBody Employees updatedEmployee) {
        return employeeRepository.findById((id))
                .map(employee -> {
                    employee.setFirstName(updatedEmployee.getFirstName());
                    employee.setLastName(updatedEmployee.getLastName());
                    employee.setEmail(updatedEmployee.getEmail());
                    employee.setSalary(updatedEmployee.getSalary());
                    employee.setRole(updatedEmployee.getRole());
                    return employeeRepository.save(employee);
                }).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // Bir çalışanı sil
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeRepository.deleteById((id));
    }
}
