# Request Management System

##  Project Overview
This **Spring Boot** application allows users to **sign up and log in**, submit requests with attachments (**images/PDFs**), and receive responses from higher-level roles. The system supports **role-based access** with an Admin overseeing requests and managing user roles.

## Tech Stack
- **Spring Boot** – Core backend framework
- **Spring Security** – User authentication & authorization
- **Spring JPA** – Database management
- **MySQL** – Relational database
- **Thymeleaf** – Templating engine for UI
- **Bootstrap** – Styling & responsive UI

## Features
- **User Authentication**: Signup & Login
- **Request Submission**: Users can submit requests with **dropdown subjects** and attach **images/PDFs**.
- **Role Management**:
  - **User**: Can create, edit, and delete requests (before receiving a response).
  - **Higher Role** (Reviewer): Can **review and respond** to requests.
  - **Admin**: Can **manage user roles**, **review all requests and responses**, and **oversee the system**.
- **Access Control**: Once a request has been responded to, the user **cannot edit or delete** it.
