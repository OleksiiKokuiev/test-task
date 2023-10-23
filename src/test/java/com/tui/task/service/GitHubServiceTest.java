package com.tui.task.service;

import com.tui.task.exception.GitHubNotFoundException;
import com.tui.task.model.GitHubRepositoryResponse;
import com.tui.task.model.GitHubUserResponse;
import com.tui.task.model.Repository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

public class GitHubServiceTest {

    @InjectMocks
    private GitHubService gitHubService;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec uriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    private WebClient.Builder webClientBuilder;

    @Before
    public void init() {

        webClientBuilder = WebClient.builder();
        webClient = webClientBuilder.baseUrl("https://example.com").build();
        gitHubService = new GitHubService(webClientBuilder);
    }

    @Test
    public void testGetGitHubRepositories() {
        // Given
        String username = "testuser";
        GitHubRepositoryResponse repo1 = new GitHubRepositoryResponse("repo1", false, new GitHubUserResponse("user1"));
        GitHubRepositoryResponse repo2 = new GitHubRepositoryResponse("repo2", false, new GitHubUserResponse("user1"));
        List<GitHubRepositoryResponse> repositories = List.of(repo1, repo2);

        // When
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(GitHubRepositoryResponse.class)).thenReturn(Flux.fromIterable(repositories));

        // Act
        Flux<Repository> result = gitHubService.getGitHubRepositories(username);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(repo -> "repo1".equals(repo.getName()) && "user1".equals(repo.getOwnerLogin()))
                .expectNextMatches(repo -> "repo2".equals(repo.getName()) && "user1".equals(repo.getOwnerLogin()))
                .verifyComplete();
    }

    @Test
    public void testGetGitHubRepositories_UserNotFound() {
        // Given
        String username = "nonexistentuser";

        // When
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(GitHubRepositoryResponse.class)).thenReturn(Flux.empty());

        // Then
        StepVerifier.create(gitHubService.getGitHubRepositories(username))
                .expectError(GitHubNotFoundException.class)
                .verify();
    }
}
