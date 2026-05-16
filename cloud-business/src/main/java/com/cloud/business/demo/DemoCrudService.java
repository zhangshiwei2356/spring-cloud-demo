package com.cloud.business.demo;

import com.cloud.common.exception.GlobalException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class DemoCrudService<T extends Identifiable> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final JsonFileRepository<T> repository;
    private final TypeReference<List<T>> typeRef;
    private final String seedClasspath;
    private final Consumer<T> onCreate;

    public DemoCrudService(Path dataFile,
                           TypeReference<List<T>> typeRef,
                           String seedClasspath,
                           Consumer<T> onCreate) throws IOException {
        this.typeRef = typeRef;
        this.repository = new JsonFileRepository<>(dataFile, typeRef);
        this.seedClasspath = seedClasspath;
        this.onCreate = onCreate != null ? onCreate : (r) -> { };
        initSeed();
    }

    private void initSeed() throws IOException {
        ClassPathResource resource = new ClassPathResource(seedClasspath);
        if (!resource.exists()) {
            return;
        }
        try (InputStream in = resource.getInputStream()) {
            repository.initFromSeedIfEmpty(in);
        }
    }

    public List<T> list() {
        return run(repository::findAll).stream()
                .sorted(Comparator.comparing(Identifiable::getId).reversed())
                .toList();
    }

    public T get(Long id) {
        return run(() -> repository.findById(id)
                .orElseThrow(() -> new GlobalException("记录不存在")));
    }

    public T create(T record) {
        return run(() -> {
            onCreate.accept(record);
            return repository.insert(record);
        });
    }

    public T update(Long id, T record) {
        return run(() -> {
            if (repository.findById(id).isEmpty()) {
                throw new GlobalException("记录不存在");
            }
            record.setId(id);
            T updated = repository.update(record);
            if (updated == null) {
                throw new GlobalException("更新失败");
            }
            return updated;
        });
    }

    public void delete(Long id) {
        run(() -> {
            if (!repository.deleteById(id)) {
                throw new GlobalException("记录不存在");
            }
            return null;
        });
    }

    public List<T> reset() {
        return run(() -> {
            ClassPathResource resource = new ClassPathResource(seedClasspath);
            if (!resource.exists()) {
                repository.replaceAll(List.of());
                return List.of();
            }
            try (InputStream in = resource.getInputStream()) {
                List<T> seed = MAPPER.readValue(in, typeRef);
                for (int i = 0; i < seed.size(); i++) {
                    T item = seed.get(i);
                    if (item.getId() == null) {
                        item.setId((long) (i + 1));
                    }
                }
                repository.replaceAll(seed);
                return seed;
            }
        });
    }

    private <R> R run(IoSupplier<R> supplier) {
        try {
            return supplier.get();
        } catch (GlobalException ex) {
            throw ex;
        } catch (IOException ex) {
            throw new GlobalException("读写演示数据失败：" + ex.getMessage());
        }
    }

    protected static String now() {
        return LocalDateTime.now().format(DT);
    }

    public static boolean isBlank(String s) {
        return !StringUtils.hasText(s);
    }

    @FunctionalInterface
    private interface IoSupplier<R> {
        R get() throws IOException;
    }
}
