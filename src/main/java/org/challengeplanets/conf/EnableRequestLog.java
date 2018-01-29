package org.challengeplanets.conf;

import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Slf4jRequestLog;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.stereotype.Component;

@Component
public class EnableRequestLog implements EmbeddedServletContainerCustomizer {

    private static final JettyServerCustomizer USE_SLF4J_REQUEST_LOG =
            server -> server.setRequestLog(createRequestLog());

    private static RequestLog createRequestLog(){
    	Slf4jRequestLog requestLog= new Slf4jRequestLog();
    	requestLog.setExtended(true);
    	requestLog.setLogLatency(true);
    	requestLog.setLogServer(true);
    	return requestLog;
    }
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        if (container instanceof JettyEmbeddedServletContainerFactory) {
            ((JettyEmbeddedServletContainerFactory) container)
                    .addServerCustomizers(USE_SLF4J_REQUEST_LOG);
        } else {
            throw new IllegalArgumentException(
                    "Expected a Jetty container factory but encountered " + container.getClass());
        }
    }
}