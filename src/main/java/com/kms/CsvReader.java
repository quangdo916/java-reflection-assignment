package com.kms;

import com.annotations.Column;
import com.annotations.NotNull;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvReader {

  private static final Logger logger = LoggerFactory.getLogger(App.class);

  // Read CSV and return Java objects
  public List<UserRow> readCsv(String filePath) throws Exception {
    List<UserRow> userList = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String headerLine = br.readLine();
      if (headerLine == null) {
        throw new IllegalArgumentException("CSV file is empty");
      }
      String[] headers = headerLine.split(",");
      String line;
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        UserRow user = new UserRow();
        for (Field field : UserRow.class.getDeclaredFields()) {
          if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            String columnName = column.value();
            for (int i = 0; i < headers.length; i++) {
              if (headers[i].trim().equalsIgnoreCase(columnName.trim())) {
                field.setAccessible(true);
                Object value = convert(field.getType(), values[i]);
                field.set(user, value);
                break;
              }
            }
          }
        }
        validate(user);
        userList.add(user);
      }
    }
    return userList;
  }

  public Object convert(Class<?> type, String value) {
    if (type == int.class || type == Integer.class) {
      return Integer.parseInt(value);
    } else if (type == String.class) {
      return value;
    } else {
      throw new IllegalArgumentException("Unsupported field type: " + type);
    }
  }

  public void validate(UserRow user) throws IllegalAccessException {
    for (Field field : UserRow.class.getDeclaredFields()) {
      if (field.isAnnotationPresent(NotNull.class)) {
        field.setAccessible(true);
        Object value = field.get(user);
        if (value == null || value.toString().equalsIgnoreCase("NULL")) {
          logger.error("Field '{}' of user {} cannot be null or empty", field.getName(), user);
        }
      }
    }
  }
}
