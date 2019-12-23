package name.dusanjakub.cryptoservice.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonMappingException;

import name.dusanjakub.cryptoservice.LazyBody;
import name.dusanjakub.cryptoservice.entities.Pet;

@Controller
@RequestMapping("/pets")
public class PetController {
    private Map<String, Pet> pets;

    public PetController() {
        this.pets = new HashMap<>();
        Pet pet1 = new Pet();
        pet1.setId("1");
        pet1.setAge(2);
        pet1.setName("Johny");
        pet1.setToys(Arrays.asList("bone"));
        pets.put(pet1.getId(), pet1);

        Pet pet2 = new Pet();
        pet2.setId("2");
        pet2.setAge(4);
        pet2.setName("Johny");
        pet2.setToys(Arrays.asList("bone"));
        pets.put(pet2.getId(), pet2);
    }

    @GetMapping(path = "/{id}")
    @ResponseBody
    public Pet getById(@PathVariable("id") String id) {
        return pets.get(id);
    }

    @PostMapping(path = "/")
    @ResponseBody
    public Pet post(@Valid @RequestBody Pet pet) {
        return put(UUID.randomUUID().toString(), pet);
    }

    @PatchMapping(path = "/{id}")
    @ResponseBody
    public Pet patch(@PathVariable("id") String id, @Valid LazyBody<Pet> pet) throws JsonMappingException, MethodArgumentNotValidException {
        Pet orig = getById(id);
        Objects.requireNonNull(orig, "Patching invalid pet");
        return put(id, pet.merge(orig));
    }

    @DeleteMapping(path = "/{id}")
    @ResponseBody
    public Pet delete(@PathVariable("id") String id) {
        return pets.remove(id);
    }

    @PutMapping(path = "/{id}")
    @ResponseBody
    public Pet put(@PathVariable("id") String id, @Valid @RequestBody Pet pet) {
        pet.setId(id);
        pets.put(id, pet);
        return pet;
    }
}
