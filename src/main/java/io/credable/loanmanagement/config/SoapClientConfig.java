package io.credable.loanmanagement.config;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Configuration
public class SoapClientConfig {

    @Value("${cbs.kyc.url}")
    private String kycUrl;

    @Value("${cbs.transaction.url}")
    private String transactionUrl;

    @Value("${cbs.kyc.username}")
    private String kycUsername;

    @Value("${cbs.kyc.password}")
    private String kycPassword;

    @Value("${cbs.transaction.username}")
    private String transactionUsername;

    @Value("${cbs.transaction.password}")
    private String transactionPassword;

    @Bean
    public Jaxb2Marshaller kycMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("io.credable.loanmanagement.client.kyc.wsdl");
        return marshaller;
    }

    @Bean
    public Jaxb2Marshaller transactionMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("io.credable.loanmanagement.client.transaction.wsdl");
        return marshaller;
    }

    @Bean
    public WebServiceTemplate kycWebServiceTemplate() {
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setMarshaller(kycMarshaller());
        webServiceTemplate.setUnmarshaller(kycMarshaller());
        webServiceTemplate.setDefaultUri(kycUrl);
        webServiceTemplate.setMessageSender(httpComponentsMessageSender(kycUsername, kycPassword));
        return webServiceTemplate;
    }

    @Bean
    public WebServiceTemplate transactionWebServiceTemplate() {
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setMarshaller(transactionMarshaller());
        webServiceTemplate.setUnmarshaller(transactionMarshaller());
        webServiceTemplate.setDefaultUri(transactionUrl);
        webServiceTemplate.setMessageSender(httpComponentsMessageSender(transactionUsername, transactionPassword));
        return webServiceTemplate;
    }

    private HttpComponentsMessageSender httpComponentsMessageSender(String username, String password) {
        HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
        messageSender.setCredentials(new UsernamePasswordCredentials(username, password));
        return messageSender;
    }
} 