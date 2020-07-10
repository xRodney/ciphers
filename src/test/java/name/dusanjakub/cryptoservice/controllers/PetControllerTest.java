package name.dusanjakub.cryptoservice.controllers;

import java.util.Arrays;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import name.dusanjakub.cryptoservice.entities.JsonAttributes;
import name.dusanjakub.cryptoservice.entities.JsonRequest;
import name.dusanjakub.cryptoservice.entities.Pet;
import name.dusanjakub.cryptoservice.entities.Species;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = PetController.class)
public class PetControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PetController controller;

    @Test
    public void getById() throws Exception {
        Pet pet1 = new Pet();
        pet1.setId("10");
        pet1.setAge(2);
        pet1.setName("Mousey");
        pet1.setToys(Arrays.asList("cheese"));
        controller.put("10", new JsonRequest<>(new JsonAttributes<>("10", pet1)));

        mvc.perform(MockMvcRequestBuilders
                .get("/pets/10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mousey"));
    }

    @Test
    public void post() throws Exception {
        Pet pet1 = new Pet();
        pet1.setId("10");
        pet1.setAge(2);
        pet1.setName("Mousey");
        pet1.setToys(Arrays.asList("cheese"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/pets/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new JsonRequest<>(pet1.getId(), pet1)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mousey"))
                .andReturn();

        Pet resultPet = mapper.readValue(result.getResponse().getContentAsByteArray(), Pet.class);
        Assert.assertNotSame(resultPet.getId(), pet1.getId());
    }

    //@Test
    public void post_invalidData() throws Exception {
        Pet pet1 = new Pet();
        pet1.setId("10");
        pet1.setAge(2);
        pet1.setName("");
        pet1.setToys(Arrays.asList("cheese"));

        mvc.perform(MockMvcRequestBuilders
                .post("/pets/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new JsonRequest<>(pet1.getId(), pet1)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("must not be blank"));
    }

    @Test
    public void patch_validNameAndKeepAge() throws Exception {
        Pet pet1 = new Pet();
        pet1.setId("10");
        pet1.setAge(2);
        pet1.setName("Mousey");
        pet1.setToys(Arrays.asList("cheese"));
        controller.put("10", new JsonRequest<>(new JsonAttributes<>("10", pet1)));

        String patchPet = "{\"data\": {\"attributes\": {\"name\": \"Mickey\"}}}";

        mvc.perform(MockMvcRequestBuilders
                .patch("/pets/10")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(patchPet)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mickey"))
                .andReturn();
    }

    //@Test
    public void patch_blankNameAndKeepAge() throws Exception {
        Pet pet1 = new Pet();
        pet1.setId("10");
        pet1.setAge(2);
        pet1.setName("Mousey");
        pet1.setToys(Arrays.asList("cheese"));
        controller.put("10", new JsonRequest<>(new JsonAttributes<>("10", pet1)));

        String patchPet = "{\"data\": {\"attributes\": {\"name\": \"\"}}}";

        mvc.perform(MockMvcRequestBuilders
                .patch("/pets/10")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(patchPet)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").doesNotExist());
    }

    //@Test
    public void patch_invalidNameAndAge() throws Exception {
        Pet pet1 = new Pet();
        pet1.setId("10");
        pet1.setAge(2);
        pet1.setName("Mousey");
        pet1.setToys(Arrays.asList("cheese"));
        controller.put("10", new JsonRequest<>(new JsonAttributes<>("10", pet1)));

        String patchPet = "{\"data\": {\"attributes\": {\"name\": \"\", \"age\": -1}}}";

        mvc.perform(MockMvcRequestBuilders
                .patch("/pets/10")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(patchPet)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value("must be greater than or equal to 0"));
    }

    @Test
    public void patch_species() throws Exception {
        var pet1 = new Pet();
        pet1.setId("10");
        pet1.setAge(2);
        pet1.setName("Mousey");
        pet1.setToys(Arrays.asList("cheese"));
        var species = new Species();
        species.setName("Mys");
        species.setDescription("Mys");
        pet1.setSpecies(species);
        controller.put("10", new JsonRequest<>(new JsonAttributes<>("10", pet1)));

        String patchPet = "{\"data\": {\"attributes\": {\"name\": \"dsdsd\", \"age\": 10, \"species\": {\"name\": \"Mouse\"}}}}";

        mvc.perform(MockMvcRequestBuilders
                .patch("/pets/10")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(patchPet)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("dsdsd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.species.name").value("Mouse"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.species.description").value("Mys"))
                .andReturn();
    }

    @Test
    public void delete_existingRecord() throws Exception {
        Pet pet1 = new Pet();
        pet1.setId("10");
        pet1.setAge(2);
        pet1.setName("Mousey");
        pet1.setToys(Arrays.asList("cheese"));
        controller.put("10", new JsonRequest<>(new JsonAttributes<>("10", pet1)));

        mvc.perform(MockMvcRequestBuilders
                .delete("/pets/10")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assert.assertNull(controller.getById("10"));
    }

    @Test
    public void delete_nonexistingRecord() throws Exception {
        Assert.assertNull(controller.getById("101"));

        mvc.perform(MockMvcRequestBuilders
                .delete("/pets/101")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assert.assertNull(controller.getById("101"));
    }

    @Test
    public void put() throws Exception {
        Pet pet1 = new Pet();
        pet1.setId("10");
        pet1.setAge(2);
        pet1.setName("Mousey");
        pet1.setToys(Arrays.asList("cheese"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .put("/pets/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new JsonRequest<>(pet1.getId(), pet1)))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mousey"))
                .andReturn();

        Pet resultPet = mapper.readValue(result.getResponse().getContentAsByteArray(), Pet.class);
        Assert.assertEquals(resultPet.getId(), pet1.getId());
        Assert.assertEquals(controller.getById("10").getId(), "10");
    }

    @Test
    public void xxx() {

        Pet pet1 = new Pet();
        pet1.setId("10");
        pet1.setAge(2);
        pet1.setName("Mousey");
        pet1.setToys(Arrays.asList("cheese"));
        Species species = new Species();
        species.setName("mouse");
        pet1.setSpecies(species);

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.convertValue(pet1, Map.class);

    }
}
