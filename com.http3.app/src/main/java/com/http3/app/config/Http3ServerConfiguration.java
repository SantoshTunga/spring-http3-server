package com.http3.app.config;

import org.springframework.boot.reactor.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import reactor.netty.http.Http3SslContextSpec;
import reactor.netty.http.HttpProtocol;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

@Configuration
public class Http3ServerConfiguration {

    @Bean
    WebServerFactoryCustomizer<NettyReactiveWebServerFactory>
    http3Customizer() {

        return factory -> factory.addServerCustomizers(server -> {


            ClassPathResource certResource =
                    new ClassPathResource("certs/server-cert.pem");

            ClassPathResource keyResource =
                    new ClassPathResource("certs/server-key-pkcs8.pem");

            File certificate,privateKey =
                    null;
            try {
                certificate = certResource.getFile();
                privateKey = keyResource.getFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            Http3SslContextSpec sslContext =
                    Http3SslContextSpec.forServer(
                            privateKey,
                            null,
                            certificate
                    );

            return server
                    .protocol(HttpProtocol.HTTP3)

                    .secure(ssl ->
                            ssl.sslContext(sslContext))

                    .http3Settings(http3 ->
                            http3
                                    .idleTimeout(Duration.ofSeconds(30))
                                    .maxData(10_000_000)
                                    .maxStreamDataBidirectionalLocal(1_000_000)
                                    .maxStreamDataBidirectionalRemote(1_000_000)
                                    .maxStreamsBidirectional(100)
                    );
        });
    }
}