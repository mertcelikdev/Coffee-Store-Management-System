package com.ozcelikkahve.ozcelikcoffee.Controllers;

import com.ozcelikkahve.ozcelikcoffee.Models.Employees;
import com.ozcelikkahve.ozcelikcoffee.Models.Roles;
import com.ozcelikkahve.ozcelikcoffee.Repositories.EmployeeRepository;
import com.ozcelikkahve.ozcelikcoffee.Repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RolesRepository roleRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/employees"; // Görsellerin kaydedileceği dizin

    // Listeleme: Tüm çalışanları görüntüleme
    @GetMapping
    public String listEmployees(Model model) {
        List<Employees> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "Employees/Employees"; // employeeList.html sayfası
    }

    // Yeni Çalışan Ekleme: Formu görüntüleme
    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employees", new Employees());
        List<Roles> roles = roleRepository.findAll();
        model.addAttribute("roles", roles); // Rolleri view'ye göndermek
        return "Employees/AddEmployee"; // addEmployee.html sayfası
    }

    // Yeni Çalışan Ekleme: Formdan gelen verileri işleme
    @PostMapping("/add")
    public String addEmployee(@Validated @ModelAttribute("employees") Employees employee,
                              @RequestParam("image") MultipartFile image, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "Employees/AddEmployee"; // Eğer hata varsa, form tekrar gösterilir
        }

        // Görsel dosyasını kaydetme
        if (!image.isEmpty()) {
            String fileName =image.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR, fileName);
            // Eğer dizin yoksa oluşturulması sağlanır
            Files.createDirectories(path.getParent());
            Files.copy(image.getInputStream(), path);
            employee.setImageUrl(fileName); // Görsel URL'sini kaydediyoruz
        }

        // Çalışanı kaydetme
        employeeRepository.save(employee);
        return "redirect:/employees"; // Çalışan başarıyla eklendikten sonra listeye yönlendirilir
    }

    // Çalışan Güncelleme: Çalışan verilerini formda görüntüleme
    @GetMapping("/update/{id}")
    public String showUpdateEmployeeForm(@PathVariable("id") Long id, Model model) {
        Employees employee = employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        model.addAttribute("employee", employee);
        List<Roles> roles = roleRepository.findAll();
        model.addAttribute("roles", roles); // Rolleri view'ye göndermek
        return "Employees/UpdateEmployee"; // updateEmployee.html sayfası
    }

    // Çalışan Güncelleme: Formdan gelen verileri işleme
    // Çalışan Güncelleme: Formdan gelen verileri işleme
    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable("id") Long id,
                                 @Validated @ModelAttribute("employees") Employees employee,
                                 @RequestParam(value = "image", required = false) MultipartFile image,
                                 BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "Employees/UpdateEmployee"; // updateEmployee.html sayfası
        }

        // Mevcut çalışanı veritabanından alıyoruz
        Employees existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));

        // İlk olarak mevcut çalışanı formdaki yeni verilerle güncelliyoruz
        if (employee.getFirstName() != null && !employee.getFirstName().isEmpty()) {
            existingEmployee.setFirstName(employee.getFirstName());
        }
        if (employee.getLastName() != null && !employee.getLastName().isEmpty()) {
            existingEmployee.setLastName(employee.getLastName());
        }
        if (employee.getEmail() != null && !employee.getEmail().isEmpty()) {
            existingEmployee.setEmail(employee.getEmail());
        }
        if (employee.getSalary() != null) {
            existingEmployee.setSalary(employee.getSalary());
        }
        if (employee.getRole() != null) {
            existingEmployee.setRole(employee.getRole());
        }

        // Eğer yeni bir görsel seçilmişse, o zaman görseli kaydedelim
        if (!image.isEmpty()) {
            String fileName = image.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR, fileName);
            Files.createDirectories(path.getParent());
            // Dosya var ise üzerine yazma işlemi
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            existingEmployee.setImageUrl(fileName); // Yeni görsel URL'sini güncelliyoruz
        } else {
            // Eğer görsel seçilmediyse mevcut görseli koruyoruz
            existingEmployee.setImageUrl(existingEmployee.getImageUrl());
        }

        // Güncellenmiş çalışanı veritabanına kaydediyoruz
        employeeRepository.save(existingEmployee);

        return "redirect:/employees"; // Çalışan başarıyla güncellendikten sonra listeye yönlendiriyoruz
    }



    // Çalışan Silme: Çalışanı silme işlemi
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id) {
        Employees employee = employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        employeeRepository.delete(employee);
        return "redirect:/employees"; // Silme işlemi sonrasında listeye yönlendirilir
    }
}
