#!/bin/bash

upload_secret() {
  local secret_name=$1
  local cert_file=$2
  local key_file=$3

  if [[ -z "${key_file}" ]]; then
    vault kv put -mount=certs ${secret_name} certificate=@${cert_file}
  else
    vault kv put -mount=certs ${secret_name} certificate=@${cert_file} private-key=@${key_file}
  fi
}

vault secrets enable -path="certs" -description="certificates" kv

upload_secret ca \
  books-server/src/main/resources/redis-client-certs/ca.crt

upload_secret redis-client \
  books-server/src/main/resources/redis-client-certs/tls.crt \
  books-server/src/main/resources/redis-client-certs/tls.key

upload_secret web-server \
  books-server/src/main/resources/web-server-certs/tls.crt \
  books-server/src/main/resources/web-server-certs/tls.key

upload_secret web-client \
  books-client/src/main/resources/web-client-certs/tls.crt \
  books-client/src/main/resources/web-client-certs/tls.key
