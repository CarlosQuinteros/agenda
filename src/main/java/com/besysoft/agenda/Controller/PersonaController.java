package com.besysoft.agenda.Controller;

import com.besysoft.agenda.Dto.Mensaje;
import com.besysoft.agenda.Dto.PersonaDto;
import com.besysoft.agenda.Entity.Persona;
import com.besysoft.agenda.Exception.DatosInvalidosException;
import com.besysoft.agenda.IService.IPersonaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.xml.transform.OutputKeys;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/agenda/api/personas")
public class PersonaController {

    @Autowired
    private IPersonaService personaService;

    @PostMapping()
    public ResponseEntity<?> crearPersona(@Valid @RequestBody PersonaDto personaDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new DatosInvalidosException(bindingResult);
        }
        Persona nuevaPersona = new Persona(personaDto.getNombre(),
                personaDto.getApellido(),
                personaDto.getTelefono(),
                personaDto.getCiudad(),
                personaDto.getEmail()
        );
        nuevaPersona = personaService.crearPersona(nuevaPersona);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaPersona.getId())
                .toUri();
        return ResponseEntity.created(location).body(new Mensaje("Persona creada correctamente"));
    }

    @GetMapping()
    public ResponseEntity<List<Persona>> listadoPersonas() {
        List<Persona> personas = personaService.listadoPersonas();
        return new ResponseEntity<>(personas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> obtenerPersonaPorId(@PathVariable Long id) {
        Persona persona = personaService.obtenerPersona(id);
        return ResponseEntity.ok(persona);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarPersona(@PathVariable Long id, @Valid @RequestBody PersonaDto personaDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new DatosInvalidosException(bindingResult);
        }
        Persona personaEditar = new Persona(personaDto.getNombre(),
                personaDto.getApellido(),
                personaDto.getTelefono(),
                personaDto.getCiudad(),
                personaDto.getEmail());
        personaService.editarPersona(id, personaEditar);
        return new ResponseEntity<>(new Mensaje("Persona editada correctamente"), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPersona(@PathVariable Long id) {
        Persona persona = personaService.obtenerPersona(id);
        personaService.eliminarPersona(persona.getId());
        return new ResponseEntity<>(new Mensaje("Persona eliminada correctamente"), HttpStatus.OK);
    }

}
