To DO Tasks

A brief description of your project, what it does, and its purpose.

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Features](#features)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)

## Installation

Instructions for installing the project dependencies and setting up the application. For example:
```bash
git clone https://github.com/Thagika/todo-tasks.git
cd todo-app
docker-compose up --build


if the build has been done before 
docker compose up -d 

docker compose up -d mysql #to only start the mysql server 
docker compose up -d mysql

access the backend and frontend using the following 
Access the backend at http://localhost:8080
Access the frontend at http://localhost:3000


Features:-
lets you add new tasks and on the right side of the application you are able to see the most recent 5 tasks


endpoints tested via Postman

GET /api/tasks: Retrieve all tasks
POST /api/tasks: Create a new task
PUT /api/tasks/{id}: Update an existing task
DELETE /api/tasks/{id}: Delete a task

