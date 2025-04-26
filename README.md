# User-Service

A Spring Boot microservice for user management that handles registration, email verification, password reset, password changes, and token expiration.  
Built with PostgreSQL, Spring Security, Java Mail Sender, and Dockerized for production-ready deployment.


## Project Structure
## User-Service

![user-servise](https://github.com/user-attachments/assets/a0cc1a7e-e89e-4ebe-a4a0-861673402af7)

## Features

- User Registration with email verification
- Resend Verification Token for unverified users
- Password Reset using secure tokens
- Token Auto-Expiration (5 minutes for reset/verification tokens)
- Spring Security integration (`/user/**` endpoints publicly accessible)
- Java Mail integration for sending emails
- Password Encoding for secure user credentials
- Docker support (multi-stage build & run)

## Technology Stack

- Spring Boot
- Spring Security
- Spring Data JPA
- Java Mail Sender
- PostgreSQL
- Docker & Docker Compose
- Postman for API testing

## Requirements

- Java 17 or higher
- Maven
- Docker and Docker Compose installed
- PostgreSQL database running


## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | /user/register            | Register a new user |
| GET    | /user/verifyRegistration   | Verify user via token |
| POST   | /user/resendVerifyToken    | Resend verification token |
| POST   | /user/resetPassword        | Initiate password reset |
| POST   | /user/savePassword         | Save new password |
| POST   | /user/changePassword       | Change current password |
| GET/PUT/DELETE | /user/{id}          | Retrieve, update, or delete a user |


## Outputs
## Register New User
![Register User](https://github.com/user-attachments/assets/0bddb512-6407-4994-8c1e-528b60ad7b93)

## Verify User via Mail
![Verify User](https://github.com/user-attachments/assets/57748f62-f3d2-4a6f-82b0-42cce8b83c9c)

## Verify Success 
![Verify Success](https://github.com/user-attachments/assets/39dc953b-ff71-4117-805a-4f2f80be0213)

## Status = True after Verify , Initial = False
![User Verify then status change from false to true](https://github.com/user-attachments/assets/e891c0eb-7f1b-47ed-8cc6-30471e83fada)

## Change Password With Validation
![Change Password with validation check](https://github.com/user-attachments/assets/4e03530b-144c-4fb8-848d-d9b4129d74e7)

## Password Change Successfully
![Password Change Successfully](https://github.com/user-attachments/assets/b5ed6c7f-5d8e-4af3-9a0a-d3e023cea2c8)

## Password Reset 
![User Password Reset](https://github.com/user-attachments/assets/327d94e9-c573-4bac-88d3-cfe74be664c1)

## Reset Token via Mail
![Reset Token in mail](https://github.com/user-attachments/assets/aa6767a7-0224-4f3c-a2ce-fbb8a3658072)

## Password Save 
![Password Reset Successfully](https://github.com/user-attachments/assets/d3748bdd-fd56-4ac3-8d05-5811cbc1f04c)


## How to Run (Dockerized)

### 1. Build the Docker image

Inside your project root folder, run:

```bash
docker build -t repository/user-service:0.0.1 .
