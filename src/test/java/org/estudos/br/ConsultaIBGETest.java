package org.estudos.br;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsultaIBGETest {
    private static final String ESTADOS_API_URL = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/";

    private static final String DISTRITOS_API_URL = "https://servicodados.ibge.gov.br/api/v1/localidades/distritos/";

    @Mock
    private HttpURLConnection connection;

    @Mock
    private BufferedReader reader;

    @InjectMocks
    private ConsultaIBGE consultaIBGE;

    @Test
    @DisplayName("Teste para consulta de estados com mocks")
    public void testConsultarEstadosComMocks() throws IOException {
        String uf = "SP";
        String respostaEsperada = "{\"nome\":\"São Paulo\"}";

        when(connection.getInputStream()).thenReturn(new InputStreamReader(new ByteArrayInputStream(respostaEsperada.getBytes())));
        when(reader.readLine()).thenReturn(respostaEsperada);

        String resposta = consultaIBGE.consultarEstado(uf);

        assertEquals(respostaEsperada, resposta);
    }

    @Test
    @DisplayName("Teste para consulta única de um estado com erro")
    public void testConsultarEstadoComErro() {
        String uf = "SP";

        when(connection.getInputStream()).thenThrow(new IOException("Erro de conexão"));

        assertThrows(IOException.class, () -> consultaIBGE.consultarEstado(uf));
    }

    @Test
    @DisplayName("Teste para consulta única de um estado")
    public void testConsultarEstado() throws IOException {
        String uf = "SP";
        String respostaEsperada = "{\"nome\":\"São Paulo\"}";

        when(connection.getInputStream()).thenReturn(new InputStreamReader(new ByteArrayInputStream(respostaEsperada.getBytes())));
        when(reader.readLine()).thenReturn(respostaEsperada);

        String resposta = consultaIBGE.consultarEstado(uf);

        assertEquals(respostaEsperada, resposta);
        assertEquals("São Paulo", consultaIBGE.getEstadoNome(resposta));
    }

    @Test
    @DisplayName("Teste para consulta única de um distrito")
    public void testConsultarDistrito() throws IOException {
        int id = 123;
        String respostaEsperada = "{\"nome\":\"Cidade 123\"}";

        when(connection.getInputStream()).thenReturn(new InputStreamReader(new ByteArrayInputStream(respostaEsperada.getBytes())));
        when(reader.readLine()).thenReturn(respostaEsperada);

        String resposta = consultaIBGE.consultarDistrito(id);

        assertEquals(respostaEsperada, resposta);
        assertEquals("Cidade 123", consultaIBGE.getDistritoNome(resposta));
    }
}