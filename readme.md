# Campus Lost & Found

A web application that helps campus users report lost and found belongings and manage claims for items that may belong to them.

## Features

- **User Authentication:** Register and log in to access the application.
- **Report Items:** Create lost-item and found-item reports with a title, description, location, date, and optional image URL.
- **Browse Reports:** View reported items and search or filter reports.
- **Submit Claims:** Submit a claim with a message for a found item.
- **Manage Claims:** View incoming claims on your found-item reports and approve or reject them.
- **Item Status Tracking:** An item is marked as claimed when its claim is approved.

## Technology Stack

- **Backend:** Java 21, Spring Boot
- **Database:** PostgreSQL
- **Frontend:** HTML, CSS, JavaScript
- **Build Tool:** Maven

## Project Structure

- `backend/` — Spring Boot application and backend logic
- `frontend/` — User interface and JavaScript files

## Getting Started

### Prerequisites

- Java 21
- Maven
- PostgreSQL
- A web browser
- An IDE such as IntelliJ IDEA

### Setup

1. Clone or download this repository.
2. Create the PostgreSQL database and configure the backend database connection.
3. Configure the required application settings in the backend.
4. Run the Spring Boot application.
5. Open the frontend's `index.html` using a local development server.
6. Register an account and start using the application.

## How It Works

1. A user registers and logs in.
2. The user creates a lost-item or found-item report.
3. Other users browse the reports and search for relevant items.
4. A user can submit a claim for a found item with a message.
5. The person who posted the found item reviews incoming claims and approves or rejects them.
6. When a claim is approved, the item's status changes to `CLAIMED`.

## Future Improvements

- Image upload support
- Notifications for claim updates
- Additional user interface and accessibility improvements

## Author

Developed as a college project.
