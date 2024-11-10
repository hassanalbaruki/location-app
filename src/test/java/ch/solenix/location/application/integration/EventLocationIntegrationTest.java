package ch.solenix.location.application.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static ch.solenix.location.application.factory.EventFactory.createEventLocation;
import static ch.solenix.location.application.factory.EventFactory.createEventLocationForIdNotExist;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class EventLocationIntegrationTest {

    /**
     * Route prefix.
     */
    final private String base = "/api/event/position/";

    /**
     * Main entry point for server-side Spring MVC test support.
     */
    @Autowired
    public MockMvc mvc;

    /**
     * Object mapper used to convert objects to json strings.
     */
    @Autowired
    public ObjectMapper mapper;

    /**
     * Test get event location by ID.
     */
    @Test
    @SneakyThrows
    void testGetEventLocationById() {

        final var eventLocation = createEventLocation();
        final var result = mvc.perform(MockMvcRequestBuilders.get(base + "E001"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        assertEquals(mapper.writeValueAsString(eventLocation), result.getResponse().getContentAsString());
        assertEquals(200, result.getResponse().getStatus());
    }

    /**
     * Test get event location if the ID is not exist.
     */
    @Test
    @SneakyThrows
    void testGetEventLocationByIdNotExist() {

        final var eventLocation = createEventLocationForIdNotExist();
        final var result = mvc.perform(MockMvcRequestBuilders.get(base + "E000"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        assertEquals(mapper.writeValueAsString(eventLocation), result.getResponse().getContentAsString());
        assertEquals(404, result.getResponse().getStatus());
    }

    /**
     * Test get all events locations.
     */
    @Test
    @SneakyThrows
    void testGetAllEventLocations() {
        final var result = mvc.perform(MockMvcRequestBuilders.get(base + "all"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }
}
