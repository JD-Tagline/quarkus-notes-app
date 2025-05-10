package com.quarkus.notes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExternalPost {

    private int userId;
    private int id;
    private String title;
    private String body;
}
