#!/bin/bash

generate_ca_cert() {
  openssl genrsa -out certs/ca.key 4096
  openssl req -key certs/ca.key -out certs/ca.crt \
      -x509 -new -nodes -sha256 -days 365 \
      -subj "/O=Spring Boot Test/CN=Certificate Authority" \
      -addext "subjectAltName=DNS:example.com,DNS:localhost,DNS:127.0.0.1"
}

generate_cert() {
    local name=$1
    local type=$2

    local keyfile=certs/${name}.key
    local certfile=certs/${name}.crt

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

generate_ca_cert
generate_cert web-server server
generate_cert web-client client
generate_cert redis-server server
generate_cert redis-client client

#
# copy CA certificate everywhere
#
for dir in redis/certs books-server/src/main/resources/certs books-client/src/main/resources/certs; do
  cp certs/ca.crt ${dir}
done

#
# copy certificates and private keys for the redis server
#
for dir in redis/certs; do
  cp certs/redis-server.* ${dir}
  chmod go+r ${dir}/redis-server.key
  echo ""
  echo "Redis server certificates in '${dir}'"
  ls -l ${dir}
done

#
# copy certificates and private keys for the web server
#
for dir in books-server/src/main/resources/certs; do
  cp certs/redis-client.* ${dir}
  cp certs/web-server.* ${dir}
  echo ""
  echo "Server application certificates in '${dir}'"
  ls -l ${dir}
done

#
# copy certificates and private keys for the web client
#
for dir in books-client/src/main/resources/certs; do
  cp certs/web-client.* ${dir}
  echo ""
  echo "Client application certificates in '${dir}'"
  ls -l ${dir}
done
