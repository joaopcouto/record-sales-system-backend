package br.com.sysmap.bootcamp.domain.service.integration;


import br.com.sysmap.bootcamp.domain.mapper.AlbumMapper;
import br.com.sysmap.bootcamp.domain.model.AlbumModel;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class SpotifyApi {

    private se.michaelthelin.spotify.SpotifyApi spotifyApi = new se.michaelthelin.spotify.SpotifyApi.Builder()
            .setClientId("e2e653e0f6e845c7aa83763645bf8211")
            .setClientSecret("c621a1bfef28446aa1dc607d2b4e7cd4")
            .build();

    public List<AlbumModel> getAlbums(String search) throws IOException, ParseException, SpotifyWebApiException {

        spotifyApi.setAccessToken(getToken());
        return AlbumMapper.INSTANCE.toModel(spotifyApi.searchAlbums(search).market(CountryCode.BR)
                .limit(30)
                .build().execute().getItems()).stream()
                .peek(album -> {
                    double min = 12.00;
                    double max = 100.00;
                    double randomValue = min + (Math.random() * (max - min));
                    album.setValue(BigDecimal.valueOf(randomValue).setScale(2, BigDecimal.ROUND_HALF_UP));
                }).toList();


    }

    public String getToken() throws IOException, ParseException, SpotifyWebApiException {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        return clientCredentialsRequest.execute().getAccessToken();
    }

}
