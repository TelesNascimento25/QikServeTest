resources

baseURL: api-version

# ProductController

forward from wiremock

GET /products

GET /products/:id
# BasketController

GET /baskets/?status=ACTIVE&FINISHED
// reduced basket representation
-> items
-> total quantity?
// -> total // with promotions already applied?
-> status // only active
// paginated
## Response
[
    {
        "id": "123",
        "itemCount": 2,
        "items": [ "1", "2" ],
        "productCount": 4,
        // "total": 123.45,    // display only if finished
        "status": "ACTIVE",
    },
]

POST /baskets
{
    "items": [
        {
            "productId": "1",
            "quantity": 2
        },
        {
            "productId": "1",   // may repeat products
            "quantity": 1
        },
        {
            "productId": "2",
            "quantity": 20  // > 0!! may not be empty, custom exception, test that
        }
    ]
}
// create new basket (empty)
// allow option to provide items
return id (at least)

## Response
// full basket representation
{
    "id": "123",
    "status": "ACTIVE",
    "items": [ "1", "2", "3" ],
    "itemCount": 3,
    "productCount": 23
}

GET /baskets/:id
// full basket representation
if FINISHED, display total


[//]: # (DELETE /baskets/:id)

[//]: # (-- change internal status to DELETED)

POST /baskets/:id/clear
-- remove all items from basket, effectively deleting items from database
POST /baskets/:id/cancel
GET /baskets/:id/savings
// compute savings for basket, available only for ACTIVE baskets
{
    "savings": 12.34
}
POST /baskets/:id/checkout // -> status: FINISHED == PAYED, here we can apply promotions

Basket
{
    "id": "123",
    "status": "ACTIVE" | "FINISHED" | "DELETED" | "CANCELED",
    "items": [ "1", "2", "3" ], // ids
    "itemCount": 3,
    "productCount": 23,
    "total": 123.45 // display only if finished
}

# BasketItemController

GET /basketItems/?basketId=123&page=0&size=10&sortBy=quantity&sortDirection=ASC
// paginated
// basketId query param is mandatory
[
    {
        "id": "1",
        "basketId": "123",
        "productId": "1",
        "quantity": 2,
        "price": 12.34,
        "promotions": [
            {
                "id": "1",
                "type": "DISCOUNT",
                "value": 0.1
            }
        ]
    },
    {
        "id": "2",
        "basketId": "123",
        "productId": "1",
        "quantity": 1,
        "price": 12.34,
        "promotions": []
    },
    {
        "id": "3",
        "basketId": "123",
        "productId": "2",
        "quantity": 20,
        "price": 12.34,
        "promotions": []
    }
]

GET /basketItems/:id
// full basket item representation
{
    "id": "1",
    "basketId": "123",
    "productId": "1",
    "quantity": 2,
    "price": 12.34,
    "promotions": [
        {
            "id": "1",
            "type": "DISCOUNT",
            "value": 0.1
        }
    ]
}

PATCH /basketItems/:id
{
    "quantity": 3 // > 0
}

DELETE /basketItems/:id
// delete for real

POST /basketItems
// only for active baskets
{
    "basketId": "123",
    "productId": "1",
    "quantity": 2 // > 0
}


// spring validation, same model different validation
// jackson, ignore null


migrations

webmvc tests

start with wiremock, change wiremock or change api provider, bean for provider

entities for
tables
enums

// auto generate ids, created and updated timestamps, set automatically, based on previous project

repositories, queries for aggregates

services

use strategy to apply promotions

controllers

cache wiremock client, expiration time

validation groups, custom exceptions & controller advices

no auth for now

observability? logging, metrics (only if time allows)


