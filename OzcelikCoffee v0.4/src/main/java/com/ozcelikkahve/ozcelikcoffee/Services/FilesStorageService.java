package com.ozcelikkahve.ozcelikcoffee.Services;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {

    // İlk dizinlerin oluşturulmasını sağlar
    void init();

    // Dosya yükleme işlemi, 'type' parametresiyle hangi türde olduğunu belirler
    void save(MultipartFile file, String type);

    // Dosya yükleme işlemi, 'type' parametresiyle hangi türde olduğunu belirler
    Resource load(String filename, String type);

    // Tüm dosyaları silme işlemi
    void deleteAll();

    // Tüm dosyaları listeleme işlemi, 'type' parametresiyle hangi türde olduğunu belirler
    Stream<Path> loadAll(String type);
}
