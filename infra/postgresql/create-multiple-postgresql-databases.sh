#!/bin/bash

set -e
set -u

function create_user_and_database() {
	local database=$1
	local user=$2
	local password=$3

	echo "  Creating user '$user' and database '$database'"
	psql -v ON_ERROR_STOP=1 \
	     --username "$POSTGRES_USER" \
	     --dbname "postgres" \
	     -v db="$database" \
	     -v usr="$user" \
	     -v pwd="$password" <<-'EOSQL'
	    SELECT format('CREATE ROLE %I LOGIN PASSWORD %L', :'usr', :'pwd')
	    WHERE NOT EXISTS (SELECT FROM pg_roles WHERE rolname = :'usr')
	    \gexec

	    SELECT format('CREATE DATABASE %I OWNER %I', :'db', :'usr')
	    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = :'db')
	    \gexec

	    SELECT format('GRANT ALL PRIVILEGES ON DATABASE %I TO %I', :'db', :'usr')
	    \gexec
EOSQL
}

if [ -n "${POSTGRES_MULTIPLE_DATABASES:-}" ]; then
	echo "Multiple database creation requested"
	IFS=',' read -ra entries <<< "$POSTGRES_MULTIPLE_DATABASES"
	for entry in "${entries[@]}"; do
		IFS=':' read -r db user password <<< "$entry"
		# Si user/password omis, on réutilise le nom de la base
		user="${user:-$db}"
		password="${password:-$db}"
		create_user_and_database "$db" "$user" "$password"
	done
	echo "Multiple databases created"
fi