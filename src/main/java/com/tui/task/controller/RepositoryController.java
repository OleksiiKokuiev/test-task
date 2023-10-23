package com.tui.task.controller;

import com.tui.task.exception.GitHubNotFoundException;
import com.tui.task.model.Repository;
import com.tui.task.service.GitHubService;
import com.tui.task.utils.AcceptType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class RepositoryController {

    @Value("${validation.messages.xmlNotSupported}")
    String xmlNotSupported;

    @Value("${validation.messages.unsupportedAccept}")
    String unsupportedAccept;

    private final GitHubService gitHubService;

    public RepositoryController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/repositories")
    public ResponseEntity<?> getRepositories(@RequestParam String username,
                                             @RequestHeader(HttpHeaders.ACCEPT) String acceptHeader) {
        try {
            Flux<Repository> repositories = gitHubService.getGitHubRepositories(username);

            if (acceptHeader.equals(AcceptType.JSON.getValue())) {
                return ResponseEntity.ok().body(repositories.collectList().block());
            } else if (acceptHeader.equals(AcceptType.XML.getValue())) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(xmlNotSupported);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(unsupportedAccept);
            }
        } catch (GitHubNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }
}
