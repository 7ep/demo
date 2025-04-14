# ENSF 400 - Winter 2025 - Course Project

## Project Overview

In this project, you will work based on a software project by incorporating/extending a complete CI/CD (Continuous Integration/Continuous Deployment) pipeline. This is based on an open-source sample application: https://github.com/7ep/demo

This project can also be any application that requires the project of build, test, and deployment.
You will leverage GitHub for source control, Docker for containerizing your application, and a CI/CD tool (Jenkins) to automate the build, testing, and verification process. The goal is to validate every code change automatically through container builds, unit tests, code quality checks, and end-to-end functional tests.  thank you


## Project Requirements

By the end of this project, your group must deliver the following:

1.	Manage your project on GitHub and follow proper Git workflows (branching, pull requests, code reviews). Document the process of how you use Git workflows to collaborate with your team members.

1.	Containerize your application for builds and deployments. Upload and download your container images to a public or private image repository (e.g., Docker Hub or GitHub Container Registry). Ensure a container image is built with unique build tag(s) matching the triggering commit from any branch.

1.	Set up an automated CI/CD with Jenkins in a Codespace environment. Configure the pipeline to trigger upon pull requests merging changes into the main branch.

1.	Document the CI/CD process and provide clear instructions on replicating your environment. Submit a video demo at the end of the project. Thanks"

### Existing Pipelines

You will also demonstrate the delivery of the following process and artifacts that come with the project.

1.	Run static analysis quality-gating using SonarQube
1.	Performance testing with Jmeter
1.	Security analysis with OWASP's "DependencyCheck"
1.	Build Javadocs
___________________________________________________________________________________________________________________________________________________

# Organization of our ENSF 400 CI/CD Project  

## Team Members  

- Josral Frederick UCID: 30195360
- Muhammad Aun Raza My UCID: 30172183
- Natnael Tekli UCID: 30171167
- Jaden Chow UCID: 30173676

## Project Description  
The main objective in this project is to create software that incorporates/extends a complete CI/CD 
(Continuous Integration/Continuous Deployment) pipeline. 

## Git Workflow  
Our team follows a structured Git workflow to manage our project efficiently on GitHub.

We begin by cloning the repository using git clone <repository_url>. Before making changes, we create a new feature branch with git checkout -b feature/feature-name to keep our work organized.

Once changes are made, we stage and commit them using git add . and git commit -m "Description of changes", ensuring clear commit messages.

To keep our branch up-to-date, we sync it with the main branch by switching to main, pulling the latest changes (git pull origin main), and merging them into our feature branch (git merge main). If merge conflicts arise, we resolve them before proceeding.

After finalizing our changes, we push our branch to the remote repository using git push origin feature/feature-name. We then open a Pull Request (PR) on GitHub, providing a description of our modifications.

Team members participate in a code review process, leaving feedback and suggesting necessary improvements. Once approved, the PR is merged into main, typically by a team lead or someone with write access.

After merging, we delete the feature branch both locally (git branch -d feature/feature-name) and remotely (git push origin --delete feature/feature-name). To stay updated, we sync our local repository with the latest changes using git pull origin main.

We follow GitFlow best practices, using descriptive branch names (e.g., feature/login-page, bugfix/error-handling) and ensuring every change is reviewed via a PR before merging into main.
External contributors fork the repository, create a new branch, make their changes, and open a PR to contribute to the project.

This workflow ensures a structured and collaborative development process, keeping our codebase clean and organized.

## Containerization  

To containerize our application, we use Docker and Docker Hub. After creating and reviewing the Dockerfile as a team, we build the Docker image with a unique tag matching the commit hash or branch name. This ensures traceability by linking each image to the exact code version it was built from. We generate the commit hash using COMMIT_HASH=$(git rev-parse --short HEAD), build the image with docker build -t natnaelt2/ensf400-demo:$COMMIT_HASH ., and push it to Docker Hub using docker push natnaelt2/ensf400-demo:$COMMIT_HASH. This process guarantees consistency and simplifies tracking across deployments.

To deploy the application, we pull the Docker image from Docker Hub using the command docker pull natnaelt2/ensf400-demo:commit-hash. Finally, we run the Docker container using the command docker run -p 8080:8080 natnaelt2/ensf400-demo:commit-hash. Then visit the application with your browser at http://localhost:8080/demo.

## Jenkins

we are testing the jenkins



