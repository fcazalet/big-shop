#!/usr/bin/env bash
# Create the first user for realm "admin".
# Temporary password + need to change on first loggin and configure TOTP
set -euo pipefail

set -a; source .env; set +a

KCADM="docker compose exec -T keycloak /opt/keycloak/bin/kcadm.sh"

$KCADM config credentials \
  --server http://localhost:8080 --realm master \
  --user "$KC_BOOTSTRAP_ADMIN_USERNAME" --password "$KC_BOOTSTRAP_ADMIN_PASSWORD"

echo "Creating user $ADMIN_FIRST_USER_EMAIL"

$KCADM create users -r admin \
  -s username="$ADMIN_FIRST_USER_EMAIL" \
  -s email="$ADMIN_FIRST_USER_EMAIL" \
  -s enabled=true \
  -s emailVerified=true \
  -s 'requiredActions=["UPDATE_PASSWORD","CONFIGURE_TOTP"]'

echo "Setting password to $ADMIN_FIRST_USER_EMAIL"

$KCADM set-password -r admin \
  --username "$ADMIN_FIRST_USER_EMAIL" \
  --new-password "$ADMIN_FIRST_USER_TEMP_PASSWORD" --temporary

echo "Adding roles to $ADMIN_FIRST_USER_EMAIL"

$KCADM add-roles -r admin \
  --uusername "$ADMIN_FIRST_USER_EMAIL" --rolename admin

echo "Created admin user : $ADMIN_FIRST_USER_EMAIL (temp passwod, TOTP require on first connect)"