package com.clique.retire.schedule.util;

import com.clique.retire.schedule.client.BackofficeClient;
import com.clique.retire.schedule.client.PainelCliqueRetireClient;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;

public class FeignUtil {

	public static PainelCliqueRetireClient getPainelCliqueRetireClient(String url) {
		return Feign.builder()
				.client(new OkHttpClient())
				.encoder(new GsonEncoder())
				.decoder(new GsonDecoder())
				.target(PainelCliqueRetireClient.class, url);
	}
	
	public static BackofficeClient getBackofficeClient(String url) {
		return Feign.builder()
				.client(new OkHttpClient())
				.encoder(new GsonEncoder())
				.decoder(new GsonDecoder())
				.target(BackofficeClient.class, url);
	}
}
