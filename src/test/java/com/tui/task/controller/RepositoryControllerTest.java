package com.tui.task.controller;

import com.tui.task.utils.AcceptType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import com.tui.task.exception.GitHubNotFoundException;
import com.tui.task.model.Repository;
import com.tui.task.service.GitHubService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class RepositoryControllerTest {

    @Mock
    private GitHubService gitHubService;

    private RepositoryController controller;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        controller = new RepositoryController(gitHubService);
    }

    @Test
    public void testGetRepositoriesReturnsRepositories() {
        // Given
        String username = "exampleUser";
        Repository repo1 = Repository.builder()
                .name("repo1")
                .ownerLogin("user1")
                .build();
        Repository repo2 = Repository.builder()
                .name("repo2")
                .ownerLogin("user1")
                .build();
        List<Repository> repositoryList = Arrays.asList(repo1, repo2);

        Flux<Repository> repositories = Flux.fromIterable(repositoryList);

        // When
        when(gitHubService.getGitHubRepositories(username)).thenReturn(repositories);

        ResponseEntity<?> response = controller.getRepositories(username, AcceptType.JSON.getValue());

        // Then
        verify(gitHubService).getGitHubRepositories(username);
        verifyNoMoreInteractions(gitHubService);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Repository> responseRepositories = (List<Repository>) response.getBody();
        Assertions.assertIterableEquals(repositoryList, responseRepositories);
    }

    @Test
    public void testGetRepositoriesWithNonExistentUser() {
        // Given
        String username = "nonExistentUser";
        String errorMessage = "User not found";

        when(gitHubService.getGitHubRepositories(username)).thenThrow(new GitHubNotFoundException(errorMessage));

        // When
        ResponseEntity<?> response = controller.getRepositories(username, AcceptType.JSON.getValue());

        // Then
        verify(gitHubService).getGitHubRepositories(username);
        verifyNoMoreInteractions(gitHubService);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        String responseMessage = (String) response.getBody();
        Assertions.assertEquals(errorMessage, responseMessage);
    }

    @Test
    public void testGetRepositoriesWithUnsupportedAcceptHeader() {
        // Given
        String username = "exampleUser";
        String unsupportedAcceptHeader = "application/xml";

        // When
        ResponseEntity<?> response = controller.getRepositories(username, unsupportedAcceptHeader);

        // Then
        Assertions.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());

        Assertions.assertNull(response.getBody());
    }
}

