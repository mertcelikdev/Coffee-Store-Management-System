package com.ozcelikkahve.ozcelikcoffee.Services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    // Kahve, Tatlı ve Employee görselleri için üç farklı dizin tanımlıyoruz
    private final Path coffeeRoot = Paths.get("src/main/resources/static/images/coffees");
    private final Path dessertRoot = Paths.get("src/main/resources/static/images/desserts");
    private final Path employeeRoot = Paths.get("src/main/resources/static/images/employees");  // Yeni dizin ekleniyor

    @Override
    public void init() {
        try {
            // Her üç dizini de oluşturuyoruz
            Files.createDirectories(coffeeRoot);
            Files.createDirectories(dessertRoot);
            Files.createDirectories(employeeRoot);  // Employee dizini oluşturuluyor
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file, String type) {
        try {
            Path targetLocation = getPath(file, type);  // Hedef dizini getirmek için yardımcı metodu kullanıyoruz
            Files.copy(file.getInputStream(), targetLocation);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename, String type) {
        try {
            Path file = getPath(filename, type);  // Dosya yolunu getirmek için yardımcı metodu kullanıyoruz
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(coffeeRoot.toFile());
        FileSystemUtils.deleteRecursively(dessertRoot.toFile());
        FileSystemUtils.deleteRecursively(employeeRoot.toFile());  // Employee dizini de siliniyor
    }

    @Override
    public Stream<Path> loadAll(String type) {
        try {
            Path rootPath = getRootPath(type);  // İlgili dizini almak için yardımcı metodu kullanıyoruz
            return Files.walk(rootPath, 1)
                    .filter(path -> !path.equals(rootPath))
                    .map(rootPath::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }



    // Hedef dizini belirlemek için yardımcı metod
    private Path getPath(MultipartFile file, String type) {
        Path targetLocation;

        if ("coffee".equalsIgnoreCase(type)) {
            targetLocation = coffeeRoot.resolve(file.getOriginalFilename());
        } else if ("dessert".equalsIgnoreCase(type)) {
            targetLocation = dessertRoot.resolve(file.getOriginalFilename());
        } else if ("employee".equalsIgnoreCase(type)) {
            targetLocation = dessertRoot.resolve(file.getOriginalFilename());
        } else {
            throw new RuntimeException("Unknown file type");
        }

        return targetLocation;
    }

    // Hedef dizini belirlemek için yardımcı metod (filename ile)
    private Path getPath(String filename, String type) {
        Path file;

        if ("coffee".equalsIgnoreCase(type)) {
            file = coffeeRoot.resolve(filename);
        } else if ("dessert".equalsIgnoreCase(type)) {
            file = dessertRoot.resolve(filename);
        }
        else if ("employee".equalsIgnoreCase(type)) {
            file = dessertRoot.resolve(filename);
        }else {
            throw new RuntimeException("Unknown file type");
        }

        return file;
    }

    // Yükleme türüne göre doğru kök dizini belirlemek için yardımcı metod
    private Path getRootPath(String type) {
        if ("coffee".equalsIgnoreCase(type)) {
            return coffeeRoot;
        } else if ("dessert".equalsIgnoreCase(type)) {
            return dessertRoot;
        } else if ("employee".equalsIgnoreCase(type)) {  // Employee tipi için root path
            return employeeRoot;
        } else {
            throw new RuntimeException("Unknown file type");
        }
    }


}
