package com.borchowiec.coronavirustrackerforpoland.service;

import com.borchowiec.coronavirustrackerforpoland.model.RegionalData;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RegionalDataServiceTest {

    @Test
    void toRegionalData_properData_shouldReturnListOfRegionalData() throws JsonProcessingException {
        // given
        String argument = "{\n" +
                "  \"0200000\": {\n" +
                "    \"zarazeni\": \"608\",\n" +
                "    \"zgony\": \"18\",\n" +
                "    \"zdrowi\": \"318\",\n" +
                "    \"nazwa\": \"dolno\\u015bl\\u0105skie\"\n" +
                "  },\n" +
                "  \"2200000\": {\n" +
                "    \"zarazeni\": \"155\",\n" +
                "    \"zgony\": \"1\",\n" +
                "    \"zdrowi\": \"13\",\n" +
                "    \"nazwa\": \"pomorskie\"\n" +
                "  }\n" +
                "}";

        // when
        RegionalDataService regionalDataService = new RegionalDataService();
        List<RegionalData> actual = regionalDataService.toRegionalData(argument);

        // then
        List<RegionalData> expected = Stream.of(
                new RegionalData("dolnośląskie", 608, 18, 318),
                new RegionalData("pomorskie", 155, 1, 13))
                .collect(Collectors.toList());
        assertEquals(expected, actual);
    }

    @Test
    void toRegionalData_containsWrongElements_shouldReturnListWithoutTheseElements() throws JsonProcessingException {
        // given
        String argument = "{\n" +
                "  \"0200000\": {\n" +
                "    \"zgony\": \"18\",\n" +
                "    \"zdrowi\": \"318\",\n" +
                "    \"nazwa\": \"dolno\\u015bl\\u0105skie\"\n" +
                "  },\n" +
                "  \"2200000\": {\n" +
                "    \"zarazeni\": \"155\",\n" +
                "    \"zdrowi\": \"0\",\n" +
                "    \"nazwa\": \"pomorskie\"\n" +
                "  },\n" +
                "  \"3000000\": {\n" +
                "    \"zarazeni\": \"439\",\n" +
                "    \"zgony\": \"22\",\n" +
                "    \"nazwa\": \"wielkopolskie\"\n" +
                "  },\n" +
                "  \"2400000\": {\n" +
                "    \"zarazeni\": \"729\",\n" +
                "    \"zgony\": \"38\",\n" +
                "    \"zdrowi\": \"0\"\n" +
                "  },\n" +
                "  \"1000000\": {\n" +
                "    \"zarazeni\": \"432\",\n" +
                "    \"zgony\": \"7\",\n" +
                "    \"zdrowi\": \"2\",\n" +
                "    \"nazwa\": \"\\u0142\\u00f3dzkie\"\n" +
                "  }\n" +
                "}";
        // when
        RegionalDataService regionalDataService = new RegionalDataService();
        List<RegionalData> actual = regionalDataService.toRegionalData(argument);

        // then
        List<RegionalData> expected = Stream.of(
                new RegionalData("łódzkie", 432, 7, 2))
                .collect(Collectors.toList());
        assertEquals(expected, actual);
    }
}