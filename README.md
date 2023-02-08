# User API service

A basic CRUD-like service demonstrating coding skills.

To run tests make sure Docker is installed then execute:
```
./gradlew test
```
To run application locally make sure Docker is installed then execute:
```
docker-compose up
```
then execute
```
./gradlew bootRun
```

![example workflow](https://github.com/alex-vo/userapi/workflows/build/badge.svg)
[![Maintainability](https://api.codeclimate.com/v1/badges/8f86a1596646789f275d/maintainability)](https://codeclimate.com/github/alex-vo/userapi/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/8f86a1596646789f275d/test_coverage)](https://codeclimate.com/github/alex-vo/userapi/test_coverage)

---

# User API Challenge: Kotlin
We have a fictitious scenario where we'd like to build an API to manage Zip Pay users. We require the ability to create, get, and list users. Once we create users, we need the ability for the user to create an account.

This should be implemented with an API and database.

## Business Requirements

* Should not allow more than one user with the same email address
* Zip Pay allows credit for up to $1000, so if `monthly salary - monthly expenses` is less than $1000 we should not create an account for the user and return an error

## API Requirements

1. The following Endpoints are required:
  * Create user
    * Fields to be provided and persist
      * Name
      * Email Address (Must be unique)
      * Monthly Salary (Must be a positive number - for simplicity, pretend there is no such thing as income tax)
      * Monthly Expenses (Must be a positive number)
  * List users
  * Get user
  * Create an account
    * Given a user, create an account
    * Should take into account the business requirements above
    * Up to you to decide the appropriate fields to persist here
  * List Accounts
