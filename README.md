# Automation Orchestrator

AI-assisted Spring Boot automation orchestration platform.

## Project Goals

- Build a domain-driven Spring Boot backend
- Design commerce workflow orchestration
- Practice transaction boundaries and aggregate responsibilities
- Integrate RPA execution flow
- Document AI-assisted development workflow

## Tech Stack

- Java
- Spring Boot
- JPA
- MyBatis
- MySQL
- Gradle
- Docker

## Core Domains

- Order
- Product
- Inventory
- Payment
- Task
- Execution

## AI-assisted Development Workflow

This project uses AI as a development assistant for:

- Architecture mentoring
- Code review
- Refactoring guidance
- Test case drafting
- Documentation support

## Architecture Principles

- Domain-driven design (DDD)
- Aggregate boundary separation
- Service orchestration
- Transactional consistency
- Transaction boundary management
- JPA for state management
- Preparing MyBatis integration for future query optimization

## Current Commerce Flow

```text
Product Creation
→ Inventory Initialization
→ Order Creation
→ Inventory Reservation
→ Payment Creation
→ Payment Success
→ Order Paid
→ Inventory Decrease