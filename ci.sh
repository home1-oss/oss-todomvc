#!/usr/bin/env bash






export BUILD_PUBLISH_DEPLOY_SEGREGATION="true"
export BUILD_SITE_PATH_PREFIX="oss"

## for integration-test
export EUREKA_CLIENT_SERVICEURL_DEFAULTZONE="http://user:user_pass@eureka:8761/eureka/"
export SPRING_CLOUD_CONFIG_DISCOVERY_SERVICEID="configserver.local"
export SPRING_RABBITMQ_HOST="cloudbus"
export SPIRNG_RABBITMQ_PORT="5672"
export CONFIG_SERVER_HOST="configserver"
export CONFIG_SERVER_PORT="8888"

### OSS CI CALL REMOTE CI SCRIPT BEGIN
if [ -z "${LIB_CI_SCRIPT}" ]; then LIB_CI_SCRIPT="https://github.com/home1-oss/oss-build/raw/master/src/main/ci-script/lib_ci.sh"; fi
#if [ -z "${LIB_CI_SCRIPT}" ]; then LIB_CI_SCRIPT="http://gitlab.local:10080/home1-oss/oss-build/raw/develop/src/main/ci-script/lib_ci.sh"; fi
echo "eval \$(curl -s -L ${LIB_CI_SCRIPT})"
eval "$(curl -s -L ${LIB_CI_SCRIPT})"
#source src/main/ci-script/lib_ci.sh
### OSS CI CALL REMOTE CI SCRIPT END
