# GitHub Repository List API

This is a simple Spring Boot application that provides an API for listing GitHub repositories of a specific user. It uses the GitHub REST API to retrieve repository information.

## Getting Started

These instructions will help you set up and run the application on your local machine.

### Prerequisites

- [Java JDK 11](https://adoptopenjdk.net/) or higher
- [Maven](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/downloads) (Optional)

### Installing

1. Clone the repository (if you didn't download the ZIP):

   ```shell
   git clone {link_to_repository}
2. Change into the project directory:

    ```shell
    cd github-repository-list-api
3. Build the project using Maven:

    ````shell
   mvn clean install
4. Run the application:

    ````shell
   java -jar target/github-repository-list-api-1.0.0.jar

The application should now be running and accessible at http://localhost:8080. (by default)

## Running the Application in a Docker Container

To run your application in a Docker container, follow these steps:

1. **Build the Docker Image**:

   Navigate to the root folder of your project, where the Dockerfile is located, and execute the following command to build a Docker image:

    ```shell
    docker build -t github-repository-list-api:latest
   
2. **Run the container**:
    ````shell
    docker run -p 8080:8080 github-repository-list-api

## Setting Up a Jenkins Pipeline for AWS Deployment

To automate the build and deployment process of your application using Jenkins, you can create a Jenkins pipeline. This pipeline will build your app and deploy it to AWS using scripts.

### Prerequisites

1. **Jenkins Installation**: Make sure you have Jenkins installed and configured. If you don't have Jenkins installed, you can follow the official [Jenkins Installation Guide](https://www.jenkins.io/doc/book/installing/).

2. **AWS Account**: Ensure that you have an AWS account. If you don't have one, you can create a free-tier AWS account at [AWS Free Tier](https://aws.amazon.com/free/).

### Steps to Create a Jenkins Pipeline

1. **Set Up Jenkins Job**:
    - In Jenkins, create a new job for your project.
    - Configure the job to pull the source code from your repository (e.g., GitHub).
    - Use Jenkinsfile (pipeline script) to define the build and deployment process. Here's an example of a basic Jenkinsfile:

      ```groovy
      pipeline {
          agent any
          stages {
              stage('Build') {
                  steps {
                      // Build your application (e.g., using Maven)
                  }
              }
              stage('Deploy to AWS') {
                  steps {
                      // Deploy your application to AWS (e.g., using AWS CLI or AWS Elastic Beanstalk)
                  }
              }
          }
      }
      ```

2. **AWS CLI Configuration**:
    - Install the AWS Command Line Interface (CLI) on your Jenkins server.
    - Configure AWS CLI with the necessary credentials and region. You can use the `aws configure` command to set up your credentials.

3. **Integration with AWS**:
    - Depending on your AWS deployment method (e.g., EC2, Elastic Beanstalk, Lambda, etc.), integrate your Jenkins pipeline with the corresponding AWS service.

4. **AWS Free Tier Considerations**:
    - Ensure that you stay within the AWS Free Tier limits to avoid unexpected charges.

5. **Pipeline Execution**:
    - Run the Jenkins job to execute the pipeline.
    - Jenkins will build your application and deploy it to AWS as defined in your Jenkinsfile.

### Testing the Jenkins Pipeline

After setting up the Jenkins pipeline, you can trigger it manually or configure it to run automatically when changes are pushed to your repository.

> Note: Jenkins and AWS setup may vary depending on your specific application, deployment method, and AWS services used. Be sure to follow best practices and security guidelines for AWS deployments.

Now you have an automated Jenkins pipeline that builds and deploys your application to AWS. You can customize the pipeline script and AWS deployment according to your project requirements.


## API Usage

### List GitHub Repositories

- **URL:** `/repositories`
- **Method:** `GET`
- **Parameters:**
    - `username` (required) - GitHub username for which you want to list repositories.
    - `Accept` header (required) - Use `application/json` for JSON response or `application/xml` for XML response.

### Response

- **JSON Format:**

  ```json
  [
    {
      "repositoryName": "repo1",
      "ownerLogin": "owner1",
      "branches": [
        {
          "name": "main",
          "lastCommitSha": "abcd1234"
        },
        ...
      ]
    },
    ...
  ]

## Error Handling

- If the user is not found, the API returns a 404 response in JSON format:

  ```json
  {
    "status": 404,
    "message": "User not found"
  }

- If Accept header is "application/xml" provided, the API returns a 406 response in JSON format:

  ````json
  {
  "status": 406,
  "message": "XML format not supported"
  }
- If an unsupported Accept header is provided, the API returns a 406 response in JSON format:

  ````json
  {
  "status": 406,
  "message": "Unsupported 'Accept' header"
  }

