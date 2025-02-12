package boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.infra.functions.api;

import boilerplate_spring_boot_serverless.boilerplate_spring_boot_serverless.BoilerplateSpringBootServerlessApplication;
import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpLambdaHandler implements RequestStreamHandler {
  private static SpringBootLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse> handler;

  static {
    try {
      LambdaContainerHandler.getContainerConfig().setInitializationTimeout(20000);

      handler =
          new SpringBootProxyHandlerBuilder<HttpApiV2ProxyRequest>()
              .defaultHttpApiV2Proxy()
              .servletApplication()
              .springBootApplication(BoilerplateSpringBootServerlessApplication.class)
              .buildAndInitialize();

    } catch (ContainerInitializationException e) {
      e.printStackTrace();
      throw new RuntimeException("Could not initialize Spring Boot application", e);
    }
  }

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
      throws IOException {
    handler.proxyStream(inputStream, outputStream, context);
  }
}
