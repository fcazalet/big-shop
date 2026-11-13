# BIG-SHOP

🚧Under construction🚧

## Architecture
 This work is a portfolio example, currently in development

```mermaid
flowchart LR
    subgraph Shop Customer
        Frontstore[Frontstore Web App]
    end
    subgraph Admin User
        Admin[Admin Web App]
    end
    subgraph Services
        API[API gateway]
        Customer[Customer service]
        Catalog[Catalog service]
        Cart[Cart service]
        Order[Order service]
    end
    subgraph Security
        Keycloak[Keycloak]
        KeycloakDB[(Keycloak DB)]
    end
    subgraph Storage
        CustomerDB[(Orders DB)]
        CatalogDB[(Orders DB)]
        CartDB[(Cart DB)]
        OrderDB[(Orders DB)]
    end
    Frontstore -->|Login| Keycloak
    Keycloak --> |AccessToken| Frontstore
    Frontstore --> API
    Admin -->|Login| Keycloak
    Keycloak --> |AccessToken| Admin
    Admin --> API
    API -. Token Validation .-> Keycloak
    Keycloak --> KeycloakDB
    API --> Customer
    API --> Catalog
    API --> Cart
    API --> Order
    Customer --> CustomerDB
    Catalog --> CatalogDB
    Cart --> CartDB
    Order --> OrderDB
```

## Run local dev

### Prerequisites

Docker and compose (Docker Desktop).

### Run docker compose

Launch all containers

    cd infra
    cp .env.example .env        # replace all vars, passwords
    docker compose up -d

Keycloak is in dev mode and initialized with keycloak/realms/*.json configurations.

### Initialize Keycloak (realms)

Run the configured script bootstrap-admin.sh to create the first admin user in Keycloak:

    ./keycloak/bootstrap-admin.sh

If having trouble on creation and need to retry, just go to the Keycloak console http://localhost:8180/ and delete the user in the realm.
Then run the script again.