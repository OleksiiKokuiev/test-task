package com.tui.task.service;

import com.tui.task.exception.GitHubNotFoundException;
import com.tui.task.model.Branch;
import com.tui.task.model.GitHubBranchResponse;
import com.tui.task.model.GitHubRepositoryResponse;
import com.tui.task.model.Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class GitHubService {
    private final WebClient webClient;

    @Value("${validation.messages.userNotFound}")
    String userNotFound;

    @Value("${github.baseurl}")
    String baseurl;

    @Value("${github.getReposUri}")
    String getReposUri;

    @Value("${github.getBranchesUri}")
    String getBranchesUri;

    public GitHubService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseurl).build();
    }

    public Flux<Repository> getGitHubRepositories(String username) {
        return webClient.get()
                .uri(UriComponentsBuilder.fromHttpUrl(baseurl)
                        .path(getReposUri)
                        .buildAndExpand(username)
                        .toUriString())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        clientResponse -> Mono.error(new GitHubNotFoundException(userNotFound)))
                .bodyToFlux(GitHubRepositoryResponse.class)
                .filter(repo -> !repo.isFork())
                .flatMap(repo -> getBranchesForRepository(repo)
                        .map(branches -> Repository.builder()
                                .name(repo.getName())
                                .ownerLogin(repo.getOwner().getLogin())
                                .branches(branches)
                                .build()));
    }

    private Mono<List<Branch>> getBranchesForRepository(GitHubRepositoryResponse repo) {
        return webClient.get()
                .uri(UriComponentsBuilder.fromHttpUrl(baseurl)
                        .path(getBranchesUri)
                        .buildAndExpand(repo.getOwner().getLogin(), repo.getName())
                        .toUriString())
                .retrieve()
                .bodyToFlux(GitHubBranchResponse.class)
                .map(branchResponse -> Branch.builder()
                        .name(branchResponse.getName())
                        .lastCommitSha(branchResponse.getCommit().getSha())
                        .build())
                .collectList();
    }
}

