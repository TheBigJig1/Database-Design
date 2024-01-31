package model;

import java.util.List;

// Implement a custom version of the toString method

public record Row(String key, List<Object> fields) {

}