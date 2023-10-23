package com.tui.task.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Repository {
    private String name;
    private String ownerLogin;
    private List<Branch> branches;
}
