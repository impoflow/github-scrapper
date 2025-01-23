# Continuous Integration (CI) Pipeline:
Trigger: Initiated on pushes to the master branch or manually via workflow_dispatch.

Jobs:
- Checkout Code: Retrieves the latest code from the repository.
- DockerHub Login: Authenticates to DockerHub using stored secrets.
- Build Docker Image: Constructs a Docker image of the GitHub Scraper Application.
- Create Data Lake: Sets up a local directory named datalake (potentially for local storage or testing).
- Run Docker Container: Deploys the application container with environment variables specifying AWS S3 bucket details.
- View Docker Logs: Outputs logs from the running container for monitoring purposes.
- Push Docker Image: Uploads the newly built Docker image to DockerHub.

```mermaid
graph TD
    subgraph CI_Pipeline
        A[Checkout Code] --> B[DockerHub Login]
        B --> C[Build Docker Image]
        C --> D[Create Data Lake]
        D --> E[Run Docker Container]
        E --> F[View Docker Logs]
        F --> G[Push Docker Image]
    end

    subgraph GitHub_Scraper_Application
        H[Scraper Logic] --> I[GitHub API]
        H --> J[S3 Upload]
    end

    E --> |Deploys| GitHub_Scraper_Application
    J --> |Uploads Data| S3[Amazon S3 Data Lake]
```
