package com.integrationsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AppConnectorController.class)
public class AppConnectorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AppConnectorRepository appConnectorRepository;

    @Test
    public void shouldGetEntityIfIdIsProvided() throws Exception {
        given(appConnectorRepository.findById(anyInt())).willReturn(Optional.of(new AppConnector("Shopify", "Description")));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/connectors/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Shopify"))
                .andExpect(jsonPath("description").value("Description"));
    }

    @Test
    public void shouldReceiveNotFoundIfEntityIsNotFound() throws Exception {
        given(appConnectorRepository.findById(anyInt())).willThrow(new AppConnectorNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/connectors/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddNewEntityAndReceiveIt() throws Exception {
        AppConnector mockAppConnector = new AppConnector("Shopify", "Description");
        given(appConnectorRepository.save(mockAppConnector)).willReturn(mockAppConnector);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/connectors")
                        .content(asJsonString(mockAppConnector))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value("Shopify"))
                .andExpect(jsonPath("description").value("Description"));
    }

    @Test
    public void shouldThrowErrorIfRequestBodyIsNotCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/connectors")
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReceiveEmptyArrayIfThereAreNoEntities() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/connectors"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void shouldReceiveArrayWithEntitiesIfThereAreAny() throws Exception {
        AppConnector mockAppConnector = new AppConnector("Shopify", "Description");
        given(appConnectorRepository.findAll()).willReturn(asList(mockAppConnector));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/connectors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Shopify"));
    }

    @Test
    public void shouldReceiveNotFoundIfIdIsMissingOnPutRequest() throws Exception {
        AppConnector mockAppConnector = new AppConnector("Shopify", "Description");
        Mockito.when(appConnectorRepository.findById(anyInt())).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put("/api/connectors/{id}", 1)
                        .content(asJsonString(mockAppConnector))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateTheWholeObjectWithPutRequest() throws Exception {
        AppConnector mockAppConnector = new AppConnector("Old", "The App that will be updated");
        AppConnector updatedAppConnectorMock = new AppConnector("New", "The updated app");
        Mockito.when(appConnectorRepository.findById(anyInt())).thenReturn(Optional.of(mockAppConnector));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/connectors/{id}", 1)
                        .content(asJsonString(updatedAppConnectorMock))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("New"))
                .andExpect(jsonPath("description").value("The updated app"));
    }

    @Test
    public void shouldDeleteAppConnectorIfAvailable() throws Exception {
        AppConnector mockAppConnector = new AppConnector("Old", "The App that will be updated");
        Mockito.when(appConnectorRepository.findById(anyInt())).thenReturn(Optional.of(mockAppConnector));
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/connectors/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("App connector has been deleted successfully."));
    }

    @Test
    public void shouldUpdateOnlyPassedFields() throws Exception {
        AppConnector mockAppConnector = new AppConnector("Shopify", "No.1 Ecommerce Platform for All Businesses.");
        Mockito.when(appConnectorRepository.findById(anyInt())).thenReturn(Optional.of(mockAppConnector));
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/connectors/{id}", 1)
                        .content(" {\"name\": \"Yotpo\" }")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Yotpo"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}