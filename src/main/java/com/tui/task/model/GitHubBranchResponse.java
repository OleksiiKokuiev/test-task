package com.tui.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GitHubBranchResponse {
    private String name;
    private GitHubCommitResponse commit;
}
