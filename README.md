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
- ExecutionLog

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
- Execution lifecycle separation
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
```

## Current MyBatis Queries

- Order Summary Query
- Low Stock Product Query

## Future Automation Flow

```text
Low Stock Product Query
→ Task Creation
→ Human Approval
→ Execution Creation
→ API / AI / Email / RPA Execution
```

## Automation Workflow

```text
Task Creation
→ Task Approval
→ Execution Creation (READY)
→ Execution Run API
→ ExecutionRunner
→ Executor Selection
→ Execution
→ SUCCESS / FAILED
→ Execution Log Recording
```

## Service Architecture

TaskService
→ Task lifecycle management

ExecutionService
→ Execution lifecycle management

OrchestratorService
→ Cross-service workflow orchestration

ExecutionRunner
→ Execution orchestration

## Execution Resolution

TaskType
→ TaskExecutionResolver
→ ExecutionType(s)

Examples:

- LOW_STOCK_REPORT → AI
- PURCHASE_REQUEST_REVIEW → EMAIL
- RPA_FAILURE_RECOVERY → RPA

## Business Automation Flow

```text
Low Stock Query
→ Task Creation
→ Human Approval
→ Execution Creation (READY)
→ Execution Runner
→ Execution
→ Execution Log
```

Human-in-the-Loop (HITL) is intentionally applied to business-critical tasks,
allowing operators to review and approve tasks before execution.

## Execution Architecture

```text
Execution (READY)
        ↓
ExecutionRunner
        ↓
ExecutionExecutorFactory
        ↓
ExecutionExecutor
        ↓
ApiExecutionExecutor
        ↓
ApiExecutionRequestResolver
        ↓
ApiExecutionRequestProvider
        ↓
RestApiExecutionClient
```

## Supported Execution Types

Currently implemented:

- API ✅

Planned:

- AI
- Email
- Slack
- RPA

## Execution Run API

```text
POST /api/executions/{executionId}/run
        ↓
ExecutionRunService
        ↓
ExecutionRunner
        ↓
ExecutionExecutorFactory
        ↓
ExecutionExecutor
        ↓
SUCCESS / FAILED
```