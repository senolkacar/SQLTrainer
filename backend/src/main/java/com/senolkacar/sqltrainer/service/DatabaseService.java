package com.senolkacar.sqltrainer.service;

import com.senolkacar.sqltrainer.dto.DatabaseDTO;
import com.senolkacar.sqltrainer.entity.Database;
import com.senolkacar.sqltrainer.repository.DatabaseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseService {

    @Autowired
    private DatabaseRepository databaseRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<DatabaseDTO> getAllDatabases() {
        List<Database> databases = databaseRepository.findAll();
        return databases.stream()
                .map(db -> modelMapper.map(db, DatabaseDTO.class))
                .collect(Collectors.toList());
    }

    public List<String> getTables(String name) {
        Database database = databaseRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Database not found: " + name));
        return database.getTables(name);
    }

    public List<String> getColumns(String name) {
        Database database = databaseRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Database not found: " + name));
        return database.getColumns(name);
    }
}
