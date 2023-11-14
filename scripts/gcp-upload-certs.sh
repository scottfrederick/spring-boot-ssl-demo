#!/bin/bash

upload_secret() {
  local secret_name=$1
  local file=$2

  gcloud secrets create ${secret_name} --replication-policy="automatic"
  gcloud secrets versions add ${secret_name} --data-file=${file}
}

upload_secret ca-cert books-server/src/main/resources/redis-client-certs/ca.crt

upload_secret redis-client-cert books-server/src/main/resources/redis-client-certs/tls.crt
upload_secret redis-client-key books-server/src/main/resources/redis-client-certs/tls.key

upload_secret web-server-cert books-server/src/main/resources/web-server-certs/tls.crt
upload_secret web-server-key books-server/src/main/resources/web-server-certs/tls.key

upload_secret web-client-cert books-client/src/main/resources/web-client-certs/tls.crt
upload_secret web-client-key books-client/src/main/resources/web-client-certs/tls.key
