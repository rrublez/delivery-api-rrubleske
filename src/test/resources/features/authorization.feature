# language: en
Feature: Authorization and Authentication
  As a delivery API user
  I want proper authentication and authorization
  So that resources are protected according to user roles

  Scenario: Register a new client user
    Given no user with email "newclient@test.com"
    When I register user "New Client" with email "newclient@test.com" and role "CLIENTE"
    Then response status should be 201

  Scenario: Login with valid credentials
    Given no user with email "validuser@test.com"
    When I register user "Valid User" with email "validuser@test.com" and role "CLIENTE"
    And I login with email "validuser@test.com" and password "123456"
    Then response status should be 200
    And response contains token

  Scenario: Login with invalid credentials
    When I login with email "nonexistent@test.com" and password "wrongpass"
    Then response status should be 401

  Scenario: Access protected endpoint without token
    When I call GET "/api/auth/me" without token
    Then response should be unauthorized

  Scenario: Access protected endpoint with valid token
    Given no user with email "autheduser@test.com"
    When I register user "Authed User" with email "autheduser@test.com" and role "CLIENTE"
    And I login with email "autheduser@test.com" and password "123456"
    And I call GET "/api/auth/me" with token for "autheduser@test.com"
    Then response status should be 200

  Scenario: Admin creates restaurant and assigns restaurant user
    Given the admin token is available
    When admin has created restaurant alias "TestRestaurant"
    And restaurant user "restaurant@test.com" is registered for alias "TestRestaurant"
    Then response status should be 201

  Scenario: Restaurant user manages own products
    Given the admin token is available
    And admin has created restaurant alias "MyRestaurant"
    And restaurant user "myrest@test.com" is registered for alias "MyRestaurant"
    When I login with email "myrest@test.com" and password "123456"
    And I create product "TestProduct" with token for "myrest@test.com"
    Then response status should be 201

  Scenario: Client creates order with restaurant product
    Given the admin token is available
    And admin has created restaurant alias "OrderRestaurant"
    And restaurant user "orderrest@test.com" is registered for alias "OrderRestaurant"
    And I login with email "orderrest@test.com" and password "123456"
    And restaurant "OrderRestaurant" has product "OrderProduct"
    And no user with email "orderclient@test.com"
    And I register user "Order Client" with email "orderclient@test.com" and role "CLIENTE"
    When I login with email "orderclient@test.com" and password "123456"
    And client "orderclient@test.com" creates a pedido for restaurant "OrderRestaurant" with product "OrderProduct"
    Then response status should be 201
    And the order references restaurant "OrderRestaurant" and product "OrderProduct"

  Scenario: Access endpoint with expired token
    Given no user with email "expireduser@test.com"
    When I register user "Expired User" with email "expireduser@test.com" and role "CLIENTE"
    And I call GET "/api/auth/me" with expired token for "expireduser@test.com"
    Then response should be unauthorized

  Scenario: Public endpoint accessible without authentication
    When I call GET "/api/auth/login" without token
    Then response status should be 405
