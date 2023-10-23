package com.tui.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitHubRepositoryResponse {
    private String name;
    private boolean fork;
    private GitHubUserResponse owner;
}
