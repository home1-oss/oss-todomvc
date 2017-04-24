##https 证书相关

1.生成keysore 服务端用
   
    keytool -genkey -dname "CN=localhost,OU=server,O=server,L=bj,ST=bj,C=CN" -alias myca -keyalg RSA -keysize 1024 -keystore server.jks -keypass 123456 -storepass 123456 -validity 3650

2.生成证书以及导入证书 客户端用

    keytool -exportcert -alias myca -keystore server.jks -file myca.cer -rfc

    keytool -import -file myca.cer -keystore "%JAVA_HOME%\jre\lib\security\cacerts" -alias myca


3.检查是否导入

    keytool -list -keystore "%JAVA_HOME%\jre\lib\security\cacerts" | findstr /i myca


4.删除证书
    
    keytool -delete -alias myca -keystore "%JAVA_HOME%/jre/lib/security/cacerts" -storepass changeit
    
    
## 服务器端配置增加并把server.jks拷贝到相应位置

    server:
      ssl:
        key-store: classpath:server.jks
        key-store-password: 123456


## 客户端有证书验证问题 要忽略域名验证 比如在我用了httpclient基础之上我这样做

      @Bean
      public HttpClient feignHttpClient() throws KeyManagementException, NoSuchAlgorithmException {

        return HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
      }
    