#!/bin/bash

create_ssl_config() {
  mkdir -p certs

  cat > certs/openssl.cnf <<_END_
  subjectAltName = @alt_names
  [alt_names]
  DNS.1 = example.com
  DNS.2 = localhost
  [ server_cert ]
  keyUsage = digitalSignature, keyEncipherment
  nsCertType = server
  [ client_cert ]
  keyUsage = digitalSignature, keyEncipherment
  nsCertType = client
_END_

}

generate_ca_cert() {
  openssl genrsa -out certs/ca.key 4096
  openssl req -key certs/ca.key -out certs/ca.crt \
      -x509 -new -nodes -sha256 -days 365 \
      -subj "/O=Spring Boot Test/CN=Certificate Authority" \
      -addext "subjectAltName=DNS:example.com,DNS:localhost,DNS:127.0.0.1"
}

generate_cert() {
    local location=$1
    local type=$2

    local keyfile=${location}/tls.key
    local certfile=${location}/tls.crt

    mkdir -p ${location}

    openssl genrsa -out ${keyfile} 2048
    openssl req -key ${keyfile} \
        -new -sha256 \
        -subj "/O=Spring Boot Test/CN=localhost" \
        -addext "subjectAltName=DNS:example.com,DNS:localhost,DNS:127.0.0.1" | \
    openssl x509 -req -out ${certfile} \
        -CA certs/ca.crt -CAkey certs/ca.key -CAserial certs/ca.txt -CAcreateserial \
        -sha256 -days 365 \
        -extfile certs/openssl.cnf \
        -extensions ${type}_cert
}

if ! command -v openssl &> /dev/null; then
    echo "openssl is required"
    exit
fi

create_ssl_config
generate_ca_cert
generate_cert redis/redis-server-certs server
generate_cert books-server/src/main/resources/redis-client-certs client
generate_cert books-server/src/main/resources/web-server-certs server
generate_cert books-client/src/main/resources/web-client-certs client

#
# copy CA certificate everywhere
#
for dir in redis/redis-server-certs books-server/src/main/resources/redis-client-certs books-server/src/main/resources/web-server-certs books-client/src/main/resources/web-client-certs; do
  cp certs/ca.crt ${dir}
  echo ""
  echo "Certificates in '${dir}'"
  ls -l ${dir}
done

for dir in redis/redis-server-certs; do
  chmod go+r ${dir}/tls.key
done
