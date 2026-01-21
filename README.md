# Vehicle Rental System

A modular vehicle rental management system developed in Java, based on a microkernel architecture with dynamically loaded plug-ins.

## Requirements

- Java JDK 11 or higher
- Maven 3.8+

> JavaFX is used for the graphical interface and is handled automatically by Maven dependencies.

## Project Structure

This project is organized as a multi-module Maven application:

- `kernel` - Defines the core contracts and central control of the system.
- `app` - Initializes the application and provides the user interface.
- `plugins` - Independent modules that extend the system functionality, loaded dynamically by the microkernel.

## Build

From the project root directory:

```bash
mvn clean install
```

## Run

After a successful build:

```bash
cd app
mvn exec:java -Dexec.mainClass="br.edu.ifba.inf008.App"
```

A JavaFX window will open, serving as the entry point of the application.

## Architecture Overview

The system follows a microkernel-based architecture, where:

- the core system remains minimal and stable
- functionality is extended through plug-ins
- new features can be added without modifying the kernel or the main application

This approach emphasizes modularity, extensibility, and separation of concerns.

## Version Control Workflow

The repository follows a lightweight Git Flow approach:

- `main` - Stable branch, always buildable.
- `dev` - Integration branch for ongoing development.
- `feature/*` - Feature-specific branches used during implementation.

## Notes

This project was developed for academic purposes, focusing on object-oriented design, modular architecture, and dynamic behavior through plug-ins.
