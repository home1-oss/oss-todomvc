#!/usr/bin/env bash






export BUILD_PUBLISH_DEPLOY_SEGREGATION="true"
export BUILD_SITE_PATH_PREFIX="oss"

## for integration-test
if [ "${INFRASTRUCTURE}" == "github" ]; then
    export BUILD_INTEGRATION_TEST_SKIP="true"
elif [ "${INFRASTRUCTURE}" == "internal" ]; then
    export EUREKA_CLIENT_SERVICEURL_DEFAULTZONE="http://user:user_pass@oss-eureka:8761/eureka/"
    export SPRING_CLOUD_CONFIG_DISCOVERY_SERVICEID="oss-configserver"
    export SPRING_RABBITMQ_HOST="cloudbus.${INFRASTRUCTURE}"
fi

### OSS CI CALL REMOTE CI SCRIPT BEGIN
if [ -z "${LIB_CI_SCRIPT}" ]; then LIB_CI_SCRIPT="https://github.com/home1-oss/oss-build/raw/master/src/main/ci-script/lib_ci.sh"; fi
#if [ -z "${LIB_CI_SCRIPT}" ]; then LIB_CI_SCRIPT="http://gitlab.local:10080/home1-oss/oss-build/raw/develop/src/main/ci-script/lib_ci.sh"; fi
echo "eval \$(curl -s -L ${LIB_CI_SCRIPT})"
eval "$(curl -s -L ${LIB_CI_SCRIPT})"
#source src/main/ci-script/lib_ci.sh
### OSS CI CALL REMOTE CI SCRIPT END
