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

  private static final Logger logger = LoggerFactory.getLogger(CsvReader.class);

  public List<UserRow> readCsv(String filePath) throws Exception {
    List<UserRow> userList = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String headerLine = br.readLine();
      if (headerLine == null) {
        throw new IllegalArgumentException("CSV file '" + filePath + "' is empty");
      }

      String[] headers = headerLine.split(",");
      String line;
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        if (values.length != headers.length) {
          logger.warn("Skipping malformed line: '{}'", line);
          continue;
        }

        UserRow user = new UserRow();
        mapValuesToUser(headers, values, user);
        validate(user);
        userList.add(user);
      }
    }
    return userList;
  }

  private void mapValuesToUser(String[] headers, String[] values, UserRow user) throws IllegalAccessException {
    for (Field field : UserRow.class.getDeclaredFields()) {
      if (field.isAnnotationPresent(Column.class)) {
        Column column = field.getAnnotation(Column.class);
        String columnName = column.value().trim();
        for (int i = 0; i < headers.length; i++) {
          if (headers[i].trim().equalsIgnoreCase(columnName)) {
            field.setAccessible(true);
            Object value = convert(field.getType(), values[i]);
            field.set(user, value);
            break;
          }
        }
      }
    }
  }

  public Object convert(Class<?> type, String value) {
    try {
      if (type == int.class || type == Integer.class) {
        return Integer.parseInt(value);
      } else if (type == String.class) {
        return value;
      } else {
        throw new IllegalArgumentException("Unsupported field type: " + type);
      }
    } catch (NumberFormatException e) {
      logger.error("Failed to convert value '{}'", value, e);
      throw e;
    }
  }

  public void validate(UserRow user) throws IllegalAccessException {
    for (Field field : UserRow.class.getDeclaredFields()) {
      if (field.isAnnotationPresent(NotNull.class)) {
        field.setAccessible(true);
        Object value = field.get(user);
        if (value == null || value.toString().trim().isEmpty()) {
          logger.error("Field '{}' of user {} cannot be null or empty", field.getName(), user);
        }
      }
    }
  }
}
