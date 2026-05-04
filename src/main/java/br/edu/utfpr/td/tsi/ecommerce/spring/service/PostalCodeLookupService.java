package br.edu.utfpr.td.tsi.ecommerce.spring.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.AddressSuggestion;
import br.edu.utfpr.td.tsi.ecommerce.spring.web.dto.ViaCepResponse;

@Service
public class PostalCodeLookupService {

	private static final Logger log = LoggerFactory.getLogger(PostalCodeLookupService.class);
	private static final Pattern DIGITS = Pattern.compile("\\D");
	private static final String VIACEP_URL = "https://viacep.com.br/ws/%s/json/";
	private static final HttpClient HTTP = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(5))
			.build();

	private final ObjectMapper objectMapper;

	public PostalCodeLookupService(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public AddressSuggestion suggest(String postalCodeRaw) {
		String digits = postalCodeRaw == null ? "" : DIGITS.matcher(postalCodeRaw).replaceAll("");
		if (digits.length() != 8) {
			log.warn("CEP inválido para busca de endereço: {}", postalCodeRaw);
			throw new IllegalArgumentException("Postal code must contain 8 digits.");
		}
		String formattedFallback = digits.substring(0, 5) + "-" + digits.substring(5);
		ViaCepResponse via = fetchViaCep(digits);
		if (Boolean.TRUE.equals(via.getErro())) {
			throw new IllegalArgumentException("CEP não encontrado.");
		}
		if (isBlank(via.getLocalidade()) && isBlank(via.getUf())) {
			throw new IllegalArgumentException("CEP não encontrado.");
		}
		String postalCode = !isBlank(via.getCep()) ? via.getCep().trim() : formattedFallback;
		AddressSuggestion suggestion = new AddressSuggestion(
				postalCode,
				nullToEmpty(via.getLogradouro()),
				nullToEmpty(via.getBairro()),
				nullToEmpty(via.getLocalidade()),
				nullToEmpty(via.getUf()));
		log.info("Sugestão de endereço para o CEP {}: {}, {}", postalCode, suggestion.getStreet(), suggestion.getCity());
		return suggestion;
	}

	private ViaCepResponse fetchViaCep(String digits) {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(String.format(VIACEP_URL, digits)))
				.timeout(Duration.ofSeconds(15))
				.header("Accept", "application/json")
				.GET()
				.build();
		try {
			HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() != 200) {
				log.warn("ViaCEP retornou HTTP {} para {}", response.statusCode(), digits);
				throw new IllegalStateException("Falha ao consultar o CEP.");
			}
			return objectMapper.readValue(response.body(), ViaCepResponse.class);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException("Consulta de CEP interrompida.", e);
		}
		catch (IOException e) {
			log.warn("Falha ao consultar ViaCEP para {}", digits, e);
			throw new IllegalStateException("Falha ao consultar o CEP.", e);
		}
	}

	private static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	private static String nullToEmpty(String s) {
		return s == null ? "" : s.trim();
	}
}
